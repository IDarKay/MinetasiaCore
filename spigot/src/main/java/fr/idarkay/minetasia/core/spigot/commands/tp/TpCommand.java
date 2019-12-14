package fr.idarkay.minetasia.core.spigot.commands.tp;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.MainCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>TpCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.tp
 * TpCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 13/12/2019 at 13:47
 */
public class TpCommand extends MainCommand implements FixCommand {

    public TpCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_TP, CommandPermission.TP, 1);
        child.add(new AtAFirstArgsCommand(plugin));
        child.add(new UserFirstArgsCommand(plugin));
    }

    @Override
    public String getLabel() {
        return "tp";
    }
}
