package fr.idarkay.minetasia.test;

import fr.idarkay.minetasia.core.api.utils.Kit;
import fr.idarkay.minetasia.core.api.utils.MainKit;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.bson.Document;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * File <b>KitMain</b> located on fr.idarkay.minetasia.core.spigot.kits
 * KitMain is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 11/02/2020 at 12:29
 */
public class KitMain implements MainKit
{

    private final String name;
    private final int maxLvl;
    private final List<Integer> priceList;
    private final int[] price;
    private final Material mat;
    private final Map<String, KitLang> kits = new HashMap<>();
    private final KitLang defaultKit;

    public KitMain(@NotNull Document d)
    {
        this.name = d.getString("_id");
        this.maxLvl = d.getInteger("max_lvl");
        this.mat = Material.valueOf(d.getString("material"));
        priceList = d.getList("price", int.class);
        price = new int[priceList.size()];
        int i = -1;
        for(int in : priceList)
        {
            price[i++] = in;
        }

        d.get("lang", Document.class).forEach((k, v) -> kits.put(k, new KitLang(this, k, (Document) v)));

        defaultKit = Objects.requireNonNull(kits.get(MinetasiaLang.BASE_LANG));
    }

    public KitMain(final String isoLang, final String name, final String displayName, final int maxLvl, final int[] price, Material displayMat, final String[] lvlDesc, final String... desc)
    {
        this.name = name;
        this.price = price;

        priceList = new ArrayList<>();

        for(int p : price)
        {
            priceList.add(p);
        }

        this.mat = displayMat;
        if(lvlDesc.length != maxLvl + 1)
        {
            throw new IllegalArgumentException("lvlDesc lenght must be equal to maxLvl + 1!");
        }
        this.maxLvl = maxLvl;

        this.defaultKit = new KitLang(this, isoLang, displayName, lvlDesc, desc);
        kits.put(isoLang, defaultKit);
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

    @Override
    public Material getDisplayMet()
    {
        return mat;
    }

    @Override
    public Kit getLang(String lang)
    {
        return kits.getOrDefault(lang, defaultKit);
    }

    @Override
    public Document toDocument()
    {
        final Document l = new Document();
        kits.forEach((k, v) -> l.append(k, v.toDocument()));

        return new Document("_id", name)
                .append("max_lvl", maxLvl)
                .append("material", mat)
                .append("price", priceList)
                .append("lang", l);

    }

}
