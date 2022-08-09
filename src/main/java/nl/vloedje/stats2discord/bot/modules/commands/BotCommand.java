package nl.vloedje.stats2discord.bot.modules.commands;

import nl.vloedje.stats2discord.Stats2Discord;
import nl.vloedje.stats2discord.utils.Messages;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public abstract class BotCommand {

    private String id;
    private String usage;
    private int length;
    private String[] aliases;
    private String description;

    public BotCommand(String id, String usage, int length, String[] aliases, String description) {
        this.id = id;
        this.usage = usage;
        this.length = length;
        this.aliases = aliases;
        this.description = description;
    }

    public void initiateCommand(User sender, MessageChannel channel, String[] args) {
        if(args.length < length) {
            Stats2Discord.getDiscordBotHandler().sendTempMessage(channel, Messages.PREFIX + Messages.USAGE.replace("{usage}", usage));
            return;
        }

        onCommand(sender, channel, args);
    }

    public abstract void onCommand(User sender, MessageChannel channel, String[] args);

    public String getId() {
        return id;
    }

    public String getUsage() {
        return usage;
    }

    public String[] getAliases() {
        return aliases;
    }

    public boolean hasAlias(String alias) {
        if(id.equalsIgnoreCase(alias)) return true;

        for(String s : aliases) {
            if(s.equalsIgnoreCase(alias)) return true;
        }

        return false;
    }

    public String getDescription() {
        return description;
    }
}
