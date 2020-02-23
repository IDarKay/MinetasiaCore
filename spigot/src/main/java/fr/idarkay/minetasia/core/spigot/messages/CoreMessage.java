package fr.idarkay.minetasia.core.spigot.messages;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * File <b>CoreMessage</b> located on fr.idarkay.minetasia.core.spigot.messages
 * CoreMessage is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 01/02/2020 at 13:41
 */
public abstract class CoreMessage
{

   public static final Map<String, CoreMessage> MESSAGE = new HashMap<>();
   public static final String CHANNEL = "core-messaging";



   CoreMessage(String identifier)
   {
       MESSAGE.put(identifier, this);
   }

    public abstract void actionOnGet(MinetasiaCore plugin, String... args);

    static @NotNull String getMessage(String identifier, Object... args)
    {
        final StringBuilder builder = new StringBuilder(identifier + ";");
        for(Object a : args)
        {
            builder.append(a).append(";");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    @Nullable
    static String concat(@NotNull String[] array, @Nullable String split, int indexFrom)
    {
        StringBuilder result = new StringBuilder();
        for(int i = indexFrom; i < array.length; i++) result.append(i != indexFrom && split != null ? split : "").append(array[i]);
        String r = result.toString();
        return r.equals("null") ? null : r;
    }

}
