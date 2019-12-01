package fr.idarkay.minetasia.core.spigot.commands.permission.group.stepcommand;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.permission.PermissionManager;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * File <b>GroupCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.group.stepcommand
 * GroupCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 30/11/2019 at 22:33
 */
public final class GroupCommand extends StepCommand implements FixCommand {

    private final PermissionManager pm;

    public GroupCommand(MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_GROUP, CommandPermission.PERMISSION_GROUP, 2);
        this.pm = plugin.getPermissionManager();
        child.add(new CreateGroupCommand(plugin));
        child.add(new NameCommand(plugin));
    }

    @Override
    public List<String> tabComplete(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        List<String> back = getBasicTabCompleter(sender, args); // get create cmd
        back.addAll(getCustomCompleter(sender, args, pm.groups.keySet(), true)); //get all group name
        return back;
    }

    @Override
    public String getLabel() {
        return "group";
    }
}
