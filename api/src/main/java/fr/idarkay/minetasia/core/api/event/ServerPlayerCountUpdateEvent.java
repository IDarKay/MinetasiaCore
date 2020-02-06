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
 * @author Alois. B. (IDarKay),
 * Created the 04/02/2020 at 18:46
 */
public class ServerPlayerCountUpdateEvent extends Event
{
    private static HandlerList handlerList = new HandlerList();

    @NotNull
    private final Server server;

    private final int newCount;
    private final int oldCount;

    public ServerPlayerCountUpdateEvent(@NotNull final Server server, int newCount, int oldCount)
    {
        super(true);
        Validate.notNull(server);
        this.server = server;
        this.newCount = newCount;
        this.oldCount = oldCount;
    }

    @NotNull
    public Server getServer()
    {
        return server;
    }

    public int getNewCount()
    {
        return newCount;
    }

    public int getOldCount()
    {
        return oldCount;
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
