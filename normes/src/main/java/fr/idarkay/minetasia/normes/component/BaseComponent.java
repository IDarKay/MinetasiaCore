package fr.idarkay.minetasia.normes.component;

import fr.idarkay.minetasia.normes.component.event.ClickEventType;
import fr.idarkay.minetasia.normes.component.event.hover.HoverEvent;
import fr.idarkay.minetasia.normes.utils.GeneralUtils;
import net.minecraft.server.v1_15_R1.*;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;

/**
 * File <b>BaseComponent</b> located on fr.idarkay.minetasia.normes.component
 * BaseComponent is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 05/04/2020 at 03:55
 */
public abstract class BaseComponent
{



//    private boolean italic = false;
//    private boolean strikethrough = false;
//    private boolean bold = false;
//    private boolean underlined = false;
//    private boolean obfuscated = false;
    protected ChatColor chatColor = null;
    protected ClickEventType clickEventType = null;
    protected String clickEventValue = null;
    protected HoverEvent hoverEvent = null;
    protected BaseComponent[] extra = null;
    protected ChatModifier chatModifier;

    public static BaseComponent fromIChatBaseComponent(IChatBaseComponent iChatBaseComponent)
    {
        if(iChatBaseComponent instanceof ChatMessage)
        {
            return new TranslatableComponent((ChatMessage) iChatBaseComponent);
        }
        else if(iChatBaseComponent instanceof ChatComponentText)
        {
            return new TextComponent((ChatComponentText) iChatBaseComponent);
        }
        else if(iChatBaseComponent instanceof ChatComponentKeybind)
        {
            return new KeyBindComponent((ChatComponentKeybind) iChatBaseComponent);
        }
        else
        {
            throw new IllegalArgumentException(iChatBaseComponent.getClass().getName() + " not register");
        }
    }

    public static BaseComponent fromIChatBaseComponent(String stringValue)
    {
        if(stringValue.startsWith(MinetasiaTranslatableComponent.identifier))
        {
            String[] split = stringValue.split(";", 2);
            Validate.isTrue(split.length == 2, "invalidate string value " + stringValue);
            return new MinetasiaTranslatableComponent(split[1]);
        }
        else
            return fromIChatBaseComponent(IChatBaseComponent.ChatSerializer.a(stringValue));
    }

    protected BaseComponent(BaseComponent clone)
    {
//        this.msg = msg;
        copyDisplay(clone);
    }

    protected void copyDisplay(BaseComponent clone)
    {
        this.chatModifier = clone.chatModifier;
        this.extra = clone.extra;
        this.clickEventValue = clone.clickEventValue;
        this.clickEventType = clone.clickEventType;
        this.chatColor = clone.chatColor;
        this.hoverEvent = clone.hoverEvent;
    }

    public BaseComponent()
    {
//        this.msg = msg;
        chatModifier = new ChatModifier();;
        chatModifier.setItalic(false);
    }

    protected BaseComponent(IChatBaseComponent iChatBaseComponent)
    {
        this.chatModifier = iChatBaseComponent.getChatModifier();
        if(chatModifier.getColor() != null)
            this.chatColor = ChatColor.getByChar(chatModifier.getColor().character);
        if(chatModifier.getClickEvent() != null)
        {
            clickEventType = ClickEventType.fromNms(chatModifier.getClickEvent().a());
            clickEventValue = chatModifier.getClickEvent().b();
        }
        if(chatModifier.getHoverEvent() != null)
        {
//            chatModifier.getHoverEvent();
            //todo: hover
        }
        if(iChatBaseComponent.getSiblings() != null)
            extra = iChatBaseComponent.getSiblings().stream().map(iChatBaseComponent1 -> fromIChatBaseComponent(iChatBaseComponent)).toArray(BaseComponent[]::new);

    }

    public BaseComponent setBold(boolean bold)
    {
//        this.bold = bold;
        chatModifier.setBold(true);
        return this;
    }

    public BaseComponent setItalic(boolean italic)
    {
//        this.italic = italic;
        chatModifier.setItalic(true);
        return this;
    }

    public BaseComponent setStrikethrough(boolean strikethrough)
    {
//        this.strikethrough = strikethrough;
        chatModifier.setStrikethrough(true);
        return this;
    }

    public BaseComponent setUnderlined(boolean underlined)
    {
//        this.underlined = underlined;
        chatModifier.setUnderline(true);
        return this;
    }

    public BaseComponent setObfuscated(boolean obfuscated)
    {
//        this.obfuscated = obfuscated;
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

    public BaseComponent addExtra(BaseComponent... extra)
    {
        Validate.noNullElements(extra);
        Validate.notEmpty(extra);
        if(this.extra == null) this.extra = extra;
        else this.extra = GeneralUtils.concat(this.extra, extra);
        return this;
    }

    public boolean isBold()
    {
        return chatModifier.isBold();
    }

    public boolean isItalic()
    {
        return chatModifier.isItalic();
    }

    public boolean isStrikethrough()
    {
        return chatModifier.isStrikethrough();
    }

    public boolean isUnderlined()
    {
        return chatModifier.isUnderlined();
    }

    public boolean isObfuscated()
    {
        return chatModifier.isRandom();
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

        for (EnumChatFormat value : EnumChatFormat.values())
        {
            if(value.character == chatColor.getChar()) chatModifier.setColor(value);
        }
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
        NBTTagCompound nbtTagCompound = new NBTTagCompound();

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
        return toChatBaseComponent(null, null);
    }

    /**
     * for skyblock and MinetasiaTranslatableComponent
     * @param lang lang translate
     * @return the chat component
     */
    public @NotNull <R> IChatBaseComponent toChatBaseComponent(String lang, R argsObject)
    {
        final IChatBaseComponent iChatBaseComponent = getBaseChatComponent().setChatModifier(chatModifier);
        if(extra != null)
            for (BaseComponent baseComponent : extra)
            {
                iChatBaseComponent.addSibling(baseComponent.toChatBaseComponent(lang, argsObject));
            }
        return iChatBaseComponent;
    }

    /**
     * for skyblock and MinetasiaTranslatableComponent
     * @param lang lang translate
     * @return the chat component
     * not apply on this
     */
    public @NotNull <R> IChatBaseComponent[] toChatBaseComponentWithSplit(String lang, R argsObject, int maxChar)
    {
        int current = 0;
        final List<IChatBaseComponent> back = new ArrayList<>();
//        final IChatBaseComponent iChatBaseComponent = ;
        back.add(getBaseChatComponent().setChatModifier(chatModifier));
        if(extra != null)
            for (BaseComponent baseComponent : extra)
            {
                if(baseComponent instanceof MinetasiaTranslatableComponent)
                {
                    IChatBaseComponent[] c = baseComponent.toChatBaseComponentWithSplit(lang, argsObject, maxChar);
                    if(c.length == 0)
                    {
                        continue;
                    }
                    else if(c.length == 1)
                    {
                        back.get(current).addSibling(c[0]);
                    }
                    else
                    {
                        back.get(current).addSibling(c[0]);
                        for (int i = 1; i < c.length; i++)
                        {
                            current = i;
                            back.add(c[i]);
                        }
                    }
                }
                else
                    back.get(current).addSibling(baseComponent.toChatBaseComponent(lang, argsObject));
            }
        return back.toArray(new IChatBaseComponent[0]);
    }

    public static Collector<NBTBase, NBTTagList, NBTTagList> toNBTTagListCollector()
    {
        return Collector.of(NBTTagList::new, NBTTagList::add,  (left, right) ->  { left.addAll(right); return left;});
    }

//    public static Collector<NBTBase, NBTTagList, NBTTagList> toNBTTagListCollector()
//    {
//        return Collector.of(NBTTagList::new, NBTTagList::add,  (left, right) ->  { left.addAll(right); return left;});
//    }

}
