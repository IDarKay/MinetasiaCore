package fr.idarkay.minetasia.core.spigot;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import fr.idarkay.minetasia.core.api.Economy;
import fr.idarkay.minetasia.core.api.MinetasiaCoreApi;
import fr.idarkay.minetasia.core.api.exception.FRSDownException;
import fr.idarkay.minetasia.core.api.exception.PlayerNotFoundException;
import fr.idarkay.minetasia.core.api.utils.Server;
import fr.idarkay.minetasia.core.spigot.executor.FriendsExecutor;
import fr.idarkay.minetasia.core.spigot.executor.LangExecutor;
import fr.idarkay.minetasia.core.spigot.gui.GUI;
import fr.idarkay.minetasia.core.spigot.listener.FRSMessageListener;
import fr.idarkay.minetasia.core.spigot.listener.InventoryClickListener;
import fr.idarkay.minetasia.core.spigot.listener.PlayerListener;
import fr.idarkay.minetasia.core.spigot.server.ServerManager;
import fr.idarkay.minetasia.core.spigot.user.Player;
import fr.idarkay.minetasia.core.spigot.user.PlayerManager;
import fr.idarkay.minetasia.core.spigot.utils.FRSClient;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import fr.idarkay.minetasia.core.spigot.utils.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
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

    private SQLManager sqlManager;
    private FRSClient frsClient;
    private PlayerManager playerManager;
    private ServerManager serverManager;
    private GUI gui;

    private FriendsExecutor friendsExecutor;

    private String serverType;

    private boolean friends, lang;

    private Cache<Integer, Map<UUID, String>> onlinePlayer;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        getMinetasiaLang().init();

        Lang.prefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("prefix")));
        serverType = getConfig().getString("server_type");

        sqlManager = new SQLManager(this);
        frsClient = new FRSClient(this);
        frsClient.startConnection(System.out, getConfig().getString("frs.host"), getConfig().getInt("frs.port"),
                getConfig().getString("frs.password"), getConfig().getInt("frs.timeout"));

        sqlManager.update("CREATE TABLE IF NOT EXISTS `uuid_username` ( `uuid` VARCHAR(36) NOT NULL , `username` VARCHAR(16) NOT NULL , PRIMARY KEY (`uuid`))");
    }

    @Override
    public void onEnable() {

        onlinePlayer = CacheBuilder.newBuilder().expireAfterWrite(getConfig().getLong("cache.online_player"), TimeUnit.MINUTES).maximumSize(1L).build();

        playerManager = new PlayerManager(this);
        serverManager = new ServerManager(this);
        gui = new GUI(this);

        friends = getConfig().getBoolean("commands.friends", false);
        if(friends)
        {
            friendsExecutor = new FriendsExecutor(this);
            Objects.requireNonNull(getCommand("friends")).setExecutor(friendsExecutor);
        }

        lang = getConfig().getBoolean("commands.lang", false);
        if(friends)
        {
            gui.createLangInventory();
            Objects.requireNonNull(getCommand("lang")).setExecutor(new LangExecutor(this));
        }

        getServer().getPluginManager().registerEvents(new FRSMessageListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);

    }

    @Override
    public void onDisable() {
        frsClient.shutdown(true);
        serverManager.disable();
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
    public void setPlayerData(@NotNull UUID uuid, @NotNull String key, @NotNull String value) {
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
    public void addPlayerMoney(UUID uuid, Economy economy, float amount) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            if(amount < 0) throw new IllegalArgumentException("negative money amount");
            Player p = playerManager.get(uuid);
            if(p != null){
                p.addMoney(economy, amount);
                publish("core-data", "money;" + uuid.toString() + ";" + economy.name() + ";" + p.getMoney(economy));
                getFrsClient().setValue("usersData", uuid.toString(), p.getJson());
            } else throw new PlayerNotFoundException("can't add money to not found user");
        });
    }

    @Override
    public void removePlayerMoney(UUID uuid, Economy economy, float amount) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            if(amount < 0) throw new IllegalArgumentException("negative money amount");
            Player p = playerManager.get(uuid);
            if(p != null){
                p.removeMooney(economy, amount);
                publish("core-data", "money;" + uuid.toString() + ";" + economy.name() + ";" + p.getMoney(economy));
                getFrsClient().setValue("usersData", uuid.toString(), p.getJson());
            } else throw new PlayerNotFoundException("can't remove money to not found user");
        });
    }

    @Override
    public void setPlayerMoney(UUID uuid, Economy economy, float amount) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            if(amount < 0) throw new IllegalArgumentException("negative money amount");
            Player p = playerManager.get(uuid);
            if(p != null){
                p.setMoney(economy, amount);
                publish("core-data", "money;" + uuid.toString() + ";" + economy.name() + ";" + amount);
                getFrsClient().setValue("usersData", uuid.toString(), p.getJson());
            } else throw new PlayerNotFoundException("can't set money to not found user");
        });
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
    public void publish(@NotNull String chanel, @NotNull String message) {
        if(frsClient.isConnected())
            frsClient.publish(chanel, message, false);
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
        player.kickPlayer("Disconnect hub function not set");
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
    public void shutdown() {
        getServer().getOnlinePlayers().forEach(this::movePlayerToHub);
        getServer().shutdown();
    }

    @Override
    @NotNull
    public Map<UUID, String> getOnlinePlayers() {
        try {
            onlinePlayer.get(1, () -> {
                sqlManager.getSQL();
                try(PreparedStatement ps = sqlManager.getSQL().prepareStatement("SELECT uuid, username FROM `online_player`"); ResultSet rs = ps.executeQuery())
                {
                    Map<UUID, String> online = new HashMap<>();
                    while (rs.next()) online.put(UUID.fromString(rs.getString("uuid")), rs.getString("username"));
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

    public boolean isFriendsOn() {
        return friends;
    }

    public boolean isLangOn() {
        return lang;
    }
}
