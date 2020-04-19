package fr.idarkay.minetasia.core.spigot.listener.inventory;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.normes.GUIFlags;
import fr.idarkay.minetasia.normes.MinetasiaGUI;
import fr.idarkay.minetasia.normes.MinetasiaGuiHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * File <b>InventoryClickListener</b> located on fr.idarkay.minetasia.core.common.listener
 * InventoryClickListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 25/11/2019 at 20:24
 * @since 1.0
 */
public final class InventoryClickListener implements Listener {

//    public static final ArrayList<Class<? extends MinetasiaGUI>> blackListClazz = new ArrayList<>();

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
                if(gui != null)
                    gui.click(player, e, holder);
            }
        }
    }
}
