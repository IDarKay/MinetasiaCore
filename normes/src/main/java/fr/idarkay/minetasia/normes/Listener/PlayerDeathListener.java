package fr.idarkay.minetasia.normes.Listener;

import fr.idarkay.minetasia.normes.npc.MinetasiaNpc;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * File <b>PlayerDeathListener</b> located on fr.idarkay.minetasia.normes.Listener
 * PlayerDeathListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 28/04/2020 at 01:16
 */
public class PlayerDeathListener implements Listener
{

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event)
    {
        MinetasiaNpc.disconnectPlayerForAll(event.getEntity());
    }

}
