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
    boolean isCache();

    float getMoney(@NotNull Economy economy);

    void addMoney(@NotNull Economy economy, float amount);

    void removeMoney(@NotNull Economy economy, float amount);

    void setMoney(@NotNull Economy economy, float amount);

    @NotNull
    UUID getUUID();

    @NotNull
    String getName();

    @NotNull
    Map<UUID, String> getFriends();

    @NotNull
    String getLang();

    boolean isFriend(@NotNull  UUID uuid);

    void addFriends(@NotNull  UUID uuid);

    void removeFriends(@NotNull UUID uuid);

    @Nullable
    String getGeneralData(@NotNull String key);

    void putGeneralData(@NotNull String key, @Nullable String value);

    @Nullable
    String getData(@NotNull String key);

    void putData(@NotNull String key, @Nullable String value);

    @NotNull
    PlayerStats getStats();

    void updatePlayerStats(@NotNull StatsUpdater updater);

    int getStatus();

}
