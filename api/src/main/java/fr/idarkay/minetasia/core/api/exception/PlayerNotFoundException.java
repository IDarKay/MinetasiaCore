package fr.idarkay.minetasia.core.api.exception;

/**
 * File <b>PlayerNotFoundExecption</b> located on fr.idarkay.minetasia.core.api.exception
 * PlayerNotFoundException is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 23/11/2019 at 08:30
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
