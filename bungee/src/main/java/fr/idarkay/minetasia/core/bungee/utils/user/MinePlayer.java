package fr.idarkay.minetasia.core.bungee.utils.user;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.idarkay.minetasia.core.bungee.MinetasiaCoreBungee;
import fr.idarkay.minetasia.core.bungee.utils.FRSClient;
import fr.idarkay.minetasia.core.bungee.utils.FRSKey;
import fr.idarkay.minetasia.core.bungee.utils.JSONUtils;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * File <b>MinePlayer</b> located on fr.idarkay.minetasia.core.spigot.user
 * MinePlayer is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 05/02/2020 at 16:18
 */
public class MinePlayer
{

    private static final FRSClient CORE = MinetasiaCoreBungee.getInstance().getFrsClient();
    private static final JsonParser PARSER = new JsonParser();


    @NotNull private final UUID uuid;

    @NotNull private final JsonObject moneys;

    @NotNull private Map<String, String> generalData;
    @NotNull private String username;


    public MinePlayer(@NotNull UUID uuid)
    {
        this.uuid = Objects.requireNonNull(uuid);

        final String data = Objects.requireNonNull(CORE.getValue(FRSKey.DATA.getKey(uuid)));
        if(data.equalsIgnoreCase("null")) throw new NullPointerException();
        final JsonObject dataJson = PARSER.parse(data).getAsJsonObject();

        this.generalData = JSONUtils.jsonObjectToStringMap(dataJson);

        this.username = Objects.requireNonNull(generalData.get("username"));
        if(!generalData.containsKey("money"))
            this.moneys = new JsonObject();
        else
            this.moneys = dataJson.getAsJsonObject("money");
    }

    public MinePlayer(@NotNull UUID uuid, @NotNull String name)
    {
        this.uuid = Objects.requireNonNull(uuid);

        this.generalData = new HashMap<>();

        this.username = name;
        generalData.put("username", username);
        this.moneys = new JsonObject();
    }

    public void setUsername(@NotNull String username)
    {
        this.username = username;
        generalData.put("username", username);
        saveGeneralData();
    }

    public @NotNull UUID getUUID()
    {
        return uuid;
    }

    
    public @NotNull String getName()
    {
        return username;
    }

    public @NotNull String getLang()
    {
        return generalData.getOrDefault("lang", MinetasiaLang.BASE_LANG);
    }

    public @Nullable String getGeneralData(@NotNull String key)
    {
        return generalData.get(key);
    }

    
    public synchronized void putGeneralData(@NotNull String key, @Nullable String value)
    {
        if(value == null)
        {
            if(generalData.remove(key) == null) return;
        }
        else generalData.put(key, value);
        saveGeneralData();
    }


    public synchronized void saveGeneralData()
    {
        generalData.putIfAbsent("lang", MinetasiaLang.BASE_LANG);
        final JsonObject object = JSONUtils.mapToJsonObject(generalData);
        object.add("money", moneys);
        System.out.println(object.toString());
        CORE.setValue(FRSKey.DATA.getKey(uuid), object.toString(), true);
    }

}
