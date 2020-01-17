package fr.idarkay.minetasia.core.spigot.commands.permission.group;

import fr.idarkay.minetasia.core.api.BoostType;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.permission.Group;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * File <b>BoostValidCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.group
 * BoostValidCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 16/01/2020 at 22:43
 */
public class BoostValidCommand extends SubCommand implements FlexibleCommand
{
    public BoostValidCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_PERMISSION_GROUP_BOOST_VALID, CommandPermission.PERMISSION_GROUP_BOOST, 7);
    }

    @Override
    public String getLabel()
    {
        return "<value>";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label)
    {
        final String lang = getLangOfSender(sender);
        Group g = plugin.getPermissionManager().groups.get(args[1]);
        if(g != null)
        {
            try
            {
                final float f = Float.parseFloat(args[5]);
                final BoostType b = BoostType.valueOf(args[4]);
                if(args[3].equalsIgnoreCase("party"))
                {
                    g.setPartyBoost(b, f);
                    sender.sendMessage(Lang.GROUP_BOOST_ADD.get(lang, f, args[3].toLowerCase(), b, g.getName()));
                }
                else if(args[3].equalsIgnoreCase("personal"))
                {
                    g.setPersonalBoost(b ,f);
                    sender.sendMessage(Lang.GROUP_BOOST_ADD.get(lang, f, args[3].toLowerCase(), b, g.getName()));
                }
                else
                {
                    sender.sendMessage(Lang.GROUP_BOOST_INVALID_TYPE.get(lang));
                }
            }
            catch (NumberFormatException e)
            {
                sender.sendMessage(Lang.ILLEGAL_NUMBER_VALUE.get(lang));
            }
            catch (IllegalArgumentException e)
            {
                sender.sendMessage(Lang.GROUP_BOOST_INVALID_TYPE.get(lang));
            }
        }
        else sender.sendMessage(Lang.GROUP_NOT_EXIST.get(lang));

    }

    @Override
    public boolean isAllPossibilities()
    {
        return true;
    }

    @Override
    public List<String> getPossibilities()
    {
        return null;
    }

}
