package fr.idarkay.minetasia.core.spigot.commands.permission.group.stepcommand;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * File <b>ParentCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.group.stepcommand
 * ParentCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 01/12/2019 at 13:50
 */
public final class ParentCommand extends StepCommand implements FixCommand {


    public ParentCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_GROUP_PARENT, CommandPermission.PERMISSION_GROUP_PARENT, 4);
        child.add(new ParentAddCommand(plugin));
        child.add(new ParentRemoveCommand(plugin));
    }

    @Override
    public String getLabel() {
        return "parent";
    }
}
