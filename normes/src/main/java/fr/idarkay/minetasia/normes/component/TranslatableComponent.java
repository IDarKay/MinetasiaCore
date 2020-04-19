package fr.idarkay.minetasia.normes.component;

import net.minecraft.server.v1_15_R1.ChatBaseComponent;
import net.minecraft.server.v1_15_R1.ChatMessage;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * File <b>TranslatableComponent</b> located on fr.idarkay.minetasia.normes.component
 * TranslatableComponent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 05/04/2020 at 04:07
 */
public class TranslatableComponent extends BaseComponent
{

    private final String src;
    private final List<BaseComponent> with = new ArrayList<>();
//    private final NBTTagList with = new NBTTagList();
//    private final List<IChatBaseComponent> iChatBaseComponents = new ArrayList<>();


    public TranslatableComponent(@NotNull ChatMessage chatBaseComponents)
    {
        super(chatBaseComponents);
        this.src = chatBaseComponents.getKey();
        for (Object arg : chatBaseComponents.getArgs())
        {
            if(arg instanceof IChatBaseComponent)
            {
                with.add(BaseComponent.fromIChatBaseComponent((IChatBaseComponent) arg));
            }
        }
    }

    /**
     * create Translatable component
     * @param src of the translate
     */
    public TranslatableComponent(@NotNull String src)
    {
        super();
        this.src = src;
    }

    /**
     * get translatableComponent with the name of item
     * @param material the material for th item
     * @return the component
     */
    public static TranslatableComponent getTranslatableComponentForItem(Material material)
    {
        return new TranslatableComponent((material.isBlock() ? "block." : "item.") + "minecraft."  + material.name().toLowerCase());
    }

    /**
     * add argument
     * @param component argument
     */
    public void with(BaseComponent... component)
    {
        with.addAll(Arrays.asList(component));
//        with.addAll(Arrays.stream(component).map(BaseComponent::toNbtTagCompound).collect(Collectors.toList()));
    }

    /**
     * add argument
     * @param component argument
     */
    public void with(String... component)
    {
        for (String s : component)
        {
            with.add(new TextComponent(s));
        }
//        with.addAll(Arrays.stream(component).map(co -> new TextComponent(co).toNbtTagCompound()).collect(Collectors.toList()));
    }

    @Override
    protected @NotNull NBTTagCompound getAddon()
    {
        final NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setString("translate", src);
        if(!with.isEmpty())
        {
            nbtTagCompound.set("with", with.stream().map(BaseComponent::toNbtTagCompound).collect(toNBTTagListCollector()));
        }
        return nbtTagCompound;
    }

    @Override
    protected @NotNull ChatBaseComponent getBaseChatComponent()
    {
        return new ChatMessage(src, (Object[]) with.stream().map(BaseComponent::toChatBaseComponent).toArray(Object[]::new));
    }


}
