package fr.idarkay.minetasia.core.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>ServerModeRequestEvent</b> located on fr.idarkay.minetasia.core.api.event
 * ServerModeRequestEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 20/04/2020 at 20:28
 */
public class ServerModeRequestEvent extends Event
{

    @NotNull private static final HandlerList handlerList = new HandlerList();
    @NotNull private final String serverConfig;

    public ServerModeRequestEvent(@NotNull String serverConfig)
    {
        this.serverConfig = serverConfig;
    }

    public @NotNull String getServerConfig()
    {
        return serverConfig;
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
