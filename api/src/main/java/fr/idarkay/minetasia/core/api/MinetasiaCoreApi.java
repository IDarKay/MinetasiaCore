package fr.idarkay.minetasia.core.api;

import fr.idarkay.minetasia.core.api.utils.SQLManager;
import fr.idarkay.minetasia.normes.MinetasiaPlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * File <b>MinetasiaCoreApi</b> located on fr.idarkay.minetasia.core.api
 * MinetasiaCoreApi is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 13/11/2019 at 15:05
 * @since 1.0
 *
 * Main class of API please no extends this class
 * you will find all methods for your plugin
 * if you want new methods tell IDarKay !
 */
public abstract class MinetasiaCoreApi extends MinetasiaPlugin {

    private static MinetasiaCoreApi instance;

    protected MinetasiaCoreApi()
    {
        if(instance == null) instance = this;
    }

    /**
     * get the complete instance of the abstract class
     * use this for use methods !
     * don't extends your class with {@link MinetasiaCoreApi}
     * but don't forget to use {@link MinetasiaPlugin} for you main class
     * @return {@link MinetasiaCoreApi} instance
     * @since 1.0
     */
    public static MinetasiaCoreApi getInstance() {
        return instance;
    }

    /**
     * test function connexion between core and api
     * @deprecated
     * @return "pong" if connexion work
     * @since 1.0
     */
    public abstract String ping();

    /**
     * return complete {@link SQLManager} instance  to be use for get The connexion to SQL
     * don't create self connexion.
     * @see SQLManager
     * @return complete {@link SQLManager} instance
     * @since 1.0
     */
    public abstract SQLManager getSqlManager();

    /**
     * set a data to a specific player if key already exist this will be replaced by the new values
     * @param uuid {@link NotNull} uuid of the player
     * @param key  {@link NotNull} String key of the data
     * @param value {@link NotNull} String value of data
     * @throws IllegalStateException if one or more parameter are null
     * @since 1.0
     */
    public abstract void setPlayerData(@NotNull UUID uuid, @NotNull String key, @NotNull String value);

    /**
     * get a data of a specific player and key
     * @param uuid {@link NotNull} uuid of the player
     * @param key  {@link NotNull} String key of the data
     * @return String value or {@code null} if player not found or data not found
     * @throws IllegalStateException if one or more parameter are null
     * @since 1.0
     */
    public abstract String getPlayerData(@NotNull UUID uuid, @NotNull String key);

    /**
     * publish a message to the redis system
     * the message will be get by all only server with the {@link fr.idarkay.minetasia.core.api.event.FRSMessageEvent}
     *
     * @param chanel  {@link NotNull} chanel of the message
     * @param message  {@link NotNull} message
     * @see fr.idarkay.minetasia.core.api.event.FRSMessageEvent
     * @since 1.0
     */
    public abstract void publish(@NotNull String chanel, @NotNull String message);

    /**
     * move a player to random lobby
     * @param player {@link NotNull}  to return to the lobby
     * @see fr.idarkay.minetasia.core.api.event.FRSMessageEvent
     * @since 1.0
     */
    public abstract void movePlayerToHub(@NotNull Player player);

    /**
     * use this for shutdown your server :
     * move all player to lobby
     * shutdown the server
     * @since 1.0
     */
    public abstract void shutdown();

}
