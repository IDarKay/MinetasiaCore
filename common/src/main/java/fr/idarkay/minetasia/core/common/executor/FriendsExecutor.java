package fr.idarkay.minetasia.core.common.executor;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
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

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * File <b>FriendsExecutor</b> located on fr.idarkay.minetasia.core.common.executor
 * FriendsExecutor is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 22/11/2019 at 13:07
 */
public final class FriendsExecutor implements TabExecutor {

    private final MinetasiaCore minetasiaCore;
    private final Cache<UUID, UUID> friendRequest;

    public FriendsExecutor(MinetasiaCore minetasiaCore)
    {
        this.minetasiaCore = minetasiaCore;
        friendRequest = CacheBuilder.newBuilder().expireAfterWrite(minetasiaCore.getConfig().getLong("cache.friends"), TimeUnit.MINUTES).build();
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
                    if(args.length > 0)
                    {
                        if (args.length > 1 && args[0].equalsIgnoreCase("add"))
                        {
                            UUID uuid = minetasiaCore.getPlayerUUID(args[1]);
                            if(uuid != null)
                            {
                                if(!minetasiaCore.isFriend(player.getUniqueId(), uuid))
                                {
                                    if(minetasiaCore.isPlayerOnline(uuid))
                                    {
                                        sender.sendMessage(Lang.REQUEST_SEND_FRIENDS.get(lang, args[1]));
                                        org.bukkit.entity.Player p = minetasiaCore.getServer().getPlayer(uuid);
                                        if(p != null)
                                        {
                                            newRequest(uuid, player.getUniqueId(), p);
                                        }
                                        else minetasiaCore.publish("core-cmd", "friends;" + player.getUniqueId().toString() +";" + uuid.toString());
                                    }
                                    else sender.sendMessage(Lang.PLAYER_NOT_ONLY.get(lang));
                                }
                                else sender.sendMessage(Lang.ALREADY_FRIEND.get(lang, args[1]));
                            }
                            else sender.sendMessage(Lang.PLAYER_NOT_EXIST.get(lang));
                        }
                        else if (args.length > 1 && args[0].equalsIgnoreCase("remove"))
                        {
                            UUID uuid = minetasiaCore.getPlayerUUID(args[1]);
                            if(uuid != null)
                            {
                                if(minetasiaCore.isFriend(player.getUniqueId(), uuid)) {
                                    minetasiaCore.removeFriend(player.getUniqueId(), uuid);
                                    sender.sendMessage(Lang.REMOVE_FRIEND.get(lang, args[1]));
                                }
                                else sender.sendMessage(Lang.NOT_FRIEND.get(lang, args[1]));
                            }
                            else sender.sendMessage(Lang.PLAYER_NOT_EXIST.get(lang));
                        }
                        else if (args[0].equalsIgnoreCase("accept"))
                        {
                            UUID u = friendRequest.getIfPresent(player.getUniqueId());
                            if( u != null)
                            {
                                friendRequest.invalidate(player.getUniqueId());
                                minetasiaCore.addFriend(player.getUniqueId(), u);
                                sender.sendMessage(Lang.NEW_FRIEND.get(lang, minetasiaCore.getPlayerName(u)));
                                minetasiaCore.publish("core-msg",  "NEW_FRIEND;" + u.toString() +";" + player.getName());
                            }
                        }
                        else sendHelpMsg(player, lang);
                    }
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
                        }
                        if (onlineCount > 0) onlyPlayer.deleteCharAt(onlyPlayer.length() - 1);
                        if (offlineCount > 0) offlinePlayer.deleteCharAt(offlinePlayer.length() - 1);
                        player.sendMessage( ChatColor.GREEN + Lang.ONLINE.get(lang) + ChatColor.GRAY + "(" + ChatColor.GREEN
                                + onlineCount + ChatColor.GRAY + ") "  + ChatColor.GREEN + onlyPlayer.toString());
                        player.sendMessage( ChatColor.RED + Lang.OFFLINE.get(lang) + ChatColor.GRAY + "(" + ChatColor.RED
                                + offlineCount  + ChatColor.GRAY + ") "  + ChatColor.RED + offlinePlayer.toString());
                    }
                } else sender.sendMessage(Lang.NO_PERMISSION.get(minetasiaCore.getPlayerLang(player.getUniqueId())));
            });
        } else sender.sendMessage(Lang.NEED_BE_PLAYER.get(MinetasiaLang.BASE_LANG));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull  String[] args) {
        if (sender instanceof Player) {

            if(args.length == 2)
            {
                if(args[0].equalsIgnoreCase("add"))
                {
                    return (List<String>) minetasiaCore.getOnlinePlayers().values();
                } else if(args[0].equalsIgnoreCase("remove"))
                {
                    return new ArrayList<>(minetasiaCore.getFriends(((Player) sender).getUniqueId()).values());
                }
            }
            else if(args.length == 1)
            {
                return Stream.of("add", "remove", "accept").filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
            } else  if (args.length == 0) return Arrays.asList("add", "remove", "accept");
        }
        return Collections.emptyList();
    }

    private void sendHelpMsg(Player player, String lang)
    {
        //todo : help
    }

    public void newRequest(UUID uuid, UUID uuid1, Player p)
    {
        friendRequest.put(uuid, uuid1);
        p.sendMessage(Lang.REQUEST_FRIEND.get(minetasiaCore.getPlayerLang(uuid), minetasiaCore.getPlayerName(uuid1)));
    }

}
