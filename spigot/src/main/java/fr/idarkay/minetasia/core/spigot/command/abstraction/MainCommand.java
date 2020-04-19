package fr.idarkay.minetasia.core.spigot.command.abstraction;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * File <b>MainCommand</b> located on fr.idarkay.minetasia.core.spigot.command.abstraction
 * MainCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 30/11/2019 at 23:05
 */
public abstract class MainCommand extends Command {

    public MainCommand(@NotNull MinetasiaCore plugin, @Nullable Lang description, @NotNull CommandPermission permission, int length) {
        super(plugin, description, permission, length);
    }

    @Override
    public List<String> tabComplete(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        if(sender.hasPermission(permission.getPermission()))
        {
            return getBasicTabCompleter(sender, args);
        }
        else return Collections.emptyList();
    }

    // usage of the command
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label)
    {
        if(sender.hasPermission(permission.getPermission()))
            for(String m : getUsage(plugin, sender, args, label)) sender.sendMessage(m);
        else
        {
            sender.sendMessage(Lang.NO_PERMISSION.get(getLangOfSender(sender)));
        }
    }

    public void addChild(Command child)
    {
        this.child.add(child);
    }

}
