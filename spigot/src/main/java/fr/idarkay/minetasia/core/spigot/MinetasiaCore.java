package fr.idarkay.minetasia.core.spigot;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonParser;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import fr.idarkay.minetasia.common.MongoCollections;
import fr.idarkay.minetasia.common.ServerConnection.MessageClient;
import fr.idarkay.minetasia.common.ServerConnection.MessageServer;
import fr.idarkay.minetasia.common.message.*;
import fr.idarkay.minetasia.core.api.*;
import fr.idarkay.minetasia.core.api.advancement.AdvancementFrame;
import fr.idarkay.minetasia.core.api.advancement.AdvancementIcon;
import fr.idarkay.minetasia.core.api.advancement.MinetasiaBaseAdvancement;
import fr.idarkay.minetasia.core.api.event.AsyncMinetasiaPacketCombingEvent;
import fr.idarkay.minetasia.core.api.event.MessageReceivedEvent;
import fr.idarkay.minetasia.core.api.event.SocketPrePossesEvent;
import fr.idarkay.minetasia.core.api.exception.PlayerNotFoundException;
import fr.idarkay.minetasia.core.api.utils.*;
import fr.idarkay.minetasia.core.spigot.advancement.AdvancementManager;
import fr.idarkay.minetasia.core.spigot.advancement.MinetasiaAdvancement;
import fr.idarkay.minetasia.core.spigot.command.CommandManager;
import fr.idarkay.minetasia.core.spigot.command.CommandPermission;
import fr.idarkay.minetasia.core.spigot.executor.*;
import fr.idarkay.minetasia.core.spigot.executor.sanction.AdminSanctionCommand;
import fr.idarkay.minetasia.core.spigot.executor.sanction.SanctionCommand;
import fr.idarkay.minetasia.core.spigot.executor.sanction.UnSanctionCommand;
import fr.idarkay.minetasia.core.spigot.gui.GUI;
import fr.idarkay.minetasia.core.spigot.gui.LangGui;
import fr.idarkay.minetasia.core.spigot.kits.KitMain;
import fr.idarkay.minetasia.core.spigot.kits.KitsManager;
import fr.idarkay.minetasia.core.spigot.listener.AsyncPlayerChatListener;
import fr.idarkay.minetasia.core.spigot.listener.MessageListener;
import fr.idarkay.minetasia.core.spigot.listener.PlayerListener;
import fr.idarkay.minetasia.core.spigot.listener.PluginMessageReceivedListener;
import fr.idarkay.minetasia.core.spigot.listener.inventory.InventoryClickListener;
import fr.idarkay.minetasia.core.spigot.listener.inventory.InventoryCloseListener;
import fr.idarkay.minetasia.core.spigot.listener.inventory.InventoryDragListener;
import fr.idarkay.minetasia.core.spigot.listener.inventory.InventoryOpenListener;
import fr.idarkay.minetasia.core.spigot.messages.CoreMessage;
import fr.idarkay.minetasia.core.spigot.messages.CorePacketManger;
import fr.idarkay.minetasia.core.spigot.messages.SanctionMessage;
import fr.idarkay.minetasia.core.spigot.messages.ServerMessage;
import fr.idarkay.minetasia.core.spigot.moderation.PlayerSanction;
import fr.idarkay.minetasia.core.spigot.moderation.SanctionManger;
import fr.idarkay.minetasia.core.spigot.moderation.SanctionType;
import fr.idarkay.minetasia.core.spigot.moderation.report.ReportManager;
import fr.idarkay.minetasia.core.spigot.old_combats.OldCombatsManger;
import fr.idarkay.minetasia.core.spigot.permission.PermissionManager;
import fr.idarkay.minetasia.core.spigot.runnable.IpRunnable;
import fr.idarkay.minetasia.core.spigot.server.MineServer;
import fr.idarkay.minetasia.core.spigot.server.ServerManager;
import fr.idarkay.minetasia.core.spigot.settings.SettingsManager;
import fr.idarkay.minetasia.core.spigot.user.CorePlayer;
import fr.idarkay.minetasia.core.spigot.user.MinePlayer;
import fr.idarkay.minetasia.core.spigot.user.PartyManager;
import fr.idarkay.minetasia.core.spigot.user.PlayerManager;
import fr.idarkay.minetasia.core.spigot.utils.*;
import fr.idarkay.minetasia.normes.MinetasiaGUI;
import fr.idarkay.minetasia.normes.Reflection;
import fr.idarkay.minetasia.normes.Tuple;
import org.apache.commons.lang.Validate;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
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

    public static class PartyServerBoost implements Boost
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

    private final PartyServerBoost partyServerBoost = new PartyServerBoost();

    private MongoDBManager mongoDBManager;
    private PlayerManager playerManager;
    private ServerManager serverManager;
    private PermissionManager permissionManager;
    private CommandManager commandManager;
    private KitsManager kitsManager;
    private PartyManager partyManager;
    private AdvancementManager advancementManager;
    private PlayerListManager playerListManager;
    private SettingsManager settingsManager;
    private SanctionManger sanctionManger;
    private ReportManager reportManager;
    private OldCombatsManger oldCombatsManger;
    private GUI gui;

    private IpRunnable ipRunnable;

    private String serverType, prefix = "", serverConfig = "", ip = "";
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
        Lang.minetasiaLang = getMinetasiaLang();
        prefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("prefix")));
        serverConfig = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("config_name")));
        Lang.prefix = prefix;
        Lang.api = this;
        serverType = getConfig().getString("server_type");
        ip = getConfig().getString("ip");
        isHub = serverType.startsWith(HUB_NAME);

        maxPlayerCountAddAdmin = getConfig().getInt("max-player-count-add-admin");

        // init db system
        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "Load messaging system");

        messageServer = new MessageServer(getConfig().getInt("publish-port"));

        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "Load mongo");
        mongoDBManager = new MongoDBManager(Objects.requireNonNull(this.getConfig().getString("dbm.host")),
                                            Objects.requireNonNull(this.getConfig().getString("dbm.dbname")));
    }

    public void initClientReceiver()
    {
        final JsonParser packetJsonParser = new JsonParser();
        MessageClient.setReceiver(socket -> {
            if(this.isEnabled())
            {
                Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                    try
                    {
                        final String msg = MessageClient.read(socket);
                        if(msg == null || msg.isEmpty())
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

                        String channel = split[0];

                        if(split.length == 2 && channel.startsWith(MinetasiaPacketManager.channelPrefix))
                        {
                            MessageOutChanel<?, ?> messageInChanel = MinetasiaPacketManager.getMessageOutChanel(channel);
                            if(messageInChanel != null)
                            {
                                MinetasiaPacket packet = messageInChanel.getPacketSerializer().read(packetJsonParser.parse(split[1]).getAsJsonObject());
                                final AsyncMinetasiaPacketCombingEvent event = new AsyncMinetasiaPacketCombingEvent(packet);
                                if(event.isNeedRep() && event.getRep() != null)
                                {
                                    MessageClient.send(socket, event.getRep().write().toString());
                                }
                                socket.close();
                                return;

                            }
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
                });
            }
        });
    }

    @Override
    public void onEnable() {
        super.onEnable();
        try {
            PluginManager pm = getServer().getPluginManager();
            PermissionDefault permDefault = PermissionDefault.OP;

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
        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "init party manager");
        partyManager = new PartyManager(this);
        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "init advancement manager");
        MinetasiaAdvancement.initLangToLoad(getConfig());
        advancementManager = new AdvancementManager(this);
        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "init settings manager");
        settingsManager = new SettingsManager(this);
        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "init sanction manager");
        sanctionManger = new SanctionManger(this);
        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "init report manager");
        reportManager = new ReportManager(this.getConfig());
        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "init old combats manager");
        oldCombatsManger = new OldCombatsManger(this);
        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "init gui");
        gui = new GUI(this);

        // register command
        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "init commands");
        CustomCommandExecutor customCommandExecutor = new CustomCommandExecutor(this);

        setCommandsIsEnable(Command.PARTY_XP_BOOST.by, getConfig().getBoolean("partyxpboost", true));
        setCommandsIsEnable(Command.FRIEND.by, getConfig().getBoolean("commands.friends", true));
        if(isCommandEnable(Command.FRIEND))
        {
            Objects.requireNonNull(getCommand("friends")).setExecutor(customCommandExecutor);
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
            Objects.requireNonNull(getCommand("lang")).setExecutor(new LangExecutor(this));
        }


        setCommandsIsEnable(Command.HUB.by, getConfig().getBoolean("commands.hub", true));
        if(isCommandEnable(Command.HUB))
        {
            Objects.requireNonNull(getCommand("hub")).setExecutor(new HubExecutor(this));
        }

        setCommandsIsEnable(Command.PARTY.by, getConfig().getBoolean("commands.party", true));
        if(isCommandEnable(Command.PARTY))
        {
            Objects.requireNonNull(getCommand("party")).setExecutor(customCommandExecutor);
        }

        setCommandsIsEnable(Command.MSG.by, getConfig().getBoolean("commands.hub", true));
        if(isCommandEnable(Command.MSG))
        {
            Objects.requireNonNull(getCommand("msg")).setExecutor(new MSGExecutor(this));
            Objects.requireNonNull(getCommand("r")).setExecutor(new RExecutor(this));
            Objects.requireNonNull(getCommand("socialspy")).setExecutor(new SocialSpyExecutor(this));
        }

        Objects.requireNonNull(getCommand("help")).setExecutor(customCommandExecutor);
        Objects.requireNonNull(getCommand("playerdata")).setExecutor(customCommandExecutor);
        Objects.requireNonNull(getCommand("broadcast")).setExecutor(customCommandExecutor);
        Objects.requireNonNull(getCommand("whitelist")).setExecutor(customCommandExecutor);
        Objects.requireNonNull(getCommand("maintenance")).setExecutor(customCommandExecutor);
        Objects.requireNonNull(getCommand("settingseditor")).setExecutor(new SettingsEditorExecutor(this));
        Objects.requireNonNull(getCommand("sanction")).setExecutor(new SanctionCommand(this));

        Objects.requireNonNull(getCommand("ban")).setExecutor(new AdminSanctionCommand(this, CommandPermission.BAN, SanctionType.BAN));
        Objects.requireNonNull(getCommand("mute")).setExecutor(new AdminSanctionCommand(this, CommandPermission.MUTE, SanctionType.MUTE));
        Objects.requireNonNull(getCommand("warn")).setExecutor(new AdminSanctionCommand(this, CommandPermission.WARN, SanctionType.WARN));

        Objects.requireNonNull(getCommand("unban")).setExecutor(new UnSanctionCommand(this, CommandPermission.UN_BAN, SanctionType.BAN));
        Objects.requireNonNull(getCommand("unmute")).setExecutor(new UnSanctionCommand(this, CommandPermission.UN_MUTE, SanctionType.MUTE));
        Objects.requireNonNull(getCommand("unwarn")).setExecutor(new UnSanctionCommand(this, CommandPermission.UN_WARN, SanctionType.WARN));

        Objects.requireNonNull(getCommand("report")).setExecutor(new ReportExecutor(this));
        Objects.requireNonNull(getCommand("stop")).setExecutor(new StopExecutor());

        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "register events");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessageReceivedListener());

        //register Listener
        registerListener();

        console.sendMessage(ChatColor.GREEN + LOG_PREFIX + "init messaging");
        CorePacketManger.init();
        initClientReceiver();
        messageServer.open();


        //for sign
//        registerPlayerPacketComingEvent();

        this.ipRunnable = new IpRunnable(this);
        this.ipRunnable.runTaskTimerAsynchronously(this, 20L, 8L);
        this.playerListManager = new PlayerListManager(this);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new PlayerCountScheduler(), 0L, 20L);

        setCommandsIsEnable(Command.TAB_RANK.by, getConfig().getBoolean("commands.rank_in_tab", true));
        if(isCommandEnable(Command.TAB_RANK))
        {
           Bukkit.getScheduler().runTaskLater(this, this.permissionManager::loadTabGroup, 1L);
        }



        Bukkit.getScheduler().runTaskLater(this, () -> {
            if(getConfig().getBoolean("default-register"))
            {
                this.setMaxPlayerCount(60);
                this.setServerPhase(ServerPhase.STARTUP);
            }
        }, 1);

    }


    private void registerListener()
    {
        getServer().getPluginManager().registerEvents(new MessageListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryDragListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryOpenListener(), this);
        getServer().getPluginManager().registerEvents(new AsyncPlayerChatListener(this), this);
    }

    public class PlayerCountScheduler implements Runnable
    {
        private volatile int lastPlayerCount = 0;

        @Override
        public void run()
        {
            final int c = Bukkit.getOnlinePlayers().size();
            if(c != lastPlayerCount)
            {
                lastPlayerCount = c;
                serverManager.sayServerUpdate(CoreMessage.CHANNEL, ServerMessage.getMessage(ServerMessage.PLAYER_COUNT, MinetasiaCore.this.getThisServer().getName(), c), true);
            }
        }
    }

    @Override
    public void onDisable() {
        serverManager.disable();
        mongoDBManager.close();
        messageServer.close();
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
        final Document d = mongoDBManager.getCollection(MongoCollections.USERS).find(Filters.regex("username",  "^" + username + "$", "i")).first();
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
                p.incrementMoney(m);
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
    public @NotNull Map<UUID, Tuple<String, String>> getFriends(@NotNull UUID uuid) {
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
                p.removeFriends(uuid2);
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
            if(proxy)
            {
                mongoDBManager.getAll(MongoCollections.PROXY).forEach(document -> MessageClient.send(document.getString("ip"), document.getInteger("publish_port"), fullMsg, false));
            }

            mongoDBManager.getAll(MongoCollections.SERVERS).forEach(document -> MessageClient.send(document.getString("ip"), document.getInteger("publish_port"), fullMsg, false));

//            final Map<String, Integer> ipPortMap = getServers().values().stream().collect(Collectors.toMap(Server::getIp, Server::getPublishPort));

//            ipPortMap.forEach((ip, port) -> MessageClient.send(ip, port, fullMsg, false));

        };

        if(!sync) Bukkit.getScheduler().runTaskAsynchronously(this, run);
        else run.run();
    }

    @Override
    public void publishProxy(@NotNull String chanel, String message, boolean sync)
    {
        Validate.notNull(chanel);
        final Runnable run = () -> {

            final String fullMsg = chanel + ";" + (message == null ? "" : message);

            mongoDBManager.getAll(MongoCollections.PROXY).forEach(document -> MessageClient.send(document.getString("ip"), document.getInteger("publish_port"), fullMsg, false));
        };

        if(!sync) Bukkit.getScheduler().runTaskAsynchronously(this, run);
        else run.run();
    }

    public void publishHub(@NotNull String chanel, String message, boolean sync)
    {
       publishServerType(chanel, message, "hub", sync);
    }

    @Override
    public void publishServerType(@NotNull String chanel, String message, String serverType, boolean sync)
    {
        Validate.notNull(chanel);
        final Runnable run = () -> {

            final String fullMsg = chanel + ";" + (message == null ? "" : message);

            mongoDBManager.getCollection(MongoCollections.SERVERS).find(Filters.regex("type", "^" + serverType +"$", "i")).forEach(document -> MessageClient.send(document.getString("ip"), document.getInteger("publish_port"), fullMsg, false));

//            List<String> pas = new ArrayList<>();
//            getServers(serverType).forEach((name, server) -> {
//                System.out.println(name + " " + fullMsg);
//                if(!pas.contains(server.getIp() + ";" + server.getPort()))
//                {
//                    pas.add(server.getIp() + ";" + server.getPort());
//                    MessageClient.send(server.getIp(), server.getPublishPort(), fullMsg, false);
//                }
//
//            });
        };

        if(!sync) Bukkit.getScheduler().runTaskAsynchronously(this, run);
        else run.run();
    }

    @Override
    public void publishServerTypeRegex(@NotNull String chanel, String message, String regex, boolean sync)
    {
        Validate.notNull(chanel);
        final Runnable run = () -> {

            final String fullMsg = chanel + ";" + (message == null ? "" : message);

            mongoDBManager.getCollection(MongoCollections.SERVERS).find(Filters.regex("type", regex, "i")).forEach(document -> MessageClient.send(document.getString("ip"), document.getInteger("publish_port"), fullMsg, false));

        };

        if(!sync) Bukkit.getScheduler().runTaskAsynchronously(this, run);
        else run.run();
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
        PlayerStatueFix playerStatue = getPlayerStatue(target);
        if(playerStatue != null)
            return publishTarget(chanel, message, playerStatue.getServer(), rep, sync);
        return null;
    }

    @Override
    public String publishTargetPlayer(@NotNull String chanel, String message, PlayerStatueFix target, boolean rep, boolean sync)
    {
        return publishTarget(chanel, message, target.getServer(), rep, sync);
    }

    @Override
    public <T extends MinetasiaPacketOut> void sendPacketGlobal(@NotNull MinetasiaPacketOut packetOut, boolean proxy, boolean sync)
    {
        publishGlobal(MinetasiaPacketManager.channelPrefix + packetOut.name(), packetOut.write().toString(), proxy, sync);
    }

    @Override
    public <T extends MinetasiaPacketOut> void sendPacketProxy(@NotNull MinetasiaPacketOut packetOut, boolean sync)
    {
        publishProxy(MinetasiaPacketManager.channelPrefix + packetOut.name(), packetOut.write().toString(), sync);
    }

    @Override
    public <T extends MinetasiaPacketOut> void sendPacketType(@NotNull MinetasiaPacketOut packetOut, String serverType, boolean sync)
    {
        publishServerType(MinetasiaPacketManager.channelPrefix + packetOut.name(), packetOut.write().toString(), serverType, sync);
    }

    @Override
    public <T extends MinetasiaPacketOut> MinetasiaPacketIn sendPacketToServer(@NotNull MinetasiaPacketOut packetOut, Server server, boolean sync)
    {
        String back = publishTarget(MinetasiaPacketManager.channelPrefix + packetOut.name(), packetOut.write().toString(), server, packetOut.isNeedRep(), sync);
        return back == null ? null : minetasiaPacketInFromString(MinetasiaPacketManager.channelPrefix + packetOut.name(), back);
    }

    @Override
    public <T extends MinetasiaPacketOut> MinetasiaPacketIn publishTargetPlayer(@NotNull MinetasiaPacketOut packetOut, PlayerStatueFix player, boolean sync)
    {
        String back = publishTargetPlayer(MinetasiaPacketManager.channelPrefix + packetOut.name(), packetOut.write().toString(), player, packetOut.isNeedRep(), sync);
        return back == null ? null : minetasiaPacketInFromString(MinetasiaPacketManager.channelPrefix + packetOut.name(), back);
    }

    private MinetasiaPacketIn minetasiaPacketInFromString(String channel, String s)
    {

        MessageInChanel<?, ?> messageInChanel = MinetasiaPacketManager.getMessageInChanel(channel);
        if(messageInChanel != null)
        {
            return messageInChanel.getPacketSerializer().read(JSON_PARSER.parse(s).getAsJsonObject());
        }
        return null;
    }

    public String publishTarget(@NotNull String chanel, String message, @NotNull String ip, int port, boolean rep, boolean sync)
    {
        if(rep && !sync) throw new IllegalArgumentException("cant get rep in async");
        final String fullMsg = chanel + ";" + (message == null ? "" : message);

        if(sync)
        {
            return MessageClient.send(ip, port, fullMsg, rep);
        }
        else
        {
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> MessageClient.send(ip, port, fullMsg, false));
            return null;
        }
    }

    @Override
    public void movePlayerToHub(@NotNull org.bukkit.entity.Player player)
    {
       movePlayerToType(player, "hub");
    }

    @Override
    public void movePlayerToSkyblockHub(@NotNull org.bukkit.entity.Player player)
    {
        movePlayerToType(player, "skyblock-hub");
    }

    public void movePlayerToType(@NotNull org.bukkit.entity.Player player, String type)
    {
        Tuple<InventorySyncType, String> map = InventorySyncTools.map(player, getThisServer().getType(), type);

        String msg = player.getUniqueId() + ";" + type + ";" + map.a().name() + ";" + map.b();

        MinePlayer minePlayer = playerManager.getOnlyInCache(player.getUniqueId());;
        if(minePlayer == null) return;
        publishTarget("connect-type", msg, minePlayer.getProxyIp(), minePlayer.getProxyPublishPort(), false, false);

//        ByteArrayDataOutput output = ByteStreams.newDataOutput();
//        output.writeUTF("ConnectType");
//        output.writeUTF(type);
//        output.writeUTF(map.a().name());
//        output.writeUTF(map.b());
//
//        player.sendPluginMessage(this, "BungeeCord", output.toByteArray());
    }

    @Override
    public void movePlayerToSkyblockIsland(@NotNull org.bukkit.entity.Player player)
    {
        Tuple<InventorySyncType, String> map = InventorySyncTools.map(player, getThisServer().getType(), "skyblock-island");
        Document doc = getPlayer(player.getUniqueId()).getData("skyblock", Document.class);

        String msg = player.getUniqueId() + ";" + (doc == null || !doc.containsKey("island") ? "" : doc.getString("island")) + ";" +  (doc == null ? 1 : doc.getInteger("island_weight", 1)) + ";" + map.a().name() + ";" + map.b();

        MinePlayer minePlayer = playerManager.getOnlyInCache(player.getUniqueId());;
        if(minePlayer == null) return;
        publishTarget("connect-skyblock-island", msg, minePlayer.getProxyIp(), minePlayer.getProxyPublishPort(), false, false);

//        ByteArrayDataOutput output = ByteStreams.newDataOutput();
//        output.writeUTF("ConnectSkyblockIsland");
//
//        output.writeUTF(doc == null ? "" : doc.containsKey("island") ? doc.getString("island") : "");
//        output.writeInt();
//        output.writeUTF(map.a().name());
//        output.writeUTF(map.b());
//
//        player.sendPluginMessage(this, "BungeeCord", output.toByteArray());
    }

    @Override
    public void movePlayerToServer(@NotNull org.bukkit.entity.Player player, Server server) {

        Tuple<InventorySyncType, String> map = InventorySyncTools.map(player, getThisServer().getType(), server.getType());

        String msg = player.getUniqueId() + ";" + server.getName() + ";" + map.a().name() + ";" + map.b();

        MinePlayer minePlayer = playerManager.getOnlyInCache(player.getUniqueId());;
        if(minePlayer == null) return;
        publishTarget("connect-player", msg, minePlayer.getProxyIp(), minePlayer.getProxyPublishPort(), false, false);

//        ByteArrayDataOutput output = ByteStreams.newDataOutput();
//        output.writeUTF("Connect");
//        output.writeUTF(server.getName());
//        output.writeUTF(map.a().name());
//        output.writeUTF(map.b());
//
//        player.sendPluginMessage(this, "BungeeCord", output.toByteArray());
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
//        Filters.regex("username", "/^" + username + "$/i")

        final Document d =  mongoDBManager.getCollection(MongoCollections.ONLINE_USERS).aggregate(Arrays.asList(
                Aggregates.match(Filters.regex("username",  "^" + name + "$", "i")),
                Aggregates.lookup("servers", "server_id", "_id", "server"))).first();

        if(d == null) return null;

        return new PlayerStatueFixC(
                UUID.fromString(d.getString("_id")),
                name,
                d.getString("proxy_id"),
                MineServer.getServerFromDocument(d.getList("server", Document.class).get(0))
        );
    }

    private final static Document EMPTY_DOC = new Document();

    @NotNull
    public Document getPlayerKitDocument(UUID uuid)
    {
        final Document doc = getPlayerData(uuid, "kits", Document.class);
        return doc == null ? EMPTY_DOC : doc;
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
    public void setPlayerKitLvl(UUID uuid, String kitName, int lvl)
    {
        setPlayerData(uuid, "kits", getPlayerKitDocument(uuid).append(kitName, lvl));
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
    public List<MainKit> getMainKits(String prefix)
    {
        return kitsManager.getKits().values().stream().filter(k -> k.getName().startsWith(prefix)).collect(Collectors.toList());
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
    public MainKit createMonoLvlCoinsKit(String isoLang, String name, String displayName, Economy economy, int price, Material displayMat, String[] lvlDesc, String... desc)
    {
        return new KitMain(isoLang, name, displayName, economy, price, displayMat, lvlDesc, desc);
    }

    @Override
    public MainKit createMonoLvlPermsKit(String isoLang, String name, String displayName, String perm, Material displayMat, String[] lvlDesc, String... desc)
    {
        return new KitMain(isoLang, name, displayName, perm, displayMat, lvlDesc, desc);
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
        if(statsUpdater.getUpdate().isEmpty()) return;
        CorePlayer corePlayer = getPlayerManager().getCorePlayer(uuid);
        if(corePlayer.getPlayTime() > 30_000)
        {
            statsUpdater.getUpdate().put(getServerType() + ".play_time", TimeUnit.MICROSECONDS.toSeconds(corePlayer.getPlayTime()));
            corePlayer.resetJoinTime();
        }
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
        if(moneyUpdater.getUpdateMoney().isEmpty()) return;
        if(Bukkit.getPlayer(uuid) == null)
        {
            addGameWonMoneyToOfflinePlayer(uuid, moneyUpdater, boost, async);
            return;
        }
        if(isCommandEnable(Command.PARTY_XP_BOOST))
        {
            Consumer<BukkitTask> bukkitTaskConsumer = bukkitTask -> {
                Boost playerBoost = getPlayerPersonalBoost(uuid);
                StringBuilder money = new StringBuilder();

                Map<Economy, Float> newMap = new HashMap<>();
                moneyUpdater.getUpdateMoney().forEach((k,v) ->{
                    if(k == Economy.SHOPEX) return;
                    final float b =  1 + playerBoost.getBoost().getOrDefault(k.boostType, 0f) / 100f + partyServerBoost.getBoost(k.boostType) / 100f;
                    newMap.put(k, v * b);
                    if(v * b > 0)
                    {
                        if(money.length() > 0) money.append(",");
                        money.append(moneyColor).append(v * b).append(" ").append(k.displayName);
                    }
                });



                String[] toSend = Lang.GAME_REWARDS.getWithSplit(getPlayerLang(uuid), Lang.Argument.SERVER_TYPE.match(serverType), Lang.Argument.REWARDS.match(money.toString()));
                org.bukkit.entity.Player p = Bukkit.getPlayer(uuid);
                if(p != null)
                    for(String s : toSend) p.sendMessage(s);


                addPlayerMoneys(uuid, newMap, false);
            };

            if(async)
                Bukkit.getScheduler().runTaskAsynchronously(this, bukkitTaskConsumer);
            else
                bukkitTaskConsumer.accept(null);
        }
        else
            Bukkit.getLogger().warning("plugin :" + getName() + " want give party won money but server have PARTY_XP_BOOST = false");
    }

    @Override
    public boolean isMuted(UUID player)
    {
        final PlayerSanction sanction = getPlayerManager().get(player).getSanction(SanctionType.MUTE);
        return sanction != null && !sanction.isEnd();
    }

    private void addGameWonMoneyToOfflinePlayer(@NotNull UUID uuid, @NotNull MoneyUpdater moneyUpdater, boolean boost, boolean async)
    {
        if(moneyUpdater.getUpdateMoney().isEmpty()) return;
        if(isCommandEnable(Command.PARTY_XP_BOOST))
        {
            Consumer<BukkitTask> bukkitTaskConsumer = bukkitTask -> {
                final MinePlayer mp = playerManager.get(uuid);
                if(mp == null) return;

                Boost playerBoost = mp.getPersonalBoost();

                Map<Economy, Float> newMap = new HashMap<>();
                moneyUpdater.getUpdateMoney().forEach((k,v) ->{
                    if(k == Economy.SHOPEX) return;
                    final float b =  1 + playerBoost.getBoost().getOrDefault(k.boostType, 0f) / 100f + partyServerBoost.getBoost(k.boostType) / 100f;
                    newMap.put(k, v * b);
                });
                addPlayerMoneys(uuid, newMap, false);
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

        final Group g = getPlayerMasterGroup(player);
        if(g == null) return "";
        else return ChatColor.translateAlternateColorCodes('&', g.getDisplayName());
    }

    @Nullable
    public Group getPlayerMasterGroup(UUID player)
    {
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

        return g;
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
        }
        else
        {
            //add place for admin
            if(phase == ServerPhase.GAME)
            {
                setMaxPlayerCount( getThisServer().getMaxPlayerCount() + maxPlayerCountAddAdmin, false);
            }
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                mongoDBManager.getCollection(MongoCollections.SERVERS).updateOne(Filters.eq(getThisServer().getName()), Updates.set("phase", phase.ordinal()));
                serverManager.sayServerUpdate(CoreMessage.CHANNEL, ServerMessage.getMessage(ServerMessage.SERVER_STATUE, getThisServer().getName(), phase.name()), true);
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
        return getThisServer().getMaxPlayerCount();
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
    @Deprecated
    public void registerGui(MinetasiaGUI gui)
    {
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

    @Override
    public void openPartyGui(@NotNull Player player)
    {
        Validate.notNull(player);
        gui.getPartyGui().open(player);
    }

    @Override
    public void openFriendGui(@NotNull Player player)
    {
        Validate.notNull(player);
        gui.friendsGui.open(player, 0);
    }

    @Override
    public void validateAdvancement(@NotNull UUID player, @NotNull NamespacedKey namespacedKey)
    {
        advancementManager.validate(namespacedKey, Objects.requireNonNull(getPlayerManager().get(player)));
    }

    @Override
    public MinetasiaBaseAdvancement createAdvancement(@NotNull NamespacedKey namespacedKey, @NotNull AdvancementIcon icon, @NotNull AdvancementFrame frame, @NotNull String title, @NotNull String description, @NotNull String lang)
    {
        return new MinetasiaAdvancement(namespacedKey, icon, frame, title, description, lang);
    }

    @Override
    public void registerAdvancement(@NotNull MinetasiaBaseAdvancement advancement)
    {
        if(advancement instanceof MinetasiaAdvancement)
            advancementManager.registerAndSave((MinetasiaAdvancement) advancement);
        else throw new IllegalArgumentException("advancement is'nt instanceof of fr.idarkay.minetasia.core.spigot.advancement.MinetasiaAdvancement");
    }

    @NotNull
    @Override
    public <T> MinetasiaSettings<T> getSettings(@NotNull SettingsKey<T> key)
    {
        return settingsManager.getSettings(key);
    }

    @Override
    public String getIp()
    {
        return ip;
    }

    @Override
    public void registerIpConsumer(Consumer<String> ipConsumer)
    {
        this.ipRunnable.addConsumer(ipConsumer);
    }

    @Override
    public void setInventorySyncGetter(Function<Player, InventorySyncPlayer> func)
    {
        InventorySyncTools.function = func;
    }

    @Override
    public Map<String, GuiLang> getLang()
    {
        return LangGui.LANG_TEXTURE;
    }

    public void setMaxPlayerCount(int maxPlayer, boolean startup)
    {
        if(startup && getThisServer().getServerPhase() != ServerPhase.LOAD) throw new IllegalArgumentException("can set maxPlayerCount only in Load Phase !");
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

    private void setCommandsIsEnable(byte b, boolean value)
    {
        if(value)
            commands |= 1 << b;
        else
            commands &= ~(1 << b);
    }

    public long setBoolIsValue(long bool, byte b, boolean value)
    {
        if(value)
            bool |= 1 << b;
        else
            bool &= ~(1 << b);
        return bool;
    }

    public boolean isBollTrue(long bool, byte b)
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

    public PartyManager getPartyManager()
    {
        return partyManager;
    }

    public PlayerListManager getPlayerListManager()
    {
        return playerListManager;
    }

    public AdvancementManager getAdvancementManager()
    {
        return advancementManager;
    }

    public SettingsManager getSettingsManager()
    {
        return settingsManager;
    }

    public SanctionManger getSanctionManger()
    {
        return sanctionManger;
    }

    public void applySanctionToPlayerServerBound(@NotNull MinePlayer minetasiaPlayer, @NotNull PlayerSanction sanction)
    {
        Validate.notNull(minetasiaPlayer);
        Validate.notNull(sanction);
        final Player bukkitPlayer = Bukkit.getPlayer(minetasiaPlayer.getUUID());
        minetasiaPlayer.setSanction(sanction);
        //for client bound
        if(bukkitPlayer != null)
        {
            applySanctionToPlayerClientBound(bukkitPlayer, sanction);
        }
        else
        {
            final PlayerStatueFix playerStatue = getPlayerStatue(minetasiaPlayer.getUUID());
            if(playerStatue != null)
            {
                publishTarget(CoreMessage.CHANNEL, SanctionMessage.getMessage(minetasiaPlayer.getUUID(), sanction), playerStatue.getServer(), false, false);
            }
        }
    }

    public void applySanctionToPlayerClientBound(@NotNull Player player, @NotNull PlayerSanction sanction)
    {
        if(sanction.sanctionType == SanctionType.BAN)
        {
            Bukkit.getScheduler().runTask(this, () -> player.kickPlayer(Lang.BAN_FORMAT.getWithoutPrefix(getPlayerLang(player.getUniqueId()), Lang.Argument.PLAYER.match(sanction.authorName)
                    , Lang.Argument.REASON.match(sanction.reason)
                    , Lang.Argument.TIME.match(sanction.baseTimeUnit.convert(sanction.during, TimeUnit.MILLISECONDS) + " " + sanction.baseTimeUnit.name().toLowerCase()))
                    .replace("@@", "\n")));
        }
        else
        {
            player.sendMessage(( sanction.sanctionType == SanctionType.MUTE ? Lang.MUTE_FORMAT : Lang.WARN_FORMAT).get(getPlayerLang(player.getUniqueId()), Lang.Argument.PLAYER.match(sanction.authorName)
                    , Lang.Argument.REASON.match(sanction.reason)
                    , Lang.Argument.TIME.match(sanction.baseTimeUnit.convert(sanction.during, TimeUnit.MILLISECONDS) + " " + sanction.baseTimeUnit.name().toLowerCase())));
        }
    }

    public ReportManager getReportManager()
    {
        return reportManager;
    }


    /**
     * Method used by power, effect and particle manager to get every classes registered in package
     * @param pckgname Name of the package (with '/' instead of '.')
     * @return List of Class<?> in this package
     * @throws ClassNotFoundException If we can't find the class in the package
     * @throws IOException If we can't find the package
     */
    public List<Class<?>> getClasses(String pckgname) throws ClassNotFoundException, IOException
    {
        final ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        final JarFile jar = new JarFile(this.getFile());
        Enumeration<JarEntry> jfile = jar.entries();
        while(jfile.hasMoreElements())
        {
            String name = jfile.nextElement().getName();
            if(name.startsWith(pckgname) && name.split("/").length > pckgname.split("/").length) classes.add(Class.forName(name.substring(0, name.length() - 6).replace('/', '.')));
        }
        jar.close();
        return classes;
    }

    public OldCombatsManger getOldCombatsManger()
    {
        return oldCombatsManger;
    }

    public void doAsync(Runnable runnable)
    {
        Bukkit.getScheduler().runTaskAsynchronously(this, runnable);
    }

    public void doSync(Runnable runnable)
    {
        Bukkit.getScheduler().runTask(this, runnable);
    }

}