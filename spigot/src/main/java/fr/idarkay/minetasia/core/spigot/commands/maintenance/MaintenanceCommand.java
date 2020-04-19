package fr.idarkay.minetasia.core.spigot.commands.maintenance;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.MainCommand;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>MaintenanceCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.maintenance
 * MaintenanceCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 11/04/2020 at 00:00
 */
public class MaintenanceCommand extends MainCommand implements FixCommand
{
    public MaintenanceCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, null, CommandPermission.MAINTENANCE, 1);
        addChild(new MaintenanceAllCommand(plugin));
        addChild(new MaintenanceServerTypeCommand(plugin));
    }

    @Override
    public String getLabel()
    {
        return "maintenance";
    }
}
