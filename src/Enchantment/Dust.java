package Enchantment;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Utils.Glow;
import Utils.Util;
import net.md_5.bungee.api.ChatColor;

public class Dust {
	public static boolean isDust(ItemStack dust) {
		if (!dust.hasItemMeta())
			return false;

		ItemMeta im = dust.getItemMeta();
		if (!im.hasLore())
			return false;

		if (im.getLore().get(im.getLore().size() - 1).equalsIgnoreCase(Util.hideS("solardust"))) {
			return true;
		}

		return false;
	}

	public static PEnchantTier getTier(ItemStack dust) {
		if (!isDust(dust)) {
			return null;
		}
		PEnchantTier tier = null;
		ItemMeta im = dust.getItemMeta();
		String name = im.getDisplayName().replaceAll(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Bột ", "");
		for(PEnchantTier z : PEnchantTier.values()){
			if(z.getName().equalsIgnoreCase(ChatColor.stripColor(name))){
				tier = z;
				break;
			}
		}
		
		return tier;
	}

	public static ItemStack getDust(PEnchantTier tier) {
		ItemStack is = new ItemStack(Material.GLOWSTONE_DUST);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Bột " + tier.getNameColor() + tier.getName());
		Glow gl = new Glow(200);
		im.addEnchant(gl, 1, true);
		im.setLore(Arrays.asList(new String[] { ChatColor.DARK_BLUE + "Ném bột này vào hố đen khi đang phù phép ",
				ChatColor.DARK_BLUE + "Để tăng thêm " + ChatColor.DARK_BLUE + "2%" + ChatColor.DARK_BLUE
						+ " tỉ lệ thành công cho cấp độ " + tier.getNameColor() + "" + ChatColor.BOLD + tier.getName(),
				Util.hideS("solardust") }));

		is.setItemMeta(im);
		return is;
	}
}
