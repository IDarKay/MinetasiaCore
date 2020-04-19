package fr.idarkay.minetasia.core.api;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * File <b>Economy</b> located on fr.idarkay.minetasia.core.api
 * Economy is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),-
 * Created the 15/11/2019 at 21:20
 * @since 1.0
 */
public enum Economy {

    MINECOINS("minecoins", "&6MineCoins", BoostType.MINECOINS),
    @Deprecated
    SHOPEX("shopex", "&6Shopex", BoostType.SHOPEX),
    STARS("stars", "&6Stars", BoostType.STARS),
    BATTLE_XP("battle_xp", "&bBattle Xp", BoostType.BATTLE_XP),
    FLY_COINS("fly_coins", "&6Fly coins", BoostType.SKYBLOCK)
    ;

    public final String name;
    public final String displayName;
    public final BoostType boostType;

    /**
     * @param name the generic name of the money its preferable to never change this when is create !
     *             its preferable to not set special char or color char
     * @param displayName   the display name can be  change the display name will be show in gui message etc...
     * @param boostType  see {@link BoostType}
     * @since 1.0
     */
    Economy(String name, String displayName, BoostType boostType)
    {
        this.name = name;
        this.displayName = displayName;
        this.boostType = boostType;
    }

    /**
     * get {@link Economy} from name
     *
     * @param name to get the {@link Economy}
     * @return the  {@link Economy} or null if none exist
     * @since 1.0
     */
    @Nullable Economy getFromName(String name)
    {
        try
        {
            return Arrays.stream(Economy.values()).filter(m ->  m.name.equals(name)).collect(Collectors.toList()).get(0);
        } catch (IndexOutOfBoundsException ignore)
        {
            return null;
        }
    }

}
