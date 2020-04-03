package fr.idarkay.minetasia.core.spigot.advancement;

import com.google.gson.JsonObject;
import fr.idarkay.minetasia.core.api.Economy;
import fr.idarkay.minetasia.core.api.advancement.Criteria;
import fr.idarkay.minetasia.core.api.advancement.MinetasiaLangAdvancement;
import fr.idarkay.minetasia.normes.Tuple;
import org.bson.Document;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * File <b>MinetasiaAdvancementLang</b> located on fr.idarkay.minetasia.core.spigot.advancement
 * MinetasiaAdvancementLang is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 21/03/2020 at 19:51
 */
public class MinetasiaAdvancementLang implements MinetasiaLangAdvancement
{


    private final MinetasiaAdvancement advancement;
    private final String isoLang;
    private final String title;
    private final String description;
//    private final NamespacedKey namespacedKey;

    public MinetasiaAdvancementLang(MinetasiaAdvancement advancement, String isoLang, String title, String description)
    {
        this.advancement = advancement;
        this.isoLang = isoLang;
        this.title = title;
        this.description = description;

        //noinspection deprecation ignore frome copy another frome another plguin
//        this.namespacedKey = new NamespacedKey(advancement.namespacedKey.getNamespace() + "_" + isoLang,  advancement.namespacedKey.getKey());
    }

    public static MinetasiaAdvancementLang fromDocument(MinetasiaAdvancement advancement, String lang, Document document)
    {
        return new MinetasiaAdvancementLang(advancement, lang, document.getString("title"), document.getString("description"));
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    @Override
    public String getIsoLang()
    {
        return isoLang;
    }

    @Override
    public Document toLangDocument()
    {
        return new Document().append("title", title).append("description", description);
    }

    @Override
    public @NotNull MinetasiaLangAdvancement getLang(@NotNull String lang)
    {
        return advancement.getLang(lang);
    }

    @Override
    public void withCriteria(@NotNull  Criteria... criteria)
    {
        advancement.setCriteria(criteria);
    }

    @Override
    public void setParent(@Nullable NamespacedKey parent)
    {
        advancement.setParent(parent);
    }

    @Override
    public void setBackGround(@Nullable String texturePatch)
    {
        advancement.setBackGround(texturePatch);
    }

    @Override
    public void setStatsCriteria(@Nullable Tuple<String, Integer> statsCriteria)
    {
        advancement.setStatsCriteria(statsCriteria);
    }

    @Override
    public void setDefault()
    {
        advancement.setDefault();
    }

    @Override
    public void setRewards(@NotNull Tuple<Economy, Double> rewards)
    {
        advancement.setRewards(rewards);
    }

    @Override
    public @NotNull NamespacedKey getNamespacedKey()
    {
        return advancement.namespacedKey;
    }

    @NotNull
    @Override
    public JsonObject toJson(@NotNull String isoLang)
    {
        return advancement.toJson(isoLang);
    }

    @NotNull
    @Override
    public Document toDocument()
    {
        return advancement.toDocument();
    }
}
