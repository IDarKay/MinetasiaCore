package fr.idarkay.minetasia.core.api.event;

import fr.idarkay.minetasia.core.api.utils.Server;
import org.apache.commons.lang.Validate;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;


/**
 * File <b>ServerRemoveEvent</b> located on fr.idarkay.minetasia.core.api.event
 * ServerRemoveEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 04/02/2020 at 18:46
 */
public class ServerUnregisterEvent extends Event
{
    private static HandlerList handlerList = new HandlerList();

    @NotNull private final String serverConfig, type, name;

    public ServerUnregisterEvent(@NotNull final Server server)
    {
        super(true);
        Validate.notNull(server);
        this.serverConfig = server.getServerConfig();
        this.type = server.getType();
        this.name = server.getName();
    }

    @NotNull
    public String getName()
    {
        return name;
    }

    @NotNull
    public String getServerConfig()
    {
        return serverConfig;
    }

    @NotNull
    public String getType()
    {
        return type;
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
