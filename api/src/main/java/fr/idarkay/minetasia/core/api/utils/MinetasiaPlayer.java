package fr.idarkay.minetasia.core.api.utils;

import fr.idarkay.minetasia.core.api.Economy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

/**
 * File <b>MinetasiaPlayer</b> located on fr.idarkay.minetasia.core.api.utils
 * MinetasiaPlayer is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 05/02/2020 at 15:55
 */
public interface MinetasiaPlayer
{
    /**
     * @return true if the player isn't present on this server else false
     */
    boolean isCache();

    /**
     * get amount of specific money have the player
     * @param economy type of money
     * @return amount of money
     */
    float getMoney(@NotNull Economy economy);

    /**
     * add amount of specific money to player
     * @param economy type of money
     * @param amount amount of money need positive
     * @throws IllegalArgumentException if amount is not positive
     */
    void addMoney(@NotNull Economy economy, float amount);

    /**
     * remove amount of specific money to player
     * @param economy type of money
     * @param amount amount of money need positive
     * @throws IllegalArgumentException if amount is not positive
     */
    void removeMoney(@NotNull Economy economy, float amount);

    /**
     * set amount of specific money to player
     * @param economy type of money
     * @param amount amount of money need positive
     * @throws IllegalArgumentException if amount is not positive
     */
    void setMoney(@NotNull Economy economy, float amount);

    /**
     * @return UUID of the user
     */
    @NotNull
    UUID getUUID();

    /**
     * @return name of the user
     */
    @NotNull
    String getName();

    /**
     * get all friends of the user
     * @return map uuid - username
     */
    @NotNull
    Map<UUID, String> getFriends();

    /**
     * return the lang of the player in <a href="https://www.data.gouv.fr/fr/datasets/r/b4d4331f-d82c-45ce-92fe-615a1a6adc1b">ISO-3166-1 </a>
     * @return lang in  <a href="https://www.data.gouv.fr/fr/datasets/r/b4d4331f-d82c-45ce-92fe-615a1a6adc1b">ISO-3166-1 </a>
     */
    @NotNull
    String getLang();

    /**
     * check if this player is friends with that other
     * @param uuid other player to check
     * @return true if it's else false
     */
    boolean isFriend(@NotNull  UUID uuid);

    /**
     * /!\ ad just friends for this user other user still haven't this user at friends
     * @param uuid of the friends to add
     */
    void addFriends(@NotNull UUID uuid);

    /**
     * /!\ remove just friends for this user other user still have this user at friends
     * @param uuid of the friends to remove
     */
    void removeFriends(@NotNull UUID uuid);

    /**
     * general data is common data load anywhere : uuid username lang etc...
     * get a data of teh user
     * @param key of the data
     * @return value or null if not found
     */
    @Nullable
    String getGeneralData(@NotNull String key);

    /**
     * general data is common data load anywhere : uuid username lang etc...
     * put a data of the player
     * @param key of the data
     * @param value value of the data set {@code null} for remove
     */
    void putGeneralData(@NotNull String key, @Nullable String value);

    /**
     * get a data of teh user
     * @param key of the data
     * @return value or null if not found
     */
    @Nullable
    String getData(@NotNull String key);

    /**
     * put a data of the player
     * @param key of the data
     * @param value value of the data set {@code null} for remove
     */
    void putData(@NotNull String key, @Nullable String value);

    /**=
     * @return the {@link PlayerStats} of the player
     */
    @NotNull
    PlayerStats getStats();

    /**
     * update the stats of the player
     * @param updater update
     * @see StatsUpdater
     */
    void updatePlayerStats(@NotNull StatsUpdater updater);

    /**
     * @return a core value
     */
    int getStatus();

    /**
     * @return the party of the player or null if player isn"t in party
     */
    Party getParty();

}
