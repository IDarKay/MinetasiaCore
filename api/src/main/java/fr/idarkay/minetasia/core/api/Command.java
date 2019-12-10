package fr.idarkay.minetasia.core.api;

/**
 * File <b>Command</b> located on fr.idarkay.minetasia.core.api
 * Command is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 29/11/2019 at 12:38
 */
public enum Command {

    FRIEND(((byte) (0))),
    LANG(((byte) (1))),
    PERMISSION(((byte) (2))),
    HUB(((byte) (3))),
    ;

    public final byte by;

    Command(byte by)
    {
        this.by = by;
    }

}
