package fr.idarkay.minetasia.core.spigot.commands.party;

import fr.idarkay.minetasia.core.api.utils.Party;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>PartyLeaveCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.party
 * PartyLeaveCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 22/02/2020 at 17:26
 */
public class PartyLeaveCommand extends SubCommand implements FixCommand
{
    public PartyLeaveCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_PARTY_LEAVE, CommandPermission.PARTY, 2);
    }

    @Override
    public String getLabel()
    {
        return "leave";
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
                    plugin.getPartyManager().deleteAndUpdate(p.getId());
                    sender.sendMessage(Lang.PARTY_DISBAND.get(getLangOfSender(sender)));
                }
                else
                {
                    plugin.getPartyManager().removePlayerAdnUpdate(p.getId(), ((Player) sender).getUniqueId());
                    sender.sendMessage(Lang.PARTY_LEAVE.get(getLangOfSender(sender)));
                }
            }
            else sender.sendMessage(Lang.PARTY_NOT_IN_PARTY.get(getLangOfSender(sender)));
        }
    }

}
