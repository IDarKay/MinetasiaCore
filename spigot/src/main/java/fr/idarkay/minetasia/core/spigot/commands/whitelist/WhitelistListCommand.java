package fr.idarkay.minetasia.core.spigot.commands.whitelist;

import fr.idarkay.minetasia.core.api.SettingsKey;
import fr.idarkay.minetasia.core.api.utils.MinetasiaSettings;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bson.Document;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * File <b>WhitelistListCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.whitelist
 * WhitelistListCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 10/04/2020 at 23:07
 */
public class WhitelistListCommand extends SubCommand implements FixCommand
{
    public WhitelistListCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_WHITELIST_LIST, CommandPermission.WHITELIST, 2);
    }

    @Override
    public String getLabel()
    {
        return "list";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull  String[] args, @NotNull String label)
    {
        final MinetasiaSettings<List> settings = plugin.getSettings(SettingsKey.WHITELIST);
        final List<String> user = settings.getValue() == null ? new ArrayList<>() : ((List<Document>) settings.getValue()).stream().map(document -> document.getString("username")).collect(Collectors.toList());
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < user.size(); i++)
        {
            if(i != 0) builder.append(", ");
            builder.append(user.get(i));
        }

        sender.sendMessage(Lang.WHITELIST_LIST.get(getLangOfSender(sender), Lang.Argument.PLAYERS.match(builder)));
    }
}
