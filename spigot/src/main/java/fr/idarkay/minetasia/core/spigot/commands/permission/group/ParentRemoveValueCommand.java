package fr.idarkay.minetasia.core.spigot.commands.permission.group;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.permission.Group;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * File <b>ParentRemoveValueCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.group
 * ParentRemoveValueCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 01/12/2019 at 14:05
 */
public class ParentRemoveValueCommand extends SubCommand implements FlexibleCommand {

    public ParentRemoveValueCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_GROUP_PARENT_REMOVE_VALUE, CommandPermission.PERMISSION_GROUP_PARENT, 6);
    }

    @Override
    public String getLabel() {
        return "<group>";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull  String[] args, @NotNull String label) {
        final String lang = getLangOfSender(sender);
        Group g = plugin.getPermissionManager().groups.get(args[1]);
        if(g != null)
        {
            if(plugin.getPermissionManager().groups.get(args[4]) != null)
            {
                if(g.getParents().contains(args[4]))
                {
                    g.removeParent(args[4]);
                    sender.sendMessage(Lang.GROUP_PARENT_REMOVE.get(lang, args[4], args[1]));
                }
                else sender.sendMessage(Lang.GROUP_PARENT_CANT_REMOVE.get(lang, args[1], args[4]));
            }
            else sender.sendMessage(Lang.GROUP_NOT_EXIST.get(lang));
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
