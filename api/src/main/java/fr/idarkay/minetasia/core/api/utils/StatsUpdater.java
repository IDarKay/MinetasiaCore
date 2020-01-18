package fr.idarkay.minetasia.core.api.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * File <b>StatsUpdater</b> located on fr.idarkay.minetasia.core.api.utils
 * StatsUpdater is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 15/01/2020 at 20:59
 */
public interface StatsUpdater
{
    /**
     * stats format : pluginName_statsName <br>
     * pluginName need be same of server_type in config !
     * @return {@code Map<String, Long> with all stats to up String -> name of teh stats Long -> value to ADD}
     */
    @NotNull
    Map<@NotNull String, @NotNull Long> getUpdate();
}
