package fr.idarkay.minetasia.core.api;

/**
 * File <b>PlayerStatue</b> located on fr.idarkay.minetasia.core.spigot.utils
 * PlayerStatue is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 12/12/2019 at 22:38
 */
public enum PlayerStatue {

    SOCIAL_SPY((byte) 0),
    HIDE_POWDER((byte) 1),
    ;

    public final byte by;

    PlayerStatue(byte by)
    {
        this.by = by;
    }

}
