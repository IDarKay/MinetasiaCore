package fr.idarkay.minetasia.core.spigot.settings;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import fr.idarkay.minetasia.core.api.MongoCollections;
import fr.idarkay.minetasia.core.api.SettingsKey;
import fr.idarkay.minetasia.core.api.utils.MinetasiaSettings;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.messages.CoreMessage;
import fr.idarkay.minetasia.core.spigot.messages.SettingsUpdate;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * File <b>Settings</b> located on fr.idarkay.minetasia.core.spigot.settings
 * Settings is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 31/03/2020 at 15:04
 */
public final class Settings<T> implements MinetasiaSettings<T>
{
    @NotNull
    private final MinetasiaCore plugin;
    @NotNull
    private final SettingsKey<T> settingsKey;
    @NotNull
    private final Class<T> clazz;
    @Nullable
    private T value = null;

    public Settings(@NotNull MinetasiaCore plugin, @NotNull SettingsKey<T> settingsKey, @NotNull Class<T> clazz, @Nullable T value)
    {
        this.plugin = plugin;
        this.clazz = clazz;
        this.settingsKey = settingsKey;
        this.value = value;
    }

    @Nullable
    @Override
    public T getValue()
    {
        return value;
    }

    @Override
    public void setValue(@NotNull T value)
    {
        setValueLocal(value);
        final Document doc = toDocument();
        plugin.getMongoDbManager().getCollection(MongoCollections.SETTINGS).replaceOne(Filters.eq(settingsKey.getHash()), doc, new ReplaceOptions().upsert(true));
        plugin.publishGlobal(CoreMessage.CHANNEL, SettingsUpdate.getMessage(doc.toJson()), true, false);
    }

    @Override
    public void setValueLocal(@NotNull T value)
    {
        this.value = value;
    }

    @NotNull
    @Override
    public Class<T> getClazz()
    {
        return clazz;
    }

    private Document toDocument()
    {
        if(value == null) throw new IllegalArgumentException("can't save null value !");
        return new Document("_id", settingsKey.getHash()).append("_comment", "key name " + settingsKey.name()).append("value", value);
    }

}
