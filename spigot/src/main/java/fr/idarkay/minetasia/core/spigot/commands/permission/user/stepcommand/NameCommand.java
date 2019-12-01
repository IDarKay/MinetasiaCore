package fr.idarkay.minetasia.core.spigot.commands.permission.user.stepcommand;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.commands.permission.common.PermissionValueCommand;
import fr.idarkay.minetasia.core.spigot.commands.permission.common.StepAddCommand;
import fr.idarkay.minetasia.core.spigot.commands.permission.common.StepPermissionCommand;
import fr.idarkay.minetasia.core.spigot.commands.permission.common.StepRemoveCommand;
import fr.idarkay.minetasia.core.spigot.commands.permission.user.InfoCommand;
import fr.idarkay.minetasia.core.spigot.commands.permission.user.TempPermissionAddCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * File <b>NameCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.user.stepcommand
 * NameCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 01/12/2019 at 14:58
 */
public class NameCommand extends StepCommand implements FlexibleCommand {

    public NameCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_USER_NAME, CommandPermission.PERMISSION_USER, 3);

        // for permission
        StepCommand permissionC = new StepPermissionCommand(plugin, Lang.DESC_PERMISSION_USER_PERMISSION, CommandPermission.PERMISSION_USER_PERMISSION, length +1);
        StepCommand addC = new StepAddCommand(plugin, Lang.DESC_PERMISSION_USER_PERMISSION_ADD, CommandPermission.PERMISSION_USER_PERMISSION, length +2);
        SubCommand v = new PermissionValueCommand(plugin, Lang.DESC_PERMISSION_USER_PERMISSION_ADD_VALUE, CommandPermission.PERMISSION_USER_PERMISSION, length + 3, PermissionValueCommand.Type.USER_ADD);
        v.getChild().add(new TempPermissionAddCommand(plugin));
        addC.addChild(v);
        permissionC.addChild(addC);
        StepCommand removeC = new StepRemoveCommand(plugin, Lang.DESC_PERMISSION_USER_PERMISSION_REMOVE, CommandPermission.PERMISSION_USER_PERMISSION, length + 2, StepRemoveCommand.Type.GROUP);
        removeC.addChild(new PermissionValueCommand(plugin, Lang.DESC_PERMISSION_USER_PERMISSION_REMOVE_VALUE, CommandPermission.PERMISSION_USER_PERMISSION, length + 3, PermissionValueCommand.Type.USER_REMOVE));
        permissionC.addChild(removeC);

        child.add(permissionC);
        child.add(new InfoCommand(plugin));
        child.add(new GroupCommand(plugin));
    }

    @Override
    public String getLabel() {
        return "<user name>";
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
