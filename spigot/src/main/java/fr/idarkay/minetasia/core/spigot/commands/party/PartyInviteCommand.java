package fr.idarkay.minetasia.core.spigot.commands.party;

import fr.idarkay.minetasia.core.api.utils.Party;
import fr.idarkay.minetasia.core.api.utils.PlayerStatueFix;
import fr.idarkay.minetasia.core.api.utils.Server;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.messages.CoreMessage;
import fr.idarkay.minetasia.core.spigot.messages.PartyMessage;
import fr.idarkay.minetasia.core.spigot.user.CorePlayer;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * File <b>PartyInviteCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.party
 * PartyInviteCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 22/02/2020 at 17:27
 */
public class PartyInviteCommand extends StepCommand implements FixCommand
{
    public PartyInviteCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_PARTY_INVITE, CommandPermission.PARTY, 1);
        child.add(new SubPartyInviteCommand(plugin));
    }

    @Override
    public String getLabel()
    {
        return "invite";
    }

    public static class SubPartyInviteCommand extends SubCommand implements FlexibleCommand
    {

        public SubPartyInviteCommand(@NotNull MinetasiaCore plugin)
        {
            super(plugin, Lang.DESC_PARTY_INVITE, CommandPermission.PARTY, 2);
        }

        @Override
        public String getLabel()
        {
            return "<player name>";
        }

        @Override
        public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label)
        {
            if(sender instanceof Player)
            {
                //check not self
                if(sender.getName().equalsIgnoreCase(args[1]))
                {
                    sender.sendMessage(Lang.PARTY_INVITE_SELF.get(getLangOfSender(sender)));
                    return;
                }

                Party p = plugin.getPlayer(((Player) sender).getUniqueId()).getParty();
                //check not already in team
                if(p != null && p.getPlayers().containsValue(args[1]))
                {
                    sender.sendMessage(Lang.PARTY_ALREADY_IN_PARTY.get(getLangOfSender(sender)));
                    return;
                }


                final CorePlayer c = plugin.getPlayerManager().getCorePlayer(((Player) sender).getUniqueId());
                if(!c.isEndCountDown(CorePlayer.CountdownType.INVITE_PARTY))
                {
                    sender.sendMessage(Lang.WAIT_BETWEEN_USE.get(getLangOfSender(sender), Lang.Argument.TIME.match(
                            TimeUnit.MILLISECONDS.toSeconds( c.getCountDown(CorePlayer.CountdownType.INVITE_PARTY)))));
                    return;
                }
                if(System.currentTimeMillis() - c.getInvitedPlayerParty().getOrDefault(args[1], 0L) < CorePlayer.CountdownType.INVITE_SAME_PLAYER_PARTY.getTime())
                {
                    sender.sendMessage(Lang.PARTY_ALREADY_INVITE.get(getLangOfSender(sender)));
                    return;
                }

                c.getInvitedPlayerParty().put(args[1], System.currentTimeMillis());

                final UUID uuid;
                final Server server;
                final Player bukkitP = Bukkit.getPlayer(args[1]);
                if(bukkitP != null)
                {
                    uuid = bukkitP.getUniqueId();
                    server = plugin.getThisServer();
                }
                else
                {
                    final PlayerStatueFix playerStatueFix = plugin.getPlayerStatue(args[1]);
                    if(playerStatueFix == null)
                    {
                        sender.sendMessage(Lang.PLAYER_NOT_EXIST.get(getLangOfSender(sender)));
                        c.startCountDown(CorePlayer.CountdownType.INVITE_PARTY);
                        return;
                    }
                    uuid = playerStatueFix.getUUID();
                    server = playerStatueFix.getServer();
                }


                if (p == null)
                {
                    p = plugin.getPartyManager().createParty((Player) sender);
                }
                if(p.getOwner().equals(((Player) sender).getUniqueId()))
                {
                    if(p.limitSize() > p.getPlayers().size())
                    {
                        final String msg = PartyMessage.getInvitePlayerMessage(p, uuid, sender.getName());
                        if(server.getName().equals(plugin.getThisServer().getName()))
                        {
                            PartyMessage.ActionType.INVITE.run(plugin, msg.split(";"));
                            c.getInvitedPlayerParty().put(args[1], System.currentTimeMillis());
                        }
                        else
                            plugin.publishTarget(CoreMessage.CHANNEL, msg, server, false, true);
                        sender.sendMessage(Lang.PARTY_INVITE_SEND.get(getLangOfSender(sender), Lang.Argument.PLAYER.match(args[1])));
                    }
                    else sender.sendMessage(Lang.PARTY_MAX_SIZE.get(getLangOfSender(sender)));
                }
                else sender.sendMessage(Lang.PARTY_NOT_OWNER.get(getLangOfSender(sender)));

            }
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
