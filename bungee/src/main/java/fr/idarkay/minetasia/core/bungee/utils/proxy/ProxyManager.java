package fr.idarkay.minetasia.core.bungee.utils.proxy;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.idarkay.minetasia.core.bungee.MinetasiaCoreBungee;
import fr.idarkay.minetasia.core.bungee.MongoCollections;
import fr.idarkay.minetasia.core.bungee.utils.FRSClient;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import org.bson.Document;

import java.net.InetSocketAddress;

/**
 * File <b>ProxyManager</b> located on fr.idarkay.minetasia.core.bungee.utils.proxy
 * ProxyManager is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 26/11/2019 at 18:52
 */
public final class ProxyManager {

    public static String motd;
    private final MinetasiaCoreBungee plugin;
    private Proxy proxy;

    public ProxyManager(MinetasiaCoreBungee plugin)
    {
        this.plugin = plugin;
        motd = plugin.getConfig().getString("server_motd");
    }

    public void init()
    {
        String ip =  plugin.getProxy().getConfig().getListeners().iterator().next().getHost().getHostName();
        int port = plugin.getProxy().getConfig().getListeners().iterator().next().getHost().getPort();

        proxy = new Proxy(ip, port);

        plugin.getMongoDBManager().insert(MongoCollections.PROXY, proxy.toDocument());

        plugin.getProxy().getConfig().getServers().clear();
        plugin.getProxy().getServers().clear();

        for(Document d : plugin.getMongoDBManager().getAll(MongoCollections.SERVERS))
        {
            try
            {
                String sIp = d.getString("ip");
                int sPort = d.getInteger("port");
                String name = d.getString("_id");

                if(!plugin.getProxy().getServers().containsKey(name))
                {
                    ServerInfo serverinfo = plugin.getProxy().constructServerInfo(name, InetSocketAddress.createUnresolved(sIp, sPort),
                            motd.replace("%s", name) , false);
                    ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("["+name+"]" + "<-> server registered"));
                    plugin.getProxy().getServers().put(name, serverinfo);
                }
            } catch (Exception ignore)
            {
            }
        }
    }

    public void disable()
    {
        plugin.getMongoDBManager().delete(MongoCollections.PROXY, proxy.getUuid().toString());
    }

    public Proxy getProxy() {
        return proxy;
    }
}
