package fr.idarkay.minetasia.core.api.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * File <b>PlayerStats</b> located on fr.idarkay.minetasia.core.api.utils
 * PlayerStats is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 15/01/2020 at 21:08
 */
public interface PlayerStats
{
    @NotNull
    Map<@NotNull String, @NotNull Long> getAllStats();

    @NotNull
    Map<@NotNull String, @NotNull Long> getPluginStats(String pluginName);

    long getStatsValue(String statsName);
}
