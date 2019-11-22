package fr.idarkay.minetasia.core.api.event;


import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>OnFRSMessageEvent</b> located on fr.idarkay.minetasia.core.api.event
 * OnFRSMessageEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 14/11/2019 at 13:48
 * @since 1.0
 *
 * The event call when {@link fr.idarkay.minetasia.core.api.MinetasiaCoreApi#publish(String, String)} have called
 * please don't call yourself the event , this very boring
 */
public final class FRSMessageEvent extends Event {

    private static HandlerList handlerList = new HandlerList();
    private final String chanel, value;


    public FRSMessageEvent(@NotNull String chanel, @NotNull String value)
    {
        super(true);
        this.chanel = chanel;
        this.value = value;
    }

    /**
     * get channel of the message, it define in {@link fr.idarkay.minetasia.core.api.MinetasiaCoreApi#publish(String, String)}
     * @return String channel
     * @since 1.0
     */
    public @NotNull String getChanel() {
        return chanel;
    }

    /**
     * get message of the message, it define in {@link fr.idarkay.minetasia.core.api.MinetasiaCoreApi#publish(String, String)}
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
