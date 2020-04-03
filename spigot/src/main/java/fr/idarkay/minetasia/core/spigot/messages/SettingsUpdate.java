package fr.idarkay.minetasia.core.spigot.messages;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>SettingsUpdate</b> located on fr.idarkay.minetasia.core.spigot.messages
 * SettingsUpdate is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 31/03/2020 at 15:31
 */
public class SettingsUpdate extends CoreMessage
{


    public SettingsUpdate()
    {
        super(getIdentifier());
    }

    @Override
    public void actionOnGet(MinetasiaCore plugin, String... args)
    {
       final String concat = concat(args, ";", 1);
       plugin.getSettingsManager().Update(concat);
    }

    public static @NotNull String getMessage(String json)
    {
        return CoreMessage.getMessage(getIdentifier(), json);
    }

    public static  @NotNull String getIdentifier()
    {
        return "core-settings";
    }

}
