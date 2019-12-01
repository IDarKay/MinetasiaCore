package fr.idarkay.minetasia.core.spigot.commands.permission;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.*;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.MainCommand;
import fr.idarkay.minetasia.core.spigot.commands.permission.group.stepcommand.GroupCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>PermissionCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission
 * PermissionCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 30/11/2019 at 23:03
 */
public class PermissionCommand extends MainCommand implements FixCommand {

    public PermissionCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION, CommandPermission.PERMISSION, 1);
        child.add(new GroupCommand(plugin));
        child.add(new HelpCommand(plugin));
        child.add(new PermissionListCommand(plugin));
        child.add(new UserCommand(plugin));
    }

    @Override
    public String getLabel() {
        return "permission";
    }
}
