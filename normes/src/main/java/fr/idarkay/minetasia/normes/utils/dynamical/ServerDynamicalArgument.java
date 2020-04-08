package fr.idarkay.minetasia.normes.utils.dynamical;

import fr.idarkay.minetasia.normes.Args;
import fr.idarkay.minetasia.normes.Tuple;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

/**
 * File <b>ServerDynamicalArgument</b> located on fr.idarkay.minetasia.normes.utils.dynamical
 * ServerDynamicalArgument is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 06/04/2020 at 15:43
 */
public class ServerDynamicalArgument extends DynamicalArgument
{

    private final Supplier<String> valueSupplier;

    public ServerDynamicalArgument(Args args, Supplier<String> valueSupplier)
    {
        super(args);
        this.valueSupplier = valueSupplier;
    }

    public Tuple<? extends Args, String> get(Player player)
    {
        return match(valueSupplier.get());
    }

}
