package fr.idarkay.minetasia.core.spigot.listener;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.idarkay.minetasia.core.api.Command;
import fr.idarkay.minetasia.core.api.GeneralPermission;
import fr.idarkay.minetasia.core.api.PlayerStatue;
import fr.idarkay.minetasia.core.api.ServerPhase;
import fr.idarkay.minetasia.core.api.event.PlayerPermissionLoadEndEvent;
import fr.idarkay.minetasia.core.api.utils.Boost;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.user.CorePlayer;
import fr.idarkay.minetasia.core.spigot.user.MinePlayer;
import fr.idarkay.minetasia.core.spigot.utils.InventorySyncTools;
import fr.idarkay.minetasia.core.spigot.utils.InventorySyncType;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * File <b>PlayerListener</b> located on fr.idarkay.minetasia.core.common.listener
 * PlayerListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 20/11/2019 at 14:12
 */
public class PlayerListener implements Listener {

    private final MinetasiaCore plugin;

    public PlayerListener(MinetasiaCore plugin)
    {
        this.plugin = plugin;
    }



    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinEvent(PlayerJoinEvent e)
    {
        e.setJoinMessage(null);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final MinePlayer minePlayer = plugin.getPlayerManager().load(e.getPlayer().getUniqueId());
            Tuple<InventorySyncType, String> syncInv = InventorySyncTools.pendingSync.remove(e.getPlayer().getUniqueId());
            if(syncInv != null)
            {
                String data = null;
                try
                {
                    if ( syncInv.a() == InventorySyncType.BDD )
                    {
                        String invData = minePlayer.getData(syncInv.b(), String.class);
                        if(invData != null)
                        {
                            data = invData;
                        }
                    }
                    else if (syncInv.a() == InventorySyncType.DAT)
                    {
                        data = syncInv.b();
                    }
                    if (data != null)
                    {
                        InventorySyncTools.applied(data, e.getPlayer());
                    }
                }
                catch (CommandSyntaxException exception)
                {
                    exception.printStackTrace();
                }

            }
            plugin.getPermissionManager().loadUser(e.getPlayer().getUniqueId(), false);
            Bukkit.getPluginManager().callEvent(new fr.idarkay.minetasia.core.api.event.PlayerJoinEvent(minePlayer, e.getPlayer()));
            long i = plugin.getPlayer(e.getPlayer().getUniqueId()).getStatus();
            plugin.getPlayerListManager().updatePlayer(e.getPlayer());

            if(plugin.isBollTrue(i, PlayerStatue.SOCIAL_SPY.by)) plugin.socialSpyPlayer.add(e.getPlayer());


            if(plugin.isCommandEnable(Command.PARTY_XP_BOOST))
            {
                Boost boost = plugin.getPlayerPartyBoost(e.getPlayer().getUniqueId());
                if(boost.getBoost().size() > 0)
                {
                    plugin.getPartyServerBoost().upgrade(boost);
                    boost.getBoost().forEach((k, v) -> {
                        if(v > 0)
                        {
                            final float a = plugin.getPartyServerBoost().getBoost(k);
                            final float m = MinetasiaCore.limit.get(k);
                            Bukkit.getOnlinePlayers().forEach(p -> {
                                String lang = plugin.getPlayerLang(p.getUniqueId());
                                p.sendMessage(Lang.PLAYER_BOOST.get(lang, Lang.Argument.PLAYER.match(e.getPlayer().getName()), Lang.Argument.MONEY_TYPE.match(k.name())
                                        , Lang.Argument.ACTUAL_BOOST.match(a),  Lang.Argument.MAX_BOOST.match(m)));
                            });
                        }
                    });
                }
            }
        });
        plugin.getOldCombatsManger().onPlayerJoinEvent(e.getPlayer(), e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuitEvent(PlayerQuitEvent e)
    {
        String bddLinkType = InventorySyncTools.getBddLinkType(plugin.getThisServer().getType());
        final String inv = bddLinkType == null ? null : InventorySyncTools.getRawPlayerData(e.getPlayer());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final CorePlayer corePlayer = plugin.getPlayerManager().getCorePlayer(e.getPlayer().getUniqueId());
            final MinePlayer minePlayer = plugin.getPlayerManager().get(e.getPlayer().getUniqueId());
            if(corePlayer.getPlayTime() > 30_000)
            {

                if(minePlayer != null)
                    minePlayer.updatePlayerStats(() -> Collections.singletonMap(plugin.getServerType() + ".play_time", TimeUnit.MILLISECONDS.toSeconds(corePlayer.getPlayTime())));
                corePlayer.resetJoinTime();
            }
            if(bddLinkType != null && inv != null && minePlayer != null)
            {
                ;minePlayer.putData(bddLinkType, inv);
            }

            plugin.getPlayerManager().removePlayer(e.getPlayer().getUniqueId());
            plugin.getPartyManager().disconnectPlayer(e.getPlayer().getUniqueId());
            plugin.getPermissionManager().removePlayer(e.getPlayer().getUniqueId());
            plugin.socialSpyPlayer.remove(e.getPlayer());
        });
        e.setQuitMessage(null);
        plugin.getOldCombatsManger().onPlayerQuitEvent(e.getPlayer(), e);
    }

    @EventHandler
    public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent e)
    {
        plugin.getOldCombatsManger().onWorldChange(e.getPlayer(), e);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPermissionLoadEndEvent(PlayerPermissionLoadEndEvent e)
    {
        if(plugin.getServerPhase() == ServerPhase.GAME && !plugin.isHub())
        {
            if(Bukkit.getOnlinePlayers().size() > plugin.getMaxPlayerCount())
            {
                if(!e.getPlayer().hasPermission(GeneralPermission.ADMIN_SPECTATOR.getPermission()))
                {
                    plugin.movePlayerToHub(e.getPlayer());
                }
            }
        }
    }
}
