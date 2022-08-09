package nl.vloedje.stats2discord.bot.modules.commands;

import nl.vloedje.stats2discord.Stats2Discord;
import nl.vloedje.stats2discord.bot.modules.BotModule;
import nl.vloedje.stats2discord.bot.DiscordBotHandler;
import nl.vloedje.stats2discord.utils.Logger;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandModule extends BotModule implements Listener {

    private Stats2Discord plugin;

    private List<BotCommand> commands;

    public CommandModule(DiscordBotHandler botHandler, Stats2Discord plugin) {
        super("CommandModule", botHandler);

        this.plugin = plugin;
    }

    @Override
    public void onEnable() {
        this.commands = new ArrayList<>();
        commands.add(new StatsCommand(Stats2Discord.getMainConfig().get().getStringList("stats")));

        botHandler.getBot().addEventListener(this);
    }

    @Override
    public void onDisable() {
        botHandler.getBot().removeEventListener(this);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        try {
            Logger.debug("message event fired");

            if (e.getAuthor().isBot()) {
                return;
            }

            String cmd = e.getMessage().getContentRaw().replace("!", "");
            String[] args = cmd.split(" ");

            //e.getChannel().deleteMessageById(e.getMessageIdLong()).queue();

            for (BotCommand command : commands) {
                if (command.hasAlias(args[0])) {
                    ArrayList<String> newArgs = new ArrayList<String>();
                    Collections.addAll(newArgs, args);
                    newArgs.remove(0);
                    args = newArgs.toArray(new String[newArgs.size()]);

                    String[] finalArgs = args;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            command.initiateCommand(e.getAuthor(), e.getChannel(), finalArgs);
                        }
                    }.runTaskAsynchronously(Stats2Discord.getPlugin());
                    return;
                }
            }
        } catch(Exception ex) {
            if(Logger.enabled) ex.printStackTrace();
        }
    }
}
