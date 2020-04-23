package fr.idarkay.minetasia.common.message;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>MinetasiaPacketSerializer</b> located on fr.idarkay.minetasia.common.message
 * MinetasiaPacketSerializer is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 22/04/2020 at 14:25
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