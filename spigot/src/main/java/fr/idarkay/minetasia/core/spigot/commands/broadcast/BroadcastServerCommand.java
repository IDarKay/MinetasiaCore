package fr.idarkay.minetasia.core.spigot.commands.broadcast;

import fr.idarkay.minetasia.core.api.utils.Server;
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

import java.util.ArrayList;
import java.util.List;

/**
 * File <b>BroadcastServerCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.broadcast
 * BroadcastServerCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 10/04/2020 at 20:07
 */
public class BroadcastServerCommand extends StepCommand implements FixCommand
{
    public BroadcastServerCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_BROADCAST_SERVER, CommandPermission.BROADCAST, 2);
        addChild(new BroadcastServerValueCommand(plugin));
    }

    @Override
    public String getLabel()
    {
        return "server";
    }

    @Override
    public List<String> tabComplete(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label)
    {
        final List<String> value = new ArrayList<>(plugin.getServerManager().getServers().keySet());
        value.add("this");
        return getCustomCompleter(sender, args, value, true);
    }

    private static class BroadcastServerValueCommand extends StepCommand implements FlexibleCommand
    {

        public BroadcastServerValueCommand(@NotNull MinetasiaCore plugin)
        {
            super(plugin, Lang.DESC_BROADCAST_SERVER_MESSAGE,  CommandPermission.BROADCAST, 3);
            addChild(new BroadcastServerMessageCommand(plugin));
        }

        @Override
        public String getLabel()
        {
            return "<server_id>";
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

    private static class BroadcastServerMessageCommand extends SubCommand implements FlexibleCommand
    {

        public BroadcastServerMessageCommand(@NotNull MinetasiaCore plugin)
        {
            super(plugin, Lang.DESC_BROADCAST_SERVER_MESSAGE, CommandPermission.BROADCAST, 4);
        }

        @Override
        public String getLabel()
        {
            return "<message>";
        }

        @Override
        public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull  String[] args, @NotNull String label)
        {
            final Server server = "this".equalsIgnoreCase(args[1]) ? plugin.getThisServer() : plugin.getServer(args[1]);
            if(server == null)
            {
                sender.sendMessage(Lang.SERVER_NOT_FOUND.get(getLangOfSender(sender)));
                return;
            }

            final String message = GeneralUtils.concat(args, " ", 2);
            plugin.publishTarget(CoreMessage.CHANNEL, BroadCastMessage.getMessage(message), server, false, true);
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
