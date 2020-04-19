package fr.idarkay.minetasia.core.spigot.runnable;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * File <b>IpRunnable</b> located on fr.idarkay.minetasia.core.spigot.runnable
 * IpRunnable is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 02/04/2020 at 18:57
 */
public class IpRunnable extends BukkitRunnable
{
    private final MinetasiaCore core;
    private final String ip;
    private final int ipLength;
    private final StringBuilder msg = new StringBuilder();
    private final List<Consumer<String>> ipConsumers = new ArrayList<>();

    public IpRunnable(MinetasiaCore core)
    {
        this.core = core;
        this.ip = core.getIp();
        ipLength = ip.length();
    }

    private volatile String fullIp = "";
    private int c = 0;
    private IPPhase phase = IPPhase.WRITE;

    public void addConsumer(Consumer<String> consumer)
    {
        ipConsumers.add(consumer);
    }

    @Override
    public void run()
    {
        if(phase == IPPhase.WRITE)
        {
            if(ipLength > c)
            {
                msg.append(ip.charAt(c));
                fullIp = ChatColor.GOLD + msg.toString() + "_";
                c++;
            }
            else
            {
                c = 0;
                phase = IPPhase.LIGHT;
            }
        }
        else if(phase == IPPhase.LIGHT)
        {
            if(c < 8)
            {
                fullIp = (c%2 == 0 ? ChatColor.RED : ChatColor.GOLD) + msg.toString();
                c++;
            }
            else
            {
                c = 0;
                phase = IPPhase.DELETE;
            }
        }
        else if (phase == IPPhase.DELETE)
        {
            final int l = msg.length();
            if(l > 0)
            {
                msg.deleteCharAt(l -1);
                fullIp = ChatColor.GOLD  + msg.toString() + "_";
            }
            else
            {
                phase = IPPhase.WRITE;
            }
        }
        ipConsumers.forEach(c -> c.accept(fullIp));
    }

    private enum IPPhase
    {
        WRITE,
        LIGHT,
        DELETE
    }

}
