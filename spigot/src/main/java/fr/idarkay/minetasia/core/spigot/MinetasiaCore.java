package fr.idarkay.minetasia.core.spigot;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.model.Filters;
import fr.idarkay.minetasia.common.ServerConnection.MessageClient;
import fr.idarkay.minetasia.common.ServerConnection.MessageServer;
import fr.idarkay.minetasia.core.api.*;
import fr.idarkay.minetasia.core.api.event.MessageReceivedEvent;
import fr.idarkay.minetasia.core.api.event.PlayerMoveToHubEvent;
import fr.idarkay.minetasia.core.api.event.SocketPrePossesEvent;
import fr.idarkay.minetasia.core.api.exception.PlayerNotFoundException;
import fr.idarkay.minetasia.core.api.utils.*;
import fr.idarkay.minetasia.core.spigot.Executor.*;
import fr.idarkay.minetasia.core.spigot.command.CommandManager;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.kits.KitMain;
import fr.idarkay.minetasia.core.spigot.kits.KitsManager;
import fr.idarkay.minetasia.core.spigot.listener.*;
import fr.idarkay.minetasia.core.spigot.listener.inventory.InventoryClickListener;
import fr.idarkay.minetasia.core.spigot.listener.inventory.InventoryCloseListener;
import fr.idarkay.minetasia.core.spigot.listener.inventory.InventoryDragListener;
import fr.idarkay.minetasia.core.spigot.listener.inventory.InventoryOpenListener;
import fr.idarkay.minetasia.core.spigot.permission.PermissionManager;
import fr.idarkay.minetasia.core.spigot.gui.GUI;
import fr.idarkay.minetasia.core.spigot.server.MineServer;
import fr.idarkay.minetasia.core.spigot.server.ServerManager;
import fr.idarkay.minetasia.core.spigot.user.MinePlayer;
import fr.idarkay.minetasia.core.spigot.user.PlayerManager;
import fr.idarkay.minetasia.core.spigot.utils.*;
import fr.idarkay.minetasia.normes.MinetasiaGUI;
import fr.idarkay.minetasia.normes.Reflection;
import fr.idarkay.minetasia.normes.anontation.MinetasiaGuiNoCallEvent;
import org.apache.commons.lang.Validate;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    private static MinetasiaCore instance;

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

    private MongoDBManager mongoDBManager;
    private PlayerManager playerManager;
    private ServerManager serverManager;
    private PermissionManager permissionManager;
    private CommandManager commandManager;
    private KitsManager kitsManager;
    private GUI gui;

    private FriendsExecutor friendsExecutor;

    private String serverType, prefix = "", serverConfig = "";
    private boolean isHub;

    private int commands = 0, maxPlayerCountAddAdmin;

    private Cache<Integer, Map<UUID, String>> onlinePlayer;
    private MessageServer messageServer;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        instance = this;
        // init lang file
        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "Load lang file");
        getMinetasiaLang().init();
        prefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("prefix")));
        serverConfig = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("config_name")));
        Lang.prefix = prefix;
        Lang.api = this;

        serverType = getConfig().getString("server_type");
        isHub = serverType.startsWith(HUB_NAME);

        maxPlayerCountAddAdmin = getConfig().getInt("max-player-count-add-admin");

        // init db system
        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "Load SQL");
        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "Load FRS");
        messageServer = new MessageServer(getConfig().getInt("publish-port"));
        mongoDBManager = new MongoDBManager(this);
    }

    public void initClientReceiver()
    {
        MessageClient.setReceiver(socket -> Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
        try
        {
            final String msg = MessageClient.read(socket);

            if(msg == null)
            {
                socket.close();
                return;
            }

            final String[] split = msg.split(";", 2);
            if(split.length == 0)
            {
                socket.close();
                return;
            }


            final SocketPrePossesEvent e = new SocketPrePossesEvent(socket, split.length == 1 ? "none" : split[0], split.length == 1 ? split[0] : split[1]);
            Bukkit.getPluginManager().callEvent(e);

            if(e.getAnswer() != null)
            {
                MessageClient.send(socket, e.getAnswer());
            }

            socket.close();

            if(!e.isCancelled())
            {
                Bukkit.getPluginManager().callEvent(new MessageReceivedEvent(split.length == 1 ? "none" : split[0], split.length == 1 ? split[0] : split[1]));
            }
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }));
    }

    @Override
    public void onEnable() {

        try {
            PluginManager pm = getServer().getPluginManager();
            PermissionDefault permDefault = getConfig().getBoolean("commands-allow-op") ? PermissionDefault.OP : PermissionDefault.FALSE;

            // register command permissions (core)
            console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "Register core permission");
            for (CommandPermission p : CommandPermission.values()) {
                console.sendMessage(ChatColor.GRAY + LOG_PREFIX + "Register permission : " + p.getPermission());
                pm.addPermission(new Permission(p.getPermission(), p.getDescription(), permDefault, p.getALLChild()));
            }

            // register general permissions
            console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "Register general permission");
            for (GeneralPermission p : GeneralPermission.values()) {
                console.sendMessage(ChatColor.GRAY + LOG_PREFIX + "Register permission : " + p.getPermission());
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
        registerListener();

        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "start player count schedule");
        startPlayerCountSchedule();

    }

    private void registerListener()
    {
        getServer().getPluginManager().registerEvents(new FRSMessageListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryDragListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryOpenListener(), this);
        getServer().getPluginManager().registerEvents(new AsyncPlayerChatListener(this), this);
    }

    private void startPlayerCountSchedule()
    {
        //todo: change
    }

    @Override
    public void onDisable() {
        serverManager.disable();
        mongoDBManager.close();
    }

    @Override
    public String ping() {
        return "pong";
    }

    @Override
    public void setPlayerData(@NotNull UUID uuid, @NotNull String key, Object value) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () ->
        {
            MinePlayer p;
            if((p = playerManager.get(uuid)) != null)
            {
                p.putData(key, value);
            }
        });
    }

    @Deprecated
    public void setGeneralPlayerData(@NotNull UUID uuid, @NotNull String key, Object value) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () ->
        {
            MinePlayer p;
            if((p = playerManager.get(uuid)) != null)
            {
                p.putGeneralData(key, value);
            }
        });
    }

    @Override
    public Object getPlayerData(@NotNull UUID uuid, @NotNull String key) {
        MinePlayer p;
        if((p = playerManager.get(uuid)) != null) return p.getData(key);
        else return null;
    }

    @Override
    public <T> T getPlayerData(@NotNull UUID uuid, @NotNull String key, Class<T> cast)
    {
        return cast.cast(getPlayerData(uuid, key));
    }

    @Deprecated
    public Object getGeneralPlayerData(@NotNull UUID uuid, @NotNull String key) {
        MinePlayer p;
        if((p = playerManager.get(uuid)) != null) return p.getGeneralData(key);
        else return null;
    }

    @Override
    public @Nullable UUID getPlayerUUID(@NotNull String username) {
        final Player p = Bukkit.getPlayer(username);
        if(p != null) return p.getUniqueId();
        final Document d = mongoDBManager.getCollection(MongoCollections.USERS).find(Filters.eq("username", username)).first();
        if (d == null) return null;
        else return UUID.fromString(d.getString("_id"));
    }

    @Override
    public double getPlayerMoney(UUID uuid, Economy economy) {
        MinePlayer p = playerManager.get(uuid);
        if(p != null) return p.getMoney(economy);
        else return -1.0F;
    }

    @Override
    public void addPlayerMoney(UUID uuid, Economy economy, float amount, boolean async) {

        final Consumer<BukkitTask> c = bukkitTask -> {
            if(amount < 0) throw new IllegalArgumentException("negative money amount");
            MinePlayer p = playerManager.get(uuid);
            if(p != null)
            {
                p.addMoney(economy, amount);
            }
            else throw new PlayerNotFoundException("can't add money to not found user");
        };

        if(async)
            Bukkit.getScheduler().runTaskAsynchronously(this, c);
        else
            c.accept(null);
    }

    public void addPlayerMoneys(UUID uuid, Map<Economy, Float> m, boolean async)
    {
        final Consumer<BukkitTask> c = bukkitTask -> {
            MinePlayer p = playerManager.get(uuid);
            if(p != null)
            {
                m.forEach((k, v )-> {
                    if(v < 0) throw new IllegalArgumentException("negative money amount");

                    p.addMoneyWithoutSave(k, v);
                });
//                p.saveGeneralData();
                //todo:
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

        final Consumer<BukkitTask> c = bukkitTask -> {
            if(amount < 0) throw new IllegalArgumentException("negative money amount");
            MinePlayer p = playerManager.get(uuid);
            if(p != null){
                p.removeMoney(economy, amount);
            } else throw new PlayerNotFoundException("can't remove money to not found user");
        };

        if(async)
            Bukkit.getScheduler().runTaskAsynchronously(this, c);
        else
            c.accept(null);
    }

    @Override
    public void setPlayerMoney(UUID uuid, Economy economy, float amount, boolean async) {

        final Consumer<BukkitTask> c = bukkitTask -> {
            if(amount < 0) throw new IllegalArgumentException("negative money amount");
            final MinePlayer p = playerManager.get(uuid);
            if(p != null){
                p.setMoney(economy, amount);
            } else throw new PlayerNotFoundException("can't set money to not found user");
        };
        if(async)
            Bukkit.getScheduler().runTaskAsynchronously(this, c);
        else
            c.accept(null);
    }

    @Override
    public @NotNull Map<UUID, String> getFriends(@NotNull UUID uuid) {
        MinePlayer p = playerManager.get(uuid);
        if (p != null) return p.getFriends();
        return new HashMap<>();
    }

    @Override
    public boolean isFriend(@NotNull UUID uuid, @NotNull UUID uuid2) {
        MinePlayer p = playerManager.get(uuid);
        if(p != null) return p.isFriend(uuid2);
        return false;
    }

    @Override
    public void removeFriend(@NotNull UUID uuid, @NotNull UUID uuid2) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            final MinePlayer p = playerManager.get(uuid);
            if(p != null)
            {
                p.removeFriends(uuid);
            }
        });
    }

    @Override
    public void addFriend(@NotNull UUID uuid, @NotNull UUID uuid2) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            MinePlayer p = playerManager.get(uuid);
            if(p != null)
            {
                p.addFriends(uuid2);
            }
        });
    }


    @Override
    public boolean isPlayerOnline(@NotNull UUID uuid)
    {
        if(Bukkit.getPlayer(uuid) != null) return true;
        return mongoDBManager.match(MongoCollections.ONLINE_USERS, uuid.toString());
    }

    @Override
    public boolean isPlayerOnline(@NotNull String name) {
        if(Bukkit.getPlayer(name) != null) return true;
        return mongoDBManager.match(MongoCollections.ONLINE_USERS, "username", name);
    }

    public List<UUID> getPlayerOnlineUUID()
    {
        return StreamSupport.stream(mongoDBManager.getAll(MongoCollections.ONLINE_USERS).spliterator(), false).map(document -> UUID.fromString(document.getString("_id"))).collect(Collectors.toList());
    }

    public List<String> getPlayerOnlineName()
    {
        return StreamSupport.stream(mongoDBManager.getAll(MongoCollections.ONLINE_USERS).spliterator(), false).map(document -> document.getString("username")).collect(Collectors.toList());
    }

    @Override
    public void publishGlobal(@NotNull String chanel, String message, boolean proxy, boolean sync)
    {
        Validate.notNull(chanel);
        final Runnable run = () -> {

            final String fullMsg = chanel + ";" + (message == null ? "" : message);

            final Map<String, Integer> ipPortMap = getServers().values().stream().collect(Collectors.toMap(Server::getIp, Server::getPublishPort));

            if(proxy)
            {
                //todo: add proxy
            }

            ipPortMap.forEach((ip, port) -> MessageClient.send(ip, port, message, false));

        };

        if(sync) Bukkit.getScheduler().runTaskAsynchronously(this, run);
        else Bukkit.getScheduler().runTask(this, run);
    }


    @Override
    public void publishServerType(@NotNull String chanel, String message, String serverType, boolean sync)
    {
        Validate.notNull(chanel);
        final Runnable run = () -> {

            final String fullMsg = chanel + ";" + (message == null ? "" : message);

            getServers(serverType).values().stream().collect(Collectors.toMap(Server::getIp, Server::getPublishPort)).forEach((ip, port) -> MessageClient.send(ip, port, message, false));

        };

        if(sync) Bukkit.getScheduler().runTaskAsynchronously(this, run);
        else Bukkit.getScheduler().runTask(this, run);
    }

    @Override
    public String publishTarget(@NotNull String chanel, String message, Server target, boolean rep, boolean sync)
    {
        Validate.notNull(chanel);
        Validate.notNull(target);

        if(rep && !sync) throw new IllegalArgumentException("cant get rep in async");

        final String fullMsg = chanel + ";" + (message == null ? "" : message);
        if(sync)
        {
            return MessageClient.send(target.getIp(), target.getPublishPort(), fullMsg, rep);
        }
        else
        {
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> MessageClient.send(target.getIp(), target.getPublishPort(), fullMsg, false));
            return null;
        }
    }

    @Override
    public String publishTargetPlayer(@NotNull String chanel, String message, UUID target, boolean rep, boolean sync)
    {
        return publishTarget(chanel, message, getPlayerStatue(target).getServer(), rep, sync);
    }

    @Override
    public String publishTargetPlayer(@NotNull String chanel, String message, PlayerStatueFix target, boolean rep, boolean sync)
    {
        return publishTarget(chanel, message, target.getServer(), rep, sync);
    }

    @Override
    public void movePlayerToHub(@NotNull org.bukkit.entity.Player player)
    {

        Bukkit.getScheduler().runTaskAsynchronously(this, () ->
        {
            Bukkit.getPluginManager().callEvent(new PlayerMoveToHubEvent(player));
            int i = -1;
            Server sr = null;

            for(Server s : getServers(HUB_NAME).values())
            {
                if(sr == null || i == -1 || s.getPlayerCount() < i) {
                    sr = s;
                    i = s.getPlayerCount();
                }
            }
            if(sr == null) Bukkit.getScheduler().runTask(this, () -> player.kickPlayer("No valid hub"));
            else  movePlayerToServer(player, sr);
        });
    }

    @Override
    public void movePlayerToServer(@NotNull org.bukkit.entity.Player player, Server server) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(server.getName());

        player.sendPluginMessage(this, "BungeeCord", output.toByteArray());
    }

    @Override
    public void movePlayerToServer(@NotNull UUID player, Server server)
    {
        final Player p = Bukkit.getPlayer(player);
        if(p != null) movePlayerToServer(p, server);
        else
        {
            publishTargetPlayer("core-player-tp", player.toString() + ";" +  server.getName(), player, false, false);
        }
    }

    @Override
    public String getPlayerLang(@NotNull UUID uuid) {
        return getPlayer(uuid).getLang();
    }

    @Override
    public @Nullable String getPlayerName(UUID uuid) {
        org.bukkit.entity.Player p;
        if((p = Bukkit.getPlayer(uuid)) != null) return p.getName();
        else
        {
            final Document d = mongoDBManager.getByKey(MongoCollections.USERS, uuid.toString());
            if(d == null) return null;
            else return d.getString("username");
        }
    }

    @Override
    public PlayerStatueFix getPlayerStatue(UUID uuid) {
        final Document d = mongoDBManager.getWithReferenceAndMatch(MongoCollections.ONLINE_USERS, "_id", uuid.toString(), "servers", "server_id", "_id", "server").first();
        if(d == null) return null;

        return new PlayerStatueFixC(
                uuid,
                d.getString("username"),
                d.getString("proxy_id"),
                MineServer.getServerFromDocument(d.getList("server", Document.class).get(0))
        );
    }

    @Override
    public PlayerStatueFix getPlayerStatue(String name) {
        final Document d = mongoDBManager.getWithReferenceAndMatch(MongoCollections.ONLINE_USERS, "username", name, "servers", "server_id", "_id", "server").first();
        if(d == null) return null;

        return new PlayerStatueFixC(
                UUID.fromString(d.getString("_id")),
                name,
                d.getString("proxy_id"),
                MineServer.getServerFromDocument(d.getList("server", Document.class).get(0))
        );
    }

    public Document getPlayerKitDocument(UUID uuid)
    {
        return getPlayerData(uuid, "kits", Document.class);
    }

    @Override
    public Map<String, Integer> getPlayerKitsLvl(UUID uuid) {
        return getPlayerKitDocument(uuid).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> ((Integer) e.getValue())));
    }

    @Override
    public Map<String, Integer> getPlayerKitsLvl(UUID uuid, String gameFilter) {
        return getPlayerKitDocument(uuid).entrySet().stream().filter(e -> e.getKey().startsWith(gameFilter)).collect(Collectors.toMap(Map.Entry::getKey, e -> ((Integer) e.getValue())));
    }

    @Override
    public int getPlayerKitLvl(UUID uuid, String kitName) {
        return getPlayerKitDocument(uuid).getInteger(kitName, 0);
    }

    @Override
    public Kit getKitKit(String name, String lang) {
        return getKitLang(name, lang);
    }

    @Override
    public MainKit getMainKit(String name)
    {
        return kitsManager.getKits().get(name);
    }

    @Override
    public Kit getKitLang(String kitName, String lang)
    {
        return getMainKit(kitName).getLang(lang);
    }

    @Override
    public void saveDefaultKit(MainKit kit)
    {
        if(!kitsManager.getKits().containsKey(kit.getName()))
        {
            kitsManager.getKits().put(kit.getName(), kit);
            mongoDBManager.insert(MongoCollections.KITS, kit.toDocument());
        }
    }

    @Override
    public MainKit createKit(String isoLang, String name, String displayName, int maxLvl, int[] price, Material displayMat, String[] lvlDesc, String... desc)
    {
        return new KitMain(isoLang, name, displayName, maxLvl, price, displayMat, lvlDesc, desc);
    }


    @Override
    public PlayerStats getPlayerStats(@NotNull UUID uuid)
    {
        MinePlayer p = playerManager.get(uuid);
        if(p != null) return p.getStats();
        return null;
    }

    @Override
    public void addStatsToPlayer(@NotNull UUID uuid, @NotNull StatsUpdater statsUpdater, boolean async)
    {
        Validate.notNull(uuid, "uuid can't be null");
        Validate.notNull(statsUpdater, "statsUpdater can't be null");

        final Consumer<BukkitTask> c = bukkitTask -> {
            try
            {
                final MinePlayer p = Objects.requireNonNull(playerManager.get(uuid));
                p.updatePlayerStats(statsUpdater);
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
                    if(v * b > 0)
                    {
                        if(money.length() > 0) money.append(",");
                        money.append(moneyColor).append(v * b).append(" ").append(k.displayName);
                    }
                });

                addPlayerMoneys(uuid, newMap, false);

                String[] toSend = Lang.GAME_REWARDS.getWithoutPrefix(getPlayerLang(uuid), Lang.Argument.SERVER_TYPE.match(serverType), Lang.Argument.REWARDS.match(money.toString())).split("\n");
                org.bukkit.entity.Player p = Bukkit.getPlayer(uuid);
                if(p != null)
                    for(String s : toSend) p.sendMessage(s);
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
        return StreamSupport.stream(mongoDBManager.getAll(MongoCollections.ONLINE_USERS).spliterator(), false).collect(Collectors.toMap(document -> UUID.fromString(document.getString("_id")), document -> document.getString("username")));
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
        return serverManager.getServer(name);
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

    @Override
    public void setServerPhase(@NotNull ServerPhase phase)
    {
        Validate.notNull(phase);
        //check if max player is not -1
        if(phase != ServerPhase.LOAD && getThisServer().getMaxPlayerCount() < 0) throw new IllegalArgumentException("cant change phase without set maxPlayerCount !");
        getThisServer().setPhase(phase);
        if(phase == ServerPhase.STARTUP)
        {
            serverManager.registerServer();
            messageServer.open();
        }
        else
        {
            //add place for admin
            if(phase == ServerPhase.GAME)
            {
                setMaxPlayerCount( getThisServer().getMaxPlayerCount() + maxPlayerCountAddAdmin, false);
            }
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                mongoDBManager.getCollection(MongoCollections.SERVERS).updateOne(Filters.eq(getThisServer().getName()), new Document("phase", phase.ordinal()));
            });
        }
    }

    @Override
    public @NotNull ServerPhase getServerPhase()
    {
        return getThisServer().getServerPhase();
    }

    @Override
    public void setMaxPlayerCount(int maxPlayer)
    {
        setMaxPlayerCount(maxPlayer, true);
        getThisServer().setMaxPlayerCount(maxPlayer);
    }

    @Override
    public int getMaxPlayerCount()
    {
        return getThisServer().getServerPhase() == ServerPhase.GAME || getThisServer().getServerPhase() == ServerPhase.END ? getThisServer().getMaxPlayerCount() - maxPlayerCountAddAdmin : getThisServer().getMaxPlayerCount();
    }

    @Override
    public boolean isHub()
    {
        return isHub;
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
    @NotNull
    public String getPrefix()
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
        return playerManager.get(uuid);
    }

    @Override
    public MongoDbManager getMongoDbManager()
    {
        return mongoDBManager;
    }

    public void setMaxPlayerCount(int maxPlayer, boolean startup)
    {
        if(startup && getThisServer().getServerPhase() != ServerPhase.LOAD) throw new IllegalArgumentException("can set maxPlayerCount only in Load Phase !");
        try
        {
            Object playerList = Reflection.getCraftBukkitClass("CraftServer").getDeclaredMethod("getHandle").invoke(Bukkit.getServer());
            Field maxPlayers =  Reflection.getField(playerList.getClass().getSuperclass(), "maxPlayers", true);
            maxPlayers.set(playerList, maxPlayer);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

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
            MinePlayer p;
            if((p = playerManager.get(uuid)) != null)
            {
                p.setUsername(username);
            }
        });
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public ServerManager getServerManager() {
        return serverManager;
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

    public static MinePlayer validateNotNullPlayer(@Nullable MinePlayer player)
    {
        if(player == null) throw new PlayerNotFoundException();
        return player;
    }

    public static MinetasiaCore getCoreInstance()
    {
        return instance;
    }

    public MessageServer getMessageServer()
    {
        return messageServer;
    }
}