package fr.idarkay.minetasia.core.spigot.executor;

import fr.idarkay.minetasia.core.api.utils.PlayerStatueFix;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.messages.CoreMessage;
import fr.idarkay.minetasia.core.spigot.messages.MsgMessage;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * File <b>RExecutor</b> located on fr.idarkay.minetasia.core.spigot.Executor
 * RExecutor is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
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
        if(!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "need be a player !");
            return true;
        }

        if(sender.hasPermission(CommandPermission.UTILS_CHAT_MSG.getPermission()))
        {
            if(args.length > 0)
            {
                Bukkit.getScheduler().runTaskAsynchronously(minetasiaCore, () -> {

                    final String lastTalker = minetasiaCore.getPlayerData(((Player) sender).getUniqueId(), "last_talker", String.class);
                    if(lastTalker == null)
                    {
                        sender.sendMessage(Lang.NO_PREVIOUS_MSG.get(lang));
                        return;
                    }

                    final String msg = concat(args, " ", 0);
                    final String fullMsg = Lang.MSG_FORMAT.getWithoutPrefix(lang, Lang.Argument.PLAYER_SENDER.match(sender.getName()), Lang.Argument.PLAYER_RECEIVER.match(lastTalker), Lang.Argument.MESSAGE.match(msg));

                    final Player player = Bukkit.getPlayer(label);

                    if(player != null)
                    {
                        player.sendMessage(fullMsg);
                    }
                    else
                    {
                        final PlayerStatueFix playerStatueFix = minetasiaCore.getPlayerStatue(lastTalker);

                        if(playerStatueFix != null)
                        {
                            minetasiaCore.publishTargetPlayer(CoreMessage.CHANNEL,
                                    MsgMessage.getMessage(Lang.MSG_FORMAT, playerStatueFix.getUUID(), false,
                                            Lang.Argument.PLAYER_SENDER.match(sender.getName()),
                                            Lang.Argument.PLAYER_RECEIVER.match(lastTalker),
                                            Lang.Argument.MESSAGE.match(msg))
                                    , playerStatueFix, false, true);
                        }
                        else
                        {
                            sender.sendMessage(Lang.PLAYER_NOT_ONLINE.get(lang));
                            return;
                        }
                    }

                    // socialspy
                    final String ssMsg = Lang.MSG_FORMAT_SOCIAL_SPY.getWithoutPrefix(MinetasiaLang.BASE_LANG, Lang.Argument.MESSAGE.match(fullMsg));
                    for(Player pl : minetasiaCore.socialSpyPlayer) pl.sendMessage(ssMsg);
                    sender.sendMessage(fullMsg);

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
