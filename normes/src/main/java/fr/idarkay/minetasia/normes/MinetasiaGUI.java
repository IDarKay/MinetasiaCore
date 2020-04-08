package fr.idarkay.minetasia.normes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.idarkay.minetasia.normes.component.BaseComponent;
import fr.idarkay.minetasia.normes.component.TextComponent;
import fr.idarkay.minetasia.normes.utils.ReflectionVar;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.MojangsonParser;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;
import net.minecraft.server.v1_15_R1.NBTTagString;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author AloIs B. (IDarKay),
 * <p>
 *     Copyright (c) 2019 Normes, Minetasia.
 * </p>
 * @since 1.0
 */
public abstract class MinetasiaGUI<T extends JavaPlugin> {

    protected final T plugin;
    private final boolean isRealInventory;
    private final UUID id;
    private final GUIFlags[] flags;

    /**
     *
     * @param plugin plugin class
     * @param isRealInventory set false for custom gui true for other already exist inventory
     * @param guiFlags flags of teh inventory
     */
    public MinetasiaGUI(T plugin, boolean isRealInventory, GUIFlags... guiFlags)
    {
        this.plugin = plugin;
        this.isRealInventory = isRealInventory;
        this.id = UUID.randomUUID();
        this.flags = Objects.requireNonNull(guiFlags);
    }

    public boolean isRealInventory()
    {
        return isRealInventory;
    }

    public MinetasiaGuiHolder getHolder()
    {
        return new MinetasiaGuiHolder(id, this, flags);
    }

    public abstract void open(Player player);

    public void open(Player player, InventoryOpenEvent event)
    {}

    public void open(Player player, InventoryOpenEvent event, MinetasiaGuiHolder holder)
    {
        open(player, event);
    }

    public void close(Player player, InventoryCloseEvent event)
    {}

    public void close(Player player, InventoryCloseEvent event, MinetasiaGuiHolder holder)
    {
        close(player, event);
    }

    public void click(Player player, InventoryClickEvent event)
    {}

    public void click(Player player, InventoryClickEvent event, MinetasiaGuiHolder holder)
    {
        click(player, event);
    }

    public void drag(Player player, InventoryDragEvent event)
    {}

    public void drag(Player player, InventoryDragEvent event, MinetasiaGuiHolder holder)
    {
        drag(player, event);
    }

    public void onOtherEvent(Player player, InventoryEvent event)
    {}

    public void onOtherEvent(Player player, InventoryEvent event, MinetasiaGuiHolder holder)
    {
        onOtherEvent(player, event);
    }

    /**
     * like {@link MinetasiaGUI#createGUI(InventoryHolder, int, String, InventoryFileType, ItemStack)} but
     * automatic set a custom holder ({@link MinetasiaGuiHolder}) with id
     * @see MinetasiaGUI#createGUI(InventoryHolder, int, String, InventoryFileType, ItemStack)
     * @param numberOfRow need in [1;6]
     * @param name the title of the gui
     * @param type {@link InventoryFileType}
     * @param fileMaterial the item stack use for file
     * @return created inventory
     * @since 1.0
     */
    protected Inventory createGUI(int numberOfRow, @NotNull String name, @NotNull InventoryFileType type, @Nullable ItemStack fileMaterial)
    {
        return createGUI(getHolder(), numberOfRow, name, type, fileMaterial);
    }

    /**
     * like {@link MinetasiaGUI#createGUI(InventoryHolder, int, String, InventoryFileType, ItemStack)} but
     * automatic set a custom holder ({@link MinetasiaGuiHolder}) with id
     * @see MinetasiaGUI#createGUI(InventoryHolder, int, String, InventoryFileType, ItemStack)
     * @param id set into {@link MinetasiaGuiHolder} use in {@link org.bukkit.event.inventory.InventoryEvent}
     * @param minetasiaGUI linkedGui
     * @param numberOfRow need in [1;6]
     * @param name the title of the gui
     * @param type {@link InventoryFileType}
     * @param fileMaterial the item stack use for file
     * @return created inventory
     * @since 1.0
     */
    public static Inventory createGUI(@NotNull UUID id, MinetasiaGUI minetasiaGUI , int numberOfRow, @NotNull String name, @NotNull InventoryFileType type, @Nullable ItemStack fileMaterial)
    {
        return createGUI(new MinetasiaGuiHolder(id, minetasiaGUI), numberOfRow, name, type, fileMaterial);
    }


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
    public static Inventory createGUI(@NotNull UUID id, int numberOfRow, @NotNull String name, @NotNull InventoryFileType type, @Nullable ItemStack fileMaterial)
    {
        return createGUI(new MinetasiaGuiHolder(id, null), numberOfRow, name, type, fileMaterial);
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
    public static ItemStack createItemStack(Material material, int amount, String name, String... lore)
    {
        ItemStack back = new ItemStack(material, amount);
        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(material);
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        back.setItemMeta(meta);
        return back;
    }

    /**
     * create new custom item
     * @param material the tpe of the item
     * @param lore lore to add
     * @param amount teh amount
     * @param name name of the item
     * @return the item
     */
    public static ItemStack createItemStack(Material material, int amount, String name, List<String> lore)
    {
        ItemStack back = new ItemStack(material, amount);
        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(material);
        meta.setDisplayName(name);
        meta.setLore(lore);
        back.setItemMeta(meta);
        return back;
    }

    /**
     * create new custom item
     * @param material the tpe of the item
     * @param lore lore to add
     * @param amount teh amount
     * @param name name of the item
     * @param flags flags to add to the item
     * @return the item
     */
    public static ItemStack createItemStack(Material material, int amount, String name, @NotNull ItemFlag[] flags, String... lore)
    {
        ItemStack back = new ItemStack(material, amount);
        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(material);
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        meta.addItemFlags(flags);
        back.setItemMeta(meta);
        return back;
    }

    /**
     * create new custom item
     * @param material the tpe of the item
     * @param nbt the nbt of the item
     * @param amount teh amount
     * @return the item
     */
    public static ItemStack createItemStack(Material material, String nbt, int amount)
    {
        try
        {
            final net.minecraft.server.v1_15_R1.ItemStack itemStack = new net.minecraft.server.v1_15_R1.ItemStack(CraftMagicNumbers.getItem(material), amount);
            final NBTTagCompound nbtTagCompound = MojangsonParser.parse(nbt);
            itemStack.setTag(nbtTagCompound);
            return CraftItemStack.asBukkitCopy(itemStack);
        } catch (CommandSyntaxException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * create new custom item
     * @param material the tpe of the item
     * @param lore lore to add
     * @param amount teh amount
     * @param name name of the item with BaseComponent
     * @return the item
     */
    public static ItemStack createItemStack(Material material, List<String> lore,  int amount, BaseComponent... name)
    {
        final net.minecraft.server.v1_15_R1.ItemStack itemStack = new net.minecraft.server.v1_15_R1.ItemStack(CraftMagicNumbers.getItem(material), amount);
        final NBTTagCompound nbtTagCompound = new NBTTagCompound();

        final NBTTagCompound display = new NBTTagCompound();

        final JsonArray array = new JsonArray();
        for (BaseComponent component : name)
        {
            array.add(component.toJsonElement());
        }
        display.setString("Name", array.toString());



        final NBTTagList tagList = new NBTTagList();



        final List<NBTTagString> nbtLore = lore.stream().map(line -> NBTTagString.a(jsonArrayAsList(new TextComponent(line).toJsonElement()).toString())).collect(Collectors.toList());

        tagList.addAll(nbtLore);

        display.set("Lore", tagList);
        nbtTagCompound.set("display", display);
        itemStack.setTag(nbtTagCompound);
        return CraftItemStack.asBukkitCopy(itemStack);
    }

    /**
     * create new custom item
     * @param material the tpe of the item
     * @param nbt the nbt of the item
     * @param amount teh amount
     * @param name name of the item with BaseComponent
     * @return the item
     */
    public static ItemStack createItemStack(Material material,  String nbt, int amount, BaseComponent... name)
    {
        try
        {
            final net.minecraft.server.v1_15_R1.ItemStack itemStack = new net.minecraft.server.v1_15_R1.ItemStack(CraftMagicNumbers.getItem(material), amount);
            final NBTTagCompound nbtTagCompound = MojangsonParser.parse(nbt);
            itemStack.setTag(nbtTagCompound);
            final JsonArray array = new JsonArray();
            for (BaseComponent component : name)
            {
                array.add(component.toJsonElement());
            }

            itemStack.a(IChatBaseComponent.ChatSerializer.a(array));
            return CraftItemStack.asBukkitCopy(itemStack);
        } catch (CommandSyntaxException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * create new custom item
     * @param material the tpe of the item
     * @param nbt the nbt of the item
     * @param addLore lore to add
     * @param afterComponentNbtLore true for set the addlore after the nbt lore
     * @param amount teh amount
     * @param name name of the item with BaseComponent
     * @return the item
     */
    public static ItemStack createItemStack(Material material,  String nbt, List<String> addLore, boolean afterComponentNbtLore, int amount, BaseComponent... name)
    {
        try
        {
            final net.minecraft.server.v1_15_R1.ItemStack itemStack = new net.minecraft.server.v1_15_R1.ItemStack(CraftMagicNumbers.getItem(material), amount);
            final NBTTagCompound nbtTagCompound = MojangsonParser.parse(nbt);
            Validate.notNull(nbtTagCompound);

            final NBTTagCompound display;
            if (nbtTagCompound.hasKeyOfType("display", 10)) {
                display =  nbtTagCompound.getCompound("display");
            } else {
                display = new NBTTagCompound();
            }

            final JsonArray array = new JsonArray();
            for (BaseComponent component : name)
            {
                array.add(component.toJsonElement());
            }
            display.setString("Name", array.toString());



            NBTTagList tagList;

            if(display.hasKeyOfType("Lore", 9))
            {
                tagList = display.getList("Lore", 8);
            }
            else tagList = new NBTTagList();


            List<NBTTagString> nbtLore = addLore.stream().map(line -> NBTTagString.a(jsonArrayAsList(new TextComponent(line).toJsonElement()).toString())).collect(Collectors.toList());

            if(afterComponentNbtLore)
            {
                final NBTTagList loreNbt = new NBTTagList();
                loreNbt.addAll(nbtLore);
                loreNbt.addAll(tagList);
                tagList = loreNbt;
            }
            else
            {
                tagList.addAll(nbtLore);
            }

            display.set("Lore", tagList);
            nbtTagCompound.set("display", display);
            itemStack.setTag(nbtTagCompound);
            return CraftItemStack.asBukkitCopy(itemStack);
        } catch (CommandSyntaxException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static JsonArray jsonArrayAsList(JsonElement... jsonElement)
    {
        final JsonArray jsonArray = new JsonArray();
        for (JsonElement element : jsonElement)
        {
            jsonArray.add(element);
        }
        return jsonArray;
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
        return createHead(amount, name, textures, Arrays.asList(lore));
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
    public static ItemStack createHead(int amount, String name, String textures,  List<String> lore)
    {
        ItemStack back = new ItemStack(Material.PLAYER_HEAD, amount);
        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
        meta.setDisplayName(name);
        meta.setLore(lore);
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
            final Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, getGameProfile(textures));
        }
        catch (Exception e) { e.printStackTrace(); }
        return meta;
    }


    public static void headFromTexturesRefToBlock(String textures, Skull skull) {
        try
        {
            final Object world = ReflectionVar.GET_WORLD_HANDLE.invoke(skull.getWorld());
            final Object tileSkull = ReflectionVar.GET_WORLD_TILE_ENTITY.invoke(world, ReflectionVar.BLOCK_POSITION_CONSTRUCTOR.newInstance(skull.getX(), skull.getY(), skull.getZ()));
            ReflectionVar.SET_GAME_PROFILE.invoke(tileSkull, getGameProfile(textures));
        }
        catch (IllegalAccessException | InvocationTargetException | InstantiationException e)
        {
            e.printStackTrace();
        }
    }

    public static GameProfile getGameProfile(String textures)
    {
        final String[] data = textures.split("_");

        final GameProfile gameProfile = new GameProfile(UUID.randomUUID(),null);
        final PropertyMap propertyMap = gameProfile.getProperties();

        final Property property;
        if(data.length > 1) property = new Property("textures", data[0], data[1]);
        else property = new Property("textures", textures);
        propertyMap.put("textures", property);
        return gameProfile;
    }

}
