package fr.idarkay.minetasia.common.message;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>MinetasiaPacketInSerializer</b> located on fr.idarkay.minetasia.common.message
 * MinetasiaPacketInSerializer is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 21/04/2020 at 21:38
 */
public abstract class MinetasiaPacketInSerializer<T extends MinetasiaPacketIn> extends MinetasiaPacketSerializer<T>
{
    public MinetasiaPacketInSerializer(@NotNull String name)
    {
        super(name);
    }

    public JsonObject write(MinetasiaPacket packet)
    {
        T fPacket = (T) packet;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", fPacket.getCode());
        jsonObject.addProperty("message", fPacket.getRepMessage());
        return jsonObject;
    }
}
