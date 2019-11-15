package fr.idarkay.minetasia.core.common.user;

import fr.idarkay.minetasia.core.api.Economy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * File <b>IPlayer</b> located on fr.idarkay.minetasia.core.api.utils
 * IPlayer is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 13/11/2019 at 15:52
 */
public interface IPlayer
{


    float getMoney(@NotNull Economy economy);

    void addMoney(@NotNull Economy economy, float amount, boolean ignoreWarn);

    void setMoney(@NotNull Economy economy, float amount);

    @Nullable String getData(@NotNull String key);

    void setData(@NotNull String key, @NotNull String value);

    HashMap<UUID, String> getFriends();

    boolean isFriend(UUID uuid);

    void addFriends(@NotNull UUID uuid);

    boolean removeFriends(@NotNull UUID uuid);

    String getName();



}
