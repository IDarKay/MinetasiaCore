package fr.idarkay.minetasia.core.spigot.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * File <b>EventListener</b> located on fr.idarkay.minetasia.core.spigot.utils
 * EventListener is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 14/04/2020 at 16:39
 */
public interface EventListener
{
    default void onPlayerJoinEvent(Player player, PlayerJoinEvent event)
    {

    }

    default void onPlayerQuitEvent(Player player, PlayerQuitEvent event)
    {

    }

    default void onWorldChange(Player player, PlayerChangedWorldEvent event)
    {

    }

}
