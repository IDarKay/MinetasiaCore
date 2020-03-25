package fr.idarkay.minetasia.core.spigot.command;

import com.google.common.collect.ImmutableList;
import fr.idarkay.minetasia.core.api.exception.CommandException;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.abstraction.Command;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.commands.NoPermissionCommand;
import fr.idarkay.minetasia.core.spigot.commands.friends.FriendCommand;
import fr.idarkay.minetasia.core.spigot.commands.miscellaneous.Help;
import fr.idarkay.minetasia.core.spigot.commands.money.MoneyCommand;
import fr.idarkay.minetasia.core.spigot.commands.party.PartyCommand;
import fr.idarkay.minetasia.core.spigot.commands.permission.PermissionCommand;
import fr.idarkay.minetasia.core.spigot.commands.tp.TpCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * File <b>CommandManager</b> located on fr.idarkay.minetasia.core.spigot.command
 * CommandManager is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 30/11/2019 at 23:10
 */
public class CommandManager {

    public final NoPermissionCommand np;
    public final List<Command> baseCommand;

    public CommandManager(MinetasiaCore plugin) {
        baseCommand = ImmutableList.<Command>builder()
                .add(new PermissionCommand(plugin))
                .add(new MoneyCommand(plugin))
                .add(new TpCommand(plugin))
                .add(new PartyCommand(plugin))
                .add(new Help(plugin))
                .add(new FriendCommand(plugin))
                .build();
        np = new NoPermissionCommand(plugin);
    }

    public Command getCommand(String label, String[] args, boolean forTab, CommandSender sender)
    {
        // get the base command
        Command base = null;
        for(Command c : baseCommand)
        {
            if(c.getLabel().equals(label)){
                if(sender.hasPermission(c.getPermission().getPermission()))
                {
                    base = c;
                    break;
                } else return np;
            }
        }
        if(base == null) throw new CommandException("Can't find command " + label); // if the command isn't in the list

        Command back = base;
        int r = 1;
        //get the command
        for(String s : args)
        {
            if(forTab && args.length == r) break;
            r++;
            boolean up = false;
            for(Command c : back.getChild())
            {
                if(c instanceof FixCommand)
                {
                    if(c.getLabel().equalsIgnoreCase(s) && sender.hasPermission(c.getPermission().getPermission()))
                    {
                        back = c;
                        up = true;
                        break;
                    }
                } else if(c instanceof FlexibleCommand)
                {
                    if(sender.hasPermission(c.getPermission().getPermission()) && (((FlexibleCommand) c).isAllPossibilities() || containsIgnoreCase(((FlexibleCommand) c).getPossibilities(), s)))
                    {
                        back = c;
                        up = true;
                        break;
                    }
                }
            }
            if(!up) {
                return back;
            }
        }
        return back;
    }


    private boolean containsIgnoreCase(List<String> list, String element)
    {
        if(list.contains(element)) return true;

        for(String s : list) if (s.equalsIgnoreCase(element)) return true;

        return false;
    }

}
