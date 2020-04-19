package fr.idarkay.minetasia.core.spigot.commands.playerdata;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * File <b>PlayerDataPlayerCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.playerdata
 * PlayerDataPlayerCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 10/04/2020 at 13:42
 */
public class PlayerDataPlayerCommand extends StepCommand implements FlexibleCommand
{
    public PlayerDataPlayerCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_PLAYER_DATA_PLAYER , CommandPermission.PLAYER_DATA, 2);
        addChild(new PlayerDataGetCommand(plugin));
        addChild(new PlayerDataSetCommand(plugin));
        addChild(new PlayerDataRemoveCommand(plugin));
    }

    @Override
    public String getLabel()
    {
        return "<username>";
    }

    @Override
    public boolean isAllPossibilities()
    {
        return true;
    }

    @Override
    public List<String> getPossibilities()
    {
        return null;
    }
}
