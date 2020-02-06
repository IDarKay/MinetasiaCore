package fr.idarkay.minetasia.core.spigot.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * File <b>JSONUtils</b> located on fr.idarkay.minetasia.core.spigot.utils
 * JSONUtils is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 05/02/2020 at 16:55
 */
public class JSONUtils
{
    @NotNull
    public static String getOrDefaultString(@NotNull JsonObject object, @NotNull String key, @NotNull String def)
    {
        Validate.notNull(object);
        Validate.notNull(key);
        Validate.notNull(def);
        final JsonElement langElement = object.get(key);
        return langElement == null ? def : langElement.getAsString();
    }

    public static int getOrDefaultInt(@NotNull JsonObject object, @NotNull String key, int def)
    {
        Validate.notNull(object);
        Validate.notNull(key);
        final JsonElement langElement = object.get(key);
        return langElement == null ? def : langElement.getAsInt();
    }

    @NotNull
    public static Map<String, String> jsonObjectToStringMap(@NotNull JsonObject object)
    {
        Validate.notNull(object);
        final Map<String, String> map = new HashMap<>();
        object.entrySet().forEach(k ->  {
            if(!k.getValue().isJsonObject())
                map.put(k.getKey(), k.getValue().getAsString());
        });
        return map;
    }

    public static  Map<String, String> jsonObjectToStringMap(JsonObject object, TreeMap<String, String> defaultMap)
    {
        Validate.notNull(object);
        object.entrySet().forEach(k -> defaultMap.put(k.getKey(), k.getValue().getAsString()));
        return defaultMap;
    }

    @NotNull
    public static <K, V> Map<K, V> jsonObjectToMap(@NotNull JsonObject object, Function<String, K> key, Function<JsonElement, V> value, Map<K, V> defaultMap)
    {
        Validate.notNull(object);
        object.entrySet().forEach(k -> defaultMap.put(key.apply(k.getKey()), value.apply(k.getValue())));
        return defaultMap;
    }

    @NotNull
    public static <K, V> Map<K, V> jsonObjectToMap(@NotNull JsonObject object, Function<String, K> key, Function<JsonElement, V> value)
    {
        Validate.notNull(object);
        final Map<K, V> map = new HashMap<>();
        object.entrySet().forEach(k -> map.put(key.apply(k.getKey()), value.apply(k.getValue())));
        return map;
    }

    public static JsonObject mapToJsonObject(@NotNull Map<?,?> map)
    {
        final JsonObject jsonObject = new JsonObject();
        map.forEach((k, v) -> jsonObject.addProperty(k.toString(), v.toString()));
        return jsonObject;
    }

    public static JsonObject floatMapToJsonObject(@NotNull Map<?,Float> map)
    {
        final JsonObject jsonObject = new JsonObject();
        map.forEach((k, v) -> jsonObject.addProperty(k.toString(), v));
        return jsonObject;
    }

}
