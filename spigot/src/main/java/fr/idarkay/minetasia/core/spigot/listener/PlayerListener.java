package fr.idarkay.minetasia.core.spigot.listener;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

/**
 * File <b>PlayerListener</b> located on fr.idarkay.minetasia.core.common.listener
 * PlayerListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 20/11/2019 at 14:12
 */
public class PlayerListener implements Listener {

    private final MinetasiaCore plugin;

    public PlayerListener(MinetasiaCore plugin)
    {
        this.plugin = plugin;
    }



    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinEvent(PlayerJoinEvent e)
    {
        e.setJoinMessage(null);
        plugin.getPermissionManager().loadUser(e.getPlayer().getUniqueId(), false);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuitEvent(PlayerQuitEvent e)
    {
        plugin.getPermissionManager().removePlayer(e.getPlayer().getUniqueId());
    }

}
