package fr.idarkay.minetasia.core.spigot.commands;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>NoPermissionCommand</b> located on fr.idarkay.minetasia.core.spigot.commands
 * NoPermissionCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 01/12/2019 at 13:20
 */
public class NoPermissionCommand extends SubCommand implements FixCommand {

    public NoPermissionCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, null, CommandPermission.NO_PERMISSION, 0);
    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        sender.sendMessage(Lang.NO_PERMISSION.get(getLangOfSender(sender)));
    }
}
