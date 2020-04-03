package fr.idarkay.minetasia.core.api.advancement;

import org.bson.Document;

/**
 * File <b>MinetasiaLangAdvancement</b> located on fr.idarkay.minetasia.core.api.advancement
 * MinetasiaLangAdvancement is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 21/03/2020 at 19:45
 */
public interface MinetasiaLangAdvancement extends MinetasiaBaseAdvancement
{
    String getDescription();

    String getTitle();

    String getIsoLang();

    Document toLangDocument();
}
