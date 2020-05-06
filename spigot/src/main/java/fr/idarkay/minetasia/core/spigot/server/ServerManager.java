package fr.idarkay.minetasia.core.spigot.server;

import com.mongodb.client.model.Filters;
import fr.idarkay.minetasia.common.MongoCollections;
import fr.idarkay.minetasia.core.api.utils.Server;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.messages.CoreMessage;
import fr.idarkay.minetasia.core.spigot.messages.ServerMessage;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

/**
 * File <b>ServerManager</b> located on fr.idarkay.minetasia.core.spigot.server
 * ServerManager is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 26/11/2019 at 20:08
 */
public final class ServerManager {

    private final HashMap<String, fr.idarkay.minetasia.core.api.utils.Server> servers = new HashMap<>();
    private final MineServer server;
    private final MinetasiaCore plugin;
    private final String updateSay;

    public ServerManager(MinetasiaCore plugin)
    {
        this.plugin = plugin;
        String ip = Bukkit.getIp();
        if(ip.equals("")) ip = "127.0.0.1";
        int port = Bukkit.getPort();

        this.server = new MineServer(ip, port, plugin.getMessageServer().getPort(), plugin.getServerType(),  plugin.getServerConfig());
        servers.put(server.getName(), server);
        String serverTypeLoad = plugin.getConfig().getString("server_type_load");
        this.updateSay = plugin.getConfig().getString("update_say");

        if(serverTypeLoad == null || serverTypeLoad.isEmpty() || serverTypeLoad.equals("none"))
        {
            return;
        }

        plugin.getMongoDbManager().getCollection(MongoCollections.SERVERS).find(Filters.regex("type", serverTypeLoad)).forEach(d -> servers.put(d.getString("_id"), MineServer.getServerFromDocument(d)));
    }

    private boolean register = false;


    public void registerServer()
    {
        if(!register)
        {
            plugin.getMongoDbManager().insert(MongoCollections.SERVERS, server.toDocument());

            String message = ServerMessage.getMessage(ServerMessage.CREATE,  server.toJson());
            boolean isSync = !Bukkit.isPrimaryThread();

            plugin.publishProxy(CoreMessage.CHANNEL, message, isSync);
            plugin.publishServerTypeRegex(CoreMessage.CHANNEL, message, updateSay, isSync);

            register = true;
        }
    }

    public void sayServerUpdate(String channel, String message, boolean sync)
    {
        plugin.publishProxy(channel, message, sync);
        plugin.publishServerTypeRegex(channel, message, updateSay, sync);
    }

    public void disable()
    {
        plugin.getMongoDbManager().delete(MongoCollections.SERVERS, server.getName());
        String message = ServerMessage.getMessage(ServerMessage.REMOVE,  server.getName());

        plugin.publishProxy(CoreMessage.CHANNEL, message, true);
        plugin.publishServerTypeRegex(CoreMessage.CHANNEL, message, updateSay, true);
        register = true;
    }

    public MineServer getServer()
    {
        return server;
    }

    @Nullable
    public Server getServer(String name)
    {

        if(!validateNam(name))  throw new IllegalArgumentException("invalid name format");

        final Server server = servers.get(name);
        if(server != null)
        {
            return server;
        }
        else
        {
            final Document data = plugin.getMongoDbManager().getByKey(MongoCollections.SERVERS, name);
            if(data != null)
            {
                return MineServer.getServerFromDocument(data);
            }
        }
        return null;
    }

    public HashMap<String,Server> getServers()
    {
        return servers;
    }

    public void addServer(MineServer server)
    {
        servers.put(server.getName(), server);
    }

    public void removeServer(String name)
    {
        servers.remove(name);
    }

    private boolean validateNam(String name)
    {
        final String[] split = name.split("#",2);
        try
        {
            //check is good uuid
            UUID.fromString(split[1]);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
