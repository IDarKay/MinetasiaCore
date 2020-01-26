package fr.idarkay.minetasia.test;

import fr.idarkay.minetasia.core.api.Command;
import fr.idarkay.minetasia.core.api.Economy;
import fr.idarkay.minetasia.core.api.MinetasiaCoreApi;
import fr.idarkay.minetasia.core.api.ServerPhase;
import fr.idarkay.minetasia.core.api.event.FRSMessageEvent;
import fr.idarkay.minetasia.core.api.utils.*;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * File <b>MinetasiaTest</b> located on fr.idarkay.minetasia.test
 * MinetasiaTest is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 17/01/2020 at 23:24
 */
public class MinetasiaTest extends MinetasiaCoreApi
{

    HashMap<UUID, HashMap<String, String>> data = new HashMap<>();

    private Server server = new Server()
    {
        private UUID uuid = UUID.randomUUID();

        @Override
        public long getCreatTime()
        {
            return System.currentTimeMillis();
        }

        @Override
        public @NotNull String getIp()
        {
            return Bukkit.getIp();
        }

        @Override
        public int getPort()
        {
            return Bukkit.getPort();
        }

        @Override
        public @NotNull UUID getUuid()
        {
            return uuid;
        }

        @Override
        public @NotNull String getType()
        {
            return "test";
        }

        @Override
        public @NotNull String getName()
        {
            return "test#" + uuid;
        }

        @Override
        public int getPlayerCount()
        {
            return Bukkit.getOnlinePlayers().size();
        }

        @Override
        public void setPlayerCount(int i)
        {

        }
    };

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Fake Plugin start");

    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Fake Plugin disable");
    }

    @Override
    public String ping() {
        return "pong";
    }

    @Override
    public SQLManager getSqlManager() {
        return null;
    }

    @Override
    public void setPlayerData(@NotNull UUID uuid, @NotNull String s, @NotNull String s1) {
        HashMap<String, String> t = data.getOrDefault(uuid, new HashMap<>());
        t.put(s, s1);
        data.put(uuid, t);
    }

    @Override
    public String getPlayerData(@NotNull UUID uuid, @NotNull String s) {
        HashMap<String, String> d = data.get(uuid);
        if(d != null) return  d.get(s);
        else return null;
    }

    @Override
    public @Nullable UUID getPlayerUUID(@NotNull String s)
    {
        Player player = Bukkit.getPlayer(s);
        return player == null ? null : player.getUniqueId();
    }

    @Override
    public float getPlayerMoney(UUID uuid, Economy economy)
    {
        return 100;
    }

    @Override
    public void addPlayerMoney(UUID uuid, Economy economy, float v, boolean async)
    {

    }

    @Override
    public void removePlayerMoney(UUID uuid, Economy economy, float v, boolean async)
    {

    }

    @Override
    public void setPlayerMoney(UUID uuid, Economy economy, float v, boolean async)
    {

    }

    @Override
    public @NotNull HashMap<UUID, String> getFriends(@NotNull UUID uuid)
    {
        return new HashMap<>();
    }

    @Override
    public boolean isFriend(@NotNull UUID uuid, @NotNull UUID uuid1)
    {
        return true;
    }

    @Override
    public void removeFriend(@NotNull UUID uuid, @NotNull UUID uuid1)
    {

    }

    @Override
    public void addFriend(@NotNull UUID uuid, @NotNull UUID uuid1)
    {

    }

    @Override
    public boolean isPlayerOnline(@NotNull UUID uuid)
    {
        return Bukkit.getPlayer(uuid) != null;
    }

    @Override
    public boolean isPlayerOnline(@NotNull String s)
    {
        return Bukkit.getPlayer(s) != null;
    }

    @Override
    public void publish(@NotNull String s, String s1, boolean... booleans)
    {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> Bukkit.getPluginManager().callEvent(new FRSMessageEvent(s, s1)));
    }

    HashMap<String, HashMap<String, String>> values = new HashMap<>();

    @Override
    public String getValue(String s, String s1)
    {
        return values.getOrDefault(s, new HashMap<>()).getOrDefault(s1, null);
    }

    @Override
    public Set<String> getFields(String s)
    {
        return values.getOrDefault(s, new HashMap<>()).keySet();
    }

    @Override
    public Map<String, String> getValues(String s, Set<String> set)
    {
        return values.getOrDefault(s, new HashMap<>());
    }

    @Override
    public void setValue(String s, String s1, String s2, boolean... booleans)
    {
        HashMap<String, String> v = values.getOrDefault(s, new HashMap<>());
        v.put(s1, s2);
        values.put(s, v);

    }


    @Override
    public void movePlayerToHub(@NotNull org.bukkit.entity.Player player) {
        player.kickPlayer("Disconnect hub function not set (test core version)");
    }

    @Override
    public void movePlayerToServer(@NotNull Player player, Server server)
    {
        player.kickPlayer("Disconnect move function not set (test core version)");
    }

    @Override
    public String getPlayerLang(@NotNull UUID uuid)
    {
        return MinetasiaLang.BASE_LANG;
    }

    @Override
    public @Nullable String getPlayerName(UUID uuid)
    {
        Player p = Bukkit.getPlayer(uuid);
        return  p == null ? null : p.getName();
    }

    @Override
    public PlayerStatueFix getPlayerStatue(UUID uuid)
    {
        return getPlayerStatue(getPlayerName(uuid), uuid);
    }

    @Override
    public PlayerStatueFix getPlayerStatue(String s)
    {
        return getPlayerStatue(s, getPlayerUUID(s));
    }

    public PlayerStatueFix getPlayerStatue(String s, UUID u)
    {
        return new PlayerStatueFix()
        {
            @Override
            public Server getServer()
            {
                return server;
            }

            @Override
            public String getProxy()
            {
                return null;
            }

            @Override
            public String getUserName()
            {
                return s;
            }

            @Override
            public UUID getUUID()
            {
                return u;
            }
        };
    }

    @Override
    public Map<String, Integer> getPlayerKitsLvl(UUID uuid)
    {
        return new HashMap<>();
    }

    @Override
    public Map<String, Integer> getPlayerKitsLvl(UUID uuid, String s)
    {
        return new HashMap<>();
    }

    @Override
    public int getPlayerKitLvl(UUID uuid, String s)
    {
        return 1;
    }

    private HashMap<String, Kit> kits = new HashMap<>();

    @Override
    public Kit getKitKit(String s, String s1)
    {
        return kits.get(s + "_" + s1);
    }

    @Override
    public void saveDefaultKit(Kit kit)
    {
        kits.put(kit.getName() + "_" + kit.getIsoLang(), kit);
    }



    public class Stats implements PlayerStats
    {

        @Override
        public @NotNull Map<String, Long> getAllStats()
        {
            return new HashMap<>();
        }

        @Override
        public @NotNull Map<String, Long> getPluginStats(String s)
        {
            return new HashMap<>();
        }

        @Override
        public long getStatsValue(String s)
        {
            return 1;
        }
    }


    @Override
    public @Nullable PlayerStats getPlayerStats(@NotNull UUID uuid)
    {
        return new Stats();
    }

    @Override
    public void addStatsToPlayer(@NotNull UUID uuid, @NotNull StatsUpdater statsUpdater, boolean b)
    {

    }

    @Override
    public @NotNull Boost getPlayerPersonalBoost(@NotNull UUID uuid)
    {
        return HashMap::new;
    }

    @Override
    public @NotNull Boost getPlayerPartyBoost(@NotNull UUID uuid)
    {
        return HashMap::new;
    }

    @Override
    public void shutdown() {
        getServer().getOnlinePlayers().forEach(this::movePlayerToHub);
        getServer().shutdown();
    }

    @Override
    public void addGameWonMoneyToPlayer(@NotNull UUID uuid, @NotNull MoneyUpdater moneyUpdater, boolean b, boolean b1)
    {

    }

    @Override
    public @NotNull Map<UUID, String> getOnlinePlayers()
    {
        return Bukkit.getOnlinePlayers().stream().collect(Collectors.toMap(Entity::getUniqueId, HumanEntity::getName));
    }

    @Override
    public @NotNull
    List<String> getOnlinePlayersForTab()
    {
        return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
    }

    @Override
    public @NotNull String getServerType()
    {
        return "test";
    }

    @Override
    public @NotNull Server getThisServer()
    {
        return server;
    }

    @Override
    public @Nullable Server getServer(String s)
    {
        return null;
    }

    @Override
    public @NotNull Map<String, Server> getServers()
    {
        return new HashMap<>();
    }

    @Override
    public @NotNull Map<String, Server> getServers(String s)
    {
        return new HashMap<>();
    }

    @Override
    public boolean isCommandEnable(byte b)
    {
        return true;
    }

    @Override
    public boolean isCommandEnable(Command command)
    {
        return true;
    }

    @Override
    public @NotNull String getGroupDisplay(UUID uuid)
    {
        return ChatColor.GOLD + "[test]";
    }

    @Override
    public void setServerPhase(ServerPhase phase)
    {
        System.out.println("Server Phase set to " + phase.name());
    }

}
