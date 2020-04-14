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
 * File <b>BroadcastAllCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.broadcast
 * BroadcastAllCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 10/04/2020 at 19:55
 */
public class BroadcastAllCommand extends StepCommand implements FixCommand
{
    public BroadcastAllCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_BROADCAST_ALL, CommandPermission.BROADCAST, 2);
        addChild(new BroadcastAllEndCommand(plugin));
    }

    @Override
    public String getLabel()
    {
        return "all";
    }

    private static class BroadcastAllEndCommand extends SubCommand implements FlexibleCommand
    {

        public BroadcastAllEndCommand(@NotNull MinetasiaCore plugin)
        {
            super(plugin, Lang.DESC_BROADCAST_ALL, CommandPermission.BROADCAST, 3);
        }

        @Override
        public String getLabel()
        {
            return "<message>";
        }

        @Override
        public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull  String[] args, @NotNull String label)
        {
            final String msg = GeneralUtils.concat(args, " ", 1);
            plugin.publishGlobal(CoreMessage.CHANNEL, BroadCastMessage.getMessage(msg), false, true);
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
