package fr.idarkay.minetasia.core.spigot.listener;

import fr.idarkay.minetasia.core.api.Command;
import fr.idarkay.minetasia.core.api.GeneralPermission;
import fr.idarkay.minetasia.core.api.PlayerStatue;
import fr.idarkay.minetasia.core.api.ServerPhase;
import fr.idarkay.minetasia.core.api.event.PlayerPermissionLoadEndEvent;
import fr.idarkay.minetasia.core.api.utils.Boost;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * File <b>PlayerListener</b> located on fr.idarkay.minetasia.core.common.listener
 * PlayerListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
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
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getPlayerManager().load(e.getPlayer().getUniqueId());
            plugin.getPermissionManager().loadUser(e.getPlayer().getUniqueId(), false);
            long i = plugin.getPlayer(e.getPlayer().getUniqueId()).getStatus();
            plugin.getPlayerListRunnable().updatePlayer(e.getPlayer());

            if(plugin.isBollTrue(i, PlayerStatue.SOCIAL_SPY.by)) plugin.socialSpyPlayer.add(e.getPlayer());


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
        plugin.getPlayerManager().removePlayer(e.getPlayer().getUniqueId());
        plugin.getPartyManager().disconnectPlayer(e.getPlayer().getUniqueId());
        plugin.getPermissionManager().removePlayer(e.getPlayer().getUniqueId());
        plugin.socialSpyPlayer.remove(e.getPlayer());
        e.setQuitMessage(null);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPermissionLoadEndEvent(PlayerPermissionLoadEndEvent e)
    {
        if(plugin.getServerPhase() == ServerPhase.GAME && !plugin.isHub())
        {
            if(Bukkit.getOnlinePlayers().size() > plugin.getMaxPlayerCount())
            {
                if(!e.getPlayer().hasPermission(GeneralPermission.ADMIN_SPECTATOR.getPermission()))
                {
                    plugin.movePlayerToHub(e.getPlayer());
                    return;
                }
            }
        }
    }
}
