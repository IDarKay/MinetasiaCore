package fr.idarkay.minetasia.core.bungee.utils.user;

import fr.idarkay.minetasia.core.bungee.MinetasiaCoreBungee;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * File <b>PlayerManagement</b> located on fr.idarkay.minetasia.core.common.user
 * PlayerManagement is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 15/11/2019 at 22:25
 */
public class PlayerManager {

    private final MinetasiaCoreBungee plugin;

    public PlayerManager(MinetasiaCoreBungee minetasiaCore)
    {
        this.plugin = minetasiaCore;
    }

    public @Nullable MinePlayer get(UUID uuid)
    {
        try
        {
            return new MinePlayer(uuid);
        }
        catch (IllegalArgumentException ignore)
        {
            return null;
        }
    }

    public void newPlayer(UUID uuid, String name, String lang)
    {
        MinePlayer p = new MinePlayer(uuid, name);
        p.putData("lang", lang);
        p.saveNew();
    }
}
