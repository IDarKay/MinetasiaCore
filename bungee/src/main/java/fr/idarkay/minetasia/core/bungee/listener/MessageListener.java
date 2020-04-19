package fr.idarkay.minetasia.core.bungee.listener;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.idarkay.minetasia.core.bungee.MinetasiaCoreBungee;
import fr.idarkay.minetasia.core.bungee.event.MessageEvent;
import fr.idarkay.minetasia.core.bungee.utils.proxy.ProxyManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

/**
 * File <b>MessageListener</b> located on fr.idarkay.minetasia.core.bungee.listener
 * MessageListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
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
                if(e.getChanel().equals("core-messaging"))
                {
                    String[] msg = e.getValue().split(";", 3);
                    if(msg.length > 1)
                    {
                        if (msg[0].equals("core-server")) {
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
                        else if(msg[0].equalsIgnoreCase("core-broadcast"))
                        {
                            final String message = concat(msg, ";", 1);
                            ProxyServer.getInstance().broadcast(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message)));
                        }
                        else if(msg[0].equalsIgnoreCase("core-settings"))
                        {
                            final String concat = concat(msg, ";", 1);
                            plugin.getSettingsManager().Update(concat);
                        }
                    }
                }
            } catch (Exception ignore)
            {ignore.printStackTrace();} finally {
                e.completeIntent(plugin);
            }

        });

    }


    public  @NotNull String concat(String[] array, String split, int indexFrom)
    {
        StringBuilder result = new StringBuilder();
        for(int i = indexFrom; i < array.length; i++) result.append(i != indexFrom && split != null ? split : "").append(array[i]);
        return result.toString();
    }

}
