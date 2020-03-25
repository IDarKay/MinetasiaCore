package fr.idarkay.minetasia.core.api;

/**
 * File <b>KitType</b> located on fr.idarkay.minetasia.core.api
 * KitType is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 08/03/2020 at 13:57
 */
public enum KitType
{
    /**
     * default type, price in minecoins for each lvl
     */
    BASIC,
    /**
     * a klt with only one lvl buy with {@link Economy#MINECOINS}
     */
    MONO_LVL_MINECOINS,
    /**
     * a kit with only on lvl buy with {@link Economy#STARS}
     */
    MONO_LVL_STARS,
    /**
     * a kit with only on lvl that can't buy only get with a specific permission for rank
     */
    MONO_LVL_PERM,
}
