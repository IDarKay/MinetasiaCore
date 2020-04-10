package fr.idarkay.minetasia.core.spigot.moderation.report;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * File <b>ReportManager</b> located on fr.idarkay.minetasia.core.spigot.moderation.report
 * ReportManager is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 08/04/2020 at 15:06
 */
public class ReportManager
{

    private final Map<String, ReportType> reportTypeList;

    public ReportManager(FileConfiguration fileConfiguration)
    {
        reportTypeList = fileConfiguration.getConfigurationSection("report").getKeys(false)
                .stream().map(section -> new ReportType(fileConfiguration, "report." + section)).collect(Collectors.toMap(ReportType::getGeneric, reportType -> reportType));
    }

    public  Map<String, ReportType> getReportTypeList()
    {
        return reportTypeList;
    }


}
