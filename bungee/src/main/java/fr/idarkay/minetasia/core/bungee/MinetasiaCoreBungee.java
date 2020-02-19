package fr.idarkay.minetasia.core.bungee;

import com.mongodb.client.model.Filters;
import fr.idarkay.minetasia.common.ServerConnection.MessageClient;
import fr.idarkay.minetasia.common.ServerConnection.MessageServer;
import fr.idarkay.minetasia.core.bungee.event.MessageEvent;
import fr.idarkay.minetasia.core.bungee.listener.MessageListener;
import fr.idarkay.minetasia.core.bungee.listener.PlayerListener;
import fr.idarkay.minetasia.core.bungee.utils.MongoDBManager;
import fr.idarkay.minetasia.core.bungee.utils.proxy.ProxyManager;
import fr.idarkay.minetasia.core.bungee.utils.user.PlayerManager;
import net.md_5.bungee.api.ReconnectHandler;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Function;
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

    private MongoDBManager mongoDBManager;
    private Configuration configuration;
    private ProxyManager proxyManager;
    private PlayerManager playerManager;
    private MessageServer messageServer;
    private static MinetasiaCoreBungee instance;

    @Override
    public void onLoad() {
        instance = this;
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

        final int publishPort = getConfig().getInt("publish-port");
        messageServer = new MessageServer(publishPort);

        mongoDBManager = new MongoDBManager(Objects.requireNonNull(configuration.getString("dbm.host")),
                Objects.requireNonNull(configuration.getString("dbm.dbname")),
                Objects.requireNonNull(configuration.getString("dbm.login")),
                Objects.requireNonNull(configuration.getString("dbm.password")));

    }

    public void initClientReceiver()
    {
        MessageClient.setReceiver(socket -> getProxy().getScheduler().runAsync(this, () -> {
            try
            {
                final String msg = MessageClient.read(socket);
                System.out.println(msg);
                if(msg == null)
                {
                    socket.close();
                    return;
                }

                final String[] split = msg.split(";", 2);
                if(split.length == 0)
                {
                    socket.close();
                    return;
                }

                socket.close();
                System.out.println("good");
                getProxy().getPluginManager().callEvent(new MessageEvent(split.length == 1 ? "none" : split[0], split.length == 1 ? split[0] : split[1]));
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }));
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
        getProxy().getPluginManager().registerListener(this, new MessageListener(this));

        initClientReceiver();
        messageServer.open();

    }

    @Override
    public void onDisable() {
        proxyManager.disable();
        mongoDBManager.getCollection(MongoCollections.ONLINE_USERS).deleteMany(Filters.eq("proxy", proxyManager.getProxy().getUuid().toString()));

    }

    public Configuration getConfig() {
        return configuration;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }


    public ProxyManager getProxyManager() {
        return proxyManager;
    }


    private Function<String, String> getStatementProcessor() {
        return s -> s.replace("'", "`"); // use backticks for quotes
    }

    public MongoDBManager getMongoDBManager()
    {
        return mongoDBManager;
    }

    public static MinetasiaCoreBungee getInstance()
    {
        return instance;
    }

}
