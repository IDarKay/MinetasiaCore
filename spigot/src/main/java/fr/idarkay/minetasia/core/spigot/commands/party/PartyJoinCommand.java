package fr.idarkay.minetasia.core.spigot.commands.party;

import fr.idarkay.minetasia.core.api.utils.Party;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.user.CorePlayer;
import fr.idarkay.minetasia.core.spigot.user.PlayerParty;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * File <b>PartyJoinCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.party
 * PartyJoinCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 22/02/2020 at 17:27
 */
public class PartyJoinCommand extends SubCommand implements FixCommand
{
    public PartyJoinCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.PARTY_JOIN, CommandPermission.PARTY, 1);
    }

    @Override
    public String getLabel()
    {
        return "join";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label)
    {
        if(sender instanceof Player)
        {
            final CorePlayer c = plugin.getPlayerManager().getCorePlayer(((Player) sender).getUniqueId());

            if(c.getLastPartyRequest() != null)
            {

                final PlayerParty newParty = plugin.getPartyManager().getParty(c.getLastPartyRequest());
                if(newParty == null)
                {
                    sender.sendMessage(Lang.PARTY_DELETE.get(getLangOfSender(sender)));
                    c.setLastPartyRequest(null);
                    return;
                }

                final Party p = plugin.getPlayer(((Player) sender).getUniqueId()).getParty();

                if (p != null)
                {
                    if(p.getOwner().equals(((Player) sender).getUniqueId()))
                    {
                        plugin.getPartyManager().deleteAndUpdate(p.getId());
                        sender.sendMessage(Lang.PARTY_DISBAND.get(getLangOfSender(sender)));
                    }
                    else
                    {
                        plugin.getPartyManager().removePlayerAdnUpdate(p.getId(), ((Player) sender).getUniqueId());
                        sender.sendMessage(Lang.PARTY_LEAVE.get(getLangOfSender(sender)));
                    }
                }

                if(newParty.limitSize() > newParty.getPlayers().size())
                {
                    plugin.getPartyManager().addPlayerAndUpdate(newParty, ((Player) sender).getUniqueId(), sender.getName());
                    sender.sendMessage(Lang.PARTY_JOIN.get(getLangOfSender(sender), Lang.Argument.PLAYER.match(newParty.getOwnerName())));
                }
                else sender.sendMessage(Lang.PARTY_FULL.get(getLangOfSender(sender)));

                c.setLastPartyRequest(null);
            }
            else sender.sendMessage(Lang.PARTY_NOT_REQUEST.get(getLangOfSender(sender)));
        }
    }
}
