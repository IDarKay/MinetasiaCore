package fr.idarkay.minetasia.normes.Listener;

import fr.idarkay.minetasia.normes.hologram.Hologram;
import fr.idarkay.minetasia.normes.npc.MinetasiaNpc;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * File <b>PlayerQuitListener</b> located on fr.idarkay.minetasia.normes.Listener
 * PlayerQuitListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 07/04/2020 at 19:54
 */
public class PlayerQuitListener implements Listener
{
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event)
    {
        Hologram.disconnectPlayerForAll(event.getPlayer());
        MinetasiaNpc.disconnectPlayerForAll(event.getPlayer());
    }
}
