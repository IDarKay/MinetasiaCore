package fr.idarkay.minetasia.core.common;

import fr.idarkay.minetasia.core.api.MinetasiaCoreApi;
import fr.idarkay.minetasia.core.common.user.IPlayer;
import fr.idarkay.minetasia.core.common.utils.FRSClient;
import fr.idarkay.minetasia.core.common.utils.SQLManager;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * File <b>MinetasiaCore</b> located on fr.idarkay.mintasia.core.common
 * MinetasiaCore is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 13/11/2019 at 15:06
 */
public class MinetasiaCore extends MinetasiaCoreApi {

    private SQLManager sqlManager;
    private FRSClient frsClient;

    @Override
    public void onEnable() {
        saveConfig();
        getMinetasiaLang().init();
        sqlManager = new SQLManager(this);
        frsClient = new FRSClient(this);
    }

    @Override
    public String ping() {
        return "pong";
    }

    @Override
    public fr.idarkay.minetasia.core.api.utils.SQLManager getSqlManager() {
        return sqlManager;
    }

    @Override
    public void setPlayerData(@NotNull UUID uuid, @NotNull String key, @NotNull String value) {

    }

    @Override
    public String getPlayerData(@NotNull UUID uuid, @NotNull String key) {
        return null;
    }


    @Override
    public void Publish(String chanel, String message) {
        frsClient.publish(chanel, message, false);
    }

    public FRSClient getFrsClient() {
        return frsClient;
    }
}
