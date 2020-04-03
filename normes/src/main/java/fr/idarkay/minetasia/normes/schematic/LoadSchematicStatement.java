package fr.idarkay.minetasia.normes.schematic;

import fr.idarkay.minetasia.normes.Direction;
import fr.idarkay.minetasia.normes.utils.VoidConsumer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * File <b>LoadSchematicStatement</b> located on fr.idarkay.minetasia.normes
 * LoadSchematicStatement is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 29/02/2020 at 20:34
 */
public class LoadSchematicStatement
{
    private final World world;
    private final VoidConsumer endConsumer;
    private final Consumer<? super Block> blockConsumer;
    private final Direction direction;
    private final Material[] blocks;
    private final String[] data;
    private final String[] metadata;
    private final boolean haveConsumer;
    private final boolean isNorth;
    private final boolean ignoreAir;
    private final int x0, y0, z0, length, width, height, cut, interval;
    private volatile int x = 0, y = 0, z = 0;

    public LoadSchematicStatement(@NotNull Schematic schematic, @NotNull Location location,
                                  boolean ignoreAir, @NotNull Direction d, int cut, int interval,
                                  @Nullable Consumer<? super Block> blockConsumer,
                                  @NotNull VoidConsumer endConsumer)
    {
        this.world = location.getWorld();
        this.endConsumer = endConsumer;
        this.blockConsumer = blockConsumer;
        this.interval = interval;
        this.haveConsumer = blockConsumer != null;
        this.direction = d;
        this.isNorth = d == Direction.NORTH;
        this.blocks = schematic.getBlocks();
        this.data = schematic.getData();
        this.metadata = schematic.getMetadata();
        this.x0 = location.getBlockX();
        this.y0 = location.getBlockY();
        this.z0 = location.getBlockZ();
        this.length = schematic.getLength();
        this.width = schematic.getWidth();
        this.height = schematic.getHeight();
        this.cut = cut;
        this.ignoreAir = ignoreAir;
    }

    public World getWorld()
    {
        return world;
    }

    public VoidConsumer getEndConsumer()
    {
        return endConsumer;
    }

    public Consumer<? super Block> getBlockConsumer()
    {
        return blockConsumer;
    }

    public Direction getDirection()
    {
        return direction;
    }

    public Material[] getBlocks()
    {
        return blocks;
    }

    public String[] getData()
    {
        return data;
    }

    public String[] getMetadata()
    {
        return metadata;
    }

    public boolean isHaveConsumer()
    {
        return haveConsumer;
    }

    public boolean isNorth()
    {
        return isNorth;
    }

    public int getX0()
    {
        return x0;
    }

    public int getY0()
    {
        return y0;
    }

    public int getZ0()
    {
        return z0;
    }

    public int getLength()
    {
        return length;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setZ(int z)
    {
        this.z = z;
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

    public boolean isIgnoreAir()
    {
        return ignoreAir;
    }

    public int getCut()
    {
        return cut;
    }
}
