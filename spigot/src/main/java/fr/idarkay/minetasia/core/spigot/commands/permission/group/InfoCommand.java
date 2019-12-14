package fr.idarkay.minetasia.core.spigot.commands.permission.group;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.permission.Group;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>InfoCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.group
 * InfoCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 01/12/2019 at 11:49
 */
public final class InfoCommand extends SubCommand implements FixCommand {

    public InfoCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_GROUP_INFO, CommandPermission.PERMISSION_GROUP_INFO, 4);
    }

    @Override
    public String getLabel() {
        return "info";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        final String lang = getLangOfSender(sender);
        Group g = plugin.getPermissionManager().groups.get(args[1]);
        if(g != null)
        {
            sender.sendMessage(ChatColor.RED + "-----------" + g.getName() + "-----------");
            sender.sendMessage(ChatColor.AQUA + "name : " + ChatColor.GREEN + g.getName());
            sender.sendMessage(ChatColor.AQUA + "display name : " + ChatColor.GREEN + ChatColor.translateAlternateColorCodes('&', g.getDisplayName()));
            sender.sendMessage(ChatColor.AQUA + "priority : " + ChatColor.GREEN + g.getPriority());
            sender.sendMessage(ChatColor.AQUA + "permission :");
            for(String p : g.getPermissions()) sender.sendMessage("\t- " + ChatColor.GREEN + p);
            sender.sendMessage(ChatColor.AQUA + "parents :");
            for(String p : g.getParents()) sender.sendMessage("\t- " + ChatColor.GREEN + p);

        }
        else sender.sendMessage(Lang.GROUP_NOT_EXIST.get(lang));

    }

}
