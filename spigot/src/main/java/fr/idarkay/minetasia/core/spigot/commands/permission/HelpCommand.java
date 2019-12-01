package fr.idarkay.minetasia.core.spigot.commands.permission;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.Command;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * File <b>HelpCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission
 * HelpCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 01/12/2019 at 14:27
 */
public class HelpCommand extends SubCommand implements FixCommand {

    public HelpCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_HELP, CommandPermission.PERMISSION_HELP, 2);
    }

    @Override
    public String getLabel() {
        return "help";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        Command c = plugin.getCommandManager().getCommand(label, new String[] {}, false, sender);
        getChild(c, new ArrayList<>(), plugin, sender, args, label);
    }

    private void getChild( Command c, List<String> l, @NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label)
    {
        for(Command ch : c.getChild())
        {
            if(sender.hasPermission(ch.getPermission().getPermission()))
            {
                for(String m : getUsage(plugin, sender, args, label)) sender.sendMessage(m);
                getChild(ch, l, plugin, sender, args, label);
            }
        }
    }

}
