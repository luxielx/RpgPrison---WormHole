package Enchantment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Utils.Util;
import net.md_5.bungee.api.ChatColor;

public class ExpBooster implements Listener {
	public static HashMap<UUID, Long> Multiplying = new HashMap<>();
	public static HashMap<UUID, Long> gb = new HashMap<>();
	public static ArrayList<UUID> gbqueue = new ArrayList<>();
//3600000
	public static boolean IsGlobalBoosting() {
		if (gb.isEmpty())return false;		
		return true;
	}

	public static UUID gbBooster() {
		if (IsGlobalBoosting()) {
			for (UUID u : gb.keySet()) {
				return u;
			}
		} else {
			return null;
		}
		return null;
	}
	public static void gbUpdate(){
		if(gb == null) return;
		if(gbqueue == null) return;
		if(gb.isEmpty()){
			if(!gbqueue.isEmpty()){
				gb.put(gbqueue.get(0), System.currentTimeMillis() + 3600000);
				gbqueue.remove(0);
			}
		}else{
			
			for(Long z : gb.values()){
				if(z < System.currentTimeMillis()){
					gb.clear();
					break;
				}
			}
		}
	}
	public static String Globaltimeleft() {
		if(IsGlobalBoosting()){
			for(Long z : gb.values()){
				return Util.convertMiliToMinute(z - System.currentTimeMillis());					
				}
			}
		return "Không có ai boost cả";
			
		
		
	}
	public static void addBooster(UUID u){
		gbqueue.add(u);
	}

	public static ItemStack getExpBottle() {
		ItemStack is = new ItemStack(Material.EXP_BOTTLE);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.YELLOW + "x" + 2 + " Kinh nghiệm");
		im.setLore(Arrays.asList(new String[] { Util.hideS("solar.bottle"),
				ChatColor.BLUE + "Dùng chuột phải để x" + 2 + " kinh nghiệm trong vòng 1 giờ", }));
		is.setItemMeta(im);
		return is;
	}

	public static boolean isExpBottleItem(ItemStack is) {
		if (is == null)
			return false;
		if (is.getType() == Material.AIR)
			return false;

		if (!is.hasItemMeta())
			return false;
		if (!is.getItemMeta().hasLore())
			return false;
		for (String s : is.getItemMeta().getLore()) {
			if (s.contains(Util.hideS("solar.bottle"))) {
				return true;
			}
		}
		return false;

	}

	public static boolean multiornah(OfflinePlayer player) {
		if (Multiplying.containsKey(player.getUniqueId())) {
			if (System.currentTimeMillis() >= Multiplying.get(player.getUniqueId())) {
				Multiplying.remove(player.getUniqueId());
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public static void add1Hour(Player player) {
		if (Multiplying.containsKey(player.getUniqueId())) {
			if (System.currentTimeMillis() >= Multiplying.get(player.getUniqueId())) {
				Multiplying.put(player.getUniqueId(), System.currentTimeMillis() + 3600000);

			}
		} else {
			Multiplying.put(player.getUniqueId(), System.currentTimeMillis() + 3600000);
		}
	}

	public static String timeleft(OfflinePlayer player) {
		update(player);
		if (!Multiplying.containsKey(player.getUniqueId()))
			return "Hết hạn";
		return Util.convertMiliToMinute(Multiplying.get(player.getUniqueId()) - System.currentTimeMillis());
	}

	public static void update(OfflinePlayer player) {
		if (Multiplying.containsKey(player.getUniqueId())) {
			if (System.currentTimeMillis() >= Multiplying.get(player.getUniqueId())) {
				Multiplying.remove(player.getUniqueId());
			}
		}
	}

	@EventHandler
	public void rc(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player player = e.getPlayer();
			if (player.getItemInHand().getType() == Material.EXP_BOTTLE) {
				ItemStack a = player.getItemInHand();
				ItemStack b = a.clone();
				b.setAmount(1);
				if (isExpBottleItem(b)) {
					e.setCancelled(true);

					if (Multiplying.containsKey(player.getUniqueId())) {
						if (System.currentTimeMillis() >= Multiplying.get(player.getUniqueId())) {
							player.getInventory().removeItem(b);
							add1Hour(player);
							return;
						} else {
							player.sendMessage(ChatColor.RED + "Bình nhân kinh nghiệm trước của bạn chưa hết , Còn lại "
									+ Util.convertMiliToMinute(
											Multiplying.get(player.getUniqueId()) - System.currentTimeMillis()));
							player.updateInventory();
							return;
						}

					} else {
						player.getInventory().removeItem(b);
						add1Hour(player);
						return;
					}
				}

			}

		}
	}
}
