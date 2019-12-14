package fr.idarkay.minetasia.core.spigot.commands.tp;

import fr.idarkay.minetasia.core.api.utils.PlayerStatueFix;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * File <b>UserFirstArgsCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.tp
 * UserFirstArgsCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 13/12/2019 at 13:56
 */
public class UserFirstArgsCommand extends StepCommand implements FlexibleCommand {

    public UserFirstArgsCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_TP_USER, CommandPermission.TP, 2);
        child.add(new SecondArgsCommand(plugin));
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        if(sender instanceof Player)
        {
            final String lang = plugin.getPlayerLang(((Player) sender).getUniqueId());
            Player p0 = Bukkit.getPlayer(args[0]);
            if(p0 != null)
            {
                Bukkit.getScheduler().runTask(plugin, () -> ((Player) sender).teleport(p0));
            }
            else
            {
                PlayerStatueFix p = plugin.getPlayerStatue(args[0]);
                if(p != null)
                {
                    plugin.movePlayerToServer((Player) sender, p.getServer());
                    //todo: move ti player
                }
                else sender.sendMessage(Lang.PLAYER_NOT_EXIST.get(lang));
            }
        }
        else super.execute(plugin, sender, args, label);
    }

    @Override
    public String getLabel() {
        return "<user>";
    }

    @Override
    public boolean isAllPossibilities() {
        return true;
    }

    @Override
    public List<String> getPossibilities() {
        return plugin.getOnlinePlayersForTab();
    }
}
