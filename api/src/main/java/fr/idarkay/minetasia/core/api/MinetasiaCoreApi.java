package fr.idarkay.minetasia.core.api;

import fr.idarkay.minetasia.core.api.utils.SQLManager;
import fr.idarkay.minetasia.normes.MinetasiaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * File <b>MinetasiaCoreApi</b> located on fr.idarkay.minetasia.core.api
 * MinetasiaCoreApi is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 13/11/2019 at 15:05
 */
public abstract class MinetasiaCoreApi extends MinetasiaPlugin {

    private static MinetasiaCoreApi instance;

    protected MinetasiaCoreApi()
    {
        if(instance == null) instance = this;
    }

    public static MinetasiaCoreApi getInstance() {
        return instance;
    }

    public abstract String ping();

    public abstract SQLManager getSqlManager();

    public abstract void setPlayerData(@NotNull UUID uuid, @NotNull String key, @NotNull String value);

    public abstract String getPlayerData(@NotNull UUID uuid, @NotNull String key);

    public abstract void Publish(String chanel, String message);



}
