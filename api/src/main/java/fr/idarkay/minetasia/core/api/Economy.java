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
 * @author alice. B. (IDarKay),
 * Created the 15/11/2019 at 21:20
 */
public enum Economy {

    MINECOINS("minecoins", "&6MineCoins", 1_000)
    ;

    public final String name;
    public final String displayName;
    public final float warnNumber;

    /**
     * @param name the generic name of the money its preferable to never change this when is create !
     *             its preferable to not set special char or color char
     * @param displayName   the display name can be  change the display name will be show in gui message etc...
     * @param warnNumber the limits number after that a warn message will be send in log and in to Webhooks.
     */
    Economy(String name, String displayName, float warnNumber)
    {
        this.name = name;
        this.displayName = displayName;
        this.warnNumber = warnNumber;
    }

    /**
     * get {@link Economy} from name
     *
     * @param name to get the {@link Economy}
     * @return the  {@link Economy} or null if none exist
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
