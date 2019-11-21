package fr.idarkay.minetasia.core.common.listener;

import fr.idarkay.minetasia.core.common.MinetasiaCore;
import fr.idarkay.minetasia.core.common.user.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e)
    {
        //todo: set join message
//        e.setJoinMessage(null);
        UUID uuid = e.getPlayer().getUniqueId();
        Player player = plugin.getPlayerManagement().get(uuid);
        if(player != null)
        {
            String name;
            if (!player.getName().equals(name = e.getPlayer().getName()))
            {
                //todo: change pseudo msg
                plugin.getSqlManager().update("UPDATE `uuid_username` SET `username` = ? WHERE uuid = ?", name, uuid.toString());
                plugin.setUserName(uuid, name);
            }
        } else
        {
            //todo: set bvn msg
            plugin.getPlayerManagement().newPlayer(uuid, e.getPlayer().getName());
        }


    }

}
