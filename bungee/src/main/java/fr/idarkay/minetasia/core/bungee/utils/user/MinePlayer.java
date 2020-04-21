package fr.idarkay.minetasia.core.bungee.utils.user;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import fr.idarkay.minetasia.core.bungee.MinetasiaCoreBungee;
import fr.idarkay.minetasia.core.bungee.MongoCollections;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * File <b>MinePlayer</b> located on fr.idarkay.minetasia.core.spigot.user
 * MinePlayer is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 05/02/2020 at 16:18
 */
public class MinePlayer
{

    private static final MinetasiaCoreBungee CORE = MinetasiaCoreBungee.getInstance();

    @NotNull private final UUID uuid;

    private final Map<String, Object> data;
//    private final Map<UUID, String> friends;

    @NotNull private String username;

//    private Stats stats;

    public MinePlayer(@NotNull UUID uuid, String username)
    {
        this.uuid = uuid;
        this.username = username;
        this.data = new HashMap<>();
    }

    public void saveNew()
    {
        final Document d = new Document("_id", uuid.toString())
                .append("username", username)
                .append("data", new Document(data));

        CORE.getMongoDBManager().insert(MongoCollections.USERS, d);
    }

    public MinePlayer(@NotNull UUID uuid)
    {
        this.uuid = uuid;

        final Document doc = CORE.getMongoDBManager().getByKey(MongoCollections.USERS, uuid.toString());
        if(doc == null) throw new NullPointerException();
        username = doc.getString("username");


//        this.friends = new HashMap<>();
//        if(doc.containsKey("friends"))
//            doc.get("friends", Document.class).forEach((k, v) -> friends.put(UUID.fromString(k), v.toString()));

        this.data = new HashMap<>();
        if(doc.containsKey("data"))
            doc.get("data", Document.class).forEach(data::put);

//        if(doc.containsKey("stats"))
//            this.stats = new Stats(doc.get("stats", Document.class));
//        else
//            this.stats = new Stats();
    }

    public void setUsername(@NotNull String username)
    {
        this.username = username;
        set("username", username);
    }


    public @NotNull UUID getUUID()
    {
        return uuid;
    }

    
    public @NotNull String getName()
    {
        return username;
    }

    public @NotNull String getLang()
    {
        return data.getOrDefault("lang", MinetasiaLang.BASE_LANG).toString();
    }

    @Deprecated
    public @Nullable Object getGeneralData(@NotNull String key)
    {
        return getData(key);
    }

    
    @Deprecated
    public synchronized void putGeneralData(@NotNull String key, @Nullable Object value)
    {
        putData(key, value);
    }

    
    public @Nullable Object getData(@NotNull String key)
    {
        return data.get(key);
    }

    
    public synchronized void putData(@NotNull String key, @Nullable Object value)
    {
            if(value == null)
            {
                if(data.remove(key) == null) return;
                unset("data." + key);
            }
            else
            {
                data.put(key, value);
                set("data." + key, value);
            }
    }



    
//    public void updatePlayerStats(@NotNull StatsUpdater updater)
//    {
//        stats.update(updater);
//        if(validateNotCache(PlayerMessage.ActionType.UPDATE_STATS, stats.toJsonObject().toString()))
//        {
//
//            final Document doc =  new Document();
//            updater.getUpdate().forEach(doc::append);
//
//            CORE.getMongoDbManager().getCollection(MongoCollections.USERS).updateOne(Filters.eq(uuid.toString()), new Document("$inc", doc));
//        }
//    }


    public int getStatus()
    {
        return (int) data.getOrDefault("statue", 0);
    }



    private synchronized void set(String key, Object value)
    {
        CORE.getMongoDBManager().getCollection(MongoCollections.USERS).updateOne(Filters.eq(uuid.toString()), Updates.set(key, value));
    }

    private synchronized void increment(String key, Number increment)
    {
        CORE.getMongoDBManager().getCollection(MongoCollections.USERS).updateOne(Filters.eq(uuid.toString()), Updates.inc(key, increment));
    }

    private synchronized void unset(String key)
    {
        CORE.getMongoDBManager().getCollection(MongoCollections.USERS).updateOne(Filters.eq(uuid.toString()), Updates.unset(key));
    }

//    private void saveFriends()
//    {
//        final List<Document> all = new ArrayList<>();
//        friends.forEach((k, v) -> all.add(new Document("uuid", k.toString()).append("name", v)));
//        set("friends", all);
//    }



}
