package fr.idarkay.minetasia.normes.utils;

import com.mojang.authlib.GameProfile;
import fr.idarkay.minetasia.normes.Reflection;
import org.bukkit.Material;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * File <b>ReflectionVar</b> located on fr.idarkay.minetasia.normes.utils
 * ReflectionVar is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
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
    public static final Field BLOCK_FIELD = Reflection.getDeclaredField(PACKET_PLAYER_OUT_BLOCK_CHANGE, "block", false);
    public static final Method CRAFT_SIGN_SAINTLINESS = Reflection.getDeclaredMethod(Objects.requireNonNull(Reflection.getCraftBukkitClass("block.CraftSign")), false, "sanitizeLines", String[].class);

    public static final Method GET_NMS_SERVER = Reflection.getDeclaredMethod(Objects.requireNonNull(Reflection.getCraftBukkitClass("CraftServer")), false,"getServer");

    public static final Class<?> TITLE_ENTITY_SIGN =  Objects.requireNonNull(Reflection.getNMSClass("TileEntitySign"));
    public static final Constructor<?> TITLE_ENTITY_SIGN_CONSTRUCTOR = Reflection.getConstructor(TITLE_ENTITY_SIGN, false);
    public static final Method SIGN_SET_POSITION = Reflection.getDeclaredMethod(Objects.requireNonNull(Reflection.getNMSClass("TileEntity")), false, "setPosition", BLOCK_POSITION);
    public static final Field SIGN_LINES = Reflection.getDeclaredField(TITLE_ENTITY_SIGN, "lines", true);
    public static final Method SIGN_GET_UPDATE_PACKET = Reflection.getDeclaredMethod(TITLE_ENTITY_SIGN, false, "getUpdatePacket");

    public static final Constructor<?> PACKET_PLAY_OUT_OPEN_SIGN_EDITOR_CONSTRUCTOR = Reflection.getConstructor(Objects.requireNonNull(Reflection.getNMSClass("PacketPlayOutOpenSignEditor")), false, BLOCK_POSITION);

    public static final Method GET_WORLD_HANDLE = Reflection.getDeclaredMethod(Objects.requireNonNull(Reflection.getCraftBukkitClass("CraftWorld")), false, "getHandle");
    public static final Method GET_WORLD_TILE_ENTITY = Reflection.getMethod(Objects.requireNonNull(Reflection.getNMSClass("WorldServer")), false, "getTileEntity", BLOCK_POSITION);
    public static final Method SET_GAME_PROFILE = Reflection.getDeclaredMethod(Objects.requireNonNull(Reflection.getNMSClass("TileEntitySkull")), false, "setGameProfile", GameProfile.class);

    public final static Class<?> CRAFT_PLAYER_CLASS = Reflection.getCraftBukkitClass("entity.CraftPlayer");
    public final static Method CRAFT_PLAYER_GET_HANDLE = Reflection.getDeclaredMethod(Objects.requireNonNull(CRAFT_PLAYER_CLASS), true, "getHandle");
    public final static Class<?> ENTITY_HUMAN_CLASS = Objects.requireNonNull(Reflection.getNMSClass("EntityHuman"));
    public final static Class<?> ENTITY_PLAYER_CLASS = Objects.requireNonNull(Reflection.getNMSClass("EntityPlayer"));
    public final static Method ENTITY_PLAYER_GET_PROFILE = Reflection.getDeclaredMethod(ENTITY_HUMAN_CLASS, true,"getProfile");
    public final static Field ENTITY_PLAYER_CONFECTION_FIELD = Reflection.getField(ENTITY_PLAYER_CLASS, "playerConnection", false);

    public final static Class<?> PACKET_CLASS = Reflection.getNMSClass("Packet");
    public final static Method SEND_PACKET_METHOD = Reflection.getMethod(ENTITY_PLAYER_CONFECTION_FIELD.getType(), false, "sendPacket", ReflectionVar.PACKET_CLASS);

    public final static Class<?> I_CHAT_BASE_COMPONENT = Objects.requireNonNull(Reflection.getNMSClass("IChatBaseComponent"));

}
