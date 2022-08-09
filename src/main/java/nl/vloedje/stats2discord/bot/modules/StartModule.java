package nl.vloedje.stats2discord.bot.modules;

import nl.vloedje.stats2discord.bot.DiscordBotHandler;
import nl.vloedje.stats2discord.utils.Messages;
import java.awt.*;

public class StartModule extends BotModule {

    public StartModule(DiscordBotHandler botHandler) {
        super("StartModule", botHandler);
    }

    @Override
    public void onEnable() {
        botHandler.log(Messages.BOT_ENABLED, "", Color.GREEN);
    }

    @Override
    public void onDisable() {
        botHandler.log(Messages.BOT_DISABLED, "", Color.RED);
    }
}
