package fr.idarkay.minetasia.core.api.utils;

import fr.idarkay.minetasia.core.api.KitType;
import org.bson.Document;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * File <b>MainKit</b> located on fr.idarkay.minetasia.core.api.utils
 * MainKit is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 11/02/2020 at 12:29
 */
public interface MainKit
{
    /**
     * get the generic name of the kit <br>
     *     (no depend of the {@link Kit#getIsoLang()}) <br>
     *     need be in format : {@code <plugin_name>_<kits_name>}
     * @return {@link String} the generic name
     */
    @NotNull String getName();

    /**
     * @return the max lvl of the kit (no depend of the {@link Kit#getIsoLang()})
     */
    int getMaxLevel();

    /**
     * the element 0 is price for lvl 1
     * element 1 for lvl 2 etc <br>
     *  table.length must be equal to {@link Kit#getMaxLevel()} <br>
     * return null if  type = {@link KitType#MONO_LVL_MINECOINS}
     * @return table of int
     */
    @Nullable
    int[] getPrice();

    /**
     * @return material wil b show in gui
     */
    @NotNull
    Material getDisplayMet();

    /**
     * get the kits text in given lang
     * @param lang isolang
     * @return teh kti or default kit in {@link fr.idarkay.minetasia.normes.MinetasiaLang#BASE_LANG}
     */
    @NotNull
    Kit getLang(String lang);

    @NotNull
    Document toDocument();

    /**
     * get the type of the document
     * @return the {@link KitType}
     */
    @NotNull
    KitType getType();

    /**
     * get the permission of the kit
     * @return the permission or null if Type != {@link KitType#MONO_LVL_PERM}
     */
    @Nullable
    String getPermission();

}
