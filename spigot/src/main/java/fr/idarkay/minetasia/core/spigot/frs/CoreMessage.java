package fr.idarkay.minetasia.core.spigot.frs;

import com.google.common.collect.ImmutableMap;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * File <b>FRSMessage</b> located on fr.idarkay.minetasia.core.spigot.utils
 * CoreFRSMessage is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 01/02/2020 at 13:41
 */
public interface CoreMessage
{

   Map<String, CoreMessage> MESSAGE = new ImmutableMap.Builder<String, CoreMessage>()
            .put(ServerMessage.getIdentifier(), new ServerMessage())
            .put(PlayerMessage.getIdentifier(), new PlayerMessage())
            .build();

    String CHANNEL = "core-frs-msg";

    void actionOnGet(MinetasiaCore plugin, String... args);

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
