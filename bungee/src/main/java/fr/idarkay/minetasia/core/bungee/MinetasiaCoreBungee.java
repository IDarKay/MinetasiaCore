package fr.idarkay.minetasia.core.bungee;

import fr.idarkay.minetasia.core.bungee.listener.PlayerListener;
import fr.idarkay.minetasia.core.bungee.utils.FRSClient;
import fr.idarkay.minetasia.core.bungee.utils.SQLManager;
import fr.idarkay.minetasia.core.bungee.utils.proxy.ProxyManager;
import fr.idarkay.minetasia.core.bungee.utils.user.Player;
import fr.idarkay.minetasia.core.bungee.utils.user.PlayerManager;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.UUID;

/**
 * File <b>MinetasiaCoreBungee</b> located on fr.idarkay.minetasia.core.bungee
 * MinetasiaCoreBungee is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 26/11/2019 at 12:43
 */
public final class MinetasiaCoreBungee extends Plugin {

    private FRSClient frsClient;
    private SQLManager sqlManager;
    private Configuration configuration;
    private ProxyManager proxyManager;
    private PlayerManager playerManager;

    @Override
    public void onLoad() {
        File configFile = new File("config.yml");

        if(!configFile.getParentFile().exists()) configFile.getParentFile().mkdirs();
        if(!configFile.exists())
        {
            try (InputStream in = this.getResourceAsStream("config.yml"))
            {
                Files.copy(in, configFile.toPath());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try
        {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        frsClient  = new FRSClient(this);

        frsClient.startConnection(System.out, configuration.getString("frs.host"), configuration.getInt("frs.port"),
                configuration.getString("frs.password"));

        sqlManager = new SQLManager(this);
        sqlManager.update("CREATE TABLE IF NOT EXISTS `online_player` ( `uuid` VARCHAR(32) NOT NULL , `username` VARCHAR(16) NOT NULL , `proxy` VARCHAR(32) NOT NULL , PRIMARY KEY (`uuid`)) ENGINE = MEMORY;");
    }

    @Override
    public void onEnable() {
        proxyManager = new ProxyManager(this);
        proxyManager.init();
        playerManager = new PlayerManager(this);

        getProxy().getPluginManager().registerListener(this, new PlayerListener(this));

    }

    @Override
    public void onDisable() {
        proxyManager.disable();
    }

    public Configuration getConfig() {
        return configuration;
    }

    public FRSClient getFrsClient() {
        return frsClient;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public SQLManager getSqlManager() {
        return sqlManager;
    }

    public ProxyManager getProxyManager() {
        return proxyManager;
    }

    public void setUserName(UUID uuid, String username)
    {
        this.getProxy().getScheduler().runAsync(this, () ->
        {
            Player p;
            if((p = playerManager.get(uuid)) != null)
            {
                p.setUsername(username);
                frsClient.publish("core-data", "name;" + uuid.toString() + ";" + username);
                frsClient.setValue("usersData", uuid.toString(), p.getJson());
            }
        });
    }

}
