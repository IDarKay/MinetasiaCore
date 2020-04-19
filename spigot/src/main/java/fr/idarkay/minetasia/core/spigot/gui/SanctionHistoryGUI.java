package fr.idarkay.minetasia.core.spigot.gui;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.moderation.PlayerSanction;
import fr.idarkay.minetasia.core.spigot.moderation.SanctionType;
import fr.idarkay.minetasia.core.spigot.user.MinePlayer;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.GUIFlags;
import fr.idarkay.minetasia.normes.InventoryFileType;
import fr.idarkay.minetasia.normes.MinetasiaGUI;
import fr.idarkay.minetasia.normes.MinetasiaGuiHolder;
import fr.idarkay.minetasia.normes.sign.MinetasiaSignGui;
import fr.idarkay.minetasia.normes.utils.BukkitUtils;
import fr.idarkay.minetasia.normes.utils.HeadConstant;
import org.apache.commons.lang.Validate;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * File <b>SanctionHistoryGUi</b> located on fr.idarkay.minetasia.core.spigot.gui
 * SanctionHistoryGUi is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 10/04/2020 at 00:19
 */
public class SanctionHistoryGUI extends MinetasiaGUI<MinetasiaCore>
{
    private static final int MAX_ITEM_PER_PAGES = 45;

    final Comparator<Document> sanctionTimeComparator= (o1, o2) ->
    {
        final Long start1 = o1.getLong("start");
        final Long start2 = o2.getLong("start");
        final long result = (start2 == null ? 0 : start2) - (start1 == null ? 0 : start1);
        //safe past to int result
        return (result < 0) ? -1 : ((result > 0) ? 1 : 0);
    };

    /**
     * @param plugin          plugin class
     */
    public SanctionHistoryGUI(MinetasiaCore plugin)
    {
        super(plugin, false, GUIFlags.CANCEL_DRAG, GUIFlags.CANCEL_CLICK);
    }

    @Override
    public void open(Player player)
    {
        throw new IllegalArgumentException("can't use this function !");
    }

    public void open(@NotNull Player player, @NotNull MinePlayer target)
    {
        open(player, target, null,null,0);
    }

    public void open(@NotNull Player player, @NotNull MinePlayer target, @Nullable SanctionType filter, @Nullable String reasonFilter, int page)
    {
        Validate.notNull(player);
        Validate.notNull(target);
        if(page < 0)
        {
            open(player, target, filter, reasonFilter, 0);
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            final List<Document> documents = target.getHistory();


            final Inventory inventory = createGUI(6, "History " + target.getName(), InventoryFileType.LINE_TOP, createItemStack(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            final String lang = plugin.getPlayerLang(player.getUniqueId());


            final List<Document> historyFilter =  documents.stream()
                    //apply filter
                    .filter(sanction -> (filter == null || filter.name().equals(sanction.getString("type"))) && (reasonFilter == null || reasonFilter.equalsIgnoreCase(sanction.getString("generic_name"))))
                    //sort par pages
                    .sorted(sanctionTimeComparator).collect(Collectors.toList());

            final int maxPage = (int) Math.ceil(historyFilter.size() / (double) MAX_ITEM_PER_PAGES);

            if(historyFilter.size() == 0 && page != 0)
            {
                open(player, target, filter, reasonFilter, 0);
                return;
            }

            final List<PlayerSanction> historyFilterPages = historyFilter.stream()
                    //cut for pagination
                    .skip(Math.min(page * MAX_ITEM_PER_PAGES, documents.size()))
                    .limit(MAX_ITEM_PER_PAGES)
                    //to playSanction object
                    .map(PlayerSanction::fromDocument)
                    .collect(Collectors.toList());

            //back head
            if(page != 0)
            {
                inventory.setItem(0, createHead(1, Lang.ITEM_PREVIOUS_PAGE.getWithoutPrefix(lang), HeadConstant.LEFT_ARROW));
            }

            //new head
            if(page < maxPage - 1)
            {
                inventory.setItem(8, createHead(1, Lang.ITEM_NEXT_PAGE.getWithoutPrefix(lang), HeadConstant.RIGHT_ARROW));
            }

            //player stats & info
            inventory.setItem(4, createHead(1, ChatColor.GREEN + target.getName(), getSafeHeadTexture(target),
                    ChatColor.DARK_GRAY + "" + ChatColor.BOLD  + "[STATUS]",
                        ChatColor.WHITE + "" + ChatColor.BOLD  + "Ban: " +  getSanctionStatus(target, SanctionType.BAN),
                        ChatColor.WHITE + "" + ChatColor.BOLD  + "Mute: " + getSanctionStatus(target, SanctionType.MUTE),
                        ChatColor.WHITE + "" + ChatColor.BOLD  + "Warn: " + getSanctionStatus(target, SanctionType.WARN),
                        " ",
                        ChatColor.DARK_GRAY + "" + ChatColor.BOLD  + "[TOTAL]",
                        ChatColor.WHITE + "" + ChatColor.BOLD  + "Ban: " + ChatColor.LIGHT_PURPLE + documents.stream().filter(sanction -> sanction.getString("type").equals("BAN")).count(),
                        ChatColor.WHITE + "" + ChatColor.BOLD  + "Mute: " + ChatColor.LIGHT_PURPLE + documents.stream().filter(sanction -> sanction.getString("type").equals("MUTE")).count(),
                        ChatColor.WHITE + "" + ChatColor.BOLD  + "Warn: " + ChatColor.LIGHT_PURPLE + documents.stream().filter(sanction -> sanction.getString("type").equals("WARN")).count()
                    ));
            //filter
            inventory.setItem(2, createItemStack(Material.HOPPER, 1, Lang.FILTER_NAME.getWithoutPrefix(lang), Lang.FILTER_LORE.getWithSplit(lang)));

            //filter sign
            inventory.setItem(6, createItemStack(Material.OAK_SIGN, 1, Lang.SIGN_FILTER_NAME.getWithoutPrefix(lang), Lang.SIGN_FILTER_LOR.getWithSplit(lang)));


            final String headName = Lang.SANCTION_HEAD_NAME.getWithoutPrefix(lang);
            final String headLore = Lang.SANCTION_HEAD_LORE.getWithoutPrefix(lang);

            for (PlayerSanction playerSanction : historyFilterPages)
            {
                inventory.addItem(createHead(1, headName,
                        //get head
                        ((playerSanction.getType() == SanctionType.BAN) ? HeadConstant.BAN : ((playerSanction.getType() == SanctionType.MUTE) ? HeadConstant.MUTE : HeadConstant.WARN)),
                        headLore.replace("{player}", playerSanction.authorName)
                                .replace("{generic_reason}", playerSanction.genric_raison_name)
                                .replace("{reason}", playerSanction.reason)
                                .replace("{sanction_type}", playerSanction.getType().name())
                                .replace("{date}", new Date(playerSanction.startTime).toString())
                                .replace("{time}", playerSanction.baseTimeUnit.convert(playerSanction.during, TimeUnit.MILLISECONDS) + " " + playerSanction.baseTimeUnit.name().toLowerCase() )
                                .replace("{status}", playerSanction.isEnd() ? ChatColor.GREEN + "END" : ChatColor.RED + "ACTIVE")
                                .split(Lang.newlineRegex)

                        ));
            }

            final Map<String, Object> dataMap = ((MinetasiaGuiHolder) inventory.getHolder()).getDataMap();
            dataMap.put("target", target);
            dataMap.put("filter", filter);
            dataMap.put("reasonFilter", reasonFilter);
            dataMap.put("page", page);

            Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(inventory));
        });
    }

    private String getSafeHeadTexture(@NotNull MinePlayer minePlayer)
    {
        final String texture = minePlayer.getData("head_texture", String.class);
        return texture == null ? HeadConstant.UNKNOWN : texture;
    }

    private String getSanctionStatus(@NotNull MinePlayer minePlayer, @NotNull SanctionType sanctionType)
    {
        final PlayerSanction sanction = minePlayer.getSanction(sanctionType);
        return sanction == null || sanction.isEnd() ?  ChatColor.RED + "Inactive" : ChatColor.GREEN + "Active";
    }

    @Override
    public void click(Player player, InventoryClickEvent event, MinetasiaGuiHolder holder)
    {
        //top inventory and not null click
        if (BukkitUtils.isTopGui(event) && event.getCurrentItem() != null)
        {
            if (event.getSlot() == 0 && event.getCurrentItem().getType().equals(Material.PLAYER_HEAD))
            {
                final Map<String, Object> data = holder.getDataMap();
                final int page = (int) data.get("page");
                //double check
                if(page > 0)
                    open(player, (MinePlayer) data.get("target"), (SanctionType) data.get("filter"), (String) data.get("reasonFilter"), page -1);
            }
            else if (event.getSlot() == 8 && event.getCurrentItem().getType().equals(Material.PLAYER_HEAD))
            {
                final Map<String, Object> data = holder.getDataMap();
                final int page = (int) data.get("page");

                open(player, (MinePlayer) data.get("target"), (SanctionType) data.get("filter"), (String) data.get("reasonFilter"), page + 1);
            }
            //simple filter
            else if (event.getSlot() == 2)
            {
                final Map<String, Object> data = holder.getDataMap();
                final int page = (int) data.get("page");
                final String filter = (String) data.get("reasonFilter");
                final MinePlayer target = (MinePlayer) data.get("target");
                switch (event.getClick())
                {
                    case LEFT:
                    case SHIFT_LEFT:
                        open(player, target, SanctionType.BAN, filter, page);
                        break;
                    case MIDDLE:
                        open(player, target, SanctionType.MUTE, filter, page);
                        break;
                    case RIGHT:
                    case SHIFT_RIGHT:
                        open(player, target, SanctionType.WARN, filter, page);
                        break;
                    case DROP:
                    case CONTROL_DROP:
                        open(player, target, null, filter, page);
                        break;
                }
            }
            //sign filter
            else if (event.getSlot() == 6)
            {
                final Map<String, Object> data = holder.getDataMap();
                final int page = (int) data.get("page");
                final SanctionType filter = (SanctionType) data.get("filter");
                final MinePlayer target = (MinePlayer) data.get("target");

                if(event.getClick().equals(ClickType.RIGHT) || event.getClick().equals(ClickType.SHIFT_RIGHT))
                {
                    open(player, target, filter, null, page);
                }
                else
                {
                    new MinetasiaSignGui((player1, strings) -> {
                        open(player1, target, filter, strings[0].replace(">", ""), page);
                    }).setLines(Lang.SANCTION_SIGN_FILTER_CONTENT.getWithSplit(plugin.getPlayerLang(player.getUniqueId()))).open(player);
                }
            }
        }
    }
}
