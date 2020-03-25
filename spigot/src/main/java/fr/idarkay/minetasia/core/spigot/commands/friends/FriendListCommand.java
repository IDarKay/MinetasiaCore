package fr.idarkay.minetasia.core.spigot.commands.friends;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.Tuple;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * File <b>FriendListCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.friends
 * FriendListCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 14/03/2020 at 22:35
 */
public class FriendListCommand extends SubCommand implements FixCommand
{
    public FriendListCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_FRIENDS_LIST, CommandPermission.FRIEND, 2);
    }

    @Override
    public String getLabel()
    {
        return "list";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender,  @NotNull String[] args, @NotNull String label)
    {
        if(!(sender instanceof Player)) return;
        final Player player = (Player) sender;
        final StringBuilder onlyPlayer = new StringBuilder(),  offlinePlayer = new StringBuilder();
        int onlineCount = 0, offlineCount = 0;
        final Set<Map.Entry<UUID, Tuple<String, String>>> entries = plugin.getFriends(player.getUniqueId()).entrySet();
        if(entries.size() > 0)
        {
            final List<String> only = plugin.getPlayerOnlineName();
            for (Map.Entry<UUID, Tuple<String, String>> p : entries)
            {
                if(only.contains(p.getValue().a()))
                {
                    if(onlyPlayer.length() > 0) onlyPlayer.append(", ");
                    onlyPlayer.append(p.getValue().a());
                    onlineCount ++;
                }
                else
                {
                    if(onlyPlayer.length() > 0) onlyPlayer.append(", ");
                    offlinePlayer.append(p.getValue().a());
                    offlineCount ++;
                }
            }
        }
        final String lang = plugin.getPlayerLang(player.getUniqueId());
        player.sendMessage( ChatColor.GREEN + Lang.ONLINE.get(lang) + ChatColor.GRAY + "(" + ChatColor.GREEN
                + onlineCount + ChatColor.GRAY + ") "  + ChatColor.GREEN + onlyPlayer.toString());
        player.sendMessage( ChatColor.RED + Lang.OFFLINE.get(lang) + ChatColor.GRAY + "(" + ChatColor.RED
                + offlineCount  + ChatColor.GRAY + ") "  + ChatColor.RED + offlinePlayer.toString());
    }
}
