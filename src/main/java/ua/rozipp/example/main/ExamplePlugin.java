package ua.rozipp.example.main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import ua.rozipp.abstractplugin.command.CustomCommand;
import ua.rozipp.abstractplugin.command.CustomExecutor;
import ua.rozipp.abstractplugin.AColor;
import ua.rozipp.abstractplugin.exception.AException;
import ua.rozipp.abstractplugin.APlugin;
import ua.rozipp.example.command.TestCommand;
import ua.rozipp.example.listener.DisableXPListener;
import ua.rozipp.example.listener.ExampleListener;
import ua.rozipp.example.remotesession.ServerListenerThread;
import ua.rozipp.example.remotesession.TickHandler;

import java.io.IOException;
import java.util.ArrayList;

public class ExamplePlugin extends APlugin {

	public static ServerListenerThread serverThread;

	@Override
	public void onEnable() {
		getLogger().info("----------------------- ExamplePlugin Started -----------------------");
//		this.saveResource("localization/en_us.yml", true);
//		this.saveResource("localization/ru_ru.yml", true);
		super.onEnable();

		try {
			getSetting().loadConfig("civ");
			getSetting().loadConfig("town");
		} catch (IOException | InvalidConfigurationException e) {
			getLogger().severe(e.getMessage());
			e.printStackTrace();
			return;
		}
//		//create new tcp listener thread
//		try {
//			serverThread = ServerListenerThread.startServerListenerThread(this, getSetting().hostname, getSetting().port);
//		} catch (RJMException e) {
//			e.printStackTrace();
//			getLogger().error(e.getMessage());
//			return;
//		}

		//register commands
		initCommands();
		//register the events
		registerAllListeners();

		//setup session array
		TickHandler.sessions = new ArrayList<>();
		//setup the schedule to called the tick handler
		getTaskMaster().syncTimer(TickHandler.class.getName(), new TickHandler(), 1);
	}

	@Override
	public void onDisable() {
		super.onDisable();

//		try {
//			serverThread.running = false;
//			serverThread.serverSocket.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		TickHandler.sessions = null;
//		serverThread = null;
		getLogger().info("-------------------------- ExamplePlugin Stopped -----------------------");
	}

	public void registerAllListeners() {
		this.getListenerMaster().registerListener(new ExampleListener());

		if (getSetting().getBooleanOrDefault("civ", "global.use_exp_as_currency", false))
			this.getListenerMaster().registerListener(new DisableXPListener());

		getLogger().config("Registred " + this.getListenerMaster().getCountListener() + " listeners");
	}

	public void initCommands() {
		// Init commands
		getCommander().register(new TestCommand("test"));
		getCommander().register(new CustomCommand("kill").withExecutor(new CustomExecutor() {
			@Override
			public void run(CommandSender sender, Command cmd, String label, String[] args) throws AException {
				getCommander().getPlayer(sender).setHealth(0);
				getMessenger().sendMessageString(sender, AColor.Yellow + AColor.BOLD + getLocalizer().getString(sender, "cmd_kill_Mesage"));
			}
		}));

		getLogger().info("registred " + getCommander().getCount() + " CustomCommands");
	}

}
