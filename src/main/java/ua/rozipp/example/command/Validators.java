package ua.rozipp.example.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import ua.rozipp.abstractplugin.command.AbstractValidator;
import ua.rozipp.abstractplugin.command.CustomCommandValidException;
import ua.rozipp.abstractplugin.main.AException;
import ua.rozipp.abstractplugin.main.APlugin;

/**
 * <p>
 * Ххранит в себе статические Validator-ы
 * </p>
 * @author rozipp */
public class Validators {

	/** Разрешает выполнение админских команд связынных.*/
	public static AbstractValidator validAdmin = new AbstractValidator() {
		@Override
		public void isValid(CommandSender sender) throws CustomCommandValidException {
			if (!(sender instanceof ConsoleCommandSender)) {
				try {
					Player player = APlugin.getPlugin().getCommander().getPlayer(sender);
					if (!player.isOp()) throw new CustomCommandValidException(APlugin.getPlugin().getLocalize().getString(sender, "cmd_MustBeOP"));
				} catch (AException e) {
					throw new CustomCommandValidException("CommandSender not found");
				}
			}
		}
	};

}
