package ua.rozipp.abstractplugin.command;

import org.bukkit.command.CommandSender;

public abstract class AbstractValidator {
	public abstract void isValid(CommandSender sender) throws CustomCommandValidException;
}
