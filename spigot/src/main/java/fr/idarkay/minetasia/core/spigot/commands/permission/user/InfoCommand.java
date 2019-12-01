package fr.idarkay.minetasia.core.spigot.commands.permission.user;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * File <b>InfoCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.user
 * InfoCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 01/12/2019 at 15:05
 */
public class InfoCommand extends SubCommand implements FixCommand {

    public InfoCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_USER_INFO, CommandPermission.PERMISSION_USER_INFO, 4);
    }

    @Override
    public String getLabel() {
        return "info";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        final String lang = getLangOfSender(sender);
        UUID u = plugin.getPlayerUUID(args[1]);
        if(u != null)
        {
            sender.sendMessage(ChatColor.RED + "-----------" + args[1] + "-----------");
            sender.sendMessage(ChatColor.AQUA + "Name : " + ChatColor.GREEN + args[1]);
            sender.sendMessage(ChatColor.AQUA + "Permission :");
            for(String s : plugin.getPermissionManager().getPermissionOfUser(u)) sender.sendMessage("\t- " + ChatColor.GREEN + s);
            sender.sendMessage(ChatColor.AQUA + "Temp permission :");
            for(String s : plugin.getPermissionManager().getTempPermissionOfUser(u)) sender.sendMessage("\t- " + ChatColor.GREEN + s);
            sender.sendMessage(ChatColor.AQUA + "Group :");
            for(String s : plugin.getPermissionManager().getGroupOfUser(u)) sender.sendMessage("\t- " + ChatColor.GREEN + s);
            sender.sendMessage(ChatColor.AQUA + "Temp group :");
            for(String s : plugin.getPermissionManager().getTempGroupOfUser(u)) sender.sendMessage("\t- " + ChatColor.GREEN + s);

        }
        else sender.sendMessage(Lang.PLAYER_NOT_EXIST.get(lang));
    }
}
