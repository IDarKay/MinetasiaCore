package fr.idarkay.minetasia.core.spigot.listener.inventory;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.gui.GUI;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.GUIFlags;
import fr.idarkay.minetasia.normes.MinetasiaGUI;
import fr.idarkay.minetasia.normes.MinetasiaGuiHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.UUID;

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

    public static final ArrayList<Class<? extends MinetasiaGUI>> blackListClazz = new ArrayList<>();

    private final MinetasiaCore plugin;

    public InventoryClickListener(MinetasiaCore plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClickEvent(InventoryClickEvent e)
    {
        if(e.getInventory().getHolder() instanceof MinetasiaGuiHolder)
        {
            final MinetasiaGuiHolder holder = (MinetasiaGuiHolder) e.getInventory().getHolder();
            for(GUIFlags flags : holder.getFlags())
            {
                if(flags == GUIFlags.CANCEL_CLICK)
                {
                    e.setCancelled(true);
                    break;
                }
            }
            if(e.getSlot() > -1)
            {
                final Player player = (Player) e.getWhoClicked();
                final MinetasiaGUI gui = holder.getMinetasiaGUI();
                if(gui != null && !blackListClazz.contains(gui.getClass()))
                    gui.click(player, e);
            }
        }

        //todo: chnage for new gui system
        Inventory inventory = e.getInventory();
        if(inventory.getHolder() instanceof MinetasiaGuiHolder)
        {
            UUID id = ((MinetasiaGuiHolder) inventory.getHolder()).getUuid();
            if(id.equals(GUI.uuid))
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
