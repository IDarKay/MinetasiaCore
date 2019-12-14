package fr.idarkay.minetasia.core.spigot.commands.tp;

import fr.idarkay.minetasia.core.api.utils.PlayerStatueFix;
import fr.idarkay.minetasia.core.api.utils.Server;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * File <b>SecondArgsCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.tp
 * SecondArgsCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 13/12/2019 at 14:25
 */
public class SecondArgsCommand extends SubCommand implements FlexibleCommand {

    public SecondArgsCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, null, CommandPermission.TP, 3);
    }

    @Override
    public String getLabel() {
        return "<server / player / <X> <Y> <Z>>";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        if(args[1].contains("#"))
        {
            if(!args[0].equalsIgnoreCase("@a"))
            {
                Server s = plugin.getServer(args[1]);
                if(s != null)
                {
                    Player p = Bukkit.getPlayer(args[0]);
                    if(p != null)
                    {
                        plugin.movePlayerToServer(p, s);
                    }
                    else sender.sendMessage(Lang.PLAYER_NOT_ONLY.get(getLangOfSender(sender)));
                }
                else sender.sendMessage(Lang.SERVER_NOT_FOUND.get(getLangOfSender(sender)));
            }
            else sender.sendMessage(Lang.INCOMPATIBLE_CMD_TP.get(getLangOfSender(sender)));
        }
        else if (args.length > 3)
        {
            try
            {
                int x = Integer.parseInt(args[1]);
                int y = Integer.parseInt(args[2]);
                int z = Integer.parseInt(args[3]);
                World w = null;
                if(args.length > 4)
                {
                    w = Bukkit.getWorld(args[4]);
                    if(w == null)
                    {
                        sender.sendMessage(Lang.WORLD_NOT_FOUND.get(getLangOfSender(sender)));
                        return;
                    }
                }
                if(args[0].equalsIgnoreCase("@a"))
                {
                    if(w == null)
                    {
                        if(sender instanceof Player) w = ((Player) sender).getWorld();
                        else
                        {
                            sender.sendMessage(Lang.WORLD_NOT_FOUND.get(getLangOfSender(sender)));
                            return;
                        }
                        Location l = new Location(w, x, y, z);
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            for(Player p : Bukkit.getOnlinePlayers())
                            {
                                p.teleport(l);
                            }
                        });
                    }
                }
                else
                {
                    Player p = Bukkit.getPlayer(args[0]);
                    if(p != null)
                    {
                        if(w == null) w = p.getWorld();
                        Location l = new Location(w, x, y, z);
                        Bukkit.getScheduler().runTask(plugin, () ->  p.teleport(l));
                    }
                    else sender.sendMessage(Lang.PLAYER_NOT_ONLY.get(getLangOfSender(sender)));
                }
            }
            catch (IllegalArgumentException e)
            {
                sender.sendMessage(Lang.ILLEGAL_NUMBER_VALUE.get(getLangOfSender(sender)));
            }
        }
        else
        {
            if(args[0].equalsIgnoreCase("@a"))
            {
                Player p = Bukkit.getPlayer(args[1]);
                if(p != null)
                {
                    Bukkit.getScheduler().runTask(plugin, () ->
                    {
                        for(Player player : Bukkit.getOnlinePlayers()) player.teleport(p);
                    });
                }
                else sender.sendMessage(Lang.PLAYER_NOT_EXIST.get(getLangOfSender(sender)));
            }
            else
            {
                Player p1 = Bukkit.getPlayer(args[0]);
                if(p1 == null)
                {
                    sender.sendMessage(Lang.PLAYER_NOT_EXIST.get(getLangOfSender(sender)));
                    return;
                }
                Player p0 = Bukkit.getPlayer(args[1]);
                if(p0 != null)
                {
                    Bukkit.getScheduler().runTask(plugin, () -> p1.teleport(p0));
                }
                else
                {
                    PlayerStatueFix p = plugin.getPlayerStatue(args[1]);
                    if(p != null && p1 != null)
                    {
                        plugin.movePlayerToServer(p1, p.getServer());
                        //todo: move ti player
                    }
                    else sender.sendMessage(Lang.PLAYER_NOT_EXIST.get(getLangOfSender(sender)));
                }
            }
        }
    }

    @Override
    public boolean isAllPossibilities() {
        return true;
    }

    @Override
    public List<String> getPossibilities() {
        List<String> b = new ArrayList<>();
        b.addAll(plugin.getOnlinePlayersForTab());
        b.addAll(plugin.getServers().keySet());
        return b;
    }
}
