package fr.idarkay.minetasia.core.api.utils;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import fr.idarkay.minetasia.core.api.MongoCollections;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * File <b>NoSQLManager</b> located on fr.idarkay.minetasia.core.api.utils
 * NoSQLManager is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 09/02/2020 at 22:07
 */
public interface MongoDbManager
{
    FindIterable<Document> getAll(MongoCollections collection);

    Document getByKey(MongoCollections collection, String key);

    FindIterable<Document> getSimpleFilter(MongoCollections collection, String filterKey, String filterValue);

    AggregateIterable<Document> getWithReference(MongoCollections collection, String from, String localField);

    AggregateIterable<Document> getWithReference(MongoCollections collection, String from, String localField, String foreignField, String as);

    AggregateIterable<Document> getWithReferenceAndMatch(MongoCollections collection, String filterKey, String filterValue, String from, String localField, String foreignField, String as);

    boolean match(MongoCollections collection, String id);

    boolean match(MongoCollections collection, String key, String value);


    void insert(MongoCollections collection, String json);

    void insert(MongoCollections collection, Document document);

    void insertJsonList(MongoCollections collections, List<String> json);

    void insert(MongoCollections collections, List<Document> documents);

    void delete(MongoCollections collections, String key);

    void replace(MongoCollections collections, String key, String json);

    void replace(MongoCollections collections, String key, Document document);

    MongoCollection<Document> getCollection(MongoCollections collections);

    MongoDatabase getDatabase();
}
