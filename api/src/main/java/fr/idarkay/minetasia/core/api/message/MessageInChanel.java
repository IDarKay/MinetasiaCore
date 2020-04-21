package fr.idarkay.minetasia.core.api.message;

/**
 * File <b>MessageChanel</b> located on fr.minetasia.skyblock.core.message
 * MessageChanel is a part of MinetasiaSkyblockCore.
 * <p>
 * Copyright (c) 2020 MinetasiaSkyblockCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 21/04/2020 at 21:07
 */
public class MessageInChanel<T extends MinetasiaPacketInSerializer<R>, R extends MinetasiaPacketIn>
{
    private final T packetSerializer;
    private final Class<R> packetClass;

    public MessageInChanel(T packetSerializer, Class<R> packetClass)
    {
        this.packetSerializer = packetSerializer;
        this.packetClass = packetClass;
    }

    public Class<R> getPacketClass()
    {
        return packetClass;
    }

    public T getPacketSerializer()
    {
        return packetSerializer;
    }

}

