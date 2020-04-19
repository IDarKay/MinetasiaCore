package fr.idarkay.minetasia.core.spigot.command.abstraction;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * File <b>Command</b> located on fr.idarkay.minetasia.core.spigot.command.abstraction
 * Command is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
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
        final String cmd = "/" + label + " " + concat(args, " ", 0, Math.max(length - 1, 0));

        //add all children command
        final String border = Lang.COMMAND_HELP_BORDER.getWithoutPrefix(lang, Lang.Argument.COMMAND.match(cmd));
        u.add(border);
        u.add(" ");
        final String format = Lang.COMMAND_HELP_FORMAT.getWithoutPrefix(lang);
        for (Command v : child) {
            if(sender.hasPermission(v.permission.getPermission()))
            {
                u.add(format.replace(Lang.Argument.COMMAND.toString(), cmd + v.getLabel()).replace(Lang.Argument.DESCRIPTION.toString(), (v.getDescription() == null ? " " : v.getDescription().getWithoutPrefix(lang))));
            }
        }
        u.add(" ");
        u.add(border);
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
            getChild().forEach(command -> {
                if(sender.hasPermission(command.permission.getPermission())){
                    if(command.getLabel().startsWith(args[length - 1].toLowerCase()))
                        b.add(command.getLabel());
                    if(command instanceof FlexibleCommand && ((FlexibleCommand) command).getPossibilities() != null)
                        b.addAll(((FlexibleCommand) command).getPossibilities().stream().filter(s -> s.toLowerCase().startsWith(args[length -1].toLowerCase())).collect(Collectors.toList()));
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
            if(!filter) return new ArrayList<>(elements);
            return elements.stream().filter(value -> value.toLowerCase().startsWith(args[length - 1].toLowerCase())).collect(Collectors.toList());
//            final List<String> b = new ArrayList<>();
//            elements.forEach(v -> {
//                if (!filter
//                        || args.length < length
//                        || v.equalsIgnoreCase(args[length -1])
//                        || v.toLowerCase().startsWith(args[length - 1].toLowerCase()))
//                    b.add(v);
//            });
//            return b;
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
