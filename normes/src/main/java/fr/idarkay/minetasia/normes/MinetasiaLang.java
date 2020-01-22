package fr.idarkay.minetasia.normes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author AloIs B. (IDarKay),
 * <p>
 *     Copyright (c) 2019 Normes, Minetasia.
 * </p>
 * @since 1.0
 */
@SuppressWarnings({"unused"})
public class MinetasiaLang {

    public final static HashMap<String, FileConfiguration> LANG_CONFIG = new HashMap<>();
    public final static String BASE_LANG = "fr";

    private final File dataFolder;
    private static boolean isInit = false;

    MinetasiaLang(File dataFolder)
    {
        this.dataFolder = dataFolder;
    }

    /**
     * Create the default lang file <br>
     * standard: <br>
     * file save in to :
     *  plugin/{@code <plugin name>}/lang/<a href="https://www.data.gouv.fr/fr/datasets/r/b4d4331f-d82c-45ce-92fe-615a1a6adc1b">ISO-3166-1 </a>.yml <br>
     * default file generate : <strong>en.yml</strong>
     *
     * @since 1.0
     */
    public void init()
    {
        isInit = true;

        File f = new File(dataFolder,"lang");

        if(!f.exists())
        {
            if(!f.mkdirs())
            {
                Bukkit.getLogger().warning("[ERROR] can't create " + f.getAbsolutePath());
                return;
            }
        }

        File f2 = new File(f, "en.yml");
        if(!f2.exists()) {
            try {
                if(!f2.createNewFile()) Bukkit.getLogger().warning("[ERROR] can't create " + f2.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if(f.exists())
        {
            for(File file : Objects.requireNonNull(f.listFiles(pathname -> (pathname.isFile() && pathname.getName().toLowerCase().endsWith(".yml"))))){
                LANG_CONFIG.put(file.getName().split("[.]")[0], YamlConfiguration.loadConfiguration(file));
            }
        }

    }

    /**
     * get a message from the lang file <br>
     *      <br>
     *  if no match return the default file <br>
     *      <br>
     *  need to use {@link MinetasiaLang#init()}  first <br>
     *
     *  <br>
     *  standard: <br>
     *
     *  <br>
     *  For use this you need to create Lang enumeration ! <br>
     *  with : <br>
     *
     *   public enum Lang <br>
     *   { <br>
     *      EXAMPLE("example", "Default msg); <br>
     * <br>
     * <br>
     *      private final String path; <br>
     *      private final String defaultMsg; <br>
     * <br>
     *      Lang(String path, String defaultMsg){<br>
     *          this.path = path;<br>
     *          this.defaultMsg = defaultMsg;<br>
     *      }<br>
     * <br>
     *      public String get(@NotNull String lang, Object... args)<br>
     *      {<br>
     *          return MinetasiaLang.get(path, defaultMsg, lang, args);<br>
     *      }<br>
     *      }<br>
     * @param path key of the msg set in lang enum
     * @param defaultMsg the default message to send if no key match
     * @param lang the lang to get the msg in <a href="https://www.data.gouv.fr/fr/datasets/r/b4d4331f-d82c-45ce-92fe-615a1a6adc1b">ISO-3166-1 </a>
     * @param args args of the message
     * @param <T> type of object
     * @return String message
     * @since 1.0
     */
    @SafeVarargs
    public static <T> String get(@NotNull String path, @NotNull String defaultMsg, @NotNull String lang, Tuple<? extends Args, T>... args){

        if(!isInit)
        {
            Bukkit.getLogger().warning("get land message without init lang !");
            return defaultMsg;
        }

        final FileConfiguration l = LANG_CONFIG.getOrDefault(lang, null);

        String msg;

        if(l != null){
            msg = l.getString(path, defaultMsg);
        }
        else
            msg = defaultMsg;

        for (Tuple<? extends Args, T> arg : args)
        {
             msg = Objects.requireNonNull(msg).replace(arg.a().toString(), arg.b().toString());
        }

        return ChatColor.translateAlternateColorCodes('&', msg);

    }

}
