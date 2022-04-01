package ua.rozipp.abstractplugin;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import ua.rozipp.abstractplugin.command.ACommander;

@Getter
public class APlugin extends JavaPlugin {

	private static APlugin plugin;
	private ATaskMaster taskMaster;
	private ASettingMaster setting;
	private ALocalizerMaster localizer;
	private AMessenger messenger;
	private ACommander commander;
	private AListenerMaster listenerMaster;

	public static APlugin getPlugin() {
		return plugin;
	}

	/**
	 * Инициализация всех обёэктов плагина:
	 *
	 * <li>{@link ATaskMaster} - мастер огранизации синхронних и асинхронних задач и таймеров,
	 * <li>{@link ASettingMaster} - мастер загрузки файлов настроек /data/*.yml,
	 * <li>{@link ALocalizerMaster} - мастер загрузки и использования файлов локализаций /localize/*.yml,
	 * <li>{@link AMessenger} - мастер формирования сообщений игроку, серверу и глобальные сообщения с учетом локализации,
	 * <li>{@link ACommander} - мастер регистрации команд консоли,
	 * <li>{@link AListenerMaster} - Помощник регистрации Listener-ов плагина
	 */
	@Override
	public void onEnable() {
		plugin = this;
		taskMaster = new ATaskMaster(this);
		setting = new ASettingMaster(this);
		localizer = new ALocalizerMaster(this);
		messenger = new AMessenger(this);
		commander = new ACommander(this);
		listenerMaster = new AListenerMaster(this);
	}

	@Override
	public void onDisable() {
		getTaskMaster().stopAll();
		getListenerMaster().unregisterAllListener();
	}

}
