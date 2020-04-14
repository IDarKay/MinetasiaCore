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
import java.util.List;
import java.util.UUID;

/**
 * File <b>WhitelistAddCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.whitelist
 * WhitelistAddCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 10/04/2020 at 23:07
 */
public class WhitelistAddCommand extends StepCommand implements FixCommand
{
    public WhitelistAddCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_WHITELIST_ADD, CommandPermission.WHITELIST, 2);
        addChild(new WhitelistAddValueCommand(plugin));
    }

    @Override
    public String getLabel()
    {
        return "add";
    }

    @Override
    public List<String> tabComplete(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label)
    {
        return getCustomCompleter(sender, args, plugin.getOnlinePlayersForTab(), true);
    }

    private static class WhitelistAddValueCommand extends SubCommand implements FlexibleCommand
    {

        public WhitelistAddValueCommand(@NotNull MinetasiaCore plugin)
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
            final UUID uuid = plugin.getPlayerUUID(args[1]);
            if(uuid != null)
            {
                final MinetasiaSettings<List> settings = plugin.getSettings(SettingsKey.WHITELIST);
                final List<Document> value = settings.getValue() == null ? new ArrayList<>() : new ArrayList<>(settings.getValue());
                value.add(new Document("uuid", uuid.toString()).append("username", args[1]));
                settings.setValue(value);
                sender.sendMessage(Lang.WHITELIST_ADD.get(getLangOfSender(sender), Lang.Argument.PLAYERS.match(args[1])));
            }
            else
            {
                sender.sendMessage(Lang.PLAYER_NOT_EXIST.get(getLangOfSender(sender)));
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
