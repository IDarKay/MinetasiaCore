package fr.idarkay.minetasia.common.message.packet.out;

import com.google.gson.JsonObject;
import fr.idarkay.minetasia.common.message.MinetasiaPacketOut;
import fr.idarkay.minetasia.common.message.serializer.out.UserConnectAskOutSerializer;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * File <b>UserConnectAskOutPacket</b> located on fr.idarkay.minetasia.common.message.packet.out
 * UserConnectAskOutPacket is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 21/04/2020 at 23:29
 */
public class UserConnectAskOutPacket extends MinetasiaPacketOut
{
    private final UserConnectAskOutSerializer serializer;
    private final UUID user;
    private final Document island;

    public UserConnectAskOutPacket(UserConnectAskOutSerializer serializer, boolean needRep, @Nullable UUID user, @Nullable Document island)
    {
        super(needRep);
        this.serializer = serializer;

        this.user = user;
        this.island = island;
    }

    public UUID getUser()
    {
        return user;
    }

    public Document getIsland()
    {
        return island;
    }

    @Override
    public JsonObject write()
    {
        return serializer.write(this);
    }

    @Override
    public String name()
    {
        return "userConnectAsk";
    }
}
