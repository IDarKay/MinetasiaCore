package fr.idarkay.minetasia.core.bungee.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>Lang</b> located on fr.idarkay.minetasia.core.bungee.utils
 * Lang is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 03/04/2020 at 21:46
 */
public enum Lang
{
    BAN_FORMAT("&c&lBanned by {player}@@&cReason : {reason}@@&cFor {time}"),
    ;

   private static Configuration config;

    public static void setConfig(Configuration config)
    {
        Lang.config = config;
    }

    @NotNull
    private final String defaultMsg;

    Lang(@NotNull String defaultMsg)
    {
        this.defaultMsg = defaultMsg;
    }

    @NotNull
    public String getMessage(String lang)
    {
        String msg = config.getString("lang." + lang + "." + name());
        if(msg == null || msg.isEmpty())
        {
            if(!lang.equals("eu"))
            {
                msg = config.getString("lang.eu." + name());
                if(msg == null  || msg.isEmpty())
                {
                    msg = defaultMsg;
                }
            }
            else
            {
                msg = defaultMsg;
            }
        }
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
