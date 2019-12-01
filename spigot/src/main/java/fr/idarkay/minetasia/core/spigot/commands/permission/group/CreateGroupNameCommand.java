package fr.idarkay.minetasia.core.spigot.commands.permission.group;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.permission.PermissionManager;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * File <b>CreateGroupNameCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.group
 * CreateGroupNameCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 30/11/2019 at 22:46
 */
public final class CreateGroupNameCommand extends SubCommand implements FlexibleCommand {

    private final PermissionManager permissionManager;
    private final static int rank = 2;

    public CreateGroupNameCommand(MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_GROUP_CREATE_NAME, CommandPermission.PERMISSION_GROUP_CREATE, 4);
        this.permissionManager = plugin.getPermissionManager();
    }

    @Override
    public String getLabel() {
        return "<name>";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        final String lang = sender instanceof Player ? plugin.getPlayerLang(((Player) sender).getUniqueId()) : MinetasiaLang.BASE_LANG;
        if(!permissionManager.groups.containsKey(args[rank]))
        {
            if(args[rank].length() > 1)
            {
                permissionManager.createGroup(args[rank]);
                sender.sendMessage(Lang.GROUP_CREATE.get(lang, args[rank]));
            }
            else sender.sendMessage(Lang.GROUP_NO_ENOUGH_CHAR.get(lang, 2));
        }
        else sender.sendMessage(Lang.GROUP_ALREADY_EXIST.get(lang, args[rank]));
    }

    @Override
    public List<String> tabComplete(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        if(sender.hasPermission(permission.getPermission()))
            return Collections.singletonList(getLabel());
        return Collections.emptyList();
    }

    @Override
    public boolean isAllPossibilities() {
        return true;
    }

    @Override
    public List<String> getPossibilities() {
        return null;
    }
}
