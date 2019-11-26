package fr.idarkay.minetasia.core.spigot.executor;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * File <b>LangExecutor</b> located on fr.idarkay.minetasia.core.common.executor
 * LangExecutor is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 25/11/2019 at 18:55
 */
public final class LangExecutor implements TabExecutor {

    private final MinetasiaCore minetasiaCore;

    public LangExecutor(MinetasiaCore minetasiaCore)
    {
        this.minetasiaCore = minetasiaCore;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(minetasiaCore, () -> {
            if(sender.hasPermission("core.friends"))
            {
                if (sender.hasPermission("core.friends.admin") && args.length > 0)
                {
                    UUID uuid = minetasiaCore.getPlayerUUID(args[0]);
                    if(uuid != null)
                    {
                        sender.sendMessage(Lang.GET_LANG.get((sender instanceof Player ? minetasiaCore.getPlayerLang(((org.bukkit.entity.Player) sender).getUniqueId()) : MinetasiaLang.BASE_LANG), args[0], minetasiaCore.getPlayerLang(uuid)));
                    }
                    else sender.sendMessage(Lang.PLAYER_NOT_EXIST.get(sender instanceof Player ? minetasiaCore.getPlayerLang(((org.bukkit.entity.Player) sender).getUniqueId()) : MinetasiaLang.BASE_LANG));
                }
                else if(sender instanceof Player)
                {
                   Bukkit.getScheduler().runTask(minetasiaCore, () -> minetasiaCore.getGui().openLangInventory((Player) sender));
                }
                else sender.sendMessage(Lang.NEED_BE_PLAYER.get(MinetasiaLang.BASE_LANG));
            }
            else sender.sendMessage(Lang.NO_PERMISSION.get(sender instanceof Player ? minetasiaCore.getPlayerLang(((org.bukkit.entity.Player) sender).getUniqueId()) : MinetasiaLang.BASE_LANG));
        });
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length > 0 && sender.hasPermission("core.lang.admin")) return minetasiaCore.getOnlinePlayers().values().stream().filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
        return null;
    }
}
