package fr.idarkay.minetasia.core.spigot.commands.miscellaneous;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.command.abstraction.Command;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * File <b>Help</b> located on fr.idarkay.minetasia.core.spigot.commands.miscellaneous
 * Help is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 14/03/2020 at 18:34
 */
public class Help extends Command
{

    public Help(@NotNull MinetasiaCore plugin)
    {
        super(plugin, Lang.DESC_HELP, CommandPermission.HELP, 0);
        child.add(this);
    }

    @Override
    public String getLabel()
    {
        return "help";
    }

    private static final int MAX_ITEM_PER_PAGES =  16;

    @Override
    public void execute(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label)
    {
        final String lang = getLangOfSender(sender);
        if(args.length > 0 && sender.hasPermission(CommandPermission.HELP_STAFF.getPermission()))
        {
            try
            {
                final int page = Integer.parseInt(args[0]) - 1;
                if(page < 0)
                {
                    sender.sendMessage(Lang.ILLEGAL_NUMBER_VALUE.get(lang));
                }
                else
                {
                    // commands description
                    final List<HelpTopic> helpTopics = plugin.getServer().getHelpMap().getHelpTopics().stream().filter(t -> t.canSee(sender) && !t.getShortText().contains("Alias for")).collect(Collectors.toList()); //remove commands of player can't use and alias

                    final long length = helpTopics.size();

                    final Map<String, String> finalCommand = helpTopics.stream()
                            .skip(Math.min(page * MAX_ITEM_PER_PAGES, length))
                            .limit(MAX_ITEM_PER_PAGES)
                            .collect(Collectors.toMap(HelpTopic::getName, HelpTopic::getShortText));

                    final int max_pages = (int) Math.ceil(length / (float) MAX_ITEM_PER_PAGES);

                    //return to first pages if no item found
                    if(finalCommand.size() == 0 && page != 0)
                    {
                        execute(plugin, sender, new String[] {"1"}, label);
                        return;
                    }

                    final String border = Lang.STAFF_HELP_BORDER.getWithoutPrefix(lang, Lang.Argument.PAGE.match(page + 1), Lang.Argument.MAX_PAGE.match(max_pages));
                    sender.sendMessage(border);
                    sender.sendMessage(" ");

                    final String format = Lang.STAFF_HELP_FORMAT.getWithoutPrefix(lang);
                    finalCommand.forEach((k, v) -> sender.sendMessage(format.replace(Lang.Argument.COMMAND.toString(), k).replace(Lang.Argument.DESCRIPTION.toString(), v)));
                    sender.sendMessage(" ");
                    sender.sendMessage(border);
                    return;
                }
            }
            catch (NumberFormatException e)
            {
                sender.sendMessage(Lang.ILLEGAL_NUMBER_VALUE.get(lang));
            }
        }
        final String[] msg = Lang.USER_HELP.getWithSplit(lang);

        for (String s : msg)
        {
            sender.sendMessage(s);
        }
    }

    /**
     *
     * @param player player to check if has the permission
     * @param permission the permission can be null
     * @return true if permission == null or if player have the permission
     */
    private boolean hasPermissionIfNotNull(@NotNull CommandSender player, @Nullable String permission)
    {
        return permission == null || player.hasPermission(permission);
    }

    private String getPermissionOrEmptyIfNull(@Nullable Object desc)
    {
        return desc == null ? " " : desc.toString();
    }

    @Override
    public List<String> tabComplete(@NotNull MinetasiaCore plugin, @NotNull CommandSender sender, @NotNull String[] args, @NotNull String label)
    {
        if(sender.hasPermission(CommandPermission.HELP_STAFF.getPermission()))
        {
            return Collections.singletonList("<pages>");
        }
        return Collections.emptyList();
    }
}
