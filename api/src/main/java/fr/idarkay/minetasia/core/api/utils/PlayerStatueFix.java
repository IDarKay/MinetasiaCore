package fr.idarkay.minetasia.core.api.utils;

import java.util.UUID;

/**
 * File <b>PlayerStatueFix</b> located on fr.idarkay.minetasia.core.api.utils
 * PlayerStatueFix is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 13/12/2019 at 14:14
 */
public interface PlayerStatueFix {

    Server getServer();

    String getProxy();

    String getUserName();

    UUID getUUID();

}
