package fr.idarkay.minetasia.core.api.utils;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * File <b>Server</b> located on fr.idarkay.minetasia.core.api.utils
 * Server is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 26/11/2019 at 20:30
 */
public interface Server {

    /**
     * get the time in millis of when the server was created
     * @return long time in millis
     */
    long getCreatTime();

    /**
     * @return the ip of the server
     */
    @NotNull
    String getIp();

    /**
     * @return the server port
     */
    int getPort();

    /**
     * @return the uuid of the server
     */
    @NotNull
    UUID getUuid();

    /**
     * get the type of the server
     * the type represent what is the server : hub , sheepWars ...
     * @return the type of the server
     */
    @NotNull
    String getType();

    /**
     * get the full name of the server, the name is : {@link Server#getType()} + # + {@link Server#getUuid()}
      * @return the full name of the server
     */
    @NotNull
    String getName();

    /**
     * get the player count of the server
     * the player count is update all 10 second
     * @return the number of player of the server
     */
    int getPlayerCount();

     void setPlayerCount(int playerCount);


}
