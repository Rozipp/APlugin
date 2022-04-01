package ua.rozipp.example.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.example.main.ExampleData;
import ua.rozipp.example.main.ExamplePlugin;
import ua.rozipp.example.remotesession.RemoteSession;
import ua.rozipp.example.remotesession.TickHandler;

import java.util.EnumSet;
import java.util.Set;

public class ExampleListener implements Listener {

	@EventHandler
	public void PlayerJoin(PlayerJoinEvent event) {
		ExamplePlugin.getPlugin().getLogger().info("PlayerJoin");
		Player p = event.getPlayer();
		//p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 2, true, false));	// give night vision power
		ExamplePlugin.getPlugin().getServer().broadcastMessage("Welcome " + p.getPlayerListName());
	}

	public static final Set<Material> blockBreakDetectionTools = EnumSet.of(
			Material.DIAMOND_SWORD,
			Material.GOLD_SWORD,
			Material.IRON_SWORD,
			Material.STONE_SWORD,
			Material.WOOD_SWORD);

	public Player hostPlayer = null;

	@EventHandler(ignoreCancelled=true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		ExamplePlugin.getPlugin().getLogger().info("onPlayerInteract");
		// only react to events which are of the correct type
		if (ExampleData.hitClickType != null)
			switch(ExampleData.hitClickType) {
				case BOTH:
					if ((event.getAction() != Action.RIGHT_CLICK_BLOCK) && (event.getAction() != Action.LEFT_CLICK_BLOCK)) return;
					break;
				case LEFT:
					if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;
					break;
				case RIGHT:
					if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
					break;
			}
		ItemStack currentTool = event.getItem();
		if (currentTool == null || !blockBreakDetectionTools.contains(currentTool.getType())) {
			return;
		}
		for (RemoteSession session: TickHandler.sessions) {
			session.queuePlayerInteractEvent(event);
		}
	}

	@EventHandler(ignoreCancelled=true)
	public void onChatPosted(AsyncPlayerChatEvent event) {
		ExamplePlugin.getPlugin().getLogger().info("onChatPosted");
		//debug
		ExamplePlugin.getPlugin().getLogger().info("[Chat event fired]: " + event.getMessage());
		for (RemoteSession session: TickHandler.sessions) {
			session.queueChatPostedEvent(event);
		}
	}

	@EventHandler(ignoreCancelled=true)
	public void onProjectileHit(ProjectileHitEvent event) {
		ExamplePlugin.getPlugin().getLogger().info("onProjectileHit");
		for (RemoteSession session: TickHandler.sessions) {
			session.queueProjectileHitEvent(event);
		}
	}




}
