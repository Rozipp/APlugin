package ua.rozipp.abstractplugin.threading;

import lombok.Getter;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class ATaskMaster {

	@Getter
	private JavaPlugin plugin = null;
	private Server server = null;
	//    AsyncList
	private HashMap<String, BukkitTask> tasks = new HashMap<String, BukkitTask>();
	private HashMap<String, BukkitTask> timers = new HashMap<String, BukkitTask>();

	public ATaskMaster(JavaPlugin plugin) {
		this.plugin = plugin;
		this.server = plugin.getServer();
	}

	public List<World> getWorlds() {
		return getServer().getWorlds();
	}

	public World getWorld(String name) {
		return getServer().getWorld(name);
	}

	public Server getServer() {
		synchronized (server) {
			return server;
		}
	}

	//   syncTask
	public int syncTask(Runnable runnable) {
		return syncTask(runnable, 0);
	}

	public int syncTask(Runnable runnable, long delay) {
		return getServer().getScheduler().scheduleSyncDelayedTask(plugin, runnable, delay);
	}

	//    asyncTask
	public void asyncTask(String name, Runnable runnable, long delay) {
		if (getTask(name) != null) getTask(name).cancel();
		addTask(name, getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay));
	}

	public void asyncTask(Runnable runnable, long delay) {
		getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
	}

	//   syncTimer
	public void syncTimer(String name, Runnable runnable, long time) {
		syncTimer(name, runnable, time, time);
	}

	public void syncTimer(String name, Runnable runnable, long delay, long repeat) {
		if (getTimer(name) != null) getTimer(name).cancel();
			getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, delay, repeat);
	}

	//   asyncTimer
	public void asyncTimer(String name, Runnable runnable, long delay, long repeat) {
		if (getTimer(name) != null) getTimer(name).cancel();
		addTimer(name, getServer().getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, repeat));
	}

	public void asyncTimer(String name, Runnable runnable, long time) {
		asyncTimer(name, runnable, time, time);
	}

	private void addTask(String name, BukkitTask task) {
		tasks.put(name, task);
	}

	private void addTimer(String name, BukkitTask timer) {
		timers.put(name, timer);
	}

	public void stopAll() {
		int taskCount = stopAllTasks();
		int timerCount = stopAllTimers();
		getServer().getScheduler().cancelTasks(plugin);
		plugin.getLogger().fine("Stopped " + taskCount + " task.  Stopped " + timerCount + " timers");
	}

	public int stopAllTasks() {
		int count = 0;
		synchronized (tasks) {
			Iterator<String> iter = tasks.keySet().iterator();
			while (iter.hasNext()) {
				String name = iter.next();
				tasks.get(name).cancel();
				getPlugin().getLogger().finest("Task stopped: " + name);
				count++;
			}
			tasks.clear();
		}
		return count;
	}

	public int stopAllTimers() {
		int count = 0;
		synchronized (timers) {
			Iterator<String> iter = timers.keySet().iterator();
			while (iter.hasNext()) {
				String name = iter.next();
				timers.get(name).cancel();
				getPlugin().getLogger().finest("Timer stopped: " + name);
				count++;
			}
			timers.clear();
		}
		return count;
	}

	public void cancelTask(String name) {
		synchronized (tasks) {
			BukkitTask task = getTask(name);
			if (task != null) {
				task.cancel();
				getPlugin().getLogger().finest("Task stopped: " + name);
				tasks.remove(name);
			}
		}
	}

	public void cancelTimer(String name) {
		synchronized (timers) {
			BukkitTask timer = getTimer(name);
			if (timer != null) {
				timer.cancel();
				getPlugin().getLogger().finest("Timer stopped: " + name);
				timers.remove(name);
			}
		}
	}

	public BukkitTask getTask(String name) {
		return tasks.get(name);
	}

	public BukkitTask getTimer(String name) {
		return timers.get(name);
	}

	public boolean hasTask(String key) {
		BukkitTask task = tasks.get(key);
		if (task == null) return false;
		if (getServer().getScheduler().isCurrentlyRunning(task.getTaskId()) ||
				getServer().getScheduler().isQueued(task.getTaskId())) {
			return true;
		}
		tasks.remove(key);
		return false;
	}

	public Set<String> getTasksList() {
		return tasks.keySet();
	}

	public Set<String> getTimersList() {
		return timers.keySet();
	}
}
