package fr.idarkay.minetasia.core.spigot.commands.permission.common;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * File <b>StepAddCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.common
 * StepAddCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 01/12/2019 at 12:03
 */
public final class StepAddCommand extends StepCommand implements FixCommand {

    private List<String> perm;

    public StepAddCommand(@NotNull MinetasiaCore plugin, @Nullable Lang description, @NotNull CommandPermission permission, int length) {
        super(plugin, description, permission, length);
    }

    @Override
    public String getLabel() {
        return "add";
    }

    @Override
    public List<String> tabComplete(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        if(sender.hasPermission(permission.getPermission()))
        {
            if(perm == null)
            {
                perm = plugin.getServer().getPluginManager().getPermissions().stream().map(Permission::getName).collect(Collectors.toList());
                perm.addAll(getBasicTabCompleter(sender, args));
            }
            return getMultiCompleter(sender, args, perm, true);
        }
        else return Collections.emptyList();
    }
}
