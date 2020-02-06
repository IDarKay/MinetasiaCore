package fr.idarkay.minetasia.core.spigot.frs;

import fr.idarkay.minetasia.core.api.ServerPhase;
import fr.idarkay.minetasia.core.api.event.ServerPlayerCountUpdateEvent;
import fr.idarkay.minetasia.core.api.event.ServerRegisterEvent;
import fr.idarkay.minetasia.core.api.event.ServerUnregisterEvent;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.server.Server;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>ServerFrsMessage</b> located on fr.idarkay.minetasia.core.spigot.frs
 * ServerFrsMessage is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 01/02/2020 at 13:45
 */
public class    ServerFrsMessage implements CoreFRSMessage
{

    public static final String CREATE = "create";
    public static final String REMOVE = "remove";
    public static final String PLAYER_COUNT = "playerCount";
    public static final String SERVER_STATUE = "serverStatue";
    public static final String SERVER_MAX_PLAYER = "serverMaxPlayer";

    @Override
    public void actionOnGet(MinetasiaCore plugin, String... args)
    {
        if (args.length > 2)
        {
            if (args[1].equals(CREATE))
            {
                final Server server = Server.getServerFromJson(CoreFRSMessage.concat(args, ";", 2));
                plugin.getServerManager().addServer(server);
                Bukkit.getPluginManager().callEvent(new ServerRegisterEvent(server));
            }
            else if (args[1].equals(REMOVE))
            {
                final fr.idarkay.minetasia.core.api.utils.Server server = plugin.getServer(CoreFRSMessage.concat(args, ";", 2));
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
            } else if(args[1].equals(SERVER_MAX_PLAYER))
            {
                try
                {
                    plugin.getServer(args[2]).setMaxPlayerCount(Integer.parseInt(args[3]));
                } catch (Exception ignore)
                {
                }
            }
        }
    }

    public static @NotNull String getMessage(Object... args)
    {
        if(!goodObject(args)) throw new IllegalArgumentException();
        return CoreFRSMessage.getMessage(getIdentifier(), args);
    }

    public static boolean goodObject(Object... args)
    {
        return args.length > 1 && (args[0].equals(CREATE) || args[0].equals(REMOVE) || args[0].equals(PLAYER_COUNT) || args[0].equals(SERVER_STATUE) || args[0].equals(SERVER_MAX_PLAYER));
    }

    public static  @NotNull String getIdentifier()
    {
        return "core-server";
    }
}
