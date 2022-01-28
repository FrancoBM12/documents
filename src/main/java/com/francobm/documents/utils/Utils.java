package com.francobm.documents.utils;

import org.bukkit.ChatColor;

public class Utils {

    public static String ChatColor(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
