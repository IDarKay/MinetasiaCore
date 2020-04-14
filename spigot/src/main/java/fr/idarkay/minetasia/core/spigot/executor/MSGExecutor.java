package fr.idarkay.minetasia.core.spigot.executor;

import fr.idarkay.minetasia.core.api.utils.PlayerStatueFix;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.messages.CoreMessage;
import fr.idarkay.minetasia.core.spigot.messages.MsgMessage;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * File <b>MSGExecutor</b> located on fr.idarkay.minetasia.core.spigot.Executor
 * MSGExecutor is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 11/12/2019 at 21:51
 */
public class MSGExecutor implements TabExecutor {

    private final MinetasiaCore plugin;

    public MSGExecutor(MinetasiaCore minetasiaCore)
    {
        this.plugin = minetasiaCore;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final String lang = sender instanceof Player ? plugin.getPlayerLang(((Player) sender).getUniqueId()) : MinetasiaLang.BASE_LANG;
        if(sender.hasPermission(CommandPermission.UTILS_CHAT_MSG.getPermission()))
        {
            if(args.length > 1)
            {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    final String msg = concat(args, " ", 1);
                    final String fullMsg = Lang.MSG_FORMAT.getWithoutPrefix(lang, Lang.Argument.PLAYER_SENDER.match(sender instanceof Player ? sender.getName() : "console")
                            , Lang.Argument.PLAYER_RECEIVER.match(args[0]), Lang.Argument.MESSAGE.match(msg));


                    final Player receiver = Bukkit.getPlayer(args[0]);
                    final String name;
                    if(receiver != null) //same server
                    {
                        name = receiver.getName();
                        receiver.sendMessage(fullMsg);

                    }
                    else
                    {
                        final PlayerStatueFix playerStatueFix = plugin.getPlayerStatue(args[0]);
                        if(playerStatueFix == null)
                        {
                            sender.sendMessage(Lang.PLAYER_NOT_ONLINE.get(lang));
                            return;
                        }
                        name = playerStatueFix.getUserName();
                        plugin.publishTargetPlayer(CoreMessage.CHANNEL,
                                MsgMessage.getMessage(Lang.MSG_FORMAT, playerStatueFix.getUUID(), false,
                                        Lang.Argument.PLAYER_SENDER.match(sender instanceof Player ? sender.getName() : "console"),
                                        Lang.Argument.PLAYER_RECEIVER.match(args[0]),
                                        Lang.Argument.MESSAGE.match(msg))
                                , playerStatueFix, false, true);
                    }

                    if(name != null)
                    {
                        // socialspy
                        final String ssMsg = Lang.MSG_FORMAT_SOCIAL_SPY.getWithoutPrefix(MinetasiaLang.BASE_LANG, Lang.Argument.MESSAGE.match(fullMsg));
                        for(Player pl : plugin.socialSpyPlayer) pl.sendMessage(ssMsg);
                        sender.sendMessage(fullMsg);

                        if(sender instanceof Player)
                        {
                            final String data = plugin.getPlayerData(((Player) sender).getUniqueId(), "last_talker", String.class);
                            if(data == null || !data.equals(name))
                                plugin.setPlayerData(((Player) sender).getUniqueId(), "last_talker", name);
                        }
                    }
                });
            }
            else sender.sendMessage(Lang.DESC_MSG.get(lang));
        }
        else sender.sendMessage(Lang.NO_PERMISSION.get(lang));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(sender.hasPermission(CommandPermission.UTILS_CHAT_MSG.getPermission()))
        {
            if(args.length == 0)return plugin.getOnlinePlayersForTab();
            else if(args.length == 1) return plugin.getOnlinePlayersForTab().stream().filter(p -> p.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
            else return Collections.singletonList("<msg>");
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
