package fr.idarkay.minetasia.core.api.event;

import fr.idarkay.minetasia.core.api.utils.Server;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>ServerAskedConfigEvent</b> located on fr.idarkay.minetasia.core.api.event
 * ServerAskedConfigEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 21/04/2020 at 00:32
 */
public class ServerAskedConfigEvent extends Event
{
    @NotNull private static final HandlerList handlerList = new HandlerList();
    @NotNull private final Server server;

    public ServerAskedConfigEvent(@NotNull Server server)
    {
        super(true);
        this.server = server;

    }

    public @NotNull Server getServer()
    {
        return server;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public @NotNull static HandlerList getHandlerList()
    {
        return handlerList;
    }
}
