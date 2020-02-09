package fr.idarkay.minetasia.core.spigot.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.idarkay.minetasia.core.api.Economy;
import fr.idarkay.minetasia.core.api.utils.*;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.frs.PlayerFrsMessage;
import fr.idarkay.minetasia.core.spigot.utils.FRSKey;
import fr.idarkay.minetasia.core.spigot.utils.JSONUtils;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.apache.commons.lang.Validate;
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

    private Map<String, String> data = null;
    private Map<UUID, String> friends = null;
    @NotNull private final Map<Economy, Float> moneys;

    @NotNull private Map<String, String> generalData;
    @NotNull private String username;

    private Stats stats;
    private boolean friendsLoad = false, dataLoad = false, statsLoad = false;

    private Boost personalBoost = HashMap::new, partyBoost = HashMap::new;
    private Party party;

    public MinePlayer(@NotNull UUID uuid, boolean isCache)
    {
        Validate.notNull(uuid);
        this.uuid = uuid;
        this.isCache = isCache;

        final String data = Objects.requireNonNull(CORE.getValue(FRSKey.DATA.getKey(uuid)));
        final JsonObject dataJson = PARSER.parse(data).getAsJsonObject();

        this.generalData = JSONUtils.jsonObjectToStringMap(dataJson);

        this.username = Objects.requireNonNull(generalData.get("username"));
        if(!generalData.containsKey("money"))
            this.moneys = new HashMap<>();
        else
            this.moneys = JSONUtils.jsonObjectToMap(dataJson.getAsJsonObject("money"), Economy::valueOf, JsonElement::getAsFloat);

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
        generalData.put("username", username);
        saveGeneralData();
    }

    @Override
    public boolean isCache()
    {
        return isCache;
    }

    @Override
    public float getMoney(@NotNull Economy economy)
    {
        return moneys.getOrDefault(economy, 0.0F);
    }

    public synchronized void addMoneyWithoutSave(@NotNull Economy economy, float amount)
    {
        if(amount < 0) throw new IllegalArgumentException("negative amount");
        if(validateNotCache(PlayerFrsMessage.ActionType.ADD_MONEY, economy, amount))
        {
            moneys.merge(economy, amount, Float::sum);
        }
    }

    @Override
    public synchronized void addMoney(@NotNull Economy economy, float amount)
    {
        if(amount < 0) throw new IllegalArgumentException("negative amount");
        if(validateNotCache(PlayerFrsMessage.ActionType.ADD_MONEY, economy, amount))
        {
            moneys.merge(economy, amount, Float::sum);
            saveGeneralData();
        }

    }

    @Override
    public synchronized void removeMoney(@NotNull Economy economy, float amount)
    {
        if(amount < 0) throw new IllegalArgumentException("negative amount");
        if(validateNotCache(PlayerFrsMessage.ActionType.REMOVE_MONEY, economy, amount))
        {
            moneys.merge(economy, amount, (oldV, newV) -> {
                if(oldV < newV) throw new IllegalArgumentException("cant remove "+ amount + " " + economy.displayName + " to " + username);
                return oldV - newV;
            });
            saveGeneralData();
        }
    }

    @Override
    public void setMoney(@NotNull Economy economy, float amount)
    {
        if(amount < 0) throw new IllegalArgumentException("negative amount");
        if(validateNotCache(PlayerFrsMessage.ActionType.SET_MONEY, economy, amount))
        {
            moneys.put(economy, amount);
            saveGeneralData();
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
        checkAndLoadFriends();
        return friends;
    }

    @Override
    public @NotNull String getLang()
    {
        return generalData.getOrDefault("lang", MinetasiaLang.BASE_LANG);
    }

    @Override
    public boolean isFriend(@NotNull UUID uuid)
    {
        Validate.notNull(uuid);
        checkAndLoadFriends();
        return friends.containsKey(uuid);
    }

    @Override
    public synchronized void addFriends(@NotNull UUID uuid)
    {
        checkAndLoadFriends();
        Validate.notNull(uuid);
        if(validateNotCache(PlayerFrsMessage.ActionType.ADD_FRIENDS, uuid))
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
        checkAndLoadFriends();
        Validate.notNull(uuid);
        if(validateNotCache(PlayerFrsMessage.ActionType.REMOVE_FRIENDS, uuid))
        {
            if(friends.remove(uuid) != null)
            {
                saveFriends();
            }
        }

    }

    @Override
    public @Nullable String getGeneralData(@NotNull String key)
    {
        Validate.notNull(key);
        return generalData.get(key);
    }

    @Override
    public synchronized void putGeneralData(@NotNull String key, @Nullable String value)
    {
        Validate.notNull(key);
        if(validateNotCache(PlayerFrsMessage.ActionType.PUT_GENERAL_DATA, key, value))
        {
            if(value == null)
            {
                if(generalData.remove(key) == null) return;
            }
            else generalData.put(key, value);
            saveGeneralData();
        }
    }

    @Override
    public @Nullable String getData(@NotNull String key)
    {
        Validate.notNull(key);
        checkAndLoadCustomData();
        return data.get(key);
    }

    @Override
    public synchronized void putData(@NotNull String key, @Nullable String value)
    {
        Validate.notNull(key);
        checkAndLoadCustomData();
        if(validateNotCache(PlayerFrsMessage.ActionType.PUT_CUSTOM_DATA, key, value))
        {
            if(value == null)
            {
                if(data.remove(key) == null) return;
            }
            else
                data.put(key, value);
            saveCustomData();
        }
    }

    @Override
    public @NotNull PlayerStats getStats()
    {
        checkAndLoadStats();
        return stats;
    }

    @Override
    public void updatePlayerStats(@NotNull StatsUpdater updater)
    {
        checkAndLoadStats();
        stats.update(updater);
        if(validateNotCache(PlayerFrsMessage.ActionType.UPDATE_STATS, stats.toJsonObject().toString())) saveStats();
    }

    @Override
    public int getStatus()
    {
        return Integer.parseInt(generalData.getOrDefault("status", "0"));
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

    private boolean validateNotCache(PlayerFrsMessage.ActionType actionType, Object... args)
    {
        if(isCache)
        {
            if(CORE.isPlayerOnline(uuid))
            {
                PlayerFrsMessage.getMessage(uuid, actionType, args);
                return false;
            }
            else
            {
                return true;
            }
        }
        else return true;
    }

    private synchronized void checkAndLoadStats()
    {
        if(!statsLoad)
        {
            final String data = CORE.getValue(FRSKey.STATS.getKey(uuid));
            stats = data == null ? new Stats() : new Stats(PARSER.parse(data).getAsJsonObject());
            statsLoad = true;
        }
    }

    public synchronized void reloadStats(String data)
    {
        stats = data == null ? new Stats() : new Stats(PARSER.parse(data).getAsJsonObject());
    }

    private synchronized void checkAndLoadFriends()
    {
        if(!friendsLoad)
        {
            final String data = CORE.getValue(FRSKey.FIENDS.getKey(uuid));
            friends = data == null ? new HashMap<>()
                    : JSONUtils.jsonObjectToMap(PARSER.parse(data).getAsJsonObject(), UUID::fromString, JsonElement::getAsString, new TreeMap<>());
            friendsLoad = true;
        }
    }

    private synchronized void checkAndLoadCustomData()
    {
        if(!dataLoad)
        {
            final String data = CORE.getValue(FRSKey.CUSTOM_DATA.getKey(uuid));
            this.data = data == null ? new TreeMap<>() : JSONUtils.jsonObjectToStringMap(PARSER.parse(data).getAsJsonObject(), new TreeMap<>());
            dataLoad = true;
        }
    }

    public synchronized void saveGeneralData()
    {
        generalData.putIfAbsent("lang", MinetasiaLang.BASE_LANG);
        final JsonObject object = JSONUtils.mapToJsonObject(generalData);
        object.add("money", JSONUtils.floatMapToJsonObject(moneys));
        CORE.setValue(FRSKey.DATA.getKey(uuid), object.toString(), true);
    }

    public synchronized void saveStats()
    {
        if(statsLoad)
            CORE.setValue(FRSKey.STATS.getKey(uuid), stats.toJsonObject().toString(), true);
    }

    public synchronized void saveFriends()
    {
        if(friendsLoad)
            CORE.setValue(FRSKey.FIENDS.getKey(uuid), JSONUtils.mapToJsonObject(friends).toString(), true);
    }

    public synchronized void saveCustomData()
    {
        if(dataLoad)
            CORE.setValue(FRSKey.CUSTOM_DATA.getKey(uuid), JSONUtils.mapToJsonObject(data).toString(), true);
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
