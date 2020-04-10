package fr.idarkay.minetasia.core.spigot.advancement;

import com.mongodb.MongoWriteException;
import fr.idarkay.minetasia.core.api.MongoCollections;
import fr.idarkay.minetasia.core.api.utils.PlayerStats;
import fr.idarkay.minetasia.core.api.utils.StatsUpdater;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.user.MinePlayer;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.Tuple;
import org.apache.commons.lang.Validate;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * File <b>AdvancementManager</b> located on fr.idarkay.minetasia.core.spigot.advancement
 * AdvancementManager is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 23/03/2020 at 15:02
 */
public class AdvancementManager
{
    private final MinetasiaCore core;
    private final Map<NamespacedKey, MinetasiaAdvancement> advancements = new HashMap<>();
    private final HashMap<String, List<MinetasiaAdvancement>> advancementsStats = new HashMap<>();
    private final List<MinetasiaAdvancement> defaultAdvancements = new ArrayList<>();
    private final String filterRegex;

    public AdvancementManager(MinetasiaCore core)
    {
        this.core = core;
        this.filterRegex = core.getConfig().getString("advancement_load", ".*");
        for (Document document : core.getMongoDbManager().getCollection(MongoCollections.ADVANCEMENT).find())
        {
            final MinetasiaAdvancement a = new MinetasiaAdvancement(document);
            if(a.getNamespacedKey().toString().matches(filterRegex))
            {
                advancements.put(a.namespacedKey, a);
                checkAdnAdToStats(a);
                a.loadAdvancement();
            }
        }
//        if(!advancements.isEmpty() && co)
//            Bukkit.reloadData();
//        registerTest();
    }

    private void checkAdnAdToStats(MinetasiaAdvancement a)
    {
        if(a.getStatsCriteria() != null)
        {
            if(advancementsStats.containsKey(a.getStatsCriteria().a()))
            {
                advancementsStats.get(a.getStatsCriteria().a()).add(a);
            }
            else
            {
                final List<MinetasiaAdvancement> list = new ArrayList<>();
                list.add(a);
                advancementsStats.put(a.getStatsCriteria().a(), list);
            }
        }
        if(a.isDefault())
        {
            defaultAdvancements.add(a);
        }
    }

    public void registerAndSave(MinetasiaAdvancement advancement)
    {
        if(!advancement.toString().matches(filterRegex))
        {
            throw new IllegalArgumentException("try to create advancement with name that dose'nt match filter regex " + advancement.toString() + " " + filterRegex);
        }
        if(!advancements.containsKey(advancement.getNamespacedKey()))
        {
            try {
                core.getMongoDbManager().insert(MongoCollections.ADVANCEMENT, advancement.toDocument());
            } catch (MongoWriteException e)
            {
                e.printStackTrace();
                return;
            }

            checkAdnAdToStats(advancement);
            advancement.loadAdvancement();
//            if(core.isEnabled())
//                Bukkit.reloadData();
        }
    }

    public void validate(@NotNull NamespacedKey namespacedKey, @NotNull MinePlayer minePlayer)
    {
        Validate.notNull(namespacedKey);
        final MinetasiaAdvancement advancement = advancements.get(namespacedKey);
        if(advancement == null) return;
        if(minePlayer.hasCompleteAdvancement(namespacedKey)) return;
        final Player player = Bukkit.getPlayer(minePlayer.getUUID());
        if(player != null)
        {
            Bukkit.getScheduler().runTask(core, () -> {
                validateLocal(namespacedKey, player);
                player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
                player.sendMessage(Lang.CHALLENGE_COMPLETE.get(minePlayer.getLang(), Lang.Argument.CHALLENGE.match(advancement.getLang(minePlayer.getLang()).getTitle())));
            });
        }
        minePlayer.completeAdvancement(namespacedKey);
        if(advancement.getRewards() != null)
        {
            minePlayer.addMoney(advancement.getRewards().a(), advancement.getRewards().b().floatValue());
        }
    }

    public void validateLocal(@NotNull NamespacedKey namespacedKey, @NotNull Player player)
    {
        final Advancement advancement = Bukkit.getAdvancement(new NamespacedKey(namespacedKey.getNamespace() + "_" + core.getPlayerLang(player.getUniqueId()), namespacedKey.getKey()));
        if(advancement != null)
        {
            final AdvancementProgress a = player.getAdvancementProgress(advancement);
            a.getRemainingCriteria().forEach(a::awardCriteria);
        }
        //  else advancement not load in this server

    }

    public void loadUsers(@NotNull MinePlayer minePlayer, @NotNull Player player)
    {
        Bukkit.getScheduler().runTask(core, () -> {
            for (NamespacedKey namespacedKey : minePlayer.getValidateAdvancement())
            {
                if(namespacedKey.toString().matches(filterRegex))
                {
                    validateLocal(namespacedKey, player);
                }
            }
            for (MinetasiaAdvancement a : defaultAdvancements)
            {
                validateLocal(a.namespacedKey, player);
            }
        });
    }

//    private void registerTest()
//    {
//        final MinetasiaAdvancement advancement = new MinetasiaAdvancement(new NamespacedKey("test", "root")
//                , new AdvancementIcon(Material.DIAMOND), AdvancementFrame.TASK, "je suis un advancement de test root", "love you " + ChatColor.RED + "test color", "fr");
//        advancement.withCriteria(Criteria.IMPOSSIBLE);
//        advancement.setBackGround("minecraft:textures/gui/advancements/backgrounds/husbandry.png");
//        registerAndSave(advancement);
//        final MinetasiaAdvancement advancement1 = new MinetasiaAdvancement(new NamespacedKey("test", "child")
//                , new AdvancementIcon(Material.DIAMOND), AdvancementFrame.TASK, "je suis un advancement de test child de root", "love you " + ChatColor.RED + "test color", "fr");
//        advancement1.withCriteria(Criteria.IMPOSSIBLE);
//        advancement1.setParent(new NamespacedKey("test", "root"));
//        registerAndSave(advancement1);
//    }

    public void playerStatsUpdate(@NotNull StatsUpdater updater, @NotNull MinePlayer player)
    {
        updater.getUpdate().forEach( (k, v) ->
                {
                    final List<MinetasiaAdvancement> list = advancementsStats.get(k);
                    if(list != null)
                    {
                        final PlayerStats stats = player.getStats();
                        for (MinetasiaAdvancement advancement : list)
                        {
                            final Tuple<String, Integer> c = advancement.getStatsCriteria();
                            if(c == null) continue;
                            if(stats.getStatsValue(c.a()) >= c.b() && !player.hasCompleteAdvancement(advancement.namespacedKey))
                            {
                                validate(advancement.namespacedKey, player);
                            }
                        }
                    }
                }
        );
    }

}
