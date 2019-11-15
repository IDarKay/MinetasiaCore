package fr.idarkay.minetasia.core.common.utils;

import fr.idarkay.minetasia.normes.IMinetasiaLang;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>Lang</b> located on fr.idarkay.mintasia.core.common.utils
 * Lang is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 13/11/2019 at 15:44
 */
public enum Lang implements IMinetasiaLang {


    ;


    final String path;
    final String defaultMsg;

    Lang(@NotNull String path, @NotNull String defaultMsg)
    {
        this.path = path;
        this.defaultMsg = defaultMsg;
    }

    @Override
    public String get(String lang, Object... args) {
        return MinetasiaLang.get(path, defaultMsg, lang, args);
    }
}
