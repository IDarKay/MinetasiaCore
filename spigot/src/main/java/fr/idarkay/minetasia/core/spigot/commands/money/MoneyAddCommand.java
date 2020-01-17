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
 * File <b>MoneyAddCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.money
 * MoneyAddCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 11/12/2019 at 15:02
 */
public class MoneyAddCommand extends StepCommand implements FixCommand {

    public MoneyAddCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_MONEY_ADD, CommandPermission.MONEY_ADD, 4);
        child.add(new AmountCommand(plugin));
    }

    @Override
    public String getLabel() {
        return "add";
    }

    private final class AmountCommand extends SubCommand implements FlexibleCommand {

        AmountCommand(@NotNull MinetasiaCore plugin) {
            super(plugin, Lang.DESC_MONEY_AMOUNT, CommandPermission.MONEY_ADD, 5);
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
                        plugin.addPlayerMoney(u, e, a, true);
                                            //  amount money type player name
                        sender.sendMessage(Lang.MONEY_ADD.get(lang, a, e.displayName, args[1]));
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
