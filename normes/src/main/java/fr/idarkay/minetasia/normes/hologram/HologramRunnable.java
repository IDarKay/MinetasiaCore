package fr.idarkay.minetasia.normes.hologram;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * File <b>HologramRunnable</b> located on fr.idarkay.minetasia.normes.hologram
 * HologramRunnable is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 06/04/2020 at 01:32
 */
public class HologramRunnable extends BukkitRunnable
{

    private final static int viewRayon = 64; //in block

    @Override
    public void run()
    {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            for (Hologram hologram : Hologram.loadHolograms)
            {
                final UUID uuid =  onlinePlayer.getUniqueId();
                if(!onlinePlayer.getLocation().getWorld().equals(hologram.getCurrentLocation().getWorld()) && hologram.loadPlayer.contains(uuid))
                {
                    hologram.unShowToPlayer(onlinePlayer);
                }
                final double distance = onlinePlayer.getLocation().distance(hologram.getCurrentLocation());
                if(hologram.loadPlayer.contains(uuid))
                {
                    if(distance > viewRayon)
                    {
                        hologram.unShowToPlayer(onlinePlayer);
                    }
                }
                else
                {
                    if(distance <= viewRayon)
                    {
                        hologram.showToPlayer(onlinePlayer);
                    }
                }
            }
        }
    }
}
