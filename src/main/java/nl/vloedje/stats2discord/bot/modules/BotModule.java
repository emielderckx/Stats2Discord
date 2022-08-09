package nl.vloedje.stats2discord.bot.modules;

import nl.vloedje.stats2discord.bot.DiscordBotHandler;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public abstract class BotModule extends ListenerAdapter {

    private String id;
    public DiscordBotHandler botHandler;

    public BotModule(String id, DiscordBotHandler botHandler) {
        this.id = id;
        this.botHandler = botHandler;
    }

    public abstract void onEnable();
    public abstract void onDisable();

    public String getId() {
        return id;
    }
}
