package fr.idarkay.minetasia.core.spigot.listener;

import fr.idarkay.minetasia.core.api.Command;
import fr.idarkay.minetasia.core.api.GeneralPermission;
import fr.idarkay.minetasia.core.api.ServerPhase;
import fr.idarkay.minetasia.core.api.event.PlayerPermissionLoadEndEvent;
import fr.idarkay.minetasia.core.api.utils.Boost;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.user.Player;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.core.spigot.utils.PlayerStatue;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

/**
 * File <b>PlayerListener</b> located on fr.idarkay.minetasia.core.common.listener
 * PlayerListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 20/11/2019 at 14:12
 */
public class PlayerListener implements Listener {

    private final MinetasiaCore plugin;

    public PlayerListener(MinetasiaCore plugin)
    {
        this.plugin = plugin;
    }



    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinEvent(PlayerJoinEvent e)
    {
        e.setJoinMessage(null);
        plugin.getPermissionManager().loadUser(e.getPlayer().getUniqueId(), false);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->{
            String statue = plugin.getPlayerData(e.getPlayer().getUniqueId(), "statue");

            try {
                int i = Integer.parseInt(statue);

                if(plugin.isBollTrue(i, PlayerStatue.SOCIAL_SPY.by)) plugin.socialSpyPlayer.add(e.getPlayer());

            } catch (Exception ignore) {
                plugin.setPlayerData(e.getPlayer().getUniqueId(), "statue", String.valueOf(0x0));
            }

            if(plugin.isCommandEnable(Command.PARTY_XP_BOOST))
            {
                Boost boost = plugin.getPlayerPartyBoost(e.getPlayer().getUniqueId());
                if(boost.getBoost().size() > 0)
                {
                    plugin.getPartyServerBoost().upgrade(boost);
                    boost.getBoost().forEach((k, v) -> {
                        if(v > 0)
                        {
                            final float a = plugin.getPartyServerBoost().getBoost(k);
                            final float m = MinetasiaCore.limit.get(k);
                            Bukkit.getOnlinePlayers().forEach(p -> {
                                String lang = plugin.getPlayerLang(p.getUniqueId());
                                p.sendMessage(Lang.PLAYER_BOOST.get(lang, Lang.Argument.PLAYER.match(e.getPlayer().getName()), Lang.Argument.MONEY_TYPE.match(k.name())
                                        , Lang.Argument.ACTUAL_BOOST.match(a),  Lang.Argument.MAX_BOOST.match(m)));
                            });
                        }
                    });
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuitEvent(PlayerQuitEvent e)
    {
        plugin.getPermissionManager().removePlayer(e.getPlayer().getUniqueId());
        plugin.socialSpyPlayer.remove(e.getPlayer());
        e.setQuitMessage(null);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPermissionLoadEndEvent(PlayerPermissionLoadEndEvent e)
    {
        if(plugin.getServerPhase() == ServerPhase.GAME)
        {
            if(Bukkit.getOnlinePlayers().size() > plugin.getMaxPlayerCount())
            {
                if(!e.getPlayer().hasPermission(GeneralPermission.ADMIN_SPECTATOR.getPermission()))
                {
                    plugin.movePlayerToHub(e.getPlayer());
                }
            }
        }
    }
}
