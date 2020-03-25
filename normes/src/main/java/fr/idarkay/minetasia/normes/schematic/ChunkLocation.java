package fr.idarkay.minetasia.normes.schematic;

import org.bukkit.Chunk;
import org.bukkit.Location;

/**
 * File <b>ChunkLocation</b> located on fr.idarkay.minetasia.normes.schematic
 * ChunkLocation is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 01/03/2020 at 18:28
 */
public class ChunkLocation
{

    private int x, y, z;
    private LocateChunk chunk;

    public ChunkLocation(LocateChunk chunk, int x, int y, int z)
    {
        this.chunk = chunk;
        if(x < 0 || x > 15) throw new IllegalArgumentException("x need be in [0; 15] actual : " + x );
        if(z < 0 || z > 15) throw new IllegalArgumentException("z need be in [0; 15] actual" + z);

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ChunkLocation(Location location)
    {
        this(new LocateChunk(cordToChunkCord(location.getBlockX()), cordToChunkCord(location.getBlockZ())), cordToChunkInCord(location.getBlockX()), location.getBlockY(), cordToChunkInCord(location.getBlockZ()));
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getZ()
    {
        return z;
    }

    public LocateChunk getChunk()
    {
        return chunk;
    }

    private Location toLoc()
    {
        return new Location(null, chunkCordToCord(chunk.getCx(), x), y, chunkCordToCord(chunk.getCz(), z));
    }

    private static int chunkCordToCord(int cChunk, int cord)
    {
        return cChunk * 16 + cord;
    }

    public static int chunkInCord(int cChunk)
    {
        return cChunk * 16;
    }

    public static int cordToChunkInCord(int pos)
    {
        return pos < 0 ? 16 + pos%16 : pos%16;
    }

    private static int cordToChunkCord(int pos)
    {
        return (int) Math.floor(pos/16f);
    }


    /**
     * sort to location
     * @param location the future littlest location
     */
    public void sort(ChunkLocation location)
    {
        final Location loc1 = toLoc();
        final Location loc2 = location.toLoc();

        x = Math.min(loc1.getBlockX(), loc2.getBlockX());
        y = Math.min(loc1.getBlockY(), loc2.getBlockY());
        z = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

        location.x = Math.max(loc1.getBlockX(), loc2.getBlockX());
        location.y = Math.max(loc1.getBlockY(), loc2.getBlockY());
        location.z = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
    }

    @Override
    public String toString()
    {
        return "chunkLocation{" + x + " , " + y + " , " + z + ", " + chunk.getCx() + " ," + chunk.getCz();
    }
}
