package fr.idarkay.minetasia.core.spigot.Executor;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>HubExecutor</b> located on fr.idarkay.minetasia.core.spigot.Executor
 * HubExecutor is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 08/12/2019 at 21:18
 */
public class HubExecutor implements CommandExecutor {

    private final MinetasiaCore minetasiaCore;

    public HubExecutor(MinetasiaCore minetasiaCore)
    {
        this.minetasiaCore = minetasiaCore;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull  String[] args) {
        if(sender instanceof Player)
            if(sender.hasPermission(CommandPermission.LANG.getPermission()))
            {
                minetasiaCore.movePlayerToHub((Player) sender);
            }
            else sender.sendMessage(Lang.NO_PERMISSION.get(minetasiaCore.getPlayerLang(((org.bukkit.entity.Player) sender).getUniqueId())));
        return true;
    }
}
