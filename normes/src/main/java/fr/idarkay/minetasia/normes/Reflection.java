package fr.idarkay.minetasia.normes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * File <b>Reflexion</b> located on fr.idarkay.minetasia.skyblockbattle.utils
 * Reflexion is a part of MinetasiaSkyBlockBattle.
 * <p>
 * Copyright (c) 2019 MinetasiaSkyBlockBattle.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 19/12/2019 at 16:55
 */
public class Reflection
{

    /**
     * send packet to player
     * @param player to send packet
     * @param packet packet
     */
    public static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get net.minecraft.server Class
     * @param name name of class
     * @return Class or null
     */
    public static Class<?> getNMSClass(String name) {
        try
        {
            return Class.forName("net.minecraft.server."
                    + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get craftBukkit Class
     * @param name name of class
     * @return Class or null
     * @deprecated use {@link Reflection#getCraftBukkitClass(String)}
     */
    public static Class<?> getNMSBClass(String name)
    {
        try
        {
            return Class.forName("org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        }
        catch (ClassNotFoundException ex) { ex.printStackTrace(); }
        return null;
    }

    /**
     * get craftBukkit Class
     * @param name name of class
     * @return Class or null
     */
    public static Class<?> getCraftBukkitClass(String name)
    {
        try
        {
            return Class.forName("org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        }
        catch (ClassNotFoundException ex) { ex.printStackTrace(); }
        return null;
    }

    private final static Method iChatCon;

    static {
        try {
            iChatCon = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Object getIChatBaseComponent(String msg) throws InvocationTargetException, IllegalAccessException
    {
        return iChatCon.invoke(null, "{\"text\": \"" + msg + "\"}");
    }


    private final static Class<?> worldBorderClass = getNMSClass("WorldBorder");
    private final static Class<?> packetPlayOutWorldBorderClass = getNMSClass("PacketPlayOutWorldBorder");
    private final static Class<?> packetPlayOutWorldBorderEnumWorldBorderActionClass = getNMSClass("PacketPlayOutWorldBorder$EnumWorldBorderAction");

    private final static Constructor<?> packetPlayOutWorldBorderConstructor = getConstructor(packetPlayOutWorldBorderClass, false);
    private final static Constructor<?> worldBorderConstructor = getConstructor(worldBorderClass, false);

    private final static Method worldBorderSetCenter = getDeclaredMethod(worldBorderClass, false, "setCenter", double.class, double.class);
    private final static Method worldBorderSetSize = getDeclaredMethod(worldBorderClass, false, "setSize", double.class);

//    private final Constructor<?> packetPlayOutWorldBorderConstructor = packetPlayOutWorldBorderClass.getConstructor(worldBorderClass, packetPlayOutWorldBorderEnumWorldBorderActionClass);


    //dont use complete constructor because --> broken !!!! fucking mojang (can't get field world for get world )
    // redefined auu private field for work


    private final static Field worldBorderFieldI = getField(worldBorderClass, "i", true);

    private final static Field packetFieldAEnum = getField(packetPlayOutWorldBorderClass, "a", true);;
    private final static Field packetFieldCCenterX = getField(packetPlayOutWorldBorderClass, "c", true);
    private final static Field packetFieldDCenterZ = getField(packetPlayOutWorldBorderClass, "d", true);
    private final static Field packetFieldFSize = getField(packetPlayOutWorldBorderClass, "f", true);
    private final static Field packetFieldE  = getField(packetPlayOutWorldBorderClass, "e", true);
    private final static Field packetFieldG  = getField(packetPlayOutWorldBorderClass, "g", true);
    private final static Field packetFieldB  = getField(packetPlayOutWorldBorderClass, "b", true);
    private final static Field packetFieldIWarningDistance = getField(packetPlayOutWorldBorderClass, "i", true);
    private final static Field packetFieldHWarningTime = getField(packetPlayOutWorldBorderClass, "h", true);

    /**
     * get the packet WorldBorder from {@link BossBar}
     * @param action action
     * @param worldBorder worldBorder
     * @return Packet
     * @see WorldBorder
     */
    public static Object getWorldBorderPacket(WorldBorder.EnumWorldBorderAction action, WorldBorder worldBorder)
    {
        try {

            Object worldBorderObj = worldBorderConstructor.newInstance();
            worldBorderSetCenter.invoke(worldBorderObj, worldBorder.x(), worldBorder.z());
            worldBorderSetSize.invoke(worldBorderObj, worldBorder.r());
//            return packetPlayOutWorldBorderConstructor.newInstance(worldBorderObj, packetPlayOutWorldBorderEnumWorldBorderActionClass.getField(action.name()).get(null));
            Object object =  packetPlayOutWorldBorderConstructor.newInstance();
            packetFieldAEnum.set(object, packetPlayOutWorldBorderEnumWorldBorderActionClass.getField(action.name()).get(null));
            packetFieldCCenterX.set(object, worldBorder.x());
            packetFieldDCenterZ.set(object, worldBorder.z());
            packetFieldFSize.set(object, worldBorder.r());
//            Object worldBorderSubClass = worldBorderFieldI.get(worldBorderObj);
            packetFieldE.set(object,worldBorder.r() );
            packetFieldG.set(object, 0L);
            packetFieldB.set(object, 29999984);
            packetFieldIWarningDistance.set(object, worldBorder.getWaringBlock());
            packetFieldHWarningTime.set(object, worldBorder.getWaringTime());
            return object;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * send a world border to player
     * @param action action
     * @param worldBorder worldBorder to send
     * @param players all player to send
     * @see WorldBorder
     */
    public static void sendWorldBorderPlayer(WorldBorder.EnumWorldBorderAction action, WorldBorder worldBorder, Player... players)
    {
        Object packet = getWorldBorderPacket(action, worldBorder);
        for(Player p : players)
        {
            sendPacket(p, packet);
        }
    }


    private static final Class<?> bossBattleCl = getNMSClass("BossBattleServer");
    private static final Class<?> bossBattleCl0 = getNMSClass("BossBattle");
    private static final Class<?> action = getNMSClass("PacketPlayOutBoss").getDeclaredClasses()[0];
    private static final Class<?> color =  bossBattleCl0.getDeclaredClasses()[1];
    private static final Class<?> division =  bossBattleCl0.getDeclaredClasses()[0];

    private static final Constructor<?> packetPlayOutBossCo = getConstructor(getNMSClass("PacketPlayOutBoss"), false, action, bossBattleCl0);
    private static final Constructor<?> bossBattleCo = getConstructor(bossBattleCl, false, getNMSClass("IChatBaseComponent"), color, division);

    private static final Field bossUUIDField = getField(bossBattleCl0, "h", true);

    private static final Method setHeal = getDeclaredMethod(bossBattleCl0, false, "a", float.class);
    private static final Method setCF = getDeclaredMethod(bossBattleCl0, false, "a", boolean.class);
    private static final Method setDK = getDeclaredMethod(bossBattleCl0, false, "b", boolean.class);
    private static final Method setPBM = getDeclaredMethod(bossBattleCl0, false, "c", boolean.class);

    /**
     * get the packet bossabr from {@link BossBar}
     * @param action action
     * @param bossBar bossBar
     * @return Packet
     */
    public static Object getBossBarPacket(BossBar.Action action, BossBar bossBar)
    {
        try {

            Object bossBattleO = bossBattleCo.newInstance(getIChatBaseComponent(bossBar.getTitle()), color.getField(bossBar.getColor().name()).get(null), division.getField(bossBar.getDivision().name()).get(null));

            bossUUIDField.set(bossBattleO, bossBar.getUuid());
            setHeal.invoke(bossBattleO, bossBar.getHeal());

            setCF.invoke(bossBattleO,bossBar.isFlag(BossBar.Flags.CREATE_FOG));

            setDK.invoke(bossBattleO,bossBar.isFlag(BossBar.Flags.DARKEN_SKY));

            setPBM.invoke(bossBattleO,bossBar.isFlag(BossBar.Flags.PLAY_BOSS_MUSIC));

            return packetPlayOutBossCo.newInstance(Reflection.action.getField(action.name()).get(null), bossBattleO);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * send a world border to player
     * @param action action
     * @param bossBar bossBar to send
     * @param players all player to send
     * @see BossBar
     */
    public static void sendBossBarToPlayer(BossBar.Action action, BossBar bossBar, Player... players)
    {
        Object packet = getBossBarPacket(action, bossBar);
        for(Player p : players)
        {
            sendPacket(p, packet);
        }
    }


    private final static Class<?> nMSItemStack = getNMSClass("ItemStack");
    private final static Class<?> craftItemStack = getNMSBClass("inventory.CraftItemStack");
    private final static Class<?> nBTTagCompound = getNMSClass("NBTTagCompound");

    private final static Constructor<?> nBTTagCompoundCon = getConstructor(nBTTagCompound, false);

    private final static Method asNMSCopy = getDeclaredMethod(craftItemStack, false, "asNMSCopy", ItemStack.class);
    private final static Method asBukkitCopy = getDeclaredMethod(craftItemStack, false, "asBukkitCopy", nMSItemStack);
    private final static Method hasNbtTag = getDeclaredMethod(nMSItemStack, false, "hasTag");
    private final static Method getTag = getDeclaredMethod(nMSItemStack, false, "getTag");
    private final static Method setString = getDeclaredMethod(nBTTagCompound, false, "setString", String.class, String.class);
    private final static Method setTag = getDeclaredMethod(nMSItemStack, false, "setTag", nBTTagCompound);
    private final static Method hasKey = getDeclaredMethod(nBTTagCompound, false, "hasKey", String.class);
    private final static Method getString = getDeclaredMethod(nBTTagCompound, false, "getString", String.class);

    /**
     * add new NBTTag to item
     * @param itemStack ItemStack to add nbt will be not edit !
     * @param key key of the nbt
     * @param values value of the nbt
     * @return new ItemStack with nbtTag
     */
    public static ItemStack addNBTTag(final ItemStack itemStack, String key, String values)
    {
        try
        {
            Object it = asNMSCopy.invoke(null, itemStack);
            Object compound;
            if((boolean) hasNbtTag.invoke(it)) compound = getTag.invoke(it);
            else compound = nBTTagCompoundCon.newInstance();
            setString.invoke(compound, key, values);
            setTag.invoke(it, compound);
            return (ItemStack) asBukkitCopy.invoke(null, it);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * get the nbtTag of item from key
     * @param itemStack nbTag of item
     * @param key of the value
     * @return value or null if not found
     */
    public static String getNBTTag(ItemStack itemStack, String key)
    {
        try
        {
            Object it = asNMSCopy.invoke(null, itemStack);
            Object compound;
            if((boolean) hasNbtTag.invoke(it)) compound = getTag.invoke(it);
            else return null;
            if((boolean) hasKey.invoke(compound, key))
                return (String) getString.invoke(compound, key);
            else return null;

        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * check if 2 item have same NBTTag
     * @param itemStack1 it 1
     * @param itemStack2 it 2
     * @return true if same else false
     */
    public static boolean hasSameCompound(ItemStack itemStack1, ItemStack itemStack2)
    {
        try
        {
            Object it1 = asNMSCopy.invoke(null, itemStack1);
            Object compound1 = null;
            if((boolean) hasNbtTag.invoke(it1)) compound1 = getTag.invoke(it1);

            Object it2 = asNMSCopy.invoke(null, itemStack2);
            Object compound2 = null;
            if((boolean) hasNbtTag.invoke(it1)) compound2 = getTag.invoke(it2);

            return (compound1 == null && compound2 == null) || (compound1 != null && compound1.equals(compound2));
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Constructor getConstructor(Class<?> clazz, boolean setAccessible, Class... args)
    {
        try {
            Constructor c = clazz.getDeclaredConstructor(args);
            if(setAccessible) c.setAccessible(true);
            return c;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Method getDeclaredMethod(Class<?> clazz, boolean setAccessible, String name, Class... args)
    {
        try {
            Method m = clazz.getDeclaredMethod(name, args);
            if(setAccessible) m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Method getMethod(Class<?> clazz, boolean setAccessible, String name, Class... args)
    {
        try {
            Method m = clazz.getMethod(name, args);
            if(setAccessible) m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Field getField(Class<?> clazz, String name, boolean setAccessible)
    {
        try {
            Field f = clazz.getDeclaredField(name);
            if(setAccessible) f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
