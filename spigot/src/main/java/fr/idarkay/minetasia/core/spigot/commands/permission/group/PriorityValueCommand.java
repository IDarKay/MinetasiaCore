package fr.idarkay.minetasia.core.spigot.commands.permission.group;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.FlexibleCommand;
import fr.idarkay.minetasia.core.spigot.command.abstraction.SubCommand;
import fr.idarkay.minetasia.core.spigot.permission.Group;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * File <b>PriorityValueCommand</b> located on fr.idarkay.minetasia.core.spigot.commands.permission.group
 * PriorityValueCommand is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 01/12/2019 at 11:24
 */
public final class PriorityValueCommand extends SubCommand implements FlexibleCommand {

    private final static List<String> POSSIBILITY;

    static {
        POSSIBILITY = new ArrayList<>();
        for(int i = -127 ; i < 128; i++) POSSIBILITY.add(String.valueOf(i));
    }

    public PriorityValueCommand(@NotNull MinetasiaCore plugin) {
        super(plugin, Lang.DESC_PERMISSION_GROUP_PRIORITY_VALUE, CommandPermission.PERMISSION_GROUP_PRIORITY, 5);
    }

    @Override
    public String getLabel() {
        return "<priority [-127 ; 127]";
    }

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label) {
        final String lang = getLangOfSender(sender);
        if(sender.hasPermission(permission.getPermission()))
        {
            Group g = plugin.getPermissionManager().groups.get(args[1]);
            if(g != null)
            {
                try
                {
                    byte i = Byte.parseByte(args[3]);
                    g.setPriority(i);
                    sender.sendMessage(Lang.GROUP_PRIORITY_CHANGE.get(lang, args[1], i));
                } catch (IllegalArgumentException ignore)
                {
                    sender.sendMessage(Lang.ILLEGAL_NUMBER_VALUE.get(lang));
                }
            }
            else sender.sendMessage(Lang.GROUP_NOT_EXIST.get(lang));
        }
        else sender.sendMessage(Lang.NO_PERMISSION.get(lang));
    }

    @Override
    public boolean isAllPossibilities() {
        return false;
    }

    @Override
    public List<String> getPossibilities() {
        return POSSIBILITY;
    }
}
