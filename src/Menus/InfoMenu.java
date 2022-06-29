package Menus;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

import Enchantment.ExpBooster;
import Solar.prison.Main;
import Solar.prison.Prisoner;
import Utils.LevelTop;
import net.md_5.bungee.api.ChatColor;

public class InfoMenu implements Listener{
	public static void openInfo(Player player , OfflinePlayer info) {
		Inventory inv = Bukkit.createInventory(new holder(), 9, ChatColor.RED + player.getName());
		Bukkit.getScheduler().runTaskAsynchronously(Main.getMain(), new Runnable() {
			
			@Override
			public void run() {
				inv.setItem(4, playerInfo(info));
				player.openInventory(inv);
				
			}
		});
		
	}

	private static ItemStack playerInfo(OfflinePlayer player) {
		Essentials ess = (Essentials) Main.getMain().getServer().getPluginManager().getPlugin("Essentials");
		Main.getMain().console.sendMessage(player.getName());
		User nickname = ess.getOfflineUser(player.getName());
		
		Prisoner Prisoner = null;
		if(player.isOnline()){ Prisoner = Main.getMain().getPrisoners().get((player).getUniqueId());
		}else{
			Prisoner = new Prisoner(player);
			Prisoner.loadData();
		}
		String gang = "";
		
//		if(GangsPlugin.getInstance().gangManager.isInGang(player)){
//			 gang = GangsPlugin.getInstance().gangManager.getPlayersGang(player).getName();
//		}
		
		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		SkullMeta skullmeta = (SkullMeta) item.getItemMeta();
		skullmeta.setOwner(player.getName());
		skullmeta.setDisplayName(ChatColor.YELLOW + player.getName());
		skullmeta.setLore(Arrays.asList(new String[] { ChatColor.RED + "Biệt danh: " + nickname.getNickname(),ChatColor.BLUE + "Băng nhóm: " +ChatColor.LIGHT_PURPLE+ gang ,
				ChatColor.GOLD + "Có thể Chat?: " + ChatColor.GREEN + nickname.getMuted(),
				ChatColor.GREEN + "Số Tiền: " +ChatColor.GREEN + "$"+ ChatColor.GOLD + Prisoner.getBalance(),
				ChatColor.AQUA + "Kinh Nghiệm: " + ChatColor.GREEN + Prisoner.getEXP(),
				ChatColor.RED + "Cấp độ: " + ChatColor.GREEN + Prisoner.getLevel(),ChatColor.BLUE +"Hạng: " +ChatColor.GREEN + LevelTop.getPlace(player.getUniqueId()) , ChatColor.RED + "Nhân đôi kinh nghiệm: " + ChatColor.BLUE + ExpBooster.timeleft(player)   }));
		item.setItemMeta(skullmeta);
		return item;
		//GangsPlusAPI.getPlayersGang(player).getName()
	}
	@EventHandler
	public void click(InventoryClickEvent e){
		if(e.getClickedInventory() == null) return;
		if(e.getCurrentItem().getType().equals(Material.AIR)) return;
		
			if(e.getClickedInventory().getHolder() instanceof  holder){
				e.setCancelled(true);
				e.getWhoClicked().closeInventory();
			}
		
	}
}
class holder implements InventoryHolder{

	@Override
	public Inventory getInventory() {
		return null;
	}
	
}