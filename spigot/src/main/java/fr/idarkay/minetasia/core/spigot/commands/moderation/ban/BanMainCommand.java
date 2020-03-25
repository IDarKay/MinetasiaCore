package fr.idarkay.minetasia.core.spigot.commands.moderation.ban;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.MainCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>BanMainCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.moderation.ban
 * BanMainCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 12/03/2020 at 19:59
 */
public class BanMainCommand extends MainCommand
{
    public BanMainCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DEC_BAN, CommandPermission.BAN, 1);
    }

    @Override
    public String getLabel()
    {
        return "ban";
    }
}
