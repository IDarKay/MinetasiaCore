package fr.idarkay.minetasia.normes.schematic;

import fr.idarkay.minetasia.normes.Direction;
import fr.idarkay.minetasia.normes.MaterialID;
import fr.idarkay.minetasia.normes.Utils.VoidConsumer;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.function.Consumer;

/**
 * File <b>SchematicUtils</b> located on fr.idarkay.minetasia.normes.schematic
 * SchematicUtils is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 01/03/2020 at 17:51
 */
public class SchematicUtils
{
    /**
     * create new schematic from world
     * @param min location (with minimum x y and z)
     * @param max location (with maximum x y and z)
     * @return create schematic
     * @throws IllegalArgumentException if both location dosen't have same world or loc min {@code > } loc max
     */
    public static Schematic createSchematic(Location min, Location max) throws IllegalArgumentException
    {
        World w0 = min.getWorld();
        World w1 = max.getWorld();

        if(w1 == null || w0 != w1)
        {
            throw new IllegalArgumentException("min location haven't same world of max location");
        }

        int x0 = min.getBlockX(), x1 = max.getBlockX();
        int y0 = min.getBlockY(), y1 = max.getBlockY();
        int z0 = min.getBlockZ(), z1 = max.getBlockZ();

        if(x0 > x1 || y0 > y1 || z0 > z1)
        {
            throw new IllegalArgumentException("min location is superior of max location");
        }

        short height = (short) (y1 - y0 + 1), length = (short) (x1 - x0 + 1), width = (short) (z1 - z0 + 1);

        final Material[] blocks = new Material[height * length * width];
        final String[] data = new String[height * length * width];
        final String[] metadata = new String[height * length * width];
        for (int x = x0; x <= x1 ; x++)
        {
            for(int z = z0; z <= z1; z++)
            {
                for(int y = y0; y <= y1; y++)
                {
                    Block block = w0.getBlockAt(x, y, z);
                    int i = ((y - y0) * length * width) + ((z - z0) * length) + (x - x0);
                    String d = block.getBlockData().getAsString(true);
                    data[i] = d.indexOf('[') == -1 ? null : d;
                    blocks[i] = w0.getBlockAt(x, y, z).getType();
                    final List<MetadataValue> schem = block.getMetadata("schem");
                    metadata[i] = schem.isEmpty() ? null : schem.get(0).asString();
                }
            }
        }

        return new Schematic(blocks, data, metadata, length, width, height);
    }


    /**
     * load a schematic in the world
     * @param schematic schematic to load
     * @param location location of the minimum location fo the build
     *  ignoreAir = {@code true} <br>
     *  direction = {@link Direction#NORTH} <br>
     *  consumer = {@code null}
     */
    public static void loadSchematic(JavaPlugin plugin, @NotNull Schematic schematic, @NotNull Location location)
    {
        loadSchematic(plugin, schematic, location, true, Direction.NORTH, null);
    }

    /**
     * load a schematic in the world
     * @param schematic schematic to load
     * @param location location of the minimum location fo the build
     * @param ignoreAir if true air block will be not place
     * @param d direction of the build {@link Direction}
     *  @param blockConsumer action to ably to all block (ignore air if true) if not use set {@code null}
     */
    public static void loadSchematic(JavaPlugin plugin, @NotNull Schematic schematic, @NotNull Location location, boolean ignoreAir, @NotNull Direction d, @Nullable Consumer<? super Block> blockConsumer)
    {
        final Material[] blocks = schematic.getBlocks();
        final String[] data = schematic.getData();
        final String[] metadata = schematic.getMetadata();
        World w = location.getWorld();
        boolean haveConsumer = blockConsumer != null;
        boolean isNorth = d == Direction.NORTH;

        if(w == null) throw new NullPointerException("location haven't world");

        int x0 = location.getBlockX(), y0 = location.getBlockY(), z0 = location.getBlockZ();
        int length = schematic.getLength(), width = schematic.getWidth(), height = schematic.getHeight();

        for (int x = 0; x < length ; x++)
        {
            for(int z = 0; z < width; z++)
            {
                for(int y = 0; y < height; y++)
                {
                    int i = y * length * width + z * length + x;
                    final Material m = blocks[i];
                    if(!ignoreAir || m.isAir())
                    {
                        Block block = w.getBlockAt(d.x(x, z, length, width) + x0, y + y0, d.z(x, z, length , width) + z0);
                        block.setType(m, true);
                        if(data[i] != null)
                        {
                            if(!isNorth &&  block.getBlockData() instanceof Directional)
                                block.setBlockData(Bukkit.createBlockData(d.rotateData(data[i])));
                            else
                                block.setBlockData(Bukkit.createBlockData(data[i]));
                        }
                        if(metadata[i] != null)
                        {
                            block.setMetadata("schem", new FixedMetadataValue(plugin, metadata[i]));
                        }
                        if(haveConsumer) blockConsumer.accept(block);
                    }
                }
            }
        }
    }

    public static void asyncLoadSchematic(JavaPlugin plugin,
            @NotNull Schematic schematic, @NotNull Location location,
                                   boolean ignoreAir, @NotNull Direction d, int cut, int interval,
                                   @Nullable Consumer<? super Block> blockConsumer,
                                   @NotNull VoidConsumer endConsumer)
    {
        final LoadSchematicStatement loadSchematicStatement = new LoadSchematicStatement(schematic, location, ignoreAir, d, cut, interval, blockConsumer, endConsumer);
        runPart(loadSchematicStatement, plugin );

    }

    private static void runPart(LoadSchematicStatement statement, JavaPlugin plugin)
    {
        final int length = statement.getLength(), width = statement.getWidth(), height = statement.getHeight();
        final int x0 = statement.getX0(), y0 = statement.getY0(), z0 = statement.getZ0();
        final Material[] blocks = statement.getBlocks();
        final String[] data = statement.getData();
        final String[] metadata = statement.getMetadata();
        final Direction d = statement.getDirection();
        final boolean ignoreAir = statement.isIgnoreAir(), isNorth = statement.isNorth(), haveConsumer = statement.isHaveConsumer();
        final Consumer<? super Block> blockConsumer = statement.getBlockConsumer();
        int c = 0;

        for (int x = statement.getX(); x < statement.getLength() ; x++)
        {
            statement.setX(x);
            for(int z = statement.getZ(); z < statement.getWidth(); z++)
            {
                statement.setZ(z);
                for(int y = statement.getY(); y < statement.getHeight(); y++)
                {
                    statement.setY(y);
                    if(c == statement.getCut())
                    {
                        Bukkit.getScheduler().runTaskLater(plugin, () -> runPart(statement, plugin), 10L);
                        return;
                    }

                    int i = y *  length * width + z * length + x;
                    final Material m = blocks[i];
                    if(!ignoreAir || !m.isAir())
                    {
                        c++;
                        final Block block = statement.getWorld().getBlockAt(d.x(x, z, length, width) + x0, y + y0, d.z(x, z, length , width) + z0);
                        block.setType(m, true);
                        if(data[i] != null)
                        {
                            if(!isNorth &&  block.getBlockData() instanceof Directional)
                                block.setBlockData(Bukkit.createBlockData(d.rotateData(data[i])));
                            else
                                block.setBlockData(Bukkit.createBlockData(data[i]));
                        }
                        if(metadata[i] != null)
                        {
                            block.setMetadata("schem", new FixedMetadataValue(plugin, metadata[i]));
                        }
                        if(haveConsumer) blockConsumer.accept(block);
                    }
                    else if(metadata[i] != null)
                    {
                        c++;
                        final Block block = statement.getWorld().getBlockAt(d.x(x, z, length, width) + x0, y + y0, d.z(x, z, length , width) + z0);
                        block.setMetadata("schem", new FixedMetadataValue(plugin, metadata[i]));
                        if(haveConsumer) blockConsumer.accept(block);
                    }
                }
                statement.setY(0);
            }
            statement.setZ(0);
        }
        statement.getEndConsumer().run();
    }


    private static final byte ENTER_CHAR = (byte) 10;
    private static final byte FORM_FEED_CHAR = (byte) 12;

    /**
     * save schematic in plugin/{@code <plugin name>}/schematic/{@code <schematic_name>}.minetasiaschem
     * @param schematic the schematic to save
     * @param name name of teh file
     * @throws IOException if error when write data
     */
    public static void saveSchematic(@NotNull Schematic schematic, @NotNull String name, File dataFolder) throws IOException
    {
        Validate.notNull(schematic);
        Validate.notNull(name);
        File directory = new File(dataFolder, "schematic");
        if(!directory.exists()) directory.mkdirs();

        File file = new File(directory, name + ".minetasiaschem");
        if(!file.exists())  if(!file.createNewFile()) throw new NullPointerException("file can't create");

        try(FileOutputStream fos = new FileOutputStream(file))
        {
            short max = (short) getMaxLen(schematic.getData());
            FileChannel fc = fos.getChannel();
            ByteBuffer buf = ByteBuffer.allocate(max);
            buf.putShort(schematic.getLength());
            drainBuffer(buf, fc);
            buf.putShort(schematic.getWidth());
            drainBuffer(buf, fc);
            buf.putShort(schematic.getHeight());
            drainBuffer(buf, fc);
            buf.putInt(schematic.getBlocks().length);
            drainBuffer(buf, fc);
            buf.putInt(max);
            drainBuffer(buf, fc);
            for(Material s : schematic.getBlocks())
            {
                buf.putShort(MaterialID.valueOf(s.name()).id);
                drainBuffer(buf, fc);
            }
            int i = 0;
            for(String s : schematic.getData())
            {
                if(s != null)
                {
                    String[] split = splitInTow(s,'[');
                    if(split[1] != null)
                    {
                        buf.put((i + ";[" + split[1]).getBytes());
                        buf.put(ENTER_CHAR);
                        drainBuffer(buf, fc);
                    }
                }
                i++;
            }
            buf.putInt(FORM_FEED_CHAR);
            drainBuffer(buf, fc);
            i = 0;
            for(String s : schematic.getMetadata())
            {
                if(s != null)
                {
                    buf.put((i + ";" + s).getBytes());
                    buf.put(ENTER_CHAR);
                    drainBuffer(buf, fc);
                }
                i++;
            }
        }
    }

    private static String[] splitInTow(String s, char spliter)
    {
        String[] back = new String[2];
        int i = s.indexOf(spliter);
        if(i == -1)
        {
            back[0] = s;
            back[1] = null;
        }
        else
        {
            back[0] = s.substring(0, i);
            back[1] = s.substring(i + 1);
        }
        return back;
    }

    private static void drainBuffer (ByteBuffer buffer, FileChannel fc) throws IOException {
        buffer.flip();
        fc.write(buffer);
        buffer.clear();
    }


    public static int getMaxLen(String[] data)
    {
        int max = 64;
        for(String s : data) if(s.length() > max) max = s.length();
        return max;
    }


    /**
     * read schematic from  plugin/{@code <plugin name>}/schematic/{@code <schematic_name>}.minetasiaschem
     * @param name nam of schematic
     * @return create schematic from data
     * @throws IOException if error on read data
     */
    public static Schematic readSchematic(String name, File dataFolder) throws IOException
    {
        final File file = new File(dataFolder, "schematic/" + name + ".minetasiaschem");
        if(!file.exists()) throw new IOException("file not exist");

        return readSchematic(file);
    }

    /**
     * read schematic from  plugin/{@code <plugin schemFile>}/schematic/{@code <schematic_name>}.minetasiaschem
     * @param schemFile file  of the schematic
     * @return create schematic from data
     * @throws IOException if error on read data
     */
    public static Schematic readSchematic(File schemFile) throws IOException
    {
        try(FileInputStream fis = new FileInputStream(schemFile))
        {
            MaterialID[] value = MaterialID.values();
            FileChannel fc = fis.getChannel();
            int size = (int)fc.size();
            ByteBuffer buf = ByteBuffer.allocate(size);
            fc.read(buf);
            buf.flip();
            short length =  buf.getShort();
            short width = buf.getShort();
            short height = buf.getShort();
            int sizeD = buf.getInt();
            int max = buf.getInt();

            String[] data = new String[sizeD];
            Material[] block = new Material[sizeD];
            String[] metadata = new String[sizeD];

            byte[] buffer = new byte[max];
            int r = 0;

            for(int i = 0; i < sizeD; i++)
            {
                block[i] = value[buf.getShort()].material;
            }
            byte b;
            while (buf.hasRemaining() && (b = buf.get()) != FORM_FEED_CHAR)
            {
                if (b != ENTER_CHAR)
                {
                    buffer[r++] = b;
                } else
                {
                    int len = 0;
                    for (int i = 0; i < buffer.length; i++)
                    {
                        if (buffer[i] == (char) 0)
                        {
                            len = i;
                            break;
                        }
                    }

                    byte[] result = new byte[len];

                    System.arraycopy(buffer, 0, result, 0, len);

                    String[] split = new String(result).split(";", 2);

                    data[Integer.parseInt(split[0])] = "minecraft:" + block[Integer.parseInt(split[0])].name().toLowerCase() + split[1];

                    r = 0;
                    buffer = new byte[max];
                }
            }
            r = 0;
            buffer = new byte[max];
            while (buf.hasRemaining())
            {
                b = buf.get();
                if (b != ENTER_CHAR)
                {
                    buffer[r++] = b;
                }
                else
                {
                    int len = 0;
                    for (int i = 0; i < buffer.length; i++)
                    {
                        if (buffer[i] == (char) 0)
                        {
                            len = i;
                            break;
                        }
                    }

                    byte[] result = new byte[len];

                    System.arraycopy(buffer, 0, result, 0, len);
                    String[] split = new String(result).split(";", 2);

                    metadata[Integer.parseInt(split[0])] = split[1];

                    r = 0;
                    buffer = new byte[max];
                }
            }
            return new Schematic(block, data, metadata, length, width, height);
        }
    }
}
