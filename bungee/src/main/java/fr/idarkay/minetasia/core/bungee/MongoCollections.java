/*
 * File <b>MongoCollections</b> located on fr.idarkay.minetasia.core.spigot.utils
 * MongoCollections is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 09/02/2020 at 20:49
 */
package fr.idarkay.minetasia.core.bungee;


/**
 * @deprecated use {@link fr.idarkay.minetasia.common.MongoCollections}
 */
@Deprecated
public enum MongoCollections
{
    USERS("users"),
    ONLINE_USERS("online_users"),
    SERVERS("servers"),
    GROUPS("groups"),
    KITS("kits"),
    PROXY("proxy"),
    PARTY("party"),
    SETTINGS("settings"),

    SKYBLOCK_SHOP_ITEM("skyblock_shop_item"),
    SKYBLOCK_HDV("skyblock_hdv")

    ;
    public final String name;

    MongoCollections(String name)
    {
        this.name = name;
    }

}
