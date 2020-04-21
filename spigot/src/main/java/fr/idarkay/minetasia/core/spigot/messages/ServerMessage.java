package fr.idarkay.minetasia.core.spigot.messages;

import fr.idarkay.minetasia.core.api.ServerPhase;
import fr.idarkay.minetasia.core.api.event.*;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.server.MineServer;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>ServerMessage</b> located on fr.idarkay.minetasia.core.spigot.messages
 * ServerMessage is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 01/02/2020 at 13:45
 */
public class ServerMessage extends CoreMessage
{

    @Deprecated
    public static final String CREATE = "create";

    public static final String SERVER_ON = "server_on";
    public static final String SET_INFORMATION = "set_information";
    public static final String ASK_CONFIG = "ask_config";
    public static final String HUB_ASK_CONFIG = "hub_ask_config";

    public static final String REMOVE = "remove";
    public static final String PLAYER_COUNT = "playerCount";
    public static final String SERVER_STATUE = "serverStatue";

    public ServerMessage()
    {
        super(getIdentifier());
    }

    @Override
    public void actionOnGet(MinetasiaCore plugin, String... args)
    {
        System.out.println(concat(args, " ", 0));
        if (args.length > 2)
        {
            if(args[1].equals(HUB_ASK_CONFIG))
            {
                final fr.idarkay.minetasia.core.api.utils.Server server = plugin.getServer(args[2]);
                if(server != null)
                    Bukkit.getPluginManager().callEvent(new ServerAskedConfigEvent(server));
            }
            else if(args[1].equals(SERVER_ON))
            {
                final MineServer server = MineServer.getServerFromJson(CoreMessage.concat(args, ";", 2));
                plugin.getServerManager().addServer(server);
                Bukkit.getPluginManager().callEvent(new ServerRegisterEvent(server));
            }
            else if(args[1].equals(SET_INFORMATION))
            {
                final fr.idarkay.minetasia.core.api.utils.Server server = plugin.getServer(args[2]);
                if(server == null) return;
                String cfg = args[2];
                int maxPlayer = Integer.parseInt(args[3]);
                server.setServerConfig(cfg);;
                server.setMaxPlayerCount(maxPlayer);
                Bukkit.getPluginManager().callEvent(new ServerInformationUpdateEvent(server, cfg, maxPlayer));
            }
            else if(args[1].equals(ASK_CONFIG))
            {
                System.out.println("ask " + args[2]);
                plugin.doSync(() -> Bukkit.getPluginManager().callEvent(new ServerModeRequestEvent(args[2])));
            }
            else if (args[1].equals(CREATE))
            {
                final MineServer server = MineServer.getServerFromJson(CoreMessage.concat(args, ";", 2));
                plugin.getServerManager().addServer(server);
                Bukkit.getPluginManager().callEvent(new ServerRegisterEvent(server));
            }
            else if (args[1].equals(REMOVE))
            {
                final fr.idarkay.minetasia.core.api.utils.Server server = plugin.getServer(CoreMessage.concat(args, ";", 2));
                if(server != null)
                {
                    Bukkit.getPluginManager().callEvent(new ServerUnregisterEvent(server));
                    plugin.getServerManager().removeServer(server.getName());
                }
            }
            else if (args[1].equals(PLAYER_COUNT))
            {
                try
                {
                    final fr.idarkay.minetasia.core.api.utils.Server server = plugin.getServer(args[2]);
                    final int old = server.getPlayerCount();
                    final int count = Integer.parseInt(args[3]);
                    if(old != count)
                    {
                        server.setPlayerCount(count);
                        Bukkit.getPluginManager().callEvent(new ServerPlayerCountUpdateEvent(server, count, old));
                    }

                } catch (Exception ignore)
                {
                }
            } else if(args[1].equals(SERVER_STATUE))
            {
                try
                {
                    plugin.getServer(args[2]).setPhase(ServerPhase.valueOf(args[3]));
                } catch (Exception ignore)
                {
                }
            }
        }
    }

    public static @NotNull String getMessage(Object... args)
    {
        if(!goodObject(args)) throw new IllegalArgumentException();
        return CoreMessage.getMessage(getIdentifier(), args);
    }

    public static boolean goodObject(Object... args)
    {
        return true;
//        return args.length > 1 && (args[0].equals(CREATE) || args[0].equals(REMOVE) || args[0].equals(ASK_CONFIG) || args[0].equals(PLAYER_COUNT) || args[0].equals(SERVER_STATUE) || args[0].equals(SERVER_ON) || args[0].equals(SET_INFORMATION));
    }

    public static  @NotNull String getIdentifier()
    {
        return "core-server";
    }
}
