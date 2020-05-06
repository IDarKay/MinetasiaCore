package fr.idarkay.minetasia.common.message;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * File <b>MinetasiaPacketManager</b> located on fr.idarkay.minetasia.common.message
 * MinetasiaPacketManager is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 21/04/2020 at 21:46
 */
public abstract class MinetasiaPacketManager
{

    private static final Map<String, MessageOutChanel<?,?>> registeredOutPacket = new HashMap<>();
    private static final Map<String, MessageInChanel<?,?>> registeredInPacket = new HashMap<>();

    public static final String channelPrefix = "packet-";

    public static void registerPacket(@NotNull  MessageOutChanel<?,?> messageInChanelIn, @Nullable MessageInChanel<?,?> messageChannelOut)
    {
        registeredOutPacket.put(channelPrefix + messageInChanelIn.getPacketSerializer().getName(), messageInChanelIn);
        if(messageChannelOut != null)
            registeredInPacket.put(channelPrefix + messageInChanelIn.getPacketSerializer().getName(), messageChannelOut);
    }

    @Nullable
    public static MessageOutChanel<?,?> getMessageOutChanel(@NotNull String name)
    {
        return registeredOutPacket.get(name);
    }

    @Nullable
    public static MessageInChanel<?,?> getMessageInChanel(@NotNull String name)
    {
        return registeredInPacket.get(name);
    }

    public static MinetasiaPacketInSerializer<?> getInFromOut(@NotNull MinetasiaPacketOut packetOut)
    {
        return getMessageInChanel(channelPrefix + packetOut.name()).getPacketSerializer();
    }

}
