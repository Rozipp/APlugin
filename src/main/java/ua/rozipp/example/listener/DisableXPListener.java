package ua.rozipp.example.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import ua.rozipp.abstractplugin.main.AMessenger;
import ua.rozipp.example.main.ExamplePlugin;
import ua.rozipp.example.main.Static;

public class DisableXPListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onExpBottleEvent(ExpBottleEvent event) {
		ExamplePlugin.getPlugin().getLogger().info("onExpBottleEvent");
		event.setExperience(0);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onEnchantItemEvent(EnchantItemEvent event) {
		ExamplePlugin.getPlugin().getLogger().info("onEnchantItemEvent");
		Static.getMessenger().sendErrorLocalized(event.getEnchanter(), "customItem_NoEnchanting");
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onItemSpawnEvent(ItemSpawnEvent event) {
		ExamplePlugin.getPlugin().getLogger().info("onItemSpawnEvent");
//		if (event.getEntity().getType().equals(EntityType.EXPERIENCE_ORB)) {
//			event.setCancelled(true);
//		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		ExamplePlugin.getPlugin().getLogger().info("onPlayerInteractEvent");
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

//		if (event.getClickedBlock() == null || ItemManager.getTypeId(event.getClickedBlock()) == CivData.AIR) {
//			return;
//		}
//
//		Block block = event.getClickedBlock();
//
//		if (block.getType().equals(Material.ENCHANTMENT_TABLE)) {
//			CivMessage.sendError(event.getPlayer(), CivSettings.localize.getString("customItem_enchantTableDisabled"));
//			event.setCancelled(true);
//		}
//
//		if (block.getType().equals(Material.ANVIL)) {
//
//			// Started to get annoyed not being able to rename items as OP. This makes it easier.
//			if (!(event.getPlayer().isOp()))
//			{
//				CivMessage.sendError(event.getPlayer(), CivSettings.localize.getString("customItem_anvilDisabled"));
//				event.setCancelled(true);
//			}
//		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerExpChange(PlayerExpChangeEvent event) {
		ExamplePlugin.getPlugin().getLogger().info("onPlayerExpChange");
//		Resident resident = CivGlobal.getResident(event.getPlayer());
//		CivMessage.send(resident, CivColor.LightGreen+CivSettings.localize.getString("var_customItem_Pickup",CivColor.Yellow+event.getAmount()+CivColor.LightGreen,CivSettings.CURRENCY_NAME));
//		resident.getTreasury().deposit(event.getAmount());
		
		
		event.setAmount(0);
	}
	
}
