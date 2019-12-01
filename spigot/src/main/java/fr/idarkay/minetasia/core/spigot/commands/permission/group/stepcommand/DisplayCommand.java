package fr.idarkay.minetasia.core.spigot.commands.permission.group.stepcommand;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.commands.permission.group.DisplayNameCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>DisplayCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.group.stepcommand
 * DisplayCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 01/12/2019 at 10:53
 */
public final class DisplayCommand extends StepCommand implements FixCommand {

    public DisplayCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_GROUP_DISPLAY, CommandPermission.PERMISSION_GROUP_DISPLAY, 4);
        child.add(new DisplayNameCommand(plugin));
    }

    @Override
    public String getLabel() {
        return "display";
    }
}
