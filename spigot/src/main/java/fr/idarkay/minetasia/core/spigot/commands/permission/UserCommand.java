package fr.idarkay.minetasia.core.spigot.commands.permission;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.commands.permission.user.stepcommand.NameCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * File <b>UserCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission
 * UserCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 01/12/2019 at 14:48
 */
public class UserCommand extends StepCommand implements FixCommand {

    public UserCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_USER, CommandPermission.PERMISSION_USER, 2);
        child.add(new NameCommand(plugin));
    }

    @Override
    public String getLabel() {
        return "user";
    }

    @Override
    public List<String> tabComplete(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        List<String> back = getBasicTabCompleter(sender, args); // get create cmd
        back.addAll(getCustomCompleter(sender, args, plugin.getOnlinePlayersForTab(), true)); //get all group name
        return back;
    }

}
