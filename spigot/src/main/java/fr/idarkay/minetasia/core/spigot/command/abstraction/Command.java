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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * File <b>Command</b> located on fr.idarkay.minetasia.core.spigot.command.abstraction
 * Command is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 30/11/2019 at 17:37
 */
public abstract class Command {

    protected final @NotNull List<Command> child;
    protected final @Nullable Lang description;
    protected final @NotNull CommandPermission permission;
    public final int length;
    protected final @NotNull MinetasiaCore plugin;

    public Command(@NotNull MinetasiaCore plugin, @Nullable Lang description, @NotNull CommandPermission permission, int length)
    {
        child = new ArrayList<>();
        this.description = description;
        this.permission = permission;
        this.length = length;
        this.plugin = plugin;
    }

    @NotNull
    public CommandPermission getPermission() {
        return permission;
    }

    @Nullable
    public Lang getDescription() {
        return description;
    }

    @NotNull
    public List<Command> getChild() {
        return child;
    }

    public abstract String getLabel();

    public abstract void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label);

    public abstract List<String> tabComplete(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label);

    // usage of the command
    public List<String> getUsage(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label)
    {
        final List<String> u = new ArrayList<>();
        final String lang = getLangOfSender(sender);

        //get already enter cmd
        final String cmd = "/" + label + " " + concat(args, " ", 0, length - 2);

        //add all children command
        u.add(ChatColor.RED + "-----------" + cmd + "-----------");
        for (Command v : child) {
            if(sender.hasPermission(v.permission.getPermission()))
            {
                StringBuilder l = new StringBuilder();
                l.append(ChatColor.AQUA).append(cmd).append(" ");
                l.append(v.getLabel());
                if (v.getDescription() != null) {
                    l.append(" : ").append(ChatColor.GREEN);
                    l.append(v.getDescription().getWithoutPrefix(lang));
                }
                u.add(l.toString());
            }
        }
        return u;
    }

    protected List<String> getMultiCompleter(@NotNull CommandSender sender, @NotNull String[] args, Collection<String> elements, boolean filter)
    {
        List<String> back = getBasicTabCompleter(sender, args); // get create cmd
        back.addAll(getCustomCompleter(sender, args, elements, filter)); //get all group name
        return back;
    }

    protected List<String> getBasicTabCompleter( @NotNull CommandSender sender,  @NotNull String[] args)
    {
        if(sender.hasPermission(permission.getPermission()))
        {
            final List<String> b = new ArrayList<>();
            getChild().forEach(v -> {
                if(sender.hasPermission(v.permission.getPermission())){
                    if (v instanceof FlexibleCommand
                            || args.length < length
                            || v.getLabel().equalsIgnoreCase(args[length -1])
                            || v.getLabel().startsWith(args[length - 1].toLowerCase())  )
                        b.add(v.getLabel());
                }

            });
            return b;
        }
        else return Collections.emptyList();
    }

    protected List<String> getCustomCompleter(@NotNull CommandSender sender, @NotNull String[] args, Collection<String> elements, boolean filter)
    {
        if(sender.hasPermission(permission.getPermission()))
        {
            final List<String> b = new ArrayList<>();
            elements.forEach(v -> {
                if (!filter
                        || args.length < length
                        || v.equalsIgnoreCase(args[length -1])
                        || v.toLowerCase().startsWith(args[length - 1].toLowerCase()))
                    b.add(v);
            });
            return b;
        }
        else return Collections.emptyList();
    }

    protected String concat(String[] array, String split, int indexFrom, int indexEnd)
    {
        StringBuilder result = new StringBuilder();
        for(int i = indexFrom; i < (indexEnd < 0 ? array.length : indexEnd); i++) result.append(i != indexFrom && split != null ? split : "").append(array[i]);
        return result.toString();
    }

    protected String getLangOfSender(CommandSender sender)
    {
        return sender instanceof Player ? plugin.getPlayerLang(((Player) sender).getUniqueId()) : MinetasiaLang.BASE_LANG;
    }

}