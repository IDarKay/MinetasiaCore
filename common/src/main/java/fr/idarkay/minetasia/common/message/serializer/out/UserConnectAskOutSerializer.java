package fr.idarkay.minetasia.common.message.serializer.out;

import com.google.gson.JsonObject;
import fr.idarkay.minetasia.common.message.MinetasiaPacket;
import fr.idarkay.minetasia.common.message.MinetasiaPacketOutSerializer;
import fr.idarkay.minetasia.common.message.packet.out.UserConnectAskOutPacket;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * File <b>UserConnectAskOutSerializer</b> located on fr.idarkay.minetasia.common.message.serializer.out
 * UserConnectAskOutSerializer is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 21/04/2020 at 23:37
 */
public class UserConnectAskOutSerializer extends MinetasiaPacketOutSerializer<UserConnectAskOutPacket>
{

    public UserConnectAskOutSerializer()
    {
        super("userConnectAsk");
    }

    @Override
    public JsonObject write(MinetasiaPacket packet)
    {
        JsonObject jsonObject = super.write(packet);
        UserConnectAskOutPacket fPacket = (UserConnectAskOutPacket) packet;
        jsonObject.addProperty("user", fPacket.getUser().toString());
        if(fPacket.getIsland() != null)
            jsonObject.addProperty("island", fPacket.getIsland().toJson());
        return jsonObject;
    }

    @Override
    public UserConnectAskOutPacket read(@NotNull JsonObject jsonObject)
    {
        Document island = jsonObject.has("island") ? Document.parse(jsonObject.get("island").getAsString()) : null;

        return new UserConnectAskOutPacket(this, true,
                UUID.fromString(jsonObject.get("uuid").getAsString()), island);
    }
}
