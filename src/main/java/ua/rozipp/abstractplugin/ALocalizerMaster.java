package ua.rozipp.abstractplugin;

/*
 * This plugin needs a default_lang.yml file in the jar file. This file includes the default strings.
 */

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class ALocalizerMaster {
	private static String FOLDERNAME = "localization/";
	private APlugin plugin;
	public List<String> localizeNames = new ArrayList<>();
	private String serverLanguage;
	private Map<String, FileConfiguration> languageFiles = new HashMap<>();

	public ALocalizerMaster(APlugin plugin) {
		this.plugin = plugin;
		serverLanguage = plugin.getSetting().getStringOrDefault("", "server_language", "en_us").toLowerCase();
		try {
			loadLanguageFiles();
		} catch (Exception e) {
			plugin.getLogger().severe(e.getMessage());
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
			localString = plugin.getLocalizer().getLanguageFile(language).getString(pathToString);
		} catch (Exception e1) {
			localString = pathToString;
		}
		return compoundedArgs(localString, args);
	}

	private String compoundedArgs(String string, Object... args) {
		if (args.length == 0) return string;
		try {
			for (int arg = 0; arg < args.length; ++arg) {
				string = string.replace("%" + arg, args[arg].toString());
			}
			return string;
		} catch (IllegalFormatException e1) {
			return string + " - [" + getString("", "string_formatting_error") + "]";
		}
	}

	private void loadLanguageFiles() throws Exception {
		File folder = new File(plugin.getDataFolder(), FOLDERNAME);
		if (!folder.exists()) {
			try {
				plugin.saveResource(FOLDERNAME + serverLanguage + ".yml", true);
				plugin.getLogger().severe("Language file: " + FOLDERNAME + serverLanguage + ".yml" + " was missing. Streaming to disk from Jar.");
			} catch (Exception e1) {
				e1.printStackTrace();
				plugin.saveResource(FOLDERNAME + "en_us" + ".yml", true);
			}
		}

		for (String fileName : folder.list())
			localizeNames.add(fileName.substring(0, fileName.length() - 4).toLowerCase());
		for (String fileName : localizeNames)
			try {
				languageFiles.put(fileName, plugin.getSetting().loadFileConfig(FOLDERNAME + fileName + ".yml", false));
			} catch (Exception e) {
				plugin.getLogger().severe("Language pack (" + FOLDERNAME + fileName + ".yml" + ") not loaded.");
				e.printStackTrace();
			}

		if (!languageFiles.containsKey(serverLanguage))
			try {
				languageFiles.put(serverLanguage, plugin.getSetting().loadFileConfig(FOLDERNAME + serverLanguage + ".yml", true));
			} catch (Exception e) {
				plugin.getLogger().severe("Server language pack (" + serverLanguage + ") not found. Server language reset to \"en_us\"");
				serverLanguage = "en_us";
				if (!languageFiles.containsKey(serverLanguage))
					languageFiles.put(serverLanguage, plugin.getSetting().loadFileConfig(FOLDERNAME + serverLanguage + ".yml", true));
			}
	}

	private FileConfiguration getLanguageFile(String language) {
		if (language == null || language.isEmpty() || !languageFiles.containsKey(language.toLowerCase()))
			return languageFiles.get(serverLanguage);
		return languageFiles.get(language.toLowerCase());
	}

}