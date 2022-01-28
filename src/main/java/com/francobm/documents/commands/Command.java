package com.francobm.documents.commands;

import com.francobm.documents.Documents;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Command implements CommandExecutor{
    private Documents plugin;

    public Command(Documents plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length >= 1){
                if(args[0].equalsIgnoreCase("ver") || args[0].equalsIgnoreCase("see")){
                    plugin.getDocumentManager().openDocumentGui(player);
                    return true;
                    //abre menu con tus datos
                }else if(args[0].equalsIgnoreCase("mostrar") || args[0].equalsIgnoreCase("show")){
                    double radius = plugin.getConfig().getDouble("radius-player");
                    for(Entity entity : player.getNearbyEntities(radius, radius, radius)){
                        if(entity.isDead() || !entity.isValid()) continue;
                        if(!(entity instanceof Player)) continue;
                        Player target = (Player) entity;
                        plugin.getDocumentManager().showDocumentGui(player, target);
                        return true;
                    }
                    player.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.gui.no_player_around"));
                    return true;
                    //le muestra a un jugador tus datos
                }else if(args[0].equalsIgnoreCase("crear") || args[0].equalsIgnoreCase("create")){
                    plugin.getDocumentManager().createDocument(player);
                    return true;
                }else if(args[0].equalsIgnoreCase("reload")){
                    if(!player.hasPermission("documents.reload")){
                        player.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.no-permission"));
                        return true;
                    }
                    plugin.getMenus().reload();
                    plugin.getConfig().reload();
                    plugin.getMessages().reload();
                    player.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.reload"));
                    return true;
                }else if(args[0].equalsIgnoreCase("borrar") || args[0].equalsIgnoreCase("eliminar")){
                    if(!player.hasPermission("documents.remove")){
                        player.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.no-permission"));
                        return true;
                    }
                    if(args.length != 2) {
                        player.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.remove.usage"));
                        return true;
                    }
                    Player target = Bukkit.getPlayer(args[1]);
                    if(target == null){
                        player.sendMessage(plugin.pluginName + plugin.getMessages().getString("dni.offline-player"));
                        return true;
                    }
                    plugin.getDocumentManager().deleteDocument(player, target);
                    return true;
                } else if(args[0].equalsIgnoreCase("help")){
                    plugin.getDocumentManager().sendHelp(player);
                    return true;
                }
            }
            plugin.getDocumentManager().openDocumentGui(player);
            return true;
        }
        return true;
    }
}
