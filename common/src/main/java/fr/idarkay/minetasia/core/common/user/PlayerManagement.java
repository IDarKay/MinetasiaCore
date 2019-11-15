package fr.idarkay.minetasia.core.common.user;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import fr.idarkay.minetasia.core.common.MinetasiaCore;
import org.bukkit.plugin.Plugin;
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
 * @author Alois. B. (IDarKay),
 * Created the 15/11/2019 at 22:25
 */
public class PlayerManagement {

    private final Cache<UUID, Player> userCache;
    private final MinetasiaCore plugin;

    public PlayerManagement(MinetasiaCore plugin)
    {
        this.plugin = plugin;
        userCache = CacheBuilder.newBuilder().expireAfterAccess(plugin.getConfig().getLong("cache.user"), TimeUnit.MINUTES).build();
    }

    public @Nullable Player get(UUID uuid)
    {
        try {
            return userCache.get(uuid, () -> new Player(plugin.getFrsClient().getValue("usersData", uuid.toString())));
        } catch (ExecutionException ignore) {
            return null;
        }
    }

    public @Nullable Player getOnlyInCache(UUID uuid)
    {
        try {
            return userCache.get(uuid, () -> null);
        } catch (ExecutionException ignore) {
            return null;
        }
    }

}
