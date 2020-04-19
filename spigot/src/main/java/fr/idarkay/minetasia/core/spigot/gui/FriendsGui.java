package fr.idarkay.minetasia.core.spigot.gui;

import fr.idarkay.minetasia.core.api.utils.Party;
import fr.idarkay.minetasia.core.api.utils.PlayerStatueFix;
import fr.idarkay.minetasia.core.api.utils.Server;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.messages.CoreMessage;
import fr.idarkay.minetasia.core.spigot.messages.PartyMessage;
import fr.idarkay.minetasia.core.spigot.user.CorePlayer;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.*;
import fr.idarkay.minetasia.normes.utils.BukkitUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * File <b>FriendsGui</b> located on fr.idarkay.minetasia.core.spigot.gui
 * FriendsGui is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 15/03/2020 at 00:50
 */
public class FriendsGui extends MinetasiaGUI<MinetasiaCore>
{

    public static final String UNKNOWN_HEAD_SKIN = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGY1YjZmOWRiOGIyMzdiNWVjNjdiNThhMWVmYmY4YTFhNWRjZjgxM2QzZDg2OWIyMzI4NDdmMjFjMjk4OTQ4In19fQ==";
    private final ItemStack PAN = createItemStack(Material.GREEN_STAINED_GLASS_PANE, 1, " ");

    public FriendsGui(MinetasiaCore plugin)
    {
        super(plugin, false, GUIFlags.CANCEL_DRAG, GUIFlags.CANCEL_CLICK);
    }

    @Override
    public void open(Player player)
    {
        open(player, 0);
    }

    private static final int MAX_ITEM_PER_PAGES = 36;

    public void open(Player player, int page)
    {
        Validate.notNull(player);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
        {
            final Map<UUID, Tuple<String, String>> allFriends = plugin.getFriends(player.getUniqueId());
            final int maxPage = (int) Math.ceil(allFriends.size() / (double) MAX_ITEM_PER_PAGES);
            if(maxPage != 0 && maxPage <= page)
            {
                open(player, 0);
                return;
            }
            final String lang = plugin.getPlayerLang(player.getUniqueId());

            final Inventory inventory = createGUI(6, Lang.FRIENDS_GUI_NAME.getWithoutPrefix(lang), InventoryFileType.LINES, PAN);
            ((MinetasiaGuiHolder) Objects.requireNonNull(inventory.getHolder())).setData(page);

            //add back arrow
            if(page != 0)
            {
                inventory.setItem(45, createItemStack(Material.SPECTRAL_ARROW, 1, Lang.ITEM_PREVIOUS_PAGE.getWithoutPrefix(lang)));
            }

            if(page < maxPage - 1)
            {
                inventory.setItem(53, createItemStack(Material.SPECTRAL_ARROW, 1, Lang.ITEM_NEXT_PAGE.getWithoutPrefix(lang)));
            }

            final List<String> membersLore = Arrays.asList(Lang.FRIENDS_GUI_FRIEND_LORE.getWithSplit(lang));
            
            final Map<UUID, Tuple<String, String>> friends = allFriends.entrySet().stream()
                    .skip(Math.min(page * MAX_ITEM_PER_PAGES, allFriends.size()))
                    .limit(MAX_ITEM_PER_PAGES)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            friends.forEach((u, t) -> inventory.addItem(Reflection.addNBTTag(createHead(1, ChatColor.GREEN + t.a(), getTexture(t.b()), membersLore), "uuid", u.toString() + ";" + t.a())));

            Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(inventory));
        });
    }

    @Override
    public void click(Player player, InventoryClickEvent event)
    {
        if(BukkitUtils.isTopGui(event) && event.getCurrentItem() != null)
        {
            final int page = (int) ((MinetasiaGuiHolder) event.getInventory().getHolder()).getData()[0];
            if(event.getCurrentItem().getType() == Material.SPECTRAL_ARROW)
            {
                if(event.getSlot() == 45)
                {
                    open(player, page - 1); // before page
                }
                else if(event.getSlot() == 53)
                {
                    open(player, page + 1); // next page
                }
                return;
            }

            if(event.getSlot() > 8 && event.getSlot() < 45)
            {
                final String[] data = Objects.requireNonNull(Reflection.getNBTTag(event.getCurrentItem(), "uuid")).split(";");
                final UUID targetUUID = UUID.fromString(data[0]); // get clicked player uuid
                final String username = data[1];

                final ClickType click = event.getClick();
                if(click == ClickType.SHIFT_RIGHT || click == ClickType.RIGHT) // remove friends
                {
                    plugin.removeFriend(player.getUniqueId(), targetUUID);
                    plugin.removeFriend(targetUUID, event.getWhoClicked().getUniqueId());
                    open(player, page);
                    player.sendMessage(Lang.REMOVE_FRIEND.get(plugin.getPlayerLang(event.getWhoClicked().getUniqueId()), Lang.Argument.PLAYER.match(username)));
                }
                else if(click == ClickType.SHIFT_LEFT || click == ClickType.LEFT) // add to party
                {
                    final String lang = plugin.getPlayerLang(player.getUniqueId());
                    Party p = plugin.getPlayer(event.getWhoClicked().getUniqueId()).getParty();
                    //check not already in team
                    if(p != null && p.getPlayers().containsKey(targetUUID))
                    {
                        player.sendMessage(Lang.PARTY_ALREADY_IN_PARTY.get(lang));
                        return;
                    }


                    final CorePlayer c = plugin.getPlayerManager().getCorePlayer(player.getUniqueId());
                    if(!c.isEndCountDown(CorePlayer.CountdownType.INVITE_PARTY))
                    {
                        player.sendMessage(Lang.WAIT_BETWEEN_USE.get(lang, Lang.Argument.TIME.match(
                                TimeUnit.MILLISECONDS.toSeconds( c.getCountDown(CorePlayer.CountdownType.INVITE_PARTY)))));
                        return;
                    }
                    if(System.currentTimeMillis() - c.getInvitedPlayerParty().getOrDefault(username, 0L) < CorePlayer.CountdownType.INVITE_SAME_PLAYER_PARTY.getTime())
                    {
                        player.sendMessage(Lang.PARTY_ALREADY_INVITE.get(lang));
                        return;
                    }

                    c.getInvitedPlayerParty().put(username, System.currentTimeMillis());

                    final UUID uuid;
                    final Server server;
                    final Player bukkitP = Bukkit.getPlayer(targetUUID);
                    if(bukkitP != null)
                    {
                        uuid = bukkitP.getUniqueId();
                        server = plugin.getThisServer();
                    }
                    else
                    {
                        final PlayerStatueFix playerStatueFix = plugin.getPlayerStatue(targetUUID);
                        if(playerStatueFix == null)
                        {
                            player.sendMessage(Lang.PLAYER_NOT_EXIST.get(lang));
                            c.startCountDown(CorePlayer.CountdownType.INVITE_PARTY);
                            return;
                        }
                        uuid = playerStatueFix.getUUID();
                        server = playerStatueFix.getServer();
                    }


                    if (p == null)
                    {
                        p = plugin.getPartyManager().createParty(player);
                    }
                    if(p.getOwner().equals(player.getUniqueId()))
                    {
                        if(p.limitSize() > p.getPlayers().size())
                        {
                            final String msg = PartyMessage.getInvitePlayerMessage(p, uuid, player.getName());
                            if(server.getName().equals(plugin.getThisServer().getName()))
                            {
                                PartyMessage.ActionType.INVITE.run(plugin, msg.split(";"));
                                c.getInvitedPlayerParty().put(username, System.currentTimeMillis());
                            }
                            else
                                plugin.publishTarget(CoreMessage.CHANNEL, msg, server, false, true);
                            player.sendMessage(Lang.PARTY_INVITE_SEND.get(lang, Lang.Argument.PLAYER.match(username)));
                        }
                        else player.sendMessage(Lang.PARTY_MAX_SIZE.get(lang));
                    }
                    else player.sendMessage(Lang.PARTY_NOT_OWNER.get(lang));

                }
            }
        }
    }

    /**
     * @param texture texture to check if not null
     * @return texture or {@link PartyGui#UNKNOWN_HEAD_SKIN} if texture null
     */
    private String getTexture(@Nullable String texture)
    {
        return texture == null ? UNKNOWN_HEAD_SKIN : texture;
    }

}
