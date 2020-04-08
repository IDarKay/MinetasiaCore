package fr.idarkay.minetasia.normes;

import fr.idarkay.minetasia.normes.Listener.PlayerPacketListener;
import fr.idarkay.minetasia.normes.Listener.PlayerQuitListener;
import fr.idarkay.minetasia.normes.hologram.Hologram;
import fr.idarkay.minetasia.normes.npc.MinetasiaNpc;
import fr.idarkay.minetasia.normes.packet.PlayerConnectionListener;
import fr.idarkay.minetasia.normes.schematic.Schematic;
import fr.idarkay.minetasia.normes.schematic.SchematicUtils;
import fr.idarkay.minetasia.normes.utils.VoidConsumer;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.function.Consumer;

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

    private volatile static boolean playerPacketComingEventRegister = false;
    private volatile static boolean isEnable;

    @Override
    public void onEnable()
    {
        if(!isEnable)
        {
            MinetasiaNpc.setPlugin(this);
            Hologram.setPlugin(this);
            getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
            isEnable = true;
            registerPlayerPacketComingEvent();
        }
    }

    public void registerPlayerPacketComingEvent()
    {
        if(!playerPacketComingEventRegister)
        {
            playerPacketComingEventRegister = true;
            new PlayerConnectionListener(this);
            new PlayerPacketListener(this);
        }
    }

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

    /**
     * create new schematic from world
     * @param min location (with minimum x y and z)
     * @param max location (with maximum x y and z)
     * @return create schematic
     * @throws IllegalArgumentException if both location dosen't have same world or loc min {@code > } loc max
     */
    public Schematic createSchematic(Location min, Location max) throws IllegalArgumentException
    {
        return SchematicUtils.createSchematic(min, max);
    }

    private static final Material[] AIR = new Material[]{Material.AIR, Material.VOID_AIR, Material.CAVE_AIR};

    public void loadSchematicInChunk(@NotNull Schematic schematic, @NotNull ChunkGenerator.ChunkData chunkData, int height)
    {
        SchematicUtils.loadSchematicInOneChunk(schematic, chunkData, height);
    }

    /**
     * load a schematic in the world
     * @param schematic schematic to load
     * @param location location of the minimum location fo the build
     *  ignoreAir = {@code true} <br>
     *  direction = {@link Direction#NORTH} <br>
     *  consumer = {@code null}
     */
    public void loadSchematic(@NotNull Schematic schematic, @NotNull Location location)
    {
        SchematicUtils.loadSchematic(this, schematic, location, true, Direction.NORTH, null);
    }

    /**
     * load a schematic in the world
     * @param schematic schematic to load
     * @param location location of the minimum location fo the build
     * @param ignoreAir if true air block will be not place
     * @param d direction of the build {@link Direction}
    *  @param blockConsumer action to ably to all block (ignore air if true) if not use set {@code null}
     */
    public void loadSchematic(@NotNull Schematic schematic, @NotNull Location location, boolean ignoreAir, @NotNull Direction d, @Nullable Consumer<? super Block> blockConsumer)
    {
        SchematicUtils.loadSchematic(this, schematic, location, ignoreAir, d, blockConsumer);
    }

    public void asyncLoadSchematic(@NotNull Schematic schematic, @NotNull Location location,
                                   boolean ignoreAir, @NotNull Direction d, int cut, int interval,
                                   @Nullable Consumer<? super Block> blockConsumer,
                                   @NotNull VoidConsumer endConsumer)
    {
      SchematicUtils.asyncLoadSchematic(this, schematic, location, ignoreAir, d, cut, interval, blockConsumer, endConsumer);

    }



    /**
     * save schematic in plugin/{@code <plugin name>}/schematic/{@code <schematic_name>}.minetasiaschem
     * @param schematic the schematic to save
     * @param name name of teh file
     * @throws IOException if error when write data
     */
    public void saveSchematic(@NotNull Schematic schematic, @NotNull String name) throws IOException
    {
        SchematicUtils.saveSchematic(schematic, name, getDataFolder());
    }

    /**
     * read schematic from  plugin/{@code <plugin name>}/schematic/{@code <schematic_name>}.minetasiaschem
     * @param name nam of schematic
     * @return create schematic from data
     * @throws IOException if error on read data
     */
    public Schematic readSchematic(String name) throws IOException
    {
        return SchematicUtils.readSchematic(name, getDataFolder());
    }

    /**
     * read schematic from  plugin/{@code <plugin schemFile>}/schematic/{@code <schematic_name>}.minetasiaschem
     * @param schemFile file  of the schematic
     * @return create schematic from data
     * @throws IOException if error on read data
     */
    public Schematic readSchematic(File schemFile) throws IOException
    {
       return SchematicUtils.readSchematic(schemFile);
    }

    /**
     * save data in world folder ( ./world/data/{@code <key>}.dat in file value
     * @param world world where save data
     * @param key key of the value need can be a filename
     * @param value value of the key null for remove data
     */
    public void saveDataInWorld(@NotNull World world, @NotNull String key, @Nullable String value)
    {
        Validate.notNull(world);
        Validate.notNull(key);
        Validate.notEmpty(key);

        final File f = new File(world.getWorldFolder(),  "data/" + key + ".dat");

        if(value == null)
        {
            if(f.exists()) f.delete();
        }
            else
        {
            try
            {
                if(!f.exists())
                {
                    if(f.getParentFile().exists()) f.getParentFile().mkdirs();
                }
                f.createNewFile();

                try(FileOutputStream fos = new FileOutputStream(f))
                {
                    final byte[] valueData = value.getBytes();

                    final FileChannel fc = fos.getChannel();
                    final ByteBuffer buf = ByteBuffer.allocate(valueData.length);

                    buf.put(valueData);
                    drainBuffer(buf, fc);
                }
            }
                catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static void drainBuffer (ByteBuffer buffer, FileChannel fc) throws IOException {
        buffer.flip();
        fc.write(buffer);
        buffer.clear();
    }

    /**
     * get data in world folder ( ./world/data/{@code <key>}.dat in file value
     * @param world where get the data
     * @param key key of the data
     * @return load data or null if not found
     */
    @Nullable
    public String getDataInWorld(@NotNull World world, @NotNull String key)
    {
        Validate.notNull(world);
        Validate.notNull(key);
        Validate.notEmpty(key);

        final File f = new File(world.getWorldFolder(), "data/" +key + ".dat");

        if(f.exists())
        {
            try(FileInputStream fis = new FileInputStream(f))
            {
                FileChannel fc = fis.getChannel();
                int size = (int) fc.size();
                ByteBuffer buf = ByteBuffer.allocate(size);

                fc.read(buf);
                buf.flip();

                return new String(buf.array());

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }
        else return  null;
    }

}
