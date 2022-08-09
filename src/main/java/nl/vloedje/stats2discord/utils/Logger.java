package nl.vloedje.stats2discord.utils;

import net.md_5.bungee.api.ChatColor;
import static org.bukkit.Bukkit.getLogger;

public class Logger {

    public static boolean enabled = false;

    public static void debug(String msg) {
        if(!enabled) return;
        getLogger().info(ChatColor.translateAlternateColorCodes('&', "[Stats2Discord Debugger] " + msg));
    }

    public static void error(String msg) {
        getLogger().info(ChatColor.translateAlternateColorCodes('&', "[Stats2Discord Error] " + msg));
    }

    public static void log(String msg) {
        getLogger().info(ChatColor.translateAlternateColorCodes('&', "[Stats2Discord Logger] " + msg));
    }
}

