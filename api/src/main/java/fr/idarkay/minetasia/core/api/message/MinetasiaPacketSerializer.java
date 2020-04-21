package fr.idarkay.minetasia.core.api.message;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>MinetasiaPacketSerializer</b> located on fr.idarkay.minetasia.core.api.message
 * MinetasiaPacketSerializer is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 21/04/2020 at 21:38
 */
public abstract class MinetasiaPacketSerializer<T extends MinetasiaPacket>
{

    private final String name;

    public MinetasiaPacketSerializer(@NotNull String name)
    {

        this.name = name;
    }

    public abstract JsonObject write(MinetasiaPacket packet);

    public abstract T read(@NotNull JsonObject jsonObject);

    public String getName()
    {
        return name;
    }
}
