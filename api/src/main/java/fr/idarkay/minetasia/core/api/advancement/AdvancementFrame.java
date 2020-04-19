package fr.idarkay.minetasia.core.api.advancement;

import org.jetbrains.annotations.NotNull;

/**
 * File <b>AdvancementFrame</b> located on fr.idarkay.minetasia.core.spigot.advancement
 * AdvancementFrame is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 20/03/2020 at 23:41
 */
public enum AdvancementFrame
{
    TASK("task"),
    GOAL("goal"),
    CHALLENGE("challenge");

    @NotNull public final String value;

    AdvancementFrame(@NotNull String value)
    {
        this.value = value;
    }

    @NotNull
    public String getValue()
    {
        return value;
    }
}
