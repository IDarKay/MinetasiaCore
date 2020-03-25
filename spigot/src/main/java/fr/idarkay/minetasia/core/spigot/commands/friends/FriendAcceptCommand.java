package fr.idarkay.minetasia.core.spigot.commands.friends;

import fr.idarkay.minetasia.core.api.utils.PlayerStatueFix;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * File <b>FriendAcceptCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.friends
 * FriendAcceptCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 14/03/2020 at 23:55
 */
public class FriendAcceptCommand extends SubCommand implements FixCommand
{
    public FriendAcceptCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_FRIENDS_ACCEPT, CommandPermission.FRIEND, 2);
    }

    @Override
    public String getLabel()
    {
        return "accept";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull  String[] args, @NotNull String label)
    {
        if(!(sender instanceof Player)) return;
        final Player player = (Player) sender;
        final String lang = plugin.getPlayerLang(player.getUniqueId());

        final UUID u = FriendCommand.getFriendRequest().getIfPresent(player.getUniqueId());
        if( u != null)
        {
            FriendCommand.getFriendRequest().invalidate(player.getUniqueId());
            if(plugin.getFriends(player.getUniqueId()).size() >= FriendCommand.getMaxFriends(player))
            {
                sender.sendMessage(Lang.MAX_FRIENDS_REACH.get(lang));
                return;
            }
            plugin.addFriend(player.getUniqueId(), u);
            plugin.addFriend(u, player.getUniqueId());

            sender.sendMessage(Lang.NEW_FRIEND.get(lang, Lang.Argument.PLAYER.match( plugin.getPlayerName(u))));
            final Player player1 = Bukkit.getPlayer(u);
            if(player1 != null) player1.sendMessage(Lang.NEW_FRIEND.get(lang, Lang.Argument.PLAYER.match(player.getName())));
            else
            {
                final PlayerStatueFix playerStatue = plugin.getPlayerStatue(u);
                if(playerStatue != null)
                    plugin.publishTargetPlayer("core-msg",  "NEW_FRIEND;" + u.toString() +";true;" + Lang.Argument.PLAYER.name() + "\\" +  player.getName(), playerStatue, false, true);
            }
        }
    }
}
