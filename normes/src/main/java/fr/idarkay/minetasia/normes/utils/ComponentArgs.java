package fr.idarkay.minetasia.normes.utils;

import fr.idarkay.minetasia.normes.Args;
import fr.idarkay.minetasia.normes.Tuple;

import java.util.function.Function;

/**
 * File <b>ComponentArgs</b> located on fr.idarkay.minetasia.normes.utils
 * ComponentArgs is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 15/04/2020 at 22:15
 */
public interface ComponentArgs extends Args
{
    default <R> Tuple<Args, Function<R, Object>> matchFunction(Function<R, Object> function)
    {
        return new Tuple<>(this, function);
    }
}
