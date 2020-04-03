package fr.idarkay.minetasia.normes.npc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

/**
 * File <b>PlayerJoinLeaveListener</b> located on fr.idarkay.minetasia.normes.listener
 * PlayerJoinLeaveListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 26/03/2020 at 15:07
 */
public class PlayerQuitListener implements Listener
{
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e)
    {
        final UUID uuid = e.getPlayer().getUniqueId();
        MinetasiaNpc.loadNpc.forEach(n -> n.loadPlayer.remove(uuid));
    }

}
