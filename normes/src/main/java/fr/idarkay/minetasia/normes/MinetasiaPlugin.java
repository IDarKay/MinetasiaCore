package fr.idarkay.minetasia.normes;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author AloIs B. (IDarKay),
 * <p>
 *     Copyright (c) 2019 Normes, Minetasia.
 * </p>
 * @since 1.0
 */
@SuppressWarnings("unused")
public abstract class MinetasiaPlugin extends JavaPlugin {



    private MinetasiaLang minetasiaLang;

    /**
     * @see MinetasiaLang
     * @return instance of {@link MinetasiaLang}
     */
    public MinetasiaLang getMinetasiaLang() {
        if(minetasiaLang == null)
        {
            minetasiaLang = new MinetasiaLang(getDataFolder());
        }
        return minetasiaLang;
    }

    /**
     * Create the default tmp folder <br>
     * standard: <br>
     * all tmp file must be set into plugin/{@code <plugin name>}/tmp
     *
     * @since 1.0
     */
    public void saveDefaultTmpFolder()
    {
        File f = new File(getDataFolder(),"tmp");
        if(!f.exists()) if(!f.mkdirs()) Bukkit.getLogger().warning("[ERROR] can't create " + f.getAbsolutePath());
    }

    /**
     * Create the default schematic folder <br>
     * standard: <br>
     * all schematic file must be set into plugin/{@code <plugin name>}/schematic
     *
     * @since 1.0
     */
    public void saveDefaultSchematicFolder()
    {
        File f = new File(getDataFolder(),"schematic");
        if(!f.exists()) if(!f.mkdirs()) Bukkit.getLogger().warning("[ERROR] can't create " + f.getAbsolutePath());
    }

    /**
     * Create the default data folder <br>
     * standard: <br>
     * all tmp file must be set into plugin/{@code <plugin name>}/tmp
     *
     * @since 1.0
     */
    public void saveDefaultDataFolder()
    {
        File f = new File(getDataFolder(),"data");
        if(!f.exists()) if(!f.mkdirs()) Bukkit.getLogger().warning("[ERROR] can't create " + f.getAbsolutePath());
    }


    public Schematic createSchematic(Location min, Location max) throws IllegalArgumentException
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

        int height = y1 - y0 + 1, length = x1 - x0 + 1, width = z1 - z0 + 1;

        String[] blocks = new String[height * length * width];
        String[] data = new String[height * length * width];
        for (int x = x0; x <= x1 ; x++)
        {
            for(int z = z0; z <= z1; z++)
            {
                for(int y = y0; y <= y1; y++)
                {
                    Block block = w0.getBlockAt(x, y, z);
                    int i = ((y - y0) * length * width) + ((z - z0) * length) + (x - x0);
                    data[i] = block.getBlockData().getAsString(true);
                    blocks[i] = w0.getBlockAt(x, y, z).getType().name();
                }
            }
        }

        return new Schematic(blocks, data, length, width, height);
    }

    public void loadSchematic(@NotNull Schematic schematic, @NotNull Location location)
    {
        String[] blocks = schematic.getBlocks();
        String[] data = schematic.getData();
        World w = location.getWorld();

        if(w == null) throw new NullPointerException("location haven't world");

        int x0 = location.getBlockX(), y0 = location.getBlockY(), z0 = location.getBlockZ();
        int length = schematic.getLength(), width = schematic.getWidth(), height = schematic.getHeight();

        for (int x = 0; x < length ; x++)
        {
            for(int z = 0; z < width; z++)
            {
                for(int y = 0; y < height; y++)
                {
                    Block block = w.getBlockAt(x + x0, y + y0, z + z0);
                    int i = y * length * width + z * length + x;
                    block.setType(Material.valueOf(blocks[i]), true);
                    if(data[1] != null && !data[i].equals(""))
                        block.setBlockData(Bukkit.createBlockData(data[i]));
                }
            }
        }
    }

    private static final byte ENTER_CHAR = (byte) 10;

    public void saveSchematic(Schematic schematic, String name) throws IOException
    {
        File directory = new File(getDataFolder(), "schematic");
        if(!directory.exists()) directory.mkdirs();

        File file = new File(directory, name + ".minetasiaschem");
        if(!file.exists())  if(!file.createNewFile()) throw new NullPointerException("file can't create");

        try(FileOutputStream fos = new FileOutputStream(file))
        {
            int max = getMaxLen(schematic.getData());
            FileChannel fc = fos.getChannel();
            ByteBuffer buf = ByteBuffer.allocate(max);
            buf.putInt(schematic.getLength());
            drainBuffer(buf, fc);
            buf.putInt(schematic.getWidth());
            drainBuffer(buf, fc);
            buf.putInt(schematic.getHeight());
            drainBuffer(buf, fc);
            buf.putInt(schematic.getBlocks().length);
            drainBuffer(buf, fc);
            buf.putInt(max);
            drainBuffer(buf, fc);
            for(String s : schematic.getBlocks())
            {
                buf.put(s.getBytes());
                buf.put(ENTER_CHAR);
                drainBuffer(buf, fc);
            }
            for(String s : schematic.getData())
            {
                buf.put(s.getBytes());
                buf.put(ENTER_CHAR);
                drainBuffer(buf, fc);
            }
        }
    }


    private static void drainBuffer (ByteBuffer buffer, FileChannel fc) throws IOException {
        buffer.flip();
        fc.write(buffer);
        buffer.clear();
    }


    public int getMaxLen(String[] data)
    {
        int max = 128;
        for(String s : data) if(s.length() > max) max = s.length();
        return max;
    }

    public Schematic readSchematic(String name) throws IOException
    {
        File file = new File(getDataFolder(), "schematic/" + name + ".minetasiaschem");
        if(!file.exists()) throw new IOException("file not exist");

        try(FileInputStream fis = new FileInputStream(file))
        {
            FileChannel fc = fis.getChannel();
            int size = (int)fc.size();
            ByteBuffer buf = ByteBuffer.allocate(size);
            fc.read(buf);
            buf.flip();
            int length =  buf.getInt();
            int width = buf.getInt();
            int height = buf.getInt();
            int sizeD = buf.getInt();
            int max = buf.getInt();

            String[] data = new String[sizeD];
            String[] block = new String[sizeD];

            byte[] buffer = new byte[max];
            int r = 0;
            int c = 0;

            while (buf.hasRemaining()){
                byte b = buf.get();
                if(b != ENTER_CHAR)
                {
                    buffer[r++] = b;
                }
                else
                {
                    int len = 0;
                    for(int i = 0; i < buffer.length; i++)
                    {
                        if(buffer[i] == (char) 0)
                        {
                            len = i;
                            break;
                        }
                    }

                    byte[] result = new byte[len];

                    System.arraycopy(buffer, 0, result, 0, len);

                    if(c < sizeD)
                        block[c] = new String(result);
                    else
                        data[c - sizeD] = new String(result);
                    c++;

                    r = 0;
                    buffer = new byte[max];
                }
            }
            return new Schematic(block, data, length, width, height);
        }
    }

}
