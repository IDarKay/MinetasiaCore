package fr.idarkay.minetasia.core.spigot.commands.permission.group;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.permission.Group;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * File <b>DisplayNameCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.group
 * DisplayNameCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 01/12/2019 at 10:58
 */
public final class DisplayNameCommand extends SubCommand implements FlexibleCommand {


    public DisplayNameCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_GROUP_DISPLAY_NAME, CommandPermission.PERMISSION_GROUP_DISPLAY, 5);
    }

    @Override
    public String getLabel() {
        return "<new display name>";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        final String lang = getLangOfSender(sender);
        if(sender.hasPermission(permission.getPermission()))
        {
            Group g = plugin.getPermissionManager().groups.get(args[1]);
            if(g != null)
            {
                String name = concat(args, " ", 3, -1);
                if(name.trim().length() > 0 )
                {
                    g.setDisplayName(name);
                    sender.sendMessage(Lang.GROUP_DISPLAY_CHANGE.get(lang, args[1], name));
                }
                else sender.sendMessage(Lang.GROUP_NO_ENOUGH_CHAR.get(lang, 1));
            }
            else sender.sendMessage(Lang.GROUP_NOT_EXIST.get(lang));
        }
        else sender.sendMessage(Lang.NO_PERMISSION.get(lang));
    }

    @Override
    public List<String> tabComplete(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        return Collections.singletonList(getLabel());
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
