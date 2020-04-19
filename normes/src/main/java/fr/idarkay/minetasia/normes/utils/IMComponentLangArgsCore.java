package fr.idarkay.minetasia.normes.utils;

import fr.idarkay.minetasia.normes.Args;
import fr.idarkay.minetasia.normes.Tuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * File <b>IMComponentLangArgs</b> located on fr.minetasia.skyblock.api.lang
 * IMComponentLangArgs is a part of MinetasiaSkyblockCore.
 * <p>
 * Copyright (c) 2020 MinetasiaSkyblockCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 15/04/2020 at 21:25
 */
public final class IMComponentLangArgsCore<T> implements IMComponentLang
{

    private static final Map<String, IMComponentLangArgsCore<?>> registered = new HashMap<>();

    @SafeVarargs
    public static <R> IMComponentLangArgsCore<R> register(IMComponentLang imComponentLang, @NotNull Tuple<Args, Function<R, Object>>... args)
    {
        IMComponentLangArgsCore<R> rimComponentLangArgsCore = new IMComponentLangArgsCore<>(imComponentLang, args);
        registered.putIfAbsent(imComponentLang.getKey(), rimComponentLangArgsCore);
        return rimComponentLangArgsCore;
    }

    @Nullable
    public static <R> IMComponentLangArgsCore<R> getRegistered(String key) throws ClassCastException
    {
        return (IMComponentLangArgsCore<R>) registered.get(key);
    }

    private final IMComponentLang imComponentLang;
    private final Tuple<Args, Function<T, Object>>[] args;

    @SafeVarargs
    private IMComponentLangArgsCore(IMComponentLang imComponentLang, @NotNull Tuple<Args, Function<T, Object>>... args)
    {
        this.imComponentLang = imComponentLang;
        this.args = args;
    }

    @NotNull
    public Tuple<Args, Function<T, Object>>[] getArgs()
    {
        return args;
    }

    @Override
    public String getDefault()
    {
        return imComponentLang.getDefault();
    }

    @Override
    public String getKey()
    {
        return imComponentLang.getKey();
    }
}
