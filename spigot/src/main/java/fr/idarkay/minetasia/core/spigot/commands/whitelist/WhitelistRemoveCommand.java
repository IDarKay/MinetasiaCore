package fr.idarkay.minetasia.core.spigot.commands.whitelist;

import fr.idarkay.minetasia.core.api.SettingsKey;
import fr.idarkay.minetasia.core.api.utils.MinetasiaSettings;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bson.Document;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * File <b>WhitelistRemoveCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.whitelist
 * WhitelistRemoveCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 10/04/2020 at 23:07
 */
public class WhitelistRemoveCommand extends StepCommand implements FixCommand
{
    public WhitelistRemoveCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_WHITELIST_REMOVE, CommandPermission.WHITELIST, 2);
        addChild(new WhitelistRemoveValueCommand(plugin));
    }

    @Override
    public String getLabel()
    {
        return "remove";
    }

    @Override
    public List<String> tabComplete(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label)
    {
        final MinetasiaSettings<List> settings = plugin.getSettings(SettingsKey.WHITELIST);
        return settings.getValue() == null ? Collections.emptyList()
                : ((List<Document>) settings.getValue()).stream().map(document -> document.getString("username"))
                .filter(username -> username.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());

    }

    private static class WhitelistRemoveValueCommand extends SubCommand implements FlexibleCommand
    {

        public WhitelistRemoveValueCommand(@NotNull MinetasiaCore plugin)
        {
            super(plugin, Lang.DESC_WHITELIST_ADD, CommandPermission.WHITELIST, 3);
        }

        @Override
        public String getLabel()
        {
            return "<username>";
        }

        @Override
        public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull  String[] args, @NotNull String label)
        {
            final MinetasiaSettings<List> settings = plugin.getSettings(SettingsKey.WHITELIST);
            final List<Document> value = settings.getValue() == null ? new ArrayList<>() : new ArrayList<>(settings.getValue());
            if(value.removeIf(document -> args[1].equalsIgnoreCase(document.getString("username"))))
            {
                settings.setValue(value);
                sender.sendMessage(Lang.WHITELIST_REMOVE_GOOD.get(getLangOfSender(sender), Lang.Argument.PLAYERS.match(args[1])));
            }
            else
            {
                sender.sendMessage(Lang.WHITELIST_REMOVE_FAIL.get(getLangOfSender(sender), Lang.Argument.PLAYERS.match(args[1])));
            }
        }

        @Override
        public boolean isAllPossibilities()
        {
            return true;
        }

        @Override
        public List<String> getPossibilities()
        {
            return null;
        }
    }

}
