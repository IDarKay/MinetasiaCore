package fr.idarkay.minetasia.core.bungee.event;

import net.md_5.bungee.api.event.AsyncEvent;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>MessageEvent</b> located on fr.idarkay.minetasia.core.bungee.utils
 * MessageEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 26/11/2019 at 12:56
 */
public final class MessageEvent extends AsyncEvent<MessageEvent> {

    private final String chanel, value;


    public MessageEvent(@NotNull String chanel, @NotNull String value)
    {
        super((result, error) -> {});
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
