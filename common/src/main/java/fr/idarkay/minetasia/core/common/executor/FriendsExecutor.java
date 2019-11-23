package fr.idarkay.minetasia.core.common.executor;

import fr.idarkay.minetasia.core.common.MinetasiaCore;
import fr.idarkay.minetasia.core.common.utils.Lang;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * File <b>FriendsExecutor</b> located on fr.idarkay.minetasia.core.common.executor
 * FriendsExecutor is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 22/11/2019 at 13:07
 */
public final class FriendsExecutor implements TabExecutor {

    private final MinetasiaCore minetasiaCore;

    public FriendsExecutor(MinetasiaCore minetasiaCore)
    {
        this.minetasiaCore = minetasiaCore;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull  String[] args) {
        if(sender instanceof Player)
        {
            Bukkit.getScheduler().runTaskAsynchronously(minetasiaCore, () -> {
                Player player = (Player) sender;
                if(player.hasPermission("core.friends"))
                {
                    String lang = minetasiaCore.getPlayerLang(player.getUniqueId());
                    if(args.length > 1)
                    {
                        if (args[0].equalsIgnoreCase("add"))
                        {

                        }
                        else if (args[0].equalsIgnoreCase("remove"))
                        {
                            UUID uuid = minetasiaCore.getPlayerUUID(args[1]);
                            if(uuid != null)
                            {
                                if(minetasiaCore.removeFriend(player.getUniqueId(), uuid)) sender.sendMessage(Lang.ADD_FRIEND.get(lang, args[1]));
                                else sender.sendMessage(Lang.NOT_FRIEND.get(lang, args[1]));
                            }
                            else sender.sendMessage(Lang.PLAYER_NOT_EXIST.get(lang));
                        }
                        else if (args[0].equalsIgnoreCase("accept"))
                        {

                        }
                        else sendHelpMsg(player);
                    }
                    else if (args.length > 0) sendHelpMsg(player);
                    else
                    {
                        StringBuilder onlyPlayer = new StringBuilder(),  offlinePlayer = new StringBuilder();
                        int onlineCount = 0, offlineCount = 0;
                        for (Map.Entry<UUID, String> p : minetasiaCore.getFriends(player.getUniqueId()).entrySet())
                        {
                            if(minetasiaCore.isPlayerOnline(p.getKey()))
                            {
                                onlyPlayer.append(p.getValue()).append(", ");
                                onlineCount ++;
                            }
                            else
                            {
                                offlinePlayer.append(p.getValue()).append(", ");
                                offlineCount ++;
                            }
                            if (onlineCount > 0) onlyPlayer.deleteCharAt(onlyPlayer.length());
                            if (offlineCount > 0) offlinePlayer.deleteCharAt(onlyPlayer.length());
                            player.sendMessage( ChatColor.GREEN + Lang.ONLINE.get(lang) + ChatColor.GRAY + "(" + ChatColor.GREEN
                                    + onlineCount + ChatColor.GRAY + ") "  + ChatColor.GREEN + onlyPlayer.toString());
                            player.sendMessage( ChatColor.RED + Lang.OFFLINE.get(lang) + ChatColor.GRAY + "(" + ChatColor.RED
                                    + offlineCount  + ChatColor.GRAY + ") "  + ChatColor.RED + onlyPlayer.toString());
                        }
                    }
                } else sender.sendMessage(Lang.NO_PERMISSION.get(minetasiaCore.getPlayerLang(player.getUniqueId())));
            });
        } else sender.sendMessage(Lang.NEED_BE_PLAYER.get(MinetasiaLang.BASE_LANG));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull  String[] args) {
        return null;
    }

    private void sendHelpMsg(Player player)
    {
        //todo: todo
    }

}
