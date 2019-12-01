package fr.idarkay.minetasia.core.spigot.commands.permission.group.stepcommand;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.commands.permission.common.PermissionValueCommand;
import fr.idarkay.minetasia.core.spigot.commands.permission.common.StepAddCommand;
import fr.idarkay.minetasia.core.spigot.commands.permission.common.StepPermissionCommand;
import fr.idarkay.minetasia.core.spigot.commands.permission.common.StepRemoveCommand;
import fr.idarkay.minetasia.core.spigot.commands.permission.group.DeleteCommand;
import fr.idarkay.minetasia.core.spigot.commands.permission.group.InfoCommand;
import fr.idarkay.minetasia.core.spigot.commands.permission.group.SaveCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * File <b>NameCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.group.stepcommand
 * NameCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 01/12/2019 at 10:23
 */
public final class NameCommand extends StepCommand implements FlexibleCommand {

    public NameCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_GROUP_NAME, CommandPermission.PERMISSION_GROUP, 3);

        // for permission
        StepCommand permissionC = new StepPermissionCommand(plugin, Lang.DESC_PERMISSION_GROUP_PERMISSION, CommandPermission.PERMISSION_GROUP_PERMISSION, length +1);
        StepCommand addC = new StepAddCommand(plugin, Lang.DESC_PERMISSION_GROUP_PERMISSION_ADD, CommandPermission.PERMISSION_GROUP_PERMISSION, length +2);
        addC.addChild(new PermissionValueCommand(plugin, Lang.DESC_PERMISSION_GROUP_PERMISSION_ADD_VALUE, CommandPermission.PERMISSION_GROUP_PERMISSION, length + 3, PermissionValueCommand.Type.GROUP_ADD));
        permissionC.addChild(addC);
        StepCommand removeC = new StepRemoveCommand(plugin, Lang.DESC_PERMISSION_GROUP_PERMISSION_REMOVE, CommandPermission.PERMISSION_GROUP_PERMISSION, length + 2, StepRemoveCommand.Type.GROUP);
        removeC.addChild(new PermissionValueCommand(plugin, Lang.DESC_PERMISSION_GROUP_PERMISSION_REMOVE_VALUE, CommandPermission.PERMISSION_GROUP_PERMISSION, length + 3, PermissionValueCommand.Type.GROUP_REMOVE));
        permissionC.addChild(removeC);

        // set all child
        child.add(permissionC);
        child.add(new DisplayCommand(plugin));
        child.add(new PriorityCommand(plugin));
        child.add(new SaveCommand(plugin));
        child.add(new InfoCommand(plugin));
        child.add(new ParentCommand(plugin));
        child.add(new DeleteCommand(plugin));

    }

    @Override
    public String getLabel() {
        return "<group name>";
    }

    @Override
    public boolean isAllPossibilities() {
        return true;
    }

    @Override
    public List<String> getPossibilities() {
        return null;
    }
}
