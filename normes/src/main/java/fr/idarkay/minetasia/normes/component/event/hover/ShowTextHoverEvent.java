package fr.idarkay.minetasia.normes.component.event.hover;

import fr.idarkay.minetasia.normes.component.BaseComponent;
import net.minecraft.server.v1_15_R1.ChatComponentText;
import net.minecraft.server.v1_15_R1.ChatHoverable;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>ShowTextHoverEvent</b> located on fr.idarkay.minetasia.normes.component.event.hover
 * ShowTextHoverEvent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 08/04/2020 at 16:29
 */
public class ShowTextHoverEvent implements HoverEvent
{
    @NotNull
    private final IChatBaseComponent text;

    public ShowTextHoverEvent(@NotNull BaseComponent text)
    {
        Validate.notNull(text);
        this.text = text.toChatBaseComponent();
    }

    public ShowTextHoverEvent(@NotNull String text)
    {
        Validate.notNull(text);
        this.text = new ChatComponentText(text);
    }

    @NotNull
    @Override
    public String getKey()
    {
        return "show_text";
    }

    @NotNull
    @Override
    public IChatBaseComponent getValue()
    {
        return text;
    }

    @Override
    public @NotNull ChatHoverable.EnumHoverAction getChatHoverable()
    {
        return ChatHoverable.EnumHoverAction.SHOW_TEXT;
    }

}
