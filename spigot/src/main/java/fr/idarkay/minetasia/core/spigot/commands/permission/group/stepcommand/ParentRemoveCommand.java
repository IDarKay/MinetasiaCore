package fr.idarkay.minetasia.core.spigot.commands.permission.group.stepcommand;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.commands.permission.group.ParentRemoveValueCommand;
import fr.idarkay.minetasia.core.spigot.permission.Group;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * File <b>ParentRemoveCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.group.stepcommand
 * ParentRemoveCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 01/12/2019 at 13:57
 */
public class ParentRemoveCommand extends StepCommand implements FixCommand {

    public ParentRemoveCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_GROUP_PARENT_REMOVE, CommandPermission.PERMISSION_GROUP_PARENT, 5);
        child.add(new ParentRemoveValueCommand(plugin));
    }

    @Override
    public List<String> tabComplete(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        Group g = plugin.getPermissionManager().groups.get(args[1]);
        if(g == null) return super.tabComplete(plugin, sender, args, label);
        return getMultiCompleter(sender, args, g.getParents(), true);
    }

    @Override
    public String getLabel() {
        return "remove";
    }
}
