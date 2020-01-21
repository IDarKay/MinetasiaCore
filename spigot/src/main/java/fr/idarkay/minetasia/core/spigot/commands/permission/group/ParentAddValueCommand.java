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
 * File <b>ParentAddValueCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.group
 * ParentAddValueCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 01/12/2019 at 14:05
 */
public class ParentAddValueCommand extends SubCommand implements FlexibleCommand {

    public ParentAddValueCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_GROUP_PARENT_ADD_VALUE, CommandPermission.PERMISSION_GROUP_PARENT, 6);
    }

    @Override
    public String getLabel() {
        return "<group>";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        final String lang = getLangOfSender(sender);
        Group g = plugin.getPermissionManager().groups.get(args[1]);
        if(g != null)
        {
            if(plugin.getPermissionManager().groups.get(args[4]) != null)
            {
                if(!args[1].equalsIgnoreCase(args[4]) && !g.getParents().contains(args[4]) && noRecursive(g, args[4]))
                {
                    g.addParents(args[4]);
                    sender.sendMessage(Lang.GROUP_PARENT_ADD.get(lang, Lang.Argument.GROUP_PARENT.match( args[4]), Lang.Argument.GROUP_NAME.match( args[1])));
                }
                else sender.sendMessage(Lang.GROUP_PARENT_CANT_ADD.get(lang, Lang.Argument.GROUP_PARENT.match( args[4]), Lang.Argument.GROUP_NAME.match( args[1])));

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

    private boolean noRecursive(Group g, String todd)
    {
        if(g != null)
        {
            for(String gr : g.getParents())
            {
                if(gr.equalsIgnoreCase(todd)) return false;
                if(!noRecursive(plugin.getPermissionManager().groups.get(gr), todd)) return false;
            }
        }
        return true;
    }

}
