package fr.idarkay.minetasia.core.spigot.commands.party;

import fr.idarkay.minetasia.core.api.utils.Party;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.MainCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


/**
 * File <b>PartyCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.party
 * PartyCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 22/02/2020 at 17:26
 */
public class PartyCommand extends MainCommand implements FixCommand
{
    public PartyCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_PARTY, CommandPermission.PARTY, 1);
        child.add(new PartyLeaveCommand(plugin));
        child.add(new PartyJoinCommand(plugin));
        child.add(new PartyInviteCommand(plugin));
        child.add(new PartyKickCommand(plugin));
        child.add(new PartyMakeLeaderCommand(plugin));
        child.add(new PartyListCommand(plugin));
    }

    @Override
    public String getLabel()
    {
        return "party";
    }
}
