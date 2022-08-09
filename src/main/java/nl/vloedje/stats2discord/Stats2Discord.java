package nl.vloedje.stats2discord;

import nl.vloedje.stats2discord.bot.DiscordBotHandler;
import nl.vloedje.stats2discord.bot.modules.BotModule;
import nl.vloedje.stats2discord.bot.modules.StartModule;
import nl.vloedje.stats2discord.bot.modules.commands.CommandModule;
import nl.vloedje.stats2discord.settings.ConfigurationInstance;
import nl.vloedje.stats2discord.utils.Logger;
import nl.vloedje.stats2discord.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Arrays;
import java.util.Objects;

public class Stats2Discord extends JavaPlugin {

    private static ConfigurationInstance mainConfig;
    private static ConfigurationInstance messagesConfig;

    private static DiscordBotHandler discordBotHandler;

    @Override
    public void onEnable() {
        //getCommand("showtest").setExecutor(new TestCommand());

        //FILES
        mainConfig = new ConfigurationInstance(this, "config.yml");
        messagesConfig = new ConfigurationInstance(this, "messages.yml");

        //CONFIG.YML
        mainConfig.get().addDefault("debug", false);
        mainConfig.get().addDefault("bot.token", "TOKEN");
        mainConfig.get().addDefault("bot.guild", 111111111111111111L);
        mainConfig.get().addDefault("bot.channels.logs", 111111111111111111L);
        mainConfig.get().addDefault("bot.activity", "play.example.com");
        mainConfig.get().addDefault("stats", Arrays.asList(
                "`m.sandstone: {m.sandstone}",
                "m.stone: {m.stone}`"
        ));

        mainConfig.get().options().copyDefaults(true);
        mainConfig.save();

        if(Objects.requireNonNull(mainConfig.get().getString("bot.token")).equalsIgnoreCase("TOKEN")) {
            Logger.error("Setup your bot in the config!");
            return;
        }

        Logger.enabled = mainConfig.get().getBoolean("debug");

        //MESSAGES.YML
        Messages.registerMessages();

        messagesConfig.get().options().copyDefaults(true);
        messagesConfig.save();

        discordBotHandler = new DiscordBotHandler(this,
                mainConfig.get().getString("bot.token"),
                mainConfig.get().getLong("bot.guild"),
                mainConfig.get().getLong("bot.channels.logs"),
                mainConfig.get().getString("bot.activity")
        );

        discordBotHandler.registerModule(new StartModule(discordBotHandler), new CommandModule(discordBotHandler, this));
    }

    @Override
    public void onDisable() {
        if(discordBotHandler != null) {
            for(BotModule module : discordBotHandler.getModules()) {
                module.onDisable();
            }
        }
    }

    public static ConfigurationInstance getMainConfig() {
        return mainConfig;
    }

    public static ConfigurationInstance getMessagesConfig() {
        return messagesConfig;
    }

    public static DiscordBotHandler getDiscordBotHandler() {
        return discordBotHandler;
    }

    public static Plugin getPlugin() {
        return Bukkit.getServer().getPluginManager().getPlugin("Stats2Discord");
    }
}
