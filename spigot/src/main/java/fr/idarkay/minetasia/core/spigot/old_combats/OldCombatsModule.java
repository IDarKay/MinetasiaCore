package fr.idarkay.minetasia.core.spigot.old_combats;

import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.utils.EventListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * File <b>OldCombatsModule</b> located on fr.idarkay.minetasia.core.spigot.old_combats
 * OldCombatsModule is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 14/04/2020 at 15:27
 */
public abstract class OldCombatsModule implements EventListener
{

    private static final FileConfiguration fileConfiguration = JavaPlugin.getPlugin(MinetasiaCore.class).getConfig();

    private final boolean enable;

    public OldCombatsModule(String configSection)
    {
        enable = fileConfiguration.getBoolean("old_combats_module." + configSection);
    }

    public final boolean isEnable()
    {
        return enable;
    }
}
