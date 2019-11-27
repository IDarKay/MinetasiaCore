package fr.idarkay.minetasia.core.spigot.listener;

import fr.idarkay.minetasia.core.api.Economy;
import fr.idarkay.minetasia.core.api.event.FRSMessageEvent;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.server.Server;
import fr.idarkay.minetasia.core.spigot.user.Player;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

/**
 * File <b>FRSMessageListener</b> located on fr.idarkay.minetasia.core.common.listener
 * FRSMessageListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 20/11/2019 at 13:46
 */
public final class FRSMessageListener implements Listener {

    private final MinetasiaCore plugin;

    public FRSMessageListener(MinetasiaCore plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFRSMessageEvent(FRSMessageEvent e)
    {
        switch (e.getChanel())
        {
            case "core-data":
            {
                String[] msg = e.getValue().split(";");
                if (msg.length > 2) {
                    try {
                        Player p;
                        if ((p = plugin.getPlayerManager().getOnlyInCache(UUID.fromString(msg[1]))) != null) {
                            switch (msg[0]) {
                                case "data":
                                    p.setData(msg[1], concat(msg, ";", 3));
                                    break;
                                case "username":
                                    p.setUsername(concat(msg, ";", 2));
                                    break;
                                case "money":
                                    p.setMoney(Economy.valueOf(msg[2]), Float.parseFloat(msg[3]));
                                    break;
                                case "fremove":
                                    p.removeFriends(UUID.fromString(msg[2]));
                                    break;
                                case "fadd":
                                    p.addFriends(UUID.fromString(msg[2]));
                            }
                        }
                    } catch (IllegalArgumentException ignore) {
                    }
                }
                break;
            }
            case "core-cmd":
            {
                String[] msg = e.getValue().split(";");
                if (msg.length > 2) {
                    try {
                        UUID u = UUID.fromString(msg[2]);
                        org.bukkit.entity.Player p = plugin.getServer().getPlayer(u);
                        if (p != null) {
                            if ("friends".equals(msg[0])) {
                                if (plugin.isFriendsOn())
                                    plugin.getFriendsExecutor().newRequest(u, UUID.fromString(msg[1]), p);
                            }
                        }

                    } catch (IllegalArgumentException ignore) {
                    }
                }
                break;
            }
            case "core-msg":
            {
                String[] msg = e.getValue().split(";");
                if (msg.length > 1) {
                    try {
                        UUID u = UUID.fromString(msg[1]);
                        org.bukkit.entity.Player p = plugin.getServer().getPlayer(u);
                        if (p != null) {
                            Object[] arg = new Object[msg.length - 2];
                            for (int i = 2; i < msg.length; i++) arg[i - 2] = msg[i];
                            p.sendMessage(Lang.valueOf(msg[0]).get(plugin.getPlayerLang(u), arg));
                        }

                    } catch (IllegalArgumentException ignore) {
                    }
                }
                break;
            }
            case "core-server":
            {
                String[] msg = e.getValue().split(";");
                if (msg.length > 1) {
                    if(msg[0].equals("create"))
                    {
                        plugin.getServerManager().addServer(Server.getServerFromJson(concat(msg, ";", 1)));
                    }
                    else if (msg[0].equals("remove"))
                    {
                        plugin.getServerManager().removeServer(concat(msg, ";", 1));
                    }
                }
            }
        }
    }

    private String concat(String[] array, String split, int indexFrom)
    {
        StringBuilder result = new StringBuilder();
        for(int i = indexFrom; i < array.length; i++) result.append(i != indexFrom && split != null ? split : "").append(array[i]);
        return result.toString();
    }

}
