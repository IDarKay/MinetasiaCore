package fr.idarkay.minetasia.core.spigot.Executor;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * File <b>RExecutor</b> located on fr.idarkay.minetasia.core.spigot.Executor
 * RExecutor is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 11/12/2019 at 22:31
 */
public class RExecutor implements TabExecutor {
                        // player from  player to

    private final MinetasiaCore minetasiaCore;

    public RExecutor(MinetasiaCore minetasiaCore)
    {
        this.minetasiaCore = minetasiaCore;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final String lang = sender instanceof Player ? minetasiaCore.getPlayerLang(((Player) sender).getUniqueId()) : MinetasiaLang.BASE_LANG;
        if(sender.hasPermission(CommandPermission.UTILS_CHAT_MSG.getPermission()) && sender instanceof Player)
        {
            if(args.length > 0)
            {
                Bukkit.getScheduler().runTaskAsynchronously(minetasiaCore, () -> {
                    try
                    {
                        UUID u = UUID.fromString(minetasiaCore.getPlayerData(((Player) sender).getUniqueId(), "last_talker"));
                            if(minetasiaCore.isPlayerOnline(u))
                            {
                                String msg = concat(args, " ", 1);
                                sender.sendMessage(Lang.MSG_FORMAT.getWithoutPrefix(lang, Lang.Argument.PLAYER_SENDER.match(sender.getName()), Lang.Argument.PLAYER_RECEIVER.match(args[0]), Lang.Argument.MESSAGE.match(msg)));
                                minetasiaCore.publish("core-msg","MSG_FORMAT;" + u.toString() + ";false;" + Lang.Argument.PLAYER_SENDER.name() + "\\" + sender.getName() + ";" + Lang.Argument.PLAYER_RECEIVER.name() + "\\" + args[0] + ";" + Lang.Argument.MESSAGE + "\\" + msg.replace(';', ':').replace('\\', '/'));
                            }
                            else sender.sendMessage(Lang.PLAYER_NOT_ONLY.get(lang));
                    }
                    catch (Exception e)  // if uuid is wrong or null
                    {
                        sender.sendMessage(Lang.NO_PREVIOUS_MSG.get(lang));
                    }
                });
            }
            else sender.sendMessage(Lang.DESC_R.get(lang));
        }
        else sender.sendMessage(Lang.NO_PERMISSION.get(lang));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(sender.hasPermission(CommandPermission.UTILS_CHAT_MSG.getPermission()))
        {
            return Collections.singletonList("<msg>");
        }
        return null;
    }

    private @NotNull String concat(String[] array, String split, int indexFrom)
    {
        StringBuilder result = new StringBuilder();
        for(int i = indexFrom; i < array.length; i++) result.append(i != indexFrom && split != null ? split : "").append(array[i]);
        return result.toString();
    }

}
