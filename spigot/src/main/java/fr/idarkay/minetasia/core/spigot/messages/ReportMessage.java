package fr.idarkay.minetasia.core.spigot.messages;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.Args;
import fr.idarkay.minetasia.normes.Tuple;
import fr.idarkay.minetasia.normes.component.event.ClickEventType;
import fr.idarkay.minetasia.normes.component.event.hover.ShowTextHoverEvent;
import fr.idarkay.minetasia.normes.utils.BukkitUtils;
import fr.idarkay.minetasia.normes.utils.GeneralUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>ReportMessage</b> located on fr.idarkay.minetasia.core.spigot.messages
 * ReportMessage is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 10/04/2020 at 10:29
 */
public class ReportMessage extends CoreMessage
{
    public ReportMessage()
    {
        super(getIdentifier());
    }

    @Override
    public void actionOnGet(MinetasiaCore plugin, String... args)
    {
        String arg = GeneralUtils.concat(args, " ", 4);
        arg = arg.isEmpty() ? "" : "[" + arg + "]";

        final Tuple<? extends Args, Object>[] argument = new Tuple[] {
                Lang.Argument.PLAYER.match(args[1]),
                Lang.Argument.TARGET.match(args[2]),
                Lang.Argument.REASON.match(args[3]),
                Lang.Argument.ARGUMENT.match(arg),
                };

        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            if(onlinePlayer.hasPermission(CommandPermission.REPORT_VIEWER.getPermission()))
            {
                final String lang = plugin.getPlayerLang(onlinePlayer.getUniqueId());
                BukkitUtils.sendToPlayer(onlinePlayer,
                        Lang.REPORT_WAR_MESSAGE.getWithoutPrefixToBaseComponent(lang, argument)
                                .setClickEvent(ClickEventType.RUN_COMMAND, "/tp " + args[2])
                                .setHoverEvent(new ShowTextHoverEvent(Lang.HOVER_CLICK_FOR_TELEPORT.getWithoutPrefix(lang)))
                );
            }
        }
    }

    public static @NotNull String getMessage(@NotNull String sender, @NotNull String targetName, @NotNull String reportType, @NotNull String... args)
    {
        Validate.noNullElements(args);
        return CoreMessage.getMessage(getIdentifier(), GeneralUtils.concat(new Object[]{sender, targetName, reportType},  (Object[]) args));
    }

    public static @NotNull String getIdentifier()
    {
        return "core-report";
    }

}
