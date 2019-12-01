package fr.idarkay.minetasia.core.spigot.commands.permission.group.stepcommand;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.commands.permission.group.PriorityValueCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>PriorityCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.group.stepcommand
 * PriorityCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 01/12/2019 at 11:19
 */
public final class PriorityCommand extends StepCommand implements FixCommand {

    public PriorityCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_GROUP_PRIORITY, CommandPermission.PERMISSION_GROUP_PRIORITY, 4);
        child.add(new PriorityValueCommand(plugin));
    }

    @Override
    public String getLabel() {
        return "priority";
    }
}
