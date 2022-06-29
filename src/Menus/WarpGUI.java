package Menus;

import Listeners.Teleportation;
import Solar.prison.Main;
import Solar.prison.Prisoner;
import Utils.Glow;
import Utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Created by ADMIN on 1/14/2018.
 */
public class WarpGUI implements Listener {
    public static void openWarpGui(Player p) {
        Inventory inv = Bukkit.createInventory(new HolderZ(), 54, ChatColor.LIGHT_PURPLE + "Du lịch");
        Prisoner pr = Main.getMain().getPrisoners().get(p.getUniqueId());

        int index = 0;
        for (int i = 1; i <= 43; i++) {
            ItemStack is;
            if (i * 2.5 <= pr.getLevel()) {
                is = new ItemStack(Material.DIAMOND_PICKAXE);
            } else {
                is = new ItemStack(Material.BARRIER);

            }

            ItemMeta im = is.getItemMeta();
            im.setDisplayName(ChatColor.RED + "Khu đào " + ChatColor.GOLD + Util.convertToRoman(i));
            if (i <= 40) {
                if (i == 1) {
                    im.setLore(Arrays.asList(new String[]{ChatColor.BLUE + "Khu mỏ dành cho người có cấp độ " + ChatColor.GREEN + 0 + "" + ChatColor.BLUE + " Trở lên"}));
                    is.setType(Material.IRON_PICKAXE);

                } else {
                    im.setLore(Arrays.asList(new String[]{ChatColor.BLUE + "Khu mỏ dành cho người có cấp độ " + ChatColor.GREEN + (int) (i * 2.5) + "" + ChatColor.BLUE + " Trở lên"}));

                }
            } else {
                if (i == 41) {
                    if (!p.hasPermission("prison.vip1")) {
                        is.setType(Material.BARRIER);
                    } else {
                        is.setType(Material.GOLD_PICKAXE);
                        im.setLore(Arrays.asList(new String[]{ChatColor.AQUA + "Khu mỏ dành cho người có rank " + ChatColor.YELLOW + "Obsidian " + ChatColor.AQUA + "trở lên"}));
                    }
                }
                if (i == 42) {
                    if (!p.hasPermission("prison.vip2")) {
                        is.setType(Material.BARRIER);
                    } else {
                        is.setType(Material.GOLD_PICKAXE);
                        im.setLore(Arrays.asList(new String[]{ChatColor.BLUE + "Khu mỏ dành cho người có rank " + ChatColor.AQUA + "Lapis " + ChatColor.BLUE + "trở lên"}));
                    }
                }
                if (i == 43) {
                    if (!p.hasPermission("prison.vip3")) {
                        is.setType(Material.BARRIER);
                    } else {
                        is.setType(Material.GOLD_PICKAXE);
                        im.setLore(Arrays.asList(new String[]{ChatColor.BLUE + "Khu mỏ dành cho người có rank " + ChatColor.RED + "Redstone " + ChatColor.BLUE + "trở lên"}));
                    }
                }
            }
            im.addEnchant(new Glow(200), 1, true);
            is.setItemMeta(im);
            inv.setItem(index, is);
            index++;

        }
        p.openInventory(inv);
    }

    @EventHandler
    public void click(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType() == Material.AIR) return;
        if (e.getClickedInventory().getHolder() instanceof HolderZ) {
            Player player = (Player) e.getWhoClicked();
            Prisoner pr = Main.getMain().getPrisoners().get(player.getUniqueId());
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Khu đào")) {

                String w = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().replace("Khu đào", "").replace(" ", ""));
                if (e.getCurrentItem().getType() == Material.BARRIER) {
                    e.setCancelled(true);
                    return;
                } else {
                    if (Util.romanConvert(w) == 1) {
                        Teleportation.teleportToWarp((Player) e.getWhoClicked(), w);
                        e.setCancelled(true);
                        e.getWhoClicked().closeInventory();
                        return;
                    }
                    if (Util.romanConvert(w) == 41) {
                        if (player.hasPermission("prison.vip1")) {
                            Teleportation.teleportToWarp((Player) e.getWhoClicked(), w);
                        }
                        e.setCancelled(true);
                        e.getWhoClicked().closeInventory();
                        return;
                    }
                    if (Util.romanConvert(w) == 42) {
                        if (player.hasPermission("prison.vip2")) {
                            Teleportation.teleportToWarp((Player) e.getWhoClicked(), w);

                        }
                        e.setCancelled(true);
                        e.getWhoClicked().closeInventory();
                        return;
                    }
                    if (Util.romanConvert(w) == 43) {
                        if (player.hasPermission("prison.vip3")) {
                            Teleportation.teleportToWarp((Player) e.getWhoClicked(), w);

                        }
                        e.setCancelled(true);
                        e.getWhoClicked().closeInventory();
                        return;
                    }
                    if ((Util.romanConvert(w)) * 2.5 <= pr.getLevel())
                        Teleportation.teleportToWarp((Player) e.getWhoClicked(), w);

                    e.setCancelled(true);
                    e.getWhoClicked().closeInventory();
                }
            }
        }
    }
}

class HolderZ implements InventoryHolder {

    @Override
    public Inventory getInventory() {
        return null;
    }
}
