package fr.idarkay.minetasia.normes.Utils;

import com.mojang.authlib.GameProfile;
import fr.idarkay.minetasia.normes.Reflection;
import org.bukkit.Material;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * File <b>ReflectionVar</b> located on fr.idarkay.minetasia.normes.Utils
 * ReflectionVar is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 04/03/2020 at 16:53
 */
public abstract class ReflectionVar
{
    public static final Method WORLD_GET_HANDLE = Reflection.getDeclaredMethod(Objects.requireNonNull(Reflection.getCraftBukkitClass("CraftWorld")), false,"getHandle");
    public static final Class<?> PACKET_PLAYER_OUT_BLOCK_CHANGE =  Objects.requireNonNull(Reflection.getNMSClass("PacketPlayOutBlockChange"));
    public static final Class<?> BLOCK_POSITION = Objects.requireNonNull(Reflection.getNMSClass("BlockPosition"));
    public static final Constructor<?> PACKET_PLAYER_OUT_BLOCK_CHANGE_CONSTRUCTOR = Reflection.getConstructor(PACKET_PLAYER_OUT_BLOCK_CHANGE, false, Objects.requireNonNull(Reflection.getNMSClass("IBlockAccess")), BLOCK_POSITION);
    public static final Constructor<?> BLOCK_POSITION_CONSTRUCTOR = Reflection.getConstructor(BLOCK_POSITION, false, int.class, int.class, int.class);
    public static final Method CRAFT_MAGIC_NUMBERS_GET_BLOCK = Reflection.getDeclaredMethod(Objects.requireNonNull(Reflection.getCraftBukkitClass("util.CraftMagicNumbers")), false, "getBlock", Material.class, byte.class);
    public static final Field BLOCK_FIELD = Reflection.getField(PACKET_PLAYER_OUT_BLOCK_CHANGE, "block", false);
    public static final Method CRAFT_SIGN_SAINTLINESS = Reflection.getDeclaredMethod(Objects.requireNonNull(Reflection.getCraftBukkitClass("block.CraftSign")), false, "sanitizeLines", String[].class);

    public static final Class<?> TITLE_ENTITY_SIGN =  Objects.requireNonNull(Reflection.getNMSClass("TileEntitySign"));
    public static final Constructor<?> TITLE_ENTITY_SIGN_CONSTRUCTOR = Reflection.getConstructor(TITLE_ENTITY_SIGN, false);
    public static final Method SIGN_SET_POSITION = Reflection.getDeclaredMethod(Objects.requireNonNull(Reflection.getNMSClass("TileEntity")), false, "setPosition", BLOCK_POSITION);
    public static final Field SIGN_LINES = Reflection.getField(TITLE_ENTITY_SIGN, "lines", true);
    public static final Method SIGN_GET_UPDATE_PACKET = Reflection.getDeclaredMethod(TITLE_ENTITY_SIGN, false, "getUpdatePacket");

    public static final Constructor<?> PACKET_PLAY_OUT_OPEN_SIGN_EDITOR_CONSTRUCTOR = Reflection.getConstructor(Objects.requireNonNull(Reflection.getNMSClass("PacketPlayOutOpenSignEditor")), false, BLOCK_POSITION);

    public static final Method GET_WORLD_HANDLE = Reflection.getDeclaredMethod(Objects.requireNonNull(Reflection.getCraftBukkitClass("CraftWorld")), false, "getHandle");
    public static final Method GET_WORLD_TILE_ENTITY = Reflection.getMethod(Objects.requireNonNull(Reflection.getNMSClass("WorldServer")), false, "getTileEntity", BLOCK_POSITION);
    public static final Method SET_GAME_PROFILE = Reflection.getDeclaredMethod(Objects.requireNonNull(Reflection.getNMSClass("TileEntitySkull")), false, "setGameProfile", GameProfile.class);


}
