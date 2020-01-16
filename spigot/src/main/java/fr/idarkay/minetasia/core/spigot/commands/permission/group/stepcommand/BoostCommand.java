package fr.idarkay.minetasia.core.spigot.commands.permission.group.stepcommand;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>BoostCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.group.stepcommand
 * BoostCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 16/01/2020 at 22:31
 */
public class BoostCommand extends StepCommand implements FixCommand
{
    public BoostCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.GROUP_BOOST, CommandPermission.PERMISSION_GROUP_BOOST, 4);
        addChild(new BoostTypeCommand(plugin));
    }

    @Override
    public String getLabel()
    {
        return "boost";
    }


}
