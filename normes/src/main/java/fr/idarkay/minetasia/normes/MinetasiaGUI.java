package fr.idarkay.minetasia.normes;

import com.google.common.collect.ForwardingMultimap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author alice B. (IDarKay),
 * <p>
 *     Copyright (c) 2019 Normes, Minetasia.
 * </p>
 * @since 1.0
 */
public class MinetasiaGUI {

    /**
     * like {@link MinetasiaGUI#createGUI(InventoryHolder, int, String, InventoryFileType, ItemStack)} but
     * automatic set a custom holder ({@link MinetasiaGuiHolder}) with id
     * @see MinetasiaGUI#createGUI(InventoryHolder, int, String, InventoryFileType, ItemStack)
     * @param id set into {@link MinetasiaGuiHolder} use in {@link org.bukkit.event.inventory.InventoryEvent}
     * @param numberOfRow need in [1;6]
     * @param name the title of the gui
     * @param type {@link InventoryFileType}
     * @param fileMaterial the item stack use for file
     * @return created inventory
     * @since 1.0
     */
    public static Inventory createGUI(@NotNull String id, int numberOfRow, @NotNull String name, @NotNull InventoryFileType type, @Nullable ItemStack fileMaterial)
    {
        return createGUI(new MinetasiaGuiHolder(id), numberOfRow, name, type, fileMaterial);
    }

    /**
     * create a gui
     * @param holder the {@link InventoryHolder}
     * @param numberOfRow need in [1;6]
     * @param name the title of the gui
     * @param type {@link InventoryFileType}
     * @param fileMaterial the item stack use for file
     * @return created inventory
     * @since 1.0
     */
    public static Inventory createGUI(@Nullable InventoryHolder holder, int numberOfRow, @NotNull String name, @NotNull InventoryFileType type, @Nullable ItemStack fileMaterial)
    {
        Inventory inventory = Bukkit.createInventory(holder, numberOfRow * 9, name);

        final int nbc = 9 * numberOfRow;
        switch (type)
        {
            case SQUARE:
                int[] rankss = new int[(numberOfRow - 2) * 2];
                int r = 0;
                for(int i = 1; i <= numberOfRow - 2; i++){
                    rankss[r] = i * 9;
                    r++;
                    rankss[r] =  9 * i +8;
                    r++;
                }
                addRecursiveItemInGui(fileMaterial, inventory, rankss);
            case LINES:
                addRecursiveItemInGui(fileMaterial, inventory, 0,1,2,3,4,5,6,7,8);
            case LINE_BOTTOM:
                int[] ranks = new int[9];
                for(int i = 1; i < 10; i++) ranks[i - 1] = nbc - i;
                addRecursiveItemInGui(fileMaterial, inventory, ranks);
                break;
            case FULL:
                for(int i = 0; i < nbc; i++) inventory.setItem(i, fileMaterial);
                break;
            case LINE_TOP:
                addRecursiveItemInGui(fileMaterial, inventory, 0,1,2,3,4,5,6,7,8);
                break;
            case EMPTY:
                break;
        }

        if(holder instanceof MinetasiaGuiHolder) ((MinetasiaGuiHolder) holder).setInventory(inventory);
        return inventory;
    }

    /**
     * create item stack with many arguments
     * @param material material of the item
     * @param amount of the item need ∈ [0 ; 64]
     * @param name  of the item
     * @param lore of the item
     * @return {@link ItemStack}
     * @since 1.0
     */
    public static ItemStack createItemStack(Material material, int amount, String name, String... lore){
        ItemStack back = new ItemStack(material, amount);
        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(material);
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        back.setItemMeta(meta);
        return back;
    }

    /**
     * create a head with many argument
     * @param amount of the head need ∈ [0 ; 64]
     * @param name  of the head
     * @param textures the texture
     * @param lore of the head
     * @return the head in {@link ItemStack}
     * @since 1.0
     */
    public static ItemStack createHead(int amount, String name, String textures,  String... lore)
    {
        ItemStack back = new ItemStack(Material.PLAYER_HEAD, amount);
        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        headFromTexturesRef(textures, meta);
        back.setItemMeta(meta);
        return back;
    }

    /**
     *  add a same item in gui a different place
     * @param itemStack the itemStack to add
     * @param inventory the inventory
     * @param rank all rank you wanna add the item
     */
    public static void addRecursiveItemInGui(ItemStack itemStack, Inventory inventory, int... rank ){
        for(int i : rank) inventory.setItem(i, itemStack);
    }

    /**
     * add lore to item stack
     * @param itemStack to add the lore
     * @param lore the lore
     * @return the ItemStack not the it the same of the given item stack
     * @since 1.0
     */
    public static ItemStack addLoreToItem(ItemStack itemStack, String... lore){
        ItemMeta itemMeta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemStack.getType());
        List<String> l = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
        l.addAll(Arrays.asList(lore));
        itemMeta.setLore(l);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * set lore to item stack
     * @param itemStack to add the lore
     * @param lore the lore
     * @return the ItemStack not the it the same of the given item stack
     * @since 1.0
     */
    public static ItemStack setLoreToItem(ItemStack itemStack, String... lore)
    {
        ItemMeta itemMeta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemStack.getType());
        itemMeta.setLore(Arrays.asList(lore));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     *
     * split a list to cut list of given size
     *
     * @param objects the list of object
     * @param page the number of the page to get
     * @param nbItemPerPage number of object pâr page
     * @param <T> class of the object
     * @return {@code list<T>} wanted
     * @since 1.0
     */
    public static <T> List<T> getPage(List<T> objects, int page, int nbItemPerPage)
    {
        List<T> back = new ArrayList<>();
        for(int i = page * nbItemPerPage ; i < nbItemPerPage * (page + 1) && i <= objects.size(); i++)
            if(objects.size() > i) back.add(objects.get(i));
        return back;
    }

    /**
     * add to ItemMeta a skull texture from texture
     * @param textures the texture
     * @param meta the meta where add the skull (will be update)
     * @return ItemMeta
     * @since 1.0
     */
    public static ItemMeta headFromTexturesRef(String textures, ItemMeta meta) {
        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);

            String[] data = textures.split("_");

            Constructor<?> gameprofileConstructor = Class.forName("com.mojang.authlib.GameProfile").getConstructor(UUID.class, String.class);
            Object gameprofileInstance = gameprofileConstructor.newInstance(UUID.randomUUID(), null);
            Object propertiesMap = gameprofileInstance.getClass().getDeclaredMethod("getProperties").invoke(gameprofileInstance);

            Constructor<?> propTexture = Class.forName("com.mojang.authlib.properties.Property").getConstructor(String.class, String.class);
            Constructor<?> propTextureSignature = Class.forName("com.mojang.authlib.properties.Property").getConstructor(String.class, String.class, String.class);

            Object property;
            if(data.length > 1) property = propTextureSignature.newInstance("textures", data[0], data[1]);
            else property = propTexture.newInstance("textures", textures);

            ForwardingMultimap.class.getDeclaredMethod("put", Object.class, Object.class).invoke(propertiesMap, "textures", property);

            profileField.set(meta, gameprofileInstance);
        }
        catch (Exception e) { e.printStackTrace(); }
        return meta;
    }

}
