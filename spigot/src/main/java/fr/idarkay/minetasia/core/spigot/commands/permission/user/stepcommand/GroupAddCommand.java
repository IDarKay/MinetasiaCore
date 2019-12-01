package fr.idarkay.minetasia.core.spigot.commands.permission.user.stepcommand;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.commands.permission.user.GroupAddValueCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * File <b>GroupAddCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.user.stepcommand
 * GroupAddCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 01/12/2019 at 15:51
 */
public class GroupAddCommand extends StepCommand implements FixCommand  {

    public GroupAddCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_PERMISSION_USER_GROUP_ADD, CommandPermission.PERMISSION_USER_GROUP, 5);
        child.add(new GroupAddValueCommand(plugin));
    }

    @Override
    public List<String> tabComplete(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        return getMultiCompleter(sender, args, plugin.getPermissionManager().groups.keySet(), true);
    }

    @Override
    public String getLabel() {
        return "add";
    }

}
