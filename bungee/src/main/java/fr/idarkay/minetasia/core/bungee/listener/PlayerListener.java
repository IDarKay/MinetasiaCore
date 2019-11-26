package fr.idarkay.minetasia.core.bungee.listener;

import fr.idarkay.minetasia.core.bungee.MinetasiaCoreBungee;
import fr.idarkay.minetasia.core.bungee.exception.FRSDownException;
import fr.idarkay.minetasia.core.bungee.utils.user.Player;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.UUID;

/**
 * File <b>PlayerListener</b> located on fr.idarkay.minetasia.core.bungee.listener
 * PlayerListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 26/11/2019 at 21:35
 */
public final class PlayerListener implements Listener {

    private MinetasiaCoreBungee plugin;

    public PlayerListener(MinetasiaCoreBungee plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLoginEvent(LoginEvent e)
    {
        plugin.getProxy().getScheduler().runAsync(plugin, () -> {
            try{
                ProxiedPlayer proxiedPlayer = plugin.getProxy().getPlayer(e.getConnection().getName());
                UUID uuid = proxiedPlayer.getUniqueId();

                Player player = plugin.getPlayerManager().get(uuid);
                if(player != null)
                {
                    String name;
                    if (!player.getName().equals(name = proxiedPlayer.getName()))
                    {

                        plugin.getSqlManager().updateAsynchronously("UPDATE `uuid_username` SET `username` = ? WHERE uuid = ?", name, uuid.toString());
                        plugin.setUserName(uuid, name);
                        plugin.getFrsClient().publish("core-data", "username;" + uuid.toString()  + ";" + name);
                    }
                } else
                {
                    plugin.getPlayerManager().newPlayer(uuid, proxiedPlayer.getName());
                    plugin.getFrsClient().publish("core-msg",  "NEW_FRIEND;" + uuid.toString() +";" + proxiedPlayer.getName());
                }
                String proxyName = plugin.getProxyManager().getProxy().getUuid().toString();
                plugin.getSqlManager().updateAsynchronously("INSERT INTO `online_player`(uuid, username, proxy) VALUES(?,?,?) ON DUPLICATE KEY UPDATE username = ?, proxy = ?",
                        uuid.toString(), proxiedPlayer.getName(), proxyName, proxiedPlayer.getName(), proxyName);
            } catch (FRSDownException ignore){
                plugin.getLogger().warning("FRS DISCONNECT CAN4 LOAD PLAYER ! THE PLAYER WAS KICK !");
                e.setCancelled(true);
                e.setCancelReason(TextComponent.fromLegacyText("Fatal error can't load your profile retry later"));
                e.completeIntent(plugin);
            }
        });
    }

    public void onPlayerDisconnectEvent(PlayerDisconnectEvent e)
    {
        plugin.getProxy().getScheduler().runAsync(plugin, () -> {
            ProxiedPlayer proxiedPlayer = e.getPlayer();
            plugin.getSqlManager().updateAsynchronously("DELETE FROM `online_player` WHERE uuid = ?", proxiedPlayer.getUniqueId());
        });

    }

}
