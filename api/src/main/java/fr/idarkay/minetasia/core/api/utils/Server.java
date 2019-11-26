package fr.idarkay.minetasia.core.api.utils;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * File <b>Server</b> located on fr.idarkay.minetasia.core.api.utils
 * Server is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 26/11/2019 at 20:30
 */
public interface Server {

    long getCreatTime();

    @NotNull
    String getIp();

    int getPort();

    @NotNull
    UUID getUuid();

    @NotNull
    String getType();

    @NotNull
    String getName();

}
