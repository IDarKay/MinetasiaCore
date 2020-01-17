package fr.idarkay.minetasia.core.spigot.listener;

import fr.idarkay.minetasia.core.api.Command;
import fr.idarkay.minetasia.core.api.Economy;
import fr.idarkay.minetasia.core.api.event.FRSMessageEvent;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.permission.Group;
import fr.idarkay.minetasia.core.spigot.server.Server;
import fr.idarkay.minetasia.core.spigot.user.Player;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * File <b>FRSMessageListener</b> located on fr.idarkay.minetasia.core.common.listener
 * FRSMessageListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
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
            case "core-group":
            {
                String[] msg = e.getValue().split(";", 3);
                if (msg.length > 2) {
                    if(msg[0].equals("group"))
                    {
                        if(!msg[2].equals("null"))
                        {
                            plugin.getPermissionManager().groups.put(msg[1], new Group(msg[2], plugin.getPermissionManager()));
                            plugin.getPermissionManager().updateGroupToPlayer(msg[1]);
                        }
                    }
                }
                break;
            }
            case "core-data":
            {
                String[] msg = e.getValue().split(";");
                if (msg.length > 2) {
                    try {
                        Player p;
                        if ((p = plugin.getPlayerManager().getOnlyInCache(UUID.fromString(msg[1]))) != null) {
                            switch (msg[0]) {
                                case "data":
                                    p.setData(msg[2], concat(msg, ";", 3));
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
                                    break;
                                case "stats":
                                    p.upDateStats(concat(msg, ";", 2));
                                    break;
                            }
                        }
                    } catch (IllegalArgumentException ignore) { }
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
                            if ("friends".equals(msg[0]))
                            {
                                if (plugin.isCommandEnable(Command.FRIEND))
                                    plugin.getFriendsExecutor().newRequest(u, UUID.fromString(msg[1]), p);
                            }
                            else if("permission".equals(msg[0]))
                            {
                                plugin.getPermissionManager().loadUser(u, true);
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
                if (msg.length > 2) {
                    try {
                        UUID u = UUID.fromString(msg[1]);
                        org.bukkit.entity.Player p = plugin.getServer().getPlayer(u);
                        if(msg[0].equals(Lang.MSG_FORMAT.name()))
                        {
                            for(org.bukkit.entity.Player pl : plugin.socialSpyPlayer) pl.sendMessage(Lang.MSG_FORMAT_SOCIAL_SPY.getWithoutPrefix(MinetasiaLang.BASE_LANG, msg[3], msg[4], msg[5]));
                        }
                        if (p != null) {
                            boolean prefix = Boolean.parseBoolean(msg[2]);
                            Object[] arg = new Object[msg.length - 3];
                            for (int i = 3; i < msg.length; i++) arg[i - 3] = msg[i];
                            String m;

                            if(prefix)  m = Lang.valueOf(msg[0]).get(plugin.getPlayerLang(u), arg);
                            else m = Lang.valueOf(msg[0]).getWithoutPrefix(plugin.getPlayerLang(u), arg);

                            if(msg[0].equals(Lang.MSG_FORMAT.name()) && !msg[3].equals("console"))
                            {
                                UUID uu = plugin.getPlayerUUID(msg[3]);
                                if(uu != null)
                                {
                                    String uus = uu.toString();
                                    String data = plugin.getPlayerData(u, "last_talker");
                                    if(data == null || !data.equals(uus))
                                        plugin.setPlayerData(u, "last_talker", uus);
                                }
                            }
                            p.sendMessage(m);
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
                    else if (msg[0].equalsIgnoreCase("playerCount"))
                    {
                        try
                        {
                            plugin.getServer(msg[1]).setPlayerCount(Integer.parseInt(msg[2]));
                        }
                        catch (Exception ignore) { }

                    }
                }
            }
        }
    }

    private String concat(String[] array, String split, int indexFrom)
    {
        StringBuilder result = new StringBuilder();
        for(int i = indexFrom; i < array.length; i++) result.append(i != indexFrom && split != null ? split : "").append(array[i]);
        String r = result.toString();
        return r.equals("null") ? null : r;
    }

}
