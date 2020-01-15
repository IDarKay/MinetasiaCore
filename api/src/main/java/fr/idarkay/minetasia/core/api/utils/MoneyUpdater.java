package fr.idarkay.minetasia.core.api.utils;

import fr.idarkay.minetasia.core.api.Economy;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * File <b>MoneyUpdater</b> located on fr.idarkay.minetasia.core.api.utils
 * MoneyUpdater is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 15/01/2020 at 23:11
 */
public interface MoneyUpdater
{
    @NotNull Map<@NotNull Economy, @NotNull Float> getUpdate();
}
