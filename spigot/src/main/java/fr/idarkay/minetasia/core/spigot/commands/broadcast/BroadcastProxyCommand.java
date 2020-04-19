package fr.idarkay.minetasia.core.spigot.commands.broadcast;

import fr.idarkay.minetasia.core.api.MongoCollections;
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
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * File <b>BroadcastProxyCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.broadcast
 * BroadcastProxyCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 10/04/2020 at 20:19
 */
public class BroadcastProxyCommand extends StepCommand implements FixCommand
{
    private List<String> knownProxy;
    private long lastUpdate = 0L;

    public BroadcastProxyCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_BROADCAST_PROXY, CommandPermission.BROADCAST, 2);
        addChild(new BroadcastProxyValueCommand(plugin));
    }

    @Override
    public List<String> tabComplete(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label)
    {
        final List<String> value = new ArrayList<>(getProxy());
        value.add("this");
        return getCustomCompleter(sender, args, value, true);
    }

    private List<String> getProxy()
    {
        if(knownProxy == null || System.currentTimeMillis() - lastUpdate > 60000)
        {
            knownProxy = new ArrayList<>();;
            plugin.getMongoDbManager().getCollection(MongoCollections.PROXY).find().forEach(proxy -> knownProxy.add(proxy.get("_id", String.class)));
            lastUpdate = System.currentTimeMillis();
        }
        return knownProxy;
    }

    @Override
    public String getLabel()
    {
        return "proxy";
    }

    private static class BroadcastProxyValueCommand extends StepCommand implements FlexibleCommand
    {

        public BroadcastProxyValueCommand(@NotNull MinetasiaCore plugin)
        {
            super(plugin, Lang.DESC_BROADCAST_PROXY_MESSAGE,  CommandPermission.BROADCAST, 3);
            addChild(new BroadcastProxyMessageCommand(plugin));
        }

        @Override
        public String getLabel()
        {
            return "<proxy_id>";
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

    private static class BroadcastProxyMessageCommand extends SubCommand implements FlexibleCommand
    {

        public BroadcastProxyMessageCommand(@NotNull MinetasiaCore plugin)
        {
            super(plugin, Lang.DESC_BROADCAST_PROXY_MESSAGE, CommandPermission.BROADCAST, 4);
        }

        @Override
        public String getLabel()
        {
            return "<message>";
        }

        @Override
        public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull  String[] args, @NotNull String label)
        {

            final Document proxy;
            if(sender instanceof Player && "this".equalsIgnoreCase(args[1]))
            {
                try
                {
                    proxy = plugin.getMongoDbManager().getWithReferenceAndMatch(MongoCollections.ONLINE_USERS, "_id", ((Player) sender).getUniqueId().toString(), "proxy", "proxy_id", "_id", "proxy").first().getList("proxy", Document.class).get(0);
                }
                catch (NullPointerException exception)
                {
                    sender.sendMessage(ChatColor.RED + "Cant get your poxy for unknown reason sorry");
                    return;
                }
            }
            else
            {
                proxy = plugin.getMongoDbManager().getByKey(MongoCollections.PROXY, args[1]);
            }

//            final Proxy server = plugin.getServer(args[1]);
            if(proxy == null)
            {
                sender.sendMessage(Lang.SERVER_NOT_FOUND.get(getLangOfSender(sender)));
                return;
            }

            final int port = proxy.getInteger("publish_port");
            final String ip = proxy.getString("ip");

            final String message = GeneralUtils.concat(args, " ", 2);
            plugin.publishTarget(CoreMessage.CHANNEL, BroadCastMessage.getMessage(message), ip, port, false, true);
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
