package nl.vloedje.stats2discord.utils;

import nl.vloedje.stats2discord.Stats2Discord;
import net.md_5.bungee.api.ChatColor;
import java.util.ArrayList;
import java.util.List;

public class Messages {

    public static String PREFIX;
    public static String NO_PERMISSION;
    public static String NO_CONSOLE;
    public static String USAGE;
    public static String UNKNOWN_PLAYER;
    public static String UNKNOWN_ERROR;
    public static String ACTION_COMPLETED;

    public static String GET_HELP;
    public static String HELP_0;
    public static String HELP_COMMAND;
    public static String UNKNOWN_COMMAND;

    public static String BOT_ENABLED;
    public static String BOT_DISABLED;
    public static String COMMAND_BOT_STATS;
    public static String MOJANG_ERROR;
    public static String PLAYER_SCORE;
    public static String UNKNOWN_OBJECTIVE;
    public static String SERVER_TOTALS;
    public static String PLAYER_TOTALS;
    public static String WRONG_USAGE;
    public static String VALUE_TOP;
    public static String VALUE_TOP_N;
    public static String LOADING_TOP;

    public static void registerMessages() {
        PREFIX = registerMessage("PREFIX", "&6&lStats2Discord &r&7> &r");
        NO_PERMISSION = registerMessage("NO_PERMISSION", "&cYou are lacking the permission: {permission}.");
        NO_CONSOLE = registerMessage("NO_CONSOLE", "&cUnable to execute this command in the console.");
        USAGE = registerMessage("USAGE", "&cUsage: {usage}");
        UNKNOWN_PLAYER = registerMessage("UNKNOWN_PLAYER", "Unknown player.");
        UNKNOWN_ERROR = registerMessage("UNKNOWN_ERROR", "&cUnknown error.");
        ACTION_COMPLETED = registerMessage("ACTION_COMPLETED", "Action finished.");

        GET_HELP = registerMessage("GET_HELP", "&cUse /{id} help to get a list of commands.");
        HELP_0 = registerMessage("HELP_0", "&6->-- /{id} help --<-");
        HELP_COMMAND = registerMessage("HELP_COMMAND", "&7/{id} {usage} ; &6{description}");
        UNKNOWN_COMMAND = registerMessage("UNKNOWN_COMMAND", "&cUnknown command. Use /{id} help to get a list of commands.");

        BOT_ENABLED = registerMessage("BOT_ENABLED", "The Discord Bot Engine was successfully enabled.");
        BOT_DISABLED = registerMessage("BOT_DISABLED", "The Discord Bot Engine was disabled.");
        COMMAND_BOT_STATS = registerMessage("COMMAND_BOT_STATS", "Stats command");
        MOJANG_ERROR = registerMessage("MOJANG_ERROR", "It seems like the Mojang servers are offline. Try again later.");
        PLAYER_SCORE = registerMessage("PLAYER_SCORE", "{player}'s score for {objective} is: {score}.");
        UNKNOWN_OBJECTIVE = registerMessage("UNKNOWN_OBJECTIVE", "Unknown objective.");
        SERVER_TOTALS = registerMessage("SERVER_TOTALS", "Server totals:");
        PLAYER_TOTALS = registerMessage("PLAYER_TOTALS", "{player}'s stats:");
        WRONG_USAGE = registerMessage("WRONG_USAGE", "Either the player does not exist or the objective does not exist. Try again.");
        VALUE_TOP = registerMessage("VALUE_TOP", "Top 10 for {objective}:");
        VALUE_TOP_N = registerMessage("VALUE_TOP_N", "{number}. {player} ({score})");
        LOADING_TOP = registerMessage("LOADING_TOP", "We are loading the top list. Please wait.");
    }

    private static String registerMessage(String name, String message) {
        Stats2Discord.getMessagesConfig().get().addDefault(name, message);
        return translate(Stats2Discord.getMessagesConfig().get().getString(name));
    }

    public static List<String> replace(List<String> list, String s1, String s2) {
        List<String> newList = new ArrayList<>();
        for(String s : list) {
            newList.add(s.replace(s1, s2));
        }

        return newList;
    }

    public static String translate(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
