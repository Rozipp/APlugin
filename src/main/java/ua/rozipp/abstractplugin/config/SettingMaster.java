package ua.rozipp.abstractplugin.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ua.rozipp.example.main.ExamplePlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SettingMaster {

	private final FileConfiguration mainConfig;
	private Map<String, FileConfiguration> configsFiles = new HashMap<>();

	public SettingMaster(JavaPlugin thisPlugin){
		//save a copy of the default config.yml if one is not there
		thisPlugin.saveDefaultConfig();
		mainConfig = thisPlugin.getConfig();
	}

	public void init() throws IOException, InvalidConfigurationException {
		loadConfig("civ");
		loadConfig("town");
	}

	private void loadConfig(String name) throws IOException, InvalidConfigurationException {
		configsFiles.put(name, loadFileConfig("data/" + name + ".yml", false));
	}

	private FileConfiguration getFileConfig(String fileName){
		if (fileName == null || fileName.isEmpty() || !configsFiles.containsKey(fileName)) return mainConfig;
		return configsFiles.get(fileName);
	}

	public FileConfiguration loadFileConfig(String filepath, boolean replace) throws IOException, InvalidConfigurationException {
		File file = new File(ExamplePlugin.getPlugin().getPlugin().getDataFolder().getPath() + "/" + filepath);
		if (!file.exists()) {
			ExamplePlugin.getPlugin().getLogger().warning("Configuration file: " + filepath + " was missing. Streaming to disk from Jar.");
			ExamplePlugin.getPlugin().getPlugin().saveResource(filepath, replace);
		}

		ExamplePlugin.getPlugin().getLogger().info("Loading Configuration file: " + filepath);
		// read the config.yml into memory
		YamlConfiguration cfg = new YamlConfiguration();
		cfg.load(file);
		return cfg;
	}

	public String getString(String fileName, String path) throws InvalidConfiguration {
		FileConfiguration cfg = getFileConfig(fileName);
		if (!cfg.contains(path)) throw new InvalidConfiguration("Could not get configuration string \"" + path + "\" in file \"" + fileName + ".yml\"");
		ExamplePlugin.getPlugin().getLogger().config(path + ": " + cfg.getString(path));
		return cfg.getString(path);
	}

	public int getInteger(String fileName, String path) throws InvalidConfiguration {
		FileConfiguration cfg = getFileConfig(fileName);
		if (!cfg.contains(path)) throw new InvalidConfiguration("Could not get configuration integer \"" + path + "\" in file \"" + fileName + ".yml\"");
		ExamplePlugin.getPlugin().getLogger().config(path + ": " + cfg.getInt(path));
		return cfg.getInt(path);
	}

	public double getDouble(String fileName, String path) throws InvalidConfiguration {
		FileConfiguration cfg = getFileConfig(fileName);
		if (!cfg.contains(path)) throw new InvalidConfiguration("Could not get configuration double \"" + path + "\" in file \"" + fileName + ".yml\"");
		ExamplePlugin.getPlugin().getLogger().config(path + ": " + cfg.getDouble(path));
		return cfg.getDouble(path);
	}

	public boolean getBoolean(String fileName, String path) throws InvalidConfiguration {
		FileConfiguration cfg = getFileConfig(fileName);
		ExamplePlugin.getPlugin().getLogger().info(cfg.getCurrentPath());
		if (!cfg.contains(path)) throw new InvalidConfiguration("Could not get configuration boolean \"" + path + "\" in file \"" + fileName + ".yml\"");
		ExamplePlugin.getPlugin().getLogger().config(path + ": " + cfg.getBoolean(path));
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
