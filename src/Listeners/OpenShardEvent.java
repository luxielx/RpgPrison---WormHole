package Listeners;

import java.util.HashMap;
import java.util.UUID;

import Solar.prison.Main;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import Enchantment.PEnchantTier;
import Enchantment.Shard;
import Utils.Util;
import net.md_5.bungee.api.ChatColor;

public class OpenShardEvent implements Listener {
	HashMap<UUID , Long> delay = new HashMap<>();
	@EventHandler
	public void rightclick(PlayerInteractEvent e){
		
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(!e.getClickedBlock().getMetadata("destroy.me").isEmpty()){
				e.setCancelled(true);
				e.getClickedBlock().setType(Material.AIR);
				e.getClickedBlock().removeMetadata("destroy.me" , Main.getMain());
				for (Entity s : e.getClickedBlock().getWorld().getNearbyEntities(e.getClickedBlock().getLocation(),2,2, 2)) {
					if (s.getType() == EntityType.ARMOR_STAND) s.remove();
				}
				return;
			}
			Player player = e.getPlayer();
			if(e.getClickedBlock().getType() == Material.AIR) return;
			if(Shard.isShard(player.getItemInHand())){
				if(player.getInventory().firstEmpty() == -1){
					player.sendMessage(ChatColor.GREEN + "Hãy dọn túi đồ của bạn trước khi mở Mảnh");
					return;
				}
				if(!delay.containsKey(player.getUniqueId()) || ( (System.currentTimeMillis() - delay.get(player.getUniqueId())) > 100 ) ){
					Block b =	e.getPlayer().getWorld().getBlockAt(e.getClickedBlock().getLocation().add(0,1,0));

					if(b.getType() != Material.AIR) return;

					ItemStack shard = player.getItemInHand().clone();
					shard.setAmount(1);
					PEnchantTier tier  = Shard.getShardTier(shard);
					
					if(tier == PEnchantTier.LEGENDARY || tier == PEnchantTier.ULTIMATE){
						b.setType(Material.ENDER_CHEST);
						

					}else{
						
						for(Block z : Util.getNearbyBlocks(b.getLocation(), 1)){
							if(z.getType() != Material.AIR && z.getLocation().getY() == b.getLocation().getY()) {
								return;
							
							}
						}
						b.setType(Material.CHEST);
						
						
					}
					player.getInventory().removeItem((shard));
					
					b.setMetadata("crate", new FixedMetadataValue(Main.getMain(), true));
					
					
					
					Shard.openShard(tier, player, b);
					delay.put(player.getUniqueId(), System.currentTimeMillis());
				}
				
				
			}
		
		
		}
	}
}
