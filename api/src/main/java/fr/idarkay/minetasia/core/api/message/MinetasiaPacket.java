package fr.idarkay.minetasia.core.api.message;

import com.google.gson.JsonObject;

/**
 * File <b>MinetasiaPacket</b> located on fr.idarkay.minetasia.core.api.message
 * MinetasiaPacket is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 21/04/2020 at 21:36
 */
public interface MinetasiaPacket
{
    JsonObject write();

    String name();
}
