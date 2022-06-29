package CustomEvents;

import java.util.ArrayList;
import java.util.List;

import Solar.prison.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Jul 30, 2015 6:43:34 PM
 */
public class ArmorListener implements Listener {

	private Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("SolarPrison");
	private final List<String> blockedMaterials;

	public ArmorListener() {
		this.blockedMaterials = getBlocks();
	}

	@EventHandler
	public final void onInventoryClick(final InventoryClickEvent e) {
		boolean shift = false;
		boolean numberkey = false;
		Player player = (Player) e.getWhoClicked();
		if (e.isCancelled()) {
			return;
		}
		if ((e.getClick().equals(ClickType.SHIFT_LEFT)) || (e.getClick().equals(ClickType.SHIFT_RIGHT))) {
			shift = true;
		}
		if (e.getClick().equals(ClickType.NUMBER_KEY)) {
			numberkey = true;
		}
		if (((e.getSlotType() != InventoryType.SlotType.ARMOR) || (e.getSlotType() != InventoryType.SlotType.QUICKBAR))
				&& (!e.getInventory().getType().equals(InventoryType.CRAFTING))
				&& (!e.getInventory().getType().equals(InventoryType.PLAYER))) {
			return;
		}
		if (!(e.getWhoClicked() instanceof Player)) {
			return;
		}
		if (e.getCurrentItem() == null) {
			return;
		}
		ArmorType newArmorType = ArmorType.matchType(shift ? e.getCurrentItem() : e.getCursor());
		if ((!shift) && (newArmorType != null) && (e.getRawSlot() != newArmorType.getSlot())) {
			return;
		}
		if (e.getInventory().getType() == InventoryType.CRAFTING) {
			if ((e.getRawSlot() >= 0) && (e.getRawSlot() <= 4)) {
				return;
			}
		}
		if (shift) {
			newArmorType = ArmorType.matchType(e.getCurrentItem());
			if (newArmorType != null) {
				boolean equipping = true;
				if (e.getRawSlot() == newArmorType.getSlot()) {
					equipping = false;
				}
				if (((newArmorType.equals(ArmorType.HELMET))
						&& (equipping ? e.getWhoClicked().getInventory().getHelmet() == null
								: e.getWhoClicked().getInventory().getHelmet() != null))
						|| ((newArmorType.equals(ArmorType.CHESTPLATE))
								&& (equipping ? e.getWhoClicked().getInventory().getChestplate() == null
										: e.getWhoClicked().getInventory().getChestplate() != null))
						|| ((newArmorType.equals(ArmorType.LEGGINGS))
								&& (equipping ? e.getWhoClicked().getInventory().getLeggings() == null
										: e.getWhoClicked().getInventory().getLeggings() != null))
						|| ((newArmorType.equals(ArmorType.BOOTS))
								&& (equipping ? e.getWhoClicked().getInventory().getBoots() == null
										: e.getWhoClicked().getInventory().getBoots() != null))) {
					ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(player,
							ArmorEquipEvent.EquipMethod.SHIFT_CLICK, newArmorType,
							equipping ? null : e.getCurrentItem(), equipping ? e.getCurrentItem() : null);
					Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
					if (armorEquipEvent.isCancelled()) {
						e.setCancelled(true);
					}
				}
			}
		} else {
			ItemStack newArmorPiece = e.getCursor();
			if (newArmorPiece == null) {
				newArmorPiece = new ItemStack(Material.AIR);
			}
			ItemStack oldArmorPiece = e.getCurrentItem();
			if (oldArmorPiece == null) {
				oldArmorPiece = new ItemStack(Material.AIR);
			}
			if (numberkey) {
				if (e.getInventory().getType().equals(InventoryType.PLAYER)) {
					ItemStack hotbarItem = e.getInventory().getItem(e.getHotbarButton());
					if (hotbarItem != null) {
						newArmorType = ArmorType.matchType(hotbarItem);
						newArmorPiece = hotbarItem;
						oldArmorPiece = e.getInventory().getItem(e.getSlot());
					} else {
						newArmorType = ArmorType.matchType(
								(e.getCurrentItem() != null) && (e.getCurrentItem().getType() != Material.AIR)
										? e.getCurrentItem() : e.getCursor());
					}
				}
			} else {
				newArmorType = ArmorType
						.matchType((e.getCurrentItem() != null) && (e.getCurrentItem().getType() != Material.AIR)
								? e.getCurrentItem() : e.getCursor());
			}
			if ((newArmorType != null) && (e.getRawSlot() == newArmorType.getSlot())) {
				ArmorEquipEvent.EquipMethod method = ArmorEquipEvent.EquipMethod.DRAG;
				if ((e.getAction().equals(InventoryAction.HOTBAR_SWAP)) || (numberkey)) {
					method = ArmorEquipEvent.EquipMethod.HOTBAR_SWAP;
				}
				final ItemStack It = newArmorPiece.clone();
				final ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(player, method, newArmorType, oldArmorPiece,
						newArmorPiece);
				Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
					@SuppressWarnings("deprecation")
					public void run() {
						ItemStack I = e.getWhoClicked().getInventory().getItem(e.getSlot());
						if (e.getInventory().getType().equals(InventoryType.PLAYER)) {
							if (e.getSlot() == ArmorType.HELMET.getSlot()) {
								I = e.getWhoClicked().getEquipment().getHelmet();
							}
							if (e.getSlot() == ArmorType.CHESTPLATE.getSlot()) {
								I = e.getWhoClicked().getEquipment().getChestplate();
							}
							if (e.getSlot() == ArmorType.LEGGINGS.getSlot()) {
								I = e.getWhoClicked().getEquipment().getLeggings();
							}
							if (e.getSlot() == ArmorType.BOOTS.getSlot()) {
								I = e.getWhoClicked().getEquipment().getBoots();
							}
						}
						if (I == null) {
							if (e.getInventory().getType().equals(InventoryType.CRAFTING)) {
								I = new ItemStack(Material.AIR, 0);
							}
							if (e.getInventory().getType().equals(InventoryType.PLAYER)) {
								I = new ItemStack(Material.AIR, 1);
							}
						}
						if ((I.isSimilar(It)) || ((I.getType() == Material.AIR) && (It.getType() == Material.AIR))) {
							Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
							if (armorEquipEvent.isCancelled()) {
								if(It.getType() != Material.AIR){
									e.setCurrentItem(e.getCursor());
									e.setCursor(I);
								}
								
								
								e.setCancelled(true);
							}
						}
					}
				}, 0L);
			} else if (Version.getVersion().getVersionInteger().intValue() < Version.v1_10_R1.getVersionInteger()
					.intValue()) {
				if (e.getHotbarButton() >= 0) {
					if (e.getSlot() > 8) {
						newArmorPiece = e.getWhoClicked().getInventory().getItem(e.getHotbarButton());
					} else {
						newArmorPiece = e.getWhoClicked().getInventory().getItem(e.getSlot());
					}
					if (newArmorPiece == null) {
						newArmorPiece = new ItemStack(Material.AIR);
					}
					if ((ArmorType.matchType(oldArmorPiece) != null) || (oldArmorPiece.getType() == Material.AIR)) {
						if ((ArmorType.matchType(newArmorPiece) != null)
								&& (e.getRawSlot() != ArmorType.matchType(newArmorPiece).getSlot())
								&& (oldArmorPiece.getType() == Material.AIR)) {
							return;
						}
						ArmorEquipEvent.EquipMethod method = ArmorEquipEvent.EquipMethod.DRAG;
						if ((e.getAction().equals(InventoryAction.HOTBAR_SWAP)) || (numberkey)) {
							method = ArmorEquipEvent.EquipMethod.HOTBAR_SWAP;
						}
						if ((ArmorType.matchType(newArmorPiece) != null)
								&& (e.getRawSlot() != ArmorType.matchType(newArmorPiece).getSlot())) {
							ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(player, method, null, oldArmorPiece,
									new ItemStack(Material.AIR));
							Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
							if (armorEquipEvent.isCancelled()) {
								e.setCancelled(true);
							}
							return;
						}
						ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(player, method, newArmorType,
								oldArmorPiece, newArmorPiece);
						Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
						if (armorEquipEvent.isCancelled()) {
							e.setCancelled(true);
						}
					}
				}
			} else if (e.getHotbarButton() >= 0) {
				newArmorPiece = e.getWhoClicked().getInventory().getItem(e.getHotbarButton());
				if ((oldArmorPiece != null)
						&& ((ArmorType.matchType(oldArmorPiece) != null) || (oldArmorPiece.getType() == Material.AIR))
						&& ((ArmorType.matchType(newArmorPiece) != null) || (newArmorPiece == null))) {
					if ((ArmorType.matchType(oldArmorPiece) != null)
							&& (e.getRawSlot() != ArmorType.matchType(oldArmorPiece).getSlot())) {
						return;
					}
					if ((ArmorType.matchType(newArmorPiece) != null)
							&& (e.getRawSlot() != ArmorType.matchType(newArmorPiece).getSlot())) {
						return;
					}
					ArmorEquipEvent.EquipMethod method = ArmorEquipEvent.EquipMethod.DRAG;
					if ((e.getAction().equals(InventoryAction.HOTBAR_SWAP)) || (numberkey)) {
						method = ArmorEquipEvent.EquipMethod.HOTBAR_SWAP;
					}
					ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(player, method, newArmorType, oldArmorPiece,
							newArmorPiece);
					Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
					if (armorEquipEvent.isCancelled()) {
						e.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void playerInteractEvent(PlayerInteractEvent e) {

		if (e.getAction() == Action.PHYSICAL) {
			return;
		}

		if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) {

			if (e.isCancelled() && e.getAction() != Action.RIGHT_CLICK_AIR) {
				return;
			}
			Player player = e.getPlayer();
			if ((e.getClickedBlock() != null) && (e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				Material mat = e.getClickedBlock().getType();
				for (String s : this.blockedMaterials) {
					if (mat.name().toLowerCase().contains(s.toLowerCase())) {
						return;
					}
				}
			}

			ArmorType newArmorType = ArmorType.matchType(e.getItem());
			if ((newArmorType != null)
					&& (((newArmorType.equals(ArmorType.HELMET)) && (e.getPlayer().getInventory().getHelmet() == null))
							|| ((newArmorType.equals(ArmorType.CHESTPLATE))
									&& (e.getPlayer().getInventory().getChestplate() == null))
							|| ((newArmorType.equals(ArmorType.LEGGINGS))
									&& (e.getPlayer().getInventory().getLeggings() == null))
							|| ((newArmorType.equals(ArmorType.BOOTS))
									&& (e.getPlayer().getInventory().getBoots() == null)))) {
				ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(e.getPlayer(), ArmorEquipEvent.EquipMethod.HOTBAR,
						ArmorType.matchType(e.getItem()), null, e.getItem());

				Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
				if (armorEquipEvent.isCancelled()) {
					e.setCancelled(true);
					player.updateInventory();
				}
			}
		}
	}


	@EventHandler
	public void itemBreakEvent(PlayerItemBreakEvent e) {
		ArmorType type = ArmorType.matchType(e.getBrokenItem());
		if (type != null) {
			Player p = e.getPlayer();
			ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.BROKE, type,
					e.getBrokenItem(), null);
			Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
			if (armorEquipEvent.isCancelled()) {
				ItemStack i = e.getBrokenItem().clone();
				i.setAmount(1);
				i.setDurability((short) (i.getDurability() - 1));
				if (type.equals(ArmorType.HELMET)) {
					p.getInventory().setHelmet(i);
				} else if (type.equals(ArmorType.CHESTPLATE)) {
					p.getInventory().setChestplate(i);
				} else if (type.equals(ArmorType.LEGGINGS)) {
					p.getInventory().setLeggings(i);
				} else if (type.equals(ArmorType.BOOTS)) {
					p.getInventory().setBoots(i);
				}
			}
		}
	}

	@EventHandler
	public void playerDeathEvent(PlayerDeathEvent e) {
		Player p = e.getEntity();
		ItemStack[] arrayOfItemStack;
		int j = (arrayOfItemStack = p.getInventory().getArmorContents()).length;
		for (int i = 0; i < j; i++) {
			ItemStack i2 = arrayOfItemStack[i];
			if ((i2 != null) && (!i2.getType().equals(Material.AIR))) {
				Bukkit.getServer().getPluginManager().callEvent(
						new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.DEATH, ArmorType.matchType(i2), i2, null));
			}
		}
	}

	private List<String> getBlocks() {
		List<String> blocks = new ArrayList<String>();
		blocks.add("FURNACE");
		blocks.add("CHEST");
		blocks.add("TRAPPED_CHEST");
		blocks.add("BEACON");
		blocks.add("DISPENSER");
		blocks.add("DROPPER");
		blocks.add("HOPPER");
		blocks.add("WORKBENCH");
		blocks.add("ENCHANTMENT_TABLE");
		blocks.add("ENDER_CHEST");
		blocks.add("ANVIL");
		blocks.add("BED_BLOCK");
		blocks.add("FENCE_GATE");
		blocks.add("SPRUCE_FENCE_GATE");
		blocks.add("BIRCH_FENCE_GATE");
		blocks.add("ACACIA_FENCE_GATE");
		blocks.add("JUNGLE_FENCE_GATE");
		blocks.add("DARK_OAK_FENCE_GATE");
		blocks.add("IRON_DOOR_BLOCK");
		blocks.add("WOODEN_DOOR");
		blocks.add("SPRUCE_DOOR");
		blocks.add("BIRCH_DOOR");
		blocks.add("JUNGLE_DOOR");
		blocks.add("ACACIA_DOOR");
		blocks.add("DARK_OAK_DOOR");
		blocks.add("WOOD_BUTTON");
		blocks.add("STONE_BUTTON");
		blocks.add("TRAP_DOOR");
		blocks.add("IRON_TRAPDOOR");
		blocks.add("DIODE_BLOCK_OFF");
		blocks.add("DIODE_BLOCK_ON");
		blocks.add("REDSTONE_COMPARATOR_OFF");
		blocks.add("REDSTONE_COMPARATOR_ON");
		blocks.add("FENCE");
		blocks.add("SPRUCE_FENCE");
		blocks.add("BIRCH_FENCE");
		blocks.add("JUNGLE_FENCE");
		blocks.add("DARK_OAK_FENCE");
		blocks.add("ACACIA_FENCE");
		blocks.add("NETHER_FENCE");
		blocks.add("BREWING_STAND");
		blocks.add("CAULDRON");
		blocks.add("SIGN_POST");
		blocks.add("WALL_SIGN");
		blocks.add("SIGN");
		blocks.add("DRAGON_EGG");
		blocks.add("LEVER");
		blocks.add("SHULKER_BOX");
		return blocks;
	}

}