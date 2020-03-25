package fr.idarkay.minetasia.core.api.utils;

import java.util.Map;
import java.util.UUID;

/**
 * File <b>Party</b> located on fr.idarkay.minetasia.core.api.utils
 * Party is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 05/02/2020 at 15:29
 */
public interface Party
{

    UUID getId();

    UUID getOwner();

    String getOwnerName();

   Map<UUID, String> getPlayers();

    int limitSize();
}
