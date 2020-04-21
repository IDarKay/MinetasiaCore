package fr.idarkay.minetasia.core.api;

/**
 * File <b>MongoCollections</b> located on fr.idarkay.minetasia.core.spigot.utils
 * MongoCollections is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
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
    PARTY("party"),
    ADVANCEMENT("advancement"),
    SETTINGS("settings"),

    SKYBLOCK_SHOP_ITEM("skyblock_shop_item"),
    SKYBLOCK_HDV("skyblock_hdv"),
    SKYBLOCK_ISLAND("skyblock_island"),
    SKYBLOCK_ONLINE_ISLAND("skyblock_online_island")
    ;
    public final String name;

    MongoCollections(String name)
    {
        this.name = name;
    }

}
