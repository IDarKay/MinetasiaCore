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
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label)
    {
        if(args.length > 0)
            super.execute(plugin, sender, args, label);
        else
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
                    plugin.getPartyManager().getPlayersParty().keySet().forEach(System.out::println);
                    System.out.println("============");
                    plugin.getPartyManager().getPartyMap().keySet().forEach(System.out::println);
                    super.execute(plugin, sender, args, label);
                }
            }
        }
    }

    @Override
    public String getLabel()
    {
        return "party";
    }
}
