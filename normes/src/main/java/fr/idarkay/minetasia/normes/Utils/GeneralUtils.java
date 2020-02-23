package fr.idarkay.minetasia.normes.Utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * File <b>GeneralUtils</b> located on fr.idarkay.minetasia.hub.Utils
 * GeneralUtils is a part of minetasiahub.
 * <p>
 * Copyright (c) 2020 minetasiahub.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 30/01/2020 at 21:44
 */
public class GeneralUtils
{

    public static  @NotNull String concat(String[] array, String split, int indexFrom)
    {
        StringBuilder result = new StringBuilder();
        for(int i = indexFrom; i < array.length; i++) result.append(i != indexFrom && split != null ? split : "").append(array[i]);
        return result.toString();
    }

    public static long parseLongOrDefault(@Nullable String rawNumber, long defaultN)
    {
        return rawNumber == null ? defaultN : Long.parseLong(rawNumber);
    }

    public static <T> T[] splitArray(T[] array, int page, int startIndex, int numberOfElements)
    {
        return Arrays.copyOfRange(array, page * numberOfElements + startIndex, (page + 1) * numberOfElements + startIndex);
    }

    @NotNull
    public static JsonArray listToJsonArray(@NotNull List<?> list)
    {
        final JsonArray arrays = new JsonArray();
        list.forEach(s -> {
            if(s instanceof Number)
                arrays.add((Number) s);
            else if (s instanceof Boolean)
                arrays.add((Boolean) s);
            else if (s instanceof Character)
                arrays.add((Character) s);
            else if (s instanceof JsonElement)
                arrays.add((JsonElement) s);
            else
                arrays.add(s.toString());
        });
        return arrays;
    }

}
