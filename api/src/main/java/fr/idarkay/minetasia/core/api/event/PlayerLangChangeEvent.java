package fr.idarkay.minetasia.core.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>PlayerLangChangeEvent</b> located on fr.idarkay.minetasia.core.api.event
 * PlayerLangChangeEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 16/04/2020 at 00:48
 *
 * ASYNC EVENT
 *
 */
public class PlayerLangChangeEvent extends Event
{

    private static HandlerList handlerList = new HandlerList();
    private final Player player;
    private final String newLand;

    public PlayerLangChangeEvent(Player player, String newLand)
    {
        super(true);
        this.player = player;
        this.newLand = newLand;
    }

    public String getNewLand()
    {
        return newLand;
    }

    public Player getPlayer()
    {
        return player;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList()
    {
        return handlerList;
    }

}
