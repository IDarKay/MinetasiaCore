package fr.idarkay.minetasia.core.spigot.utils;


import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * File <b>SQLManager</b> located on fr.idarkay.minetasia.core.common.utils
 * SQLManager is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 13/11/2019 at 18:02
 */
@Deprecated
public class SQLManager implements fr.idarkay.minetasia.core.api.utils.SQLManager {

    private final Plugin plugin;
    private final String host, dbname, user, pass, connexionArgs; // connection information
    private Connection connection; // connection of the database
    @Deprecated
    public SQLManager(Plugin plugin)
    {
        this.plugin = plugin;
        host = plugin.getConfig().getString("db.host");
        dbname = plugin.getConfig().getString("db.dbname");
        user = plugin.getConfig().getString("db.login");
        pass = plugin.getConfig().getString("db.password");
        connexionArgs = plugin.getConfig().getString("db.connexion_args");
    }
    @Deprecated
    @Override
    public Connection getSQL()
    {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.jdbc.Driver");
                this.connection = DriverManager.getConnection(
                        "jdbc:mysql://" + host + "/" + dbname + connexionArgs, user, pass);
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning("! IMPORTANT ! Impossible de se connecter  la base de donne !");
        }
        return connection;
    }
    @Deprecated
    @Override
    public void update(String query, Object... args)
    {
        if (!connectionIsOk())
        {
            getSQL();
            if(!connectionIsOk())
            {
                Bukkit.getLogger().warning("the sql request " + query + " wasn't send connection broke");
                return;
            }
        }
        try(PreparedStatement stmt = connection.prepareStatement("use " + dbname + ";" + query)) {
            for (int i = 0; i < args.length; i++)
                stmt.setObject(i + 1, args[i]);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Deprecated
    @Override
    public void updateAsynchronously(String query, Object... args)
    {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> update(query, args));
    }
    @Deprecated
    @Override
    public boolean connectionIsOk() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
