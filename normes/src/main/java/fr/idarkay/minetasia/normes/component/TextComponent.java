package fr.idarkay.minetasia.normes.component;

import net.minecraft.server.v1_15_R1.ChatBaseComponent;
import net.minecraft.server.v1_15_R1.ChatComponentText;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>TextBaseComponent</b> located on fr.idarkay.minetasia.normes.component
 * TextBaseComponent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 05/04/2020 at 04:01
 */
public class TextComponent extends BaseComponent
{

    private final String text;

    public TextComponent(@NotNull String text)
    {
        super();
        this.text = text;
    }

    protected TextComponent(String text, BaseComponent clone)
    {
        super(clone);
        this.text = text;
   }

    public TextComponent(@NotNull ChatComponentText chatBaseComponents)
    {
        super(chatBaseComponents);
        this.text = chatBaseComponents.getText();
    }

    public String getText()
    {
        return text;
    }

    @Override
    protected @NotNull NBTTagCompound getAddon()
    {
        final NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setString("text", text);
        return nbtTagCompound;
    }

    @Override
    protected @NotNull ChatBaseComponent getBaseChatComponent()
    {
        return new ChatComponentText(text);
    }

}
