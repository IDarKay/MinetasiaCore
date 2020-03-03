package fr.idarkay.minetasia.normes.schematic;

import fr.idarkay.minetasia.normes.Direction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * File <b>ChunkedSchematic</b> located on fr.idarkay.minetasia.normes.schematic
 * ChunkedSchematic is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 01/03/2020 at 18:02
 */
public class ChunkedSchematic
{
    private UUID uuid = UUID.randomUUID();
    private final ChunkLocation min, max;
    private final Schematic schematic;

    public ChunkedSchematic(Schematic schematic, Location location)
    {
        this.schematic = schematic;
        System.out.println(location);
        this.min = new ChunkLocation(location);
        System.out.println(new Location(null, schematic.getLength(), schematic.getHeight(), schematic.getWidth()).add(location));
        this.max = new ChunkLocation(new Location(null, schematic.getLength(), schematic.getHeight(), schematic.getWidth()).add(location));
        min.sort(max);
        System.out.println(min);
        System.out.println(max);
        System.out.println(schematic.getLength() * schematic.getHeight() * schematic.getWidth());
    }

    public boolean isInclude(int cx, int cz)
    {
        return cx >= min.getChunk().getCx() && cx <= max.getChunk().getCx() && cz >= min.getChunk().getCz() && cz <= max.getChunk().getCz();
    }

    public void apply(World world, int cx, int cz, ChunkGenerator.ChunkData chunkData, @NotNull BiConsumer<Location, String> metadata)
    {
        if(!isInclude(cx, cz)) return;
        System.out.println("CHUNK => " +cx + " ; " + cz);
        final int ccz = ChunkLocation.chunkInCord(cz);
        final int ccx = ChunkLocation.chunkInCord(cx);
        final int sZ = getStartZ(cz), mZ = getMaxZ(cz), sY = Math.max(0, min.getY()), mY = Math.min(chunkData.getMaxHeight(), max.getY());
        for(int x = getStartX(cx) ; x <= getMaxX(cx) ; x++)
        {
            for(int z = sZ; z <= mZ ; z++)
            {
                for(int y = sY ; y < mY ; y++)
                {
                    try
                    {
                        final int i = (y  - min.getY()) * schematic.getLength() * schematic.getWidth() + (z + ccz - min.getZ()) * schematic.getLength() + (x + ccx - min.getX());
                        if(!schematic.getBlocks()[i].isAir())
                        {
                            chunkData.setBlock(x, y, z, schematic.getBlocks()[i]);
                            if(schematic.getData()[i] != null)
                            {
                                chunkData.setBlock(x, y, z, Bukkit.createBlockData(schematic.getData()[i]));
                            }
                        }
                        if(schematic.getMetadata()[i] != null)
                        {
                            metadata.accept(new Location(world, x + ccx, y, z + ccz), schematic.getMetadata()[i]);
                        }
                    }
                    catch (ArrayIndexOutOfBoundsException ignore)
                    {

                    }
                }
            }
        }
    }

    private int getStartZ(int cz)
    {
        final int ccz = ChunkLocation.chunkInCord(cz);
        return ccz >= min.getZ() ? 0 : ChunkLocation.cordToChunkInCord(min.getZ());
    }

    private int getMaxZ(int cz)
    {
        final int ccz = ChunkLocation.chunkInCord(cz) + 15;
        return ccz < max.getZ() ? 15 : ChunkLocation.cordToChunkInCord(max.getZ()) -1;
    }

    private int getStartX(int cx)
    {
        final int ccx = ChunkLocation.chunkInCord(cx);
        return ccx >= min.getX() ? 0 : ChunkLocation.cordToChunkInCord(min.getX());
    }

    private int getMaxX(int cx)
    {
        final int ccx = ChunkLocation.chunkInCord(cx) + 15;
        return ccx < max.getX() ? 15 : ChunkLocation.cordToChunkInCord(max.getX()) -1;
    }

    public UUID getUuid()
    {
        return uuid;
    }
}
