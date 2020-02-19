package fr.idarkay.minetasia.core.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.Socket;

/**
 * File <b>SocketPrePossesEvent</b> located on fr.idarkay.minetasia.core.api.event
 * SocketPrePossesEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 09/02/2020 at 12:00
 */
public class SocketPrePossesEvent extends Event implements Cancellable
{


    private static HandlerList handlerList = new HandlerList();
    @NotNull private final Socket socket;
    @NotNull private final String chanel, value;
    private boolean canceled = false;
    private String answer = null;


    public SocketPrePossesEvent(@NotNull Socket socket, @NotNull String chanel, @NotNull String value)
    {
        super(true);
        this.socket = socket;
        this.chanel = chanel;
        this.value = value;
    }

    @NotNull
    public String getValue()
    {
        return value;
    }

    @NotNull
    public String getChanel()
    {
        return chanel;
    }

    public void setAnswer(@Nullable String answer)
    {
        this.answer = answer;
    }

    public String getAnswer()
    {
        return answer;
    }

    @NotNull
    public Socket getSocket()
    {
        return socket;
    }

    public static HandlerList getHandlerList()
    {
        return handlerList;
    }

    @Override
    public boolean isCancelled()
    {
        return canceled;
    }

    @Override
    public void setCancelled(boolean b)
    {
        this.canceled =b;
    }

    @Override
    public @NotNull HandlerList getHandlers()
    {
        return handlerList;
    }
}
