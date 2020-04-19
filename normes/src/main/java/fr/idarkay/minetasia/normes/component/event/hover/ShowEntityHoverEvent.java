package fr.idarkay.minetasia.normes.component.event.hover;

import net.minecraft.server.v1_15_R1.ChatHoverable;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * File <b>ShowEntityHoverEvent</b> located on fr.idarkay.minetasia.normes.component.event.hover
 * ShowEntityHoverEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 08/04/2020 at 16:41
 */
public class ShowEntityHoverEvent implements HoverEvent
{

    private final String value;

    public ShowEntityHoverEvent(@NotNull Entity entity)
    {
        this(entity.getUniqueId(), entity.getType(), entity.getName());
    }

    public ShowEntityHoverEvent(@NotNull UUID uuid, @NotNull EntityType entityType, @NotNull String name)
    {
        final NBTTagCompound compound = new NBTTagCompound();
        compound.setString("id", uuid.toString());
        compound.setString("type", "minecraft:" + entityType.name().toLowerCase());
        compound.setString("name", name);
        this.value = compound.toString();
    }

    public ShowEntityHoverEvent(NBTTagCompound value)
    {
        this.value = value.toString();
    }


    @Override
    public @NotNull String getKey()
    {
        return "show_entity";
    }

    @Override
    public @NotNull IChatBaseComponent getValue()
    {
        return null;
    }

    @Override
    public @NotNull ChatHoverable.EnumHoverAction getChatHoverable()
    {
        return ChatHoverable.EnumHoverAction.SHOW_ENTITY;
    }
}
