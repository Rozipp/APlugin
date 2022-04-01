package ua.rozipp.example.main;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ua.rozipp.abstractplugin.command.ACommander;
import ua.rozipp.abstractplugin.ASettingMaster;
import ua.rozipp.abstractplugin.ALocalizerMaster;
import ua.rozipp.abstractplugin.AMessenger;
import ua.rozipp.abstractplugin.APlugin;
import ua.rozipp.abstractplugin.ATaskMaster;
import ua.rozipp.example.remotesession.RemoteSession;
import ua.rozipp.example.remotesession.TickHandler;

import java.util.Set;
import java.util.logging.Logger;

/**
* Клас для статического обращения к объектам плагина
*/
@Getter
public class ExampleStatic {

	private static APlugin plugin;
	private static ATaskMaster taskMaster;
	private static ASettingMaster setting;
	private static ALocalizerMaster localizer;
	@Getter
	private static AMessenger messenger;
	private static ACommander commander;

	public static Logger getLogger() {
		return plugin.getLogger();
	}

	public static Server getServer() {
		return plugin.getServer();
	}

	public static void init(APlugin plugin) {
		ExampleStatic.plugin = plugin;
		ExampleStatic.taskMaster = plugin.getTaskMaster();
		ExampleStatic.setting = plugin.getSetting();
		ExampleStatic.localizer = plugin.getLocalizer();
		ExampleStatic.messenger = plugin.getMessenger();
		ExampleStatic.commander = plugin.getCommander();
	}

	/**
	 * called when a new session is established.
	 */
	public static void handleConnection(RemoteSession newSession) {
		if (checkBanned(newSession)) {
			getLogger().warning("Kicking " + newSession.getSocket().getRemoteSocketAddress() + " because the IP address has been banned.");
			newSession.kick("You've been banned from this server!");
			return;
		}
		synchronized (TickHandler.sessions) {
			TickHandler.sessions.add(newSession);
		}
	}

	public static boolean checkBanned(RemoteSession session) {
		Set<String> ipBans = getServer().getIPBans();
		String sessionIp = session.getSocket().getInetAddress().getHostAddress();
		return ipBans.contains(sessionIp);
	}


	public static Player getNamedPlayer(String name) {
		if (name == null) return null;
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (name.equals(player.getPlayerListName())) {
				return player;
			}
		}
		return null;
	}

	public static Player getHostPlayer() {
		if (ExampleData.hostPlayer != null) return ExampleData.hostPlayer;
		for(Player player : Bukkit.getOnlinePlayers()) {
			return player;
		}
		return null;
	}

	//get entity by id - DONE to be compatible with the pi it should be changed to return an entity not a player...
	public static Entity getEntity(int id) {
		for (Player p : getServer().getOnlinePlayers()) {
			if (p.getEntityId() == id) {
				return p;
			}
		}
		//check all entities in host player's world
		Player player = getHostPlayer();
		World w = player.getWorld();
		for (Entity e : w.getEntities()) {
			if (e.getEntityId() == id) {
				return e;
			}
		}
		return null;
	}
}
