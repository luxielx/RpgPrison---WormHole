package Listeners;

import java.util.ArrayList;

import com.earth2me.essentials.commands.WarpNotFoundException;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import Enchantment.ExpBooster;
import Enchantment.PEnchantTier;
import Enchantment.Shard;
import Solar.prison.Main;
import Solar.prison.Prisoner;
import net.md_5.bungee.api.ChatColor;

public class JoinQuitEvent implements Listener {
	@EventHandler
	public void join(PlayerLoginEvent e) {
		Prisoner Prisoner = new Prisoner(e.getPlayer());
		if (e.getResult().equals(PlayerLoginEvent.Result.ALLOWED)) {

			if (Prisoner.isExist()) {
				e.getPlayer().sendMessage(ChatColor.DARK_GREEN + "Đang tải dữ liệu của bạn");
				new BukkitRunnable() {

					@Override
					public void run() {
						Prisoner.loadData();
						e.getPlayer().sendMessage(ChatColor.DARK_GREEN + "Dữ Liệu đã được tải thành công");

					}
				}.runTaskAsynchronously(Main.getMain());

			} else {
				Prisoner.saveData();

			}

			Main.getMain().getPrisoners().put(e.getPlayer().getUniqueId(), Prisoner);

		}

	}

	@EventHandler
	public void giveItem(PlayerJoinEvent e) throws InvalidWorldException, WarpNotFoundException {
        if(!e.getPlayer().getWorld().getName().equalsIgnoreCase("Landfall")){
           e.getPlayer().teleport(Main.getEss().getWarps().getWarp("spawn"));
        }
		if (EnergyLevelup.getEnchantingPlayers().containsKey(e.getPlayer().getUniqueId())) {
			if (!e.getPlayer().getInventory()
					.contains(EnergyLevelup.getEnchantingPlayers().get(e.getPlayer().getUniqueId()))) {
				e.getPlayer().getInventory()
						.addItem(EnergyLevelup.getEnchantingPlayers().get(e.getPlayer().getUniqueId()));
			}

			EnergyLevelup.getEnchantingPlayers().remove(e.getPlayer().getUniqueId());
		}
		if(RepairItem.fixing.containsKey(e.getPlayer().getUniqueId())){
			e.getPlayer().getInventory().addItem(RepairItem.fixing.get(e.getPlayer().getUniqueId()));
			RepairItem.fixing.remove(e.getPlayer().getUniqueId());
		}
		if (!e.getPlayer().hasPlayedBefore()) {
			ItemStack is = new ItemStack(Material.COOKED_BEEF);
			is.setAmount(10);
			e.getPlayer().getInventory().addItem(is);
		}
		for (ItemStack is : e.getPlayer().getInventory().getArmorContents()) {
			if (is == null) {
				continue;
			} else {
				PvPEnchants.addEffect(e.getPlayer(), is);
			}

		}
		
		

	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Prisoner Prisoner = Main.getMain().getPrisoners().get(e.getPlayer().getUniqueId());
		for (PotionEffect effect : e.getPlayer().getActivePotionEffects()) {
			e.getPlayer().removePotionEffect(effect.getType());
		}

		new BukkitRunnable() {

			@Override
			public void run() {
				Prisoner.saveData();
				Main.getMain().getPrisoners().remove(e.getPlayer().getUniqueId());

			}
		}.runTaskAsynchronously(Main.getMain());

	}




}
