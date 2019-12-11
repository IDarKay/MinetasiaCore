package fr.idarkay.minetasia.core.spigot.commands.money;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * File <b>MoneyPlayerCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.money
 * MoneyPlayerCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 11/12/2019 at 15:02
 */
public class MoneyPlayerCommand extends StepCommand implements FlexibleCommand {

    public MoneyPlayerCommand(@NotNull MinetasiaCore plugin) {
        super(plugin,  Lang.DESC_MONEY_PLAYER, CommandPermission.MONEY, 3);
        child.add(new MoneyAddCommand(plugin));
        child.add(new MoneyGetCommand(plugin));
        child.add(new MoneySetCommand(plugin));
        child.add(new MoneyRemoveCommand(plugin));
    }

    @Override
    public String getLabel() {
        return "<player>";
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
