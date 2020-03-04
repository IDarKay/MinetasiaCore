package fr.idarkay.minetasia.normes.Utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.idarkay.minetasia.normes.Reflection;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * File <b>BukkitUtils</b> located on fr.idarkay.minetasia.hub.Utils
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

    public static boolean canBeStack(ItemStack it1, ItemStack it2){

        if(it1.getType() == it2.getType())
            if(it1.getAmount() + it2.getAmount()  <= it1.getMaxStackSize())
                return Reflection.hasSameCompound(it1, it2);
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

    public final static Class<?> CRAFT_PLAYER_CLASS = Reflection.getCraftBukkitClass("entity.CraftPlayer");
    public final static Method CRAFT_PLAYER_GET_HANDLE = Reflection.getDeclaredMethod(Objects.requireNonNull(CRAFT_PLAYER_CLASS), true, "getHandle");
    public final static Class<?> ENTITY_PLAYER_CLASS = Objects.requireNonNull(Reflection.getNMSClass("EntityHuman"));
    public final static Method ENTITY_PLAYER_GET_PROFILE = Reflection.getDeclaredMethod(ENTITY_PLAYER_CLASS, true,"getProfile");
//    private final static Field ENTITY_PLAYER_BT = Reflection.getField(ENTITY_PLAYER_CLASS, )

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
            final Object entityPlayer = CRAFT_PLAYER_GET_HANDLE.invoke(player);
           final GameProfile gameProfile = (GameProfile) ENTITY_PLAYER_GET_PROFILE.invoke(entityPlayer);
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

}
