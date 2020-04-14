package fr.idarkay.minetasia.core.spigot.commands.broadcast;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.MainCommand;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>BroadcastCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.broadcast
 * BroadcastCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 10/04/2020 at 19:53
 */
public class BroadcastCommand extends MainCommand implements FixCommand
{
    public BroadcastCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, null, CommandPermission.BROADCAST, 1);
        addChild(new BroadcastAllCommand(plugin));
        addChild(new BroadcastServerCommand(plugin));
        addChild(new BroadcastProxyCommand(plugin));
        addChild(new BroadcastServerTypeCommand(plugin));
    }

    @Override
    public String getLabel()
    {
        return "broadcast";
    }
}
