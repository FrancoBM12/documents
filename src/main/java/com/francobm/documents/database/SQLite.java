package com.francobm.documents.database;

import com.francobm.documents.Documents;
import com.francobm.documents.cache.PlayerData;
import com.francobm.documents.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLite extends SQL{

    public SQLite(Documents plugin) {
        super(plugin);
    }

    @Override
    public void connect() {
        try {
            plugin.getLogger().info("Conectando la base de datos con SQLite...");
            File FileSQL = new File(plugin.getDataFolder(), "documents.db");
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + FileSQL);
            plugin.getLogger().info("Conexion SQLite conectada");
        } catch (Exception e) {
            e.printStackTrace();
            this.plugin.getPluginLoader().disablePlugin(this.plugin);
        }
    }

    @Override
    public void disconnect() {
        if(isConnected()){
            try {
                connection.close();
                plugin.getLogger().info("Conexion SQLite Cerrada");
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
    }

    @Override
    public void createTable() {
        if(isConnected()) {
            try {
                connection.prepareStatement("CREATE TABLE IF NOT EXISTS documentsTable (id INTEGER PRIMARY KEY AUTOINCREMENT, UUID VARCHAR(100), PlayerName VARCHAR(100), Name VARCHAR(100), LastName VARCHAR(100), DateBirth VARCHAR(100), Nationality VARCHAR(100), Gender VARCHAR(100), Job VARCHAR(100), History VARCHAR(300))").executeUpdate();
                plugin.getLogger().info("Tabla SQLite creada con exito!");
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
    }


}
