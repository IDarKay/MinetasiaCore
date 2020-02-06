package fr.idarkay.minetasia.core.bungee.utils;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * File <b>JSONUtils</b> located on fr.idarkay.minetasia.core.spigot.utils
 * JSONUtils is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 05/02/2020 at 16:55
 */
public class JSONUtils
{

    @NotNull
    public static Map<String, String> jsonObjectToStringMap(@NotNull JsonObject object)
    {
        final Map<String, String> map = new HashMap<>();
        object.entrySet().forEach(k ->  {
            if(!k.getValue().isJsonObject())
                map.put(k.getKey(), k.getValue().getAsString());
        });
        return map;
    }

    public static JsonObject mapToJsonObject(@NotNull Map<?,?> map)
    {
        final JsonObject jsonObject = new JsonObject();
        map.forEach((k, v) -> jsonObject.addProperty(k.toString(), v.toString()));
        return jsonObject;
    }

}
