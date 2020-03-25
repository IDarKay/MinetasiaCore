package fr.idarkay.minetasia.core.spigot.commands.party;

import fr.idarkay.minetasia.core.api.utils.Party;
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
import java.util.Map;
import java.util.UUID;

/**
 * File <b>PartyKickCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.party
 * PartyKickCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 23/02/2020 at 22:50
 */
public class PartyKickCommand extends StepCommand implements FixCommand
{
    public PartyKickCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_PARTY_KICK, CommandPermission.PARTY, 1);
        child.add(new SubPartyKickCommand(plugin));
    }

    @Override
    public String getLabel()
    {
        return "kick";
    }


    public static class SubPartyKickCommand  extends SubCommand implements FlexibleCommand
    {

        public SubPartyKickCommand(@NotNull MinetasiaCore plugin)
        {
            super(plugin, Lang.DESC_PARTY_KICK, CommandPermission.PARTY, 2);
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
                        for (Map.Entry<UUID, String> entry : p.getPlayers().entrySet())
                        {
                            if(entry.getValue().equalsIgnoreCase(args[1]))
                            {
                                plugin.getPartyManager().removePlayerAdnUpdate(p.getId(), entry.getKey());
                                sender.sendMessage(Lang.PARTY_PLAYER_KICK.get(getLangOfSender(sender), Lang.Argument.PLAYER.match(args[1])));
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
