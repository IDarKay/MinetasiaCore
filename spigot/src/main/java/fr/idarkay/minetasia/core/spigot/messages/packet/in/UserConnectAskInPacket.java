package fr.idarkay.minetasia.core.spigot.messages.packet.in;

import com.google.gson.JsonObject;
import fr.idarkay.minetasia.core.api.message.MinetasiaPacketIn;
import fr.idarkay.minetasia.core.spigot.messages.serializer.in.UserConnectAskInSerializer;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Nullable;

/**
 * File <b>UserConnectAskInPacket</b> located on fr.idarkay.minetasia.core.spigot.messages.packet.out
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
        Validate.isTrue(server != null || code > 299);
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
