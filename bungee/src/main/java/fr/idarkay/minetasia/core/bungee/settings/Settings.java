package fr.idarkay.minetasia.core.bungee.settings;

import fr.idarkay.minetasia.core.bungee.MinetasiaCoreBungee;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * File <b>Settings</b> located on fr.idarkay.minetasia.core.spigot.settings
 * Settings is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 31/03/2020 at 15:04
 */
public final class Settings<T>
{
    @NotNull
    private final MinetasiaCoreBungee plugin;
    @NotNull
    private final SettingsKey<T> settingsKey;
    @NotNull
    private final Class<T> clazz;
    @Nullable
    private T value = null;

    public Settings(@NotNull MinetasiaCoreBungee plugin, @NotNull SettingsKey<T> settingsKey, @NotNull Class<T> clazz, @Nullable T value)
    {
        this.plugin = plugin;
        this.clazz = clazz;
        this.settingsKey = settingsKey;
        this.value = value;
    }

    @Nullable
    public T getValue()
    {
        return value;
    }

    public void setValueLocal(@NotNull T value)
    {
        this.value = value;
    }

    @NotNull
    public Class<T> getClazz()
    {
        return clazz;
    }

}
