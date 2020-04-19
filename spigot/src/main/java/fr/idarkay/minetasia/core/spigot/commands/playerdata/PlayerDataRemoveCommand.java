package fr.idarkay.minetasia.core.spigot.commands.playerdata;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.user.MinePlayer;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>PlayerDataRemoveCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.playerdata
 * PlayerDataRemoveCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 10/04/2020 at 13:45
 */
public class PlayerDataRemoveCommand extends SubCommand implements FixCommand
{
    public PlayerDataRemoveCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_PLAYER_DATA_PLAYER_REMOVE, CommandPermission.PLAYER_EDIT, 3);
    }

    @Override
    public String getLabel()
    {
        return "remove";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull  String[] args, @NotNull String label)
    {
        final String lang = getLangOfSender(sender);
        final MinePlayer minePlayer = plugin.getPlayerManager().get(args[0]);
        if(minePlayer == null)
        {
            sender.sendMessage(Lang.PLAYER_NOT_EXIST.getWithoutPrefix(lang));
            return;
        }
        if(args.length <= 2)
        {
            if(sender.hasPermission(permission.getPermission()))
                for(String m : getUsage(plugin, sender, args, label)) sender.sendMessage(m);
            else
            {
                sender.sendMessage(Lang.NO_PERMISSION.get(getLangOfSender(sender)));
            }
            return;
        }
        if(minePlayer.getData(args[2]) != null)
        {
            minePlayer.putData(args[2], null);
            sender.sendMessage(Lang.PLAYER_DATA_REMOVE.get(lang));
        }
        else
        {
            sender.sendMessage(Lang.PLAYER_DATA_INVALID_KEY.get(lang));
        }
    }
}
