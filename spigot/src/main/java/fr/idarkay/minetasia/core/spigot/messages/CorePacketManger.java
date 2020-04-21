package fr.idarkay.minetasia.core.spigot.messages;

import fr.idarkay.minetasia.core.api.message.MessageInChanel;
import fr.idarkay.minetasia.core.api.message.MessageOutChanel;
import fr.idarkay.minetasia.core.api.message.MinetasiaPacketManager;
import fr.idarkay.minetasia.core.spigot.messages.packet.in.UserConnectAskInPacket;
import fr.idarkay.minetasia.core.spigot.messages.packet.out.UserConnectAskOutPacket;
import fr.idarkay.minetasia.core.spigot.messages.serializer.in.UserConnectAskInSerializer;
import fr.idarkay.minetasia.core.spigot.messages.serializer.out.UserConnectAskOutSerializer;

/**
 * File <b>CorePacketManger</b> located on fr.idarkay.minetasia.core.spigot.messages
 * CorePacketManger is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 21/04/2020 at 23:43
 */
public abstract class CorePacketManger
{

    public static void init()
    {
        MinetasiaPacketManager.registerPacket(
                new MessageOutChanel<>(new UserConnectAskOutSerializer(), UserConnectAskOutPacket.class),
                new MessageInChanel<>(new UserConnectAskInSerializer(), UserConnectAskInPacket.class)
        );
    }

}
