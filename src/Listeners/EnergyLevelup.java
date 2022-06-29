package Listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.inventivetalent.particle.ParticleEffect;

import Enchantment.Dust;
import Enchantment.Energy;
import Enchantment.PEnchant;
import Enchantment.PEnchantTier;
import Solar.prison.Main;
import Utils.Util;
import io.netty.util.internal.ThreadLocalRandom;
import net.md_5.bungee.api.ChatColor;

public class EnergyLevelup implements Listener {
	public static HashMap<UUID, ItemStack> enchanting = new HashMap<>();
	HashMap<UUID , Long> delay = new HashMap<>();

	public static boolean isEnchanting(UUID u) {
		if (enchanting.containsKey(u)) {
			return true;
		}
		return false;
	}

	@EventHandler(ignoreCancelled = true)
	public void event(PlayerDropItemEvent e) {

		ItemStack is = e.getItemDrop().getItemStack();
		if (!Energy.hasEnergy(is) && is.getType() != Material.GLOWSTONE_DUST)
			return;
		final Player player = e.getPlayer();
//		if (!player.getWorld().getName().equalsIgnoreCase("spawn"))
//			return;
		boolean portal = false;
		boolean barrier = false;
		boolean beacon = false;
		Block beac = null;

		for (Block b : Util.getNearbyBlocks(player.getLocation(), 4)) {
			if (!barrier || !portal || !beacon) {
				if (b.getType() != Material.BARRIER && b.getType() != Material.ENDER_PORTAL
						&& b.getType() != Material.BEACON)
					continue;

				if (b.getType() == Material.BARRIER) {
					barrier = true;
				}
				if (b.getType() == Material.ENDER_PORTAL) {
					portal = true;
				}
				if (b.getType() == Material.BEACON) {
					beacon = true;
					beac = b;
				}
			} else {
				break;
			}
		}

		if (portal && barrier && beacon) {

			if (isEnchanting(player.getUniqueId())) {

				if (is.getType() != Material.GLOWSTONE_DUST) {
					player.sendMessage(ChatColor.RED
							+ "Bạn không thể phù phép 2 món đồ cùng lúc , Hãy hoàn thành phù phép kia trước");
					if (!e.isCancelled())
						e.setCancelled(true);
					return;
				} else {

					if (Dust.isDust(is)) {
						e.setCancelled(true);
						PEnchantTier tier = Dust.getTier(is);
						ArmorStand am = null;
						if (tier == null)
							return;
						for (Entity en : player.getWorld().getNearbyEntities(player.getEyeLocation(), 20, 20, 20)) {
							if (en instanceof ArmorStand) {
								if (!en.getMetadata("solar.upgrade.armorstand.owner").isEmpty()) {
									if (en.getMetadata("solar.upgrade.armorstand.owner").get(0).asString()
											.equals(player.getUniqueId().toString())) {
										if (!en.getMetadata("solar.upgrade.armorstand").isEmpty()) {
											PEnchantTier c = PEnchantTier.valueOf(
													en.getMetadata("solar.upgrade.armorstand.tier").get(0).asString());
											if (c == tier) {
												am = (ArmorStand) en;
												break;
											}
										}

									}
								}
							}
						}

						if (am != null) {
							int amount = e.getItemDrop().getItemStack().getAmount();
							Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {

								@Override
								public void run() {
									player.getInventory().removeItem(is);

								}
							}, 1);

							ParticleEffect.ENCHANTMENT_TABLE.send(Util.getNearbyPlayer(player.getLocation(), 10d),
									player.getEyeLocation(), 0, 0, 0, 1, 100);

							int s = 0;
							for (Entity en : am.getWorld().getNearbyEntities(am.getLocation(), 1, 1, 1)) {
								if (en instanceof ArmorStand) {
									if (!en.getMetadata("solar.upgrade.armorstand.owner").isEmpty()) {
										if (en.getMetadata("solar.upgrade.armorstand.owner").get(0).asString()
												.equals(player.getUniqueId().toString())) {

											if (!en.getMetadata("solar.upgrade.armorstand").isEmpty()) {
												if (PEnchantTier.valueOf(en.getMetadata("solar.upgrade.armorstand.tier")
														.get(0).asString()) == tier) {

													if (en.getCustomName() == null)
														continue;
													if (en.getCustomName().contains(ChatColor.WHITE + ""
															+ ChatColor.BOLD + "Tỉ lệ thành công ")) {

														s = en.getMetadata("solar.upgrade.armorstand.s").get(0).asInt();
														en.setCustomName(ChatColor.WHITE + "" + ChatColor.BOLD
																+ "Tỉ lệ thành công " + ChatColor.GREEN
																+ (s + 2 * amount) + ChatColor.LIGHT_PURPLE + "%");
														for (Entity enz : am.getWorld()
																.getNearbyEntities(en.getLocation(), 1, 1, 1)) {
															if (!enz.getMetadata("solar.upgrade.armorstand.owner")
																	.isEmpty()) {
																if (enz.getMetadata("solar.upgrade.armorstand.owner")
																		.get(0).asString()
																		.equals(player.getUniqueId().toString())) {
																	if (!enz.getMetadata("solar.upgrade.armorstand")
																			.isEmpty()) {
																		enz.setMetadata("solar.upgrade.armorstand.s",
																				new FixedMetadataValue(Main.getMain(),
																						s + 2 * amount));
																	}

																}
															}
														}

													}
													if (en.getCustomName().contains(
															ChatColor.GOLD + "" + ChatColor.BOLD + "Tỉ lệ thất bại ")) {
														s = en.getMetadata("solar.upgrade.armorstand.s").get(0).asInt();
														en.setCustomName(
																ChatColor.GOLD + "" + ChatColor.BOLD + "Tỉ lệ thất bại "
																		+ ChatColor.RED + ((100 - s) - 2 * amount)
																		+ ChatColor.LIGHT_PURPLE + "%");

													}
												}
											}

										}
									}
								}
							}

							return;
						}
					}
					return;
				}
			}

			if (is.getType() == Material.GLOWSTONE_DUST)
				return;

			if (Energy.isMaxEnergy(is)) {
				Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {

					@Override
					public void run() {
						player.getInventory().removeItem(is);

					}
				}, 1);
				Energy.levelUpEnergy(is);
				enchanting.put(player.getUniqueId(), is);
				Location l = player.getLocation();
				l.setYaw(0);
				l.setPitch(0);
				ArmorStand am = (ArmorStand) player.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
				am.setVisible(false);
				am.setGravity(false);
				am.setArms(true);
				am.setRightArmPose(new EulerAngle(Math.toRadians(-100), Math.toRadians(-90), 0));
				am.setItemInHand(is);
				am.setCustomNameVisible(true);
				am.setMetadata("solar.upgrade.armorstand.mid", new FixedMetadataValue(Main.getMain(), true));
				am.setMetadata("solar.upgrade.armorstand.owner",
						new FixedMetadataValue(Main.getMain(), player.getUniqueId()));

				if (is.hasItemMeta()) {
					if (is.getItemMeta().hasDisplayName()) {
						am.setCustomName(is.getItemMeta().getDisplayName());
					} else {
						am.setCustomName(ChatColor.AQUA + player.getName());
					}
				} else {
					am.setCustomName(ChatColor.AQUA + player.getName());
				}

				Location amloc = am.getLocation().add(0.2, 1.3, 0);
				Location beaconloc = beac.getLocation().add(0.5, 0, 0.5);

				Vector v = amloc.toVector().subtract(beaconloc.toVector()).normalize().multiply(-0.1);
				Location asd = beaconloc.clone().add(0, 0.5, -0.2);
				Location zxc = asd.clone();
				new BukkitRunnable() {
					double p = Math.PI / 32;
					double i = 0;

					double p2 = Math.PI / 32;
					double i2 = 0;
					int counter = 0;
					double ra = 1;
					double height = 0;

					double i3 = 0;
					double p3 = Math.PI / 32;

					boolean xoay = false;
					boolean xoayxong = false;

					ArmorStand am2 = null;
					ArmorStand am3 = null;
					ArmorStand am4 = null;
					ArmorStand am5 = null;
					ArmorStand am6 = null;

					ArmorStand am2holo1 = null;
					ArmorStand am2holo2 = null;
					ArmorStand am2holo3 = null;

					ArmorStand am3holo1 = null;
					ArmorStand am3holo2 = null;
					ArmorStand am3holo3 = null;

					ArmorStand am4holo1 = null;
					ArmorStand am4holo2 = null;
					ArmorStand am4holo3 = null;

					ArmorStand am5holo1 = null;
					ArmorStand am5holo2 = null;
					ArmorStand am5holo3 = null;

					ArmorStand am6holo1 = null;
					ArmorStand am6holo2 = null;
					ArmorStand am6holo3 = null;
					ArrayList<ArmorStand> amlist = new ArrayList<>();

					@Override
					public void run() {
						if (!am.isValid())
							this.cancel();
						if (!player.isOnline()) {
							int s = 0;
							if (am2 == null) {
								s = ThreadLocalRandom.current().nextInt(PEnchantTier.SIMPLE.getMinSuccessPercent(),
										PEnchantTier.SIMPLE.getMaxSuccessPercent());
							} else {
								s = am2.getMetadata("solar.upgrade.armorstand.s").get(0).asInt();
							}
							if (Util.percentRoll(s)) {
								PEnchantTier pe = PEnchantTier.SIMPLE;
								ArrayList<PEnchant> rad = new ArrayList<>();
								for (PEnchant enc : PEnchant.values()) {
									if (enc.getTier() == pe) {
										rad.add(enc);
									}
								}
								int multiply = 1;
								if(is.getType().toString().contains("WOOD")){
									multiply = 1;
								}else if(is.getType().toString().contains("STONE")){
									multiply = 2;

								}else if(is.getType().toString().contains("IRON")){
									multiply = 3;
								}else if(is.getType().toString().contains("GOLD")){
									multiply = 4;
								}else if(is.getType().toString().contains("DIAMOND")){
									multiply = 5;
								}else if(is.getType().toString().contains("BOW")){
									multiply = 5;
								}
								PEnchant enchant = rad.get(ThreadLocalRandom.current().nextInt(0, rad.size()));
								if (enchant.hasEnchant(is)) {
									if (enchant.getLevel(is) < enchant.getMaxLevel()*multiply) {
										enchant.apply(is, enchant.getLevel(is) + 1);

									} else {

									}
								} else {

									enchant.apply(is, 1);
								}
								enchanting.put(player.getUniqueId(), is);

							} else {
								player.sendMessage(ChatColor.RED + "Nâng cấp thật bại , bạn không nhận được phù phép");
								enchanting.put(player.getUniqueId(), is);

							}
							for (Entity en : l.getWorld().getNearbyEntities(zxc, 15, 15, 15)) {

								if (en instanceof ArmorStand) {
									if (!en.getMetadata("solar.upgrade.armorstand.owner").isEmpty()) {
										if (en.getMetadata("solar.upgrade.armorstand.owner").get(0).asString()
												.equals(player.getUniqueId().toString())) {
											if (!en.getMetadata("solar.upgrade.armorstand").isEmpty()) {
												en.remove();
											}
											if (!en.getMetadata("solar.upgrade.armorstand.mid").isEmpty()) {
												en.remove();
											}
										}
									}
								}
							}

							this.cancel();
						}
						if (xoayxong) {
							if(counter >= (96 + 60 * 20)){
								for (Entity en : l.getWorld().getNearbyEntities(l, 15, 15, 15)) {

									if (en instanceof ArmorStand) {
										if (!en.getMetadata("solar.upgrade.armorstand.owner").isEmpty()) {
											if (en.getMetadata("solar.upgrade.armorstand.owner").get(0).asString()
													.equals(player.getUniqueId().toString())) {
												if (!en.getMetadata("solar.upgrade.armorstand").isEmpty()) {
													en.remove();
												}
												if (!en.getMetadata("solar.upgrade.armorstand.mid").isEmpty()) {
													en.remove();
												}
											}
										}
									}
								}
								if (isEnchanting(player.getUniqueId()) && player.isOnline()) {
									enchanting.remove(player.getUniqueId());

									player.getInventory().addItem(is);
								}

								Util.sendActionBar(player,"") ;
								this.cancel();
							}
							Util.sendActionBar(player,ChatColor.GREEN + "Hãy chọn cấp độ phù phép " + ChatColor.RED + "" + (int) ((96 + 60 * 20) - counter) / 20 +"" + ChatColor.BLUE + " giây" ) ;
							counter++;
							Location lz2 = am2holo1.getLocation().subtract(0.1, 0.4, 0);
							Location lz3 = am3holo1.getLocation().subtract(0.1, 0.4, 0);
							Location lz4 = am4holo1.getLocation().subtract(0.1, 0.4, 0);
							Location lz5 = am5holo1.getLocation().subtract(0.1, 0.4, 0);
							Location lz6 = am6holo1.getLocation().subtract(0.1, 0.4, 0);
							if (am2.getRightArmPose() != new EulerAngle(Math.toRadians(-100), Math.toRadians(-40), 0))
								am2.setRightArmPose(new EulerAngle(Math.toRadians(-100), Math.toRadians(-40), 0));
							lz2.setPitch(0);
							lz2.setYaw(0);

							double x = Math.cos(p3 + i3) * (0.42);
							double y = 0;
							double z = Math.sin(p3 + i3) * (0.42);

							Location oldlz2 = lz2.clone();

							lz2.subtract(x, y, z);
							lz2.setDirection(lz2.toVector().subtract(oldlz2.toVector()).multiply(-1));
							am2.teleport(lz2);
							ParticleEffect.FIREWORKS_SPARK.send(Arrays.asList(new Player[] { player }),
									lz2.clone().add(0.2, 1.3, 0), 0, 0, 0, 0, 1);
							lz2.add(x, y, z);
							//

							if (am3.getRightArmPose() != new EulerAngle(Math.toRadians(-100), Math.toRadians(-40), 0))
								am3.setRightArmPose(new EulerAngle(Math.toRadians(-100), Math.toRadians(-40), 0));
							lz3.setPitch(0);
							lz3.setYaw(0);

							Location oldlz3 = lz3.clone();

							lz3.subtract(x, y, z);
							lz3.setDirection(lz3.toVector().subtract(oldlz3.toVector()).multiply(-1));
							am3.teleport(lz3);
							ParticleEffect.FIREWORKS_SPARK.send(Arrays.asList(new Player[] { player }),
									lz3.clone().add(0.2, 1.3, 0), 0, 0, 0, 0, 1);

							lz3.add(x, y, z);
							//

							if (am4.getRightArmPose() != new EulerAngle(Math.toRadians(-100), Math.toRadians(-40), 0))
								am4.setRightArmPose(new EulerAngle(Math.toRadians(-100), Math.toRadians(-40), 0));
							lz4.setPitch(0);
							lz4.setYaw(0);

							Location oldlz4 = lz4.clone();

							lz4.subtract(x, y, z);
							lz4.setDirection(lz4.toVector().subtract(oldlz4.toVector()).multiply(-1));
							am4.teleport(lz4);
							ParticleEffect.FIREWORKS_SPARK.send(Arrays.asList(new Player[] { player }),
									lz4.clone().add(0.2, 1.3, 0), 0, 0, 0, 0, 1);

							lz4.add(x, y, z);
							//

							if (am5.getRightArmPose() != new EulerAngle(Math.toRadians(-100), Math.toRadians(-40), 0))
								am5.setRightArmPose(new EulerAngle(Math.toRadians(-100), Math.toRadians(-40), 0));
							lz5.setPitch(0);
							lz5.setYaw(0);

							Location oldlz5 = lz5.clone();

							lz5.subtract(x, y, z);
							lz5.setDirection(lz5.toVector().subtract(oldlz5.toVector()).multiply(-1));
							am5.teleport(lz5);
							ParticleEffect.FIREWORKS_SPARK.send(Arrays.asList(new Player[] { player }),
									lz5.clone().add(0.2, 1.3, 0), 0, 0, 0, 0, 1);

							lz5.add(x, y, z);
							//

							if (am6.getRightArmPose() != new EulerAngle(Math.toRadians(-100), Math.toRadians(-40), 0))
								am6.setRightArmPose(new EulerAngle(Math.toRadians(-100), Math.toRadians(-40), 0));
							lz6.setPitch(0);
							lz6.setYaw(0);

							Location oldlz6 = lz6.clone();

							lz6.subtract(x, y, z);
							lz6.setDirection(lz6.toVector().subtract(oldlz6.toVector()).multiply(-1));
							am6.teleport(lz6);

							ParticleEffect.FIREWORKS_SPARK.send(Arrays.asList(new Player[] { player }),
									lz6.clone().add(0.2, 1.3, 0), 0, 0, 0, 0, 1);

							lz6.add(x, y, z);

							i3 += Math.PI / 32;
						}
						if (xoay && !xoayxong) {
							if (counter == 96) {
								xoayxong = true;
							}
							if (am2 == null) {
								int s = ThreadLocalRandom.current().nextInt(PEnchantTier.SIMPLE.getMinSuccessPercent(),
										PEnchantTier.SIMPLE.getMaxSuccessPercent());
								int d = 100 - s;
								am2 = (ArmorStand) player.getWorld().spawnEntity(zxc, EntityType.ARMOR_STAND);
								am2.setVisible(false);
								am2.setGravity(false);
								am2.setArms(true);
								am2.setRightArmPose(new EulerAngle(Math.toRadians(-100), Math.toRadians(-90), 0));
								am2.setItemInHand(is);
								am2.setMetadata("solar.upgrade.armorstand",
										new FixedMetadataValue(Main.getMain(), true));
								am2.setMetadata("solar.upgrade.armorstand.tier",
										new FixedMetadataValue(Main.getMain(), "SIMPLE"));
								am2.setMetadata("solar.upgrade.armorstand.s",
										new FixedMetadataValue(Main.getMain(), s));

								am2holo1 = (ArmorStand) player.getWorld().spawnEntity(zxc, EntityType.ARMOR_STAND);
								am2holo1.setVisible(false);
								am2holo1.setGravity(false);
								am2holo1.setCustomNameVisible(true);
								am2holo1.setCustomName(
										PEnchantTier.SIMPLE.getNameColor() + PEnchantTier.SIMPLE.getName());
								am2holo1.setMetadata("solar.upgrade.armorstand",
										new FixedMetadataValue(Main.getMain(), true));
								am2holo1.setMetadata("solar.upgrade.armorstand.tier",
										new FixedMetadataValue(Main.getMain(), "SIMPLE"));
								am2holo1.setMetadata("solar.upgrade.armorstand.s",
										new FixedMetadataValue(Main.getMain(), s));

								am2holo2 = (ArmorStand) player.getWorld().spawnEntity(zxc, EntityType.ARMOR_STAND);
								am2holo2.setVisible(false);
								am2holo2.setGravity(false);
								am2holo2.setCustomNameVisible(true);
								am2holo2.setCustomName(ChatColor.WHITE + "" + ChatColor.BOLD + "Tỉ lệ thành công "
										+ ChatColor.GREEN + s + ChatColor.LIGHT_PURPLE + "%");
								am2holo2.setMetadata("solar.upgrade.armorstand",
										new FixedMetadataValue(Main.getMain(), true));
								am2holo2.setMetadata("solar.upgrade.armorstand.tier",
										new FixedMetadataValue(Main.getMain(), "SIMPLE"));
								am2holo2.setMetadata("solar.upgrade.armorstand.s",
										new FixedMetadataValue(Main.getMain(), s));

								am2holo3 = (ArmorStand) player.getWorld().spawnEntity(zxc, EntityType.ARMOR_STAND);
								am2holo3.setVisible(false);
								am2holo3.setGravity(false);
								am2holo3.setCustomNameVisible(true);
								am2holo3.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "Tỉ lệ thất bại "
										+ ChatColor.RED + d + ChatColor.LIGHT_PURPLE + "%");
								am2holo3.setMetadata("solar.upgrade.armorstand",
										new FixedMetadataValue(Main.getMain(), true));
								am2holo3.setMetadata("solar.upgrade.armorstand.tier",
										new FixedMetadataValue(Main.getMain(), "SIMPLE"));
								am2holo3.setMetadata("solar.upgrade.armorstand.s",
										new FixedMetadataValue(Main.getMain(), s));

								s = ThreadLocalRandom.current().nextInt(PEnchantTier.UNCOMMON.getMinSuccessPercent(),
										PEnchantTier.UNCOMMON.getMaxSuccessPercent());
								d = 100 - s;
								am3 = (ArmorStand) player.getWorld().spawnEntity(zxc, EntityType.ARMOR_STAND);
								am3.setVisible(false);
								am3.setGravity(false);
								am3.setArms(true);
								am3.setRightArmPose(new EulerAngle(Math.toRadians(-100), Math.toRadians(-90), 0));
								am3.setItemInHand(is);
								am3.setMetadata("solar.upgrade.armorstand",
										new FixedMetadataValue(Main.getMain(), true));
								am3.setMetadata("solar.upgrade.armorstand.tier",
										new FixedMetadataValue(Main.getMain(), "UNCOMMON"));
								am3.setMetadata("solar.upgrade.armorstand.s",
										new FixedMetadataValue(Main.getMain(), s));

								am3holo1 = (ArmorStand) player.getWorld().spawnEntity(zxc, EntityType.ARMOR_STAND);
								am3holo1.setVisible(false);
								am3holo1.setGravity(false);
								am3holo1.setCustomNameVisible(true);
								am3holo1.setCustomName(
										PEnchantTier.UNCOMMON.getNameColor() + PEnchantTier.UNCOMMON.getName());

								am3holo2 = (ArmorStand) player.getWorld().spawnEntity(zxc, EntityType.ARMOR_STAND);
								am3holo2.setVisible(false);
								am3holo2.setGravity(false);
								am3holo2.setCustomNameVisible(true);
								am3holo2.setCustomName(ChatColor.WHITE + "" + ChatColor.BOLD + "Tỉ lệ thành công "
										+ ChatColor.GREEN + s + ChatColor.LIGHT_PURPLE + "%");

								am3holo3 = (ArmorStand) player.getWorld().spawnEntity(zxc, EntityType.ARMOR_STAND);
								am3holo3.setVisible(false);
								am3holo3.setGravity(false);
								am3holo3.setCustomNameVisible(true);
								am3holo3.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "Tỉ lệ thất bại "
										+ ChatColor.RED + d + ChatColor.LIGHT_PURPLE + "%");
								am3holo1.setMetadata("solar.upgrade.armorstand",
										new FixedMetadataValue(Main.getMain(), true));
								am3holo1.setMetadata("solar.upgrade.armorstand.tier",
										new FixedMetadataValue(Main.getMain(), "UNCOMMON"));
								am3holo1.setMetadata("solar.upgrade.armorstand.s",
										new FixedMetadataValue(Main.getMain(), s));

								am3holo2.setMetadata("solar.upgrade.armorstand",
										new FixedMetadataValue(Main.getMain(), true));
								am3holo2.setMetadata("solar.upgrade.armorstand.tier",
										new FixedMetadataValue(Main.getMain(), "UNCOMMON"));
								am3holo2.setMetadata("solar.upgrade.armorstand.s",
										new FixedMetadataValue(Main.getMain(), s));

								am3holo3.setMetadata("solar.upgrade.armorstand",
										new FixedMetadataValue(Main.getMain(), true));
								am3holo3.setMetadata("solar.upgrade.armorstand.tier",
										new FixedMetadataValue(Main.getMain(), "UNCOMMON"));
								am3holo3.setMetadata("solar.upgrade.armorstand.s",
										new FixedMetadataValue(Main.getMain(), s));

								s = ThreadLocalRandom.current().nextInt(PEnchantTier.ELITE.getMinSuccessPercent(),
										PEnchantTier.ELITE.getMaxSuccessPercent());
								d = 100 - s;
								am4 = (ArmorStand) player.getWorld().spawnEntity(zxc, EntityType.ARMOR_STAND);
								am4.setVisible(false);
								am4.setGravity(false);
								am4.setArms(true);
								am4.setRightArmPose(new EulerAngle(Math.toRadians(-100), Math.toRadians(-90), 0));
								am4.setItemInHand(is);
								am4.setMetadata("solar.upgrade.armorstand",
										new FixedMetadataValue(Main.getMain(), true));
								am4.setMetadata("solar.upgrade.armorstand.tier",
										new FixedMetadataValue(Main.getMain(), "ELITE"));
								am4.setMetadata("solar.upgrade.armorstand.s",
										new FixedMetadataValue(Main.getMain(), s));

								am4holo1 = (ArmorStand) player.getWorld().spawnEntity(zxc, EntityType.ARMOR_STAND);
								am4holo1.setVisible(false);
								am4holo1.setGravity(false);
								am4holo1.setCustomNameVisible(true);
								am4holo1.setCustomName(
										PEnchantTier.ELITE.getNameColor() + PEnchantTier.ELITE.getName());

								am4holo2 = (ArmorStand) player.getWorld().spawnEntity(zxc, EntityType.ARMOR_STAND);
								am4holo2.setVisible(false);
								am4holo2.setGravity(false);
								am4holo2.setCustomNameVisible(true);
								am4holo2.setCustomName(ChatColor.WHITE + "" + ChatColor.BOLD + "Tỉ lệ thành công "
										+ ChatColor.GREEN + s + ChatColor.LIGHT_PURPLE + "%");

								am4holo3 = (ArmorStand) player.getWorld().spawnEntity(zxc, EntityType.ARMOR_STAND);
								am4holo3.setVisible(false);
								am4holo3.setGravity(false);
								am4holo3.setCustomNameVisible(true);
								am4holo3.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "Tỉ lệ thất bại "
										+ ChatColor.RED + d + ChatColor.LIGHT_PURPLE + "%");

								am4holo1.setMetadata("solar.upgrade.armorstand",
										new FixedMetadataValue(Main.getMain(), true));
								am4holo1.setMetadata("solar.upgrade.armorstand.tier",
										new FixedMetadataValue(Main.getMain(), "ELITE"));
								am4holo1.setMetadata("solar.upgrade.armorstand.s",
										new FixedMetadataValue(Main.getMain(), s));

								am4holo2.setMetadata("solar.upgrade.armorstand",
										new FixedMetadataValue(Main.getMain(), true));
								am4holo2.setMetadata("solar.upgrade.armorstand.tier",
										new FixedMetadataValue(Main.getMain(), "ELITE"));
								am4holo2.setMetadata("solar.upgrade.armorstand.s",
										new FixedMetadataValue(Main.getMain(), s));

								am4holo3.setMetadata("solar.upgrade.armorstand",
										new FixedMetadataValue(Main.getMain(), true));
								am4holo3.setMetadata("solar.upgrade.armorstand.tier",
										new FixedMetadataValue(Main.getMain(), "ELITE"));
								am4holo3.setMetadata("solar.upgrade.armorstand.s",
										new FixedMetadataValue(Main.getMain(), s));

								s = ThreadLocalRandom.current().nextInt(PEnchantTier.ULTIMATE.getMinSuccessPercent(),
										PEnchantTier.ULTIMATE.getMaxSuccessPercent());
								d = 100 - s;
								am5 = (ArmorStand) player.getWorld().spawnEntity(zxc, EntityType.ARMOR_STAND);
								am5.setVisible(false);
								am5.setGravity(false);
								am5.setArms(true);
								am5.setRightArmPose(new EulerAngle(Math.toRadians(-100), Math.toRadians(-90), 0));
								am5.setItemInHand(is);
								am5.setItemInHand(is);
								am5.setMetadata("solar.upgrade.armorstand",
										new FixedMetadataValue(Main.getMain(), true));
								am5.setMetadata("solar.upgrade.armorstand.tier",
										new FixedMetadataValue(Main.getMain(), "ULTIMATE"));
								am5.setMetadata("solar.upgrade.armorstand.s",
										new FixedMetadataValue(Main.getMain(), s));

								am5holo1 = (ArmorStand) player.getWorld().spawnEntity(zxc, EntityType.ARMOR_STAND);
								am5holo1.setVisible(false);
								am5holo1.setGravity(false);
								am5holo1.setCustomNameVisible(true);
								am5holo1.setCustomName(
										PEnchantTier.ULTIMATE.getNameColor() + PEnchantTier.ULTIMATE.getName());

								am5holo2 = (ArmorStand) player.getWorld().spawnEntity(zxc, EntityType.ARMOR_STAND);
								am5holo2.setVisible(false);
								am5holo2.setGravity(false);
								am5holo2.setCustomNameVisible(true);
								am5holo2.setCustomName(ChatColor.WHITE + "" + ChatColor.BOLD + "Tỉ lệ thành công "
										+ ChatColor.GREEN + s + ChatColor.LIGHT_PURPLE + "%");

								am5holo3 = (ArmorStand) player.getWorld().spawnEntity(zxc, EntityType.ARMOR_STAND);
								am5holo3.setVisible(false);
								am5holo3.setGravity(false);
								am5holo3.setCustomNameVisible(true);
								am5holo3.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "Tỉ lệ thất bại "
										+ ChatColor.RED + d + ChatColor.LIGHT_PURPLE + "%");

								am5holo1.setMetadata("solar.upgrade.armorstand",
										new FixedMetadataValue(Main.getMain(), true));
								am5holo1.setMetadata("solar.upgrade.armorstand.tier",
										new FixedMetadataValue(Main.getMain(), "ULTIMATE"));
								am5holo1.setMetadata("solar.upgrade.armorstand.s",
										new FixedMetadataValue(Main.getMain(), s));

								am5holo2.setMetadata("solar.upgrade.armorstand",
										new FixedMetadataValue(Main.getMain(), true));
								am5holo2.setMetadata("solar.upgrade.armorstand.tier",
										new FixedMetadataValue(Main.getMain(), "ULTIMATE"));
								am5holo2.setMetadata("solar.upgrade.armorstand.s",
										new FixedMetadataValue(Main.getMain(), s));

								am5holo3.setMetadata("solar.upgrade.armorstand",
										new FixedMetadataValue(Main.getMain(), true));
								am5holo3.setMetadata("solar.upgrade.armorstand.tier",
										new FixedMetadataValue(Main.getMain(), "ULTIMATE"));
								am5holo3.setMetadata("solar.upgrade.armorstand.s",
										new FixedMetadataValue(Main.getMain(), s));

								s = ThreadLocalRandom.current().nextInt(PEnchantTier.LEGENDARY.getMinSuccessPercent(),
										PEnchantTier.LEGENDARY.getMaxSuccessPercent());
								d = 100 - s;
								am6 = (ArmorStand) player.getWorld().spawnEntity(zxc, EntityType.ARMOR_STAND);
								am6.setVisible(false);
								am6.setGravity(false);
								am6.setArms(true);
								am6.setRightArmPose(new EulerAngle(Math.toRadians(-100), Math.toRadians(-90), 0));
								am6.setItemInHand(is);
								am6.setMetadata("solar.upgrade.armorstand",
										new FixedMetadataValue(Main.getMain(), true));
								am6.setMetadata("solar.upgrade.armorstand.tier",
										new FixedMetadataValue(Main.getMain(), "LEGENDARY"));
								am6.setMetadata("solar.upgrade.armorstand.s",
										new FixedMetadataValue(Main.getMain(), s));
								am6.setMetadata("solar.upgrade.armorstand.d",
										new FixedMetadataValue(Main.getMain(), d));
								am6holo1 = (ArmorStand) player.getWorld().spawnEntity(zxc, EntityType.ARMOR_STAND);
								am6holo1.setVisible(false);
								am6holo1.setGravity(false);
								am6holo1.setCustomNameVisible(true);
								am6holo1.setCustomName(
										PEnchantTier.LEGENDARY.getNameColor() + PEnchantTier.LEGENDARY.getName());

								am6holo2 = (ArmorStand) player.getWorld().spawnEntity(zxc, EntityType.ARMOR_STAND);
								am6holo2.setVisible(false);
								am6holo2.setGravity(false);
								am6holo2.setCustomNameVisible(true);
								am6holo2.setCustomName(ChatColor.WHITE + "" + ChatColor.BOLD + "Tỉ lệ thành công "
										+ ChatColor.GREEN + s + ChatColor.LIGHT_PURPLE + "%");

								am6holo3 = (ArmorStand) player.getWorld().spawnEntity(zxc, EntityType.ARMOR_STAND);
								am6holo3.setVisible(false);
								am6holo3.setGravity(false);
								am6holo3.setCustomNameVisible(true);
								am6holo3.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "Tỉ lệ thất bại "
										+ ChatColor.RED + d + ChatColor.LIGHT_PURPLE + "%");
								am6holo1.setMetadata("solar.upgrade.armorstand",
										new FixedMetadataValue(Main.getMain(), true));
								am6holo1.setMetadata("solar.upgrade.armorstand.tier",
										new FixedMetadataValue(Main.getMain(), "LEGENDARY"));
								am6holo1.setMetadata("solar.upgrade.armorstand.s",
										new FixedMetadataValue(Main.getMain(), s));

								am6holo2.setMetadata("solar.upgrade.armorstand",
										new FixedMetadataValue(Main.getMain(), true));
								am6holo2.setMetadata("solar.upgrade.armorstand.tier",
										new FixedMetadataValue(Main.getMain(), "LEGENDARY"));
								am6holo2.setMetadata("solar.upgrade.armorstand.s",
										new FixedMetadataValue(Main.getMain(), s));

								am6holo3.setMetadata("solar.upgrade.armorstand",
										new FixedMetadataValue(Main.getMain(), true));
								am6holo3.setMetadata("solar.upgrade.armorstand.tier",
										new FixedMetadataValue(Main.getMain(), "LEGENDARY"));
								am6holo3.setMetadata("solar.upgrade.armorstand.s",
										new FixedMetadataValue(Main.getMain(), s));

								am2.setMetadata("solar.upgrade.armorstand.owner",
										new FixedMetadataValue(Main.getMain(), player.getUniqueId()));
								am2holo1.setMetadata("solar.upgrade.armorstand.owner",
										new FixedMetadataValue(Main.getMain(), player.getUniqueId()));
								am2holo2.setMetadata("solar.upgrade.armorstand.owner",
										new FixedMetadataValue(Main.getMain(), player.getUniqueId()));
								am2holo3.setMetadata("solar.upgrade.armorstand.owner",
										new FixedMetadataValue(Main.getMain(), player.getUniqueId()));

								am3.setMetadata("solar.upgrade.armorstand.owner",
										new FixedMetadataValue(Main.getMain(), player.getUniqueId()));
								am3holo1.setMetadata("solar.upgrade.armorstand.owner",
										new FixedMetadataValue(Main.getMain(), player.getUniqueId()));
								am3holo2.setMetadata("solar.upgrade.armorstand.owner",
										new FixedMetadataValue(Main.getMain(), player.getUniqueId()));
								am3holo3.setMetadata("solar.upgrade.armorstand.owner",
										new FixedMetadataValue(Main.getMain(), player.getUniqueId()));

								am4.setMetadata("solar.upgrade.armorstand.owner",
										new FixedMetadataValue(Main.getMain(), player.getUniqueId()));
								am4holo1.setMetadata("solar.upgrade.armorstand.owner",
										new FixedMetadataValue(Main.getMain(), player.getUniqueId()));
								am4holo2.setMetadata("solar.upgrade.armorstand.owner",
										new FixedMetadataValue(Main.getMain(), player.getUniqueId()));
								am4holo3.setMetadata("solar.upgrade.armorstand.owner",
										new FixedMetadataValue(Main.getMain(), player.getUniqueId()));

								am5.setMetadata("solar.upgrade.armorstand.owner",
										new FixedMetadataValue(Main.getMain(), player.getUniqueId()));
								am5holo1.setMetadata("solar.upgrade.armorstand.owner",
										new FixedMetadataValue(Main.getMain(), player.getUniqueId()));
								am5holo2.setMetadata("solar.upgrade.armorstand.owner",
										new FixedMetadataValue(Main.getMain(), player.getUniqueId()));
								am5holo3.setMetadata("solar.upgrade.armorstand.owner",
										new FixedMetadataValue(Main.getMain(), player.getUniqueId()));

								am6.setMetadata("solar.upgrade.armorstand.owner",
										new FixedMetadataValue(Main.getMain(), player.getUniqueId()));
								am6holo1.setMetadata("solar.upgrade.armorstand.owner",
										new FixedMetadataValue(Main.getMain(), player.getUniqueId()));
								am6holo2.setMetadata("solar.upgrade.armorstand.owner",
										new FixedMetadataValue(Main.getMain(), player.getUniqueId()));
								am6holo3.setMetadata("solar.upgrade.armorstand.owner",
										new FixedMetadataValue(Main.getMain(), player.getUniqueId()));
								amlist.addAll(Arrays.asList(new ArmorStand[] { am2, am3, am4, am5, am6, am2holo1,
										am3holo1, am4holo1, am5holo1 }));
								Bukkit.getScheduler().runTaskAsynchronously(Main.getMain(), new Runnable() {

									@Override
									public void run() {
										for (Player on : Bukkit.getServer().getOnlinePlayers()) {
											if (on == player)
												continue;
											PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(
													am.getEntityId());
											((CraftPlayer) on).getHandle().playerConnection.sendPacket(packet);

											packet = new PacketPlayOutEntityDestroy(am2.getEntityId());
											((CraftPlayer) on).getHandle().playerConnection.sendPacket(packet);
											packet = new PacketPlayOutEntityDestroy(am2holo1.getEntityId());
											((CraftPlayer) on).getHandle().playerConnection.sendPacket(packet);
											packet = new PacketPlayOutEntityDestroy(am2holo2.getEntityId());
											((CraftPlayer) on).getHandle().playerConnection.sendPacket(packet);
											packet = new PacketPlayOutEntityDestroy(am2holo3.getEntityId());
											((CraftPlayer) on).getHandle().playerConnection.sendPacket(packet);

											packet = new PacketPlayOutEntityDestroy(am3.getEntityId());
											((CraftPlayer) on).getHandle().playerConnection.sendPacket(packet);
											packet = new PacketPlayOutEntityDestroy(am3holo1.getEntityId());
											((CraftPlayer) on).getHandle().playerConnection.sendPacket(packet);
											packet = new PacketPlayOutEntityDestroy(am3holo2.getEntityId());
											((CraftPlayer) on).getHandle().playerConnection.sendPacket(packet);
											packet = new PacketPlayOutEntityDestroy(am3holo3.getEntityId());
											((CraftPlayer) on).getHandle().playerConnection.sendPacket(packet);

											packet = new PacketPlayOutEntityDestroy(am4.getEntityId());
											((CraftPlayer) on).getHandle().playerConnection.sendPacket(packet);
											packet = new PacketPlayOutEntityDestroy(am4holo1.getEntityId());
											((CraftPlayer) on).getHandle().playerConnection.sendPacket(packet);
											packet = new PacketPlayOutEntityDestroy(am4holo2.getEntityId());
											((CraftPlayer) on).getHandle().playerConnection.sendPacket(packet);
											packet = new PacketPlayOutEntityDestroy(am4holo3.getEntityId());
											((CraftPlayer) on).getHandle().playerConnection.sendPacket(packet);

											packet = new PacketPlayOutEntityDestroy(am5.getEntityId());
											((CraftPlayer) on).getHandle().playerConnection.sendPacket(packet);
											packet = new PacketPlayOutEntityDestroy(am5holo1.getEntityId());
											((CraftPlayer) on).getHandle().playerConnection.sendPacket(packet);
											packet = new PacketPlayOutEntityDestroy(am5holo2.getEntityId());
											((CraftPlayer) on).getHandle().playerConnection.sendPacket(packet);
											packet = new PacketPlayOutEntityDestroy(am5holo3.getEntityId());
											((CraftPlayer) on).getHandle().playerConnection.sendPacket(packet);

											packet = new PacketPlayOutEntityDestroy(am6.getEntityId());
											((CraftPlayer) on).getHandle().playerConnection.sendPacket(packet);
											packet = new PacketPlayOutEntityDestroy(am6holo1.getEntityId());
											((CraftPlayer) on).getHandle().playerConnection.sendPacket(packet);
											packet = new PacketPlayOutEntityDestroy(am6holo2.getEntityId());
											((CraftPlayer) on).getHandle().playerConnection.sendPacket(packet);
											packet = new PacketPlayOutEntityDestroy(am6holo3.getEntityId());
											((CraftPlayer) on).getHandle().playerConnection.sendPacket(packet);

										}

									}
								});

							}

							double x = Math.cos(p2 + i2) * ra;
							double y = -(height);
							double z = Math.sin(p2 + i2) * ra;

							zxc.subtract(x, y, z);

							if (counter < 96) {
								am2.teleport(zxc);
								am2holo3.teleport(zxc.clone().add(0, 0.2, 0));
								am2holo2.teleport(zxc.clone().add(0, 0.4, 0));
								am2holo1.teleport(zxc.clone().add(0, 0.6, 0));

								ParticleEffect.REDSTONE.sendColor(Arrays.asList(new Player[] { player }),
										zxc.clone().add(0.2, 1.3, 0), Color.GRAY);
							}
							if (counter < 90) {
								am3.teleport(zxc);
								am3holo3.teleport(zxc.clone().add(0, 0.2, 0));
								am3holo2.teleport(zxc.clone().add(0, 0.4, 0));
								am3holo1.teleport(zxc.clone().add(0, 0.6, 0));

								ParticleEffect.REDSTONE.sendColor(Arrays.asList(new Player[] { player }),
										zxc.clone().add(0.2, 1.3, 0), Color.GREEN);
							}
							if (counter < 84) {
								am4.teleport(zxc);
								am4holo3.teleport(zxc.clone().add(0, 0.2, 0));
								am4holo2.teleport(zxc.clone().add(0, 0.4, 0));
								am4holo1.teleport(zxc.clone().add(0, 0.6, 0));

								ParticleEffect.REDSTONE.sendColor(Arrays.asList(new Player[] { player }),
										zxc.clone().add(0.2, 1.3, 0), Color.BLUE);
							}
							if (counter < 78) {
								am5.teleport(zxc);
								am5holo3.teleport(zxc.clone().add(0, 0.2, 0));
								am5holo2.teleport(zxc.clone().add(0, 0.4, 0));
								am5holo1.teleport(zxc.clone().add(0, 0.6, 0));

								ParticleEffect.REDSTONE.sendColor(Arrays.asList(new Player[] { player }),
										zxc.clone().add(0.2, 1.3, 0), Color.RED);
							}
							if (counter < 72) {
								am6.teleport(zxc);
								am6holo3.teleport(zxc.clone().add(0, 0.2, 0));
								am6holo2.teleport(zxc.clone().add(0, 0.4, 0));
								am6holo1.teleport(zxc.clone().add(0, 0.6, 0));
								ParticleEffect.REDSTONE.sendColor(Arrays.asList(new Player[] { player }),
										zxc.clone().add(0.2, 1.3, 0), Color.YELLOW);

							}
							// ParticleEffect.FLAME.send(Arrays.asList(new
							// Player[] { player }),
							// zxc.clone().add(0.2, 1.3, 0), 0, 0, 0, 0, 1);
							zxc.add(x, y, z);
							counter++;
							if (counter % 32 == 0) {
								ra += 2;
							}
							height += 0.02;
							i2 += Math.PI / 16;

						}
						if (amloc.distance(beaconloc) < 0.1) {
							if (am.getRightArmPose() != new EulerAngle(Math.toRadians(-100), Math.toRadians(-40), 0))
								am.setRightArmPose(new EulerAngle(Math.toRadians(-100), Math.toRadians(-40), 0));
							asd.setPitch(0);
							asd.setYaw(0);

							double x = Math.cos(p + i);
							double y = 0;
							double z = Math.sin(p + i);

							Location oldloc = asd.clone();

							asd.subtract(x, y, z);
							asd.setDirection(asd.toVector().subtract(oldloc.toVector()).multiply(-1));
							am.teleport(asd);

							ParticleEffect.ENCHANTMENT_TABLE.send(Arrays.asList(new Player[] { player }),
									asd.clone().add(0.2, 1.3, 0), 0, 0, 0, 1, 5);

							asd.add(x, y, z);

							i += Math.PI / 32;
							if (!xoay) {
								xoay = true;
							}

						} else {
							amloc.add(v);
							am.teleport(amloc);
							ParticleEffect.FLAME.send(Arrays.asList(new Player[] { player }),
									amloc.clone().add(0.2, 1.3, 0), 0, 0, 0, 0, 1);
						}
					}

				}.runTaskTimer(Main.getMain(), 0, 1);

				e.getItemDrop().remove();

			} else {

				e.setCancelled(true);
				player.sendMessage(ChatColor.RED + "Vật phẩm của bạn chưa đầy năng lượng!!");
				Util.sendTitleBar(player, ChatColor.RED + "Thất Bại!",
						ChatColor.RED + "Vật phẩm của bạn chưa đầy năng lượng!!", 5, 5);
			}
		}
	}

	@EventHandler
	public void donot(PlayerInteractAtEntityEvent e) {
		if (!e.getRightClicked().getMetadata("solar.upgrade.armorstand").isEmpty()) {
			e.setCancelled(true);
		}
		if (!e.getRightClicked().getMetadata("solar.upgrade.armorstand.mid").isEmpty()) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void touch(PlayerAnimationEvent e) {

		final Player player = e.getPlayer();
		if (!isEnchanting(player.getUniqueId())) {
			return;
		}
		if(!delay.containsKey(player.getUniqueId()) || ( (System.currentTimeMillis() - delay.get(player.getUniqueId())) > 500 ) ){
			delay.put(player.getUniqueId(), System.currentTimeMillis());
		}else{
			return;
		}
		Vector v = player.getLocation().getDirection().normalize();
		Location l = player.getLocation();
		int s = 0;
		PEnchantTier tier = null;
		boolean brk = false;
		ItemStack is = null;
		Location enl = null;
		for (int i = 0; i < 7; i++) {
			if (brk)
				break;
			l.add(v);
			for (Entity en : l.getWorld().getNearbyEntities(l, 1, 1, 1)) {
				if (en instanceof ArmorStand) {

					if (!en.getMetadata("solar.upgrade.armorstand").isEmpty()) {

						if (en.getMetadata("solar.upgrade.armorstand.owner").get(0).asString()
								.equals(player.getUniqueId().toString())) {

							s = en.getMetadata("solar.upgrade.armorstand.s").get(0).asInt();
							tier = PEnchantTier
									.valueOf(en.getMetadata("solar.upgrade.armorstand.tier").get(0).asString());
							ParticleEffect.SPELL_WITCH.send(Arrays.asList(new Player[] { player }),
									l.clone().add(0.2, 1.3, 0), 0, 0, 0, 1, 10);

							if (((ArmorStand) en).hasArms()) {
								if (((ArmorStand) en).getItemInHand().getType() != Material.AIR) {
									is = ((ArmorStand) en).getItemInHand();
									brk = true;
									enl = en.getLocation();
									break;
								}
							}

						}

					}
				}
			}
		}
		if (tier == null)
			return;
		ArrayList<PEnchantTier> tierlist = new ArrayList<>();
		for (PEnchant penchant : PEnchant.values()) {
			if (Arrays.asList(penchant.getListItem()).contains(is.getType())) {
				tierlist.add(penchant.getTier());
			}
		}
		if (!tierlist.contains(tier)) {
			player.sendMessage(ChatColor.AQUA + "Vật phẩm này không có phù phép với cấp độ " + tier.getNameColor()
					+ tier.getName() + ChatColor.AQUA + " Hãy chọn cấp độ khác");
			return;
		}
		if (!brk)
			return;
		for (Entity en : l.getWorld().getNearbyEntities(l, 15, 15, 15)) {

			if (en instanceof ArmorStand) {
				if (!en.getMetadata("solar.upgrade.armorstand.owner").isEmpty()) {
					if (en.getMetadata("solar.upgrade.armorstand.owner").get(0).asString()
							.equals(player.getUniqueId().toString())) {
						if (!en.getMetadata("solar.upgrade.armorstand").isEmpty()) {
							en.remove();
						}
						if (!en.getMetadata("solar.upgrade.armorstand.mid").isEmpty()) {
							en.remove();
						}
					}
				}
			}
		}
		Block b = null;

		for (Block be : Util.getNearbyBlocks(enl, 10)) {
			if (be.getType() == Material.BEACON) {
				for (Block po : Util.getNearbyBlocks(be.getLocation(), 1)) {
					if (po.getType() == Material.ENDER_PORTAL) {
						b = be;
						break;
					}
				}

			}
		}

		String str = "";
		boolean result = true;
		int retry = 0;
		int multiply = 1;
		if(is.getType().toString().contains("WOOD")){
			multiply = 1;
		}else if(is.getType().toString().contains("STONE")){
			multiply = 2;

		}else if(is.getType().toString().contains("IRON")){
			multiply = 3;
		}else if(is.getType().toString().contains("GOLD")){
			multiply = 4;
		}else if(is.getType().toString().contains("DIAMOND")){
			multiply = 5;
		}else if(is.getType().toString().contains("BOW")){
			multiply = 5;
		}
		while (result) {
			retry++;
			if (retry >= 10) {
				str = ChatColor.RED + "Không thể phù phép thêm ở cấp độ " + tier.getNameColor() + tier.getName();
				break;
			}
			if (Util.percentRoll(s)) {
				ArrayList<PEnchant> rad = new ArrayList<>();
				for (PEnchant enc : PEnchant.values()) {
					if (enc.getTier() == tier) {
						if (Arrays.asList(enc.getToolList().getItemList()).contains(is.getType())) {
							rad.add(enc);
						}

					}
				}
				PEnchant enchant = rad.get(ThreadLocalRandom.current().nextInt(0, rad.size()));
				if (enchant.hasEnchant(is)) {
					if (enchant.getLevel(is) < enchant.getMaxLevel() * multiply) {
						enchant.apply(is, enchant.getLevel(is) + 1);
						player.sendMessage(ChatColor.YELLOW + "Nâng cấp thành công , Phù phép "
								+ enchant.getTier().getNameColor() + enchant.getName() + ChatColor.GREEN
								+ " lên cấp độ " + enchant.getTier().getLevelColor() + ""
								+ Util.convertToRoman(enchant.getLevel(is)));
						str = enchant.getTier().getNameColor() + enchant.getName() + "  "
								+ enchant.getTier().getLevelColor() + "" + Util.convertToRoman(enchant.getLevel(is));
						result = false;
					}
				} else {
					enchant.apply(is, 1);
					player.sendMessage(ChatColor.YELLOW + "Nâng cấp thành công ,nhận được Phù phép "
							+ enchant.getTier().getNameColor() + enchant.getName());
					str = enchant.getTier().getNameColor() + enchant.getName() + "  "
							+ enchant.getTier().getLevelColor() + "" + Util.convertToRoman(enchant.getLevel(is));
					result = false;
				}

			} else {
				player.sendMessage(ChatColor.RED + "Nâng cấp thật bại , bạn không nhận được phù phép");
				str = ChatColor.RED + "Thất bại " + ChatColor.YELLOW + " :((";
				result = false;
			}
		}
		lastParticle(is, b, player, str);

	}

	private void lastParticle(ItemStack is, Block b, final Player player, String str) {

		Location l = b.getLocation().add(0.5, 0, 0.5);
		Location sk = l.clone().add(0, 5, 0);

		ArmorStand am = (ArmorStand) l.getWorld().spawnEntity(sk, EntityType.ARMOR_STAND);
		am.setVisible(false);
		am.setGravity(false);
		am.setArms(true);
		am.setRightArmPose(new EulerAngle(Math.toRadians(-100), Math.toRadians(-90), 0));
		am.setItemInHand(is);
		am.setMetadata("solar.upgrade.armorstand.mid", new FixedMetadataValue(Main.getMain(), true));

		ArmorStand amholo1 = (ArmorStand) l.getWorld().spawnEntity(sk.clone().subtract(0, 0.2, 0),
				EntityType.ARMOR_STAND);
		amholo1.setVisible(false);
		amholo1.setGravity(false);
		amholo1.setCustomNameVisible(true);
		amholo1.setMetadata("solar.upgrade.armorstand.mid", new FixedMetadataValue(Main.getMain(), true));
		if (is.hasItemMeta()) {
			if (is.getItemMeta().hasDisplayName()) {
				amholo1.setCustomName(is.getItemMeta().getDisplayName());
			} else {
				amholo1.setCustomName(ChatColor.AQUA + player.getName());
			}
		} else {
			amholo1.setCustomName(ChatColor.AQUA + player.getName());
		}
		ArmorStand amholo2 = (ArmorStand) l.getWorld().spawnEntity(sk.clone().subtract(0, 0.4, 0),
				EntityType.ARMOR_STAND);
		amholo2.setVisible(false);
		amholo2.setGravity(false);
		amholo2.setCustomNameVisible(true);
		amholo2.setCustomName(str);
		amholo2.setMetadata("solar.upgrade.armorstand.mid", new FixedMetadataValue(Main.getMain(), true));
		am.setMetadata("solar.upgrade.armorstand.owner", new FixedMetadataValue(Main.getMain(), player.getUniqueId()));
		amholo1.setMetadata("solar.upgrade.armorstand.owner",
				new FixedMetadataValue(Main.getMain(), player.getUniqueId()));
		amholo2.setMetadata("solar.upgrade.armorstand.owner",
				new FixedMetadataValue(Main.getMain(), player.getUniqueId()));
		new BukkitRunnable() {
			double i3 = 0;
			double p3 = Math.PI / 32;
			double ys = 0;
			int c;

			@Override
			public void run() {

				if (!player.isOnline()) {
					enchanting.put(player.getUniqueId(), is);

					for (Entity en : l.getWorld().getNearbyEntities(l, 3, 3, 3)) {

						if (en instanceof ArmorStand) {
							if (!en.getMetadata("solar.upgrade.armorstand.owner").isEmpty()) {
								if (en.getMetadata("solar.upgrade.armorstand.owner").get(0).asString()
										.equals(player.getUniqueId().toString())) {
									if (!en.getMetadata("solar.upgrade.armorstand").isEmpty()) {
										en.remove();
									}
									if (!en.getMetadata("solar.upgrade.armorstand.mid").isEmpty()) {
										en.remove();
									}
								}
							}
						}
					}
					this.cancel();
				}
				if (amholo1.getLocation().distance(l) < 1) {
					if (c <= 1) {
						ParticleEffect.LAVA.send(Arrays.asList(new Player[] { player }),
								amholo1.getLocation().clone().add(0, 2, 0), 0, 0, 0, 2, 50);
						ParticleEffect.FLAME.send(Arrays.asList(new Player[] { player }),
								amholo1.getLocation().clone().add(0, 2, 0), 0, 0, 0, 2, 50);
					}
					if (c > 100) {
						if (isEnchanting(player.getUniqueId()) && player.isOnline()) {
							enchanting.remove(player.getUniqueId());

							player.getInventory().addItem(is);
						}
						for (Entity en : l.getWorld().getNearbyEntities(l, 3, 3, 3)) {

							if (en instanceof ArmorStand) {
								if (!en.getMetadata("solar.upgrade.armorstand.owner").isEmpty()) {
									if (en.getMetadata("solar.upgrade.armorstand.owner").get(0).asString()
											.equals(player.getUniqueId().toString())) {
										if (!en.getMetadata("solar.upgrade.armorstand").isEmpty()) {
											en.remove();
										}
										if (!en.getMetadata("solar.upgrade.armorstand.mid").isEmpty()) {
											en.remove();
										}
									}
								}
							}
						}
						this.cancel();
					}
					c++;
				}
				if (am.getRightArmPose() != new EulerAngle(Math.toRadians(-100), Math.toRadians(-40), 0))
					am.setRightArmPose(new EulerAngle(Math.toRadians(-100), Math.toRadians(-40), 0));
				sk.setPitch(0);
				sk.setYaw(0);

				double x = Math.cos(p3 + i3) * (0.42);
				double y = ys;
				double z = Math.sin(p3 + i3) * (0.42);

				Location oldlz2 = sk.clone();

				sk.subtract(x, y, z);
				sk.setDirection(sk.toVector().subtract(oldlz2.toVector()).multiply(-1));
				am.teleport(sk);

				sk.add(x, y, z);
				Location zxc = amholo1.getLocation();
				zxc.setY(am.getLocation().getY() + 0.4);
				amholo1.teleport(zxc);
				Location ccc = amholo2.getLocation();
				ccc.setY(am.getLocation().getY() + 0.2);
				amholo2.teleport(ccc);

				i3 += Math.PI / 32;
				if (c <= 1) {
					ys += 0.2;
				}

			}
		}.runTaskTimer(Main.getMain(), 0, 1);

	}

	public static HashMap<UUID, ItemStack> getEnchantingPlayers() {
		return enchanting;
	}

}
