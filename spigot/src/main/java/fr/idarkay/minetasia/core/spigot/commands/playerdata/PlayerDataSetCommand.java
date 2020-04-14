package fr.idarkay.minetasia.core.spigot.commands.playerdata;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.user.MinePlayer;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.utils.GeneralUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>PlayerDataSetCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.playerdata
 * PlayerDataSetCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 10/04/2020 at 13:45
 */
public class PlayerDataSetCommand extends SubCommand implements FixCommand
{
    public PlayerDataSetCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_PLAYER_DATA_PLAYER_SET, CommandPermission.PLAYER_EDIT, 3);
        child.add(this);
    }

    @Override
    public String getLabel()
    {
        return "set";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label)
    {
        final String lang = getLangOfSender(sender);
        final MinePlayer minePlayer = plugin.getPlayerManager().get(args[0]);
        if (minePlayer == null)
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
        if(args.length > 3)
        {
            final String value = GeneralUtils.concat(args, " ", 3);
            minePlayer.putData(args[2], value);
            sender.sendMessage(Lang.PLAYER_DATA_SET.get(lang));
        }
        else
        {
            sender.sendMessage(Lang.PLAYER_DATA_SET_NO_VALUE.get(lang));
        }
    }
}