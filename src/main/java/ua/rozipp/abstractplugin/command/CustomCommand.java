package ua.rozipp.abstractplugin.command;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ua.rozipp.abstractplugin.command.taber.AbstractTaber;
import ua.rozipp.abstractplugin.exception.AException;
import ua.rozipp.abstractplugin.exception.InvalidPermissionException;
import ua.rozipp.abstractplugin.ALocalizerMaster;
import ua.rozipp.abstractplugin.AMessenger;
import ua.rozipp.abstractplugin.APlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Общий клас для команд. После создания команды,добавление параметров возможно как через сеттеры (set...()), так через билдеры (with..())
 * </p>
 *
 * @author rozipp
 */
@Getter
public class CustomCommand {

	private final APlugin plugin;
	private final ALocalizerMaster localize;
	private final AMessenger messenger;
	private final String commandString;

	@Setter
	private String description;
	@Setter
	private List<String> aliases = null;
	@Setter
	private String usage = null;
	@Setter
	private String permission = null;
	@Setter
	private String permissionMessage = null;
	@Setter
	private List<AbstractValidator> validators = new ArrayList<>();
	@Setter
	private CustomExecutor executor = null;
	@Setter
	private List<AbstractTaber> tabs = new ArrayList<>();

	public CustomCommand(String commandString) {
		this.commandString = commandString;
		this.plugin = APlugin.getPlugin();
		this.localize = plugin.getLocalizer();
		this.messenger = plugin.getMessenger();
	}

	public CustomCommand withValidator(AbstractValidator validator) {
		this.addValidator(validator);
		return this;
	}

	public CustomCommand withExecutor(CustomExecutor commandExecutor) {
		this.executor = commandExecutor;
		return this;
	}

	public CustomCommand withPermissionMessage(String message) {
		this.permissionMessage = ChatColor.translateAlternateColorCodes('&', message);
		return this;
	}

	public CustomCommand withPermission(String permission) {
		this.permission = permission;
		return this;
	}

	public CustomCommand withUsage(String usage) {
		this.usage = usage;
		return this;
	}

	public CustomCommand withAliases(String... aliases) {
		this.aliases = Arrays.asList(aliases);
		return this;
	}

	public CustomCommand withDescription(String description) {
		this.description = description;
		return this;
	}

	public CustomCommand withTabCompleter(AbstractTaber tab) {
		this.addTab(tab);
		return this;
	}

	public void addTab(AbstractTaber tab) {
		this.tabs.add(tab);
	}

	public void addValidator(AbstractValidator validator) {
		this.validators.add(validator);
	}

	public void setAliases(String... aliases) {
		this.aliases = Arrays.asList(aliases);
	}

	public void valid(CommandSender sender) throws InvalidPermissionException {
		if (validators == null) return;
		for (AbstractValidator v : validators)
			v.isValid(sender);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		try {
			if (executor == null) throw new AException("Команда в разработке");
			valid(sender);
			executor.run(sender, cmd, label, args);
			return true;
		} catch (AException | InvalidPermissionException e) {
			getMessenger().sendErrorString(sender, e.getMessage());
			return false;
		}
	}

	public List<String> onTab(CommandSender sender, Command cmd, String label, String[] args) {
		int index = args.length - 1;
		if (index >= 0 && index < getTabs().size()) {
			try {
				return getTabs().get(index).getTabList(sender, args[index].toLowerCase());
			} catch (AException e) {
				e.printStackTrace();
				getMessenger().sendErrorString(sender, e.getMessage());
			}
		}
		return new ArrayList<>();
	}



}