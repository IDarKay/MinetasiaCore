package fr.idarkay.minetasia.core.spigot;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.idarkay.minetasia.core.api.BoostType;
import fr.idarkay.minetasia.core.api.Command;
import fr.idarkay.minetasia.core.api.Economy;
import fr.idarkay.minetasia.core.api.MinetasiaCoreApi;
import fr.idarkay.minetasia.core.api.exception.FRSDownException;
import fr.idarkay.minetasia.core.api.exception.PlayerNotFoundException;
import fr.idarkay.minetasia.core.api.utils.*;
import fr.idarkay.minetasia.core.spigot.Executor.*;
import fr.idarkay.minetasia.core.spigot.command.CommandManager;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.kits.KitsManager;
import fr.idarkay.minetasia.core.spigot.listener.*;
import fr.idarkay.minetasia.core.spigot.permission.PermissionManager;
import fr.idarkay.minetasia.core.spigot.gui.GUI;
import fr.idarkay.minetasia.core.spigot.server.ServerManager;
import fr.idarkay.minetasia.core.spigot.user.Player;
import fr.idarkay.minetasia.core.spigot.user.PlayerManager;
import fr.idarkay.minetasia.core.spigot.utils.FRSClient;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.core.spigot.utils.PlayerStatueFixC;
import fr.idarkay.minetasia.core.spigot.utils.SQLManager;
import fr.idarkay.minetasia.normes.MinetasiaLang;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * File <b>MinetasiaCore</b> located on fr.idarkay.mintasia.core.common
 * MinetasiaCore is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 13/11/2019 at 15:06
 */
@SuppressWarnings("unused")
public class MinetasiaCore extends MinetasiaCoreApi {

    public final static JsonParser JSON_PARSER = new JsonParser();

    public final static String HUB_NAME = "hub";

    private final static String LOG_PREFIX = "[Minetasia-Core] ";

    public final List<org.bukkit.entity.Player> socialSpyPlayer = new ArrayList<>();

    private final ConsoleCommandSender console =  this.getServer().getConsoleSender();

    //boost
    public static final Map<BoostType, Float> limit = ImmutableMap.<BoostType, Float>builder()
            .put(BoostType.MINECOINS, 120F)
            .put(BoostType.SHOPEX, 20F)
            .put(BoostType.STARS, 10F)
            .build()
            ;
    public class PartyServerBoost implements Boost
    {

        private final Map<BoostType, Float> boost = new HashMap<>();

        public PartyServerBoost()
        {
            for(BoostType b : BoostType.values())
            {
                boost.put(b, 0.0f);
            }
        }

        @Override
        public Map<BoostType, Float> getBoost()
        {
            return boost;
        }

        public float getBoost(BoostType type)
        {
            float f = boost.get(type);
            return f > limit.get(type) ? limit.get(type) : f;
        }

        public void upgrade(Boost boost)
        {
            boost.getBoost().forEach((k, v) -> this.boost.merge(k, v, Float::sum));
        }

        public void downgrade(Boost boost)
        {
            boost.getBoost().forEach((k, v) -> this.boost.merge(k, v, (a, b) -> a - b < 0 ? 0 : a - b));
        }
    }

    private PartyServerBoost partyServerBoost = new PartyServerBoost();

    private SQLManager sqlManager;
    private FRSClient frsClient;
    private PlayerManager playerManager;
    private ServerManager serverManager;
    private PermissionManager permissionManager;
    private CommandManager commandManager;
    private KitsManager kitsManager;
    private GUI gui;

    private FriendsExecutor friendsExecutor;

    private String serverType;

    private int commands = 0;

    private Cache<Integer, Map<UUID, String>> onlinePlayer;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        // init lang file
        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "Load lang file");
        getMinetasiaLang().init();
        Lang.prefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("prefix")));
        serverType = getConfig().getString("server_type");

        // init db system
        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "Load SQL");
        sqlManager = new SQLManager(this);
        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "Load FRS");
        frsClient = new FRSClient(this);
        frsClient.startConnection(System.out, getConfig().getString("frs.host"), getConfig().getInt("frs.port"),
                getConfig().getString("frs.password"), getConfig().getInt("frs.timeout"));

        // execute sql file
        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "Execute SQL");
        String sqlFileName = "fr/idarkay/minetasia/core/sql/" + getConfig().getString("db.system") + ".sql";
        try (InputStream is = getResource(sqlFileName)) {
            if (is == null) {
                throw new Exception("Couldn't locate schema file for " + sqlFileName);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                List<String> queries = new LinkedList<>();
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("--") || line.startsWith("#")) {
                        continue;
                    }

                    sb.append(line);

                    // check for end of declaration
                    if (line.endsWith(";")) {
                        sb.deleteCharAt(sb.length() - 1);

                        String result = getStatementProcessor().apply(sb.toString().trim());
                        if (!result.isEmpty()) {
                            queries.add(result);
                        }

                        // reset
                        sb = new StringBuilder();
                    }
                }

                try (Connection connection = sqlManager.getSQL()) {
                    boolean utf8mb4Unsupported = false;

                    try (Statement s = connection.createStatement()) {
                        for (String query : queries) {
                            s.addBatch(query);
                        }

                        try {
                            s.executeBatch();
                        } catch (BatchUpdateException e) {
                            if (e.getMessage().contains("Unknown character set")) {
                                utf8mb4Unsupported = true;
                            } else {
                                throw e;
                            }
                        }
                    }

                    // try again
                    if (utf8mb4Unsupported) {
                        try (Statement s = connection.createStatement()) {
                            for (String query : queries) {
                                s.addBatch(query.replace("utf8mb4", "utf8"));
                            }

                            s.executeBatch();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {


        // register permissions
        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "Register permission");
        try {
            PluginManager pm = getServer().getPluginManager();
            PermissionDefault permDefault = getConfig().getBoolean("commands-allow-op") ? PermissionDefault.OP : PermissionDefault.FALSE;

            for (CommandPermission p : CommandPermission.values()) {
                pm.addPermission(new Permission(p.getPermission(), p.getDescription(), permDefault, p.getALLChild()));
            }
        } catch (Exception e) {
            // this throws an exception if the plugin is /reloaded, grr
        }

        //create cache
        onlinePlayer = CacheBuilder.newBuilder().expireAfterWrite(getConfig().getLong("cache.online_player"), TimeUnit.MINUTES).maximumSize(1L).build();

        // load manager
        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "init player manager");
        playerManager = new PlayerManager(this);
        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "init server manager");
        serverManager = new ServerManager(this);
        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "init permission manager");
        permissionManager = new PermissionManager(this);
        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "init command manager");
        commandManager = new CommandManager(this);
        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "init kits manager");
        kitsManager = new KitsManager(this);
        gui = new GUI(this);

        // register command
        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "init commands");
        CustomCommandExecutor customCommandExecutor = new CustomCommandExecutor(this);

        setCommandsIsEnable(Command.PARTY_XP_BOOST.by, getConfig().getBoolean("partyxpboost", true));
        setCommandsIsEnable(Command.FRIEND.by, getConfig().getBoolean("commands.friends", true));
        if(isCommandEnable(Command.FRIEND))
        {
            friendsExecutor = new FriendsExecutor(this);
            Objects.requireNonNull(getCommand("friends")).setExecutor(friendsExecutor);
        }

        setCommandsIsEnable(Command.PERMISSION.by, getConfig().getBoolean("commands.permission", true));
        if(isCommandEnable(Command.PERMISSION))
        {
            Objects.requireNonNull(getCommand("permission")).setExecutor(customCommandExecutor);
        }

        setCommandsIsEnable(Command.MONEY.by, getConfig().getBoolean("commands.permission", true));
        if(isCommandEnable(Command.MONEY))
        {
            Objects.requireNonNull(getCommand("money")).setExecutor(customCommandExecutor);
        }

        setCommandsIsEnable(Command.TP.by, getConfig().getBoolean("commands.permission", true));
        if(isCommandEnable(Command.TP))
        {
            Objects.requireNonNull(getCommand("tp")).setExecutor(customCommandExecutor);
        }

        setCommandsIsEnable(Command.LANG.by, getConfig().getBoolean("commands.lang", true));
        if(isCommandEnable(Command.LANG))
        {
            gui.createLangInventory();
            Objects.requireNonNull(getCommand("lang")).setExecutor(new LangExecutor(this));
        }


        setCommandsIsEnable(Command.HUB.by, getConfig().getBoolean("commands.hub", true));
        if(isCommandEnable(Command.HUB))
        {
            Objects.requireNonNull(getCommand("hub")).setExecutor(new HubExecutor(this));
        }

        setCommandsIsEnable(Command.MSG.by, getConfig().getBoolean("commands.hub", true));
        if(isCommandEnable(Command.MSG))
        {
            Objects.requireNonNull(getCommand("msg")).setExecutor(new MSGExecutor(this));
            Objects.requireNonNull(getCommand("r")).setExecutor(new RExecutor(this));
            Objects.requireNonNull(getCommand("socialspy")).setExecutor(new SocialSpyExecutor(this));
        }

        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "register events");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessageReceivedListener());

        //register Listener
        getServer().getPluginManager().registerEvents(new FRSMessageListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getServer().getPluginManager().registerEvents(new AsyncPlayerChatListener(this), this);

        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "start player count schedule");
        startPlayerCountSchedule();

    }

    private void startPlayerCountSchedule()
    {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> publish("core-server"
                , "playerCount;" + getServer().getName() + ";" + Bukkit.getServer().getOnlinePlayers().size()),
        20, 20 * 10);
    }

    @Override
    public void onDisable() {
        serverManager.disable();
        frsClient.shutdown(true);
    }

    @Override
    public String ping() {
        return "pong";
    }

    @Override
    public fr.idarkay.minetasia.core.api.utils.SQLManager getSqlManager() {
        return sqlManager;
    }

    @Override
    public void setPlayerData(@NotNull UUID uuid, @NotNull String key, String value) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () ->
        {
            Player p;
            if((p = playerManager.get(uuid)) != null)
            {
                p.setData(key, value);
                publish("core-data", "data;" + uuid.toString() + ";" + key + ";" + value);
                getFrsClient().setValue("usersData", uuid.toString(), p.getJson());
            }
        });
    }

    @Override
    public String getPlayerData(@NotNull UUID uuid, @NotNull String key) {
        Player p;
        if((p = playerManager.get(uuid)) != null) return p.getData(key);
        else return null;
    }

    @Override
    public @Nullable UUID getPlayerUUID(@NotNull String username) {
        org.bukkit.entity.Player p;
        if((p = Bukkit.getPlayer(username)) != null) return p.getUniqueId();
        else
        {
            sqlManager.getSQL();
            try(PreparedStatement ps = sqlManager.getSQL().prepareStatement("SELECT uuid FROM `uuid_username` WHERE  username = ?"))
            {
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                if(rs.next())
                {
                    return UUID.fromString(rs.getString("uuid"));
                }
            }catch ( SQLException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public float getPlayerMoney(UUID uuid, Economy economy) {
        Player p = playerManager.get(uuid);
        if(p != null) return p.getMoney(economy);
        else return -1.0F;
    }

    @Override
    public void addPlayerMoney(UUID uuid, Economy economy, float amount, boolean async) {

        Consumer<BukkitTask> c = bukkitTask -> {
            if(amount < 0) throw new IllegalArgumentException("negative money amount");
            Player p = playerManager.get(uuid);
            if(p != null){
                p.addMoney(economy, amount);
                publish("core-data", "money;" + uuid.toString() + ";" + economy.name() + ";" + p.getMoney(economy));
                getFrsClient().setValue("usersData", uuid.toString(), p.getJson());
            } else throw new PlayerNotFoundException("can't add money to not found user");
        };

        if(async)
            Bukkit.getScheduler().runTaskAsynchronously(this, c);
        else
            c.accept(null);
    }

    public void addPlayerMoneys(UUID uuid, Map<Economy, Float> m, boolean async)
    {
        Consumer<BukkitTask> c = bukkitTask -> {
            Player p = playerManager.get(uuid);
            if(p != null)
            {
                m.forEach((k, v )-> {
                    if(v < 0) throw new IllegalArgumentException("negative money amount");

                    p.addMoney(k, v);
                    publish("core-data", "money;" + uuid.toString() + ";" + k.name() + ";" + p.getMoney(k));
                });
                getFrsClient().setValue("usersData", uuid.toString(), p.getJson());
            }
            else throw new PlayerNotFoundException("can't add money to not found user");
        };

        if(async)
            Bukkit.getScheduler().runTaskAsynchronously(this, c);
        else
            c.accept(null);
    }

    @Override
    public void removePlayerMoney(UUID uuid, Economy economy, float amount, boolean async) {

        Consumer<BukkitTask> c = bukkitTask -> {
            if(amount < 0) throw new IllegalArgumentException("negative money amount");
            Player p = playerManager.get(uuid);
            if(p != null){
                p.removeMooney(economy, amount);
                publish("core-data", "money;" + uuid.toString() + ";" + economy.name() + ";" + p.getMoney(economy));
                getFrsClient().setValue("usersData", uuid.toString(), p.getJson());
            } else throw new PlayerNotFoundException("can't remove money to not found user");
        };

        if(async)
            Bukkit.getScheduler().runTaskAsynchronously(this, c);
        else
            c.accept(null);
    }

    @Override
    public void setPlayerMoney(UUID uuid, Economy economy, float amount, boolean async) {

        Consumer<BukkitTask> c = bukkitTask -> {
            if(amount < 0) throw new IllegalArgumentException("negative money amount");
            Player p = playerManager.get(uuid);
            if(p != null){
                p.setMoney(economy, amount);
                publish("core-data", "money;" + uuid.toString() + ";" + economy.name() + ";" + amount);
                getFrsClient().setValue("usersData", uuid.toString(), p.getJson());
            } else throw new PlayerNotFoundException("can't set money to not found user");
        };
        if(async)
            Bukkit.getScheduler().runTaskAsynchronously(this, c);
        else
            c.accept(null);
    }

    @Override
    public @NotNull HashMap<UUID, String> getFriends(@NotNull UUID uuid) {
        Player p = playerManager.get(uuid);
        if (p != null) return p.getFriends();
        return new HashMap<>();
    }

    @Override
    public boolean isFriend(@NotNull UUID uuid, @NotNull UUID uuid2) {
        Player p = playerManager.get(uuid);
        if(p != null) return p.isFriend(uuid2);
        return false;
    }

    @Override
    public void removeFriend(@NotNull UUID uuid, @NotNull UUID uuid2) {
        removeFriend(uuid, uuid2, true);
    }

    private void removeFriend(@NotNull UUID uuid, @NotNull UUID uuid2, boolean r) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            Player p = playerManager.get(uuid);
            if(p != null)
            {
                if(p.removeFriends(uuid2))
                {
                    if(r) removeFriend(uuid2, uuid, false);
                    publish("core-data", "fremove;" + uuid.toString() + ";" + uuid2.toString());
                    getFrsClient().setValue("usersData", uuid.toString(), p.getJson());
                }
            }
        });
    }

    @Override
    public void addFriend(@NotNull UUID uuid, @NotNull UUID uuid2) {
        addFriend(uuid, uuid2, true);
    }

    private void addFriend(@NotNull UUID uuid, @NotNull UUID uuid2, boolean r) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            Player p = playerManager.get(uuid);
            if(p != null)
            {
                if(p.addFriends(uuid2))
                {
                    if(r) addFriend(uuid2, uuid, false);
                    publish("core-data", "fadd;" + uuid.toString() + ";" + uuid2.toString());
                    getFrsClient().setValue("usersData", uuid.toString(), p.getJson());
                }
            }
        });
    }

    @Override
    public boolean isPlayerOnline(@NotNull UUID uuid) {
        if(Bukkit.getPlayer(uuid) != null) return true;
        sqlManager.getSQL();
        try(PreparedStatement ps = sqlManager.getSQL().prepareStatement("SELECT * FROM `online_player` WHERE uuid = ?"))
        {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }catch ( SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isPlayerOnline(@NotNull String name) {
        if(Bukkit.getPlayer(name) != null) return true;
        sqlManager.getSQL();
        try(PreparedStatement ps = sqlManager.getSQL().prepareStatement("SELECT * FROM `online_player` WHERE username = ?"))
        {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }catch ( SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void publish(@NotNull String chanel, String message, boolean... sync) {
        if(frsClient.isConnected())
            frsClient.publish(chanel, message, sync);
        else throw new FRSDownException("can't publish frs is down");
    }

    @Override
    public String getValue(String key, String field) {
        return frsClient.getValue(key, field);
    }

    @Override
    public Set<String> getFields(String key) {
        return frsClient.getFields(key);
    }

    @Override
    public Map<String, String> getValues(String key, Set<String> fields) {
        return frsClient.getValues(key, fields);
    }

    @Override
    public void setValue(String key, String field, String value, boolean... sync) {
        frsClient.setValue(key, field, value, sync);
    }

    @Override
    public void movePlayerToHub(@NotNull org.bukkit.entity.Player player) {

        int i = -1;
        Server sr = null;

        for(Server s : getServers(HUB_NAME).values())
        {
            if(sr == null || i == -1 || s.getPlayerCount() < i) {
                sr = s;
                i = s.getPlayerCount();
            }
        }
        if(sr == null) player.kickPlayer("No valid hub");
         else movePlayerToServer(player, sr);
    }

    @Override
    public void movePlayerToServer(@NotNull org.bukkit.entity.Player player, Server server) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(server.getName());

        player.sendPluginMessage(this, "BungeeCord", output.toByteArray());
    }

    @Override
    public String getPlayerLang(@NotNull UUID uuid) {
        return getPlayerData(uuid, "lang");
    }

    @Override
    public @Nullable String getPlayerName(UUID uuid) {
        org.bukkit.entity.Player pl = Bukkit.getPlayer(uuid);
        if(pl == null)
        {
            Player p = playerManager.get(uuid);
            if(p != null) return p.getName();
        } else return pl.getName();
        return null;
    }

    @Override
    public PlayerStatueFix getPlayerStatue(UUID uuid) {
        sqlManager.getSQL();
        try(PreparedStatement ps = sqlManager.getSQL().prepareStatement("SELECT * FROM `online_player` WHERE uuid = ?"))
        {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                return new PlayerStatueFixC(uuid, rs.getNString("username"), rs.getString("proxy"), getServer(rs.getString("server")));
            }
            else return null;
        }catch ( SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PlayerStatueFix getPlayerStatue(String name) {
        sqlManager.getSQL();
        try(PreparedStatement ps = sqlManager.getSQL().prepareStatement("SELECT * FROM `online_player` WHERE username = ?"))
        {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                return new PlayerStatueFixC(UUID.fromString(rs.getString("uuid")), name, rs.getString("proxy"), getServer(rs.getString("server")));
            }
            else return null;
        }catch ( SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Integer> getPlayerKitsLvl(UUID uuid) {
        String data = getPlayerData(uuid, "kits");
        Map<String, Integer> back = new HashMap<>();
        if(data != null)
        {

            JsonObject jsonObject = JSON_PARSER.parse(data).getAsJsonObject();
            for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet())
            {
                back.put(entry.getKey(), entry.getValue().getAsInt());
            }
        }
        return back;
    }

    @Override
    public Map<String, Integer> getPlayerKitsLvl(UUID uuid, String gameFilter) {
        String data = getPlayerData(uuid, "kits");
        Map<String, Integer> back = new HashMap<>();
        if(data != null)
        {
            JsonObject jsonObject = JSON_PARSER.parse(data).getAsJsonObject();
            for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet())
            {
                if(entry.getKey().startsWith(gameFilter))
                    back.put(entry.getKey(), entry.getValue().getAsInt());
            }
        }
        return back;
    }

    @Override
    public int getPlayerKitLvl(UUID uuid, String kitName) {

        return getPlayerKitsLvl(uuid).getOrDefault(kitName, 0);
    }

    @Override
    public Kit getKitKit(String name, String lang) {
        Kit k = kitsManager.getKits().get(name + "_" + lang);
        if(k == null && !lang.equals(MinetasiaLang.BASE_LANG)) k = kitsManager.getKits().get(name + "_" + MinetasiaLang.BASE_LANG);
        return k;
    }

    @Override
    public void saveDefaultKit(Kit kit) {
        if(!kitsManager.getKits().containsKey(kit.getName() + "_" + kit.getIsoLang()))
        {
            fr.idarkay.minetasia.core.spigot.kits.Kit k = new fr.idarkay.minetasia.core.spigot.kits.Kit(kit);
            setValue("kits", kit.getName() + "_" + kit.getIsoLang(), k.getJsonString());
            kitsManager.getKits().put(kit.getName() + "_" + kit.getIsoLang(), k);
        }
    }

    @Override
    public PlayerStats getPlayerStats(@NotNull UUID uuid)
    {
        Player p = playerManager.get(uuid);
        if(p != null) return p.getStats();
        return null;
    }

    @Override
    public void addStatsToPlayer(@NotNull UUID uuid, @NotNull StatsUpdater statsUpdater, boolean async)
    {
        Validate.notNull(uuid, "uuid can't be null");
        Validate.notNull(statsUpdater, "statsUpdater can't be null");

        Consumer<BukkitTask> c = bukkitTask -> {
            try
            {
                Player p = Objects.requireNonNull(playerManager.get(uuid));
                p.upDateStats(statsUpdater);
                publish("core-data", "stats;" + uuid.toString() + ";" + p.getJsonStats().toString());
                getFrsClient().setValue("userStats", uuid.toString(), p.getJsonStats().toString());
            }
            catch (NullPointerException e)
            {
                throw new PlayerNotFoundException();
            }
        };
        if(async)
            Bukkit.getScheduler().runTaskAsynchronously(this, c);
        else
        c.accept(null);
    }

    @NotNull
    @Override
    public Boost getPlayerPersonalBoost(@NotNull UUID uuid)
    {
        return validateNotNullPlayer(playerManager.get(uuid)).getPersonalBoost();
    }

    @Override
    @NotNull
    public Boost getPlayerPartyBoost(@NotNull UUID uuid)
    {
        return validateNotNullPlayer(playerManager.get(uuid)).getPartyBoost();
    }

    @Override
    public void shutdown() {
        getServer().getOnlinePlayers().forEach(this::movePlayerToHub);
        Bukkit.getScheduler().runTaskLater(this, () ->  getServer().shutdown(), 20L * 10);
    }

    private final static ChatColor moneyColor = ChatColor.GREEN;

    @Override
    public void addGameWonMoneyToPlayer(@NotNull UUID uuid, @NotNull MoneyUpdater moneyUpdater, boolean boost, boolean async)
    {
        if(isCommandEnable(Command.PARTY_XP_BOOST))
        {
            Consumer<BukkitTask> bukkitTaskConsumer = bukkitTask -> {
                Boost playerBoost = getPlayerPersonalBoost(uuid);
                StringBuilder money = new StringBuilder();

                Map<Economy, Float> newMap = new HashMap<>();
                moneyUpdater.getUpdate().forEach((k,v) ->{
                    final float b =  1 + playerBoost.getBoost().getOrDefault(k.boostType, 0f) / 100f + partyServerBoost.getBoost(k.boostType) / 100f;
                    System.out.println(b);
                    newMap.put(k, v * b);
                    if(money.length() > 0) money.append(",");
                    money.append(moneyColor).append(v * b).append(" ").append(k.displayName);
                });

                addPlayerMoneys(uuid, newMap, false);

                String[] toSend = Lang.GAME_REWARDS.getWithoutPrefix(getPlayerLang(uuid), serverType, money.toString()).split("\n");
                org.bukkit.entity.Player p = Bukkit.getPlayer(uuid);
                for(String s : toSend) Objects.requireNonNull(p).sendMessage(s);
            };

            if(async)
                Bukkit.getScheduler().runTaskAsynchronously(this, bukkitTaskConsumer);
            else
                bukkitTaskConsumer.accept(null);
        }
        else
            Bukkit.getLogger().warning("plugin :" + getName() + " want give party won money but server have PARTY_XP_BOOST = false");
    }

    private boolean request = false;

    @Override
    @NotNull
    public Map<UUID, String> getOnlinePlayers() {
        try {
            return onlinePlayer.get(1, () -> {
                sqlManager.getSQL();
                try(PreparedStatement ps = sqlManager.getSQL().prepareStatement("SELECT uuid, username FROM `online_player`"); ResultSet rs = ps.executeQuery())
                {
                    Map<UUID, String> online = new HashMap<>();
                    while (rs.next()) online.put(UUID.fromString(rs.getString("uuid")), rs.getString("username"));
                    request = false;
                    return online;
                }catch ( SQLException e)
                {
                    e.printStackTrace();
                }
                return new HashMap<>();
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    @Override
    @NotNull
    public List<String> getOnlinePlayersForTab() {
       Map<UUID, String>  v = onlinePlayer.getIfPresent(1);
       if(v == null)
       {
           if(!request)
           {
               request = true;
               Bukkit.getScheduler().runTaskAsynchronously(this, this::getOnlinePlayers);
           }
           return getServer().getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
       }
       else return new ArrayList<>(v.values());
    }

    @Override
    public @NotNull String getServerType() {
        return serverType;
    }

    @Override
    public @NotNull Server getThisServer() {
        return serverManager.getServer();
    }

    @Override
    public Server getServer(String name) {
        return serverManager.getServers().get(name);
    }

    @Override
    public @NotNull Map<String, Server> getServers() {
        return serverManager.getServers();
    }

    @Override
    public @NotNull Map<String, Server> getServers(String type) {
        return serverManager.getServers().entrySet().stream().filter(sse -> sse.getKey().startsWith(type)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public boolean isCommandEnable(byte b)
    {
        return ((commands >> b) & 0x1) == 1;
    }

    @Override
    public boolean isCommandEnable(Command c) {
        return isCommandEnable(c.by);
    }

    @Override
    @NotNull
    public String getGroupDisplay(UUID player) {

        byte p = Byte.MIN_VALUE;
        Group g = null;

        for(String gs : getPermissionManager().getGroupsOfUser(player))
        {
            Group group = getPermissionManager().groups.get(gs);
            byte i = group.getPriority();
            if(g == null || i > p)
            {
                p = i;
                g = group;
            }
        }

        if(g == null) return "";
        else return ChatColor.translateAlternateColorCodes('&', g.getDisplayName());
    }

//    @Override
//    public int getKitLevelOfUser(UUID player, String kitName) {
//        String data = getPlayerData(player, "kits");
//        JsonObject jo;
//        if(data != null) jo = JSON_PARSER.parse(data).getAsJsonObject();
//        else return 0;
//        JsonElement lvl = jo.get(kitName);
//        if(lvl == null) return 0;
//        return lvl.getAsInt();
//    }
//
//    @Override
//    public void setKitLvlOfUser(UUID player, String kitName, int lvl) {
//        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
//            String data = getPlayerData(player, "kits");
//            JsonObject jo;
//            if(data != null) jo = JSON_PARSER.parse(data).getAsJsonObject();
//            else jo = new JsonObject();
//            jo.addProperty(kitName, lvl);
//            setPlayerData(player, "kits", jo.toString());
//        });
//    }

    private void setCommandsIsEnable(byte b, boolean value)
    {
        if(value)
            commands |= 1 << b;
        else
            commands &= ~(1 << b);
    }

    public int setBoolIsValue(int bool, byte b, boolean value)
    {
        if(value)
            bool |= 1 << b;
        else
            bool &= ~(1 << b);
        return bool;
    }

    public boolean isBollTrue(int bool, byte b)
    {
        return ((bool >> b) & 0x1) == 1;
    }

    public void setUserName(UUID uuid, String username)
    {
        Bukkit.getScheduler().runTaskAsynchronously(this, () ->
        {
            Player p;
            if((p = playerManager.get(uuid)) != null)
            {
                p.setUsername(username);
                publish("core-data", "name;" + uuid.toString() + ";" + username);
                getFrsClient().setValue("usersData", uuid.toString(), p.getJson());
            }
        });
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public ServerManager getServerManager() {
        return serverManager;
    }

    public FRSClient getFrsClient() {
        return frsClient;
    }

    public FriendsExecutor getFriendsExecutor() {
        return friendsExecutor;
    }

    public GUI getGui() {
        return gui;
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    private Function<String, String> getStatementProcessor() {
        return s -> s.replace("'", "`"); // use backticks for quotes
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public PartyServerBoost getPartyServerBoost()
    {
        return partyServerBoost;
    }

    public static Player validateNotNullPlayer(@Nullable Player player)
    {
        if(player == null) throw new PlayerNotFoundException();
        return player;
    }

}