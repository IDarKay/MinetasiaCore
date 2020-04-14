package fr.idarkay.minetasia.core.spigot.settings;

import fr.idarkay.minetasia.core.api.MongoCollections;
import fr.idarkay.minetasia.core.api.SettingsKey;
import fr.idarkay.minetasia.core.api.event.SettingsUpdateEvent;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * File <b>SettingsManager</b> located on fr.idarkay.minetasia.core.spigot.settings
 * SettingsManager is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 30/03/2020 at 21:39
 */
public class SettingsManager
{

    private final MinetasiaCore plugin;
    private final HashMap<SettingsKey<?>, Settings<?>> settings = new HashMap<>();

    public SettingsManager(MinetasiaCore plugin)
    {
        this.plugin  = plugin;
        for (Document document : plugin.getMongoDbManager().getAll(MongoCollections.SETTINGS))
        {
            final Integer id = document.getInteger("_id");
            if(id == null)
            {
                throw new IllegalArgumentException("no id !");
            }
            final SettingsKey<?> tmp = SettingsKey.fromHash(id);
            if(tmp == null)
            {
                continue;
            }

            loadSetting(tmp, document);
        }
    }

    private <T> void loadSetting(SettingsKey<T> key, Document document)
    {
        settings.put(key, new Settings<T>(plugin, key, key.getClazz(), document.get("value", key.getClazz())));
    }

    @NotNull
    public <T> Settings<T> getSettings(SettingsKey<T> key)
    {
        Settings<?> minetasiaSettings = settings.get(key);
        if(minetasiaSettings == null)
        {
            final Settings<T> s = new Settings<T>(plugin, key, key.getClazz(), null);
            settings.put(key, s);
            return s;
        }
        return (Settings<T>) minetasiaSettings;
    }

    public void Update(String json)
    {
        final Document document = Document.parse(json);
        final Integer id = document.getInteger("_id");
        if(id == null)
        {
            throw new IllegalArgumentException("no id !");
        }
        final SettingsKey<?> tmp = SettingsKey.fromHash(id);
        if(tmp == null)
        {
            throw new IllegalArgumentException("the hashKey " + id + "is invalid !");
        }
        completeUpdate(tmp, document);

    }

    private <T> void completeUpdate(SettingsKey<T> tmp, Document document)
    {
        final Settings<T> settings = getSettings(tmp);
        T value = document.get("value",tmp.getClazz());
        settings.setValueLocal(value);
        plugin.getServer().getPluginManager().callEvent(new SettingsUpdateEvent<>(tmp, value));
    }


}
