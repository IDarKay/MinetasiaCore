package fr.idarkay.minetasia.core.api.event;

import fr.idarkay.minetasia.core.api.utils.Server;
import org.apache.commons.lang.Validate;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>ServerPlayerCountUpdateEvent</b> located on fr.idarkay.minetasia.core.api.event
 * ServerPlayerCountUpdateEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 04/02/2020 at 18:46
 */
public class ServerPlayerCountUpdateEvent extends Event
{
    private static HandlerList handlerList = new HandlerList();

    @NotNull
    private final Server server;

    private final int count;

    public ServerPlayerCountUpdateEvent(@NotNull final Server server, int count)
    {
        super(false);
        Validate.notNull(server);
        this.server = server;
        this.count = count;
    }

    @NotNull
    public Server getServer()
    {
        return server;
    }

    public int getCount()
    {
        return count;
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
