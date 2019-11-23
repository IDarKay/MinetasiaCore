package fr.idarkay.minetasia.core.common.exception;

/**
 * File <b>PlayerNotFoundExecption</b> located on fr.idarkay.minetasia.core.common.exception
 * PlayerNotFoundException is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
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
