package fr.idarkay.minetasia.core.api.exception;

/**
 * File <b>PlayerNotFoundException</b> located on fr.idarkay.minetasia.core.api.exception
 * PlayerNotFoundException is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 23/11/2019 at 08:30
 * @since 1.0
 */
public final class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException()
    {
        super();
    }

    public PlayerNotFoundException(String m)
    {
        super(m);
    }
}
