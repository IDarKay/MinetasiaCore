package fr.idarkay.minetasia.core.spigot.moderation;

import fr.idarkay.minetasia.core.api.SettingsKey;
import fr.idarkay.minetasia.core.api.utils.MinetasiaSettings;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * File <b>SanctionManger</b> located on fr.idarkay.minetasia.core.spigot.moderation
 * SanctionManger is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 03/04/2020 at 16:46
 */
public class SanctionManger
{

    @NotNull
    private final MinetasiaCore plugin;

    private final Map<String, Sanction> sanctionMap = new HashMap<>();

    public SanctionManger(@NotNull final MinetasiaCore plugin)
    {
        this.plugin = plugin;
        final MinetasiaSettings<List> settings = plugin.getSettings(SettingsKey.SANCTION);
        final List<Document> value = settings.getValue();
        if(value == null) return;
        value.forEach(document ->
        {
            final Sanction sanction = new Sanction(document);
            sanctionMap.put(sanction.getGenericName().toLowerCase(Locale.ENGLISH), sanction);
        });
    }

    @Nullable
    public Sanction getSanction(String generic_name)
    {
        return sanctionMap.get(generic_name.toLowerCase(Locale.ENGLISH));
    }

    public Set<String> getSanctions()
    {
        return sanctionMap.keySet();
    }

}
