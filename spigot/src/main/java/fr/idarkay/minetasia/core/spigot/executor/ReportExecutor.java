package fr.idarkay.minetasia.core.spigot.executor;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.messages.CoreMessage;
import fr.idarkay.minetasia.core.spigot.messages.ReportMessage;
import fr.idarkay.minetasia.core.spigot.moderation.report.MainReportBook;
import fr.idarkay.minetasia.core.spigot.moderation.report.ReportType;
import fr.idarkay.minetasia.core.spigot.moderation.report.TypeReportBook;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * File <b>ReportExecutor</b> located on fr.idarkay.minetasia.core.spigot.executor
 * ReportExecutor is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 08/04/2020 at 23:39
 */
public class ReportExecutor implements TabExecutor
{
    private final MinetasiaCore plugin;

    public ReportExecutor(MinetasiaCore minetasiaCore)
    {
        this.plugin = minetasiaCore;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,  @NotNull String[] args)
    {
        final String lang = commandSender instanceof Player ? plugin.getPlayerLang(((Player) commandSender).getUniqueId()) : MinetasiaLang.BASE_LANG;
        if(!commandSender.hasPermission(CommandPermission.REPORT.getPermission()))
        {
            commandSender.sendMessage(Lang.NO_PERMISSION.get(lang));
            return true;
        }
        if(args.length > 0 && commandSender instanceof Player && !"stop".equalsIgnoreCase(args[0]) && commandSender.getName().equalsIgnoreCase(args[0]))
        {
            commandSender.sendMessage(Lang.REPORT_SELF.get(lang));
            return true;
        }
        if(args.length == 0)
        {
            commandSender.sendMessage(ChatColor.RED + "/report <player>");
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if(args.length == 1)
            {
                if(!(commandSender instanceof Player))
                {
                    commandSender.sendMessage("can't sur this cmd without be a player use full cmd /report <player> <type> <args> btw if you use console you know it :)");
                    return;
                }
                if(args[0].equalsIgnoreCase("stop"))
                {
                    return;
                }
                if(plugin.getPlayerUUID(args[0]) != null)
                {
                    new MainReportBook(lang, args[0]).open((Player) commandSender);
                }
                else
                {
                    commandSender.sendMessage(Lang.PLAYER_NOT_EXIST.get(lang));
                }
            }
            else if(args.length == 2)
            {
                if(!(commandSender instanceof Player))
                {
                    commandSender.sendMessage("can't sur this cmd without be a player use full cmd /report <player> <type> <args> --validate ;btw if you use console you know it :)");
                    return;
                }
                final ReportType reportType = plugin.getReportManager().getReportTypeList().get(args[1]);
                if(reportType != null)
                {
                    new TypeReportBook(lang, args[0], reportType, Collections.emptyList()).open((Player) commandSender);
                }
            }
            else
            {
                final ReportType reportType = plugin.getReportManager().getReportTypeList().get(args[1]);
                if(reportType == null)
                {
                    return;
                }

                final List<String> argument = new ArrayList<>();
                boolean isValidate = false;

                for (int i = 2; i < args.length; i++)
                {
                    if(!isValidate && args[i].equalsIgnoreCase("--validate"))
                    {
                        isValidate = true;
                    }
                    else
                    {
                        if(args[i].startsWith("--") && reportType.getArgs().containsKey(args[i].substring(2)))
                        {
                            argument.add(args[i]);
                        }
                    }
                }
                if(isValidate)
                {
                    if(argument.isEmpty() && !reportType.getArgs().isEmpty())
                    {
                        commandSender.sendMessage(Lang.REPORT_NO_ARGS.get(lang));
                    }
                    else
                    {
                        commandSender.sendMessage(Lang.REPORT_END.get(lang));
                        final String[] argsArray = argument.stream().map(ar -> ar.substring(2)).toArray(String[]::new);
                        plugin.publishGlobal(CoreMessage.CHANNEL, ReportMessage.getMessage(commandSender.getName(), args[0], reportType.getGeneric(), argsArray), false, true);
                        return;
                    }
                }
                if(!(commandSender instanceof Player))
                {
                    commandSender.sendMessage("can't sur this cmd without be a player use full cmd /report <player> <type> <args> --validate ;btw if you use console you know it :)");
                    return;
                }
                new TypeReportBook(lang, args[0], reportType, argument).open((Player) commandSender);
            }
        });

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,  @NotNull String[] args)
    {
        if(commandSender.hasPermission(CommandPermission.REPORT.getPermission()) && args.length == 1)
        {
            return this.plugin.getOnlinePlayersForTab().stream().filter(player -> player.startsWith(args[0])).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
