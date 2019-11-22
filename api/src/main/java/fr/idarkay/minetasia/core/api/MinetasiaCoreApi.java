package fr.idarkay.minetasia.core.api;

import fr.idarkay.minetasia.core.api.utils.SQLManager;
import fr.idarkay.minetasia.normes.MinetasiaPlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

/**
 * File <b>MinetasiaCoreApi</b> located on fr.idarkay.minetasia.core.api
 * MinetasiaCoreApi is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
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
     * get the uuid of a player from user name
     * the don't need to be only but need to be connect
     * preference use async ( create sql request if the player wasn't in the server)
     * @param username of the player
     * @return UUID of the player or null if not found
     * @since 1.0
     */
    public abstract @Nullable UUID getPlayerUUID(@NotNull String username);

    /**
     * get the money of player
     * @param uuid uuid of the player
     * @param economy the economy type {@link Economy}
     * @return float amount or -1 if player not found
     * @since 1.0
     */
    public abstract float getPlayerMoney(UUID uuid, Economy economy);

    /**
     * add some of money to player
     * @param uuid uuid of the player
     * @param economy the economy type {@link Economy}
     * @param amount of money to add (not negative)
     * @return true if money has added
     * @since 1.0
     */
    public abstract boolean addPlayerMoney(UUID uuid, Economy economy, float amount);

    /**
     * remove some of money to player
     * @param uuid uuid of the player
     * @param economy the economy type {@link Economy}
     * @param amount of money to remove  (not negative)
     * @return true if money has removed
     * @since 1.0
     */
    public abstract boolean removePlayerMoney(UUID uuid, Economy economy, float amount);

    /**
     * set the account of player to specific value
     * @param uuid uuid of the player
     * @param economy the economy type {@link Economy}
     * @param amount of money to set (not negative)
     * @return true if money has updated
     * @since 1.0
     */
    public abstract boolean setPlayerMoney(UUID uuid, Economy economy, float amount);

    /**
     *  get all friend of a player
     * @param uuid of the player
     * @return map {@code UUID} and {@code String} (username) of the player if the player isn't find return empty HashMap
     * @since 1.0
     */
    @NotNull
    public abstract HashMap<UUID, String> getFriends(@NotNull UUID uuid);

    /**
     * check if 2 player is friend
     * @param uuid uuid of the first player
     * @param uuid2 uuid of the second player
     * @return true if it is else false
     * @since 1.0
     */
    public abstract boolean isFriend(@NotNull UUID uuid, @NotNull UUID uuid2);

    /**
     * remove a friend of to player
     * @param uuid uuid of the player to remove the friend
     * @param uuid2 uuid of the player will remove
     * @return true if the friend was removed else false (if the player is not friend with the uuid2) or uuid2 not exist
     * @since 1.0
     */
    public abstract boolean removeFriend(@NotNull UUID uuid, @NotNull UUID uuid2);

    /**
     * add a friend of to player
     * @param uuid uuid of the player to add the friend
     * @param uuid2 uuid of the player will add
     * @return true if friend was added else false if uuid or uuid2 not exist
     * @since 1.0
     */
    public abstract boolean addFriend(@NotNull UUID uuid, @NotNull UUID uuid2);

    /**
     * check if player is connect in the network
     * @param uuid of the player to check if is online
     * @return true if player online else false
     * @since 1.0
     */
    public abstract boolean isPlayerOnline(@NotNull UUID uuid);

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
     * get the lang of player in <a href="https://www.data.gouv.fr/fr/datasets/r/b4d4331f-d82c-45ce-92fe-615a1a6adc1b">ISO-3166-1 </a>
     * @param uuid of the player to get the lang
     * @return return the lang in <a href="https://www.data.gouv.fr/fr/datasets/r/b4d4331f-d82c-45ce-92fe-615a1a6adc1b">ISO-3166-1 </a>
     *         or null if player not found
     * @since 1.0
     */
    public abstract String getPlayerLang(@NotNull UUID uuid);

    /**
     * @param uuid uuid of the player to get the username
     * @return the username of the player or null if player never connect to the server
     * @since 1.0
     */
    @Nullable
    public abstract String getPlayerName(UUID uuid);

    /**
     * use this for shutdown your server :
     * move all player to lobby
     * shutdown the server
     * @since 1.0
     */
    public abstract void shutdown();

}
