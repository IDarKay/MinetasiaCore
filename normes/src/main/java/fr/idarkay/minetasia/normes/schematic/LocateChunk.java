package fr.idarkay.minetasia.normes.schematic;

import org.bukkit.Chunk;

/**
 * File <b>LocateChunk</b> located on fr.idarkay.minetasia.normes.schematic
 * LocateChunk is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 01/03/2020 at 18:38
 */
public class LocateChunk
{

    private final int cx, cz;

    public LocateChunk(int cx, int cz)
    {
        this.cx = cx;
        this.cz = cz;
    }

    public LocateChunk(Chunk c)
    {
        this.cx = c.getX();
        this.cz = c.getZ();
    }

    public int getCx()
    {
        return cx;
    }

    public int getCz()
    {
        return cz;
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof LocateChunk && cx == ((LocateChunk) obj).cx && cz == ((LocateChunk) obj).cz;
    }
}
