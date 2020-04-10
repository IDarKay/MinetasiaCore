package fr.idarkay.minetasia.normes.component;

import fr.idarkay.minetasia.normes.component.event.ClickEventType;
import fr.idarkay.minetasia.normes.component.event.hover.HoverEvent;
import net.minecraft.server.v1_15_R1.*;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * File <b>BaseComponent</b> located on fr.idarkay.minetasia.normes.component
 * BaseComponent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 05/04/2020 at 03:55
 */
public abstract class BaseComponent
{
    private boolean italic = false;
    private boolean strikethrough = false;
    private boolean bold = false;
    private boolean underlined = false;
    private boolean obfuscated = false;
    private ChatColor chatColor = null;
    private ClickEventType clickEventType = null;
    private String clickEventValue = null;
    private HoverEvent hoverEvent = null;
    private BaseComponent[] extra = null;
    private ChatModifier chatModifier = new ChatModifier();

    public BaseComponent()
    {
//        this.msg = msg;
    }

    public BaseComponent setBold(boolean bold)
    {
        this.bold = bold;
        chatModifier.setBold(true);
        return this;
    }

    public BaseComponent setItalic(boolean italic)
    {
        this.italic = italic;
        chatModifier.setItalic(true);
        return this;
    }

    public BaseComponent setStrikethrough(boolean strikethrough)
    {
        this.strikethrough = strikethrough;
        chatModifier.setStrikethrough(true);
        return this;
    }

    public BaseComponent setUnderlined(boolean underlined)
    {
        this.underlined = underlined;
        chatModifier.setUnderline(true);
        return this;
    }

    public BaseComponent setObfuscated(boolean obfuscated)
    {
        this.obfuscated = obfuscated;
        chatModifier.setRandom(true);
        return this;
    }

    public BaseComponent setClickEvent(ClickEventType clickEventType, String value)
    {
        this.clickEventType = clickEventType;
        this.clickEventValue = value;
        chatModifier.setChatClickable(new ChatClickable(clickEventType.getNms(), value));
//        chatModifier.setChatHoverable(new ChatHoverable())
        return this;
    }

    public BaseComponent setHoverEvent(HoverEvent hoverEvent)
    {
        this.hoverEvent = hoverEvent;
        chatModifier.setChatHoverable(hoverEvent.toChatHoverable());
        return this;
    }

    public BaseComponent withExtra(@Nullable BaseComponent... extra)
    {
        if(extra == null)
        {
            this.extra = null;
            return this;
        }
        Validate.noNullElements(extra);
        Validate.notEmpty(extra);
        this.extra = extra;
        return this;
    }

    public boolean isBold()
    {
        return bold;
    }

    public boolean isItalic()
    {
        return italic;
    }

    public boolean isStrikethrough()
    {
        return strikethrough;
    }

    public boolean isUnderlined()
    {
        return underlined;
    }

    public boolean isObfuscated()
    {
        return obfuscated;
    }

    public ClickEventType getClickEventType()
    {
        return clickEventType;
    }

    public String getClickEventValue()
    {
        return clickEventValue;
    }

    public HoverEvent getHoverEvent()
    {
        return hoverEvent;
    }

    /**
     * set color of the message
     * @param chatColor the color not format
     * @return self
     */
    public BaseComponent setChatColor(ChatColor chatColor)
    {
        if(chatColor.isFormat()) throw new IllegalArgumentException("chatcolor can only be a color");
        this.chatColor = chatColor;
        chatModifier.setColor(EnumChatFormat.valueOf(chatColor.name()));
        return this;
    }


    public ChatColor getChatColor()
    {
        return chatColor;
    }

    @NotNull
    protected abstract NBTTagCompound getAddon();

    public NBTTagCompound toNbtTagCompound()
    {
        final NBTTagCompound nbtTagCompound = new NBTTagCompound();

        nbtTagCompound.a(getAddon()); //merge
//        getAddon().forEach(nbtTagCompound::setString);

        if(isBold()) nbtTagCompound.setBoolean("bold", true);

        // italic is default
        if(!isItalic())  nbtTagCompound.setBoolean("italic", false);

        if(isStrikethrough()) nbtTagCompound.setBoolean("strikethrough", true);

        if(isUnderlined()) nbtTagCompound.setBoolean("underlined", true);

        if(isObfuscated()) nbtTagCompound.setBoolean("obfuscated", true);

        if(chatColor != null) nbtTagCompound.setString("color", chatColor.name().toLowerCase());

        if(clickEventType != null)
        {
            final NBTTagCompound click = new NBTTagCompound();
            click.setString("action", clickEventType.getKey());
            click.setString("value", clickEventValue);
            nbtTagCompound.set("clickEvent", click);
        }

        if(hoverEvent != null) nbtTagCompound.set("hoverEvent", hoverEvent.toNBTTagCompound());

        if(extra != null)
        {
            final NBTTagList list = new NBTTagList();
            for (BaseComponent baseComponent : extra)
            {
                list.add(baseComponent.toNbtTagCompound());
            }
            nbtTagCompound.set("extra", list);
        }

        return nbtTagCompound;
    }

    protected abstract @NotNull ChatBaseComponent getBaseChatComponent();

    @NotNull
    public IChatBaseComponent toChatBaseComponent()
    {
        final IChatBaseComponent iChatBaseComponent = getBaseChatComponent().setChatModifier(chatModifier);
        if(extra != null)
            for (BaseComponent baseComponent : extra)
            {
                iChatBaseComponent.addSibling(baseComponent.toChatBaseComponent());
            }
        return iChatBaseComponent;
    }

}
