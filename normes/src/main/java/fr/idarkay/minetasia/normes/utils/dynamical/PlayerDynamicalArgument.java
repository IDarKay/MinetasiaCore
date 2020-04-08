package fr.idarkay.minetasia.normes.utils.dynamical;

import fr.idarkay.minetasia.normes.Args;
import fr.idarkay.minetasia.normes.Tuple;
import org.bukkit.entity.Player;

import java.util.function.Function;

/**
 * File <b>PlayerDynamicalArgument</b> located on fr.idarkay.minetasia.normes.utils.dynamical
 * PlayerDynamicalArgument is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 06/04/2020 at 15:44
 */
public class PlayerDynamicalArgument extends DynamicalArgument
{

    private final Function<Player, String> argsGetter;

    public PlayerDynamicalArgument(Args args, Function<Player, String> argsGetter)
    {
        super(args);
        this.argsGetter = argsGetter;
    }

    public Tuple<? extends Args, String> get(Player player)
    {
        return match(argsGetter.apply(player));
    }

}
