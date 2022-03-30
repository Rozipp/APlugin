package ua.rozipp.abstractplugin.command;

import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ua.rozipp.abstractplugin.main.*;

import java.util.HashMap;

/**
 * <p>
 * Главный Модуль управления командами.
 * </p>
 * <p>
 * Регистрирует команды по новому, а так же по старому.
 * Хранит в себе статические команды:
 * <li> для работы с исполнителем команды (sender)
 * <li> для работы с аргументами (args)
 * </p>
 * @author rozipp */
public class ACommander {

	private int count = 0;
	@Getter
	private APlugin plugin;
	@Getter
	private ALocalize localize;
	@Getter
	private AMessenger messenger;

	public ACommander(APlugin plugin) {
		this.plugin = plugin;
		messenger = plugin.getMessenger();
	}

	// ----------------- arg utils

	public String convertCommand(String comm) {
		String rus = "фисвуапршолдьтщзйкыегмцчня";
		String eng = "abcdefghijklmnopqrstuvwxyz";

		String res = comm;
		for (int i = 0; i < rus.length(); i++)
			res = res.replace(rus.charAt(i), eng.charAt(i));
		return res;
	}

	public String[] stripArgs(String[] someArgs, int amount) {
		if (amount >= someArgs.length) return new String[0];
		String[] argsLeft = new String[someArgs.length - amount];
		for (int i = 0; i < argsLeft.length; i++) {
			argsLeft[i] = someArgs[i + amount];
		}
		return argsLeft;
	}

	public String combineArgs(String[] someArgs) {
		String combined = "";
		for (String str : someArgs) {
			combined += str + " ";
		}
		return combined.trim();
	}

	public String makeInfoString(HashMap<String, String> kvs, String lowColor, String highColor) {
		String out = "";
		for (String key : kvs.keySet()) {
			out += lowColor + key + ": " + highColor + kvs.get(key) + " ";
		}
		return out;
	}

	// -------------- CommandSender

	public Player getPlayer(CommandSender sender) throws AException {
		if (sender instanceof Player) return (Player) sender;
		throw new AException(APlugin.getPlugin().getLocalize().getString(sender, "cmd_MustBePlayer"));
	}

	// --------------------- get args

	public Integer getNamedInteger(String[] args, int index) throws AException {
		if (args.length < (index + 1)) throw new AException(APlugin.getPlugin().getLocalize().getString("", "cmd_enterNumber"));
		try {
			return Integer.valueOf(args[index]);
		} catch (NumberFormatException e) {
			throw new AException(args[index] + " " + APlugin.getPlugin().getLocalize().getString("", "cmd_enterNumerError2"));
		}
	}

	public Double getNamedDouble(String[] args, int index) throws AException {
		if (args.length < (index + 1)) throw new AException(APlugin.getPlugin().getLocalize().getString("", "cmd_enterNumber"));
		try {
			return Double.valueOf(args[index]);
		} catch (NumberFormatException e) {
			throw new AException(args[index] + " " + APlugin.getPlugin().getLocalize().getString("", "cmd_enterNumerError"));
		}
	}

	public String getNamedString(String[] args, int index, String message) throws AException {
		if (args.length < (index + 1)) throw new AException(message);
		return args[index];
	}

}
