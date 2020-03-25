package fr.idarkay.minetasia.core.spigot.commands.friends;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.Tuple;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * File <b>FriendRemoveCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.friends
 * FriendRemoveCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 14/03/2020 at 23:43
 */
public class FriendRemoveCommand extends StepCommand implements FixCommand
{
    public FriendRemoveCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_FRIENDS_REMOVE, CommandPermission.FRIEND, 2);
        child.add(new FriendSubRemoveCommand(plugin));
    }

    @Override
    public String getLabel()
    {
        return "remove";
    }

    @Override
    public List<String> tabComplete(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull  String[] args, @NotNull String label)
    {
        if(!(sender instanceof Player)) return null;
        if(args.length > 1)
            return plugin.getFriends(((Player) sender).getUniqueId()).values().stream().filter(s -> s.a().startsWith(args[1])).map(Tuple::a).collect(Collectors.toList());
        else
            return plugin.getFriends(((Player) sender).getUniqueId()).values().stream().map(Tuple::a).collect(Collectors.toList());
    }

    private static class FriendSubRemoveCommand extends SubCommand implements FlexibleCommand
    {
        public FriendSubRemoveCommand(@NotNull MinetasiaCore plugin)
        {
            super(plugin, Lang.DESC_FRIENDS_REMOVE_SUB, CommandPermission.FRIEND, 2);
        }

        @Override
        public String getLabel()
        {
            return "<username>";
        }

        @Override
        public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull  String[] args, @NotNull String label)
        {
            if(!(sender instanceof Player)) return;
            final Player player = (Player) sender;
            final String lang = plugin.getPlayerLang(player.getUniqueId());

            final UUID uuid = plugin.getPlayerUUID(args[1]);
            if(uuid != null)
            {
                if(plugin.isFriend(player.getUniqueId(), uuid)) {
                    plugin.removeFriend(player.getUniqueId(), uuid);
                    plugin.removeFriend(uuid, player.getUniqueId());
                    sender.sendMessage(Lang.REMOVE_FRIEND.get(lang, Lang.Argument.PLAYER.match(args[1])));
                }
                else sender.sendMessage(Lang.NOT_FRIEND.get(lang, Lang.Argument.PLAYER.match( args[1])));
            }
            else sender.sendMessage(Lang.PLAYER_NOT_EXIST.get(lang));
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
