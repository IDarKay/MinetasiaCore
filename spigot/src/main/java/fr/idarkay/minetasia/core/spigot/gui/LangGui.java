package fr.idarkay.minetasia.core.spigot.gui;

import fr.idarkay.minetasia.core.api.event.PlayerLangChangeEvent;
import fr.idarkay.minetasia.core.api.utils.GuiLang;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.*;
import fr.idarkay.minetasia.normes.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

/**
 * File <b>LangGui</b> located on fr.idarkay.minetasia.core.spigot.gui
 * LangGui is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 24/02/2020 at 11:14
 */
public class LangGui extends MinetasiaGUI<MinetasiaCore>
{
    public final static HashMap<Integer, String> SLOT_LANG = new HashMap<>();
    public final static HashMap<String, GuiLang> LANG_TEXTURE = new HashMap<>();
    private final Inventory gui;

    /**
     * @param plugin          plugin class
     */
    public LangGui(MinetasiaCore plugin)
    {
        super(plugin, false, GUIFlags.CANCEL_DRAG, GUIFlags.CANCEL_CLICK);


        //lang
        gui = createGUI(plugin.getConfig().getInt("gui.lang.size")
                , ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("gui.lang.name"))
                , InventoryFileType.SQUARE, MinetasiaGUI.createItemStack(Material.valueOf(plugin.getConfig().getString("gui.lang.fileMaterial"))
                        , 1, " "));

        for(String sec : plugin.getConfig().getConfigurationSection("gui.lang.lang").getKeys(false))
        {

            GuiLang guiLang = new GuiLang(plugin.getConfig().getString("gui.lang.lang." + sec + ".iso"), plugin.getConfig().getString("gui.lang.lang." + sec + ".name"), plugin.getConfig().getString("gui.lang.lang." + sec + ".lore"), plugin.getConfig().getInt("gui.lang.lang." + sec + ".slot"), plugin.getConfig().getString("gui.lang.lang." + sec + ".texture"));
            LANG_TEXTURE.put(guiLang.getIso(), guiLang);
            gui.setItem(guiLang.getSlot(), MinetasiaGUI.createHead(1, guiLang.getName(), guiLang.getTexture(), guiLang.getLore().split("@@")));
            SLOT_LANG.put(guiLang.getSlot(), guiLang.getIso());

        }


    }

    @Override
    public void open(Player player)
    {
        player.openInventory(gui);
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
        if(BukkitUtils.isTopGui(e) && e.getCurrentItem() != null)
        {
            Player p = (Player) e.getWhoClicked();
            p.updateInventory();

            String lang = SLOT_LANG.get(e.getSlot());

            if(lang != null)
            {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->{
                    plugin.setPlayerData(p.getUniqueId(), "lang", lang);
                    p.sendMessage(Lang.SET_LANG.get(lang));
                    plugin.getServer().getPluginManager().callEvent(new PlayerLangChangeEvent(p, "lang"));
                });
                p.closeInventory();
            }
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
}
