package fr.idarkay.minetasia.core.api.event;

import fr.idarkay.minetasia.common.message.MinetasiaPacket;
import fr.idarkay.minetasia.common.message.MinetasiaPacketIn;
import fr.idarkay.minetasia.common.message.MinetasiaPacketOut;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * File <b>MinetasiaPacketCommingEvent</b> located on fr.idarkay.minetasia.common.message.event
 * MinetasiaPacketCommingEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 21/04/2020 at 21:55
 */
public class AsyncMinetasiaPacketCombingEvent extends Event
{

    @NotNull private static final HandlerList handlerList = new HandlerList();

    @NotNull private final MinetasiaPacket minetasiaPacket;
    private final boolean needRep;

    @Nullable
    private  MinetasiaPacketIn rep;

    public AsyncMinetasiaPacketCombingEvent(MinetasiaPacket minetasiaPacket)
    {
        super(true);

        this.minetasiaPacket = minetasiaPacket;
        needRep = minetasiaPacket instanceof MinetasiaPacketOut && ((MinetasiaPacketOut) minetasiaPacket).isNeedRep();

    }

    public MinetasiaPacket getPacket()
    {
        return minetasiaPacket;
    }

    public boolean isNeedRep()
    {
        return needRep;
    }

    public  void setRep(MinetasiaPacketIn rep)
    {
        if(!needRep) throw new UnsupportedOperationException("can't send rep to none rep packet :");
        this.rep = rep;
    }

    public @Nullable  MinetasiaPacketIn getRep()
    {
        return  rep;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public @NotNull static HandlerList getHandlerList()
    {
        return handlerList;
    }
}
