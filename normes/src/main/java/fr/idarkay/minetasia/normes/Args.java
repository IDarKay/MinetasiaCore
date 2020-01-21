package fr.idarkay.minetasia.normes;

/**
 * File <b>Args</b> located on fr.idarkay.minetasia.normes
 * Args is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 20/01/2020 at 14:57
 */

public interface Args
{


    default String getNode()
    {
        return this instanceof Enum ? "{" + ((Enum) this).name().toLowerCase() + "}" : null;
    }

    default <T> Tuple<? extends Args, T>  match(T args)
    {
        return new Tuple<>(this, args);
    }



}
