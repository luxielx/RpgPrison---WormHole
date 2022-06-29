package Listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.inventivetalent.particle.ParticleEffect;

import Enchantment.Energy;
import Solar.prison.Main;
import Utils.Util;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.ChatColor;

public class NPCTouch implements Listener {
	@EventHandler
	public void touch(NPCRightClickEvent e) {
		Player player = e.getClicker();
		NPC npc = e.getNPC();
		if (npc.getName().contains(ChatColor.RED + "Phân Giải")) {
			if(!player.isSneaking()){
				player.sendMessage(ChatColor.GREEN + "Cần phải giữ nút shift để phân giải");
				return;
			}
			ItemStack is = player.getItemInHand();
			if (Energy.hasEnergy(is)) {
				player.setItemInHand(new ItemStack(Material.AIR));
				ArmorStand am = (ArmorStand) player.getWorld().spawnEntity(npc.getStoredLocation(),	EntityType.ARMOR_STAND);
					
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
				Location lzo = am.getLocation();
				new BukkitRunnable() {
					// .add(0.2, 1.3, 0)
					double i3 = 0;
					double p3 = Math.PI / 32;
					double y = 0;
					ItemStack zz = is.clone();
					@Override
					public void run() {
						if (i3 > Math.PI * 2) {
							am.setItemInHand(Energy.getRawEnergy(Energy.getTotalEnergy(is)));
							am.setCustomName(Energy.getRawEnergy(Energy.getTotalEnergy(is)).getItemMeta().getDisplayName());
							
						}
						if(i3 > Math.PI * 4){
							
							player.getInventory().addItem(Energy.getRawEnergy(Energy.getTotalEnergy(is)));
							am.remove();
							this.cancel();
						}
						if (am.getRightArmPose() != new EulerAngle(Math.toRadians(-100), Math.toRadians(-40), 0))
							am.setRightArmPose(new EulerAngle(Math.toRadians(-100), Math.toRadians(-40), 0));
						
						Location oldlzo = lzo.clone();

						double x = Math.cos(p3 + i3) * (0.42);

						double z = Math.sin(p3 + i3) * (0.42);
						if (i3 < Math.PI) {
							y = -i3 / 2;
						}
						lzo.subtract(x, y, z);
						lzo.setDirection(lzo.toVector().subtract(oldlzo.toVector()).multiply(-1));
						am.teleport(lzo);
						if(am.getItemInHand().getType() == is.getType()){
						ParticleEffect.ITEM_CRACK.sendData(Util.getNearbyPlayer(am.getLocation(), 30d),
								am.getLocation().add(0, 1.8, 0).getX(), am.getLocation().add(0, 1.8, 0).getY(),
								am.getLocation().add(0, 1.8, 0).getZ(), 0, 0, 0, 1, 10, zz);
						}else{
							ParticleEffect.SPELL_WITCH.send(Util.getNearbyPlayer(am.getLocation(),30d), am.getLocation().add(0,1.8,0), 0, 0, 0, 0, 1, 5);
						}
						
						lzo.add(x, y, z);
						i3 += Math.PI / 32;
					}
				}.runTaskTimer(Main.getMain(), 0, 1);
			}

		}

	}

}
