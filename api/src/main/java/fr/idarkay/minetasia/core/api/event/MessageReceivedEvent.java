package fr.idarkay.minetasia.core.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>MessageReceivedEvent</b> located on fr.idarkay.minetasia.core.api.event
 * MessageReceivedEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 09/02/2020 at 11:57
 */
public class MessageReceivedEvent extends Event
{
    private static HandlerList handlerList = new HandlerList();
    private final String chanel, value;


    public MessageReceivedEvent(@NotNull String chanel, @NotNull String value)
    {
        super(true);
        this.chanel = chanel;
        this.value = value;
    }

    /**
     * @return String channel
     * @since 1.0
     */
    public @NotNull String getChanel() {
        return chanel;
    }

    /**
     * @return String message
     * @since 1.0
     */
    public @NotNull String getValue() {
        return value;
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
