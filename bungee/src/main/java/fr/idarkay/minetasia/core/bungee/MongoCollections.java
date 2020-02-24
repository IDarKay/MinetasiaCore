package fr.idarkay.minetasia.core.bungee;

/**
 * File <b>MongoCollections</b> located on fr.idarkay.minetasia.core.spigot.utils
 * MongoCollections is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 09/02/2020 at 20:49
 */
public enum MongoCollections
{
    USERS("users"),
    ONLINE_USERS("online_users"),
    SERVERS("servers"),
    GROUPS("groups"),
    KITS("kits"),
    PROXY("proxy"),
    PARTY("party")

    ;
    public final String name;

    MongoCollections(String name)
    {
        this.name = name;
    }

}
