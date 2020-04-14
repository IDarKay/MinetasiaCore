package fr.idarkay.minetasia.core.spigot.commands.maintenance;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>MaintenanceServerTypeCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.maintenance
 * MaintenanceServerTypeCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 11/04/2020 at 00:09
 */
public class MaintenanceServerTypeCommand extends StepCommand implements FixCommand
{
    public MaintenanceServerTypeCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_MAINTENANCE_SERVER_TYPE, CommandPermission.MAINTENANCE, 2);
        child.add(new MaintenanceServerTypeValueCommand(plugin));
    }

    @Override
    public String getLabel()
    {
        return "server_type";
    }
}
