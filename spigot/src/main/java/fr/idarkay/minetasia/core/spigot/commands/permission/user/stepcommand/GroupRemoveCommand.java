package fr.idarkay.minetasia.core.spigot.commands.permission.user.stepcommand;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.commands.permission.user.GroupRemoveValueCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>GroupRemoveCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.user.stepcommand
 * GroupRemoveCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 01/12/2019 at 15:51
 */
public class GroupRemoveCommand  extends StepCommand implements FixCommand {

    public GroupRemoveCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_USER_GROUP_REMOVE, CommandPermission.PERMISSION_USER_GROUP, 5);
        child.add(new GroupRemoveValueCommand(plugin));
    }

    @Override
    public String getLabel() {
        return "remove";
    }

}
