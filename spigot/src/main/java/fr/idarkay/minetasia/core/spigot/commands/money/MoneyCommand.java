package fr.idarkay.minetasia.core.spigot.commands.money;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.MainCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>MoneyCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.money
 * MoneyCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 11/12/2019 at 15:01
 */
public class MoneyCommand extends MainCommand implements FixCommand {

    public MoneyCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_MONEY, CommandPermission.MONEY, 1);
        child.add(new MoneyTypeCommand(plugin));
    }

    @Override
    public String getLabel() {
        return "money";
    }
}
