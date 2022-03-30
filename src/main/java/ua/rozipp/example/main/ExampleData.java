package ua.rozipp.example.main;

import lombok.Getter;
import org.bukkit.entity.Player;
import ua.rozipp.example.config.HitClickType;
import ua.rozipp.example.config.LocationType;

public class ExampleData {
	public static String hostname;
	public static int port;
	public static Player hostPlayer = null;

	@Getter
	public static LocationType locationType;
	@Getter
	public static HitClickType hitClickType;


	public static void init() {
		//get host and port from config.yml
		hostname = ExamplePlugin.getPlugin().getSetting().getStringOrDefault("", "hostname", "0.0.0.0");
		port = ExamplePlugin.getPlugin().getSetting().getIntegerOrDefault("","port", 4711);
		ExamplePlugin.getPlugin().getLogger().config("Using host:port - " + hostname + ":" + Integer.toString(port));

		//get location type (ABSOLUTE or RELATIVE) from config.yml
		locationType = LocationType.valueOf(ExamplePlugin.getPlugin().getSetting().getStringOrDefault("", "location", "RELATIVE").toUpperCase());
		ExamplePlugin.getPlugin().getLogger().info("Using " + locationType.name() + " locations");

		//get hit click type (LEFT, RIGHT or BOTH) from config.yml
		hitClickType = HitClickType.valueOf(ExamplePlugin.getPlugin().getSetting().getStringOrDefault("","hitclick", "RIGHT").toUpperCase());
		ExamplePlugin.getPlugin().getLogger().info("Using " + hitClickType.name() + " clicks for hits");
	}
}
