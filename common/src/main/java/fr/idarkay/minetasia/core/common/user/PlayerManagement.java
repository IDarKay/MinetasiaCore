package fr.idarkay.minetasia.core.common.user;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import fr.idarkay.minetasia.core.common.MinetasiaCore;
import fr.idarkay.minetasia.core.api.exception.FRSDownException;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * File <b>PlayerManagement</b> located on fr.idarkay.minetasia.core.common.user
 * PlayerManagement is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 15/11/2019 at 22:25
 */
public class PlayerManagement {

    private final Cache<UUID, Player> userCache;
    private final MinetasiaCore plugin;

    public PlayerManagement(MinetasiaCore minetasiaCore)
    {
        this.plugin = minetasiaCore;
        userCache = CacheBuilder.newBuilder().expireAfterWrite(plugin.getConfig().getLong("cache.user"), TimeUnit.MINUTES).build();
    }

    public @Nullable Player get(UUID uuid)
    {
        try {
            return userCache.get(uuid, () -> {
                if(plugin.getFrsClient().isConnected())
                    return new Player(plugin.getFrsClient().getValue("usersData", uuid.toString()));
                else throw new FRSDownException("can't get the player frs down ");
            });
        } catch (ExecutionException e) {
            return null;
        }
    }

    public void newPlayer(UUID uuid, String name)
    {
        Player p = new Player(uuid, name);
        userCache.put(uuid, p);
        plugin.getSqlManager().update("INSERT INTO `uuid_username`(`uuid`, `username`) VALUE(?,?)", uuid.toString(), name);
        plugin.getFrsClient().setValue("usersData", uuid.toString(), p.getJson());

    }

    public @Nullable Player getOnlyInCache(UUID uuid)
    {
        return userCache.getIfPresent(uuid);
    }

}
