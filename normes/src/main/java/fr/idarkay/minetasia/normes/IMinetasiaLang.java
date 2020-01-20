package fr.idarkay.minetasia.normes;

import org.bukkit.entity.Player;

/**
 * File <b>IMinetasiaLang</b> located on fr.idarkay.minetasia.normes
 * IMinetasiaLang is a part of Normes.
 * <p>
 * Copyright (c) 2019 Normes.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 13/11/2019 at 15:45
 * @since 1.0
 *
 * implements your lang enum with this for get the standard methods
 */
public interface IMinetasiaLang
{
     <T> String get(String lang, Tuple<? extends Args, T>... args);
}
