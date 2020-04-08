package fr.idarkay.minetasia.normes.utils;

/**
 * File <b>NMSUtils</b> located on fr.idarkay.minetasia.normes.utils
 * NMSUtils is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 06/04/2020 at 02:52
 */
public class NMSUtils
{
    public static short toDelta(double old, double neww)
    {
        double v = (neww * 32 - old * 32) * 128;
        if(v > Short.MAX_VALUE) throw new IllegalArgumentException("max move is 8 block ");
        return (short) v;
    }

    public static byte floatAngleToByte(float angle)
    {
        return (byte)((int)(angle * 256.0F / 360.0F));

    }
}
