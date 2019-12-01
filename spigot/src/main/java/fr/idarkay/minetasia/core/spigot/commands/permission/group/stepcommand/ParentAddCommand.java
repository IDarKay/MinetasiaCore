package fr.idarkay.minetasia.core.spigot.commands.permission.group.stepcommand;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.commands.permission.group.ParentAddValueCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>ParentAddCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.group.stepcommand
 * ParentAddCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 01/12/2019 at 13:57
 */
public class ParentAddCommand extends StepCommand implements FixCommand {

    public ParentAddCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_GROUP_PARENT_ADD, CommandPermission.PERMISSION_GROUP_PARENT, 5);
        child.add(new ParentAddValueCommand(plugin));
    }

    @Override
    public String getLabel() {
        return "add";
    }
}
