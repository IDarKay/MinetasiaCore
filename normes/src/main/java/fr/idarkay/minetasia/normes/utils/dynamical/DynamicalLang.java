package fr.idarkay.minetasia.normes.utils.dynamical;

import fr.idarkay.minetasia.normes.Args;
import fr.idarkay.minetasia.normes.IMinetasiaLang;
import fr.idarkay.minetasia.normes.Tuple;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * File <b>DynamicalLang</b> located on fr.idarkay.minetasia.normes.utils
 * DynamicalLang is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 06/04/2020 at 15:38
 */
public class DynamicalLang implements IMinetasiaLang
{

    private final IMinetasiaLang iMinetasiaLang;
    private final DynamicalArgument[] dynamicalArgument;
    private final boolean isOnlyServer;

    public DynamicalLang(@NotNull IMinetasiaLang iMinetasiaLang, @NotNull DynamicalArgument... dynamicalArgument)
    {
        this.iMinetasiaLang = iMinetasiaLang;
        this.dynamicalArgument = dynamicalArgument;
        for (DynamicalArgument argument : dynamicalArgument)
        {
            if(argument instanceof PlayerDynamicalArgument)
            {
                isOnlyServer = false;
                return;
            } else if(!(argument instanceof ServerDynamicalArgument)) throw new IllegalArgumentException(" argument must be a PlayerDynamicalArgument or a ServerDynamicalArgument");
        }
        isOnlyServer = true;
    }

    public boolean isOnlyServer()
    {
        return isOnlyServer;
    }

    private Tuple<? extends Args, String>[] getArgs(@Nullable Player player)
    {
        Validate.isTrue(player != null || isOnlyServer);
        final Tuple<? extends Args, String>[] arg = new Tuple[dynamicalArgument.length];
        for (int i = 0; i < dynamicalArgument.length; i++)
        {
            arg[i] = dynamicalArgument[i].get(player);
        }
        return arg;
    }

    @Override
    public <T> String get(String lang, Tuple<? extends Args, T>... args)
    {
        return iMinetasiaLang.get(lang, args);
    }

    @Override
    public <T> String getWithoutPrefix(String lang, Tuple<? extends Args, T>... args)
    {
        return  iMinetasiaLang.getWithoutPrefix(lang, args);
    }

    @Override
    public String getWithoutPrefixPlayer(String lang, Player player)
    {
        return getWithoutPrefix(lang, getArgs(player));
    }
}
