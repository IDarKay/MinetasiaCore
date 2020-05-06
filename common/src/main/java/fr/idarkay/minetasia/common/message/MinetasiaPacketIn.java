package fr.idarkay.minetasia.common.message;

/**
 * File <b>MinetasiaPacketIn</b> located on fr.idarkay.minetasia.common.message
 * MinetasiaPacketIn is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 21/04/2020 at 21:37
 */
public abstract class MinetasiaPacketIn implements MinetasiaPacket
{

    private final int code;
    private final String repMessage;

    public MinetasiaPacketIn(int code, String repMessage)
    {
        this.code = code;
        this.repMessage = repMessage;
    }

    public int getCode()
    {
        return code;
    }

    public String getRepMessage()
    {
        return repMessage;
    }

}
