package fr.idarkay.minetasia.core.spigot.utils;

import fr.idarkay.minetasia.core.api.utils.PlayerStatueFix;
import fr.idarkay.minetasia.core.api.utils.Server;

import java.util.UUID;

/**
 * File <b>PlayerStatueFixC</b> located on fr.idarkay.minetasia.core.spigot.utils
 * PlayerStatueFixC is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 13/12/2019 at 14:16
 */
public class PlayerStatueFixC implements PlayerStatueFix {

    private final Server server;
    private final String proxy, username;
    private final UUID uuid;

    public PlayerStatueFixC(UUID uuid, String username, String proxy, Server server)
    {
        this.server = server;
        this.proxy = proxy;
        this.username = username;
        this.uuid = uuid;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public String getProxy() {
        return proxy;
    }

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }
}
