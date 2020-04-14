package fr.idarkay.minetasia.core.spigot.commands.whitelist;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.MainCommand;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>WhitelistCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.whitelist
 * WhitelistCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 10/04/2020 at 23:05
 */
public class WhitelistCommand extends MainCommand
{
    public WhitelistCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, null, CommandPermission.WHITELIST, 1);
        addChild(new WhitelistAddCommand(plugin));
        addChild(new WhitelistRemoveCommand(plugin));
        addChild(new WhitelistListCommand(plugin));
    }

    @Override
    public String getLabel()
    {
        return "whitelist";
    }
}
