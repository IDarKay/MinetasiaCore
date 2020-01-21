package fr.idarkay.minetasia.core.bungee.listener;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import fr.idarkay.minetasia.core.bungee.MinetasiaCoreBungee;
import fr.idarkay.minetasia.core.bungee.exception.FRSDownException;
import fr.idarkay.minetasia.core.bungee.utils.user.Player;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.io.File;
import java.io.IOException;
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
    private DatabaseReader reader;

    public PlayerListener(MinetasiaCoreBungee plugin)
    {
        this.plugin = plugin;
        try {
            File countryFile = new File(plugin.getDataFolder(), "data/GeoLite2-Country.mmdb");
            if(!countryFile.exists()) System.out.println("fil not foound");
            reader = new DatabaseReader.Builder(countryFile).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLoginEvent(LoginEvent e)
    {
        e.registerIntent(plugin);
        plugin.getProxy().getScheduler().runAsync(plugin, () -> {
            try{
                PendingConnection proxiedPlayer = e.getConnection();
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
                    String c;
                    try
                    {
                        CountryResponse r = reader.country(proxiedPlayer.getAddress().getAddress());
                        c = r.getCountry().getIsoCode().toLowerCase();
                    } catch (Exception ignore)
                    {
                        c = "fr";
                    }

                    plugin.getPlayerManager().newPlayer(uuid, proxiedPlayer.getName(), c);
                    plugin.getFrsClient().publish("core-msg",  "WELCOME;" + uuid.toString() +";true;PLAYER\\" +proxiedPlayer.getName());

                }

            } catch (FRSDownException ignore)
            {
                plugin.getLogger().warning("FRS DISCONNECT CAN4 LOAD PLAYER ! THE PLAYER WAS KICK !");
                e.setCancelled(true);
                e.setCancelReason(TextComponent.fromLegacyText("Fatal error can't load your profile retry later"));

            } catch (Exception e1)
            {
                e1.printStackTrace();
                e.setCancelled(true);
                e.setCancelReason(TextComponent.fromLegacyText("Fatal error" + e1.getMessage()));

            } finally {
                e.completeIntent(plugin);
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onServerConnectedEvent(ServerConnectedEvent e)
    {
        String proxyName = plugin.getProxyManager().getProxy().getUuid().toString();
        String serveName = e.getServer().getInfo().getName();
        UUID uuid = e.getPlayer().getUniqueId();
        String playerName = e.getPlayer().getName();

        plugin.getSqlManager().updateAsynchronously("INSERT INTO `online_player`(uuid, username, proxy, server) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE username = ?, proxy = ?, server = ?",
                uuid.toString(),playerName, proxyName, serveName, playerName, proxyName, serveName);
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDisconnectEvent(PlayerDisconnectEvent e)
    {
        plugin.getSqlManager().updateAsynchronously("DELETE FROM `online_player` WHERE uuid = ?", e.getPlayer().getUniqueId().toString());
    }

}
