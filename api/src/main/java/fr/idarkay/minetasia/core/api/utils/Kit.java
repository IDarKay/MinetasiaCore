package fr.idarkay.minetasia.core.api.utils;

import org.jetbrains.annotations.NotNull;

/**
 * File <b>Kit</b> located on fr.idarkay.minetasia.core.api.utils
 * Kit is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 21/12/2019 at 16:40
 */
public interface Kit {

    /**
     * get the lang of this kit instance in <a href="https://www.data.gouv.fr/fr/datasets/r/b4d4331f-d82c-45ce-92fe-615a1a6adc1b">ISO-3166-1 </a>
     * @return {@link String}  the lang in <a href="https://www.data.gouv.fr/fr/datasets/r/b4d4331f-d82c-45ce-92fe-615a1a6adc1b">ISO-3166-1 </a>
     * @since 1.0
     */
    @NotNull String getIsoLang();

    /**
     * get the generic name of the kit <br>
     *     (no depend of the {@link Kit#getIsoLang()}) <br>
     *     need be in format : {@code <plugin_name>_<kits_name>}
     * @return {@link String} the generic name
     */
    @NotNull String getName();

    /**
     * get the display name of the kit <br>
     *      {@link org.bukkit.ChatColor#translateAlternateColorCodes(char, String)} } are already applied <br>
     *      return in lang {@link Kit#getIsoLang()}
     * @return {@link String} the display name of the kit
     */
    String getDisplayName();

    /**
     * get the description of the kit <br>
     *      {@link org.bukkit.ChatColor#translateAlternateColorCodes(char, String)} } are already applied <br>
     *      return in lang {@link Kit#getIsoLang()}
     * @return {@link String[]} the description name of the kit
     */
    String[] getDescription();

    /**
     * get the description of the kit <br>
     *      {@link org.bukkit.ChatColor#translateAlternateColorCodes(char, String)} } are already applied <br>
     *      return in lang {@link Kit#getIsoLang()} <br>
     *      elements 0 {@code -->} for lvl 0 <br>
     *      elements 1 {@code -->} for lvl 1 <br>
     *      ... <br>
     *      table.length = {@link Kit#getMaxLevel()} + 1 (lvl 0)
      *
     * @return {@link String[]} the description name of the kit
     */
    String[] getDescriptionPerLvl();

    /**
     * @return the max lvl of the kit (no depend of the {@link Kit#getIsoLang()})
     */
    int getMaxLevel();

}
