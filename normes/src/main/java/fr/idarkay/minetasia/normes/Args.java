package fr.idarkay.minetasia.normes;

/**
 * File <b>Args</b> located on fr.idarkay.minetasia.normes
 * Args is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 20/01/2020 at 14:57
 */

public interface Args
{

    /**
     * node of args if : {args_name_to_lower_case}
     * @return node
     */
    default String getNode()
    {
        return this instanceof Enum ? "{" + ((Enum) this).name().toLowerCase() + "}" : null;
    }

    /**
     *
     * match a args with a value (String, number, etc)
     * @param args the object to match with the args
     * @param <T> type of object
     * @return the tuple argument value
     */
    default <T> Tuple<? extends Args, T>  match(T args)
    {
        return new Tuple<>(this, args);
    }



}
