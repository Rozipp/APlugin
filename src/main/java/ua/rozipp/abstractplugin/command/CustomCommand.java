package ua.rozipp.abstractplugin.command;

import ua.rozipp.abstractplugin.command.taber.AbstractTaber;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ua.rozipp.abstractplugin.main.AException;
import ua.rozipp.abstractplugin.main.ALocalize;
import ua.rozipp.abstractplugin.main.AMessenger;
import ua.rozipp.abstractplugin.main.APlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * <p>
 * Общий клас для команд. После создания команды,добавление параметров возможно как через сеттеры (set...()), так через билдеры (with..())
 * </p>
 * executor    - вызываться при выполнении команды. Обязательный параметр.
 * description - хранит описание, для вывода в help-е подменю - List<String> aliases - Вариванты альтернативных команд
 * validator   - Проверка команды на доступность для CommandSender. Обрабатываються в порядке добавления;
 * tabs         - Класы дополнения клавишей Tab. Обрабатываються в порядке добавления
 * @author rozipp */
@Setter
@Getter
public class CustomCommand {

	private APlugin plugin;
	private ALocalize localize;
	private AMessenger messenger;

	private String string_cmd;
	private String description;
	private List<String> aliases = null;
	private String usage = null;
	private String permission = null;
	private String permissionMessage = null;
	private List<AbstractValidator> validators = new ArrayList<>();
	private CustomExecutor executor = null;
	private List<AbstractTaber> tabs = new ArrayList<>();

	public CustomCommand(String string_cmd) {
		this.string_cmd = string_cmd;
		this.plugin = APlugin.getPlugin();
		this.localize = plugin.getLocalize();
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

	public void valid(CommandSender sender) throws CustomCommandValidException {
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
		} catch (AException | CustomCommandValidException e) {
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

	public interface CustomExecutor {
		void run(CommandSender sender, Command cmd, String label, String[] args) throws AException;
	}

}