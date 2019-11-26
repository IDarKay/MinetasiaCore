package fr.idarkay.minetasia.core.spigot.server;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import org.bukkit.Bukkit;

import java.util.HashMap;

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
    private final Server server;
    private final MinetasiaCore plugin;

    public ServerManager(MinetasiaCore plugin)
    {
        this.plugin = plugin;
        String ip = Bukkit.getIp();
        int port = Bukkit.getPort();

        Server server = new Server(ip, port, plugin.getServerType());
        this.server = server;
        String json = server.toJson();
        plugin.getFrsClient().setValue("server", server.getName(), json);
        plugin.publish("core-server", "create;" + json);
        plugin.getFrsClient().getValues("core", plugin.getFrsClient().getFields("core")).forEach( (k, v) -> servers.put(k, Server.getProxyFromJson(v)));
    }

    public void disable()
    {
        plugin.getFrsClient().setValue("server", server.getName(), null);
        plugin.publish("core-server", "remove;" + server.getName());
    }

    public Server getServer()
    {
        return server;
    }

    public HashMap<String, fr.idarkay.minetasia.core.api.utils.Server> getServers()
    {
        return servers;
    }

    public void addServer(Server server)
    {
        servers.put(server.getName(), server);
    }

    public void removeServer(String name)
    {
        servers.remove(name);
    }

}
