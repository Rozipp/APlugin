package ua.rozipp.example.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import ua.rozipp.abstractplugin.command.AbstractValidator;
import ua.rozipp.abstractplugin.exception.InvalidPermissionException;
import ua.rozipp.abstractplugin.exception.AException;
import ua.rozipp.abstractplugin.APlugin;

/**
 * <p>
 * Ххранит в себе статические Validator-ы
 * </p>
 * @author rozipp */
public class Validators {

	/** Разрешает выполнение админских команд связынных.*/
	public static AbstractValidator validAdmin = new AbstractValidator() {
		@Override
		public void isValid(CommandSender sender) throws InvalidPermissionException {
			if (!(sender instanceof ConsoleCommandSender)) {
				try {
					Player player = APlugin.getPlugin().getCommander().getPlayer(sender);
					if (!player.isOp()) throw new InvalidPermissionException(APlugin.getPlugin().getLocalizer().getString(sender, "cmd_MustBeOP"));
				} catch (AException e) {
					throw new InvalidPermissionException("CommandSender not found");
				}
			}
		}
	};

}
