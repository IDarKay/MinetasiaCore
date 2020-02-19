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
 * File <b>DefaultCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.group
 * DefaultCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 19/02/2020 at 14:57
 */
public class DefaultCommand extends SubCommand implements FixCommand
{
    public DefaultCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_PERMISSION_GROUP_DEFAULT, CommandPermission.PERMISSION_GROUP_DEFAULT, 4);
    }

    @Override
    public String getLabel()
    {
        return "setdefault";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label)
    {
        final String lang = getLangOfSender(sender);
        final Group g = plugin.getPermissionManager().groups.get(args[1]);
        if(g != null)
        {
            g.setDefault(!g.isDefault());
        }
        else sender.sendMessage(Lang.GROUP_NOT_EXIST.get(lang));
    }
}
