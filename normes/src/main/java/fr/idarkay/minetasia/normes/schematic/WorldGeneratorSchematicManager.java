package fr.idarkay.minetasia.normes.schematic;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;


/**
 * File <b>WorldGeneratorSchematicManager</b> located on fr.idarkay.minetasia.normes.schematic
 * WorldGeneratorSchematicManager is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 01/03/2020 at 20:22
 */
public class WorldGeneratorSchematicManager
{

    private List<ChunkedSchematic> list = new ArrayList<>();
    private final Map<UUID, List<LocatedMetadata>> metadataMap = new HashMap<>();

    public void putSchem(ChunkedSchematic schematic)
    {
        list.add(schematic);
    }

    public ChunkGenerator.ChunkData apply(@NotNull World world, @NotNull ChunkGenerator.ChunkData chunkData, int cx, int cz, BiConsumer<Block, String> metada)
    {
        for (ChunkedSchematic chunkedSchematic : list)
        {
            if(chunkedSchematic.isInclude(cx, cz))
            {
                if(!metadataMap.containsKey(chunkedSchematic.getUuid()))
                {
                    metadataMap.put(chunkedSchematic.getUuid(), new ArrayList<>());
                }
                chunkedSchematic.apply(world, cx, cz, chunkData, (location, s) -> metada.accept(location.getBlock(),  s));
                return chunkData;
            }
        }
        return chunkData;
    }

    @NotNull
    public List<LocatedMetadata> getMetadata(ChunkedSchematic chunkedSchematic)
    {
        return metadataMap.getOrDefault(chunkedSchematic.getUuid(), new ArrayList<>());
    }

}
