package fr.idarkay.minetasia.core.spigot.commands.money;

import fr.idarkay.minetasia.core.api.Economy;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * File <b>MoneyRemoveCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.money
 * MoneyRemoveCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 11/12/2019 at 15:02
 */
public class MoneyRemoveCommand extends StepCommand implements FixCommand {

    public MoneyRemoveCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_MONEY_REMOVE, CommandPermission.MONEY_REMOVE, 4);
        child.add(new AmountCommand(plugin));
    }

    @Override
    public String getLabel() {
        return "remove";
    }

    private final class AmountCommand extends SubCommand implements FlexibleCommand {

        AmountCommand(@NotNull MinetasiaCore plugin) {
            super(plugin, Lang.DESC_MONEY_AMOUNT, CommandPermission.MONEY_SET, 5);
        }

        @Override
        public String getLabel() {
            return "<amount>";
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
                    try
                    {
                        float a = Float.parseFloat(args[3]);
                        if (a < 0) throw new IllegalArgumentException("negative number");
                        double m = plugin.getPlayerMoney(u, e);
                        if(m < a) sender.sendMessage(Lang.NO_AMOUNT_MONEY.get(lang, Lang.Argument.PLAYER.match(args[1])));
                        else
                        {
                            plugin.removePlayerMoney(u, e, a, true);
                            //  amount money type player name
                            sender.sendMessage(Lang.MONEY_REMOVE.get(lang, Lang.Argument.AMOUNT.match(a), Lang.Argument.MONEY_TYPE.match(e.displayName), Lang.Argument.PLAYER.match(args[1])));
                        }
                    }
                    catch (IllegalArgumentException ignore)
                    {
                        // wall when invalid money type
                        sender.sendMessage(Lang.ILLEGAL_NUMBER_VALUE.get(lang));
                    }
                }
                else sender.sendMessage(Lang.PLAYER_NOT_EXIST.get(lang));
            }
            catch (IllegalArgumentException e)
            {
                // wall when invalid money type
                sender.sendMessage(Lang.MONEY_WRONG_TYPE.get(lang));
            }
        }

        @Override
        public boolean isAllPossibilities() {
            return true;
        }

        @Override
        public List<String> getPossibilities() {
            return null;
        }
    }

}
