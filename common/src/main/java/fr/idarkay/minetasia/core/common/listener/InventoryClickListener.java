package fr.idarkay.minetasia.core.common.listener;

import fr.idarkay.minetasia.core.common.MinetasiaCore;
import fr.idarkay.minetasia.core.common.gui.GUI;
import fr.idarkay.minetasia.core.common.utils.Lang;
import fr.idarkay.minetasia.normes.MinetasiaGuiHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

/**
 * File <b>InventoryClickListener</b> located on fr.idarkay.minetasia.core.common.listener
 * InventoryClickListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 25/11/2019 at 20:24
 * @since 1.0
 */
public final class InventoryClickListener implements Listener {

    private final MinetasiaCore plugin;

    public InventoryClickListener(MinetasiaCore plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClickEvent(InventoryClickEvent e)
    {
        Inventory inventory = e.getInventory();
        if(inventory.getHolder() instanceof MinetasiaGuiHolder)
        {
            String id = ((MinetasiaGuiHolder) inventory.getHolder()).getId();
            if(id.equals("lang"))
            {
                e.setCancelled(true);
                Player p = (Player) e.getWhoClicked();
                p.updateInventory();

                String lang = GUI.SLOT_LANG.get(e.getSlot());

                if(lang != null)
                {
                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->{
                        plugin.setPlayerData(p.getUniqueId(), "lang", lang);
                        p.sendMessage(Lang.SET_LANG.get(lang));
                    });
                    p.closeInventory();
                }
            }
        }
    }
}
