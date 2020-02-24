package fr.idarkay.minetasia.core.spigot.gui;

import fr.idarkay.minetasia.core.api.utils.Party;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.messages.CoreMessage;
import fr.idarkay.minetasia.core.spigot.messages.PartyMessage;
import fr.idarkay.minetasia.core.spigot.user.PlayerParty;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.GUIFlags;
import fr.idarkay.minetasia.normes.InventoryFileType;
import fr.idarkay.minetasia.normes.MinetasiaGUI;
import fr.idarkay.minetasia.normes.Reflection;
import fr.idarkay.minetasia.normes.Utils.BukkitUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * File <b>PartyGui</b> located on fr.idarkay.minetasia.core.spigot.gui
 * PartyGui is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 24/02/2020 at 13:03
 */
public class PartyGui extends MinetasiaGUI<MinetasiaCore>
{

    public static final String UNKNOWN_HEAD_SKIN = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGY1YjZmOWRiOGIyMzdiNWVjNjdiNThhMWVmYmY4YTFhNWRjZjgxM2QzZDg2OWIyMzI4NDdmMjFjMjk4OTQ4In19fQ==";
    private final ItemStack PAN = createItemStack(Material.GREEN_STAINED_GLASS_PANE, 1, " ");

    /**
     * @param plugin          plugin class
     */
    public PartyGui(MinetasiaCore plugin)
    {
        super(plugin, false, GUIFlags.CANCEL_DRAG, GUIFlags.CANCEL_CLICK);
    }

    @Override
    public void open(@NotNull Player player)
    {
        Validate.notNull(player);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
        {
            final String lang = plugin.getPlayerLang(player.getUniqueId());

            final Inventory inventory = createGUI(6, Lang.PARTY_GUI_NAME.getWithoutPrefix(lang), InventoryFileType.LINES, PAN);

            final PlayerParty playerParty = plugin.getPartyManager().getByPlayer(player.getUniqueId());

            if(playerParty == null || playerParty.getOwner().equals(player.getUniqueId()))
            {
                inventory.setItem(4, createItemStack(Material.PAPER, 1, Lang.PARTY_GUI_INVITE_NAME.getWithoutPrefix(lang), Lang.PARTY_GUI_INVITE_LORE.getWithSplit(lang)));

                inventory.setItem(49, createItemStack(Material.SPRUCE_DOOR, 1, Lang.PARTY_GUI_DISBAND_NAME.getWithoutPrefix(lang), Lang.PARTY_GUI_DISBAND_LORE.getWithSplit(lang)));
            }
            else
            {
                inventory.setItem(49, createItemStack(Material.SPRUCE_DOOR, 1, Lang.PARTY_GUI_LEAVE_NAME.getWithoutPrefix(lang), Lang.PARTY_GUI_LEAVE_LORE.getWithSplit(lang)));
            }

            if(playerParty == null)
            {
                inventory.setItem(9, Reflection.addNBTTag(createHead(1, ChatColor.GREEN + player.getName(), getTexture(plugin.getPlayerData(player.getUniqueId(), "head_texture", String.class)), Lang.PARTY_GUI_OWNER_LORE.getWithoutPrefix(lang)), "uuid", player.getUniqueId().toString()));
            }
            else
            {
                inventory.setItem(9, createHead(1, ChatColor.GREEN + playerParty.getOwnerName(), getTexture(playerParty.getOwnerTexture()), Lang.PARTY_GUI_OWNER_LORE.getWithoutPrefix(lang)));
                final List<String> membersLore = Arrays.asList((playerParty.getOwner().equals(player.getUniqueId()) ? Lang.PARTY_GUI_OWNER_VIEW_MEMBER_LORE : Lang.PARTY_GUI_MEMBER_VIEW_MEMBER_LORE).getWithSplit(lang));
                playerParty.getMembersTexture().forEach((u, t) -> {
                    if(!u.equals(playerParty.getOwner()))
                        inventory.addItem(Reflection.addNBTTag(createHead(1, ChatColor.GREEN + t.a(), t.b(), membersLore), "uuid", u.toString()));
                });
            }
            Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(inventory));
        });
    }

    @Override
    public void open(Player player, InventoryOpenEvent event)
    {

    }

    @Override
    public void close(Player player, InventoryCloseEvent event)
    {

    }

    @Override
    public void click(Player player, InventoryClickEvent e)
    {
        if(BukkitUtils.isTopGui(e) && e.getCurrentItem() != null && !e.getCurrentItem().getType().isAir() && (e.getSlot() == 4 || e.getSlot() == 49 || (e.getSlot() > 8 && e.getSlot() < 45)))
        {
            if(e.getSlot() == 4 && e.getCurrentItem().getType() != Material.PAPER) return;
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

                final String lang = plugin.getPlayerLang(player.getUniqueId());
                final PlayerParty p = plugin.getPartyManager().getByPlayer(player.getUniqueId());

                // leave & disband => /party leeave
                if(e.getSlot() == 49)
                {
                    if (p != null && p.getPlayers().size() > 1)
                    {
                        if(p.getOwner().equals(player.getUniqueId()))
                        {
                            plugin.getPartyManager().deleteAndUpdate(p.getId());
                            player.sendMessage(Lang.PARTY_DISBAND.get(lang));
                        }
                        else
                        {
                            plugin.getPartyManager().removePlayerAdnUpdate(p.getId(), player.getUniqueId());
                            player.sendMessage(Lang.PARTY_LEAVE.get(lang));
                        }
                    }
                    else player.sendMessage(Lang.PARTY_NOT_IN_PARTY.get(lang));
                    open(player);
                    return;
                }

                //invite => /p invite
                if(e.getSlot() == 4)
                {
                    if(p != null)
                    {
                        if(p.getOwner().equals(player.getUniqueId()))
                        {
                            //todo sign ...
                        }
                        else player.sendMessage(Lang.PARTY_NOT_OWNER.get(lang));
                    }
                    else player.sendMessage(Lang.PARTY_NOT_IN_PARTY.get(lang));
                    return;
                }

                //if no party or player isn't owner cancel (can't kick or promote) or wrong click type
                if(p == null || !p.getOwner().equals(player.getUniqueId()) && (e.getClick() == ClickType.RIGHT || e.getClick() == ClickType.LEFT)) return;

                final String sUuid = Reflection.getNBTTag(e.getCurrentItem(), "uuid");
                if(sUuid != null)
                {
                    final UUID uuid = UUID.fromString(sUuid);
                    if(p.getPlayers().containsKey(uuid))
                    {
                        if(uuid.equals(player.getUniqueId()))
                        {
                            player.sendMessage(Lang.PARTY_YOU_ARE_LEADER.get(lang));
                            return;
                        }

                        if(!p.getPlayers().containsKey(uuid))
                        {
                            player.sendMessage(Lang.PARTY_PLAYER_NOT_IN_PARTY.get(lang));
                            return;
                        }

                        //promote
                        if(e.getClick() == ClickType.LEFT)
                        {
                            try
                            {
                                plugin.publishTargetPlayer(CoreMessage.CHANNEL, PartyMessage.getPartyMakeLeaderFirst(p, uuid), uuid, false, true);
                            }
                            //player is not only
                            catch (NullPointerException ignore)
                            {
                                return;
                            }
                            player.sendMessage(Lang.PARTY_MAKE_LEADER.get(lang, Lang.Argument.PLAYER.match(e.getCurrentItem().getItemMeta().getDisplayName())));
                        }
                        //kick
                        else if(e.getClick() == ClickType.RIGHT)
                        {
                            plugin.getPartyManager().removePlayerAdnUpdate(p.getId(), uuid);
                            player.sendMessage(Lang.PARTY_PLAYER_KICK.get(lang, Lang.Argument.PLAYER.match(e.getCurrentItem().getItemMeta().getDisplayName())));
                        }
                        Bukkit.getScheduler().runTaskLater(plugin, () -> open(player),10);
                    }
                    else player.sendMessage(Lang.PARTY_PLAYER_NOT_IN_PARTY.get(lang));
                }
            });
        }
    }

    @Override
    public void drag(Player player, InventoryDragEvent event)
    {

    }

    @Override
    public void onOtherEvent(Player player, InventoryEvent event)
    {

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
