package fr.idarkay.minetasia.core.api.utils;

/**
 * File <b>GuiLang</b> located on fr.idarkay.minetasia.core.api.utils
 * GuiLang is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 29/04/2020 at 15:30
 */
public class GuiLang
{
    private final String iso;
    private final String name;
    private final String lore;
    private final int slot;
    private final String texture;

    public GuiLang(String iso, String name, String lore, int slot, String texture)
    {
        this.iso = iso;
        this.name = name;
        this.lore = lore;
        this.slot = slot;
        this.texture = texture;
    }

    public int getSlot()
    {
        return slot;
    }

    public String getName()
    {
        return name;
    }

    public String getIso()
    {
        return iso;
    }

    public String getLore()
    {
        return lore;
    }

    public String getTexture()
    {
        return texture;
    }
}
