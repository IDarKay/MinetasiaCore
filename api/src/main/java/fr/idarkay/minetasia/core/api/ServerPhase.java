package fr.idarkay.minetasia.core.api;

/**
 * File <b>ServerPhase</b> located on fr.idarkay.minetasia.core.api
 * ServerPhase is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 26/01/2020 at 10:48
 */
public enum ServerPhase
{
    /**--
     * LOAD {@code =>} startup of server load map ; player can't connect <br>
     * <b> this phase is default phase </b>
     */
    LOAD,

    /**
     * STARTUP {@code =>} player can join, and leave the lobby waiting the game doesn't started
     */
    STARTUP,
    /**
     * GAME {@code =>} if player join game need set spectator if server full go back lobby
     */
    GAME,
    /**
     * END {@code =>} end of the game redirection player ton hb and stop server
     */
    END,
}
