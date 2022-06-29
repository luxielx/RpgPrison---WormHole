package Listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import Menus.EnchantCombine;
import Menus.QLPP;

public class MiscLis implements Listener{
	@EventHandler
	public void touch(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
			Player player = e.getPlayer();
			if(e.getClickedBlock().getType() == Material.WORKBENCH){
				
				e.setCancelled(true);
				EnchantCombine.openEnchant(player);
				
			}
			if(e.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE){
				e.setCancelled(true);
				QLPP.openEnchant(player);
			}
		}
	}
	

}
