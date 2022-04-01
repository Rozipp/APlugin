package ua.rozipp.abstractplugin;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ua.rozipp.abstractplugin.exception.InvalidConfiguration;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ASettingMaster {

	private static String FOLDERNAME = "data/";
	private APlugin plugin;
	private final FileConfiguration mainConfig;
	private Map<String, FileConfiguration> configsFiles = new HashMap<>();

	public ASettingMaster(APlugin plugin){
		//save a copy of the default config.yml if one is not there
		this.plugin = plugin;
		plugin.saveDefaultConfig();
		mainConfig = plugin.getConfig();
		try {
			plugin.getLogger().setLevel(Level.parse(getString("", "loglevel")));
		} catch (InvalidConfiguration e1){
			plugin.getLogger().warning(e1.getMessage());
		}

		try {
			getString("", "loglevel");
		} catch (InvalidConfiguration e1){
			plugin.getLogger().warning(e1.getMessage());
		}

	}

	public void loadConfig(String name) throws IOException, InvalidConfigurationException {
		configsFiles.put(name, loadFileConfig(FOLDERNAME + name + ".yml", false));
	}

	private FileConfiguration getFileConfig(String fileName){
		if (fileName == null || fileName.isEmpty() || !configsFiles.containsKey(fileName)) return mainConfig;
		return configsFiles.get(fileName);
	}

	public FileConfiguration loadFileConfig(String filepath, boolean replace) throws IOException, InvalidConfigurationException {
		File file = new File(plugin.getPlugin().getDataFolder().getPath() + "/" + filepath);
		if (!file.exists()) {
			plugin.getLogger().warning("Configuration file: " + filepath + " was missing. Streaming to disk from Jar.");
			plugin.saveResource(filepath, replace);
		}

		plugin.getLogger().info("Loading Configuration file: " + filepath);
		// read the config.yml into memory
		YamlConfiguration cfg = new YamlConfiguration();
		cfg.load(file);
		return cfg;
	}

	public String getString(String fileName, String path) throws InvalidConfiguration {
		FileConfiguration cfg = getFileConfig(fileName);
		if (!cfg.contains(path)) throw new InvalidConfiguration(FOLDERNAME + fileName + ".yml", path);
		plugin.getLogger().config(path + ": " + cfg.getString(path));
		return cfg.getString(path);
	}

	public int getInteger(String fileName, String path) throws InvalidConfiguration {
		FileConfiguration cfg = getFileConfig(fileName);
		if (!cfg.contains(path)) throw new InvalidConfiguration(FOLDERNAME + fileName + ".yml", path);
		plugin.getLogger().config(path + ": " + cfg.getInt(path));
		return cfg.getInt(path);
	}

	public double getDouble(String fileName, String path) throws InvalidConfiguration {
		FileConfiguration cfg = getFileConfig(fileName);
		if (!cfg.contains(path)) throw new InvalidConfiguration(FOLDERNAME + fileName + ".yml", path);
		plugin.getLogger().config(path + ": " + cfg.getDouble(path));
		return cfg.getDouble(path);
	}

	public boolean getBoolean(String fileName, String path) throws InvalidConfiguration {
		FileConfiguration cfg = getFileConfig(fileName);
		plugin.getLogger().info(cfg.getCurrentPath());
		if (!cfg.contains(path)) throw new InvalidConfiguration(FOLDERNAME + fileName + ".yml", path);
		plugin.getLogger().config(path + ": " + cfg.getBoolean(path));
		return cfg.getBoolean(path);
	}

	public String getStringOrDefault(String fileName, String path, String defaultValue){
		try {
			return getString(fileName, path);
		} catch (InvalidConfiguration e) {
			return defaultValue;
		}
	}

	public int getIntegerOrDefault(String fileName, String path, int defaultValue){
		try {
			return getInteger(fileName, path);
		} catch (InvalidConfiguration e) {
			return defaultValue;
		}
	}

	public double getDoubleOrDefault(String fileName, String path, double defaultValue){
		try {
			return getDouble(fileName, path);
		} catch (InvalidConfiguration e) {
			return defaultValue;
		}
	}

	public boolean getBooleanOrDefault(String fileName, String path, boolean defaultValue){
		try {
			return getBoolean(fileName, path);
		} catch (InvalidConfiguration e) {
			return defaultValue;
		}
	}

}
