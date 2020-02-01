package fr.idarkay.minetasia.core.spigot.user;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import fr.idarkay.minetasia.core.api.utils.Group;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.api.exception.FRSDownException;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.TreeMap;
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
public class PlayerManager {

    private final TreeMap<UUID, Player> userCache = new TreeMap<>();
    private final MinetasiaCore plugin;

    public PlayerManager(MinetasiaCore minetasiaCore)
    {
        this.plugin = minetasiaCore;
    }

    public @Nullable Player get(UUID uuid)
    {
        return userCache.get(uuid);
    }

    public Player load(@NotNull UUID uuid)
    {
        Validate.notNull(uuid);
        if(plugin.getFrsClient().isConnected())
        {
            final Player player = new Player(plugin.getFrsClient().getValue("usersData", uuid.toString()), plugin.getFrsClient().getValue("userStats", uuid.toString()));
            byte p = Byte.MIN_VALUE;
            Group g = null;

            plugin.getPermissionManager().groups.forEach((k, v) -> System.out.println(k));

            for (String gs : plugin.getPermissionManager().getGroupsOfUser(player))
            {
                System.out.println(gs);
                Group group = plugin.getPermissionManager().groups.get(gs);
                byte i = group.getPriority();
                if (g == null || i > p)
                {
                    p = i;
                    g = group;
                }
            }

            if (g != null)
            {
                player.setPartyBoost(g.getPartyBoost());
                player.setPersonalBoostBoost(g.getPersonalBoost());
            }
            userCache.put(uuid, player);
            return player;
        }
        else
        {
            throw new FRSDownException("can't get the player frs down ");
        }
    }

    public void removePlayer(@NotNull UUID uuid)
    {
        Validate.notNull(uuid);
        userCache.remove(uuid);
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
        return userCache.get(uuid);
    }

}
