package fr.idarkay.minetasia.core.spigot.commands.permission;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>PermissionListCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission
 * PermissionListCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 01/12/2019 at 14:40
 */
public class PermissionListCommand extends SubCommand implements FixCommand {

    public PermissionListCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_LIST, CommandPermission.PERMISSION_PERMISSION_LIST, 2);
    }

    @Override
    public String getLabel() {
        return "permissionlist";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        for(CommandPermission c : CommandPermission.values())
        {
            if(c.name().startsWith("PERMISSION") || c.name().startsWith("ALL_PERMISSION"))
            {
                sender.sendMessage(ChatColor.AQUA + c.getPermission() + " : " + ChatColor.GREEN + c.getDescription());
            }
        }
    }
}
