package fr.idarkay.minetasia.core.spigot.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import fr.idarkay.minetasia.core.api.Economy;
import fr.idarkay.minetasia.core.api.MongoCollections;
import fr.idarkay.minetasia.core.api.event.PlayerMoneyChangeEvent;
import fr.idarkay.minetasia.core.api.utils.*;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.messages.PlayerMessage;
import fr.idarkay.minetasia.core.spigot.moderation.PlayerSanction;
import fr.idarkay.minetasia.core.spigot.moderation.Sanction;
import fr.idarkay.minetasia.core.spigot.moderation.SanctionType;
import fr.idarkay.minetasia.core.spigot.utils.JSONUtils;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import fr.idarkay.minetasia.normes.Tuple;
import fr.idarkay.minetasia.normes.utils.BukkitUtils;
import org.apache.commons.lang.IllegalClassException;
import org.apache.commons.lang.Validate;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
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

    private static final MinetasiaCore CORE = JavaPlugin.getPlugin(MinetasiaCore.class);
    private static final JsonParser PARSER = new JsonParser();


    @NotNull private final UUID uuid;
    private final boolean isCache;

    private final Map<String, Object> data = new HashMap<>();
    private final Map<UUID, Tuple<String, String>> friends = new HashMap<>(); // map uuid of the friend ; tuple< name of friend ; head texture >
    @NotNull private final Map<Economy, Long> moneys = new HashMap<>();
    @NotNull private final List<NamespacedKey> validateAdvancement = new ArrayList<>();

    @NotNull private String username;

    private Stats stats;

    private Boost personalBoost = HashMap::new, partyBoost = HashMap::new;

    public MinePlayer(@NotNull Document document)
    {
        this.isCache = true;
        this.uuid = UUID.fromString(document.getString("_id"));
        andLoad(document);
    }

    public MinePlayer(@NotNull UUID uuid, boolean isCache)
    {
        Validate.notNull(uuid);
        this.uuid = uuid;
        this.isCache = isCache;

        final Document doc;
        if(isCache)
        {
            doc = CORE.getMongoDbManager().getByKey(MongoCollections.USERS, uuid.toString());
        }
        else
        {
            final Document main = CORE.getMongoDbManager().getCollection(MongoCollections.ONLINE_USERS).aggregate(
                    Arrays.asList(
                            Aggregates.match(Filters.eq(uuid.toString())),
                            Aggregates.lookup(MongoCollections.USERS.name, "_id", "_id", "users"),
                            Aggregates.lookup(MongoCollections.PARTY.name, "party_id", "_id", "party")
                    )
            ).first();
            if(main == null) throw new NullPointerException("player not in online user");

            final List<Document> party = main.getList("party", Document.class);
            if(party != null && party.size() > 0)
                CORE.getPartyManager().load(party.get(0));

            doc =  main.getList("users", Document.class).get(0);
            if(!CORE.getThisServer().getName().equals(main.getString("server_id")))
            {
                CORE.getMongoDbManager().getCollection(MongoCollections.ONLINE_USERS).updateOne(
                        Filters.eq(uuid.toString()),
                        Updates.set("server_id", CORE.getThisServer().getName())
                );
            }
        }
        andLoad(doc);
    }



    private void andLoad(Document doc)
    {

        this.username = doc.getString("username");

        if(doc.containsKey("money"))
            doc.get("money", Document.class).forEach((k, v) -> moneys.put(Economy.valueOf(k), (Long) v));


        if(doc.containsKey("friends"))
            doc.getList("friends", Document.class).forEach( v -> friends.put(UUID.fromString((v).getString("uuid")), new Tuple<>( (v).getString("name"), null)));


        if(doc.containsKey("data"))
            doc.get("data", Document.class).forEach(data::put);

        if(doc.containsKey("stats"))
            this.stats = new Stats(doc.get("stats", Document.class));
        else
            this.stats = new Stats();

        if(doc.containsKey("advancement"))
        {
            doc.getList("advancement", String.class).forEach(n -> validateAdvancement.add(BukkitUtils.namespaceKeyFromSting((String) n)));
        }

        //head
        if(!isCache)
        {
            final Player p = Bukkit.getPlayer(uuid);
            if(p != null)
            {
                final String texture = BukkitUtils.getTextureFromPlayer(p);
                final String actual = (String) data.get("head_texture");
                if(!texture.equals(actual))
                {
                    putData("head_texture", texture);
                }
            }
        }
    }

    @Override
    public boolean hasCompleteAdvancement(@NotNull NamespacedKey advancementName)
    {
        return validateAdvancement.contains(advancementName);
    }


    public void completeAdvancement(@NotNull NamespacedKey advancementName)
    {
        Validate.notNull(advancementName);
        if(!validateAdvancement.contains(advancementName))
        {
            validateAdvancement.add(advancementName);
            CORE.getMongoDbManager().getCollection(MongoCollections.USERS).updateOne(Filters.eq(uuid.toString()), Updates.push("advancement", advancementName.toString()));
        }
    }

    @NotNull
    public List<NamespacedKey> getValidateAdvancement()
    {
        return validateAdvancement;
    }

    public void unsetSanction(@NotNull PlayerSanction playerSanction, boolean removeHistory)
    {
        Validate.notNull(playerSanction);
        if(removeHistory)
        {
            final Document remove = (Document) data.remove(playerSanction.getType().name());
            if(remove == null) return;
            data.merge("history", Collections.emptyList(), (old, newValue) ->  ((List<Document>) old).removeIf(d -> Objects.equals(d.getLong("start"), remove.getLong("start"))));
            CORE.getMongoDbManager().getCollection(MongoCollections.USERS).updateOne(Filters.eq(uuid.toString()),
                    Updates.combine(
                            Updates.unset("data." + playerSanction.getType().name()),
                            Updates.pull("data.history", Filters.eq("start", playerSanction.startTime))
                    )
            );
        }
        else
            putData(playerSanction.getType().name(), null);
    }

    public void setSanction(@NotNull PlayerSanction playerSanction)
    {
        Validate.notNull(playerSanction);
        data.put(playerSanction.getType().name(), playerSanction.toDocument());
        data.merge("history", Collections.singletonList(playerSanction.toDocument()), (old, newValue) ->  {
            final List<Document> old1 = (List<Document>) old;
            old1.addAll((List<Document>) newValue);
            return old1;
        });
        CORE.getMongoDbManager().getCollection(MongoCollections.USERS).updateOne(Filters.eq(uuid.toString()),
                Updates.combine(
                        Updates.push("data.history", playerSanction.toDocument()),
                        Updates.set("data." + playerSanction.getType().name(), playerSanction.toDocument())
                )
        );
    }

    public List<Document> getHistory()
    {
        return getDataList("history", Document.class);
    }

    public int getRecurrenceOfASanction(@NotNull Sanction genericName)
    {
        return (int) getHistory().stream().filter(document -> document.getString("generic_name").equalsIgnoreCase(genericName.getGenericName())).count();
    }

    @Nullable
    public PlayerSanction getSanction(@NotNull SanctionType type)
    {
        Validate.notNull(type);
        final Document document = (Document) getData(type.name());
        if(document == null)
        {
            return null;
        }
        final PlayerSanction playerSanction = PlayerSanction.fromDocument(document);
        if(playerSanction.isEnd())
        {
            //remove the sanction
            putData(type.toString(), null);
            return null;
        }
        return playerSanction;
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
            callMoneyChange(economy, (float) getMoney(economy));
            increment("money." + economy.name(), (long) (amount * 100));
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
            callMoneyChange(economy, (float) getMoney(economy));
            increment("money." + economy.name(), (long) (amount * -100));
        }
    }

    @Override
    public void setMoney(@NotNull Economy economy, float amount)
    {
        if(amount < 0) throw new IllegalArgumentException("negative amount");
        if(validateNotCache(PlayerMessage.ActionType.SET_MONEY, economy, amount))
        {
            callMoneyChange(economy, amount);
            moneys.put(economy, (long) (amount * 100));
            set("money." + economy.name(), (long) (amount * 100));
        }
    }

    private void callMoneyChange(@NotNull Economy economy, float amount)
    {
        final Player player = Bukkit.getPlayer(uuid);
        if(player != null)
        {
            if(Bukkit.isPrimaryThread())

                Bukkit.getScheduler().runTaskAsynchronously(CORE, () -> Bukkit.getPluginManager().callEvent(new PlayerMoneyChangeEvent(player, economy, amount)));
            else
                Bukkit.getPluginManager().callEvent(new PlayerMoneyChangeEvent(player, economy, amount));
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
    public @NotNull Map<UUID, Tuple<String, String>> getFriends()
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
            if(friends.put(uuid, new Tuple<>(CORE.getPlayerName(uuid), null)) == null)
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
        try
        {
            final String[] split = key.split("\\.");
            Document current = null;
            for (int i = 0; i < split.length; i++)
            {
                if(i != split.length - 1)
                {
                    if(current == null)
                    {
                        current = (Document) data.get(split[i]);
                    }
                    else
                    {
                        current = current.get(split[i], Document.class);
                    }
                    if(current == null) return null;
                }
                else
                {
                    return  current == null ? data.get(split[i]) : current.get(split[i]);
                }
            }
            return data.get(key);
        }
        catch (IllegalClassException ignore)
        {
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public @Nullable <T> T getData(@NotNull String key, @NotNull Class<T> clazz)
    {
        Validate.notNull(key);
        Validate.notNull(clazz);
        final Object value = data.get(key);
        if(value == null) return null;
        return clazz.cast(value);
    }

    @Override
    public @NotNull <T> List<T> getDataList(@NotNull String key, @NotNull Class<T> clazz)
    {
        Validate.notNull(key);
        Validate.notNull(clazz);
        final List list = getData(key, List.class);

        if(list == null) return Collections.emptyList();

        final Iterator iterator = list.iterator();

        Object current;
        do {
            if(!iterator.hasNext())
            {
                return list;
            }

            current = iterator.next();
        } while (clazz.isAssignableFrom(current.getClass()));

        throw new ClassCastException(String.format("List element cannot be cast to %s", clazz.getName()));

    }

    @Override
    public synchronized void putData(@NotNull String key, @Nullable Object value)
    {
        Validate.notNull(key);
        if(validateNotCache(PlayerMessage.ActionType.PUT_CUSTOM_DATA, key, value))
        {
            final String[] split = key.split("\\.");
            Document current = null;
            if (value == null)
            {
                for (int i = 0; i < split.length; i++)
                {
                    if(i != split.length - 1)
                    {
                        if(current == null)
                        {
                            current = (Document) data.get(split[i]);
                        }
                        else
                        {
                            current = current.get(split[i], Document.class);
                        }
                        if(current == null) return;
                    }
                    else
                    {
                        if ((current == null ? data.remove(split[i]) : current.remove(split[i])) == null) return;
                        unset("data." + key);
                        return;
                    }
                }
                if(data.remove(key) == null) return;
                unset("data." + key);
                return;
            }
            else
            {
                for (int i = 0; i < split.length; i++)
                {
                    if(i != split.length - 1)
                    {
                        if(current == null)
                        {
                            if(!data.containsKey(split[i]))
                            {
                                current = new Document();
                                data.put(split[i], current);
                            }
                            else
                            {
                                current = (Document) data.get(split[i]);
                            }
                        }
                        else
                        {
                            if(!current.containsKey(split[i]))
                            {
                                final Document tmp = new Document();
                                current.put(split[i], tmp);
                                current = tmp;
                            }
                            else
                            {
                                current = current.get(split[i], Document.class);
                            }
                        }
                    }
                    else
                    {
                        if(current == null)
                        {
                            data.put(split[i], value);
                        }
                        else
                        {
                            current.append(split[i], value);
                        }
                        set("data." + key, value);
                        return;
                    }
                }
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
        if(!isCache)
            CORE.getAdvancementManager().playerStatsUpdate(updater, this);
        final Document doc =  new Document();
        updater.getUpdate().forEach((name, value) -> doc.append("stats." + name, value));

        CORE.getMongoDbManager().getCollection(MongoCollections.USERS).updateOne(Filters.eq(uuid.toString()), new Document("$inc", doc));

    }

    @Override
    public long getStatus()
    {
        return (long) data.getOrDefault("status", 0L);
    }

    @Override
    public Party getParty()
    {
        return CORE.getPartyManager().getByPlayer(uuid);
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
        CORE.getMongoDbManager().getCollection(MongoCollections.USERS).updateOne(Filters.eq(uuid.toString()), Updates.set(key, value));
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

        m.forEach((k,v ) -> money.append("money." + k.name(), (long) (v * 100)));

        CORE.getMongoDbManager().getCollection(MongoCollections.USERS).updateOne(Filters.eq(uuid.toString()),  new Document("$inc", money));
        final JsonObject json = new JsonObject();
        m.forEach((k,v ) -> json.addProperty(k.name(), (long) (v * 100)));
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
        friends.forEach((k, v) -> all.add(new Document("uuid", k.toString()).append("name", v.a())));
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
