package fr.idarkay.minetasia.core.spigot.moderation.report;

import fr.idarkay.minetasia.normes.utils.Message;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * File <b>ReportArgs</b> located on fr.idarkay.minetasia.core.spigot.moderation.report
 * ReportArgs is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 08/04/2020 at 15:05
 */
public class ReportArgs
{

    private final Message display;
    private final String generic;

    public ReportArgs(@NotNull FileConfiguration configuration, @NotNull String path)
    {
        this.generic = path.split("\\.")[3];
        this.display = new Message(Objects.requireNonNull(configuration.getString(path + ".key")), Objects.requireNonNull(configuration.getString(path + ".default")));
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
