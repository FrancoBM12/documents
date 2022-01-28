package com.francobm.documents.listeners;

import com.francobm.documents.Documents;
import com.francobm.documents.cache.PlayerData;
import com.francobm.documents.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerListener implements Listener {
    private final Documents plugin;

    public PlayerListener(Documents plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        plugin.getSql().loadPlayer(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        PlayerData playerData = plugin.getPlayer(event.getPlayer().getName());
        if(playerData == null) return;
        plugin.getSql().savePlayer(playerData);
    }

    @EventHandler
    public void onClickInventory(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if(event.getView().getTitle().equalsIgnoreCase(plugin.getMenus().getString("menus.dni.title"))){
            event.setCancelled(true);
            //if (event.getCurrentItem() == null) return;
            //if(event.getCurrentItem().getItemMeta() == null) return;
            /*InventoryData inventoryData = plugin.getInventory(event.getCurrentItem().getItemMeta().getDisplayName());
            if(inventoryData == null) return;*/

        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        PlayerData playerData = plugin.getPlayer(player.getName());
        if(playerData == null) return;
        if(playerData.getComplete()) return;
        if(playerData.getChat().isEmpty()) return;
        String message = event.getMessage();
        Pattern pattern = Pattern.compile("[^A-Za-z0-9]");
        Matcher match = pattern.matcher(message);
        boolean symbols = match.find();
        event.setCancelled(true);
        switch (playerData.getChat().toLowerCase()){
            case "name":
                if (symbols || message.contains(" ")) {
                    player.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.rare_symbols"));
                    return;
                }
                playerData.setName(message);
                playerData.setChat("");
                playerData.getPlayer().sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.create.name-success"));
                plugin.getDocumentManager().setupLastNameDocument(playerData);
                plugin.getDocumentManager().completeDocument(playerData);
                break;
            case "lastname":
                if (symbols || message.contains(" ")) {
                    player.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.rare_symbols"));
                    return;
                }
                playerData.setLastName(message);
                playerData.setChat("");
                playerData.getPlayer().sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.create.lastname-success"));
                plugin.getDocumentManager().setupDateBirthDocument(playerData);
                plugin.getDocumentManager().completeDocument(playerData);
                break;
            case "datebirth":
                try {
                    Date date = new SimpleDateFormat("dd/MM/yyyy").parse(message);
                    playerData.setDateBirth(message);
                    playerData.setChat("");
                    playerData.getPlayer().sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.create.datebirth-success"));
                    plugin.getDocumentManager().setupNationalityDocument(playerData);
                    plugin.getDocumentManager().completeDocument(playerData);
                } catch (ParseException e) {
                    player.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.create.datebirth-format-error"));
                }
                break;
            case "nationality":
                if (symbols || message.contains(" ")) {
                    player.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.rare_symbols"));
                    return;
                }
                playerData.setNationality(message);
                playerData.setChat("");
                playerData.getPlayer().sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.create.nationality-success"));
                plugin.getDocumentManager().setupGenderDocument(playerData);
                plugin.getDocumentManager().completeDocument(playerData);
                break;
            case "gender":
                if (symbols || message.contains(" ")) {
                    player.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.rare_symbols"));
                    return;
                }
                playerData.setGender(message);
                playerData.setChat("");
                playerData.getPlayer().sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.create.gender-success"));
                plugin.getDocumentManager().setupHistoryDocument(playerData);
                plugin.getDocumentManager().completeDocument(playerData);
                break;
            case "history":
                if(message.equalsIgnoreCase(plugin.getMessages().getString("dni.history-finish"))){
                    playerData.setChat("");
                    playerData.getPlayer().sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.create.history-success"));
                    plugin.getDocumentManager().setupJobDocument(playerData);
                    return;
                }
                if(message.length() > plugin.getConfig().getInt("history-length")){
                    player.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.create.history-max-length").replace("%max_characters%", String.valueOf(plugin.getConfig().getInt("history-length"))));
                    return;
                }
                playerData.getHistory().add(Utils.ChatColor("&e") + message);
                player.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.create.nextline-history").replace("%history_finish%", plugin.getMessages().getString("dni.history-finish")));
                break;
        }
    }
}
