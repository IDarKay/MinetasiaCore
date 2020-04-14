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

/**
 * File <b>MaintenanceServerTypeValueCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.maintenance
 * MaintenanceServerTypeValueCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 11/04/2020 at 01:19
 */
public class MaintenanceServerTypeValueCommand extends StepCommand implements FlexibleCommand
{
    public MaintenanceServerTypeValueCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_MAINTENANCE_SERVER_TYPE, CommandPermission.MAINTENANCE, 3);
        child.add(new MaintenanceServerTypeEndCommand(plugin));
        child.add(new MaintenanceServerTypeOffCommand(plugin));
    }

    @Override
    public String getLabel()
    {
        return "<server_type>";
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

    private static class MaintenanceServerTypeEndCommand extends SubCommand implements FlexibleCommand
    {

        public MaintenanceServerTypeEndCommand(@NotNull MinetasiaCore plugin)
        {
            super(plugin, Lang.DESC_MAINTENANCE_SERVER_TYPE, CommandPermission.MAINTENANCE, 4);
        }

        @Override
        public String getLabel()
        {
            return "<disconnect>";
        }

        @Override
        public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull  String[] args, @NotNull String label)
        {
            final boolean disconnect = "true".equalsIgnoreCase(args[2]);
            final MinetasiaSettings<List> settings = plugin.getSettings(SettingsKey.MAINTENANCE);
            final List<String> value = settings.getValue() == null ? new ArrayList<>() : new ArrayList<>(settings.getValue());

            value.add(args[1]);
            settings.setValue(value);
            sender.sendMessage(Lang.MAINTENANCE_ENABLE.get(getLangOfSender(sender), Lang.Argument.SERVER_TYPE.match(args[1])));

            if(disconnect)
            {
                plugin.publishServerType(CoreMessage.CHANNEL, ForceDisconnectMessage.getMessage(!"hub".equals(args[1])), args[1], true);
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
            return Arrays.asList("true", "false");
        }
    }

    private static class MaintenanceServerTypeOffCommand extends SubCommand implements FixCommand
    {

        public MaintenanceServerTypeOffCommand(@NotNull MinetasiaCore plugin)
        {
            super(plugin, Lang.DESC_MAINTENANCE_SERVER_TYPE, CommandPermission.MAINTENANCE, 4);
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
            if(value.remove(args[1]))
            {
                settings.setValue(value);
                sender.sendMessage(Lang.MAINTENANCE_DISABLE.get(getLangOfSender(sender), Lang.Argument.SERVER_TYPE.match(args[1])));
            }
        }

    }

}
