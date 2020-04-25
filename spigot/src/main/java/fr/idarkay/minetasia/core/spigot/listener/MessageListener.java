package fr.idarkay.minetasia.core.spigot.listener;

import fr.idarkay.minetasia.core.api.Command;
import fr.idarkay.minetasia.core.spigot.utils.InventorySyncType;
import fr.idarkay.minetasia.core.api.event.MessageReceivedEvent;
import fr.idarkay.minetasia.core.api.event.SocketPrePossesEvent;
import fr.idarkay.minetasia.core.api.utils.Server;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.commands.friends.FriendCommand;
import fr.idarkay.minetasia.core.spigot.messages.*;
import fr.idarkay.minetasia.core.spigot.permission.Group;
import fr.idarkay.minetasia.core.spigot.utils.InventorySyncTools;
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
 * @author Alois. B. (IDarKay),
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
        new SettingsUpdate();
        new SanctionMessage();
        new ReportMessage();
        new MsgMessage();
        new BroadCastMessage();
        new ForceDisconnectMessage();
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
            case CoreMessage.CHANNEL:
            {
                String[] split = e.getValue().split(";");
                CoreMessage msg = CoreMessage.MESSAGE.get(split[0]);
                if(msg != null) msg.actionOnGet(plugin, split);
            }
        }
    }

    @EventHandler
    public void onSocketPrePossesEvent(SocketPrePossesEvent event)
    {
        if (event.getChanel().equals("inventory-sync"))
        {
            try
            {
                String[] split = event.getValue().split(";", 3);
                UUID playerUUID = UUID.fromString(split[0]);
                InventorySyncType inventorySyncType = InventorySyncType.valueOf(split[1]);
                String value = split[2];
                if(!inventorySyncType.equals(InventorySyncType.NONE))
                {
                    InventorySyncTools.pendingSync.put(playerUUID, new Tuple<>(inventorySyncType, value));
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                event.setAnswer("error");
            }

        }
    }

}
