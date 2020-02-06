package fr.idarkay.minetasia.core.spigot.gui;

import fr.idarkay.minetasia.normes.InventoryFileType;
import fr.idarkay.minetasia.normes.MinetasiaGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

/**
 * File <b>LangGui</b> located on fr.idarkay.minetasia.core.common.gui
 * LangGui is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 25/11/2019 at 19:14
 */
public final class GUI {

    public final static HashMap<Integer, String> SLOT_LANG = new HashMap<>();
    public final static UUID uuid = UUID.randomUUID();

    private final Plugin plugin;

    private Inventory lang;

    public GUI(Plugin plugin)
    {
        this.plugin = plugin;
    }

    public void createLangInventory()
    {
        //lang
        lang = MinetasiaGUI.createGUI(uuid, plugin.getConfig().getInt("gui.lang.size")
                , ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("gui.lang.name"))
                , InventoryFileType.SQUARE, MinetasiaGUI.createItemStack(Material.valueOf(plugin.getConfig().getString("gui.lang.fileMaterial"))
                        , 1, " "));

        for(String sec : plugin.getConfig().getConfigurationSection("gui.lang.lang").getKeys(false))
        {
            int slot = plugin.getConfig().getInt("gui.lang.lang." + sec + ".slot");
            lang.setItem(slot, MinetasiaGUI.createHead(1
                    , ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("gui.lang.lang." + sec + ".name"))
                    , plugin.getConfig().getString("gui.lang.lang." + sec + ".texture")));
            SLOT_LANG.put(slot, plugin.getConfig().getString("gui.lang.lang." + sec + ".iso"));
        }
    }

    public void openLangInventory(Player player)
    {
        player.openInventory(lang);
    }

}
