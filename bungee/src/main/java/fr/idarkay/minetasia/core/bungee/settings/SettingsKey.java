package fr.idarkay.minetasia.core.bungee.settings;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * File <b>SettingsKey</b> located on fr.idarkay.minetasia.core.api
 * SettingsKey is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 29/03/2020 at 21:06
 */
public final class SettingsKey<T>
{
    private static final List<SettingsKey<?>> value = new ArrayList<>();

    public static final SettingsKey<String> MOTD = new SettingsKey<>("MOTD", String.class);
    public static final SettingsKey<List> WHITELIST = new SettingsKey<>("WHITELIST", List.class);
    public static final SettingsKey<List> MAINTENANCE = new SettingsKey<>("MAINTENANCE", List.class);


    private final int hash;
    private final Class<T> clazz;
    private final String name;

    private SettingsKey(String name, Class<T> clazz)
    {
        this.name = name;
        this.hash = name.hashCode();
        this.clazz = clazz;
        value.add(this);
    }

    public String name()
    {
        return name;
    }

    public int getHash()
    {
        return hash;
    }

    public Class<T> getClazz()
    {
        return clazz;
    }

    @Nullable
    public static SettingsKey<?> fromHash(int hash)
    {
        for (SettingsKey<?> s : value)
        {
            if(s.hash == hash) return s;
        }
        return null;
    }

    public static SettingsKey<?> fromName(@NotNull String name)
    {
        for (SettingsKey<?> s : value)
        {
            if(s.name.equalsIgnoreCase(name)) return s;
        }
        return null;
    }

    public static SettingsKey<?>[] values;

    public static SettingsKey<?>[] values()
    {
        if(values == null)
        {
            values = value.toArray(new SettingsKey<?>[0]);
        }
        return values;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SettingsKey<?> that = (SettingsKey<?>) o;
        return that.hash == this.hash;
    }

    @Override
    public int hashCode()
    {
        return hash;
    }
}
