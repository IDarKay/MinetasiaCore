package fr.idarkay.minetasia.core.spigot.commands.permission.group.stepcommand;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.commands.permission.group.CreateGroupNameCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;

/**
 * File <b>CreateGroupCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.group.stepcommand
 * CreateGroupCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 30/11/2019 at 22:39
 */
public final class CreateGroupCommand extends StepCommand implements FixCommand {

    public CreateGroupCommand(MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_GROUP_CREATE, CommandPermission.PERMISSION_GROUP_CREATE, 3);
        child.add(new CreateGroupNameCommand(plugin));
    }

    @Override
    public String getLabel() {
        return "create";
    }

}
