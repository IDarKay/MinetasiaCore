package fr.idarkay.minetasia.core.api.utils;

import fr.idarkay.minetasia.core.api.BoostType;

import java.util.Map;

/**
 * File <b>PlayerBoost</b> located on fr.idarkay.minetasia.core.api.utils
 * PlayerBoost is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 16/01/2020 at 19:29
 */
public interface Boost
{
    Map<BoostType, Float> getBoost();
}
