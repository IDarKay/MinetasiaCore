package fr.idarkay.minetasia.core.spigot.moderation.report;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.books.MinetasiaBook;
import fr.idarkay.minetasia.normes.books.MinetasiaBookPages;
import fr.idarkay.minetasia.normes.component.BaseComponent;
import fr.idarkay.minetasia.normes.component.TextComponent;
import fr.idarkay.minetasia.normes.component.event.ClickEventType;
import fr.idarkay.minetasia.normes.component.event.hover.ShowTextHoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * File <b>TypeReportBook</b> located on fr.idarkay.minetasia.core.spigot.moderation.report
 * TypeReportBook is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 08/04/2020 at 22:59
 */
public class TypeReportBook extends MinetasiaBook
{
    private static final MinetasiaCore CORE = JavaPlugin.getPlugin(MinetasiaCore.class);

    public TypeReportBook(@NotNull String lang, @NotNull String target, @NotNull ReportType reportType, @NotNull List<String> validate)
    {
        final List<BaseComponent> content = new ArrayList<>();
        content.add(Lang.REPORT.getWithoutPrefixToBaseComponent(lang, Lang.Argument.PLAYER.match(target)));
        content.add(new TextComponent("\n"));

        final String format = Lang.REPORT_TYPE_FORMAT.getWithoutPrefix(lang);
        final String hover = Lang.REPORT_HOVER_ARGS.getWithoutPrefix(lang);



        reportType.getArgs().forEach((generic, args) -> {
            final List<String> validateClone = new ArrayList<>(validate);
            if(validate.contains("--" + generic))
            {
                validateClone.remove("--" + generic);
            }
            else
            {
                validateClone.add("--" + generic);
            }
            final StringBuilder validateBuilder = new StringBuilder();
            validateClone.forEach(val -> validateBuilder.append(val).append(" "));

            final String display = CORE.getMinetasiaLang().getFromMessage(lang, args.getDisplay());
            content.add(new TextComponent(format.replace("{report_type}", display) + "\n").setBold(true).setChatColor(validate.contains("--" + generic) ? ChatColor.GREEN : ChatColor.BLACK)
                    .setClickEvent(ClickEventType.RUN_COMMAND, "/report " + target + " " + reportType.getGeneric() + " " + validateBuilder)
                    .setHoverEvent(new ShowTextHoverEvent(hover.replace("{report_type}", display))));
        });

        content.add(new TextComponent("\n\n"));
        final StringBuilder validateBuilder = new StringBuilder();
        validate.forEach(val -> validateBuilder.append(val).append(" "));

        final String validateSting = Lang.REPORT_VALIDATE.getWithoutPrefix(lang, Lang.Argument.PLAYER.match(target));
        content.add(new TextComponent(validateSting + "\n").setHoverEvent(new ShowTextHoverEvent(validateSting)).setClickEvent(ClickEventType.RUN_COMMAND, "/report " + target + " " + reportType.getGeneric() + " " + validateBuilder + "--validate"));

        final String cancelString = Lang.REPORT_CANCEL.getWithoutPrefix(lang);
        content.add(new TextComponent(cancelString).setHoverEvent(new ShowTextHoverEvent(cancelString)).setClickEvent(ClickEventType.RUN_COMMAND, "/report stop"));

        withPages(new MinetasiaBookPages(content));

    }
}
