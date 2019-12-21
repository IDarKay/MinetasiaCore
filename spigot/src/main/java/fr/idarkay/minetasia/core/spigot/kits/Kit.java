package fr.idarkay.minetasia.core.spigot.kits;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>Kit</b> located on fr.idarkay.minetasia.core.spigot.kits
 * Kit is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 21/12/2019 at 17:18
 */
public class Kit implements fr.idarkay.minetasia.core.api.utils.Kit {

    private final String isoLang;
    private final String name;
    private final String displayName;
    private final String displayNameWithouColor;
    private final String[] desc;
    private final String[] descWithoutColor;
    private final String[] lvlDesc;
    private final String[] lvlDescWithoutDesc;
    private final int maxLvl;


    public Kit(final String isoLang, final String name, final String displayName, final int maxLvl, final String[] lvlDesc, final String... desc)
    {
        this.isoLang = isoLang;
        this.name = name;
        this.displayNameWithouColor = displayName;
        this.displayName = ChatColor.translateAlternateColorCodes('&', displayNameWithouColor);
        this.descWithoutColor = desc;
        if(lvlDesc.length != maxLvl + 1)
        {
            throw new IllegalArgumentException("lvlDesc lenght must be equal to maxLvl + 1!");
        }
        this.lvlDescWithoutDesc = lvlDesc;
        this.maxLvl = maxLvl;

        this.desc = new String[descWithoutColor.length];

        for(int i = 0; i < descWithoutColor.length; i++) this.desc[i] = ChatColor.translateAlternateColorCodes('&', descWithoutColor[i]);

        this.lvlDesc = new String[lvlDescWithoutDesc.length];

        for(int i = 0; i < lvlDescWithoutDesc.length; i++) this.lvlDesc[i] = ChatColor.translateAlternateColorCodes('&', lvlDescWithoutDesc[i]);

    }

    public Kit(fr.idarkay.minetasia.core.api.utils.Kit kit)
    {
        this(kit.getIsoLang(), kit.getName(), kit.getDisplayName(), kit.getMaxLevel(), kit.getDescriptionPerLvl(), kit.getDescription());
    }

    public Kit(JsonObject jsonObject)
    {
        isoLang = jsonObject.get("isolang").getAsString();
        name = jsonObject.get("name").getAsString();
        displayNameWithouColor = jsonObject.get("displayname").getAsString();
        displayName = ChatColor.translateAlternateColorCodes('&', displayNameWithouColor);
        maxLvl = jsonObject.get("maxlvl").getAsInt();

        JsonArray desc = jsonObject.get("description").getAsJsonArray();
        descWithoutColor = new String[desc.size()];
        this.desc = new String[descWithoutColor.length];
        int i = 0;
        for(JsonElement je : desc)
        {
            descWithoutColor[i] = je.getAsString();
            this.desc[i] = ChatColor.translateAlternateColorCodes('&', descWithoutColor[i]);
            i++;
        }

        JsonArray lvlDesc = jsonObject.get("lvldescription").getAsJsonArray();
        lvlDescWithoutDesc = new String[lvlDesc.size()];
        this.lvlDesc = new String[lvlDescWithoutDesc.length];
        i = 0;
        for(JsonElement je : lvlDesc)
        {
            lvlDescWithoutDesc[i] = je.getAsString();
            this.lvlDesc[i] = ChatColor.translateAlternateColorCodes('&', lvlDescWithoutDesc[i]);
            i++;
        }

    }

    @Override
    public @NotNull String getIsoLang() {
        return isoLang;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String[] getDescription() {
        return desc;
    }

    @Override
    public String[] getDescriptionPerLvl() {
        return lvlDesc;
    }

    @Override
    public int getMaxLevel() {
        return maxLvl;
    }

    public JsonObject getJson()
    {
        JsonObject main = new JsonObject();
        main.addProperty("isolang", isoLang);
        main.addProperty("name", name);
        main.addProperty("displayname", displayNameWithouColor);
        main.addProperty("maxlvl", maxLvl);

        JsonArray desc = new JsonArray();
        for(String s : descWithoutColor) desc.add(s);
        main.add("description", desc);

        JsonArray lvlDesc = new JsonArray();
        for(String s : lvlDescWithoutDesc) lvlDesc.add(s);
        main.add("lvldescription", lvlDesc);
        return main;
    }

    public String getJsonString()
    {
        return getJson().toString();
    }

}
