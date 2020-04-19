package fr.idarkay.minetasia.normes.event;

import org.apache.commons.lang.Validate;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * File <b>PacketComingEvent</b> located on fr.idarkay.minetasia.normes.packet
 * PacketComingEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 24/02/2020 at 22:02
 */
public class PlayerPacketComingEvent extends Event implements Cancellable
{
    private static HandlerList handlerList = new HandlerList();

    private final Object packet;
    private final UUID player;
    private boolean cancelled = false;

    public PlayerPacketComingEvent(@NotNull  final UUID player, @NotNull final Object packet)
    {
        super(true);
        Validate.notNull(player);
        Validate.notNull(packet);
        this.player = player;
        this.packet = packet;
    }

    public UUID getPlayer()
    {
        return player;
    }

    public Object getPacket()
    {
        return packet;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList()
    {
        return handlerList;
    }

    @Override
    public boolean isCancelled()
    {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b)
    {
        cancelled = b;
    }
}
