package fr.idarkay.minetasia.common.message;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>MinetasiaPacktOutSerializer</b> located on fr.idarkay.minetasia.common.message
 * MinetasiaPacktOutSerializer is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 21/04/2020 at 21:40
 */
public abstract class MinetasiaPacketOutSerializer<T extends MinetasiaPacketOut> extends MinetasiaPacketSerializer<T>
{

    public MinetasiaPacketOutSerializer(@NotNull String name)
    {
        super(name);
    }

    @Override
    public JsonObject write(MinetasiaPacket packet)
    {
        T fPacket = (T) packet;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("needRep", fPacket.isNeedRep());

        return jsonObject;
    }
}
