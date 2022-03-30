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
package ua.rozipp.example.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ua.rozipp.abstractplugin.command.CustomCommand;
import ua.rozipp.abstractplugin.main.ALocalize;
import ua.rozipp.abstractplugin.main.AMessenger;
import ua.rozipp.abstractplugin.main.AException;

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
