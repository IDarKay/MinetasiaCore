package fr.idarkay.minetasia.core.bungee.settings;

import fr.idarkay.minetasia.core.bungee.MinetasiaCoreBungee;
import fr.idarkay.minetasia.core.bungee.MongoCollections;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * File <b>SettingsManager</b> located on fr.idarkay.minetasia.core.spigot.settings
 * SettingsManager is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 30/03/2020 at 21:39
 */
public class SettingsManager
{

    private final MinetasiaCoreBungee plugin;
    private final HashMap<SettingsKey<?>, Settings<?>> settings = new HashMap<>();

    public SettingsManager(MinetasiaCoreBungee plugin)
    {
        this.plugin  = plugin;
        for (Document document : plugin.getMongoDBManager().getAll(MongoCollections.SETTINGS))
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
        onUpdate(tmp, value);
    }

    private <T> void onUpdate(SettingsKey<T> key, T value)
    {
        if(key.equals(SettingsKey.WHITELIST))
        {
            plugin.getWhitelist().clear();
            if(value != null)
                plugin.getWhitelist().addAll(((List<Document>) value).stream().map(document -> UUID.fromString(document.getString("uuid"))).collect(Collectors.toList()));
        }
        else if(key.equals(SettingsKey.MAINTENANCE))
        {
            plugin.getMaintenanceServer().clear();
            if(value != null)
                plugin.getMaintenanceServer().addAll(((List<String>) value));
        }
        else if(key.equals(SettingsKey.MOTD))
        {
            plugin.getPlayerListener().setMOTD((String) value);
        }
    }


}
