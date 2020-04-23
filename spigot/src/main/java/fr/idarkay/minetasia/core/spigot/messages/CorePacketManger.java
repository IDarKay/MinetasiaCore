package fr.idarkay.minetasia.core.spigot.messages;

import fr.idarkay.minetasia.common.message.MessageInChanel;
import fr.idarkay.minetasia.common.message.MessageOutChanel;
import fr.idarkay.minetasia.common.message.MinetasiaPacketManager;
import fr.idarkay.minetasia.common.message.packet.in.UserConnectAskInPacket;
import fr.idarkay.minetasia.common.message.packet.out.UserConnectAskOutPacket;
import fr.idarkay.minetasia.common.message.serializer.in.UserConnectAskInSerializer;
import fr.idarkay.minetasia.common.message.serializer.out.UserConnectAskOutSerializer;

/**
 * File <b>CorePacketManger</b> located on fr.idarkay.minetasia.core.spigot.messages
 * CorePacketManger is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
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
