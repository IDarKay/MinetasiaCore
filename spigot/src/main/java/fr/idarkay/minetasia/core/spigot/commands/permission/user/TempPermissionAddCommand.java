package fr.idarkay.minetasia.core.spigot.commands.permission.user;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * File <b>TempPermissionAddCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.user
 * TempPermissionAddCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 01/12/2019 at 15:19
 */
public class TempPermissionAddCommand extends SubCommand implements FlexibleCommand {

    public TempPermissionAddCommand(@NotNull MinetasiaCore plugin) {
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
            String permission = args[length - 3];
            Long during = Long.parseLong(args[length -2]);
            during *= 60_000L;
            if(permission.length() == 0)
            {
                sender.sendMessage(Lang.GROUP_NO_ENOUGH_CHAR.get(lang, Lang.Argument.NUMBER.match(1)));
                return;
            }
            UUID u = plugin.getPlayerUUID(args[1]);
            if(u != null)
            {
                plugin.getPermissionManager().addTempPermission(u, permission, during);
                sender.sendMessage(Lang.USER_PERMISSION_ADD.get(lang, Lang.Argument.PERMISSION_NAME.match(permission), Lang.Argument.PLAYER.match(args[1])));
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
