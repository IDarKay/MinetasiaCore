package fr.idarkay.minetasia.core.spigot.commands.broadcast;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.messages.BroadCastMessage;
import fr.idarkay.minetasia.core.spigot.messages.CoreMessage;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.utils.GeneralUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * File <b>BroadcastServerTypeCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.broadcast
 * BroadcastServerTypeCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 10/04/2020 at 20:39
 */
public class BroadcastServerTypeCommand extends StepCommand implements FixCommand
{
    public BroadcastServerTypeCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_BROADCAST_SERVER_TYPE, CommandPermission.BROADCAST, 2);
        addChild(new BroadcastServerTypeValueCommand(plugin));
    }

    @Override
    public String getLabel()
    {
        return "server_type";
    }

    private static class BroadcastServerTypeValueCommand extends StepCommand implements FlexibleCommand
    {

        public BroadcastServerTypeValueCommand(@NotNull MinetasiaCore plugin)
        {
            super(plugin, Lang.DESC_BROADCAST_SERVER_TYPE_MESSAGE,  CommandPermission.BROADCAST, 3);
            addChild(new BroadcastServerTypeMessageCommand(plugin));
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
    }

    private static class BroadcastServerTypeMessageCommand extends SubCommand implements FlexibleCommand
    {

        public BroadcastServerTypeMessageCommand(@NotNull MinetasiaCore plugin)
        {
            super(plugin, Lang.DESC_BROADCAST_SERVER_TYPE_MESSAGE, CommandPermission.BROADCAST, 4);
        }

        @Override
        public String getLabel()
        {
            return "<message>";
        }

        @Override
        public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull  String[] args, @NotNull String label)
        {
            final String message = GeneralUtils.concat(args, " ", 2);
            plugin.publishServerType(CoreMessage.CHANNEL, BroadCastMessage.getMessage(message), args[1], true);
            sender.sendMessage(Lang.BROADCAST_MESSAGE_SEND.get(getLangOfSender(sender)));
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
