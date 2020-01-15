package fr.idarkay.minetasia.core.spigot.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.idarkay.minetasia.core.api.Economy;
import fr.idarkay.minetasia.core.api.utils.PlayerStats;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.api.exception.NoEnoughMoneyException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
public final class Player {

    private static final JsonParser PARSER = new JsonParser();

    private Stats stats;
    private HashMap<String, String> data = new HashMap<>();
    private HashMap<UUID, String> friends = new HashMap<>();
    private HashMap<Economy, Float> moneys = new HashMap<>();
    private String username;
    private UUID uuid;

    public Player(@NotNull String jsonData, @Nullable String jsonDataKit)
    {
        update(jsonData);
        stats = jsonDataKit == null ? new Stats() : new Stats(PARSER.parse(jsonDataKit).getAsJsonObject());
    }

    public Player(UUID uuid, String username)
    {
        this.uuid = uuid;
        this.username = username;
        this.stats = new Stats();
    }

    private void update(String jsonData)
    {
        JsonObject data = PARSER.parse(jsonData).getAsJsonObject();

        username = data.get("username").getAsString();
        uuid = UUID.fromString(data.get("uuid").getAsString());

        for (Map.Entry<String, JsonElement> a : data.get("data").getAsJsonObject().entrySet()) this.data.put(a.getKey(), a.getValue().getAsString());
        for (Map.Entry<String, JsonElement> a : data.get("moneys").getAsJsonObject().entrySet()) this.moneys.put(Economy.valueOf(a.getKey()), a.getValue().getAsFloat());
        for (Map.Entry<String, JsonElement> a : data.get("friends").getAsJsonObject().entrySet()) this.friends.put(UUID.fromString(a.getKey()), a.getValue().getAsString());

    }


    public float getMoney(@NotNull Economy economy) {
        Float m;
        if((m = moneys.get(economy)) != null) return m;
        else
        {
            setMoney(economy, 0);
            return 0.0F;
        }
    }


    public void addMoney(@NotNull Economy economy, float amount) {
        Float m;
        if((m = moneys.get(economy)) != null) moneys.put(economy, m + amount);
        else moneys.put(economy, amount);
    }

    public void removeMooney(@NotNull Economy economy, float amount)
    {
        Float m;
        if((m = moneys.get(economy)) != null)
        {
            if(m >= amount)
            {
                moneys.put(economy, m - amount);
            } else throw new NoEnoughMoneyException("cant remove "+ amount + " " + economy.displayName + " to " + username);

        }
        else
        {
            moneys.put(economy, 0F);
            if (amount != 0) throw new NoEnoughMoneyException("cant remove "+ amount + " " + economy.displayName + " to " + username);
        }

    }

    public void setMoney(@NotNull Economy economy, float amount) {
        moneys.put(economy, amount);
    }


    public @Nullable String getData(@NotNull String key) {
        return data.get(key);
    }


    public void setData(@NotNull String key, String value) {
        if(value == null)  data.remove(key);
        else data.put(key, value);
    }


    public HashMap<UUID, String> getFriends() {
        return friends;
    }


    public boolean isFriend(@NotNull UUID uuid) {
        return friends.containsKey(uuid);
    }


    public boolean addFriends(@NotNull UUID uuid) {
        String p = MinetasiaCore.getInstance().getPlayerName(uuid);
        if(p != null)
        {
            friends.put(uuid, p);
            return true;
        }
        return false;
    }


    public boolean removeFriends(@NotNull UUID uuid) {
        return friends.remove(uuid) != null;
    }


    public String getName() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getJson()
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uuid", uuid.toString());
        jsonObject.addProperty("username", username);

        JsonObject mo = new JsonObject();
        for (Map.Entry<Economy, Float> a : moneys.entrySet()) mo.addProperty(a.getKey().name(), a.getValue());
        jsonObject.add("moneys", mo);

        JsonObject data = new JsonObject();
        for (Map.Entry<String, String> a : this.data.entrySet()) data.addProperty(a.getKey(), a.getValue());
        jsonObject.add("data", data);

        JsonObject f = new JsonObject();
        for (Map.Entry<UUID, String> a : this.friends.entrySet()) f.addProperty(a.getKey().toString(), a.getValue());
        jsonObject.add("friends", f);

        return jsonObject.toString();
    }

    public PlayerStats getStats()
    {
        return stats;
    }

    @NotNull
    public JsonObject getJsonStats()
    {
        return stats.toJsonObject();
    }

    public class Stats implements PlayerStats {

        private Map<String, Long> stats = new HashMap<>();

        private Stats()
        {

        }

        private Stats(JsonObject data)
        {
            data.entrySet().forEach(e -> stats.put(e.getKey(), e.getValue().getAsLong()));
        }

        @Override
        public @NotNull Map<@NotNull String, @NotNull Long> getAllStats()
        {
            return new HashMap<>(stats);
        }

        @Override
        public @NotNull Map<@NotNull String, @NotNull Long> getPluginStats(String pluginName)
        {
            return stats.entrySet().stream().filter(sle -> sle.getKey().startsWith(pluginName)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        @Override
        public long getStatsValue(String statsName)
        {
            return stats.getOrDefault(statsName, -1L);
        }

        public JsonObject toJsonObject()
        {
            JsonObject obj = new JsonObject();
            stats.forEach(obj::addProperty);
            return obj;
        }

    }

}
