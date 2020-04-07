package fr.idarkay.minetasia.core.spigot.executor.sanction;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.moderation.PlayerSanction;
import fr.idarkay.minetasia.core.spigot.moderation.SanctionType;
import fr.idarkay.minetasia.core.spigot.user.MinePlayer;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * File <b>UnSanctionCommand</b> located on fr.idarkay.minetasia.core.spigot.executor.sanction
 * UnSanctionCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 04/04/2020 at 14:53
 */
public class UnSanctionCommand implements TabExecutor
{

    private final CommandPermission perm;
    private final MinetasiaCore plugin;
    private final SanctionType sanctionType;

    public UnSanctionCommand(MinetasiaCore plugin, CommandPermission permission, SanctionType sanctionType)
    {
        this.perm = permission;
        this.plugin = plugin;
        this.sanctionType = sanctionType;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull  String[] args)
    {
        final String lang = commandSender instanceof Player ? plugin.getPlayerLang(((Player) commandSender).getUniqueId()) : MinetasiaLang.BASE_LANG;
        if(commandSender.hasPermission(perm.getPermission()))
        {
            if(args.length < 1)
            {
                commandSender.sendMessage(ChatColor.RED + "/" + command.getLabel() + "<pseudo> <time_units> <during> <reason>");
                return true;
            }

                final MinePlayer minePlayer = plugin.getPlayerManager().get(args[0]);
                if(minePlayer == null)
                {
                    commandSender.sendMessage(Lang.PLAYER_NOT_EXIST.get(lang));
                    return true;
                }
            final PlayerSanction sanction = minePlayer.getSanction(sanctionType);
            if(sanction == null || sanction.isEnd())
            {
                commandSender.sendMessage(Lang.UN_SANCTION_NOT_SANCTION.get(lang, Lang.Argument.SANCTION_TYPE.match(sanctionType.name().toLowerCase())));
                return true;
            }

            minePlayer.unsetSanction(sanction, true);
            commandSender.sendMessage(Lang.UN_SANCTION_END.get(lang, Lang.Argument.SANCTION_TYPE.match(sanctionType.name().toLowerCase()),
                    Lang.Argument.PLAYER.match(args[0])));

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
            else
                return Collections.emptyList();
        }
        return null;
    }
}
