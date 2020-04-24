package fr.idarkay.minetasia.normes.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.idarkay.minetasia.normes.Reflection;
import fr.idarkay.minetasia.normes.component.BaseComponent;
import net.minecraft.server.v1_15_R1.*;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * File <b>BukkitUtils</b> located on fr.idarkay.minetasia.hub.utils
 * BukkitUtils is a part of minetasiahub.
 * <p>
 * Copyright (c) 2020 minetasiahub.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 30/01/2020 at 19:40
 */
public class BukkitUtils
{

    public static Location locationFromString(@NotNull World world, @NotNull String location)
    {
        Validate.notNull(world);
        Validate.notNull(location);
        String[] split = location.split(";");
        if(split.length == 3)
        {
            return new Location(world, Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        }
        else if(split.length == 5)
        {
            return new Location(world, Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Float.parseFloat(split[3]), Float.parseFloat(split[4]));
        }
        else throw new IllegalArgumentException();
    }

    public static boolean isTopGui(InventoryClickEvent e)
    {
        return e.getView().getTopInventory().equals(e.getClickedInventory());
    }

    public static int getSpaceInInventory(Inventory inventory, ItemStack itemStack){

        ItemStack copy = new ItemStack(itemStack);
        copy.setAmount(1);
        int max = copy.getMaxStackSize();

        int back = 0;
        for(ItemStack i : inventory.getStorageContents())
        {
            //noinspection ConstantConditions
            if(i == null || i.getType().equals(Material.AIR)) back += max;
            else if(canBeStack(copy, i)) back += max - i.getAmount();
        }
        return back;
    }

    public static int getItemRecurrence(Inventory inventory, ItemStack itemStack)
    {
        int back = 0;
        for(ItemStack i : inventory.getStorageContents())
        {
            //noinspection ConstantConditions
            if(i != null && !i.getType().isAir() && i.isSimilar(itemStack))
            {
                back += i.getAmount();
            }
        }
        return back;
    }

    public static boolean canBeStack(ItemStack it1, ItemStack it2){

        if(it1.getType() == it2.getType())
            if(it1.getAmount() + it2.getAmount()  <= it1.getMaxStackSize())
                return it1.isSimilar(it2);
        return false;
    }

    public static void setDisplayNameToItem(ItemStack item, String name)
    {
        if(item == null) return;
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
    }

    public static ItemStack addEnchantmentEffect(ItemStack itemStack)
    {
        ItemMeta im = Objects.requireNonNull(itemStack.getItemMeta());
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        itemStack.setItemMeta(im);
        return itemStack;
    }

    /**
     *
     * return the texture of the head of the player mit signature if is present
     *
     * @param player that get head
     * @return texture + _ + signature
     */
    @NotNull
    public static String getTextureFromPlayer(@NotNull Player player)
    {
        try
        {
            final Object entityPlayer = ReflectionVar.CRAFT_PLAYER_GET_HANDLE.invoke(player);
           final GameProfile gameProfile = (GameProfile) ReflectionVar.ENTITY_PLAYER_GET_PROFILE.invoke(entityPlayer);
           final Property property = gameProfile.getProperties().get("textures").iterator().next();
           final String texture = property.getValue();
           final String signature = property.getSignature();

           if(signature == null || signature.isEmpty())
           {
               return texture;
           }

           return texture + "_" + signature;
        }
            catch (IllegalAccessException | InvocationTargetException  e)
        {
            e.printStackTrace();
        }
        throw new NullPointerException("no texture found");
    }

    /**
     * get a NamespacedKey from string example (minecraft:diamond)
     * @param string string NamespacedKey
     * @return the string
     */
    public static NamespacedKey namespaceKeyFromSting(String string)
    {
        final String[] split = string.split(":");
        Validate.isTrue(split.length == 2, "string have invalid format");
        //noinspection deprecation ignore for use all plugin
        return new NamespacedKey(split[0], split[1]);
    }

    /**
     * add new NBTTag to item
     * @param itemStack ItemStack to add nbt will be not edit !
     * @param key key of the nbt
     * @param values value of the nbt
     * @return new ItemStack with nbtTag
     */
    @NotNull
    public static ItemStack addNBTTag(final ItemStack itemStack, String key, String values)
    {
        final net.minecraft.server.v1_15_R1.ItemStack it = CraftItemStack.asNMSCopy(itemStack);
        final NBTTagCompound nbtTagCompound = it.hasTag() ? it.getTag() : new NBTTagCompound();
        nbtTagCompound.setString(key, values);
        it.setTag(nbtTagCompound);
        return CraftItemStack.asBukkitCopy(it);
    }

    /**
     * remove a string nbt from the item
     * @param itemStack the item
     * @param key the key
     * @return new item
     */
    @NotNull
    public static ItemStack removeNBTTag(final ItemStack itemStack, String key)
    {

        final net.minecraft.server.v1_15_R1.ItemStack it = CraftItemStack.asNMSCopy(itemStack);
        if(it.hasTag()) {
            NBTTagCompound nbtTagCompound = it.getTag();
            nbtTagCompound.remove(key);
            it.setTag(nbtTagCompound);
            return CraftItemStack.asBukkitCopy(it);
         }
        else return itemStack;
    }


    /**
     * get the nbtTag of item from key
     * @param itemStack nbTag of item
     * @param key of the value
     * @param clazz the clazz of nbt
     * @param <T> type of the nbt
     * @return value or null if not found
     */
    @Nullable
    public static <T> T getNBTTag(ItemStack itemStack, String key, Class<T> clazz)
    {

        final net.minecraft.server.v1_15_R1.ItemStack it = CraftItemStack.asNMSCopy(itemStack);
        if(it.hasTag())
        {
            return clazz.cast(it.getTag().get(key));
        }
        else return null;

    }

    /**
     * set all nbt from json in item
     * @param itemStack the item stack (will't be edit)
     * @param nbtJson json
     * @return the itemStack
     * throws CommandSyntaxException if invalid json
     */
    @NotNull
    public static ItemStack setNbt(@NotNull ItemStack itemStack, @NotNull String nbtJson)
    {
        final net.minecraft.server.v1_15_R1.ItemStack it = CraftItemStack.asNMSCopy(itemStack);
        try
        {
            final NBTTagCompound parse = MojangsonParser.parse(nbtJson);
            it.setTag(parse);
            return CraftItemStack.asBukkitCopy(it);
        } catch (CommandSyntaxException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * set name with base component for translation
     * @param itemStack the item will not edit
     * @param components all components in the good order
     * @return the new ItemStack
     */
    @NotNull
    public static ItemStack setComponentDisplayName(@NotNull ItemStack itemStack, BaseComponent... components)
    {
        final net.minecraft.server.v1_15_R1.ItemStack it = CraftItemStack.asNMSCopy(itemStack);
        final NBTTagList array = new NBTTagList();
        for (BaseComponent component : components)
        {
            array.add(component.toNbtTagCompound());
        }
        it.a(array.l());
        return CraftItemStack.asBukkitCopy(it);
    }

    public static void sendToPlayer(Player player, BaseComponent... components)
    {
        if(components.length != 1)
        {
            for (BaseComponent component : components)
            {
                PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(component.toChatBaseComponent(), ChatMessageType.SYSTEM);
                Reflection.sendPacket(player, packetPlayOutChat);
            }

        }
        else
        {
            PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(components[0].toChatBaseComponent(), ChatMessageType.SYSTEM);
            Reflection.sendPacket(player, packetPlayOutChat);
        }
    }

    public static void sendToPlayer(Iterable<Player> players, BaseComponent... components)
    {
        NBTTagList nbtTagList = new NBTTagList();
        for (BaseComponent component : components)
        {
            nbtTagList.add(component.toNbtTagCompound());
        }
        PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(nbtTagList.l(), ChatMessageType.SYSTEM);
        for (Player player : players)
        {
            Reflection.sendPacket(player, packetPlayOutChat);
        }
    }

    @Nullable
    public static String  getRawNBTFromItem(@NotNull ItemStack itemStack)
    {
        Validate.notNull(itemStack);
        final NBTTagCompound tag = CraftItemStack.asNMSCopy(itemStack).getTag();
        return tag == null ? null : tag.toString();
    }

    @NotNull
    public static String locationToString(@NotNull Location location)
    {
        return location.getX() + ";" + location.getY() + ";" + location.getZ() + ";" + location.getYaw() + ";" + location.getPitch();
    }

    @NotNull
    public static Location locationFromString(@NotNull String location)
    {
        String[] split = location.split(";", 5);
        if(split.length == 3)
        {
            return new Location(null, Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]));
        }
        else if(split.length == 5)
        {
            return new Location(null, Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Float.parseFloat(split[3]), Float.parseFloat(split[4]));
        }
        else throw new IllegalArgumentException("invalid location format");
    }

}
