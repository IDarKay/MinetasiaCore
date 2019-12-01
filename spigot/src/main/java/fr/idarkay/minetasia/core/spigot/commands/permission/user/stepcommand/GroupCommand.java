package fr.idarkay.minetasia.core.spigot.commands.permission.user.stepcommand;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>GroupCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.user.stepcommand
 * GroupCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 01/12/2019 at 15:32
 */
public class GroupCommand extends StepCommand implements FixCommand {

    public GroupCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_GROUP_PARENT, CommandPermission.PERMISSION_GROUP_PARENT, 4);
        child.add(new GroupAddCommand(plugin));
        child.add(new GroupRemoveCommand(plugin));
    }

    @Override
    public String getLabel() {
        return "group";
    }

}
