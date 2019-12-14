package fr.idarkay.minetasia.core.spigot.listener;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.utils.PlayerStatue;
import org.bukkit.Bukkit;
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
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->{
            String statue = plugin.getPlayerData(e.getPlayer().getUniqueId(), "statue");

            try {
                int i = Integer.parseInt(statue);

                if(plugin.isBollTrue(i, PlayerStatue.SOCIAL_SPY.by)) plugin.socialSpyPlayer.add(e.getPlayer());

            } catch (Exception ignore) {
                plugin.setPlayerData(e.getPlayer().getUniqueId(), "statue", String.valueOf(0x0));
            }

        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuitEvent(PlayerQuitEvent e)
    {
        plugin.getPermissionManager().removePlayer(e.getPlayer().getUniqueId());
        plugin.socialSpyPlayer.remove(e.getPlayer());
    }

}
