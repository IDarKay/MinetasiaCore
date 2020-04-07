package fr.idarkay.minetasia.core.spigot.executor.sanction;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.moderation.PlayerSanction;
import fr.idarkay.minetasia.core.spigot.moderation.SanctionType;
import fr.idarkay.minetasia.core.spigot.user.MinePlayer;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import fr.idarkay.minetasia.normes.utils.GeneralUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * File <b>AdminSanctionCommand</b> located on fr.idarkay.minetasia.core.spigot.executor.sanction
 * AdminSanctionCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 04/04/2020 at 02:53
 */
public class AdminSanctionCommand implements TabExecutor
{

    private final CommandPermission perm;
    private final MinetasiaCore plugin;
    private final SanctionType sanctionType;

    public AdminSanctionCommand(MinetasiaCore plugin, CommandPermission permission, SanctionType sanctionType)
    {
        this.perm = permission;
        this.plugin = plugin;
        this.sanctionType = sanctionType;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {
        final String lang = commandSender instanceof Player ? plugin.getPlayerLang(((Player) commandSender).getUniqueId()) : MinetasiaLang.BASE_LANG;
        if(commandSender.hasPermission(perm.getPermission()))
        {
            if(args.length < 4)
            {
                commandSender.sendMessage(ChatColor.RED + "/" + command.getLabel() + "<pseudo> <time_units> <during> <reason>");
                return true;
            }

            try
            {
                final TimeUnit timeUnit = TimeUnit.valueOf(args[1]);
                final long during = Long.parseLong(args[2]);
                final String reason = GeneralUtils.concat(args, " ", 3);
                final MinePlayer minePlayer = plugin.getPlayerManager().get(args[0]);
                if(minePlayer == null)
                {
                    commandSender.sendMessage(Lang.PLAYER_NOT_EXIST.get(lang));
                    return true;
                }
                plugin.applySanctionToPlayerServerBound(
                        minePlayer,
                        new PlayerSanction(sanctionType, timeUnit.toMillis(during), commandSender instanceof Player ? ((Player) commandSender).getUniqueId() : UUID.randomUUID(), commandSender.getName(),"admin", reason,  timeUnit)
                );
                commandSender.sendMessage(Lang.SANCTION_COMMAND_END.get(lang
                        , Lang.Argument.SANCTION_TYPE.match(sanctionType.name().toLowerCase())
                        , Lang.Argument.PLAYER.match(args[0])
                        , Lang.Argument.TIME.match(during + " " + timeUnit.name().toLowerCase())
                        , Lang.Argument.REASON.match(reason)
                ));

            } catch (NumberFormatException ignore)
            {
                commandSender.sendMessage(Lang.ILLEGAL_NUMBER_VALUE.get(lang));
            }
            catch (IllegalArgumentException ignore)
            {
                commandSender.sendMessage(Lang.ILLEGAL_TIME_UNITS.get(lang));
            }


        }
        else
            commandSender.sendMessage(Lang.NO_PERMISSION.get(lang));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull  String[] args)
    {
        if(commandSender.hasPermission(perm.getPermission()))
        {
            if(args.length == 0)
            {
                return plugin.getOnlinePlayersForTab();
            }
            else if(args.length == 1)
            {
                return plugin.getOnlinePlayersForTab().stream().filter(player -> player.startsWith(args[0])).collect(Collectors.toList());
            }
            else if(args.length == 2)
            {
                return Arrays.stream(TimeUnit.values()).map(Enum::name).filter(timeUnit -> timeUnit.startsWith(args[1].toUpperCase())).collect(Collectors.toList());
            }
            else if(args.length == 3)
            {
                return Collections.singletonList("<during>");
            }
            else if(args.length == 4)
            {
                return Collections.singletonList("<reason>");
            }
            else
                return Collections.emptyList();
        }
        return null;
    }
}
