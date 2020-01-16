package fr.idarkay.minetasia.core.api;

/**
 * File <b>Command</b> located on fr.idarkay.minetasia.core.api
 * Command is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 29/11/2019 at 12:38
 */
public enum Command {

    FRIEND(((byte) (0))),
    LANG(((byte) (1))),
    PERMISSION(((byte) (2))),
    HUB(((byte) (3))),
    MONEY(((byte) (4))),
    MSG(((byte) (5))),
    TP(((byte) (6))),
    PARTY_XP_BOOST(((byte) (7))),
    ;

    public final byte by;

    Command(byte by)
    {
        this.by = by;
    }

}
