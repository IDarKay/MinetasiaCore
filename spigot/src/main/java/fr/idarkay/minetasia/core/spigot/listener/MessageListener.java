package fr.idarkay.minetasia.core.spigot.listener;

import fr.idarkay.minetasia.core.api.Command;
import fr.idarkay.minetasia.core.api.event.MessageReceivedEvent;
import fr.idarkay.minetasia.core.api.utils.Server;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.commands.friends.FriendCommand;
import fr.idarkay.minetasia.core.spigot.messages.CoreMessage;
import fr.idarkay.minetasia.core.spigot.messages.PartyMessage;
import fr.idarkay.minetasia.core.spigot.messages.PlayerMessage;
import fr.idarkay.minetasia.core.spigot.messages.ServerMessage;
import fr.idarkay.minetasia.core.spigot.permission.Group;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.Args;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import fr.idarkay.minetasia.normes.Tuple;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

/**
 * File <b>MessageListener</b> located on fr.idarkay.minetasia.core.common.listener
 * MessageListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 20/11/2019 at 13:46
 */
public final class MessageListener implements Listener {



    private final MinetasiaCore plugin;

    public MessageListener(MinetasiaCore plugin)
    {
        this.plugin = plugin;
        new PartyMessage();
        new PlayerMessage();
        new ServerMessage();
    }

    @EventHandler
    public void onMessageEvent(MessageReceivedEvent e)
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
            case "core-player-tp":
            {
                String[] msg = e.getValue().split(";",2);
                if(msg.length > 1)
                {
                    final UUID playerUUID = UUID.fromString(msg[0]);
                    final Server server = plugin.getServer(msg[1]);
                    if(server != null)
                    {
                        plugin.movePlayerToServer(playerUUID, server);
                    }
                }
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
                                    FriendCommand.newRequest(plugin, u, UUID.fromString(msg[1]), p);
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
                            for(org.bukkit.entity.Player pl : plugin.socialSpyPlayer) pl.sendMessage(Lang.MSG_FORMAT_SOCIAL_SPY.getWithoutPrefix(MinetasiaLang.BASE_LANG, Lang.Argument.PLAYER_SENDER.match(msg[3])
                                    , Lang.Argument.PLAYER_RECEIVER.match(msg[4]), Lang.Argument.MESSAGE.match(msg[5])));
                        }
                        if (p != null) {
                            boolean prefix = Boolean.parseBoolean(msg[2]);
                            String[] arg = new String[msg.length - 3];
                            for (int i = 3; i < msg.length; i++) arg[i - 3] = msg[i];
                            String m;

                            Tuple<? extends Args, Object>[] argument = new Tuple[arg.length];

                            int c = 0;
                            for (String o : arg)
                            {
                                String[] split = splitInTow(o, '\\');
                                argument[c] = Lang.Argument.valueOf(split[0]).match(split[1]);
                                c++;
                            }

                            if(prefix)  m = Lang.valueOf(msg[0]).get(plugin.getPlayerLang(u), argument);
                            else m = Lang.valueOf(msg[0]).getWithoutPrefix(plugin.getPlayerLang(u), argument);

                            if(msg[0].equals(Lang.MSG_FORMAT.name()) && !msg[3].equals("console"))
                            {
                                UUID uu = plugin.getPlayerUUID(msg[3]);
                                if(uu != null)
                                {
                                    final String uus = uu.toString();
                                    final String data = plugin.getPlayerData(u, "last_talker").toString();
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
            case CoreMessage.CHANNEL:
            {
                String[] split = e.getValue().split(";");
                CoreMessage msg = CoreMessage.MESSAGE.get(split[0]);
                if(msg != null) msg.actionOnGet(plugin, split);
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

    private static String[] splitInTow(String s, char spliter)
    {
        String[] back = new String[2];
        int i = s.indexOf(spliter);
        back[0] = s.substring(0, i);
        back[1] = s.substring(i + 1);
        return back;
    }


}
