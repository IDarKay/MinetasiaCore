package fr.idarkay.minetasia.core.spigot.user;

import fr.idarkay.minetasia.core.api.utils.Group;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;

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

    private final TreeMap<UUID, MinePlayer> userCache = new TreeMap<>();
    private final TreeMap<UUID, CorePlayer> coreUser = new TreeMap<>();
    private final MinetasiaCore plugin;

    public PlayerManager(MinetasiaCore minetasiaCore)
    {
        this.plugin = minetasiaCore;
    }

    public @Nullable MinePlayer get(UUID uuid)
    {
        final MinePlayer p = userCache.get(uuid);
        return p == null ? new MinePlayer(uuid, true) : p;
    }

    @NotNull
    public CorePlayer getCorePlayer(UUID uuid)
    {
        return Objects.requireNonNull(coreUser.get(uuid), "player not only to this server");
    }

    public MinePlayer load(@NotNull UUID uuid)
    {
        Validate.notNull(uuid);
        final MinePlayer player = new MinePlayer(uuid, false);
        coreUser.put(uuid, new CorePlayer(uuid, player.getName()));
        byte p = Byte.MIN_VALUE;
        Group g = null;

        final List<String> groupsOfUser = plugin.getPermissionManager().getGroupsOfUser(player);

        //default group
        for (fr.idarkay.minetasia.core.spigot.permission.Group defaultGroup : plugin.getPermissionManager().getDefaultGroups())
        {
            if(!groupsOfUser.contains(defaultGroup.getName()))
            {
                plugin.getPermissionManager().addGroupWithoutUpdate(player, defaultGroup.getName());
                groupsOfUser.add(defaultGroup.getName());
            }
        }

        for (String gs : groupsOfUser)
        {
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
            player.setPersonalBoost(g.getPersonalBoost());
        }
        try
        {
            plugin.getAdvancementManager().loadUsers(player, Objects.requireNonNull(Bukkit.getPlayer(player.getUUID())));
        } catch (NullPointerException ignore)
        {
            //player leave
        }
        userCache.put(uuid, player);
        return player;
    }

    public void removePlayer(@NotNull UUID uuid)
    {
        Validate.notNull(uuid);
        userCache.remove(uuid);
        coreUser.remove(uuid);
    }


    public @Nullable MinePlayer getOnlyInCache(UUID uuid)
    {
        return userCache.get(uuid);
    }

}
