package fr.idarkay.minetasia.normes.utils.dynamical;

import fr.idarkay.minetasia.normes.Args;
import fr.idarkay.minetasia.normes.Tuple;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * File <b>DynamicalArgument</b> located on fr.idarkay.minetasia.normes.utils
 * DynamicalArgument is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 06/04/2020 at 15:39
 */
public abstract class DynamicalArgument implements Args
{

    private final Args args;

    public DynamicalArgument(Args args)
    {
        this.args = args;
    }


    @Override
    public String getNode()
    {
        return args.getNode();
    }

    public abstract Tuple<? extends Args, String> get(@Nullable Player player);

}

