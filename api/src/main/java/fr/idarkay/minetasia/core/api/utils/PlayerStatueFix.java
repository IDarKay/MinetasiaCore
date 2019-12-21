package fr.idarkay.minetasia.core.api.utils;

import java.util.UUID;

/**
 * File <b>PlayerStatueFix</b> located on fr.idarkay.minetasia.core.api.utils
 * PlayerStatueFix is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 13/12/2019 at 14:14
 */
public interface PlayerStatueFix {

    /**
     * @return {@link Server} of the player
     */
    Server getServer();

    /**
     * @return the full name of the proxy ( can pe transform to {@link UUID}
     */
    String getProxy();

    /**
     * @return user name of player
     */
    String getUserName();

    /**
     * @return uuid of the player
     */
    UUID getUUID();

}
