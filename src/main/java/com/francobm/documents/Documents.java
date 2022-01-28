package com.francobm.documents;

import com.francobm.documents.cache.InventoryData;
import com.francobm.documents.cache.PlayerData;
import com.francobm.documents.commands.Command;
import com.francobm.documents.database.SQL;
import com.francobm.documents.database.SQLite;
import com.francobm.documents.files.FileCreator;
import com.francobm.documents.listeners.PlayerListener;
import com.francobm.documents.managers.DocumentManager;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Documents extends JavaPlugin {
    public String pluginName;
    private FileCreator messages;
    private FileCreator menus;
    private FileCreator config;
    private List<PlayerData> players;
    private List<InventoryData> inventories;
    private DocumentManager documentManager;
    private SQL sql;

    @Override
    public void onEnable() {
        super.onEnable();
        messages = new FileCreator(this, "messages");
        menus = new FileCreator(this, "menus");
        config = new FileCreator(this, "config");
        sql = new SQLite(this);
        players = new ArrayList<>();
        inventories = new ArrayList<>();
        pluginName = messages.getString("prefix");
        documentManager = new DocumentManager(this);
        loadInventory();
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        sql.disconnect();
    }

    public void registerCommands(){
        getCommand("documents").setExecutor(new Command(this));
    }

    public void registerListeners(){
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    public void addInventory(InventoryData inventoryData){
        this.inventories.add(inventoryData);
    }

    public InventoryData getInventory(String name){
        for(InventoryData inventoryData : inventories){
            if(inventoryData.getName().equalsIgnoreCase(name)){
                return inventoryData;
            }
        }
        return null;
    }

    public void addPlayer(PlayerData playerData){
        this.players.add(playerData);
    }

    public void removePlayer(PlayerData playerData){
        this.players.remove(playerData);
    }

    public PlayerData getPlayer(String name){
        for(PlayerData playerData : players){
            if(playerData.getPlayer().getName().equalsIgnoreCase(name)){
                return playerData;
            }
        }
        return null;
    }

    public void loadInventory(){
        this.inventories.clear();
        if(!menus.contains("menus.dni")) return;
        for(String key : menus.getConfigurationSection("menus.dni").getKeys(false)){
            String name = "";
            String material = "";
            int slot = menus.getInt("menus.dni." + key + ".slot");
            List<String> lore = new ArrayList<>();
            Material item = null;
            if(menus.contains("menus.dni." + key + ".name")){
                name = menus.getString("menus.dni." + key + ".name");
            }
            if(menus.contains("menus.dni." + key + ".item")){
                material = menus.getString("menus.dni." + key + ".item");
            }
            if(menus.contains("menus.dni." + key + ".slot")){
                slot = menus.getInt("menus.dni." + key + ".slot");
            }
            if(menus.contains("menus.dni." + key + ".lore")) {
                lore = menus.getStringList("menus.dni." + key + ".lore");
            }
            try{
                if(material == null) return;
                item = Material.getMaterial(material);
            }catch (Exception exception){
                //exception.printStackTrace();
                getLogger().info("Material: " + material + " Not Found.");
            }
            if(!name.isEmpty()){
                addInventory(new InventoryData(name, item, slot, lore));
            }
        }
    }

    public List<InventoryData> getInventories() {
        return inventories;
    }

    public FileCreator getMenus() {
        return menus;
    }

    public FileCreator getMessages() {
        return messages;
    }

    @Override
    public FileCreator getConfig() {
        return config;
    }

    public SQL getSql() {
        return sql;
    }

    public List<PlayerData> getPlayers() {
        return players;
    }

    public DocumentManager getDocumentManager() {
        return documentManager;
    }
}
