package Menus;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Enchantment.PEnchant;
import Utils.Glow;
import Utils.ToolList;
import net.md_5.bungee.api.ChatColor;

public class EnchantslistGUI implements Listener {
	public static void openEnchant(Player player) {
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.AQUA + "Phù phép");
		int index = 0;
		for(ToolList tool : ToolList.values()){
			inv.setItem(index, createRepresent(tool));
			index++;
		}

		player.openInventory(inv);
	}
	public static void openEnchantList(ToolList tool , Player player ){
		Inventory inv = Bukkit.createInventory(null, 54 , ChatColor.RED + "Phù phép");
		int index = 0;
		for(PEnchant pe : PEnchant.values()){
			if(pe.getToolList() == tool){
				inv.setItem(index, getItem(pe));
				index++;
			}
		}
		player.openInventory(inv);
	}
	private static ItemStack createRepresent(ToolList tool){
		ItemStack is = new ItemStack(tool.getRepresent());
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + tool.getName());
		im.setLore(Arrays.asList(new String[] {ChatColor.AQUA + "Bấm vào để xem các phù phép của " + ChatColor.RED + tool.getName()}));
		
		is.setItemMeta(im);
		return is;
	}
	private static ItemStack getItem(PEnchant pe) {
		ItemStack is = new ItemStack( pe.getToolList().getRepresent());
		ItemMeta im = is.getItemMeta();
		String des = pe.getDescription();
		String[] spl = des.split(" ");
		String zz = spl[spl.length/2];
		String[] spl2 = des.split(zz);
		String	s1a = spl2[0];
		String	s1b = spl2[1];
		im.setDisplayName(pe.getTier().getNameColor() + pe.getName());
		im.setLore(Arrays.asList(new String[] {" " + pe.getTier().getLevelColor() + s1a  + zz , pe.getTier().getLevelColor() + s1b  , ChatColor.RED + "Cấp độ cao nhất " + ChatColor.YELLOW + "" + pe.getMaxLevel()*5}));
		im.addEnchant(new Glow(200), 1, true);
		is.setItemMeta(im);
		return is;
	}
	
	
	@EventHandler
	public void clickevent(InventoryClickEvent e){
		if(e.getClickedInventory() == null) return;
		if(e.getCurrentItem().getType() == Material.AIR) return;
		if(e.getClickedInventory().getTitle().contains(ChatColor.AQUA + "Phù phép")){
			e.setCancelled(true);
			for(ToolList tool : ToolList.values()){
				if(tool.getRepresent() == e.getCurrentItem().getType()){
					openEnchantList(tool, (Player) e.getWhoClicked());
					break;
				}
			}
			
		}
		if(e.getClickedInventory().getTitle().contains(ChatColor.RED + "Phù phép")){
			e.setCancelled(true);
			Player player = (Player) e.getWhoClicked();
			if(player.hasPermission("prison.admin")){
				if(e.getCurrentItem().hasItemMeta()){
					player.getInventory().addItem(PEnchant.getEnchantFromName(e.getCurrentItem().getItemMeta().getDisplayName()).getBook(1));
				}
			}
			
		}
	}
	
	
}
