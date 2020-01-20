package fr.idarkay.minetasia.core.spigot.commands.money;

import fr.idarkay.minetasia.core.api.Economy;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * File <b>MoneyGetCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.money
 * MoneyGetCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 11/12/2019 at 15:03
 */
public class MoneyGetCommand extends SubCommand implements FixCommand {

    public MoneyGetCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_MONEY_GET, CommandPermission.MONEY_GET, 4);
    }

    @Override
    public String getLabel() {
        return "get";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        final String lang = getLangOfSender(sender);
        try
        {
            Economy e = Economy.valueOf(args[0]);
            UUID u = plugin.getPlayerUUID(args[1]);
            if(u != null)
            {
                float m = plugin.getPlayerMoney(u, e);
                                                        // player name  amount money type
                sender.sendMessage(Lang.MONEY_GET.get(lang, Lang.Argument.PLAYER.match(args[1]), Lang.Argument.AMOUNT.match(m), Lang.Argument.MONEY_TYPE.match(e.displayName)));
            }
            else sender.sendMessage(Lang.PLAYER_NOT_EXIST.get(lang));
        }
        catch (IllegalArgumentException e)
        {
            // wall when invalid money type
            sender.sendMessage(Lang.MONEY_WRONG_TYPE.get(lang));
        }

    }
}
