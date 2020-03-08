package fr.idarkay.minetasia.test;

import fr.idarkay.minetasia.core.api.Command;
import fr.idarkay.minetasia.core.api.Economy;
import fr.idarkay.minetasia.core.api.GeneralPermission;
import fr.idarkay.minetasia.core.api.MinetasiaCoreApi;
import fr.idarkay.minetasia.core.api.ServerPhase;
import fr.idarkay.minetasia.core.api.utils.Boost;
import fr.idarkay.minetasia.core.api.utils.Kit;
import fr.idarkay.minetasia.core.api.utils.MainKit;
import fr.idarkay.minetasia.core.api.utils.MinetasiaPlayer;
import fr.idarkay.minetasia.core.api.utils.MoneyUpdater;
import fr.idarkay.minetasia.core.api.utils.MongoDbManager;
import fr.idarkay.minetasia.core.api.utils.Party;
import fr.idarkay.minetasia.core.api.utils.PlayerStats;
import fr.idarkay.minetasia.core.api.utils.PlayerStatueFix;
import fr.idarkay.minetasia.core.api.utils.Server;
import fr.idarkay.minetasia.core.api.utils.StatsUpdater;
import fr.idarkay.minetasia.normes.MinetasiaGUI;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import fr.idarkay.minetasia.normes.Reflection;
import fr.idarkay.minetasia.normes.anontation.MinetasiaGuiNoCallEvent;
import fr.idarkay.minetasia.test.listener.inventory.InventoryClickListener;
import fr.idarkay.minetasia.test.listener.inventory.InventoryCloseListener;
import fr.idarkay.minetasia.test.listener.inventory.InventoryDragListener;
import fr.idarkay.minetasia.test.listener.inventory.InventoryOpenListener;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * File <b>MinetasiaTest</b> located on fr.idarkay.minetasia.test
 * MinetasiaTest is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 17/01/2020 at 23:24
 */
public class MinetasiaTest extends MinetasiaCoreApi
{

    HashMap<UUID, HashMap<String, Object>> data = new HashMap<>();

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
        public int getPublishPort()
        {
            return getPort();
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

        @Override
        public int getMaxPlayerCount()
        {
            return maxPlayerCount;
        }

        @Override
        public void setMaxPlayerCount(int maxPlayerCount)
        {

        }

        @Override
        public ServerPhase getServerPhase()
        {
            return serverPhase;
        }

        @Override
        public void setPhase(ServerPhase phase)
        {

        }

        @Override
        public String getServerConfig()
        {
            return serverConfig;
        }

        @Override
        public int compareTo(@NotNull fr.idarkay.minetasia.core.api.utils.Server server)
        {
            return  server.getServerPhase().ordinal() - getServerPhase().ordinal();
        }

    };

    private final static String LOG_PREFIX = "[Minetasia-Core-test]";
    @NotNull private String prefix = "", serverConfig = "";

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Fake Plugin start");
        final ConsoleCommandSender console =  this.getServer().getConsoleSender();
        try {
            PluginManager pm = getServer().getPluginManager();
            PermissionDefault permDefault = getConfig().getBoolean("commands-allow-op") ? PermissionDefault.OP : PermissionDefault.FALSE;

            // register general permissions
            console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "Register general permission");
            for (GeneralPermission p : GeneralPermission.values()) {
                console.sendMessage(ChatColor.GRAY + LOG_PREFIX + "Register permission : " + p.getPermission());
                pm.addPermission(new Permission(p.getPermission(), p.getDescription(), permDefault, p.getALLChild()));
            }

        } catch (Exception e) {
            // this throws an exception if the plugin is /reloaded, grr
        }


        prefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("prefix")));;
        serverConfig = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("config_name")));

        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryDragListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryOpenListener(), this);

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
    public void setPlayerData(@NotNull UUID uuid, @NotNull String s, @NotNull Object s1) {
        HashMap<String, Object> t = data.getOrDefault(uuid, new HashMap<>());
        t.put(s, s1);
        data.put(uuid, t);
    }

    @Override
    public Object getPlayerData(@NotNull UUID uuid, @NotNull String s) {
        HashMap<String, Object> d = data.get(uuid);
        if(d != null) return  d.get(s);
        else return null;
    }

    @Override
    public <T> T getPlayerData(@NotNull UUID uuid, @NotNull String key, Class<T> cast)
    {
        return cast.cast(getPlayerData(uuid, key));
    }

    @Override
    public @Nullable UUID getPlayerUUID(@NotNull String s)
    {
        Player player = Bukkit.getPlayer(s);
        return player == null ? null : player.getUniqueId();
    }

    @Override
    public double getPlayerMoney(UUID uuid, Economy economy)
    {
        return 100.0D;
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
    public void publishGlobal(@NotNull String chanel, String message, boolean proxy, boolean sync)
    {

    }

    @Override
    public void publishProxy(@NotNull String chanel, String message, boolean sync)
    {

    }

    @Override
    public void publishServerType(@NotNull String chanel, String message, String serverType, boolean sync)
    {

    }

    @Override
    public String publishTarget(@NotNull String chanel, String message, Server target, boolean rep, boolean sync)
    {
        return null;
    }

    @Override
    public String publishTargetPlayer(@NotNull String chanel, String message, UUID target, boolean rep, boolean sync)
    {
        return null;
    }

    @Override
    public String publishTargetPlayer(@NotNull String chanel, String message, PlayerStatueFix target, boolean rep, boolean sync)
    {
        return null;
    }


    HashMap<String, HashMap<String, String>> values = new HashMap<>();



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
    public void movePlayerToServer(@NotNull UUID player, Server server)
    {
        final Player player1 = Bukkit.getPlayer(player);
        if(player1 != null)
            movePlayerToServer(player1, server);
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
    public void setPlayerKitLvl(UUID uuid, String kitName, int lvl)
    {

    }

    @Override
    public int getPlayerKitLvl(UUID uuid, String s)
    {
        return 1;
    }

    private HashMap<String, MainKit> kits = new HashMap<>();

    @Override
    public Kit getKitKit(String s, String s1)
    {
        return getMainKit(s).getLang(s1);
    }

    @Override
    public List<MainKit> getMainKits(String prefix)
    {
        return kits.values().stream().filter(k -> k.getName().startsWith(prefix)).collect(Collectors.toList());
    }

    @Override
    public MainKit getMainKit(String name)
    {
        return kits.get(name);
    }

    @Override
    public Kit getKitLang(String kitName, String lang)
    {
        return getMainKit(kitName).getLang(lang);
    }

    @Override
    public void saveDefaultKit(MainKit kit)
    {
        kits.put(kit.getName(), kit);
    }

    @Override
    public MainKit createKit(String isoLang, String name, String displayName, int maxLvl, int[] price, Material displayMat, String[] lvlDesc, String... desc)
    {
        return new KitMain(isoLang, name, displayName, maxLvl, price, displayMat, lvlDesc, desc);
    }

    @Override
    public MainKit createMonoLvlCoinsKit(String isoLang, String name, String displayName, Economy economy, int price, Material displayMat, String[] lvlDesc, String... desc)
    {
        return new KitMain(isoLang, name, displayName, economy, price, displayMat, lvlDesc, desc);
    }

    @Override
    public MainKit createMonoLvlPermsKit(String isoLang, String name, String displayName, String perm, Material displayMat, String[] lvlDesc, String... desc)
    {
        return new KitMain(isoLang, name, displayName, perm, displayMat, lvlDesc, desc);
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

    private static ServerPhase serverPhase = ServerPhase.LOAD;

    private int maxPlayerCount = -1;

    @Override
    public void setServerPhase(@NotNull ServerPhase phase)
    {
        Validate.notNull(phase);
        serverPhase = phase;
        //check if max player is not -1
        if(phase != ServerPhase.LOAD && maxPlayerCount < 0) throw new IllegalArgumentException("cant change phase without set maxPlayerCount !");
        //add place for admin
        if(phase == ServerPhase.GAME) setMaxPlayerCount(maxPlayerCount + 2, false);
    }

    @Override
    public @NotNull ServerPhase getServerPhase()
    {
        return serverPhase;
    }

    @Override
    public void setMaxPlayerCount(int maxPlayer)
    {
        setMaxPlayerCount(maxPlayer, true);
    }

    @Override
    public int getMaxPlayerCount()
    {
        return serverPhase == ServerPhase.GAME || serverPhase == ServerPhase.END ? maxPlayerCount -2 : maxPlayerCount;
    }

    @Override
    public boolean isHub()
    {
        return false;
    }

    private final static String OPEN_METHODS_NAME = "open";
    private final static String CLOSE_METHODS_NAME =  "close";
    private final static String CLICK_METHODS_NAME =  "click";
    private final static String DRAG_METHODS_NAME = "drag";

    @Override
    public void registerGui(MinetasiaGUI gui)
    {
        final Class<? extends MinetasiaGUI> clazz = gui.getClass();
        for(Method method : clazz.getDeclaredMethods())
        {
            if(method.getDeclaredAnnotation(MinetasiaGuiNoCallEvent.class) != null)
            {
                final String name = method.getName();
                switch (name)
                {
                    case OPEN_METHODS_NAME:
                        InventoryOpenListener.blackListClazz.add(clazz);
                        break;
                    case CLOSE_METHODS_NAME:
                        InventoryCloseListener.blackListClazz.add(clazz);
                        break;
                    case CLICK_METHODS_NAME:
                        InventoryClickListener.blackListClazz.add(clazz);
                        break;
                    case DRAG_METHODS_NAME:
                        InventoryDragListener.blackListClazz.add(clazz);
                        break;
                }
            }
        }
    }

    @Override
    public @NotNull String getPrefix()
    {
        return prefix;
    }

    @Override
    public @NotNull String getServerConfig()
    {
        return serverConfig;
    }

    @Override
    public MinetasiaPlayer getPlayer(UUID uuid)
    {
        return null;
    }

    @Override
    public MongoDbManager getMongoDbManager()
    {
        return null;
    }

    @Override
    public void openPartyGui(@NotNull Player player)
    {
        player.sendMessage("no gui with test core");
    }

    public void setMaxPlayerCount(int maxPlayer, boolean startup)
    {
        maxPlayerCount = maxPlayer;
        if(startup && serverPhase != ServerPhase.LOAD) throw new IllegalArgumentException("can set maxPlayerCount only in Load Phase !");
        try
        {
            Object playerList = Reflection.getCraftBukkitClass("CraftServer").getDeclaredMethod("getHandle").invoke(Bukkit.getServer());
            Field maxPlayers =  Reflection.getDeclaredField(playerList.getClass().getSuperclass(), "maxPlayers", true);
            maxPlayers.set(playerList, maxPlayer);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public class MinePlayer implements MinetasiaPlayer
    {
        private final UUID partyUUID = UUID.randomUUID();
        private final UUID uuid;

        public MinePlayer(UUID uuid)
        {
            this.uuid = uuid;
        }

        @Override
        public boolean isCache()
        {
            return false;
        }

        @Override
        public double getMoney(@NotNull Economy economy)
        {
            return getPlayerMoney(uuid, economy);
        }

        @Override
        public void addMoney(@NotNull Economy economy, float amount)
        {
            addPlayerMoney(uuid, economy, amount, false);
        }

        @Override
        public void removeMoney(@NotNull Economy economy, float amount)
        {
            removePlayerMoney(uuid, economy , amount, false);
        }

        @Override
        public void setMoney(@NotNull Economy economy, float amount)
        {
            setPlayerMoney(uuid, economy, amount, false);
        }

        @Override
        public @NotNull UUID getUUID()
        {
            return uuid;
        }

        @Override
        public @NotNull String getName()
        {
            return getPlayerName(uuid);
        }

        @Override
        public @NotNull Map<UUID, String> getFriends()
        {
            return new HashMap<>();
        }

        @Override
        public @NotNull String getLang()
        {
            return MinetasiaLang.BASE_LANG;
        }

        @Override
        public boolean isFriend(@NotNull UUID uuid)
        {
            return true;
        }

        @Override
        public void addFriends(@NotNull UUID uuid)
        {

        }

        @Override
        public void removeFriends(@NotNull UUID uuid)
        {

        }

        @Override
        public @Nullable String getGeneralData(@NotNull String key)
        {
            return "no in test plugin";
        }

        @Override
        public void putGeneralData(@NotNull String key, @Nullable Object value)
        {

        }


        @Override
        public @Nullable Object getData(@NotNull String key)
        {
            return getPlayerData(uuid, key);
        }

        @Override
        public void putData(@NotNull String key, @Nullable Object value)
        {
            setPlayerData(uuid, key, value);
        }


        @Override
        public @NotNull PlayerStats getStats()
        {
            return Objects.requireNonNull(getPlayerStats(uuid));
        }

        @Override
        public void updatePlayerStats(@NotNull StatsUpdater updater)
        {

        }

        @Override
        public long getStatus()
        {
            return 0L;
        }

        @Override
        public Party getParty()
        {
            return new Party()
            {
                @Override
                public UUID getId()
                {
                    return partyUUID;
                }

                @Override
                public UUID getOwner()
                {
                    return uuid;
                }

                @Override
                public String getOwnerName()
                {
                    return getName();
                }

                @Override
                public Map<UUID, String> getPlayers()
                {
                    return Collections.singletonMap(uuid, getName());
                }

                @Override
                public int limitSize()
                {
                    return 10;
                }
            };
        }
    }

}
