package fr.idarkay.minetasia.core.spigot.utils;

/**
 * File <b>FRSKey</b> located on fr.idarkay.minetasia.core.spigot.utils
 * FRSKey is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 05/02/2020 at 16:31
 */
@Deprecated
public enum FRSKey
{

    USERS("users"),
    STATS(USERS.key + "/%1$s/stats.json"),
    FIENDS(USERS.key + "/%1$s/friends.json"),
    DATA(USERS.key + "/%1$s/data.json"),
    CUSTOM_DATA(USERS.key + "/%1$s/custom_data.json"),


    ;
    private final String key;

    FRSKey(String key)
    {
        this.key = key;
    }

    public String getKey(Object... args)
    {
        return String.format(key, args);
    }
}
