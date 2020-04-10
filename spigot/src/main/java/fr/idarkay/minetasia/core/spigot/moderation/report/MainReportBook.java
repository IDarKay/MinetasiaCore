package fr.idarkay.minetasia.core.spigot.moderation.report;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.normes.books.MinetasiaBook;
import fr.idarkay.minetasia.normes.books.MinetasiaBookPages;
import fr.idarkay.minetasia.normes.component.BaseComponent;
import fr.idarkay.minetasia.normes.component.TextComponent;
import fr.idarkay.minetasia.normes.component.event.ClickEventType;
import fr.idarkay.minetasia.normes.component.event.hover.ShowTextHoverEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * File <b>ReportBook</b> located on fr.idarkay.minetasia.core.spigot.moderation.report
 * ReportBook is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 08/04/2020 at 15:06
 */
public class MainReportBook extends MinetasiaBook
{

    private static final MinetasiaCore CORE = JavaPlugin.getPlugin(MinetasiaCore.class);

    public MainReportBook(@NotNull String lang, @NotNull String target)
    {
        final List<BaseComponent> content = new ArrayList<>();
        content.add(Lang.REPORT.getWithoutPrefixToBaseComponent(lang, Lang.Argument.PLAYER.match(target)));
        content.add(new TextComponent("\n\n"));
        final String format = Lang.REPORT_TYPE_FORMAT.getWithoutPrefix(lang);
        final String hover = Lang.REPORT_HOVER.getWithoutPrefix(lang, Lang.Argument.PLAYER.match(target));

        CORE.getReportManager().getReportTypeList().forEach((generic, type) -> {
            final String display = CORE.getMinetasiaLang().getFromMessage(lang, type.getDisplay());
            content.add(new TextComponent(format.replace("{report_type}", display) + "\n").setBold(true)
                    .setClickEvent(ClickEventType.RUN_COMMAND, "/report " + target + " " + generic)
                    .setHoverEvent(new ShowTextHoverEvent(hover.replace("{report_type}", display))));

        });
        withPages(new MinetasiaBookPages(content));
    }

}
