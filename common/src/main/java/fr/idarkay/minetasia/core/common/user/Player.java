package fr.idarkay.minetasia.core.common.user;

import fr.idarkay.minetasia.core.api.Economy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

/**
 * File <b>Player</b> located on fr.idarkay.minetasia.core.common.user
 * Player is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 15/11/2019 at 21:09
 */
public class Player implements IPlayer {

    private HashMap<String, String> data;
    private HashMap<UUID, String> friends;
    private HashMap<Economy, Float> moneys;
    private String username;
    private UUID uuid;

    public Player(String jsonData)
    {
        data = new HashMap<>();
        friends = new HashMap<>();
        moneys = new HashMap<>();

    }

    @Override
    public float getMoney(@NotNull Economy economy) {
        Float m;
        if((m = moneys.get(economy)) != null) return m;
        else
        {
            setMoney(economy, 0);
            return 0.0F;
        }
    }

    @Override
    public void addMoney(@NotNull Economy economy, float amount, boolean ignoreWarn) {

        if(!ignoreWarn && amount >= economy.warnNumber)
        {
            //todo: elastickserach webhocks
        }
        Float m;
        if((m = moneys.get(economy)) != null) moneys.put(economy, m + amount);
        else moneys.put(economy, amount);
    }

    @Override
    public void setMoney(@NotNull Economy economy, float amount) {
        moneys.put(economy, amount);
    }

    @Override
    public @Nullable String getData(@NotNull String key) {
        return data.get(key);
    }

    @Override
    public void setData(@NotNull String key, @NotNull String value) {
        data.put(key, value);
    }

    @Override
    public HashMap<UUID, String> getFriends() {
        return friends;
    }

    @Override
    public boolean isFriend(@NotNull UUID uuid) {
        return friends.containsKey(uuid);
    }

    @Override
    public void addFriends(@NotNull UUID uuid) {

    }

    @Override
    public boolean removeFriends(@NotNull UUID uuid) {
        return false;
    }

    @Override
    public String getName() {
        return username;
    }
}
