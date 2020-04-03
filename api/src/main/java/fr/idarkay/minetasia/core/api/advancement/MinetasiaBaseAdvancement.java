package fr.idarkay.minetasia.core.api.advancement;

import com.google.gson.JsonObject;
import fr.idarkay.minetasia.core.api.Economy;
import fr.idarkay.minetasia.normes.Tuple;
import org.bson.Document;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * File <b>MinetasiaBaseAdvancement</b> located on fr.idarkay.minetasia.core.api.advancement
 * MinetasiaBaseAdvancement is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 21/03/2020 at 19:44
 */
public interface MinetasiaBaseAdvancement
{
    @NotNull
    MinetasiaLangAdvancement getLang(@NotNull String lang);

    void withCriteria(@NotNull Criteria... criteria);

    void setRewards(@NotNull Tuple<Economy, Double> rewards);

    void setParent(@Nullable NamespacedKey parent);

    void setBackGround(@Nullable String texturePatch);

    void setStatsCriteria(@Nullable Tuple<String, Integer> statsCriteria);

    void setDefault();

    @NotNull
    NamespacedKey getNamespacedKey();

    @NotNull
    JsonObject toJson(@NotNull String lang);

    @NotNull
    Document toDocument();
}
