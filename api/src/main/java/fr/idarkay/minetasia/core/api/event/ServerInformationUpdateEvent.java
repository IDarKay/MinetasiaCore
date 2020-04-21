package fr.idarkay.minetasia.core.api.event;

import fr.idarkay.minetasia.core.api.utils.Server;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>ServerInformationUpdate</b> located on fr.idarkay.minetasia.core.api.event
 * ServerInformationUpdate is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 20/04/2020 at 22:27
 */
public class ServerInformationUpdateEvent extends Event
{
    @NotNull private static final HandlerList handlerList = new HandlerList();
    @NotNull private final Server server;
    @NotNull private final String serverConfig;
    private final int maxPlayer;

    public ServerInformationUpdateEvent(@NotNull Server server, @NotNull String serverConfig, int maxPlayer)
    {
        super(true);
        this.server = server;
        this.serverConfig = serverConfig;
        this.maxPlayer = maxPlayer;
    }

    public @NotNull String getServerConfig()
    {
        return serverConfig;
    }

    public int getMaxPlayer()
    {
        return maxPlayer;
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
