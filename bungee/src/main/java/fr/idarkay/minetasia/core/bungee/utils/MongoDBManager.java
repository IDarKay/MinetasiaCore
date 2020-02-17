package fr.idarkay.minetasia.core.bungee.utils;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import fr.idarkay.minetasia.core.bungee.MongoCollections;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * File <b>MongoDBManager</b> located on fr.idarkay.minetasia.core.spigot.utils
 * MongoDBManager is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 09/02/2020 at 15:32
 */
public class MongoDBManager
{

    private final MongoClient mongoClient; // connection of the database
    private final MongoDatabase database;

    public MongoDBManager(String host, String dbname, String user, String pass)
    {
        final MongoCredential credential = MongoCredential.createCredential(user, dbname, pass.toCharArray());
        final ConnectionString connectionString = new ConnectionString(host);
        final MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToSslSettings(builder -> builder.enabled(true))
                .applyConnectionString(connectionString)
                .build();

        this.mongoClient = MongoClients.create(host);

        this.database = mongoClient.getDatabase(dbname);
    }

    public FindIterable<Document> getAll(MongoCollections collection)
    {
        return getCollection(collection).find();
    }

    public Document getByKey(MongoCollections collection, String key)
    {
        return getCollection(collection).find(Filters.eq(key)).first();
    }

    public FindIterable<Document> getSimpleFilter(MongoCollections collection, String filterKey, String filterValue)
    {
        return getCollection(collection).find(Filters.eq(filterKey, filterValue));
    }

    public AggregateIterable<Document> getWithReference(MongoCollections collection, String from, String localField)
    {
        return getWithReference(collection, from, localField, "_id", from);
    }

    public AggregateIterable<Document> getWithReference(MongoCollections collection, String from, String localField, String foreignField, String as)
    {
        return getCollection(collection).aggregate(Collections.singletonList( new Document("$lookup"
                , new Document().append("from", from).append("localField", localField).append("foreignField", foreignField).append("as", as)
        )));
    }

    public AggregateIterable<Document> getWithReferenceAndMatch(MongoCollections collection, String filterKey, String filterValue, String from, String localField, String foreignField, String as)
    {
        return getCollection(collection).aggregate(Arrays.asList(
                new Document("$match"
                        , new Document(filterKey, filterValue)),
                new Document("$lookup"
                        , new Document().append("from", from).append("localField", localField).append("foreignField", foreignField).append("as", as)
        )));
    }

    public boolean match(MongoCollections collection, String id)
    {
        return getCollection(collection).countDocuments(Filters.eq(id)) > 0;
    }

    public boolean match(MongoCollections collection, String key, String value)
    {
        return getCollection(collection).countDocuments(Filters.eq(key, value)) > 0;
    }

    public void insert(MongoCollections collection, String json)
    {
        insert(collection, Document.parse(json));
    }

    public void insert(MongoCollections collection, Document document)
    {
        getCollection(collection).insertOne(document);
    }

    public void insertJsonList(MongoCollections collections, List<String> json)
    {
        insert(collections, json.stream().map(Document::parse).collect(Collectors.toList()));
    }

    public void insert(MongoCollections collections, List<Document> documents)
    {
        getCollection(collections).insertMany(documents);
    }

    public void replace(MongoCollections collections, String key, String json)
    {
        replace(collections, key, Document.parse(json));
    }

    public void replace(MongoCollections collections, String key, Document document)
    {
        getCollection(collections).replaceOne(Filters.eq(key), document);
    }

    public void delete(MongoCollections collections, String key)
    {
        getCollection(collections).deleteOne(Filters.eq(key));
    }

    public void insertOrReplaceIfExist(MongoCollections collections, String key, Document document)
    {
        getCollection(collections).updateOne(Filters.eq(key), document, new UpdateOptions().upsert(true));
    }

    @NotNull
    public MongoCollection<Document> getCollection(MongoCollections collections)
    {
        return database.getCollection(collections.name);
    }

    public MongoDatabase getDatabase()
    {
        return database;
    }

    public void close()
    {
        mongoClient.close();
    }

}
