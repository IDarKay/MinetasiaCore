package fr.idarkay.minetasia.core.spigot.executor;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * File <b>PermissionExecutor</b> located on fr.idarkay.minetasia.core.spigot.Executor
 * PermissionExecutor is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 30/11/2019 at 23:21
 */
public class CustomCommandExecutor implements TabExecutor {

    private final MinetasiaCore plugin;
    private final CommandManager commandManager;

    public CustomCommandExecutor(MinetasiaCore plugin)
    {
        this.plugin = plugin;
        this.commandManager = plugin.getCommandManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
            commandManager.getCommand(command.getName(), args, false, sender).execute(plugin, sender, args, label));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return commandManager.getCommand(command.getName(), args, true, sender).tabComplete(plugin, sender, args, alias);
    }
}
