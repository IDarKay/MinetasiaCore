package fr.idarkay.minetasia.core.api.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * File <b>MinetasiaSettings</b> located on fr.idarkay.minetasia.core.api.utils
 * MinetasiaSettings is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 29/03/2020 at 21:19
 */
public interface MinetasiaSettings<T>
{

    /**
     * get the value
     * @return the value or null
     */
    @Nullable
    T getValue();


    /**
     * set the new value into bdd and update all server
     * @param value the new value
     */
    void setValue(@NotNull T value);

    /**
     * set the new value but not updates bdd and other server
     * @param value the new value
     */
    void setValueLocal(@NotNull T value);

    Class<T> getClazz();
}
