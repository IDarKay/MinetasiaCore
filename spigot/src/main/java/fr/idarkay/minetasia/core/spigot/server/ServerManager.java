package fr.idarkay.minetasia.core.spigot.server;

import fr.idarkay.minetasia.core.api.utils.Server;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.frs.CoreFRSMessage;
import fr.idarkay.minetasia.core.spigot.frs.ServerFrsMessage;
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

        plugin.getFrsClient().getValues("server", plugin.getFrsClient().getFields("server")).forEach( (k, v) -> {
            if(v != null && !v.equals("null"))
            servers.put(k, MineServer.getServerFromJson(v));
        });
    }

    private boolean register = false;

    public void registerServer()
    {
        if(!register)
        {
            String json = server.toJson();
            plugin.getFrsClient().setValue("server/" + server.getName(), json, false);
            plugin.publish(CoreFRSMessage.CHANNEL, ServerFrsMessage.getMessage(ServerFrsMessage.CREATE, json));
            register = true;
        }
    }

    public void disable()
    {
        plugin.getFrsClient().setValue("server/" + server.getName(), null, true);
        plugin.getFrsClient().publish(CoreFRSMessage.CHANNEL, ServerFrsMessage.getMessage(ServerFrsMessage.REMOVE,  server.getName()), true);
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
            final String data = plugin.getValue("server/" + name);
            if(data != null && !data.isEmpty() && !data.equals("null"))
            {
                return MineServer.getServerFromJson(data);
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
