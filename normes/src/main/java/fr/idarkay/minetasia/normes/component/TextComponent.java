package fr.idarkay.minetasia.normes.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

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

    private final String msg;
    private final List<BaseComponent> compounds = new ArrayList<>();

    private boolean italic = false;
    private boolean strikethrough = false;
    private boolean bold = false;
    private boolean underlined = false;
    private ChatColor chatColor = null;

    public TextComponent(String msg)
    {
        this.msg = msg;
    }

    public TextComponent setBold(boolean bold)
    {
        this.bold = bold;
        return this;
    }

    public TextComponent setItalic(boolean italic)
    {
        this.italic = italic;
        return this;
    }

    public TextComponent setStrikethrough(boolean strikethrough)
    {
        this.strikethrough = strikethrough;
        return this;
    }

    public TextComponent setUnderlined(boolean underlined)
    {
        this.underlined = underlined;
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

    /**
     * set color of the message
     * @param chatColor the color not format
     * @return self
     */
    public TextComponent setChatColor(ChatColor chatColor)
    {
        if(chatColor.isFormat()) throw new IllegalArgumentException("chatcolor can only be a color");
        this.chatColor = chatColor;
        return this;
    }

    public ChatColor getChatColor()
    {
        return chatColor;
    }

    @Override
    public JsonElement toJsonElement()
    {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", msg);
        if(isBold())
            jsonObject.addProperty("bold", true);
        if(!isItalic()) // italic is default
            jsonObject.addProperty("italic", false);
        if(isStrikethrough())
            jsonObject.addProperty("strikethrough", true);
        if(isUnderlined())
            jsonObject.addProperty("underlined", true);
        if(chatColor != null)
            jsonObject.addProperty("color", chatColor.name().toLowerCase());
        return jsonObject;
    }
}
