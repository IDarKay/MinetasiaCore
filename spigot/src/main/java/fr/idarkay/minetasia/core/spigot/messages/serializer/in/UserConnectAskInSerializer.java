package fr.idarkay.minetasia.core.spigot.messages.serializer.in;

import com.google.gson.JsonObject;
import fr.idarkay.minetasia.core.api.message.MinetasiaPacket;
import fr.idarkay.minetasia.core.api.message.MinetasiaPacketInSerializer;
import fr.idarkay.minetasia.core.spigot.messages.packet.in.UserConnectAskInPacket;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>UserConnectAskInSerializer</b> located on fr.idarkay.minetasia.core.spigot.messages.serializer.in
 * UserConnectAskInSerializer is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 21/04/2020 at 23:31
 */
public class UserConnectAskInSerializer extends MinetasiaPacketInSerializer<UserConnectAskInPacket>
{

    public UserConnectAskInSerializer()
    {
        super("userConnectAsk");
    }

    @Override
    public JsonObject write(MinetasiaPacket packet)
    {
        JsonObject jsonObject = super.write(packet);
        UserConnectAskInPacket fPacket = (UserConnectAskInPacket) packet;
        if(fPacket.getServer() != null)
        {
            jsonObject.addProperty("server", fPacket.getServer());
        }
        return jsonObject;

    }

    @Override
    public UserConnectAskInPacket read(@NotNull JsonObject jsonObject)
    {
        int code = jsonObject.get("code").getAsInt();
        String message = jsonObject.has("message") ? jsonObject.get("message").getAsString() : null;

        String server = jsonObject.has("server") ? jsonObject.get("server").getAsString() : null;

        return new UserConnectAskInPacket(this, code, message, server);
    }
}
