package com.francobm.documents.managers;

import com.francobm.documents.Documents;
import com.francobm.documents.cache.PlayerData;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DocumentManager {
    private final Documents plugin;

    public DocumentManager(Documents plugin){
        this.plugin = plugin;
    }

    public void openDocumentGui(Player player){
        new InventoryManager(plugin, player);
    }
    public void showDocumentGui(Player player, Player target){
        new InventoryManager(plugin, player, target);
    }

    public void sendHelp(Player player){
        for(String message : plugin.getMessages().getStringList("dni.help")){
            player.sendMessage(message);
        }
    }

    public void createDocument(Player player){
        PlayerData playerData = plugin.getPlayer(player.getName());
        if(playerData == null) return;
        setupCreateDocument(playerData);
    }

    public void deleteDocument(Player player, Player target){
        if(plugin.getSql().removePlayer(target)){
            player.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.remove.success").replace("%target%", target.getName()));
            target.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.remove.other-success").replace("%player%", player.getName()));
            return;
        }
        player.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.remove.error").replace("%target%", target.getName()));
    }

    public void setupCreateDocument(PlayerData playerData){
        if(playerData.getComplete()){
            playerData.getPlayer().sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.create.already"));
            return;
        }
        if(playerData.getName().isEmpty()) {
            setupNameDocument(playerData);
            return;
        }
        if(playerData.getLastName().isEmpty()){
            setupLastNameDocument(playerData);
            return;
        }
        if(playerData.getDateBirth().isEmpty()){
            setupDateBirthDocument(playerData);
            return;
        }
        if(playerData.getNationality().isEmpty()){
            setupNationalityDocument(playerData);
            return;
        }
        if(playerData.getGender().isEmpty()){
            setupGenderDocument(playerData);
            return;
        }
        if(playerData.getJob().isEmpty()){
            setupJobDocument(playerData);
            return;
        }
        if(playerData.getHistory().isEmpty()){
            setupHistoryDocument(playerData);
        }
    }

    public void setupNameDocument(PlayerData playerData){
        if(!playerData.getName().isEmpty()) return;
        playerData.getPlayer().sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.create.request-name"));
        playerData.setChat("Name");
    }

    public void completeDocument(PlayerData playerData){
        if(!playerData.getComplete()) return;
        playerData.getPlayer().sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.create.complete"));
    }

    public void setupLastNameDocument(PlayerData playerData){
        if(!playerData.getLastName().isEmpty()) return;
        playerData.getPlayer().sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.create.request-lastname"));
        playerData.setChat("LastName");
    }

    public void setupDateBirthDocument(PlayerData playerData){
        if(!playerData.getDateBirth().isEmpty()) return;
        playerData.getPlayer().sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.create.request-datebirth"));
        playerData.setChat("DateBirth");
    }

    public void setupNationalityDocument(PlayerData playerData){
        if(!playerData.getNationality().isEmpty()) return;
        playerData.getPlayer().sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.create.request-nationality"));
        playerData.setChat("Nationality");
    }

    public void setupGenderDocument(PlayerData playerData){
        if(!playerData.getGender().isEmpty()) return;
        playerData.getPlayer().sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.create.request-gender"));
        playerData.setChat("Gender");
    }

    public void setupJobDocument(PlayerData playerData){
        if(!playerData.getJob().isEmpty()) {
            completeDocument(playerData);
            return;
        }
        String prefix = "%luckperms_prefix%";
        prefix = PlaceholderAPI.setPlaceholders(playerData.getPlayer(), prefix);
        String finalPrefix = ChatColor.stripColor(prefix);
        if(finalPrefix.isEmpty()){
            playerData.setJob(plugin.getMessages().getString("dni.not-job-prefix"));
            playerData.getPlayer().sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.create.not-job"));
            completeDocument(playerData);
            return;
        }
        playerData.getPlayer().sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.create.request-job").replace("%job%", finalPrefix));
        new BukkitRunnable() {
            @Override
            public void run() {
                if(playerData.getPlayer() == null){
                    cancel();
                    return;
                }
                playerData.setJob(finalPrefix);
                playerData.getPlayer().sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.create.job-success"));
                completeDocument(playerData);
            }
        }.runTaskLater(plugin, 20L);
    }

    public void setupHistoryDocument(PlayerData playerData){
        if(!playerData.getHistory().isEmpty()) return;
        playerData.getPlayer().sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.create.request-history"));
        playerData.setChat("History");
    }
}
