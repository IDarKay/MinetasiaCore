package fr.idarkay.minetasia.core.spigot.utils;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * File <b>MongoDBManager</b> located on fr.idarkay.minetasia.core.spigot.utils
 * MongoDBManager is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 09/02/2020 at 15:32
 */
public class MongoDBManager
{

    private final Plugin plugin;
    @NotNull
    private final String host, dbname, user, pass; // connection information

    private final MongoClient mongoClient; // connection of the database

    public MongoDBManager(Plugin plugin)
    {
        this.plugin = plugin;
        host = Objects.requireNonNull(plugin.getConfig().getString("dbm.host"));
        dbname = Objects.requireNonNull(plugin.getConfig().getString("dbm.dbname"));
        user = Objects.requireNonNull(plugin.getConfig().getString("dbm.login"));
        pass = Objects.requireNonNull(plugin.getConfig().getString("dbm.password"));

        final MongoCredential credential = MongoCredential.createCredential(user, dbname, pass.toCharArray());
        final ConnectionString connectionString = new ConnectionString(host);
        final MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToSslSettings(builder -> builder.enabled(true))
                .applyConnectionString(connectionString)
                .build();

        this.mongoClient = MongoClients.create(settings);

        MongoDatabase database = mongoClient.getDatabase(dbname);


    }

}
