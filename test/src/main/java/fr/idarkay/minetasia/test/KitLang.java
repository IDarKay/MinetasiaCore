package fr.idarkay.minetasia.test;

import fr.idarkay.minetasia.core.api.KitType;
import fr.idarkay.minetasia.core.api.utils.Kit;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * File <b>KitLang</b> located on fr.idarkay.minetasia.core.spigot.kits
 * KitLang is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 11/02/2020 at 12:28
 */
public class KitLang implements Kit
{

    private final KitMain kitMain;
    private final String lang;
    private final String displayName;
    private final String displayNameWithoutColor;
    private final String[] desc;
    private final String[] descWithoutColor;
    private final String[] lvlDesc;
    private final String[] lvlDescWithoutColor;

    public KitLang(KitMain kitMain, String lang, Document d)
    {
        this.kitMain = kitMain;
        this.lang = lang;
        this.displayNameWithoutColor = d.getString("display_name");
        this.displayName = ChatColor.translateAlternateColorCodes('&', displayNameWithoutColor);

        final List<String> desc = d.getList("description", String.class);
        this.descWithoutColor = desc.toArray(new String[0]);
        this.desc = desc.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).toArray(String[]::new);

        final List<String> lvlDsc = d.getList("lvl_description", String.class);
        this.lvlDescWithoutColor = lvlDsc.toArray(new String[0]);
        this.lvlDesc = lvlDsc.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).toArray(String[]::new);

    }

    public KitLang(final KitMain kitMain, final String isoLang, final String displayName, final String[] lvlDesc, final String... desc)
    {
        this.kitMain = kitMain;
        this.lang = isoLang;

        this.displayNameWithoutColor = displayName;
        this.displayName = ChatColor.translateAlternateColorCodes('&', displayNameWithoutColor);
        this.descWithoutColor = desc;
        this.lvlDescWithoutColor = lvlDesc;
        this.desc = new String[descWithoutColor.length];

        for(int i = 0; i < descWithoutColor.length; i++) this.desc[i] = ChatColor.translateAlternateColorCodes('&', descWithoutColor[i]);

        this.lvlDesc = new String[lvlDescWithoutColor.length];

        for(int i = 0; i < lvlDescWithoutColor.length; i++) this.lvlDesc[i] = ChatColor.translateAlternateColorCodes('&', lvlDescWithoutColor[i]);
    }

    @Override
    public @NotNull String getIsoLang()
    {
        return lang;
    }

    @Override
    public String getDisplayName()
    {
        return displayName;
    }

    @Override
    public String[] getDescription()
    {
        return desc;
    }

    @Override
    public String[] getDescriptionPerLvl()
    {
        return lvlDesc;
    }

    @Override
    public @NotNull String getName()
    {
        return kitMain.getName();
    }

    @Override
    public int getMaxLevel()
    {
        return kitMain.getMaxLevel();
    }

    @Override
    public int[] getPrice()
    {
        return kitMain.getPrice();
    }

    @NotNull
    @Override
    public Material getDisplayMet()
    {
        return kitMain.getDisplayMet();
    }

    @NotNull
    @Override
    public Kit getLang(String lang)
    {
        if(lang.equals(this.lang)) return this;
        return kitMain.getLang(lang);
    }

    @NotNull
    @Override
    public Document toDocument()
    {
        return new Document()
                .append("display_name", displayNameWithoutColor)
                .append("description", Arrays.asList(descWithoutColor))
                .append("lvl_description", Arrays.asList(lvlDescWithoutColor));

    }

    @Override
    public @NotNull KitType getType()
    {
        return kitMain.getType();
    }

    @Override
    public @Nullable String getPermission()
    {
        return kitMain.getPermission();
    }

}
