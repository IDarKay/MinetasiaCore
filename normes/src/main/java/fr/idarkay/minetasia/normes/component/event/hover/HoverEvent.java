package fr.idarkay.minetasia.normes.component.event.hover;

import net.minecraft.server.v1_15_R1.ChatHoverable;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>HoverEvent</b> located on fr.idarkay.minetasia.normes.component.event.hover
 * HoverEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 08/04/2020 at 16:16
 */
public interface HoverEvent
{
    @NotNull
    String getKey();

    @NotNull
    IChatBaseComponent getValue();

    @NotNull ChatHoverable.EnumHoverAction getChatHoverable();

    @NotNull
    default NBTTagCompound toNBTTagCompound()
    {
        final NBTTagCompound nbtTagCompound = new NBTTagCompound();
//        nbtTagCompound.setString("action", getKey());
//        nbtTagCompound.setString("value", getValue());
        return nbtTagCompound;
    }

    default ChatHoverable toChatHoverable()
    {
        return new ChatHoverable(getChatHoverable(), getValue());
    }


}
