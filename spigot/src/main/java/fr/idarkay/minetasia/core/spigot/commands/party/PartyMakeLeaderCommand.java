package fr.idarkay.minetasia.core.spigot.commands.party;

import fr.idarkay.minetasia.core.api.utils.Party;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.messages.CoreMessage;
import fr.idarkay.minetasia.core.spigot.messages.PartyMessage;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * File <b>PartyMakeLeaderCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.party
 * PartyMakeLeaderCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 24/02/2020 at 01:04
 */
public class PartyMakeLeaderCommand extends StepCommand implements FixCommand
{
    public PartyMakeLeaderCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_MAKER_LEADER, CommandPermission.PARTY, 1);
        child.add(new SubPartyMakeLeaderCommand(plugin));
    }

    @Override
    public String getLabel()
    {
        return "makeleader";
    }

    public static class SubPartyMakeLeaderCommand  extends SubCommand implements FlexibleCommand
    {

        public SubPartyMakeLeaderCommand(@NotNull MinetasiaCore plugin)
        {
            super(plugin, Lang.DESC_MAKER_LEADER, CommandPermission.PARTY, 2);
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
                final Party p = plugin.getPlayer(((Player) sender).getUniqueId()).getParty();
                if (p != null)
                {
                    if(p.getOwner().equals(((Player) sender).getUniqueId()))
                    {
                        if(p.getOwnerName().equalsIgnoreCase(args[1]))
                        {
                            sender.sendMessage(Lang.PARTY_YOU_ARE_LEADER.get(getLangOfSender(sender)));
                            return;
                        }

                        for (Map.Entry<UUID, String> entry : p.getPlayers().entrySet())
                        {
                            if(entry.getValue().equalsIgnoreCase(args[1]))
                            {
                                try
                                {
                                    plugin.publishTargetPlayer(CoreMessage.CHANNEL, PartyMessage.getPartyMakeLeaderFirst(p, entry.getKey()), entry.getKey(), false, true);
                                }
                                //player is not only
                                catch (NullPointerException e)
                                {
                                    break;
                                }
                                sender.sendMessage(Lang.PARTY_MAKE_LEADER.get(getLangOfSender(sender), Lang.Argument.PLAYER.match(entry.getValue())));
                                return;
                            }
                        }
                        sender.sendMessage(Lang.PARTY_PLAYER_NOT_IN_PARTY.get(getLangOfSender(sender)));
                    }
                    else
                    {
                        sender.sendMessage(Lang.PARTY_NOT_OWNER.get(getLangOfSender(sender)));
                    }
                }
                else sender.sendMessage(Lang.PARTY_NOT_IN_PARTY.get(getLangOfSender(sender)));
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
