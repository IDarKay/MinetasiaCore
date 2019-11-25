package fr.idarkay.minetasia.core.common.utils;

import fr.idarkay.minetasia.normes.IMinetasiaLang;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.jetbrains.annotations.NotNull;
import org.omg.CORBA.NO_PERMISSION;

/**
 * File <b>Lang</b> located on fr.idarkay.mintasia.core.common.utils
 * Lang is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 13/11/2019 at 15:44
 */
public enum Lang implements IMinetasiaLang {

    WELCOME("welcome", "&Welcome to the server :)"),
    CHANGE_USERNAME("change-username", "&a Hey We have detect you have change your username !"),
    HELP_FORMAT("help_format", "&6%1$s --> &9%2$s"),

    SELF_ADD_FRIEND("self-add-friend", "&cYou really must feel alone to want to add as a friend"),
    ALREADY_FRIEND("already-friend", "&cYour are friend with %1$s !"),
    REQUEST_SEND_FRIENDS("request-send-friends", "&aYour friends request have been send to %1$s !"),
    REQUEST_FRIEND("request-friend", "&a%1$s send to you a friend request use &6/friends accept&a for accept this else ignore the request but it's very sad"),
    NEW_FRIEND("new-friend", "&aYou are now friend with %1$s !"),
    REMOVE_FRIEND("remove-friend", "&a%1$s remove to your friend"),
    NOT_FRIEND("not-friend", "&cYou are not friend with %1$s !"),

    PLAYER_NOT_ONLY("player-not-only", "&cSorry this player isn't only !"),
    PLAYER_NOT_EXIST("player-not-exist", "&cThe player don't exist"),
    ONLINE("online", "online"),
    OFFLINE("offline", "offline"),
    NEED_BE_PLAYER("need-be-player", "&cYour not player"),
    NO_PERMISSION("no-permission", "&cYou don't have permission"),
    ;

    public static String prefix;

    final String path;
    final String defaultMsg;

    Lang(@NotNull String path, @NotNull String defaultMsg)
    {
        this.path = path;
        this.defaultMsg = defaultMsg;
    }


    public String getWithoutPrefix(String lang, Object... args) {
        return MinetasiaLang.get(path, defaultMsg, lang, args);
    }

    @Override
    public String get(String lang, Object... args) {
        return prefix + " " + MinetasiaLang.get(path, defaultMsg, lang, args);
    }
}
