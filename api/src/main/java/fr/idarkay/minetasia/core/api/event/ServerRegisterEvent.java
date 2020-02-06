package fr.idarkay.minetasia.core.api.event;

import fr.idarkay.minetasia.core.api.utils.Server;
import org.apache.commons.lang.Validate;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>ServerRegisterEvent</b> located on fr.idarkay.minetasia.core.api.event
 * ServerRegisterEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 04/02/2020 at 18:46
 */
public class ServerRegisterEvent extends Event
{
    private static HandlerList handlerList = new HandlerList();

    @NotNull
    private final Server server;

    public ServerRegisterEvent(@NotNull final Server server)
    {
        super(true);
        Validate.notNull(server);
        this.server = server;
    }

    @NotNull
    public Server getServer()
    {
        return server;
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
