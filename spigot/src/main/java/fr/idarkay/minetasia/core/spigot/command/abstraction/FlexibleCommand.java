package fr.idarkay.minetasia.core.spigot.command.abstraction;

import java.util.List;

/**
 * File <b>FlexibleCommand</b> located on fr.idarkay.minetasia.core.spigot.command.abstraction
 * FlexibleCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 30/11/2019 at 21:38
 */
public interface FlexibleCommand {

    boolean isAllPossibilities();

    List<String> getPossibilities();


}
