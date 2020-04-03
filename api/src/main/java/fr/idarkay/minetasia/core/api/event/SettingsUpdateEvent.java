package fr.idarkay.minetasia.core.api.event;

import fr.idarkay.minetasia.core.api.SettingsKey;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>SettingsUpdateEvent</b> located on fr.idarkay.minetasia.core.api.event
 * SettingsUpdateEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 29/03/2020 at 21:29
 */
public class SettingsUpdateEvent<T> extends Event
{
    private static HandlerList handlerList = new HandlerList();
    private final T value;
    private final SettingsKey<T> settingsKey;

    public SettingsUpdateEvent(SettingsKey<T> settingsKey, T value)
    {
        super(true);
        this.settingsKey = settingsKey;
        this.value = value;
    }

    /**
     * get the new value of the key
     * @return value
     */
    public T getValue()
    {
        return value;
    }

    /**
     * @return the key
     */
    public SettingsKey<T> getSettingsKey()
    {
        return settingsKey;
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
