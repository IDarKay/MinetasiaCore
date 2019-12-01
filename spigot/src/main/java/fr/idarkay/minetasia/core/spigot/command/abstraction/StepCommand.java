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
import java.util.Collections;
import java.util.List;

/**
 * File <b>StepCommand</b> located on fr.idarkay.minetasia.core.spigot.command.abstraction
 * StepCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 30/11/2019 at 21:39
 */
public abstract class StepCommand extends Command {


    public StepCommand(@NotNull MinetasiaCore plugin, @Nullable Lang description, @NotNull CommandPermission permission, int length) {
        super(plugin, description, permission, length);
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        if(sender.hasPermission(permission.getPermission()))
            for(String m : getUsage(plugin, sender, args, label)) sender.sendMessage(m);
        else
        {
            sender.sendMessage(Lang.NO_PERMISSION.get(getLangOfSender(sender)));
        }
    }

    @Override
    public List<String> tabComplete(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        return getBasicTabCompleter(sender, args);
    }

    public void addChild(Command child)
    {
        this.child.add(child);
    }

}
