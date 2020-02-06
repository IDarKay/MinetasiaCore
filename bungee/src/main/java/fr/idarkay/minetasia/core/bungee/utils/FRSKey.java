package fr.idarkay.minetasia.core.bungee.utils;

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
public enum FRSKey
{

    USERS("users"),
    DATA(USERS.key + "/%1$s/data.json"),


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
