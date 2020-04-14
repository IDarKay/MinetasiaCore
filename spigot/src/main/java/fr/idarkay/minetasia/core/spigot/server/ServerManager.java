package fr.idarkay.minetasia.core.spigot.server;

import com.mongodb.client.model.Filters;
import fr.idarkay.minetasia.core.api.MongoCollections;
import fr.idarkay.minetasia.core.api.utils.Server;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.messages.CoreMessage;
import fr.idarkay.minetasia.core.spigot.messages.ServerMessage;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * File <b>ServerManager</b> located on fr.idarkay.minetasia.core.spigot.server
 * ServerManager is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 26/11/2019 at 20:08
 */
public final class ServerManager {

    private final HashMap<String, fr.idarkay.minetasia.core.api.utils.Server> servers = new HashMap<>();
    private final MineServer server;
    private final MinetasiaCore plugin;

    public ServerManager(MinetasiaCore plugin)
    {
        this.plugin = plugin;
        String ip = Bukkit.getIp();
        if(ip.equals("")) ip = "127.0.0.1";
        int port = Bukkit.getPort();

        this.server = new MineServer(ip, port, plugin.getMessageServer().getPort(), plugin.getServerType(), plugin.getServerConfig());
        servers.put(server.getName(), server);

        plugin.getMongoDbManager().getCollection(MongoCollections.SERVERS).find(Filters.regex("type", Objects.requireNonNull(plugin.getConfig().getString("server_type_load")))).forEach(d -> servers.put(d.getString("_id"), MineServer.getServerFromDocument(d)));
    }

    private boolean register = false;

    public void registerServer()
    {
        if(!register)
        {
            plugin.getMongoDbManager().insert(MongoCollections.SERVERS, server.toDocument());

            if(server.getType().equalsIgnoreCase("hub"))
            {
                plugin.publishGlobal(CoreMessage.CHANNEL, ServerMessage.getMessage(ServerMessage.CREATE,  server.toJson()), true, true);
            }
            else
            {
                plugin.publishProxy(CoreMessage.CHANNEL, ServerMessage.getMessage(ServerMessage.CREATE,  server.toJson()), true);
                plugin.publishHub(CoreMessage.CHANNEL, ServerMessage.getMessage(ServerMessage.CREATE,  server.toJson()), true);
            }
            register = true;
        }
    }

    public void disable()
    {
        plugin.getMongoDbManager().delete(MongoCollections.SERVERS, server.getName());
        if(server.getType().equalsIgnoreCase("hub"))
        {
            plugin.publishGlobal(CoreMessage.CHANNEL, ServerMessage.getMessage(ServerMessage.REMOVE,  server.getName()), true, true);
        }
        else
        {
            plugin.publishProxy(CoreMessage.CHANNEL, ServerMessage.getMessage(ServerMessage.REMOVE,  server.getName()), true);
            plugin.publishHub(CoreMessage.CHANNEL, ServerMessage.getMessage(ServerMessage.REMOVE,  server.getName()), true);
        }
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
