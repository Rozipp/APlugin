package ua.rozipp.abstractplugin.main;

/*
 * This plugin needs a default_lang.yml file in the jar file. This file includes the default strings.
 */

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ua.rozipp.abstractplugin.config.SettingMaster;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ALocalize {
	private static String FOLDERNAME = "localization/";
	public List<String> localizeNames;

	public String languageFile;
	private APlugin plugin;
	private SettingMaster SETTING;

	private FileConfiguration serverLanguageFile;
	private Map<String, FileConfiguration> languageFiles = new HashMap<>();

	public ALocalize(APlugin plugin) {
		this.plugin = plugin;
		this.SETTING = plugin.getSetting();
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public String getString(CommandSender sender, String pathToString, Object... args) {
		if (sender instanceof Player)
			return getString(((Player) sender).getLocale(), pathToString, args);
		return getString("", pathToString, args);
	}

	public String getString(String language, String pathToString, Object... args) {
		String localString = "";
		try {
			localString = plugin.getLocalize().getLanguageFile(language).getString(pathToString);
		} catch (Exception e1) {
			try {
				localString = plugin.getLocalize().serverLanguageFile.getString(pathToString);
			} catch (Exception e2) {
				localString = pathToString;
			}
		}
		if (args.length >= 1) {
			return compounded(localString, args);
		}
		return localString;
	}

	private String compounded(String string, Object... args) {
		try {
			for (int arg = 0; arg < args.length; ++arg) {
				string = string.replace("[%" + arg + "]", "" + args[arg]);
			}
			return string;
		} catch (IllegalFormatException e1) {
			return string + " - [" + getString("", "stringFormattingError") + "]";
		}
	}

	public void init() throws IOException, InvalidConfigurationException {
		File folder = new File(plugin.getDataFolder(), FOLDERNAME);
		if (!folder.exists()) {
			plugin.saveResource("localization/en_us.yml", true);
			plugin.saveResource("localization/ru_ru.yml", true);
		}
		localizeNames = new ArrayList<>();
		for (String fileName : folder.list()) {
			plugin.getLogger().info(fileName);
			localizeNames.add(fileName.substring(0, fileName.length() - 4).toLowerCase());
		}
		String serverLanguageFileName = SETTING.getStringOrDefault("", "localization_file", "en_us");
		serverLanguageFile = SETTING.loadFileConfig(FOLDERNAME + serverLanguageFileName + ".yml", false);
		for (String fileName : localizeNames) {
			languageFiles.put(fileName, SETTING.loadFileConfig(FOLDERNAME + fileName + ".yml", false));
		}
	}

	public FileConfiguration getLanguageFile(String language) {
		if (language == null || language.isEmpty()) return serverLanguageFile;
		return languageFiles.get(language.toLowerCase());
	}

}