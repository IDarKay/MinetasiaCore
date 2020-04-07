package fr.idarkay.minetasia.normes.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * File <b>Message</b> located on fr.idarkay.minetasia.normes.utils
 * Message is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 03/04/2020 at 16:57
 */
public class Message
{
    @NotNull
    private final String key, def, path;

    /**
     * create a custom message can't be set in enum Lang
     * @param key the key of the lang message
     * @param def the default msg if not set in Lang.file
     */
    public Message(@NotNull String key, @NotNull String def)
    {
        this.key = Objects.requireNonNull(key);
        this.def = Objects.requireNonNull(def);
        this.path = key.toLowerCase().replace("_","-");
    }

    public String getDef()
    {
        return def;
    }

    public String getKey()
    {
        return key;
    }

    public String getPath()
    {
        return path;
    }
}
