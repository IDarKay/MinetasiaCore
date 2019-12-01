package fr.idarkay.minetasia.core.spigot.commands.permission.user;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.permission.Group;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * File <b>TempGroupAddCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.user
 * TempGroupAddCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 01/12/2019 at 16:10
 */
public class TempGroupAddCommand extends SubCommand implements FlexibleCommand {

    public TempGroupAddCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_USER_PERMISSION_ADD_VALUE_TEMP, CommandPermission.PERMISSION_USER_PERMISSION, 7);
    }

    @Override
    public String getLabel() {
        return "<time in min>";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        final String lang = getLangOfSender(sender);
        try
        {
            Group group = plugin.getPermissionManager().groups.get(args[length - 3]);
            Long during = Long.parseLong(args[length -2]);
            if (during < 1) throw new IllegalArgumentException();
            during *= 60_000L;
            if(group == null)
            {
                sender.sendMessage(Lang.GROUP_NOT_EXIST.get(lang));
                return;
            }
            UUID u = plugin.getPlayerUUID(args[1]);
            if(u != null)
            {
                plugin.getPermissionManager().addTempGroup(u, group.getName(), during);
                sender.sendMessage(Lang.USER_GROUP_ADD.get(lang, permission, args[1]));
            }
            else sender.sendMessage(Lang.PLAYER_NOT_EXIST.get(lang));
        } catch (IllegalArgumentException ignore)
        {
            sender.sendMessage(Lang.ILLEGAL_NUMBER_VALUE.get(lang));
        }

    }

    @Override
    public boolean isAllPossibilities() {
        return true;
    }

    @Override
    public List<String> getPossibilities() {
        return null;
    }

}
