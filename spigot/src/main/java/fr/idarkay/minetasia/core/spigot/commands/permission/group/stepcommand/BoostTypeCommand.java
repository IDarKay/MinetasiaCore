package fr.idarkay.minetasia.core.spigot.commands.permission.group.stepcommand;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * File <b>BoostTypeCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.group.stepcommand
 * BoostTypeCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 16/01/2020 at 22:49
 */
public class BoostTypeCommand extends StepCommand implements FlexibleCommand
{
    public BoostTypeCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_PERMISSION_GROUP_BOOST_TYPE0, CommandPermission.PERMISSION_GROUP_BOOST, 5);
        child.add(new BoostMoneyTypeCommand(plugin));
    }

    @Override
    public String getLabel()
    {
        return "<type>";
    }

    @Override
    public boolean isAllPossibilities()
    {
        return false;
    }

    private final static List<String> posi = Arrays.asList("party", "personal");

    @Override
    public List<String> getPossibilities()
    {
        return posi;
    }
}
