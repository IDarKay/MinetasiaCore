package fr.idarkay.minetasia.normes.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

/**
 * File <b>HeadConstent</b> located on fr.idarkay.minetasia.hub.Utils
 * HeadConstent is a part of minetasiahub.
 * <p>
 * Copyright (c) 2020 minetasiahub.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 31/01/2020 at 20:33
 */
public abstract class HeadConstant
{

    public static final String LEFT_ARROW = getTexture("left-arrow");
    public static final String RIGHT_ARROW = getTexture("right-arrow");
    public static final String QUESTION_MARK = getTexture("question-mak");
    public static final String UNKNOWN = getTexture("unknown");
    public static final String BAN = getTexture("ban");
    public static final String MUTE = getTexture("mute");
    public static final String WARN = getTexture("warn");



    private static final String CONFIG_NAME = "data/head.yml";
    private static FileConfiguration fileConfiguration = null;

    private static FileConfiguration getFile()
    {
        if(fileConfiguration == null)
        {
            final File file = new File("plugins/normes/" + CONFIG_NAME);
            if(!file.exists())
            {
                try
                {
                    URL url =  HeadConstant.class.getClassLoader().getResource("data/head.yml");
                    URLConnection connection = url.openConnection();
                    connection.setUseCaches(false);
                    try(Reader reader = new InputStreamReader(connection.getInputStream()))
                    {
                        fileConfiguration = YamlConfiguration.loadConfiguration(reader);
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                fileConfiguration = YamlConfiguration.loadConfiguration(file);
            }
        }
        return fileConfiguration;
    }

    private static String getTexture(String name)
    {
        return getFile().getString(name);
    }
}
