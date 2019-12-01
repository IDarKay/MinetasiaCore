package fr.idarkay.minetasia.core.spigot.commands.permission.user;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.permission.Group;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * File <b>GroupAddValueCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.user
 * GroupAddValueCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 01/12/2019 at 15:55
 */
public class GroupAddValueCommand extends StepCommand implements FlexibleCommand {

    public GroupAddValueCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_USER_GROUP_ADD_VALUE, CommandPermission.PERMISSION_USER_GROUP, 6);
        child.add(new TempGroupAddCommand(plugin));
    }

    @Override
    public String getLabel() {
        return "<group>";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        final String lang = getLangOfSender(sender);
        UUID u = plugin.getPlayerUUID(args[1]);
        Group g = plugin.getPermissionManager().groups.get(args[4]);
        if(g != null)
        {
            if(u != null)
            {
                plugin.getPermissionManager().addGroup(u, g.getName());
                sender.sendMessage(Lang.USER_GROUP_ADD.get(lang, permission, args[1]));
            }
            else sender.sendMessage(Lang.PLAYER_NOT_EXIST.get(lang));
        }
        else sender.sendMessage(Lang.GROUP_NOT_EXIST.get(lang));
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
