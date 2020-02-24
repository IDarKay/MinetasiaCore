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
 * File <b>PartyListCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.party
 * PartyListCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 24/02/2020 at 10:41
 */
public class PartyListCommand extends SubCommand implements FixCommand
{
    public PartyListCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_PARTY_LIST, CommandPermission.PARTY, 1);
    }

    @Override
    public String getLabel()
    {
        return "list";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label)
    {
        if(sender instanceof Player)
        {
            final Party p =  plugin.getPlayer(((Player) sender).getUniqueId()).getParty();
            if(p != null)
            {
                final StringBuilder m = new StringBuilder();
                boolean first = true;
                for (String value : p.getPlayers().values())
                {
                    if(!first)
                    {
                        m.append(", ");
                    }
                    else
                        first = false;

                    m.append(value);
                }

                final String[] msg = Lang.YOUR_PARTY.getWithSplit(getLangOfSender(sender), Lang.Argument.PLAYER.match(p.getOwnerName()),
                        Lang.Argument.MEMBERS.match(m.toString()));

                for (String s : msg)
                {
                    sender.sendMessage(s);
                }
            }
            else
            {
                sender.sendMessage(Lang.PARTY_NOT_IN_PARTY.get(getLangOfSender(sender)));
            }
        }
    }
}
