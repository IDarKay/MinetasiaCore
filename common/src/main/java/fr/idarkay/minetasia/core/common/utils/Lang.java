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

    ADD_FRIEND("add-friend", "&a%1$s add to your friend"),
    NOT_FRIEND("not-friend", "&cYou are not friend with %1$s !"),
    PLAYER_NOT_EXIST("player-not-exist", "&cThe player don't exist"),
    ONLINE("online", "online"),
    OFFLINE("offline", "offline"),
    NEED_BE_PLAYER("need-be-player", "&cYour not player"),
    NO_PERMISSION("no-permission", "&cYou don't have permission"),
    ;


    final String path;
    final String defaultMsg;

    Lang(@NotNull String path, @NotNull String defaultMsg)
    {
        this.path = path;
        this.defaultMsg = defaultMsg;
    }

    @Override
    public String get(String lang, Object... args) {
        return MinetasiaLang.get(path, defaultMsg, lang, args);
    }
}
