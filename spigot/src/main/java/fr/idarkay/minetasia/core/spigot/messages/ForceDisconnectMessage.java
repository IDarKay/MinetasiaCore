package fr.idarkay.minetasia.core.spigot.messages;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>ForceDisconnectMessage</b> located on fr.idarkay.minetasia.core.spigot.messages
 * ForceDisconnectMessage is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 11/04/2020 at 01:56
 */
public class ForceDisconnectMessage extends CoreMessage
{

    public ForceDisconnectMessage()
    {
        super(getIdentifier());
    }

    @Override
    public void actionOnGet(MinetasiaCore plugin, String... args)
    {
        Bukkit.getScheduler().runTask(plugin, () -> {
            final boolean movetoHub = args.length > 0 && args[0].equalsIgnoreCase("true");

            if(movetoHub)
                for (Player onlinePlayer : Bukkit.getOnlinePlayers())
                {
                    plugin.movePlayerToHub(onlinePlayer);
                }
            else
            {
                final String msg = Lang.MAINTENANCE.getWithoutPrefix(MinetasiaLang.BASE_LANG).replace("@@", "\n");
                for (Player onlinePlayer : Bukkit.getOnlinePlayers())
                {
                    onlinePlayer.kickPlayer(msg);
                }
            }
        });

    }

    public static @NotNull String getMessage(boolean moveToHub)
    {
        return CoreMessage.getMessage(getIdentifier(), moveToHub);
    }

    public static @NotNull String getIdentifier()
    {
        return "core-force-disconnect";
    }

}
