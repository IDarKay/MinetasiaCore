package fr.idarkay.minetasia.core.spigot.commands.friends;

import fr.idarkay.minetasia.core.api.utils.PlayerStatueFix;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * File <b>FriendAddCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.friends
 * FriendAddCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 14/03/2020 at 22:54
 */
public class FriendAddCommand extends StepCommand implements FixCommand
{
    public FriendAddCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_FRIENDS_ADD, CommandPermission.FRIEND, 2);
        child.add(new FriendSubAddCommand(plugin));
    }

    @Override
    public String getLabel()
    {
        return "add";
    }

    private static class FriendSubAddCommand extends SubCommand implements FlexibleCommand
    {

        public FriendSubAddCommand(@NotNull MinetasiaCore plugin)
        {
            super(plugin, Lang.DESC_FRIENDS_ADD_SUB, CommandPermission.FRIEND, 3);
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
            if(!args[1].equalsIgnoreCase(player.getName()))
            {
                if(plugin.getFriends(player.getUniqueId()).size() >= FriendCommand.getMaxFriends(player))
                {
                    sender.sendMessage(Lang.MAX_FRIENDS_REACH.get(lang));
                    return;
                }
                final UUID uuid = plugin.getPlayerUUID(args[1]);
                if(uuid != null)
                {
                    if(!plugin.isFriend(player.getUniqueId(), uuid))
                    {
                        final PlayerStatueFix playerStatueFix = plugin.getPlayerStatue(uuid);
                        if(playerStatueFix != null)
                        {
                            sender.sendMessage(Lang.REQUEST_SEND_FRIENDS.get(lang, Lang.Argument.PLAYER.match( args[1])));
                            final org.bukkit.entity.Player p = plugin.getServer().getPlayer(uuid);
                            if(p != null)
                            {
                                FriendCommand.newRequest(plugin, uuid, player.getUniqueId(), p);
                            }
                            else plugin.publishTargetPlayer("core-cmd", "friends;" + player.getUniqueId().toString() +";" + uuid.toString(), playerStatueFix, false, true);
                        }
                        else sender.sendMessage(Lang.PLAYER_NOT_ONLINE.get(lang));
                    }
                    else sender.sendMessage(Lang.ALREADY_FRIEND.get(lang, Lang.Argument.PLAYER.match( args[1])));
                }
                else sender.sendMessage(Lang.PLAYER_NOT_EXIST.get(lang));
            }
            else sender.sendMessage(Lang.SELF_ADD_FRIEND.get(lang));
        }

        @Override
        public boolean isAllPossibilities()
        {
            return true;
        }

        @Override
        public List<String> getPossibilities()
        {
            return plugin.getOnlinePlayersForTab();
        }
    }

}
