package fr.idarkay.minetasia.core.bungee.event;

import net.md_5.bungee.api.event.AsyncEvent;
import net.md_5.bungee.api.plugin.Event;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>FRSMessageEvent</b> located on fr.idarkay.minetasia.core.bungee.utils
 * FRSMessageEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 26/11/2019 at 12:56
 */
// AsyncEvent<FRSMessageEvent> todo: this
public final class FRSMessageEvent extends Event {

    private final String chanel, value;


    public FRSMessageEvent(@NotNull String chanel, @NotNull String value)
    {
        this.chanel = chanel;
        this.value = value;
    }

    /**
     * get channel of the message, it define in
     * @return String channel
     * @since 1.0
     */
    public @NotNull String getChanel() {
        return chanel;
    }

    /**
     * get message of the message, it define in
     * @return String message
     * @since 1.0
     */
    public @NotNull String getValue() {
        return value;
    }

}
