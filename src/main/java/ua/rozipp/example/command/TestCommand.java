package ua.rozipp.example.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ua.rozipp.abstractplugin.command.CustomCommand;
import ua.rozipp.abstractplugin.command.CustomExecutor;
import ua.rozipp.abstractplugin.exception.AException;

public class TestCommand extends CustomCommand {

	public TestCommand(String perentComman) {
		super(perentComman);
		this.setAliases("RJM");
		this.setExecutor(new CustomExecutor() {
			@Override
			public void run(CommandSender sender, Command cmd, String label, String[] args) throws AException {
//				Message.sendMessage(sender, RJM.localize.getString("cmd_acceptError"));
				getMessenger().sendMessageString(sender, getLocalize().getString(sender, "civ_currencyName"));
				getMessenger().sendMessageString(sender, getLocalize().getString("ru_Ru", "MobSpawner_Heading"));
				getMessenger().sendMessageString(sender, getLocalize().getString("en_US", "Finished"));
				getMessenger().sendMessageString(sender, getLocalize().getString(sender, "Repaired"));
			}
		});
	}
}
