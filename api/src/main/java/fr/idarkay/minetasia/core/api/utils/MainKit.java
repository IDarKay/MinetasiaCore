package fr.idarkay.minetasia.core.api.utils;

import org.bson.Document;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

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
     *  table.length must be equal to {@link Kit#getMaxLevel()}
     * @return table of int
     */
    int[] getPrice();

    /**
     * @return material wil b show in gui
     */
    Material getDisplayMet();

    /**
     * get the kits text in given lang
     * @param lang isolang
     * @return teh kti or default kit in {@link fr.idarkay.minetasia.normes.MinetasiaLang#BASE_LANG}
     */
    Kit getLang(String lang);

    Document toDocument();

}
