package fr.idarkay.minetasia.core.spigot.frs;

import fr.idarkay.minetasia.core.api.ServerPhase;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.server.Server;
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

    @Override
    public void actionOnGet(MinetasiaCore plugin, String... args)
    {
        if (args.length > 2)
        {
            if (args[1].equals(CREATE))
            {
                plugin.getServerManager().addServer(Server.getServerFromJson(CoreFRSMessage.concat(args, ";", 2)));
            } else if (args[1].equals(REMOVE))
            {
                plugin.getServerManager().removeServer(CoreFRSMessage.concat(args, ";", 2));
            } else if (args[1].equals(PLAYER_COUNT))
            {
                try
                {
                    plugin.getServer(args[2]).setPlayerCount(Integer.parseInt(args[3]));
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
        return CoreFRSMessage.getMessage(getIdentifier(), args);
    }

    public static boolean goodObject(Object... args)
    {
        return args.length > 1 && (args[0].equals(CREATE) || args[0].equals(REMOVE) || args[0].equals(PLAYER_COUNT) || args[0].equals(SERVER_STATUE));
    }

    public static  @NotNull String getIdentifier()
    {
        return "core-server";
    }
}
