package com.francobm.documents.managers;

import com.francobm.documents.Documents;
import com.francobm.documents.cache.InventoryData;
import com.francobm.documents.cache.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager implements InventoryHolder {
    private final Inventory inventory;
    private final Documents plugin;

    public InventoryManager(Documents plugin, Player player){
        this.plugin = plugin;
        int rows = plugin.getMenus().getInt("menus.dni.rows");
        String title = plugin.getMenus().getString("menus.dni.title");
        inventory = Bukkit.createInventory(null, rows*9, title);
        init(player);
    }

    public InventoryManager(Documents plugin, Player player, Player target){
        this.plugin = plugin;
        int rows = plugin.getMenus().getInt("menus.dni.rows");
        String title = plugin.getMenus().getString("menus.dni.title");
        inventory = Bukkit.createInventory(null, rows*9, title);
        init(player, target);
    }

    private void init(Player player){
        openDNIMenu(player);
    }

    private void init(Player player, Player target){
        openDNIMenu(player, target);
    }

    private void openDNIMenu(Player player, Player target){
        PlayerData playerData = plugin.getPlayer(player.getName());
        if(playerData == null){
            player.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.gui.no_other_dni_show"));
            return;
        }
        if(!playerData.getComplete()){
            player.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.incomplete"));
            target.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.gui.player_incomplete").replace("%player%", player.getName()));
            return;
        }
        for(InventoryData inventoryData : plugin.getInventories()){
            ItemStack itemStack = new ItemStack(inventoryData.getItem());
            SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
            itemMeta.setOwningPlayer(player);
            itemMeta.setDisplayName(inventoryData.getName());
            List<String> lore = new ArrayList<>();
            for(String l : inventoryData.getLore()){
                if(l.contains("%history%")){
                    int i = 0;
                    for(String history : playerData.getHistory()){
                        if(i == 0){
                            lore.add(l.replace("%history%", history));
                            i++;
                            continue;
                        }
                        lore.add(history);
                    }
                    continue;
                }
                lore.add(l.replace("%full_name%", playerData.getFullName()).replace("%date_birth%", playerData.getDateBirth()).replace("%nationality%", playerData.getNationality()).replace("%gender%", playerData.getGender()).replace("%job%", playerData.getJob()));
            }
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(inventoryData.getSlot(), itemStack);
        }
        player.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.gui.open_other_inventory").replace("%target%", target.getName()));
        target.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.gui.notify_target_open_inventory").replace("%player%", player.getName()));
        target.openInventory(inventory);
    }

    private void openDNIMenu(Player player){
        PlayerData playerData = plugin.getPlayer(player.getName());
        if(playerData == null){
            player.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.gui.no_dni_show"));
            return;
        }
        if(!playerData.getComplete()){
            player.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.incomplete"));
            return;
        }
        for(InventoryData inventoryData : plugin.getInventories()){
            if(inventoryData.getItem() == Material.PLAYER_HEAD){
                ItemStack itemStack = new ItemStack(inventoryData.getItem());
                SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
                itemMeta.setOwningPlayer(player);
                itemMeta.setDisplayName(inventoryData.getName());
                List<String> lore = new ArrayList<>();
                for(String l : inventoryData.getLore()){
                    if(l.contains("%history%")){
                        int i = 0;
                        for(String history : playerData.getHistory()){
                            if(i == 0){
                                lore.add(l.replace("%history%", history));
                                i++;
                                continue;
                            }
                            lore.add(history);
                        }
                        continue;
                    }
                    lore.add(l.replace("%full_name%", playerData.getFullName()).replace("%date_birth%", playerData.getDateBirth()).replace("%nationality%", playerData.getNationality()).replace("%gender%", playerData.getGender()).replace("%job%", playerData.getJob()));
                }
                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
                inventory.setItem(inventoryData.getSlot(), itemStack);
                continue;
            }
            ItemStack itemStack = new ItemStack(inventoryData.getItem());
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(inventoryData.getName());
            List<String> lore = new ArrayList<>();
            for(String l : inventoryData.getLore()){
                if(l.contains("%history%")){
                    int i = 0;
                    for(String history : playerData.getHistory()){
                        if(i == 0){
                            lore.add(l.replace("%history%", history));
                            i++;
                            continue;
                        }
                        lore.add(history);
                    }
                    continue;
                }
                lore.add(l.replace("%full_name%", playerData.getFullName()).replace("%date_birth%", playerData.getDateBirth()).replace("%nationality%", playerData.getNationality()).replace("%gender%", playerData.getGender()).replace("%job%", playerData.getJob()));
            }
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(inventoryData.getSlot(), itemStack);
        }
        player.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.gui.open_inventory"));
        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
