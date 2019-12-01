package fr.idarkay.minetasia.core.api.exception;

/**
 * File <b>CommandException</b> located on fr.idarkay.minetasia.core.api.exception
 * CommandException is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 30/11/2019 at 23:24
 * @since 1.0
 */
public final class CommandException extends RuntimeException {

    public CommandException()
    {
        super();
    }

    public CommandException(String m)
    {
        super(m);
    }

}
