package fr.idarkay.minetasia.core.spigot.commands.moderation.ban;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * File <b>BanUsernameCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.moderation.ban
 * BanUsernameCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 12/03/2020 at 22:40
 */
public class BanUsernameCommand extends StepCommand implements FlexibleCommand
{
    public BanUsernameCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DEC_BAN_USERNAME, CommandPermission.BAN, 2);
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
        return plugin.getOnlinePlayersForTab();
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label)
    {
        //todo: livre
    }
}
