package fr.idarkay.minetasia.core.spigot.command.abstraction;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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
            final List<String> b = new ArrayList<>();
            getChild().forEach(v -> {
                if(sender.hasPermission(v.permission.getPermission()))
                    b.add(v.getLabel());
            });
            return b;
        }
        else return Collections.emptyList();
    }

    // usage of the command
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label)
    {
        final String lang = getLangOfSender(sender);
        if(sender.hasPermission(permission.getPermission()))
        {
            //get already enter cmd
            final String cmd = "/" + label;
            sender.sendMessage(ChatColor.RED + "-----------" + cmd + "-----------");
            for (Command v : child)
            {
                if(sender.hasPermission(v.permission.getPermission()))
                {
                    StringBuilder l = new StringBuilder();
                    l.append(ChatColor.AQUA).append(cmd).append(" ");
                    l.append(getLabel());
                    if (v.getDescription() != null) {
                        l.append(" ; ").append(ChatColor.GREEN);
                        l.append(v.getDescription().getWithoutPrefix(lang));
                    }
                    sender.sendMessage(l.toString());
                }
            }
        }
        else sender.sendMessage(Lang.NO_PERMISSION.get(lang));
    }

    public void addChild(Command child)
    {
        this.child.add(child);
    }

}
