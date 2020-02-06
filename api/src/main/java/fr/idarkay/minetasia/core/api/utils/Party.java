package fr.idarkay.minetasia.core.api.utils;

import java.util.List;
import java.util.UUID;

/**
 * File <b>Party</b> located on fr.idarkay.minetasia.core.api.utils
 * Party is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 05/02/2020 at 15:29
 */
public interface Party
{
    UUID getOwner();

    List<UUID> getPlayers();

    int limitSize();
}
