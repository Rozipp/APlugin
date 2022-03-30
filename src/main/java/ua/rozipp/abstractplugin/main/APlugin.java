package ua.rozipp.abstractplugin.main;

import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ua.rozipp.abstractplugin.command.ACommander;
import ua.rozipp.abstractplugin.config.SettingMaster;
import ua.rozipp.abstractplugin.threading.ATaskMaster;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

@Getter
public class APlugin extends JavaPlugin {

	private static APlugin plugin;
	private ATaskMaster taskMaster;
	private SettingMaster setting;
	private ALocalize localize;
	private AMessenger messenger;
	private ACommander commander;

	private List<Listener> listeners = new ArrayList<>();

	public static APlugin getPlugin() {
		return plugin;
	}

	public void init(){
		plugin = this;
		taskMaster = new ATaskMaster(this);
		setting = new SettingMaster(this);
		getLogger().setLevel(Level.parse(setting.getStringOrDefault("","loglevel", "CONFIG")));
		localize = new ALocalize(this);
		messenger = new AMessenger(this);
		commander = new ACommander(this);
	}

	@Override
	public void onDisable() {
		getTaskMaster().stopAll();
		unregisterAllListener();
	}

	// --------------------- Listener

	public int getCountListener(){
		return listeners.size();
	}

	private void unregisterAllListener() {
		Iterator<Listener> iter = listeners.iterator();
		while (iter.hasNext())
			unregisterListener(iter.next());
	}

	public void registerListener(Listener listener) {
		this.getServer().getPluginManager().registerEvents(listener, plugin);
		listeners.add(listener);
	}

	public void unregisterListener(Listener listener) {
		HandlerList.unregisterAll(listener);
		listeners.remove(listener);
	}

	public boolean hasPlugin(String name) {
		Plugin p;
		p = getServer().getPluginManager().getPlugin(name);
		return (p != null);
	}


}
