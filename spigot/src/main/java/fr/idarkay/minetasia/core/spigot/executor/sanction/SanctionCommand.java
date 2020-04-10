package fr.idarkay.minetasia.core.spigot.executor.sanction;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.moderation.PlayerSanction;
import fr.idarkay.minetasia.core.spigot.moderation.Sanction;
import fr.idarkay.minetasia.core.spigot.user.MinePlayer;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * File <b>SanctionCommand</b> located on fr.idarkay.minetasia.core.spigot.executor
 * SanctionCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 03/04/2020 at 22:17
 */
public class SanctionCommand implements TabExecutor
{

    @NotNull
    private final MinetasiaCore core;

    public SanctionCommand(@NotNull MinetasiaCore core)
    {
        this.core = core;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {
        final String lang = commandSender instanceof Player ? core.getPlayerLang(((Player) commandSender).getUniqueId()) : MinetasiaLang.BASE_LANG;
        if(commandSender.hasPermission(CommandPermission.SANCTION.getPermission()))
        {
            if(args.length == 1 || args.length == 2)
            {
                final MinePlayer minePlayer = core.getPlayerManager().get(args[0]);
                if(minePlayer == null)
                {
                    commandSender.sendMessage(Lang.PLAYER_NOT_EXIST.get(lang));
                    return true;
                }
                if(args.length == 2)
                {
                    final Sanction sanction = core.getSanctionManger().getSanction(args[1]);
                    if(sanction == null)
                    {
                        commandSender.sendMessage(Lang.WRONG_SANCTION.get(lang));
                        return true;
                    }

                    final PlayerSanction playerSanction = new PlayerSanction(minePlayer, sanction, (commandSender instanceof Player ? ((Player) commandSender).getUniqueId() : UUID.randomUUID()), commandSender.getName());

                    core.applySanctionToPlayerServerBound(
                            minePlayer,
                            playerSanction
                    );
                    commandSender.sendMessage(Lang.SANCTION_COMMAND_END.get(lang
                            , Lang.Argument.SANCTION_TYPE.match(playerSanction.sanctionType.name().toLowerCase())
                            , Lang.Argument.PLAYER.match(args[0])
                            , Lang.Argument.TIME.match(TimeUnit.MILLISECONDS.convert(playerSanction.during, playerSanction.baseTimeUnit) + " " + playerSanction.baseTimeUnit.name().toLowerCase())
                            , Lang.Argument.REASON.match(playerSanction.reason)
                            ));
                }
                else
                {
                    if(commandSender instanceof Player)
                        core.getGui().sanctionHistoryGUI.open((Player) commandSender, minePlayer);
                    else if(commandSender instanceof ConsoleCommandSender)
                    {
                        //for console send raw txt no gui
                        minePlayer.getHistory().forEach(document -> commandSender.sendMessage(command.toString()));
                    }
                }
            }
            else commandSender.sendMessage(ChatColor.RED + "/sanction <name> [sanction_name]");
        }
        else commandSender.sendMessage(Lang.NO_PERMISSION.get(lang));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {
        if(commandSender.hasPermission(CommandPermission.SANCTION.getPermission()))
        {
            if(args.length == 0)
            {
                return core.getOnlinePlayersForTab();
            }
            else if(args.length == 1)
            {
                return core.getOnlinePlayersForTab().stream().filter(player -> player.startsWith(args[0])).collect(Collectors.toList());
            }
            else if(args.length == 2)
            {
                return core.getSanctionManger().getSanctions().stream().filter(sanction -> sanction.startsWith(args[1])).collect(Collectors.toList());
            }
            else
                return Collections.emptyList();
        }
        return null;
    }
}
