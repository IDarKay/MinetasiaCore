package fr.idarkay.minetasia.common.message;

/**
 * File <b>MinetasiaPacketOut</b> located on fr.idarkay.minetasia.common.message
 * MinetasiaPacketOut is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 21/04/2020 at 21:36
 */
public abstract class MinetasiaPacketOut implements MinetasiaPacket
{

    private final boolean needRep;

    public MinetasiaPacketOut(boolean needRep)
    {

        this.needRep = needRep;
    }

    public boolean isNeedRep()
    {
        return needRep;
    }

}
