package nl.vloedje.stats2discord.settings;

import nl.vloedje.stats2discord.utils.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigurationInstance {

    private JavaPlugin plugin;

    private String fileName;

    private FileConfiguration fileConfiguration;
    private File configurationFile;

    public ConfigurationInstance(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;

        if(!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        configurationFile = new File(plugin.getDataFolder(), fileName);

        if (!configurationFile.exists()) {
            try {
                configurationFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Logger.error("&4Could not create " + fileName + "!");
            }
        }

        fileConfiguration = YamlConfiguration.loadConfiguration(configurationFile);
    }

    public FileConfiguration get() {
        return fileConfiguration;
    }

    public void save() {
        try {
            fileConfiguration.save(configurationFile);
        } catch (Exception e) {
            Logger.error("&4Could not save " + fileName + "!");
        }
    }

    public void reload() {
        fileConfiguration = YamlConfiguration.loadConfiguration(configurationFile);
    }
}
