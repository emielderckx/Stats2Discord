package nl.vloedje.stats2discord.bot;

import nl.vloedje.stats2discord.Stats2Discord;
import nl.vloedje.stats2discord.bot.modules.BotModule;
import nl.vloedje.stats2discord.utils.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import javax.security.auth.login.LoginException;
import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DiscordBotHandler {

    private Stats2Discord plugin;

    private JDA jda = null;

    private String BOT_TOKEN;
    private String BOT_ACTIVITY;

    private Guild guild;
    private MessageChannel logs;

    private List<BotModule> modules;
    public DiscordBotHandler(Stats2Discord plugin, String token, long guildID, long logChannelID, String activity) {
        this.plugin = plugin;
        this.BOT_TOKEN = token;
        this.BOT_ACTIVITY = activity;

        this.modules = new ArrayList<>();

        Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin,
                new Runnable() {
                    @Override
                    public void run() {
                        JDABuilder builder = JDABuilder.createDefault(token);
                        builder.setActivity(Activity.playing(BOT_ACTIVITY));

                        try {
                            Logger.debug("Defining JDA");
                            jda = builder.build().awaitReady();
                            Logger.debug("JDA defined");
                        } catch (InterruptedException | LoginException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        new BukkitRunnable() {
            @Override
            public void run() {
                if(jda == null) return;
                Logger.debug("Guild: " + guildID);
                Logger.debug("Logs: " + logChannelID);

                try {
                    guild = jda.getGuildById(guildID);
                    logs = guild.getTextChannelById(logChannelID);

                    for(BotModule module : modules) {
                        module.onEnable();
                        Logger.log("BotModule " + module.getId() + " enabled.");
                    }
                } catch(Exception e) {
                    Logger.error("Invalid GUILD or LOGS information. Check your config!");
                    if(Logger.enabled) {
                        e.printStackTrace();
                    }
                }

                this.cancel();
            }
        }.runTaskTimer(plugin, 40, 40);
    }

    public void registerModule(BotModule... modules) {
        for(BotModule module : modules) {
            this.modules.add(module);
        }
    }

    public List<BotModule> getModules() {
        return modules;
    }

    public JDA getBot() {
        return jda;
    }

    public void embed(MessageChannel channel, String title, String message, Color color) {
        EmbedBuilder builder = new EmbedBuilder();
        if(jda.getSelfUser().getAvatarUrl() != null) builder.setAuthor(jda.getSelfUser().getName(), null, jda.getSelfUser().getAvatarUrl()+"?size=256");
        builder.setTitle(title);
        builder.setDescription(message);
        builder.setColor(color);

        builder.setTimestamp(Instant.now());

        channel.sendMessage(builder.build()).queue();
    }

    public void log(String title, String message, Color color) {
        embed(logs, title, message, color);
    }

    public Guild getGuild() {
        return guild;
    }

    public MessageChannel getLogs() {
        return logs;
    }

    public void sendMessage(MessageChannel channel, String content) {
        channel.sendMessage(ChatColor.stripColor(content)).queue();
    }

    //        channel.sendMessage(ChatColor.stripColor(content)).queue();
    public void sendTempMessage(MessageChannel channel, String content) {
        channel.sendMessage(ChatColor.stripColor(content)).queue((message) -> {
            message.delete().queueAfter(5, TimeUnit.SECONDS);
        });
    }
}
