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
 * @author Alois. B. (IDarKay),
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
        if(ip.equals("")) ip = "127.0.0.1";
        int port = Bukkit.getPort();

        Server server = new Server(ip, port, plugin.getServerType(), plugin.getServerConfig());
        this.server = server;
        String json = server.toJson();
        plugin.getFrsClient().setValue("server", server.getName(), json);
        server.initSQL(plugin.getSqlManager());;
        plugin.publish("core-server", "create;" + json);
        plugin.getFrsClient().getValues("server", plugin.getFrsClient().getFields("server")).forEach( (k, v) -> {
            if(v != null && !v.equals("null"))
            servers.put(k, Server.getServerFromJson(v));
        });
    }

    public void disable()
    {
        plugin.getFrsClient().setValue("server", server.getName(), null, true);
        plugin.getFrsClient().publish("core-server", "remove;" + server.getName(), true);
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
