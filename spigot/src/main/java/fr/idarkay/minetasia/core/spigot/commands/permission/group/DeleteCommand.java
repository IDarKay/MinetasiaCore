package fr.idarkay.minetasia.core.spigot.commands.permission.group;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.permission.Group;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * File <b>DeleteCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.group
 * DeleteCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 01/12/2019 at 18:36
 */
public class DeleteCommand extends SubCommand implements FixCommand {

    public DeleteCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_GROUP_DELETE, CommandPermission.PERMISSION_GROUP_DELETE, 4);
    }

    @Override
    public String getLabel() {
        return "delete";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        final String lang = getLangOfSender(sender);
        Group g = plugin.getPermissionManager().groups.get(args[1]);
        if(g != null)
        {
            plugin.getPermissionManager().removeGroup(g.getName());
            sender.sendMessage(Lang.GROUP_DELETE.get(lang, args[1]));
        }
        else sender.sendMessage(Lang.GROUP_NOT_EXIST.get(lang));
    }
}
