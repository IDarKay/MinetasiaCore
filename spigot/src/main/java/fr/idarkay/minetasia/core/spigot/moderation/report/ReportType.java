package fr.idarkay.minetasia.core.spigot.moderation.report;

import fr.idarkay.minetasia.normes.utils.Message;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * File <b>ReportType</b> located on fr.idarkay.minetasia.core.spigot.moderation.report
 * ReportType is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 08/04/2020 at 15:03
 */
public class ReportType
{

    private final Map<String, ReportArgs> args;
    private final boolean isNoArgs;
    private final Message display;
    private final String generic;

    public ReportType(@NotNull FileConfiguration configuration, @NotNull String path)
    {
        if(configuration.contains(path + ".args"))
            args = configuration.getConfigurationSection(path + ".args").getKeys(false).stream().map(section -> new ReportArgs(configuration, path + ".args." + section)).collect(Collectors.toMap(ReportArgs::getGeneric, reportArgs -> reportArgs));
        else args = new HashMap<>();
        isNoArgs = args.size() == 0;
        this.generic = path.split("\\.")[1];
        this.display = new Message(Objects.requireNonNull(configuration.getString(path + ".display.key")), Objects.requireNonNull(configuration.getString(path + ".display.default")));
    }

    public Map<String, ReportArgs> getArgs()
    {
        return args;
    }

    public boolean isNoArgs()
    {
        return isNoArgs;
    }

    public Message getDisplay()
    {
        return display;
    }

    public String getGeneric()
    {
        return generic;
    }
}
