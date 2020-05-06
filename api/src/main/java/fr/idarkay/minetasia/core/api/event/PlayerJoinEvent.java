package fr.idarkay.minetasia.core.api.event;

import fr.idarkay.minetasia.core.api.utils.MinetasiaPlayer;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>PlayerJoinEvent</b> located on fr.idarkay.minetasia.core.api.event
 * PlayerJoinEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 23/04/2020 at 23:20
 */
public class PlayerJoinEvent extends Event
{

    private static HandlerList handlerList = new HandlerList();

    @NotNull
    private final Player bukkitPlayer;
    @NotNull
    private final MinetasiaPlayer minetasiaPlayer;

    public PlayerJoinEvent(@NotNull final MinetasiaPlayer minetasiaPlayer,  @NotNull final Player bukkitPlayer)
    {
        super(true);
        this.minetasiaPlayer = minetasiaPlayer;
        Validate.notNull(bukkitPlayer);
        this.bukkitPlayer = bukkitPlayer;
    }

    @NotNull
    public Player getPlayer()
    {
        return bukkitPlayer;
    }

    @NotNull
    public MinetasiaPlayer getMinetasiaPlayer()
    {
        return minetasiaPlayer;
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
