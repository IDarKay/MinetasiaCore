package fr.idarkay.minetasia.core.api.utils;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import fr.idarkay.minetasia.core.api.MongoCollections;
import org.bson.Document;

import java.util.List;

/**
 * File <b>NoSQLManager</b> located on fr.idarkay.minetasia.core.api.utils
 * NoSQLManager is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 09/02/2020 at 22:07
 */
public interface MongoDbManager
{
    @Deprecated
    FindIterable<Document> getAll(MongoCollections collection);

    @Deprecated
    Document getByKey(MongoCollections collection, String key);

    @Deprecated
    FindIterable<Document> getSimpleFilter(MongoCollections collection, String filterKey, String filterValue);

    @Deprecated
    AggregateIterable<Document> getWithReference(MongoCollections collection, String from, String localField);

    @Deprecated
    AggregateIterable<Document> getWithReference(MongoCollections collection, String from, String localField, String foreignField, String as);

    @Deprecated
    AggregateIterable<Document> getWithReferenceAndMatch(MongoCollections collection, String filterKey, String filterValue, String from, String localField, String foreignField, String as);

    @Deprecated
    boolean match(MongoCollections collection, String id);

    @Deprecated
    boolean match(MongoCollections collection, String key, String value);

    @Deprecated
    void insert(MongoCollections collection, String json);

    @Deprecated
    void insert(MongoCollections collection, Document document);

    @Deprecated
    void insertJsonList(MongoCollections collections, List<String> json);

    @Deprecated
    void insert(MongoCollections collections, List<Document> documents);

    @Deprecated
    void delete(MongoCollections collections, String key);

    @Deprecated
    void replace(MongoCollections collections, String key, String json);

    @Deprecated
    void replace(MongoCollections collections, String key, Document document);

    @Deprecated
    void insertOrReplaceIfExist(MongoCollections collections, String key, Document document);

    @Deprecated
    MongoCollection<Document> getCollection(MongoCollections collections);

    FindIterable<Document> getAll(fr.idarkay.minetasia.common.MongoCollections collection);

    Document getByKey(fr.idarkay.minetasia.common.MongoCollections collection, String key);

    FindIterable<Document> getSimpleFilter(fr.idarkay.minetasia.common.MongoCollections collection, String filterKey, String filterValue);

    AggregateIterable<Document> getWithReference(fr.idarkay.minetasia.common.MongoCollections collection, String from, String localField);

    AggregateIterable<Document> getWithReference(fr.idarkay.minetasia.common.MongoCollections collection, String from, String localField, String foreignField, String as);

    AggregateIterable<Document> getWithReferenceAndMatch(fr.idarkay.minetasia.common.MongoCollections collection, String filterKey, String filterValue, String from, String localField, String foreignField, String as);

    boolean match(fr.idarkay.minetasia.common.MongoCollections collection, String id);

    boolean match(fr.idarkay.minetasia.common.MongoCollections collection, String key, String value);

    void insert(fr.idarkay.minetasia.common.MongoCollections collection, String json);

    void insert(fr.idarkay.minetasia.common.MongoCollections collection, Document document);

    void insertJsonList(fr.idarkay.minetasia.common.MongoCollections collections, List<String> json);

    void insert(fr.idarkay.minetasia.common.MongoCollections collections, List<Document> documents);

    void delete(fr.idarkay.minetasia.common.MongoCollections collections, String key);

    void replace(fr.idarkay.minetasia.common.MongoCollections collections, String key, String json);

    void replace(fr.idarkay.minetasia.common.MongoCollections collections, String key, Document document);

    void insertOrReplaceIfExist(fr.idarkay.minetasia.common.MongoCollections collections, String key, Document document);

    MongoCollection<Document> getCollection(fr.idarkay.minetasia.common.MongoCollections collections);

    MongoDatabase getDatabase();
}
