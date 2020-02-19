package fr.idarkay.minetasia.core.bungee.listener;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.idarkay.minetasia.core.bungee.MinetasiaCoreBungee;
import fr.idarkay.minetasia.core.bungee.event.MessageEvent;
import fr.idarkay.minetasia.core.bungee.utils.proxy.ProxyManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.net.InetSocketAddress;

/**
 * File <b>MessageListener</b> located on fr.idarkay.minetasia.core.bungee.listener
 * MessageListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 27/11/2019 at 12:57
 */
public class MessageListener implements Listener {

    private MinetasiaCoreBungee plugin;

    public MessageListener(MinetasiaCoreBungee plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMessageEvent(MessageEvent e)
    {
        e.registerIntent(plugin);
        plugin.getProxy().getScheduler().runAsync(plugin, () -> {
            try
            {
                System.out.println(e.getChanel());
                if(e.getChanel().equals("core-messaging"))
                {
                    System.out.println(e.getValue());
                    String[] msg = e.getValue().split(";", 3);
                    if (msg.length > 1 && msg[0].equals("core-server")) {
                        if(msg[1].equals("create"))
                        {
                            final JsonObject server = new JsonParser().parse(msg[2]).getAsJsonObject();
                            final String sIp = server.get("ip").getAsString();
                            final int sPort = server.get("port").getAsInt();
                            final String serverName = server.get("_id").getAsString();

                            if(!plugin.getProxy().getServers().containsKey(serverName))
                            {
                                ServerInfo serverinfo = plugin.getProxy().constructServerInfo(serverName, InetSocketAddress.createUnresolved(sIp, sPort),
                                        ProxyManager.motd.replace("%s", serverName) , false);
                                ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("["+serverName+"]" + "<-> server registered"));
                                plugin.getProxy().getServers().put(serverName, serverinfo);
                            }
                        }
                        else if (msg[1].equals("remove"))
                        {

                            if(plugin.getProxy().getServers().remove(msg[2]) != null)
                                ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("["+msg[2]+"]" + "<-> server unregistered"));
                        }
                    }
                }
            } catch (Exception ignore)
            {ignore.printStackTrace();} finally {
                e.completeIntent(plugin);
            }

        });

    }

}
