/*************************************************************************
 * 
 * AVRGAMING LLC
 * __________________
 * 
 *  [2013] AVRGAMING LLC
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of AVRGAMING LLC and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to AVRGAMING LLC
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from AVRGAMING LLC.
 */
package ua.rozipp.abstractplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class AMessenger {

	/* Stores the player name and the hash code of the last message sent to prevent error spamming the player. */
	private HashMap<String, Integer> lastMessageHashCode = new HashMap<String, Integer>();
	private final APlugin plugin;
	private final ALocalizerMaster localize;

	public AMessenger(APlugin plugin) {
		this.plugin = plugin;
		this.localize = plugin.getLocalizer();
	}

	public void sendErrorNoRepeat(Object sender, String line) {
		if (sender instanceof Player) {
			Player player = (Player) sender;

			Integer hashcode = lastMessageHashCode.get(player.getName());
			if (hashcode != null && hashcode == line.hashCode())
				return;
			lastMessageHashCode.put(player.getName(), line.hashCode());
		}

		sendToSender(getSender(sender), AColor.Rose + line);
	}

	public void sendTitle(Object sender, int fadeIn, int show, int fadeOut, String title, String subTitle) {
		CommandSender cs = getSender(sender);
		sendToSender(cs, title);
		if (subTitle != "") {
			sendToSender(cs, subTitle);
		}
	}

	public void sendTitle(Object sender, String title, String subTitle) {
		sendTitle(getSender(sender), 10, 40, 5, title, subTitle);
	}

	private CommandSender getSender(Object sender){
		CommandSender cs = null;
		if (sender instanceof CommandSender) {
			cs = ((CommandSender) sender);
		}
//			else
//				if (sender instanceof Resident) {
//					try {
//						cs = CivGlobal.getPlayer(((Resident) sender));
//					} catch (CivException e) {
//						// No player online
//					}
//				}
		return cs;
	}

	private void sendToSender(CommandSender sender, String... lines) {
		if (sender != null)
			for (String s : lines)
				sender.sendMessage(s);
	}

	public void sendMessageLocalized(CommandSender sender, String pathToString, Object... args) {
		sendToSender(sender, localize.getString(sender, pathToString, args));
	}

	public void sendMessageString(Object sender, String... lines) {
		sendToSender(getSender(sender), lines);
	}

	public void sendMessageList(Object sender, List<String> words) {
		sendToSender(getSender(sender), String.join(", ", words));
	}

	public void sendErrorLocalized(CommandSender sender, String pathToString, Object... args) {
		sendToSender(sender, AColor.Rose + localize.getString(sender, pathToString, args));
	}

	public void sendErrorString(Object sender, String line) {
		sendToSender(getSender(sender), AColor.Rose + line);
	}

	public void sendSubHeading(Object sender, String title) {
		sendToSender(getSender(sender), buildSmallTitle(title));
	}

	public void sendHeading(Object sender, String title) {
		sendToSender(getSender(sender), buildTitle(title));
	}

	public void sendSuccess(CommandSender sender, String message) {
		sendToSender(sender, AColor.LightGreen + message);
	}

	public String buildTitle(String title) {
		String line = "-------------------------------------------------";
		String titleBracket = "[ " + AColor.Yellow + title + AColor.LightBlue + " ]";

		if (titleBracket.length() > line.length()) {
			return AColor.LightBlue + "-" + titleBracket + "-";
		}

		int min = (line.length() / 2) - titleBracket.length() / 2;
		int max = (line.length() / 2) + titleBracket.length() / 2;

		String out = AColor.LightBlue + line.substring(0, Math.max(0, min));
		out += titleBracket + line.substring(max);

		return out;
	}

	public String buildSmallTitle(String title) {
		String line = AColor.LightBlue + "------------------------------";

		String titleBracket = "[ " + title + " ]";

		int min = (line.length() / 2) - titleBracket.length() / 2;
		int max = (line.length() / 2) + titleBracket.length() / 2;

		String out = AColor.LightBlue + line.substring(0, Math.max(0, min));
		out += titleBracket + line.substring(max);

		return out;
	}

	// ---------------------------- ToAllPlayers
	public void sendGlobalMessage(String string) {
		plugin.getLogger().info("[Global] " + string);
		Iterator<? extends Player> iter = Bukkit.getOnlinePlayers().iterator();
		while (iter.hasNext()) {
			Player player = iter.next();
			sendToSender(player, AColor.LightBlue + localize.getString(player, "civMsg_Globalprefix") + " " + AColor.White + string);
		}
	}

	public void sendToAllPlayersTitle(String title, String subTitle) {
		plugin.getLogger().info("[GlobalTitle] " + title + " - " + subTitle);
		String msg = buildTitle(title);
		sendToAllPlayers(msg);
		if (!subTitle.equals("")) sendToAllPlayers(subTitle);
	}

	public void sendToAllPlayersHeading(String head) {
		plugin.getLogger().info("[GlobalHeading] " + head);
		sendToAllPlayers(head);
	}

	public void sendToAllPlayers(String... lines) {
		Iterator<? extends Player> iter = Bukkit.getOnlinePlayers().iterator();
		while (iter.hasNext()) {
			Player player = iter.next();
			for (String s : lines)
				sendToSender(player, s);
		}
	}

//	public String pasteStackTrace(Player cause, Exception e) {
//		CivGlobal.dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
//		StringBuilder permissions = new StringBuilder("Пермы: ");
//		Iterator<PermissionAttachmentInfo> var3 = cause.getEffectivePermissions().iterator();
//
//		while (var3.hasNext()) {
//			PermissionAttachmentInfo permission = (PermissionAttachmentInfo) var3.next();
//			permissions.append(permission.getPermission()).append("\n");
//		}
//
//		String contents = "Игрок: " + cause.getName() + "\nВремя: " + CivGlobal.dateFormat.format(new Date()) + "\nСервер: " + Bukkit.getServerName() + "\nКарта: " + CivGlobal.getDynmapLink(Bukkit.getServerName()) + "\nЦивилизация: "
//				+ CivGlobal.getResident(cause).getCivName() + "\nГород: " + CivGlobal.getResident(cause).getTownName() + "\nДеревня: " + CivGlobal.getResident(cause).getCampName() + "\n" + permissions
//				+ "================================= Страктрейс ===================================\n\n" + ExceptionUtils.getStackTrace(e) + "\n=====================================================================\nПричина: "
//				+ ExceptionUtils.getRootCauseMessage(e.getCause()) + "\n================================= Полный страктрейс причины ===================================\n\n" + ExceptionUtils.getFullStackTrace(e.getCause()) + "\n";
//		String url = CivLog.paste(contents, "exc", "", "exc");
//		Iterator<? extends Player> var5 = Bukkit.getOnlinePlayers().iterator();
//
//		while (var5.hasNext()) {
//			Player admin = (Player) var5.next();
//			if (admin.isOp()) {
//				sendMessage((Object) admin, (String) ("§c" + RJMColor.UNDERLINE + "Новый Exception: " + url));
//			}
//		}
//
//		url = url.replace(".exc", "");
//		return url.replace("https://www.dropbox.com/home/HNM", "");
//	}

	public String plurals(final int count, final String... pluralForms) {
		final int i = (count % 10 == 1 && count % 100 != 11) ? 0 : ((count % 10 >= 2 && count % 10 <= 4 && (count % 100 < 10 || count % 100 >= 20)) ? 1 : 2);
		return pluralForms[i];
	}

	/** в строку string добавляет строку addString, если длина строки addString менше чем length, то добавляет пробелы */
	public static String addTabToString(String string, String addString, Integer length) {
		string += addString;
		for (Integer i = addString.length(); i <= length; i++) {
			string += " ";
		}
		return string;
	}
}
