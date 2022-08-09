package nl.vloedje.stats2discord.bot.modules.commands;

import nl.vloedje.stats2discord.Stats2Discord;
import nl.vloedje.stats2discord.bot.DiscordBotHandler;
import nl.vloedje.stats2discord.utils.Messages;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownServiceException;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatsCommand extends BotCommand {

    private List<String> stats;

    public StatsCommand(List<String> stats) {
        super("stats", "stats", 0, new String[]{"statistic", "statistics"}, Messages.COMMAND_BOT_STATS);

        this.stats = stats;
    }

    @Override
    public void onCommand(User sender, MessageChannel channel, String[] args) {
        DiscordBotHandler botHandler = Stats2Discord.getDiscordBotHandler();

        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();

        String text = "";
        for(String s : stats) {
            text += s + "\n";
        }

        if(args.length == 0) {
            text = replaceTags(text, null);

            //botHandler.sendMessage(channel, Messages.SERVER_TOTALS);
            //botHandler.sendMessage(channel, text);

            botHandler.embed(channel, Messages.SERVER_TOTALS, text, Color.getHSBColor(0, (float) 0, (float) 33.33));
            return;
        } else if(args.length == 1) {
            UUID uuid = getUUID(args[0]);
            if(uuid != null) {
                OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(uuid);
                if(player.hasPlayedBefore() || player.isOnline()) {
                    //player requested
                    text = replaceTags(text, player);

                    String title = Messages.PLAYER_TOTALS.replace("{player}", player.getName());

                    //botHandler.sendMessage(channel, title);
                    //botHandler.sendMessage(channel, text);

                    botHandler.embed(channel, title, text, Color.getHSBColor(0, (float) 0, (float) 33.33));

                    return;
                }
            }

            //top 10 voor een eigenschap
            Objective objective = sb.getObjective(args[0]);
            if(objective == null) {
                botHandler.sendMessage(channel, Messages.WRONG_USAGE);
                return;
            }

            botHandler.sendMessage(channel, Messages.LOADING_TOP);

            List<SortObject> top = getTop(objective);

            String msg = "";
            //msg += Messages.VALUE_TOP.replace("{objective}", objective.getName()) + "\n";
            for(int i = 0; i<=9 && i < top.size(); i++) {
                msg += Messages.VALUE_TOP_N
                        .replace("{number}", String.valueOf(i + 1))
                        .replace("{player}", top.get(i).entry)
                        .replace("{score}", String.valueOf(top.get(i).score))
                        + "\n";
            }

            //botHandler.sendMessage(channel, msg);

            botHandler.embed(channel, Messages.VALUE_TOP.replace("{objective}", objective.getName()), msg, Color.getHSBColor(0, (float) 0, (float) 33.33));

            return;
        } else if(args.length == 2) {
            UUID uuid = getUUID(args[0]);
            if(uuid == null) {
                botHandler.sendMessage(channel, Messages.MOJANG_ERROR);
                return;
            }
            OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(uuid);
            if(!player.hasPlayedBefore() && !player.isOnline()) {
                botHandler.sendMessage(channel, Messages.UNKNOWN_PLAYER);
                return;
            }

            String objective = args[1];
            if(sb.getObjective(objective) == null) {
                botHandler.sendMessage(channel, Messages.UNKNOWN_OBJECTIVE);
                return;
            }

            int score = sb.getObjective(objective).getScore(player.getName()).getScore();

            /*botHandler.sendMessage(channel, Messages.PLAYER_SCORE
                    .replace("{player}", player.getName())
                    .replace("{objective}", objective)
                    .replace("{score}", String.valueOf(score))
            );*/

            botHandler.embed(channel, player.getName(), Messages.PLAYER_SCORE
                    .replace("{player}", player.getName())
                    .replace("{objective}", objective)
                    .replace("{score}", String.valueOf(score)), Color.getHSBColor(0, (float) 0, (float) 33.33));

            return;
        }
    }

    private List<SortObject> getTop(Objective o) {
        /*List<SortObject> top = new ArrayList<>();
        for(String s : o.getScoreboard().getEntries()) {
            if(o.getScore(s) == null) continue;
            OfflinePlayer p = Bukkit.getServer().getOfflinePlayer(s);
            if(!p.hasPlayedBefore() && !p.isOnline()) continue;
            top.add(new SortObject(s, o.getScore(s).getScore()));
        }

        Collections.sort(top);*/

        List<SortObject> top = new ArrayList<>();
        int min = 0;
        for(String s : o.getScoreboard().getEntries()) {
            if(o.getScore(s) == null) continue;
            int score = o.getScore(s).getScore();
            if(score <= min || score <= 0) continue;

            /*OfflinePlayer p = Bukkit.getServer().getOfflinePlayer(s);
            if(!p.hasPlayedBefore() && !p.isOnline()) continue;*/

            if(top.size() <= 10 || score > min) {
                top.add(new SortObject(s, o.getScore(s).getScore()));
            }

            Collections.sort(top);
            if(top.size() > 10) {
                top.remove(10);
            }
        }

        return top;
    }

    private String replaceTags(String text, OfflinePlayer player) {
        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();

        Pattern p = Pattern.compile("\\{(.*?)\\}");
        Matcher m = p.matcher(text);
        while(m.find()) {
            String preO = m.group(1);
            Objective objective = sb.getObjective(preO);
            if(objective == null) {
                continue;
            }

            if(player == null) {
                int total = 0;
                for(String name : objective.getScoreboard().getEntries()) {
                    total += objective.getScore(name).getScore();
                }

                text = text.replace("{" + preO + "}", String.valueOf(total));
            } else {
                text = text.replace("{" + preO + "}", String.valueOf(objective.getScore(player.getName()).getScore()));
            }
        }

        return text;
    }

    public UUID getUUID(String playerName) {
        HttpURLConnection c;
        String id;
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(5000);
            c.setReadTimeout(5000);
            c.connect();
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(new InputStreamReader((InputStream)c.getContent()));
            JsonObject object = element.getAsJsonObject();
            id = object.get("id").getAsString();
        } catch (UnknownServiceException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if(id == null || id.equalsIgnoreCase("")) return null;

        return fromTrimmed(id);

    }

    public UUID fromTrimmed(String trimmedUUID) throws IllegalArgumentException{
        if(trimmedUUID == null) throw new IllegalArgumentException();
        StringBuilder builder = new StringBuilder(trimmedUUID.trim());
        /* Backwards adding to avoid index adjustments */
        try {
            builder.insert(20, "-");
            builder.insert(16, "-");
            builder.insert(12, "-");
            builder.insert(8, "-");
        } catch (StringIndexOutOfBoundsException e){
            throw new IllegalArgumentException();
        }

        return UUID.fromString(builder.toString());
    }

    public class SortObject implements Comparable<SortObject> {
        public String entry;
        public int score;

        public SortObject(String entry, int score) {
            this.entry = entry;
            this.score = score;
        }

        @Override
        public int compareTo(SortObject o) {
            return (this.score > o.score ? -1 :
                    (this.score == o.score ? 0 : 1));
        }
    }
}
