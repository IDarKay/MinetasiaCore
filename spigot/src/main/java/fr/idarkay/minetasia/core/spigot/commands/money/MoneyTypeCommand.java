package fr.idarkay.minetasia.core.spigot.commands.money;

import fr.idarkay.minetasia.core.api.Economy;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * File <b>MoneyTypeCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.money
 * MoneyTypeCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 11/12/2019 at 15:02
 */
public class MoneyTypeCommand extends StepCommand implements FlexibleCommand {

    public MoneyTypeCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_MONEY_TYPE, CommandPermission.MONEY, 2);
        child.add(new MoneyPlayerCommand(plugin));
    }

    @Override
    public List<String> tabComplete(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        List<String> back = getBasicTabCompleter(sender, args); // get create cmd
        back.addAll(getCustomCompleter(sender, args, plugin.getOnlinePlayersForTab(), true)); //get all group name
        return back;
    }

    @Override
    public String getLabel() {
        return "<money type>";
    }

    @Override
    public boolean isAllPossibilities() {
        return false;
    }

    @Override
    public List<String> getPossibilities() {
        return Arrays.stream(Economy.values()).map(Enum::name).collect(Collectors.toList());
    }
}
