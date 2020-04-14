package fr.idarkay.minetasia.core.spigot.commands.maintenance;

import fr.idarkay.minetasia.core.api.SettingsKey;
import fr.idarkay.minetasia.core.api.utils.MinetasiaSettings;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.messages.CoreMessage;
import fr.idarkay.minetasia.core.spigot.messages.ForceDisconnectMessage;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * File <b>MaintenanceAllCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.maintenance
 * MaintenanceAllCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 11/04/2020 at 00:09
 */
public class MaintenanceAllCommand extends StepCommand implements FixCommand
{
    public MaintenanceAllCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_MAINTENANCE_ALL, CommandPermission.MAINTENANCE, 2);
        addChild(new MaintenanceAllOffCommand(plugin));
        addChild(new MaintenanceAllOnCommand(plugin));
    }

    @Override
    public String getLabel()
    {
        return "all";
    }

    private static class MaintenanceAllOffCommand extends SubCommand implements FixCommand
    {
        public MaintenanceAllOffCommand(@NotNull MinetasiaCore plugin)
        {
            super(plugin, Lang.DESC_MAINTENANCE_ALL_OFF, CommandPermission.MAINTENANCE, 3);
        }

        @Override
        public String getLabel()
        {
            return "off";
        }

        @Override
        public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull  String[] args, @NotNull String label)
        {
            final MinetasiaSettings<List> settings = plugin.getSettings(SettingsKey.MAINTENANCE);
            final List<String> value = settings.getValue() == null ? new ArrayList<>() : new ArrayList<>(settings.getValue());
            if(value.remove("all"))
            {
                settings.setValue(value);
                sender.sendMessage(Lang.MAINTENANCE_DISABLE.get(getLangOfSender(sender), Lang.Argument.SERVER_TYPE.match("all")));
            }
        }
    }

    private static class MaintenanceAllOnCommand extends SubCommand implements FlexibleCommand
    {

        public MaintenanceAllOnCommand(@NotNull MinetasiaCore plugin)
        {
            super(plugin,  Lang.DESC_MAINTENANCE_ALL_ON, CommandPermission.MAINTENANCE, 3);
        }

        @Override
        public String getLabel()
        {
            return "<disconnect>";
        }

        @Override
        public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull  String[] args, @NotNull String label)
        {
            final DisconnectType disconnectType = DisconnectType.valueOf(args[1].toUpperCase());
            final MinetasiaSettings<List> settings = plugin.getSettings(SettingsKey.MAINTENANCE);
            final List<String> value = settings.getValue() == null ? new ArrayList<>() : new ArrayList<>(settings.getValue());
            value.add("all");
            settings.setValue(value);

            sender.sendMessage(Lang.MAINTENANCE_ENABLE.get(getLangOfSender(sender), Lang.Argument.SERVER_TYPE.match("all")));

            if(disconnectType == DisconnectType.ALL)
            {
                plugin.publishGlobal(CoreMessage.CHANNEL, ForceDisconnectMessage.getMessage(false), false, true);
            }
            else if(disconnectType == DisconnectType.HUB)
            {
                plugin.publishHub(CoreMessage.CHANNEL, ForceDisconnectMessage.getMessage(false), true);
            }
        }

        @Override
        public boolean isAllPossibilities()
        {
            return false;
        }

        @Override
        public List<String> getPossibilities()
        {
            return Arrays.stream(DisconnectType.values()).map(disconnectType -> disconnectType.name().toLowerCase()).collect(Collectors.toList());
        }
    }

    private enum DisconnectType
    {
        HUB, ALL, NONE;
    }

}
