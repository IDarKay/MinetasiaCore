package fr.idarkay.minetasia.core.bungee.utils.user;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import fr.idarkay.minetasia.core.bungee.MinetasiaCoreBungee;
import fr.idarkay.minetasia.core.bungee.exception.FRSDownException;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * File <b>PlayerManagement</b> located on fr.idarkay.minetasia.core.bungee.utils.user
 * PlayerManagement is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 26/11/2019 at 18:53
 */
public class PlayerManager {

    private final MinetasiaCoreBungee plugin;

    public PlayerManager(MinetasiaCoreBungee minetasiaCore)
    {
        this.plugin = minetasiaCore;
    }

    public @Nullable MinePlayer get(UUID uuid)
    {
        if(plugin.getFrsClient().isConnected())
            try {
                return new MinePlayer(uuid);
            } catch (Exception e) {
                return null;
            }
        else{
            System.out.println("frs down");
            throw new FRSDownException("can't get the player frs down ");
        }
    }

    public void newPlayer(UUID uuid, String name, String lang)
    {
        MinePlayer p = new MinePlayer(uuid, name);
        p.putGeneralData("lang", lang);
        plugin.getSqlManager().updateAsynchronously("INSERT INTO `uuid_username`(`uuid`, `username`) VALUE(?,?)", uuid.toString(), name);
    }
}
