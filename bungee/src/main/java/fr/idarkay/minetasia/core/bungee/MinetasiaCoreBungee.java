package fr.idarkay.minetasia.core.bungee;

import fr.idarkay.minetasia.core.bungee.listener.FRSMessageListener;
import fr.idarkay.minetasia.core.bungee.listener.PlayerListener;
import fr.idarkay.minetasia.core.bungee.utils.FRSClient;
import fr.idarkay.minetasia.core.bungee.utils.SQLManager;
import fr.idarkay.minetasia.core.bungee.utils.proxy.ProxyManager;
import fr.idarkay.minetasia.core.bungee.utils.user.Player;
import fr.idarkay.minetasia.core.bungee.utils.user.PlayerManager;
import net.md_5.bungee.api.ReconnectHandler;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * File <b>MinetasiaCoreBungee</b> located on fr.idarkay.minetasia.core.bungee
 * MinetasiaCoreBungee is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 26/11/2019 at 12:43
 */
public final class MinetasiaCoreBungee extends Plugin {

    private FRSClient frsClient;
    private SQLManager sqlManager;
    private Configuration configuration;
    private ProxyManager proxyManager;
    private PlayerManager playerManager;

    @Override
    public void onLoad() {
        File configFile = new File(getDataFolder(), "config.yml");

        if(!configFile.getParentFile().exists()) configFile.getParentFile().mkdirs();
        if(!configFile.exists())
        {
            try (InputStream in = this.getResourceAsStream("config.yml"))
            {
                Files.copy(in, configFile.toPath());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try
        {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        frsClient  = new FRSClient(this);

        frsClient.startConnection(System.out, configuration.getString("frs.host"), configuration.getInt("frs.port"),
                configuration.getString("frs.password"));

        sqlManager = new SQLManager(this);
        sqlManager.update("CREATE TABLE IF NOT EXISTS `online_player` ( `uuid` VARCHAR(36) NOT NULL , `username` VARCHAR(16) NOT NULL , `proxy` VARCHAR(36) NOT NULL , `server` VARCHAR(64) NOT NULL , PRIMARY KEY (`uuid`)) ENGINE = MEMORY;");

    }

    @Override
    public void onEnable() {

        getProxy().setReconnectHandler(new ReconnectHandler()
        {
            private Random random = new Random();

            private Map<UUID, String> fallback = new HashMap<>();

            @Override
            public void setServer(ProxiedPlayer player)
            {
                if(player.getServer() != null && player.getServer().getInfo() != null)
                    fallback.put(player.getUniqueId(), player.getServer().getInfo().getName());
            }

            @Override
            public void save() { }

            @Override
            public ServerInfo getServer(ProxiedPlayer player)
            {
                if(getProxy().getServers().size() > 0)
                {
                    String srvName = fallback.getOrDefault(player.getUniqueId(), player.getServer() != null && player.getServer().getInfo() != null ? player.getServer().getInfo().getName() : null);
                    ServerInfo info = getProxy().getServerInfo(srvName);

                    if(info != null)
                        return info;

                    List<ServerInfo> hubs = getProxy().getServers().entrySet().stream().filter(e -> e.getKey().startsWith("hub")).map(Map.Entry::getValue).collect(Collectors.toList());
                    ServerInfo hub = hubs.get(random.nextInt(hubs.size()));
                    if(hub != null)
                        return hub;

                    return getProxy().getServers().values().iterator().next();
                }
                return null;
            }

            @Override
            public void close()
            {
                fallback.clear();
            }
        });

        proxyManager = new ProxyManager(this);
        proxyManager.init();
        playerManager = new PlayerManager(this);

        getProxy().getPluginManager().registerListener(this, new PlayerListener(this));
        getProxy().getPluginManager().registerListener(this, new FRSMessageListener(this));

        getProxy().getScheduler().schedule(this, () -> getFrsClient().setValue("proxy-player-count", getProxyManager().getProxy().getUuid().toString()
                , String.valueOf(getProxy().getOnlineCount())), getConfig().getInt("player_count_update"), TimeUnit.SECONDS);

    }

    @Override
    public void onDisable() {
        proxyManager.disable();
        getSqlManager().update("DELETE FROM `online_player` WHERE proxy = ?", proxyManager.getProxy().getUuid().toString());

    }

    public Configuration getConfig() {
        return configuration;
    }

    public FRSClient getFrsClient() {
        return frsClient;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public SQLManager getSqlManager() {
        return sqlManager;
    }

    public ProxyManager getProxyManager() {
        return proxyManager;
    }

    public void setUserName(UUID uuid, String username)
    {
        this.getProxy().getScheduler().runAsync(this, () ->
        {
            Player p;
            if((p = playerManager.get(uuid)) != null)
            {
                p.setUsername(username);
                frsClient.publish("core-data", "name;" + uuid.toString() + ";" + username);
                frsClient.setValue("usersData", uuid.toString(), p.getJson());
            }
        });
    }

}
