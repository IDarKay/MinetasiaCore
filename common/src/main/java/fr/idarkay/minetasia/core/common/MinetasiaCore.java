package fr.idarkay.minetasia.core.common;

import fr.idarkay.minetasia.core.api.Economy;
import fr.idarkay.minetasia.core.api.MinetasiaCoreApi;
import fr.idarkay.minetasia.core.common.listener.FRSMessageListener;
import fr.idarkay.minetasia.core.common.listener.PlayerListener;
import fr.idarkay.minetasia.core.common.user.Player;
import fr.idarkay.minetasia.core.common.user.PlayerManagement;
import fr.idarkay.minetasia.core.common.utils.FRSClient;
import fr.idarkay.minetasia.core.common.utils.SQLManager;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

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
public class MinetasiaCore extends MinetasiaCoreApi {

    private SQLManager sqlManager;
    private FRSClient frsClient;
    private PlayerManagement playerManagement;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getMinetasiaLang().init();
        sqlManager = new SQLManager(this);
        frsClient = new FRSClient(this);
        frsClient.startConnection(System.out, getConfig().getString("frs.host"), getConfig().getInt("frs.port"),
                getConfig().getString("frs.password"), getConfig().getInt("frs.timeout"));
        playerManagement = new PlayerManagement(this);

        sqlManager.update("CREATE TABLE IF NOT EXISTS `uuid_username` ( `uuid` VARCHAR(36) NOT NULL , `username` VARCHAR(16) NOT NULL , PRIMARY KEY (`uuid`))");

        getServer().getPluginManager().registerEvents(new FRSMessageListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

    }

    @Override
    public void onDisable() {
        getFrsClient().shutdown(true);
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
            if((p = playerManagement.get(uuid)) != null)
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
        if((p = playerManagement.get(uuid)) != null) return p.getData(key);
        else return null;
    }

    @Override
    public @Nullable UUID getPlayerUUID(@NotNull String username) {
        org.bukkit.entity.Player p;
        if((p = Bukkit.getPlayer(username)) != null) return p.getUniqueId();
        else
        {
            sqlManager.getSQL();
            try(PreparedStatement ps = sqlManager.getSQL().prepareStatement("SELECT uuid FROM `uuid_username` WHERE  username = " + username);  ResultSet rs = ps.executeQuery())
            {
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
        Player p = playerManagement.get(uuid);
        if(p != null) return p.getMoney(economy);
        else return -1.0F;
    }

    @Override
    public boolean addPlayerMoney(UUID uuid, Economy economy, float amount) {
        if(amount < 0) throw new IllegalArgumentException("negative money amount");
        Player p = playerManagement.get(uuid);
        if(p != null){
            p.addMoney(economy, amount);
            return true;
        } else return false;
    }

    @Override
    public boolean removePlayerMoney(UUID uuid, Economy economy, float amount) {
        if(amount < 0) throw new IllegalArgumentException("negative money amount ");
        Player p = playerManagement.get(uuid);
        if(p != null) return p.removeMooney(economy, amount);
        else return false;
    }

    @Override
    public boolean setPlayerMoney(UUID uuid, Economy economy, float amount) {
        if(amount < 0) throw new IllegalArgumentException("negative money amount");
        Player p = playerManagement.get(uuid);
        if(p != null){
            p.setMoney(economy, amount);
            return true;
        } else return false;
    }

    @Override
    public @NotNull HashMap<UUID, String> getFriends(@NotNull UUID uuid) {
        Player p = playerManagement.get(uuid);
        if (p != null) return p.getFriends();
        return new HashMap<>();
    }

    @Override
    public boolean isFriend(@NotNull UUID uuid, @NotNull UUID uuid2) {
        Player p = playerManagement.get(uuid);
        if(p != null) return p.isFriend(uuid2);
        return false;
    }

    @Override
    public boolean removeFriend(@NotNull UUID uuid, @NotNull UUID uuid2) {
        Player p = playerManagement.get(uuid);
        if(p != null) return p.removeFriends(uuid2);
        return false;
    }

    @Override
    public boolean addFriend(@NotNull UUID uuid, @NotNull UUID uuid2) {
        Player p = playerManagement.get(uuid);
        if(p != null) return p.addFriends(uuid2);
        return false;
    }

    @Override
    public boolean isPlayerOnline(@NotNull UUID uuid) {
        return true;
    }


    @Override
    public void publish(@NotNull String chanel, @NotNull String message) {
        frsClient.publish(chanel, message, false);
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
        Player p = playerManagement.get(uuid);
        if(p != null) return p.getName();
        return null;
    }

    @Override
    public void shutdown() {
        getServer().getOnlinePlayers().forEach(this::movePlayerToHub);
        getServer().shutdown();
    }

    public void setUserName(UUID uuid, String username)
    {
        Bukkit.getScheduler().runTaskAsynchronously(this, () ->
        {
            Player p;
            if((p = playerManagement.get(uuid)) != null)
            {
                p.setUsername(username);
                publish("core-data", "name;" + uuid.toString() + ";" + username);
                getFrsClient().setValue("usersData", uuid.toString(), p.getJson());
            }
        });
    }

    public PlayerManagement getPlayerManagement() {
        return playerManagement;
    }

    public FRSClient getFrsClient() {
        return frsClient;
    }

}
