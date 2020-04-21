package fr.idarkay.minetasia.core.spigot.executor;

import fr.idarkay.minetasia.core.api.MinetasiaCoreApi;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>StopExecutor</b> located on fr.idarkay.minetasia.core.spigot.executor
 * StopExecutor is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 20/04/2020 at 17:43
 */
public class StopExecutor implements CommandExecutor
{
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings)
    {
        final String lang = commandSender instanceof Player ?  MinetasiaCoreApi.getInstance().getPlayerLang(((Player) commandSender).getUniqueId()) : MinetasiaLang.BASE_LANG;
        if(commandSender.hasPermission(CommandPermission.STOP.getPermission()))
        {
            MinetasiaCoreApi.getInstance().shutdown();
        }
        else
            commandSender.sendMessage(Lang.NO_PERMISSION.get(lang));
        return true;
    }
}
