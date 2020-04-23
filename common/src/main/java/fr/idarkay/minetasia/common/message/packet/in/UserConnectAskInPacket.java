package fr.idarkay.minetasia.common.message.packet.in;

import com.google.gson.JsonObject;
import fr.idarkay.minetasia.common.message.MinetasiaPacketIn;
import fr.idarkay.minetasia.common.message.serializer.in.UserConnectAskInSerializer;
import org.jetbrains.annotations.Nullable;

/**
 * File <b>UserConnectAskInPacket</b> located on fr.idarkay.minetasia.common.message.packet.out
 * UserConnectAskInPacket is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 21/04/2020 at 23:29
 */
public class UserConnectAskInPacket extends MinetasiaPacketIn
{

    @Nullable private final String server;
    private final UserConnectAskInSerializer serializer;

    public UserConnectAskInPacket(UserConnectAskInSerializer serializer, int code, String repMessage, @Nullable String server)
    {
        super(code, repMessage);
        this.serializer = serializer;
        this.server = server;
    }

    public String getServer()
    {
        return server;
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
