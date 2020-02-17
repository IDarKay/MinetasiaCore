package fr.idarkay.minetasia.core.spigot.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import fr.idarkay.minetasia.core.api.Economy;
import fr.idarkay.minetasia.core.api.MongoCollections;
import fr.idarkay.minetasia.core.api.utils.*;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.messages.PlayerMessage;
import fr.idarkay.minetasia.core.spigot.utils.JSONUtils;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.apache.commons.lang.Validate;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

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
public class MinePlayer implements MinetasiaPlayer
{

    private static final MinetasiaCore CORE = MinetasiaCore.getCoreInstance();
    private static final JsonParser PARSER = new JsonParser();

    @NotNull private final UUID uuid;
    private final boolean isCache;

    private final Map<String, Object> data;
    private final Map<UUID, String> friends;
    @NotNull private final Map<Economy, Long> moneys;

    @NotNull private String username;

    private Stats stats;

    private Boost personalBoost = HashMap::new, partyBoost = HashMap::new;
    private Party party;

    public MinePlayer(@NotNull UUID uuid, boolean isCache)
    {
        Validate.notNull(uuid);
        this.uuid = uuid;
        this.isCache = isCache;

        final Document doc = CORE.getMongoDbManager().getByKey(MongoCollections.USERS, uuid.toString());
        username = doc.getString("username");

        this.moneys = new HashMap<>();
        if(doc.containsKey("money"))
            doc.get("money", Document.class).forEach((k, v) -> moneys.put(Economy.valueOf(k), (Long) v));


        this.friends = new HashMap<>();
        if(doc.containsKey("friends"))
            doc.get("friends", Document.class).forEach((k, v) -> friends.put(UUID.fromString(k), v.toString()));

        this.data = new HashMap<>();
        if(doc.containsKey("data"))
            doc.get("data", Document.class).forEach(data::put);

        if(doc.containsKey("stats"))
            this.stats = new Stats(doc.get("stats", Document.class));
        else
            this.stats = new Stats();

        //todo: party
        //temp value
        party = new Party()
        {
            @Override
            public UUID getOwner()
            {
                return uuid;
            }

            @Override
            public List<UUID> getPlayers()
            {
                return Collections.singletonList(uuid);
            }

            @Override
            public int limitSize()
            {
                return 10;
            }
        };

    }

    public void setUsername(@NotNull String username)
    {
        Validate.notNull(username);
        this.username = username;
        set("username", username);
    }

    @Override
    public boolean isCache()
    {
        return isCache;
    }

    @Override
    public double getMoney(@NotNull Economy economy)
    {
        return moneys.getOrDefault(economy, 0L) / 100d;
    }

    @Override
    public synchronized void addMoney(@NotNull Economy economy, float amount)
    {
        if(amount < 0) throw new IllegalArgumentException("negative amount");
        if(validateNotCache(PlayerMessage.ActionType.ADD_MONEY, economy, amount))
        {
            moneys.merge(economy, (long) (amount * 100), Long::sum);
            increment("money." + economy.name, (long) (amount * 100));
        }
    }

    @Override
    public synchronized void removeMoney(@NotNull Economy economy, float amount)
    {
        if(amount < 0) throw new IllegalArgumentException("negative amount");
        if(validateNotCache(PlayerMessage.ActionType.REMOVE_MONEY, economy, amount))
        {
            moneys.merge(economy, (long) (amount * 100), (oldV, newV) -> {
                if(oldV < newV) throw new IllegalArgumentException("cant remove "+ amount + " " + economy.displayName + " to " + username);
                return oldV - newV;
            });
            increment("money." + economy.name, (long) (amount * -100));
        }
    }

    @Override
    public void setMoney(@NotNull Economy economy, float amount)
    {
        if(amount < 0) throw new IllegalArgumentException("negative amount");
        if(validateNotCache(PlayerMessage.ActionType.SET_MONEY, economy, amount))
        {
            moneys.put(economy, (long) (amount * 100));
            set("money." + economy.name, (long) (amount * 100));
        }
    }

    @Override
    public @NotNull UUID getUUID()
    {
        return uuid;
    }

    @Override
    public @NotNull String getName()
    {
        return username;
    }

    @Override
    public @NotNull Map<UUID, String> getFriends()
    {
        return friends;
    }

    @Override
    public @NotNull String getLang()
    {
        return data.getOrDefault("lang", MinetasiaLang.BASE_LANG).toString();
    }

    @Override
    public boolean isFriend(@NotNull UUID uuid)
    {
        Validate.notNull(uuid);
        return friends.containsKey(uuid);
    }

    @Override
    public synchronized void addFriends(@NotNull UUID uuid)
    {
        Validate.notNull(uuid);
        if(validateNotCache(PlayerMessage.ActionType.ADD_FRIENDS, uuid))
        {
            if(friends.put(uuid, CORE.getPlayerName(uuid)) == null)
            {
                saveFriends();
            }
        }
    }

    @Override
    public synchronized void removeFriends(@NotNull UUID uuid)
    {
        Validate.notNull(uuid);
        if(validateNotCache(PlayerMessage.ActionType.REMOVE_FRIENDS, uuid))
        {
            if(friends.remove(uuid) != null)
            {
                saveFriends();
            }
        }

    }

    @Override
    @Deprecated
    public @Nullable Object getGeneralData(@NotNull String key)
    {
        return getData(key);
    }

    @Override
    @Deprecated
    public synchronized void putGeneralData(@NotNull String key, @Nullable Object value)
    {
        putData(key, value);
    }

    @Override
    public @Nullable Object getData(@NotNull String key)
    {
        Validate.notNull(key);
        return data.get(key);
    }

    @Override
    public synchronized void putData(@NotNull String key, @Nullable Object value)
    {
        Validate.notNull(key);
        if(validateNotCache(PlayerMessage.ActionType.PUT_CUSTOM_DATA, key, value))
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
    }

    @Override
    public @NotNull PlayerStats getStats()
    {
        return stats;
    }

    @Override
    public void updatePlayerStats(@NotNull StatsUpdater updater)
    {
        stats.update(updater);
        if(validateNotCache(PlayerMessage.ActionType.UPDATE_STATS, stats.toJsonObject().toString()))
        {

            final Document doc =  new Document();
            updater.getUpdate().forEach(doc::append);

            CORE.getMongoDbManager().getCollection(MongoCollections.USERS).updateOne(Filters.eq(uuid.toString()), new Document("$inc", doc));
        }
    }

    @Override
    public int getStatus()
    {
        return (int) data.getOrDefault("statue", 0);
    }

    @Override
    public Party getParty()
    {
        return party;
    }

    public Boost getPersonalBoost()
    {
        return personalBoost;
    }

    public void setPartyBoost(Boost partyBoost)
    {
        this.partyBoost = partyBoost;
    }

    public void setPersonalBoost(Boost personalBoost)
    {
        this.personalBoost = personalBoost;
    }

    public Boost getPartyBoost()
    {
        return partyBoost;
    }

    private boolean validateNotCache(PlayerMessage.ActionType actionType, Object... args)
    {
        if(isCache)
        {
            if(CORE.isPlayerOnline(uuid))
            {
                PlayerMessage.getMessage(uuid, actionType, args);
                return false;
            }
            else
            {
                return true;
            }
        }
        else return true;
    }

    private synchronized void set(String key, Object value)
    {
        CORE.getMongoDbManager().getCollection(MongoCollections.USERS).updateOne(Filters.eq(uuid.toString()), Updates.addToSet(key, value));
    }


    public synchronized void incrementMoney(JsonObject json)
    {
        for (Map.Entry<String, JsonElement> e : json.entrySet())
        {
            moneys.merge(Economy.valueOf(e.getKey()), e.getValue().getAsLong(), Long::sum);
        }
    }

    public synchronized void incrementMoney(String json)
    {
        incrementMoney(PARSER.parse(json).getAsJsonObject());
    }

    public synchronized void incrementMoney(Map<Economy, Float> m)
    {
        final Document money = new Document();

        m.forEach((k,v ) -> money.append("money." + k.name, (long) (v * 100)));

        CORE.getMongoDbManager().getCollection(MongoCollections.USERS).updateOne(Filters.eq(uuid.toString()),  new Document("$inc", money));
        final JsonObject json = new JsonObject();
        m.forEach((k,v ) -> json.addProperty(k.name, (long) (v * 100)));
        if(validateNotCache(PlayerMessage.ActionType.ADD_MONEYS, json.toString()))
        {
            incrementMoney(json);
        }


    }

    private synchronized void increment(String key, Number increment)
    {
        CORE.getMongoDbManager().getCollection(MongoCollections.USERS).updateOne(Filters.eq(uuid.toString()), Updates.inc(key, increment));
    }

    private synchronized void unset(String key)
    {
        CORE.getMongoDbManager().getCollection(MongoCollections.USERS).updateOne(Filters.eq(uuid.toString()), Updates.unset(key));
    }

    private void saveFriends()
    {
        final List<Document> all = new ArrayList<>();
        friends.forEach((k, v) -> all.add(new Document("uuid", k.toString()).append("name", v)));
        set("friends", all);
    }


    private static class Stats implements PlayerStats {

        private final Map<String, Long> stats = new TreeMap<>();

        Stats()
        {

        }

        Stats(@Nullable JsonObject data)
        {
            if(data != null)
                JSONUtils.jsonObjectToMap(data, s -> s, JsonElement::getAsLong, stats);
        }

        Stats(@Nullable Document document)
        {
            if(document != null)
                document.forEach((k, v) -> stats.put(k, (Long) v));
        }

        @Override
        public @NotNull Map<@NotNull String, @NotNull Long> getAllStats()
        {
            return new HashMap<>(stats);
        }

        @Override
        public @NotNull Map<@NotNull String, @NotNull Long> getPluginStats(String pluginName)
        {
            return stats.entrySet().stream().filter(sle -> sle.getKey().startsWith(pluginName)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        @Override
        public long getStatsValue(String statsName)
        {
            return stats.getOrDefault(statsName, -1L);
        }

        private synchronized void  update(@NotNull StatsUpdater updater)
        {
            // add element to map and sum if present
            updater.getUpdate().forEach((k, v) -> stats.merge(k, v, Long::sum));
        }

        JsonObject toJsonObject()
        {
            JsonObject obj = new JsonObject();
            stats.forEach(obj::addProperty);
            return obj;
        }

    }

}
