package fr.idarkay.minetasia.core.spigot.messages;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.normes.utils.GeneralUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>BroadCastMessage</b> located on fr.idarkay.minetasia.core.spigot.messages
 * BroadCastMessage is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 10/04/2020 at 20:03
 */
public class BroadCastMessage extends CoreMessage
{
    public BroadCastMessage()
    {
        super(getIdentifier());
    }

    @Override
    public void actionOnGet(MinetasiaCore plugin, String... args)
    {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', GeneralUtils.concat(args, ";", 1)));
    }

    public static @NotNull String getMessage(@NotNull String msg)
    {
        return CoreMessage.getMessage(getIdentifier(), msg);
    }

    public static @NotNull String getIdentifier()
    {
        return "core-broadcast";
    }

}
