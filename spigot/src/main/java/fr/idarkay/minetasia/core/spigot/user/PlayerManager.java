package fr.idarkay.minetasia.core.spigot.user;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
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
public class PlayerManager {

    private final Cache<UUID, Player> userCache;
    private final MinetasiaCore plugin;

    public PlayerManager(MinetasiaCore minetasiaCore)
    {
        this.plugin = minetasiaCore;
        userCache = CacheBuilder.newBuilder().expireAfterWrite(plugin.getConfig().getLong("cache.user"), TimeUnit.MINUTES).build();
    }

    public @Nullable Player get(UUID uuid)
    {
        if(plugin.getFrsClient().isConnected())
            try {
                return userCache.get(uuid, () -> new Player(plugin.getFrsClient().getValue("usersData", uuid.toString())));
            } catch (ExecutionException e) {
                return null;
            }
        else{
            System.out.println("frs down");
            throw new FRSDownException("can't get the player frs down ");
        }
    }

    public void newPlayer(UUID uuid, String name)
    {
        Player p = new Player(uuid, name);
        userCache.put(uuid, p);
        plugin.getSqlManager().updateAsynchronously("INSERT INTO `uuid_username`(`uuid`, `username`) VALUE(?,?)", uuid.toString(), name);
        plugin.getFrsClient().setValue("usersData", uuid.toString(), p.getJson());

    }

    public @Nullable Player getOnlyInCache(UUID uuid)
    {
        return userCache.getIfPresent(uuid);
    }

}
