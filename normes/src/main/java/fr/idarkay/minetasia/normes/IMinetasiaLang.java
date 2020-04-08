package fr.idarkay.minetasia.normes;

/**
 * File <b>IMinetasiaLang</b> located on fr.idarkay.minetasia.normes
 * IMinetasiaLang is a part of Normes.
 * <p>
 * Copyright (c) 2019 Normes.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 13/11/2019 at 15:45
 * @since 1.0
 *
 * implements your lang enum with this for get the standard methods
 */

import org.bukkit.entity.Player;

/**
 * implement your Lang enum with this
 */
public interface IMinetasiaLang
{
     /**
      * get the message with lang
      * @see MinetasiaLang#get(String, String, String, Tuple[])
      * @param lang lang of user in <a href="https://www.data.gouv.fr/fr/datasets/r/b4d4331f-d82c-45ce-92fe-615a1a6adc1b">ISO-3166-1 </a>
      * @param args all argument of the message use {@link Args#match(Object)}
      * @param <T> type of object
      * @return message
      */
     <T> String get(String lang, Tuple<? extends Args, T>... args);

     /**
      * get the message with lang
      * @see MinetasiaLang#get(String, String, String, Tuple[])
      * @param lang lang of user in <a href="https://www.data.gouv.fr/fr/datasets/r/b4d4331f-d82c-45ce-92fe-615a1a6adc1b">ISO-3166-1 </a>
      * @param args all argument of the message use {@link Args#match(Object)}
      * @param <T> type of object
      * @return message
      */
      <T> String getWithoutPrefix(String lang, Tuple<? extends Args, T>... args);

    /**
     * only for npc
     * @param lang lang
     * @param player player
     * @return msg
     */
      default String getWithoutPrefixPlayer(String lang, Player player)
      {
          return this.getWithoutPrefix(lang);
      }

}
