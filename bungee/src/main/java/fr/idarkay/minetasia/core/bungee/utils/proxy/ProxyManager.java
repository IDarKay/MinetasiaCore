package fr.idarkay.minetasia.core.bungee.utils.proxy;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.idarkay.minetasia.core.bungee.MinetasiaCoreBungee;
import fr.idarkay.minetasia.core.bungee.utils.FRSClient;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;

/**
 * File <b>ProxyManager</b> located on fr.idarkay.minetasia.core.bungee.utils.proxy
 * ProxyManager is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 26/11/2019 at 18:52
 */
public final class ProxyManager {

    public static String motd;
    private final MinetasiaCoreBungee plugin;
    private final FRSClient frsClient;
    private Proxy proxy;

    public ProxyManager(MinetasiaCoreBungee plugin)
    {
        this.plugin = plugin;
        this.frsClient = plugin.getFrsClient();
        motd = plugin.getConfig().getString("server_motd");
    }

    public void init()
    {
        String ip =  plugin.getProxy().getConfig().getListeners().iterator().next().getHost().getHostName();
        int port = plugin.getProxy().getConfig().getListeners().iterator().next().getHost().getPort();

        proxy = new Proxy(ip, port);

        frsClient.setValue("proxy", proxy.getUuid().toString(), proxy.toJson());

        plugin.getProxy().getConfig().getServers().clear();
        plugin.getProxy().getServers().clear();

        for(String serverName : frsClient.getFields("server"))
        {
            try
            {
                JsonObject server = new JsonParser().parse(frsClient.getValue("server", serverName)).getAsJsonObject();
                String sIp = server.get("ip").getAsString();
                int sPort = server.get("port").getAsInt();

                if(!plugin.getProxy().getServers().containsKey(serverName))
                {
                    ServerInfo serverinfo = plugin.getProxy().constructServerInfo(serverName, InetSocketAddress.createUnresolved(sIp, sPort),
                            motd.replace("%s", serverName) , false);
                    plugin.getProxy().getServers().put(serverName, serverinfo);
                }
            } catch (Exception e)
            {
                System.out.println("rappeller à emilien de fix FRS");
            }

        }
    }

    public void disable()
    {
        frsClient.setValue("proxy-player-count", proxy.getUuid().toString(), null, true);
        frsClient.setValue("proxy", proxy.getUuid().toString(), null, true);
    }

    public Proxy getProxy() {
        return proxy;
    }
}
