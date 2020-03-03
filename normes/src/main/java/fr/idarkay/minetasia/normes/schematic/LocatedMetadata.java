package fr.idarkay.minetasia.normes.schematic;

import org.bukkit.Location;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * File <b>LocatedMetadata</b> located on fr.idarkay.minetasia.normes.schematic
 * LocatedMetadata is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 02/03/2020 at 18:46
 */
public class LocatedMetadata
{

    private final Location location;
    private final String data;

    public LocatedMetadata(@NotNull Location location, @NotNull String data)
    {
        this.location = location;
        this.data = data;
    }

    public void apply(JavaPlugin plugin)
    {
        location.getBlock().setMetadata("schem", new FixedMetadataValue(plugin, data));
    }

    public String getData()
    {
        return data;
    }

    public Location getLocation()
    {
        return location;
    }
}
