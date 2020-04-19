package fr.idarkay.minetasia.core.spigot.commands.friends;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.MainCommand;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * File <b>FriendCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.friends
 * FriendCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 14/03/2020 at 22:08
 */
public class FriendCommand extends MainCommand
{
    private static Cache<UUID, UUID> friendRequest;

    public FriendCommand(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_FRIENDS, CommandPermission.FRIEND, 1);
        child.add(new FriendListCommand(plugin));
        child.add(new FriendAddCommand(plugin));
        child.add(new FriendRemoveCommand(plugin));
        child.add(new FriendAcceptCommand(plugin));
        friendRequest = CacheBuilder.newBuilder().expireAfterWrite(plugin.getConfig().getLong("cache.friends"), TimeUnit.MINUTES).build();
    }

    @Override
    public String getLabel()
    {
        return "friends";
    }



    public static void newRequest(MinetasiaCore plugin, UUID uuid, UUID uuid1, Player p)
    {
        friendRequest.put(uuid, uuid1);
        TextComponent c = new TextComponent(Lang.REQUEST_FRIEND.get(plugin.getPlayerLang(uuid), Lang.Argument.PLAYER.match(plugin.getPlayerName(uuid1))));
        c.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "f accept"));
        p.spigot().sendMessage(c);
    }

    public static Cache<UUID, UUID> getFriendRequest()
    {
        return friendRequest;
    }

    public static int getMaxFriends(Player player)
    {
        int size = 2;

        final String perm = CommandPermission.MAX_FRIEND.getPermission() + ".";

        for (PermissionAttachmentInfo effectivePermission : player.getEffectivePermissions())
        {
            if(effectivePermission.getPermission().startsWith(perm))
            {
                try
                {
                    size = Math.max(size, Integer.parseInt(effectivePermission.getPermission().substring(perm.length())));
                }
                catch (NumberFormatException ignore) {}
            }
        }
        return size;
    }

}
