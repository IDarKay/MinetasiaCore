package fr.idarkay.minetasia.core.spigot.kits;

import fr.idarkay.minetasia.core.api.Economy;
import fr.idarkay.minetasia.core.api.KitType;
import fr.idarkay.minetasia.core.api.utils.Kit;
import fr.idarkay.minetasia.core.api.utils.MainKit;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * File <b>KitMain</b> located on fr.idarkay.minetasia.core.spigot.kits
 * KitMain is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 11/02/2020 at 12:29
 */
public class KitMain implements MainKit
{

    private final String name;
    private final int maxLvl;
    private final List<Integer> priceList;
    private final KitType kitType;
    private final int[] price;
    private final String permission;
    private final Material mat;
    private final Map<String, KitLang> kits = new HashMap<>();
    private final KitLang defaultKit;

    public KitMain(@NotNull Document d, JavaPlugin plguin)
    {
        this.name = d.getString("_id");
        this.mat = Material.valueOf(d.getString("material"));
        final String type = d.getString("type");
        if(type == null)
            kitType = KitType.BASIC;
        else
            kitType = KitType.valueOf(type);

        if(kitType == KitType.BASIC)
            this.maxLvl = d.getInteger("max_lvl");
        else
            this.maxLvl = 1;

        if(kitType != KitType.MONO_LVL_PERM)
        {
            this.priceList = d.getList("price", Integer.class);
            this.price = new int[priceList.size()];
            int i = 0;
            for(int in : priceList)
            {
                price[i] = in;
                i++;
            }
            this.permission = null;
        }
        else
        {
            this.priceList = null;
            this.price = null;
            this.permission = d.getString("permission");
            plguin.getServer().getPluginManager().addPermission(new Permission("core.kits." + name, PermissionDefault.OP));  //register the permission
        }

        d.get("lang", Document.class).forEach((k, v) -> kits.put(k, new KitLang(this, k, (Document) v)));

        KitLang kitLang = kits.get(MinetasiaLang.BASE_LANG);
        if(kitLang == null) kitLang = getFirstFromMap(kits);

        defaultKit = Objects.requireNonNull(kitLang);
    }

    public KitMain(final String isoLang, final String name, final String displayName, final int maxLvl, final int[] price, Material displayMat, final String[] lvlDesc, final String... desc)
    {
        this.kitType = KitType.BASIC;
        this.name = name;
        this.price = price;

        this.priceList = new ArrayList<>();

        for(int p : price)
        {
            this.priceList.add(p);
        }

        this.mat = displayMat;
        if(lvlDesc.length != maxLvl + 1)
        {
            throw new IllegalArgumentException("lvlDesc lenght must be equal to maxLvl + 1!");
        }
        this.maxLvl = maxLvl;

        this.defaultKit = new KitLang(this, isoLang, displayName, lvlDesc, desc);
        kits.put(isoLang, defaultKit);
        this.permission = null;
    }

    public KitMain(final String isoLang, final String name, final String displayName, Economy economy, final int price, Material displayMat, final String[] lvlDesc, final String... desc)
    {
        if(economy != Economy.MINECOINS && economy != Economy.SHOPEX)
        {
            throw new IllegalArgumentException("cant create kit with " + economy.name +  " economy");
        }
        this.kitType = economy == Economy.MINECOINS ? KitType.MONO_LVL_MINECOINS : KitType.MONO_LVL_STARS;

        this.name = name;
        this.price = new int[]{price};

        this.priceList = new ArrayList<>();
        this.priceList.add(price);

        this.mat = displayMat;
        if(lvlDesc.length != 2)
        {
            throw new IllegalArgumentException("lvlDesc length must be equal to 2");
        }
        this.maxLvl = 1;

        this.defaultKit = new KitLang(this, isoLang, displayName, lvlDesc, desc);
        kits.put(isoLang, defaultKit);
        this.permission = null;
    }

    public KitMain(final String isoLang, final String name, final String displayName, final @NotNull String permission, Material displayMat, final String[] lvlDesc, final String... desc)
    {
        this.kitType = KitType.MONO_LVL_PERM;

        this.name = name;
        this.price = null;

        this.priceList = null;

        this.mat = displayMat;
        if(lvlDesc.length != 2)
        {
            throw new IllegalArgumentException("lvlDesc length must be equal to 2");
        }
        this.maxLvl = 1;

        this.defaultKit = new KitLang(this, isoLang, displayName, lvlDesc, desc);
        kits.put(isoLang, defaultKit);
        this.permission = permission;
    }

    @Override
    public @NotNull String getName()
    {
        return name;
    }

    @Override
    public int getMaxLevel()
    {
        return maxLvl;
    }

    @Override
    public int[] getPrice()
    {
        return price;
    }

    @NotNull
    @Override
    public Material getDisplayMet()
    {
        return mat;
    }

    @NotNull
    @Override
    public Kit getLang(String lang)
    {
        return kits.getOrDefault(lang, defaultKit);
    }

    @NotNull
    @Override
    public Document toDocument()
    {
        final Document l = new Document();
        kits.forEach((k, v) -> l.append(k, v.toDocument()));

        final Document doc = new Document("_id", name).append("type", kitType.name()).append("material", mat.name()).append("lang", l);
        if(kitType != KitType.MONO_LVL_PERM)
        {
            doc.append("price", priceList);
        }
        else
        {
            doc.append("permission", permission);
        }
        if(kitType == KitType.BASIC)
        {
            doc.append("max_lvl", maxLvl);
        }
        return doc;
    }

    @NotNull
    @Override
    public KitType getType()
    {
        return kitType;
    }

    @Override
    public @Nullable String getPermission()
    {
        return permission;
    }

    @Nullable
    private <T> T getFirstFromMap(Map<?, T> maps)
    {
        for (Map.Entry<?, T> tEntry : maps.entrySet())
        {
            return tEntry.getValue();
        }
        return null;
    }

}
