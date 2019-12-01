package fr.idarkay.minetasia.core.spigot.commands.permission.common;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FixCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.StepCommand;
import fr.idarkay.minetasia.core.spigot.permission.Group;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * File <b>StepRemoveCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.common
 * StepRemoveCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 01/12/2019 at 12:08
 */
public final class StepRemoveCommand extends StepCommand implements FixCommand {

    private final Type type;

    public StepRemoveCommand(@NotNull MinetasiaCore plugin, @Nullable Lang description, @NotNull CommandPermission permission, int length, Type type) {
        super(plugin, description, permission, length);
        this.type = type;
    }

    @Override
    public String getLabel() {
        return "remove";
    }

    @Override
    public List<String> tabComplete(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        if(sender.hasPermission(permission.getPermission()))
        {
            List<String> back = getBasicTabCompleter(sender, args);

            if(type.equals(Type.GROUP))
            {
                Group g = plugin.getPermissionManager().groups.get(args[1]);
                if(g != null)
                {
                    back.addAll(g.getPermissions());
                }
            } else if(type.equals(Type.USER)){
                Player u = Bukkit.getPlayer(args[1]);
                if (u != null)
                {
                    if(plugin.getPermissionManager().permissionAttachments.containsKey(u.getUniqueId()))
                    {
                        for(Map.Entry<String, PermissionAttachment> e : plugin.getPermissionManager().permissionAttachments.get(u.getUniqueId()).entrySet())
                        {
                            if(e.getKey().startsWith("permission") || e.getKey().startsWith("temp_permission"))
                            {
                                back.addAll(e.getValue().getPermissions().keySet());
                            }
                        }
                    }
                }
            }
            return back;
        } else return Collections.emptyList();
    }

    public enum Type{
        GROUP, USER;
    }

}
