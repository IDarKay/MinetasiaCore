package fr.idarkay.minetasia.common.message;

import com.google.gson.JsonObject;

/**
 * File <b>MinetasiaPacket</b> located on fr.idarkay.minetasia.common.message
 * MinetasiaPacket is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 22/04/2020 at 14:24
 */
public interface MinetasiaPacket
{
    JsonObject write();

    String name();
}
