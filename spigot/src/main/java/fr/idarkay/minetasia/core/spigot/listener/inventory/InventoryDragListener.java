package fr.idarkay.minetasia.core.spigot.listener.inventory;

import fr.idarkay.minetasia.normes.GUIFlags;
import fr.idarkay.minetasia.normes.MinetasiaGUI;
import fr.idarkay.minetasia.normes.MinetasiaGuiHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

import java.util.ArrayList;

/**
 * File <b>InventoryDragListener</b> located on fr.idarkay.minetasia.core.spigot.listener.inventory
 * InventoryDragListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 28/01/2020 at 18:48
 */
public class InventoryDragListener implements Listener
{

    public static final ArrayList<Class<? extends MinetasiaGUI>> blackListClazz = new ArrayList<>();

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryDragEvent(InventoryDragEvent e)
    {
        if(e.getInventory().getHolder() instanceof MinetasiaGuiHolder)
        {
            final MinetasiaGuiHolder holder = (MinetasiaGuiHolder) e.getInventory().getHolder();
            for(GUIFlags flags : holder.getFlags())
            {
                if(flags == GUIFlags.CANCEL_DRAG)
                {
                    e.setCancelled(true);
                    break;
                }
            }
            final Player player = (Player) e.getWhoClicked();
            final MinetasiaGUI gui = holder.getMinetasiaGUI();
            if(gui != null  && !blackListClazz.contains(gui.getClass()))
                gui.drag(player, e);
        }
    }

}
