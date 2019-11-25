package fr.idarkay.minetasia.core.api.exception;

/**
 * File <b>NoEnoughtMoneyException</b> located on fr.idarkay.minetasia.core.api.exception
 * NoEnoughtMoneyException is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 23/11/2019 at 08:39
 */
public final class NoEnoughMoneyException extends RuntimeException {

    public NoEnoughMoneyException()
    {
        super();
    }

    public NoEnoughMoneyException(String m)
    {
        super(m);
    }

}
