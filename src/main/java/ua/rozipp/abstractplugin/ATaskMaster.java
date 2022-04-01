package ua.rozipp.abstractplugin;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Мастер огранизации синхронних и асинхронних задач и таймеров
 */
public class ATaskMaster {

	private JavaPlugin plugin = null;
	private Server server = null;
	/** Список именованних асинхронных потоков */
	private HashMap<String, BukkitTask> tasks = new HashMap<String, BukkitTask>();
	/** Список именованних таймеров */
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

	/**
	 * Немеденний запуск синхронного потока
	 * @param runnable - поток
	 */
	public int syncTask(Runnable runnable) {
		return syncTask(runnable, 0);
	}

	/**
	 * Запуск синхронного потока
	 * @param runnable - поток
	 * @param delay - задержка запуска
	 */
	public int syncTask(Runnable runnable, long delay) {
		return getServer().getScheduler().scheduleSyncDelayedTask(plugin, runnable, delay);
	}

	/**
	 * Запуск именнованого потока с задержкой
	 * @param name - имя потока
	 * @param runnable - поток
	 * @param delay - задержка запуска
	 */
	public void asyncTask(String name, Runnable runnable, long delay) {
		if (getTask(name) != null) getTask(name).cancel();
		addTask(name, getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay));
	}

	/**
	 * Запуск неименованного (временного) потока
	 * @param runnable - поток
	 * @param delay - задержка запуска
	 */
	public void asyncTask(Runnable runnable, long delay) {
		getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
	}

	/**
	 * Schedules a repeating task.
	 * <p>
	 * This task will be executed by the main server thread.
	 *
	 * @param name Name of the task
	 * @param task Task to be executed
	 * @param delay Delay in server ticks before executing first repeat
	 * @param period Period in server ticks of the task
	 * @return Task id number (-1 if scheduling failed)
	 */
	public void syncTimer(String name, Runnable task, long delay, long period) {
		if (getTimer(name) != null) getTimer(name).cancel();
			getServer().getScheduler().scheduleSyncRepeatingTask(plugin, task, delay, period);
	}

	/**
	 * Schedules a repeating task.
	 * <p>
	 * This task will be executed by the main server thread.
	 * Delay in server ticks before executing first repeat = 0
	 *
	 * @param name Name of the task
	 * @param task Task to be executed
	 * @param period Period in server ticks of the task
	 * @return Task id number (-1 if scheduling failed)
	 */
	public void syncTimer(String name, Runnable task, long period) {
		syncTimer(name, task, 0, period);
	}

	/**
	 * <b>Asynchronous tasks should never access any API in Bukkit. Great care
	 * should be taken to assure the thread-safety of asynchronous tasks.</b>
	 * <p>
	 * Returns a task that will repeatedly run asynchronously until cancelled,
	 * starting after the specified number of server ticks.
	 *
	 * @param name Name of the task
	 * @param task the task to be run
	 * @param delay the ticks to wait before running the task for the first time
	 * @param period the ticks to wait between runs
	 * @return a BukkitTask that contains the id number
	 * @throws IllegalArgumentException if plugin is null
	 * @throws IllegalArgumentException if task is null
	 */
	public void asyncTimer(String name, Runnable task, long delay, long period) {
		if (getTimer(name) != null) getTimer(name).cancel();
		addTimer(name, getServer().getScheduler().runTaskTimerAsynchronously(plugin, task, delay, period));
	}

	/**
	 * <b>Asynchronous tasks should never access any API in Bukkit. Great care
	 * should be taken to assure the thread-safety of asynchronous tasks.</b>
	 * <p>
	 * Returns a task that will repeatedly run asynchronously until cancelled,
	 * starting after the specified number of server ticks.
	 *
	 * @param name Name of the task
	 * @param task the task to be run
	 * @param period the ticks to wait between runs
	 * @return a BukkitTask that contains the id number
	 * @throws IllegalArgumentException if plugin is null
	 * @throws IllegalArgumentException if task is null
	 */
	public void asyncTimer(String name, Runnable task, long period) {
		asyncTimer(name, task, 0, period);
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

	public JavaPlugin getPlugin() {
		return this.plugin;
	}
}
