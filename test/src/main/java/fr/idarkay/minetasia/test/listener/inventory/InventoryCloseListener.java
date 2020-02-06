package fr.idarkay.minetasia.test.listener.inventory;

import fr.idarkay.minetasia.normes.MinetasiaGUI;
import fr.idarkay.minetasia.normes.MinetasiaGuiHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.ArrayList;

/**
 * File <b>InventoryCloseListener</b> located on fr.idarkay.minetasia.core.spigot.listener.inventory
 * InventoryCloseListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 28/01/2020 at 18:44
 */
public class InventoryCloseListener implements Listener
{

    public static final ArrayList<Class<? extends MinetasiaGUI>> blackListClazz = new ArrayList<>();

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryCloseEvent(InventoryCloseEvent e)
    {
        if(e.getInventory().getHolder() instanceof MinetasiaGuiHolder)
        {
            final MinetasiaGuiHolder holder = (MinetasiaGuiHolder) e.getInventory().getHolder();
            final Player player = (Player) e.getPlayer();
            final MinetasiaGUI gui = holder.getMinetasiaGUI();
            if(gui != null  && !blackListClazz.contains(gui.getClass()))
                gui.close(player, e);

            player.updateInventory();
        }
    }

}
