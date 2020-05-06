package fr.idarkay.minetasia.core.spigot.utils;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import fr.idarkay.minetasia.core.api.MongoCollections;
import fr.idarkay.minetasia.core.api.utils.MongoDbManager;
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
 * @author alice. B. (IDarKay),
 * Created the 09/02/2020 at 15:32
 */
public class MongoDBManager implements MongoDbManager
{

    private final MongoClient mongoClient; // connection of the database
    private final MongoDatabase database;

    public MongoDBManager(String host, String dbname)
    {

        this.mongoClient = MongoClients.create(host);

        this.database = mongoClient.getDatabase(dbname);
    }

    @Deprecated
    public FindIterable<Document> getAll(MongoCollections collection)
    {
        return getCollection(collection).find();
    }

    @Deprecated
    public Document getByKey(MongoCollections collection, String key)
    {
        return getCollection(collection).find(Filters.eq(key)).first();
    }

    @Deprecated
    public FindIterable<Document> getSimpleFilter(MongoCollections collection, String filterKey, String filterValue)
    {
        return getCollection(collection).find(Filters.eq(filterKey, filterValue));
    }

    @Deprecated
    public AggregateIterable<Document> getWithReference(MongoCollections collection, String from, String localField)
    {
        return getWithReference(collection, from, localField, "_id", from);
    }

    @Deprecated
    public AggregateIterable<Document> getWithReference(MongoCollections collection, String from, String localField, String foreignField, String as)
    {
        return getCollection(collection).aggregate(Collections.singletonList( new Document("$lookup"
                , new Document().append("from", from).append("localField", localField).append("foreignField", foreignField).append("as", as)
        )));
    }

    @Deprecated
    public AggregateIterable<Document> getWithReferenceAndMatch(MongoCollections collection, String filterKey, String filterValue, String from, String localField, String foreignField, String as)
    {
        return getCollection(collection).aggregate(Arrays.asList(
                new Document("$match"
                        , new Document(filterKey, filterValue)),
                new Document("$lookup"
                        , new Document().append("from", from).append("localField", localField).append("foreignField", foreignField).append("as", as)
        )));
    }

    @Deprecated
    public boolean match(MongoCollections collection, String id)
    {
        return getCollection(collection).countDocuments(Filters.eq(id)) > 0;
    }

    @Deprecated
    public boolean match(MongoCollections collection, String key, String value)
    {
        return getCollection(collection).countDocuments(Filters.eq(key, value)) > 0;
    }

    @Deprecated
    public void insert(MongoCollections collection, String json)
    {
        insert(collection, Document.parse(json));
    }

    @Deprecated
    public void insert(MongoCollections collection, Document document)
    {
        getCollection(collection).insertOne(document);
    }

    @Deprecated
    public void insertJsonList(MongoCollections collections, List<String> json)
    {
        insert(collections, json.stream().map(Document::parse).collect(Collectors.toList()));
    }

    @Deprecated
    public void insert(MongoCollections collections, List<Document> documents)
    {
        getCollection(collections).insertMany(documents);
    }

    @Deprecated
    public void replace(MongoCollections collections, String key, String json)
    {
        replace(collections, key, Document.parse(json));
    }

    @Deprecated
    public void replace(MongoCollections collections, String key, Document document)
    {
        getCollection(collections).replaceOne(Filters.eq(key), document);
    }

    @Deprecated
    public void insertOrReplaceIfExist(MongoCollections collections, String key, Document document)
    {
        getCollection(collections).replaceOne(Filters.eq(key), document, new ReplaceOptions().upsert(true));
    }

    @Deprecated
    public void delete(MongoCollections collections, String key)
    {
        getCollection(collections).deleteOne(Filters.eq(key));
    }

    @Deprecated
    @NotNull
    public MongoCollection<Document> getCollection(MongoCollections collections)
    {
        return database.getCollection(collections.name);
    }


    public FindIterable<Document> getAll(fr.idarkay.minetasia.common.MongoCollections collection)
    {
        return getCollection(collection).find();
    }

    public Document getByKey(fr.idarkay.minetasia.common.MongoCollections collection, String key)
    {
        return getCollection(collection).find(Filters.eq(key)).first();
    }

    public FindIterable<Document> getSimpleFilter(fr.idarkay.minetasia.common.MongoCollections collection, String filterKey, String filterValue)
    {
        return getCollection(collection).find(Filters.eq(filterKey, filterValue));
    }

    public AggregateIterable<Document> getWithReference(fr.idarkay.minetasia.common.MongoCollections collection, String from, String localField)
    {
        return getWithReference(collection, from, localField, "_id", from);
    }

    public AggregateIterable<Document> getWithReference(fr.idarkay.minetasia.common.MongoCollections collection, String from, String localField, String foreignField, String as)
    {
        return getCollection(collection).aggregate(Collections.singletonList( new Document("$lookup"
                , new Document().append("from", from).append("localField", localField).append("foreignField", foreignField).append("as", as)
        )));
    }

    public AggregateIterable<Document> getWithReferenceAndMatch(fr.idarkay.minetasia.common.MongoCollections collection, String filterKey, String filterValue, String from, String localField, String foreignField, String as)
    {
        return getCollection(collection).aggregate(Arrays.asList(
                new Document("$match"
                        , new Document(filterKey, filterValue)),
                new Document("$lookup"
                        , new Document().append("from", from).append("localField", localField).append("foreignField", foreignField).append("as", as)
                )));
    }

    public boolean match(fr.idarkay.minetasia.common.MongoCollections collection, String id)
    {
        return getCollection(collection).countDocuments(Filters.eq(id)) > 0;
    }

    public boolean match(fr.idarkay.minetasia.common.MongoCollections collection, String key, String value)
    {
        return getCollection(collection).countDocuments(Filters.eq(key, value)) > 0;
    }

    public void insert(fr.idarkay.minetasia.common.MongoCollections collection, String json)
    {
        insert(collection, Document.parse(json));
    }

    public void insert(fr.idarkay.minetasia.common.MongoCollections collection, Document document)
    {
        getCollection(collection).insertOne(document);
    }

    public void insertJsonList(fr.idarkay.minetasia.common.MongoCollections collections, List<String> json)
    {
        insert(collections, json.stream().map(Document::parse).collect(Collectors.toList()));
    }

    public void insert(fr.idarkay.minetasia.common.MongoCollections collections, List<Document> documents)
    {
        getCollection(collections).insertMany(documents);
    }

    public void replace(fr.idarkay.minetasia.common.MongoCollections collections, String key, String json)
    {
        replace(collections, key, Document.parse(json));
    }

    public void replace(fr.idarkay.minetasia.common.MongoCollections collections, String key, Document document)
    {
        getCollection(collections).replaceOne(Filters.eq(key), document);
    }

    @Override
    public void insertOrReplaceIfExist(fr.idarkay.minetasia.common.MongoCollections collections, String key, Document document)
    {
        getCollection(collections).replaceOne(Filters.eq(key), document, new ReplaceOptions().upsert(true));
    }

    public void delete(fr.idarkay.minetasia.common.MongoCollections collections, String key)
    {
        getCollection(collections).deleteOne(Filters.eq(key));
    }

    @NotNull
    public MongoCollection<Document> getCollection(fr.idarkay.minetasia.common.MongoCollections collections)
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
