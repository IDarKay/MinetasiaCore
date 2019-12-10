package fr.idarkay.minetasia.core.bungee.listener;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.idarkay.minetasia.core.bungee.MinetasiaCoreBungee;
import fr.idarkay.minetasia.core.bungee.event.FRSMessageEvent;
import fr.idarkay.minetasia.core.bungee.utils.proxy.ProxyManager;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * File <b>FRSMessageListener</b> located on fr.idarkay.minetasia.core.bungee.listener
 * FRSMessageListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 27/11/2019 at 12:57
 */
public class FRSMessageListener implements Listener {

    private MinetasiaCoreBungee plugin;

    public FRSMessageListener(MinetasiaCoreBungee plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFRSMessageEvent(FRSMessageEvent e)
    {
        if(e.getChanel().equals("core-server"))
        {
            String[] msg = e.getValue().split(";", 2);
            if (msg.length > 1) {
                if(msg[0].equals("create"))
                {
                    JsonObject server = new JsonParser().parse(msg[1]).getAsJsonObject();
                    String sIp = server.get("ip").getAsString();
                    int sPort = server.get("port").getAsInt();
                    String serverName = server.get("type").getAsString() + "#" + server.get("uuid").getAsString();

                    if(!plugin.getProxy().getServers().containsKey(serverName))
                    {
                        ServerInfo serverinfo = plugin.getProxy().constructServerInfo(serverName, InetSocketAddress.createUnresolved(sIp, sPort),
                                ProxyManager.motd.replace("%s", serverName) , false);
                        plugin.getProxy().getServers().put(serverName, serverinfo);
                    }
                }
                else if (msg[0].equals("remove"))
                {
                    plugin.getProxy().getServers().remove(msg[1]);
                }
            }
        }
    }

}
