package fr.idarkay.minetasia.core.api.event;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>PlayerPermissionLoadEnd</b> located on fr.idarkay.minetasia.core.api.event
 * PlayerPermissionLoadEnd is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 26/01/2020 at 11:28
 */
public class PlayerPermissionLoadEndEvent extends Event
{
    private static HandlerList handlerList = new HandlerList();

    @NotNull
    private final Player player;

    public PlayerPermissionLoadEndEvent(@NotNull final Player player)
    {
        super(false);
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
