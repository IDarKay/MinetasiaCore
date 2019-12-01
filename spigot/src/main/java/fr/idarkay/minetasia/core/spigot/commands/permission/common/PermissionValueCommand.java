package fr.idarkay.minetasia.core.spigot.commands.permission.common;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.permission.Group;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * File <b>PermissionValueCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.common
 * PermissionValueCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 01/12/2019 at 12:31
 */
public final class PermissionValueCommand extends SubCommand implements FlexibleCommand {

    private final Type type;

    public PermissionValueCommand(@NotNull MinetasiaCore plugin, @Nullable Lang description, @NotNull CommandPermission permission, int length, Type type) {
        super(plugin, description, permission, length);
        this.type = type;
    }

    @Override
    public String getLabel() {
        return "<permission>";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull  String[] args, @NotNull String label) {
        final String lang = getLangOfSender(sender);
        String permission = args[length - 2];
        if(permission.length() == 0)
        {
            sender.sendMessage(Lang.GROUP_NO_ENOUGH_CHAR.get(lang, "1"));
            return;
        }
        if(type.superT.equals(Type.GROUP))
        {
            Group g = plugin.getPermissionManager().groups.get(args[1]);
            if(g != null)
            {
                if(type.equals(Type.GROUP_ADD))
                {
                    g.addPermissions(permission);
                    sender.sendMessage(Lang.GROUP_PERMISSION_ADD.get(lang, permission, args[1]));
                }
                else
                {
                    if(g.removePermissions(permission)) sender.sendMessage(Lang.GROUP_PERMISSION_REMOVE.get(lang, permission, args[1]));
                    else sender.sendMessage(Lang.GROUP_PERMISSION_CANT_REMOVE.get(lang, args[1], permission));
                }
            }
            else sender.sendMessage(Lang.GROUP_NOT_EXIST.get(lang));
        }
        else
        {
            UUID u = plugin.getPlayerUUID(args[1]);
            if(u != null)
            {
                if(type.equals(Type.USER_ADD))
                {
                    plugin.getPermissionManager().addPermission(u, permission);
                    sender.sendMessage(Lang.USER_PERMISSION_ADD.get(lang, permission, args[1]));
                }
                else
                {
                    plugin.getPermissionManager().removePermission(u, permission);
                    plugin.getPermissionManager().removeTempPermission(u, permission);
                    sender.sendMessage(Lang.USER_PERMISSION_REMOVE.get(lang, permission, args[1]));
                }
            }
            else sender.sendMessage(Lang.PLAYER_NOT_EXIST.get(lang));
        }
    }

    @Override
    public boolean isAllPossibilities() {
        return true;
    }

    @Override
    public List<String> getPossibilities() {
        return null;
    }

    public enum Type{
        USER,
        GROUP,
        USER_ADD(USER),
        USER_REMOVE(USER),
        GROUP_ADD(GROUP),
        GROUP_REMOVE(GROUP),
        ;

        public Type superT;

        Type()
        {
            superT = this;
        }

        Type(Type superT)
        {
            this.superT = superT;
        }

    }

}
