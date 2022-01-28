package com.francobm.documents.database;

import com.francobm.documents.Documents;
import com.francobm.documents.cache.PlayerData;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SQL {
    protected Documents plugin;
    protected Connection connection;

    public SQL(Documents plugin){
        this.plugin = plugin;
        connect();
        createTable();
    }

    public abstract void connect();

    public abstract void disconnect();

    public boolean isConnected(){
        return connection != null;
    }

    public abstract void createTable();

    public PlayerData getPlayer(Player player){
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM documentsTable WHERE PlayerName = ?");
            statement.setString(1, player.getName());
            ResultSet rs =statement.executeQuery();
            PlayerData playerData = null;
            while (rs.next()){
                String history = rs.getString("History");
                List<String> histories = new ArrayList<>(Arrays.asList(history.split("@@")));
                playerData = new PlayerData(player, rs.getInt("id"), rs.getString("Name"), rs.getString("LastName"), rs.getString("DateBirth"), rs.getString("Nationality"), rs.getString("Gender"), rs.getString("Job"), histories);
            }
            return playerData;
        }catch (SQLException throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    public void loadPlayer(Player player){
        PlayerData playerData = getPlayer(player);
        if(playerData == null){
            plugin.addPlayer(new PlayerData(player));
            player.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.incomplete"));
            if(plugin.getConfig().getBoolean("on-join-verify-dni")) {
                plugin.getDocumentManager().createDocument(player);
            }
            return;
        }
        if(!playerData.getComplete()) {
            if (plugin.getConfig().getBoolean("on-join-verify-dni")) {
                plugin.getDocumentManager().createDocument(player);
            }
        }
        plugin.addPlayer(playerData);
    }

    public boolean removePlayer(Player player){
        PlayerData playerData = plugin.getPlayer(player.getName());
        if(playerData == null) return false;
        if(getPlayer(player) != null) {
            try {
                PreparedStatement statement = connection.prepareStatement("DELETE FROM documentsTable WHERE PlayerName = ?");
                statement.setString(1, player.getName());
                statement.executeUpdate();
                plugin.removePlayer(playerData);
                return true;
            } catch (SQLException throwable) {
                throwable.printStackTrace();
                return false;
            }
        }
        plugin.removePlayer(playerData);
        return true;
    }

    public void savePlayer(PlayerData playerData){
        try{
            String history = String.join("@@", playerData.getHistory());
            if(getPlayer(playerData.getPlayer()) == null) {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO documentsTable (UUID, PlayerName, Name, LastName, DateBirth, Nationality, Gender, Job, History) VALUES (?,?,?,?,?,?,?,?,?)");
                statement.setString(1, playerData.getPlayer().getUniqueId().toString());
                statement.setString(2, playerData.getPlayer().getName());
                statement.setString(3, playerData.getName());
                statement.setString(4, playerData.getLastName());
                statement.setString(5, playerData.getDateBirth());
                statement.setString(6, playerData.getNationality());
                statement.setString(7, playerData.getGender());
                statement.setString(8, playerData.getJob());
                statement.setString(9, history);
                statement.executeUpdate();
                plugin.removePlayer(playerData);
                return;
            }
            PreparedStatement statement = connection.prepareStatement("UPDATE documentsTable SET UUID = ?, Name = ?, LastName = ?, DateBirth = ?, Nationality = ?, Gender = ?, Job = ?, History = ? WHERE PlayerName = ?");
            statement.setString(1, playerData.getPlayer().getUniqueId().toString());
            statement.setString(2, playerData.getName());
            statement.setString(3, playerData.getLastName());
            statement.setString(4, playerData.getDateBirth());
            statement.setString(5, playerData.getNationality());
            statement.setString(6, playerData.getGender());
            statement.setString(7, playerData.getJob());
            statement.setString(8, history);
            statement.setString(9, playerData.getPlayer().getName());
            statement.executeUpdate();
        }catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        plugin.removePlayer(playerData);
    }


}
