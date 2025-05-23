package fr.idarkay.minetasia.core.bungee.listener;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import fr.idarkay.minetasia.common.ServerConnection.MessageClient;
import fr.idarkay.minetasia.core.bungee.MinetasiaCoreBungee;
import fr.idarkay.minetasia.core.bungee.MongoCollections;
import fr.idarkay.minetasia.core.bungee.settings.Settings;
import fr.idarkay.minetasia.core.bungee.settings.SettingsKey;
import fr.idarkay.minetasia.core.bungee.utils.Lang;
import fr.idarkay.minetasia.core.bungee.utils.user.MinePlayer;
import fr.idarkay.minetasia.core.bungee.utils.user.PlayerSanction;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    private BaseComponent MOTD;


    public void setMOTD(String motd)
    {
        MOTD = new TextComponent(TextComponent.fromLegacyText(motd.replace("\\n", "\n")));
    }

    public PlayerListener(MinetasiaCoreBungee plugin)
    {
        this.plugin = plugin;
        final Settings<String> settings = plugin.getSettingsManager().getSettings(SettingsKey.MOTD);
        setMOTD(settings.getValue() == null ? " " : settings.getValue());
        try {
            File countryFile = new File(plugin.getDataFolder(), "data/GeoLite2-Country.mmdb");
            if(!countryFile.exists()) System.out.println("fil not found");
            reader = new DatabaseReader.Builder(countryFile).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPreLogEvent(ProxyPingEvent event)
    {
        if (event.getResponse() == null) return; // Check if response is not empty



        final ServerPing ping = event.getResponse();
        final ServerPing.Players players = ping.getPlayers();
        final ServerPing.Protocol version = ping.getVersion();

        ping.setVersion(new ServerPing.Protocol("Minetasia", 578));
        ping.setDescriptionComponent(MOTD);
//        final int onlineCount = plugin.getProxy().getOnlineCount();
//        ping.setPlayers(new ServerPing.Players(onlineCount + 1, onlineCount, ));


    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPreLogEvent(PreLoginEvent e)
    {
        if(plugin.getProxy().getServers().size() == 0)
        {
            e.setCancelled(true);
            e.setCancelReason(TextComponent.fromLegacyText("No online server retry later !"));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLoginEvent(LoginEvent e)
    {
        e.registerIntent(plugin);
        plugin.getProxy().getScheduler().runAsync(plugin, () -> {
            if(plugin.getProxy().getServers().size() == 0)
            {
                e.setCancelled(true);
                e.setCancelReason(TextComponent.fromLegacyText("No online server retry later !"));
                e.completeIntent(plugin);
                return;
            }
            try{
                final PendingConnection proxiedPlayer = e.getConnection();
                final UUID uuid = proxiedPlayer.getUniqueId();
                MinePlayer player = plugin.getPlayerManager().get(uuid);
                if(player != null)
                {
                    String name;
                    if (!player.getName().equals(name = proxiedPlayer.getName()))
                    {
                        player.setUsername(name);
                    }
                    final Document document = (Document) player.getData("BAN");
                    if(document != null)
                    {
                        final PlayerSanction playerSanction = PlayerSanction.fromDocument(document);
                        if(!playerSanction.isEnd())
                        {
                            final long reaming = playerSanction.getReamingTime();
                            final TimeUnit timeUnit = getBiggerTimeUnit(reaming);
                            final String msg = Lang.BAN_FORMAT.getMessage(player.getLang())
                                    .replace("{player}", playerSanction.authorName)
                                    .replace("{reason}", playerSanction.reason)
                                    .replace("{time}", timeUnit.convert(reaming, TimeUnit.MILLISECONDS) + " " + timeUnit.name().toLowerCase())
                                    .replace("@@", "\n");
                            e.setCancelled(true);
                            e.setCancelReason(TextComponent.fromLegacyText(msg));
                            return;
                        }
                    }
                }
                else
                {
                    String c;
                    try
                    {
                        CountryResponse r = reader.country(proxiedPlayer.getAddress().getAddress());
                        c = r.getCountry().getIsoCode().toLowerCase();
                    } catch (Exception ignore)
                    {
                        c = MinetasiaLang.BASE_LANG;
                    }

                    player = plugin.getPlayerManager().newPlayer(uuid, proxiedPlayer.getName(), c);
                }

                if((plugin.getMaintenanceServer().contains("all") || plugin.getMaintenanceServer().contains("hub")) && !plugin.getWhitelist().contains(player.getUUID()))
                {
                    final String msg = Lang.MAINTENANCE.getMessage(player.getLang()).replace("@@", "\n");
                    e.setCancelled(true);
                    e.setCancelReason(TextComponent.fromLegacyText(msg));
                    return;
                }

                final String proxyName = plugin.getProxyManager().getProxy().getUuid().toString();
                final String playerName = proxiedPlayer.getName();

                plugin.getMongoDBManager().insertOrReplaceIfExist(MongoCollections.ONLINE_USERS, uuid.toString(), new Document("_id", uuid.toString()).append("proxy_id", proxyName).append("username", playerName));

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
    public void onPlayerDisconnectEvent(PlayerDisconnectEvent e)
    {
        plugin.getProxy().getScheduler().runAsync(plugin, () -> {
            final Document doc = plugin.getMongoDBManager().getCollection(MongoCollections.ONLINE_USERS).findOneAndDelete(Filters.eq(e.getPlayer().getUniqueId().toString()));
            if(doc == null) return;
            final String partyKey = doc.getString("party_id");
            if(partyKey == null) return;

            final String msg = "core-messaging;core-party;REMOVE_PLAYER;" + partyKey + ";" + e.getPlayer().getUniqueId().toString();

            final Set<String> past = new HashSet<>();
            boolean one = false;
            for (Document d : plugin.getMongoDBManager().getWithReferenceAndMatch(MongoCollections.ONLINE_USERS, "party_id", partyKey, "servers", "server_id", "_id", "server"))
            {
                try
                {
                    final Document se = d.getList("server", Document.class).get(0);
                    final String ip = se.getString("ip");
                    final int port = se.getInteger("port");

                    if(!past.contains(ip + ";" + port))
                    {
                        past.add(ip + ";" + port);
                        MessageClient.send(ip, port, msg, false);
                    }
                    one = true;
                }
                catch (IndexOutOfBoundsException ignore)
                {
                    //some time on /end
                }
                catch (NullPointerException e1)
                {
                    e1.printStackTrace();
                }
            }

            //if no player in the team
            if(!one)
            {
                plugin.getMongoDBManager().delete(MongoCollections.PARTY, partyKey);
            }
            else
            {
                //else remove player from party in bdd
                plugin.getMongoDBManager().getCollection(MongoCollections.PARTY).updateOne(
                        Filters.eq(partyKey), Updates.pull("members", new Document("uuid", e.getPlayer().getUniqueId().toString()))
                );
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onServerSwitchEvent(ServerConnectEvent e)
    {
        final String server_type = e.getTarget().getName().split("#")[0];
        if(plugin.getMaintenanceServer().contains(server_type) &&  !plugin.getWhitelist().contains(e.getPlayer().getUniqueId()))
        {
            if(e.getPlayer().getServer().getInfo().getName().split("#")[0].equals("hub"))
            {
                e.setCancelled(true);
                plugin.getProxy().getScheduler().runAsync(plugin, () -> e.getPlayer().sendMessage(TextComponent.fromLegacyText(Lang.IN_GAME_MAINTENANCE.getMessage(plugin.getPlayerManager().get(e.getPlayer().getUniqueId()).getLang()))));
            }
            else
            {
                e.setCancelled(true);
                plugin.getProxy().getScheduler().runAsync(plugin, () -> e.getPlayer().disconnect(TextComponent.fromLegacyText(Lang.MAINTENANCE.getMessage(plugin.getPlayerManager().get(e.getPlayer().getUniqueId()).getLang()))));
            }
        }
    }

    private TimeUnit getBiggerTimeUnit(long time)
    {
        if(TimeUnit.MILLISECONDS.toDays(time) > 0)
        {
            return TimeUnit.DAYS;
        }
        else if(TimeUnit.MILLISECONDS.toHours(time) > 0)
        {
            return TimeUnit.HOURS;
        }
        else if(TimeUnit.MILLISECONDS.toMinutes(time) > 0)
        {
            return TimeUnit.MINUTES;
        }
        else
        {
            return TimeUnit.SECONDS;
        }
    }

}
