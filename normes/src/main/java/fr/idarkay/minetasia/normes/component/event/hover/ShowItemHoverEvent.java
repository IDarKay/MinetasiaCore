package fr.idarkay.minetasia.normes.component.event.hover;

import net.minecraft.server.v1_15_R1.ChatHoverable;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>ShowItemHoverEvent</b> located on fr.idarkay.minetasia.normes.component.event.hover
 * ShowItemHoverEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 08/04/2020 at 16:32
 */
public class ShowItemHoverEvent implements HoverEvent
{

    @NotNull
//    private final IChatBaseComponent value;

    public ShowItemHoverEvent(ItemStack itemStack)
    {
        this(CraftItemStack.asNMSCopy(itemStack));
    }

    public ShowItemHoverEvent(@NotNull net.minecraft.server.v1_15_R1.ItemStack itemStack)
    {
//       IChatBaseComponent.ChatSerializer.a(itemStack.save(new NBTTagCompound()));
//        this.value = ;
    }

    @NotNull
    @Override
    public String getKey()
    {
        return "show_item";
    }

    @NotNull
    @Override
    public IChatBaseComponent getValue()
    {
        return null;
    }

    @Override
    public @NotNull ChatHoverable.EnumHoverAction getChatHoverable()
    {
        return ChatHoverable.EnumHoverAction.SHOW_ITEM;
    }

}
