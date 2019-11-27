package fr.idarkay.minetasia.core.bungee.utils.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * File <b>Player</b> located on fr.idarkay.minetasia.core.bungee.utils.user
 * Player is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 26/11/2019 at 18:53
 */
public class Player {

    private HashMap<String, String> data = new HashMap<>();
    private HashMap<UUID, String> friends = new HashMap<>();
    private HashMap<String, Float> moneys = new HashMap<>();
    private String username;
    private UUID uuid;

    public Player(String jsonData)
    {
        update(jsonData);
    }

    public Player(UUID uuid, String username)
    {
        this.uuid = uuid;
        this.username = username;
    }

    public void update(String jsonData)
    {
        JsonObject data = new JsonParser().parse(jsonData).getAsJsonObject();

        username = data.get("username").getAsString();
        uuid = UUID.fromString(data.get("uuid").getAsString());

        for (Map.Entry<String, JsonElement> a : data.get("data").getAsJsonObject().entrySet()) this.data.put(a.getKey(), a.getValue().getAsString());
        for (Map.Entry<String, JsonElement> a : data.get("moneys").getAsJsonObject().entrySet()) this.moneys.put(a.getKey(), a.getValue().getAsFloat());
        for (Map.Entry<String, JsonElement> a : data.get("friends").getAsJsonObject().entrySet()) this.friends.put(UUID.fromString(a.getKey()), a.getValue().getAsString());

    }

    public @Nullable String getData(@NotNull String key) {
        return data.get(key);
    }

    public void setDaa(String key, String value)
    {
        data.put(key, value);
    }

    public HashMap<UUID, String> getFriends() {
        return friends;
    }

    public String getName() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJson()
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uuid", uuid.toString());
        jsonObject.addProperty("username", username);

        JsonObject mo = new JsonObject();
        for (Map.Entry<String, Float> a : moneys.entrySet()) mo.addProperty(a.getKey(), a.getValue());
        jsonObject.add("moneys", mo);

        JsonObject data = new JsonObject();
        for (Map.Entry<String, String> a : this.data.entrySet()) data.addProperty(a.getKey(), a.getValue());
        jsonObject.add("data", data);

        JsonObject f = new JsonObject();
        for (Map.Entry<UUID, String> a : this.friends.entrySet()) f.addProperty(a.getKey().toString(), a.getValue());
        jsonObject.add("friends", f);

        return jsonObject.toString();


    }

}
