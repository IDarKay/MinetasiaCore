package fr.idarkay.minetasia.core.spigot.commands.tp;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.jetbrains.annotations.NotNull;


/**
 * File <b>AtAFirstArgsCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.tp
 * AtAFirstArgsCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 13/12/2019 at 14:23
 */
public class AtAFirstArgsCommand extends StepCommand implements FixCommand {

    public AtAFirstArgsCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_TP_A, CommandPermission.TP, 2);
        child.add(new SecondArgsCommand(plugin));
    }

    @Override
    public String getLabel()
    {
        return "@a";

    }
}
