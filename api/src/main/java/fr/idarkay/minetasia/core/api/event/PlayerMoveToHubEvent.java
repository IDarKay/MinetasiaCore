package fr.idarkay.minetasia.core.api.event;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>PlayerMoveToHubEvent</b> located on fr.idarkay.minetasia.core.api.event
 * PlayerMoveToHubEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 19/01/2020 at 13:24
 */
public class PlayerMoveToHubEvent extends Event
{
    private static HandlerList handlerList = new HandlerList();

    @NotNull
    private final Player player;

    public PlayerMoveToHubEvent(@NotNull final Player player)
    {
        super(true);
        Validate.notNull(player);
        this.player = player;
    }

    @NotNull
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
