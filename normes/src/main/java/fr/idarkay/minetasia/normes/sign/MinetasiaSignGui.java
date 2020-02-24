package fr.idarkay.minetasia.normes.sign;

import fr.idarkay.minetasia.normes.Reflection;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * File <b>MinetasiaSignGui</b> located on fr.idarkay.minetasia.normes.sign
 * MinetasiaSignGui is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 24/02/2020 at 20:00
 */
public class MinetasiaSignGui
{
    public static final Map<UUID, MinetasiaSignGui> guiMap = new HashMap<>();

    private BiConsumer<Player, String[]> completeHandler;
    private String[] lines;

    public MinetasiaSignGui(BiConsumer<Player, String[]>  completeHandler)
    {
        this.completeHandler = completeHandler;
        this.lines = new String[4];
    }

    public MinetasiaSignGui setLines(String... lines)
    {
        if (lines.length != 4) throw new IllegalArgumentException("Must have at least 4 lines");

        this.lines = lines;
        return this;
    }

    private static final Method WORLD_GET_HANDLE = Reflection.getMethod(Objects.requireNonNull(Reflection.getCraftBukkitClass("CraftWorld")), false,"getHandle");
    private static final Class<?> PACKET_PLAYER_OUT_BLOCK_CHANGE =  Objects.requireNonNull(Reflection.getNMSClass("PacketPlayOutBlockChange"));
    private static final Class<?> BLOCK_POSITION = Objects.requireNonNull(Reflection.getNMSClass("BlockPosition"));
    private static final Constructor<?> PACKET_PLAYER_OUT_BLOCK_CHANGE_CONSTRUCTOR = Reflection.getConstructor(PACKET_PLAYER_OUT_BLOCK_CHANGE, false, Objects.requireNonNull(Reflection.getNMSClass("IBlockAccess")), BLOCK_POSITION);
    private static final Constructor<?> BLOCK_POSITION_CONSTRUCTOR = Reflection.getConstructor(BLOCK_POSITION, false, int.class, int.class, int.class);
    private static final Method CRAFT_MAGIC_NUMBERS_GET_BLOCK = Reflection.getMethod(Objects.requireNonNull(Reflection.getCraftBukkitClass("util.CraftMagicNumbers")), false, "getBlock", Material.class, byte.class);
    private static final Field BLOCK_FIELD = Reflection.getField(PACKET_PLAYER_OUT_BLOCK_CHANGE, "block", false);
    private static final Method CRAFT_SIGN_SAINTLINESS = Reflection.getMethod(Objects.requireNonNull(Reflection.getCraftBukkitClass("block.CraftSign")), false, "sanitizeLines", String[].class);

    private static final Class<?> TITLE_ENTITY_SIGN =  Objects.requireNonNull(Reflection.getNMSClass("TileEntitySign"));
    private static final Constructor<?> TITLE_ENTITY_SIGN_CONSTRUCTOR = Reflection.getConstructor(TITLE_ENTITY_SIGN, false);
    private static final Method SIGN_SET_POSITION = Reflection.getMethod(Objects.requireNonNull(Reflection.getNMSClass("TileEntity")), false, "setPosition", BLOCK_POSITION);
    private static final Field SIGN_LINES = Reflection.getField(TITLE_ENTITY_SIGN, "lines", true);
    private static final Method SIGN_GET_UPDATE_PACKET = Reflection.getMethod(TITLE_ENTITY_SIGN, false, "getUpdatePacket");

    private static final Constructor<?> PACKET_PLAY_OUT_OPEN_SIGN_EDITOR_CONSTRUCTOR = Reflection.getConstructor(Objects.requireNonNull(Reflection.getNMSClass("PacketPlayOutOpenSignEditor")), false, BLOCK_POSITION);

    public void open(Player player)
    {
        try
        {
            final Object blockPosition = BLOCK_POSITION_CONSTRUCTOR.newInstance( player.getLocation().getBlockX(), 1, player.getLocation().getBlockZ());

            final Object packet = PACKET_PLAYER_OUT_BLOCK_CHANGE_CONSTRUCTOR.newInstance(WORLD_GET_HANDLE.invoke(player.getWorld()), blockPosition);
            BLOCK_FIELD.set(packet, CRAFT_MAGIC_NUMBERS_GET_BLOCK.invoke(null, Material.OAK_SIGN, (byte) 0));
            Reflection.sendPacket(player, packet);

            final Object components = CRAFT_SIGN_SAINTLINESS.invoke(null, (Object) lines);

            final Object sign =  TITLE_ENTITY_SIGN_CONSTRUCTOR.newInstance();
            SIGN_SET_POSITION.invoke(sign, blockPosition);
            SIGN_LINES.set(sign, components);
            Reflection.sendPacket(player, SIGN_GET_UPDATE_PACKET.invoke(sign));

            Reflection.sendPacket(player, PACKET_PLAY_OUT_OPEN_SIGN_EDITOR_CONSTRUCTOR.newInstance(blockPosition));

            guiMap.put(player.getUniqueId(), this);


        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e)
        {
            e.printStackTrace();
        }

    }

    public BiConsumer<Player, String[]> getCompleteHandler()
    {
        return completeHandler;
    }
}
