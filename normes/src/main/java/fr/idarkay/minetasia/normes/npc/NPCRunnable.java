package fr.idarkay.minetasia.normes.npc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * File <b>NPCRunnable</b> located on fr.idarkay.minetasia.normes.npc
 * NPCRunnable is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 26/03/2020 at 13:05
 */

/**
 * this runnable mange the spawn and remove of all load npc to all player ;
 * can be run async
 */
public class NPCRunnable extends BukkitRunnable
{
    private final static int viewRayon = 64; //in block

    @Override
    public void run()
    {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            for (MinetasiaNpc npc : MinetasiaNpc.loadNpc)
            {
                final UUID uuid =  onlinePlayer.getUniqueId();
                if(!onlinePlayer.getLocation().getWorld().equals(npc.getCurrentLocation().getWorld()) && npc.loadPlayer.contains(uuid))
                {
                    npc.unShowToPlayer(onlinePlayer, uuid);
                }
                final double distance = onlinePlayer.getLocation().distance(npc.getCurrentLocation());
                if(npc.loadPlayer.contains(uuid))
                {
                    if(distance > viewRayon)
                    {
                        npc.unShowToPlayer(onlinePlayer, uuid);
                    }
                }
                else
                {
                    if(distance <= viewRayon)
                    {
                        npc.showToPlayer(onlinePlayer, uuid);
                    }
                }
            }
        }
    }
}
