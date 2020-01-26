package fr.idarkay.minetasia.core.api;

import fr.idarkay.minetasia.core.api.utils.*;
import fr.idarkay.minetasia.normes.MinetasiaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;

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
@SuppressWarnings("unused")
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
    public abstract void setPlayerData(@NotNull UUID uuid, @NotNull String key, String value);

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
     * async task
     * add some of money to player
     * @param uuid uuid of the player
     * @param economy the economy type {@link Economy}
     * @param amount of money to add (not negative)
     * @param async true for run in async false for sync
     * @since 1.0
     */
    public abstract void addPlayerMoney(UUID uuid, Economy economy, float amount, boolean async);

    /**
     * async task
     * remove some of money to player
     * @param uuid uuid of the player
     * @param economy the economy type {@link Economy}
     * @param amount of money to remove  (not negative)
     * @param async true for run in async false for sync
     * @since 1.0
     */
    public abstract void removePlayerMoney(UUID uuid, Economy economy, float amount, boolean async);

    /**
     * async task
     * set the account of player to specific value
     * @param uuid uuid of the player
     * @param economy the economy type {@link Economy}
     * @param amount of money to set (not negative)
     * @param async true for run in async false for sync
     * @since 1.0
     */
    public abstract void setPlayerMoney(UUID uuid, Economy economy, float amount, boolean async);

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
     * async task
     * remove a friend of to player
     * @param uuid uuid of the player to remove the friend
     * @param uuid2 uuid of the player will remove
     * @since 1.0
     */
    public abstract void removeFriend(@NotNull UUID uuid, @NotNull UUID uuid2);

    /**
     * async task
     * add a friend of to player
     * @param uuid uuid of the player to add the friend
     * @param uuid2 uuid of the player will add
     * @since 1.0
     */
    public abstract void addFriend(@NotNull UUID uuid, @NotNull UUID uuid2);

    /**
     * check if player is connect in the network
     * @param uuid of the player to check if is online
     * @return true if player online else false
     * @since 1.0
     */
    public abstract boolean isPlayerOnline(@NotNull UUID uuid);

    /**
     * check if player is connect in the network
     * @param name of the player to check if is online
     * @return true if player online else false
     * @since 1.0
     */
    public abstract boolean isPlayerOnline(@NotNull String name);

    /**
     * publish a message to the redis system
     * the message will be get by all only server with the {@link fr.idarkay.minetasia.core.api.event.FRSMessageEvent}
     *
     * @param chanel  {@link NotNull} chanel of the message
     * @param message  message
     * @param sync if need be sync or not
     * @see fr.idarkay.minetasia.core.api.event.FRSMessageEvent
     * @since 1.0
     */
    public abstract void publish(@NotNull String chanel, String message, boolean... sync);

    /**
     * get value of field
     * @param key - key
     * @param field - field
     * @return String data or null
     */
    public abstract String getValue(String key, String field);

    /**
     * get all field of a key
     * @param key - key
     * @return list of fields or empty list
     */
    public abstract Set<String> getFields(String key);

    /**
     * get all values for key and Set of field
     * @param key - key
     * @param fields - fields
     * @return map of fields - value or empty map
     */
    public abstract Map<String, String> getValues(String key, Set<String> fields);

    /**
     * set data value
     * @param key - key
     * @param field - field
     * @param value - data
     * @param sync to do in sync or async
     */
    public abstract void setValue(String key, String field, String value, boolean... sync);

    /**
     * move a player to random lobby
     * @param player {@link NotNull}  to return to the lobby
     * @see fr.idarkay.minetasia.core.api.event.FRSMessageEvent
     * @since 1.0
     */
    public abstract void movePlayerToHub(@NotNull Player player);

    /**
     *  move a player to server
     *  use {@link MinetasiaCoreApi#movePlayerToHub(Player)} to move ot hub !
     * @param player {@link NotNull}  to move
     * @param server where move player
     */
    public abstract void movePlayerToServer(@NotNull Player player, Server server);

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
     * get the statue of the player at a fix moment
     * the return object will be note update !
     * @param uuid of the player
     * @return {@link PlayerStatueFix} of player
     * @see PlayerStatueFix
     */
    public abstract PlayerStatueFix getPlayerStatue(UUID uuid);

    /**
     * get the statue of the player at a fix moment
     * the return object will be note update !
     * @param name of the player
     * @return {@link PlayerStatueFix} of player
     * @see PlayerStatueFix
     */
    public abstract PlayerStatueFix getPlayerStatue(String name);

    /**
     * if player is lvl 0 in a kit , the kti will be not in the list
     * @param uuid of the player
     * @return get all kits lvl of the user
     */
    public abstract Map<String, Integer> getPlayerKitsLvl(UUID uuid);

    /**
     * if player is lvl 0 in a kit , the kti will be not in the list
     * @param uuid of the user
     * @param gameFilter the kit name need be start by this
     * @return all Kits with gameFilter
     */
    public abstract Map<String, Integer> getPlayerKitsLvl(UUID uuid, String gameFilter);

    /**
     * get the lvl of a kit of a user
     * @param uuid of teh player
     * @param kitName full name of the kit
     * @return the lvl of the kit or 0 if not found
     */
    public abstract int getPlayerKitLvl(UUID uuid, String kitName);

    /**
     * get a kit with a lang in <a href="https://www.data.gouv.fr/fr/datasets/r/b4d4331f-d82c-45ce-92fe-615a1a6adc1b">ISO-3166-1 </a> <br>
     * if not found will be retail with default lang
     * @param name name of the kit
     * @param lang lang of the kit in <a href="https://www.data.gouv.fr/fr/datasets/r/b4d4331f-d82c-45ce-92fe-615a1a6adc1b">ISO-3166-1 </a>
     * @return Kit or null
     */
    public abstract Kit getKitKit(String name, String lang);

    /**
     * save a kit in frs if not exist  <br>
     *  in {@link Kit} please not set color char just {@code '&'}
     * @param kit to save
     */
    public abstract void saveDefaultKit(Kit kit);

    /**
     * get Stats of a user
     * @param uuid player UUID
     * @return {@link PlayerStats} stats , return null if player not found
     */
    @Nullable
    public abstract PlayerStats getPlayerStats(@NotNull UUID uuid);

    /**
     * add stats to player
     * @param uuid uuid of the player
     * @param statsUpdater see {@link StatsUpdater}
     * @param async true for run in async false for sync
     */
    public abstract void addStatsToPlayer(@NotNull UUID uuid, @NotNull StatsUpdater statsUpdater, boolean async);

    @NotNull
    public abstract Boost getPlayerPersonalBoost(@NotNull UUID uuid);

    @NotNull
    public abstract Boost getPlayerPartyBoost(@NotNull UUID uuid);

    /**
     * use this for shutdown your server :
     * move all player to lobby
     * shutdown the server
     * @since 1.0
     */
    public abstract void shutdown();

    /**
     * add money to player
     * @param uuid uuid of the player
     * @param moneyUpdater see {@link MoneyUpdater}
     * @param boost false for ignore boost
     * @param async true for run in async false for sync
     */
    public abstract void addGameWonMoneyToPlayer(@NotNull UUID uuid, @NotNull MoneyUpdater moneyUpdater, boolean boost, boolean async);

    /**
     * get all uuid and name of all player log in the server
     * @return {@code HashMap<UUID, String>}
     */
    @NotNull
    public abstract Map<UUID, String> getOnlinePlayers();

    /**
     * get all only player use this for tab and not {@link MinetasiaCoreApi#getOnlinePlayers()}
     * @return {@code HashMap<UUID, String>}
     */
    @NotNull
    public abstract List<String> getOnlinePlayersForTab();

    /**
     * get the type of the server (hub or name of the mini game)
     * @return the type of the server
     */
    @NotNull
    public abstract String getServerType();

    /**
     * get the current {@link Server}
     * @return the current {@link Server}
     * @since 1.0
     */
    @NotNull
    public abstract Server getThisServer();

    /**
     * get a  {@link Server} from name or null if server not exist
     * @param name name of the server wanted
     * @return the wanted {@link Server}
     * @since 1.0
     */
    @Nullable
    public abstract Server getServer(String name);

    /**
     * get all {@link Server}
     * @return {@code Map<String, Server> }
     * @since 1.0
     */
    @NotNull
    public abstract Map<String, Server> getServers();

    /**
     * get all {@link Server} of a type
     * @param type  the type of the {@link Server}
     * @return {@code Map<String, Server> }
     * @since 1.0
     */
    @NotNull
    public abstract Map<String, Server> getServers(String type);

    /**
     * check if command is enable
     * @param b byte of the command use {@link Command}
     * @return true if enable else false
     */
    public abstract boolean isCommandEnable(byte b);

    /**
     * get if commands is enable
     * @param c {@link Command} to check
     * @return if enable
     * @since 1.0
     */
    public abstract boolean isCommandEnable(Command c);

    /**
     * get the display group  of a user
     * if player have more than one group will be take group
     * with the biggest priority
     * return {@code ""} if player d'ont have group
     * @param player uuid of the player
     * @return String display group
     */
    @NotNull
    public abstract String getGroupDisplay(UUID player);

    public abstract void setServerPhase(@NotNull ServerPhase phase);

    public abstract void setMaxPlayerCount(int maxPlayer);

    public abstract int getMaxPlayerCount();

    public abstract boolean isHub();

//    public abstract int getKitLevelOfUser(UUID player, String kitName);
//
//    public abstract void setKitLvlOfUser(UUID player, String kitName, int lvl);

}
