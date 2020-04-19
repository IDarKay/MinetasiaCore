package fr.idarkay.minetasia.core.spigot.commands.playerdata;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.MainCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * File <b>PlayerDataCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.playerdata
 * PlayerDataCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 10/04/2020 at 13:32
 */
public class PlayerDataCommand extends MainCommand implements FixCommand
{
    public PlayerDataCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, null, CommandPermission.PLAYER_DATA, 1);
        addChild(new PlayerDataPlayerCommand(plugin));
    }

    @Override
    public String getLabel()
    {
        return "playerdata";
    }

    @Override
    public List<String> tabComplete(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        List<String> back = getBasicTabCompleter(sender, args); // get create cmd
        back.addAll(getCustomCompleter(sender, args, plugin.getOnlinePlayersForTab(), true)); //get all group name
        return back;
    }
}
