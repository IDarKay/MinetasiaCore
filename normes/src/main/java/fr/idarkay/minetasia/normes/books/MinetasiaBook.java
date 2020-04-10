package fr.idarkay.minetasia.normes.books;

import fr.idarkay.minetasia.normes.MinetasiaPlugin;
import fr.idarkay.minetasia.normes.Reflection;
import net.minecraft.server.v1_15_R1.*;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>MinetasiaBook</b> located on fr.idarkay.minetasia.normes.books
 * MinetasiaBook is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 08/04/2020 at 14:02
 */
public class MinetasiaBook
{
    private static MinetasiaPlugin plugin;
    private ItemStack itemStack;

    public static void setPlugin(MinetasiaPlugin plugin)
    {
        MinetasiaBook.plugin = plugin;
    }


    public void withPages(@NotNull MinetasiaBookPages... pages)
    {
        Validate.notEmpty(pages);
        Validate.notNull(pages);

        this.itemStack = new ItemStack(CraftMagicNumbers.getItem(Material.WRITTEN_BOOK));

        final NBTTagCompound tag = new NBTTagCompound();

        final NBTTagList nbtPages = new NBTTagList();
        for (MinetasiaBookPages page : pages)
        {

                nbtPages.add(NBTTagString.a(IChatBaseComponent.ChatSerializer.a(page.toIChatBaseComponent())));

//            nbtPages.add(page.toNBT());
        }
        tag.set("pages", nbtPages);
        tag.setString("title", "_");
        tag.setString("author", "Server");
        itemStack.setTag(tag);

    }

    public void open(Player player)
    {
        Validate.isTrue(itemStack != null);

        PacketPlayOutSetSlot inventoryChange = new PacketPlayOutSetSlot(0, 45,  itemStack);
        PacketPlayOutOpenBook outOpenBook = new PacketPlayOutOpenBook(EnumHand.OFF_HAND);
        Reflection.sendPacket(player, inventoryChange);
        Reflection.sendPacket(player, outOpenBook);
        Bukkit.getScheduler().runTaskLater(plugin, player::updateInventory, 5L);
    }
}
