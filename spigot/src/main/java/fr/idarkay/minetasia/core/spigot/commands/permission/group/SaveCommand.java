package fr.idarkay.minetasia.core.spigot.commands.permission.group;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.permission.Group;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>SaveCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.group
 * SaveCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 01/12/2019 at 11:39
 */
public final class SaveCommand extends SubCommand implements FixCommand {

    public SaveCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_GROUP_SAVE, CommandPermission.PERMISSION_GROUP_SAVE, 4);
    }

    @Override
    public String getLabel() {
        return "save";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        final String lang = getLangOfSender(sender);
        if(sender.hasPermission(permission.getPermission()))
        {
            Group g = plugin.getPermissionManager().groups.get(args[1]);
            if(g != null)
            {
                plugin.getPermissionManager().updateGroupToFRS(g);
                sender.sendMessage(Lang.GROUP_SAVE.get(lang, Lang.Argument.GROUP_NAME.match(args[1])));
            }
            else sender.sendMessage(Lang.GROUP_NOT_EXIST.get(lang));
        }
        else sender.sendMessage(Lang.NO_PERMISSION.get(lang));
    }
}
