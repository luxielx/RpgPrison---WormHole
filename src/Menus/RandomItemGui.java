package Menus;

import Enchantment.PEnchant;
import Enchantment.PEnchantTier;
import Listeners.RandomItem;
import Solar.prison.Main;
import Utils.ToolList;
import Utils.Util;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ADMIN on 2/16/2018.
 */
public class RandomItemGui implements Listener {
    public static Inventory inv = null;
    public static long resettime = 0;

    public static void openGUI(Player player) {
        if (inv == null) {
            createInv();
        }
        if (System.currentTimeMillis() - resettime >= 7200000 ) {
            createInv();
        }
        player.sendMessage(ChatColor.GREEN + "Chợ đen sẽ cập nhật hàng mới trong vòng " + Util.convertMiliToMinute(7200000  - (System.currentTimeMillis() - resettime)));

        player.openInventory(inv);
    }

    public static void createInv() {
        resettime = System.currentTimeMillis();
        Inventory iv = Bukkit.createInventory(new ZHolder(), 45, ChatColor.BLUE + "Chợ đen");
        List<Material> z = Arrays.asList(ToolList.ALL.getItemList());
        for (int i = 0; i < 45; i++) {
            if (i / 9 == 0) {
                if (Util.percentRoll(20)) {
                    PEnchant en = PEnchant.getRandomEnchant(new PEnchantTier[]{PEnchantTier.SIMPLE});
                    iv.setItem(i, SetBuyPrice(en.getBook(ThreadLocalRandom.current().nextInt(1,en.getMaxLevel()*5 +1 )), Util.percentRoll(20)));
                } else {
                    iv.setItem(i, SetBuyPrice(RandomItem.randomItem(PEnchantTier.SIMPLE, z.get(ThreadLocalRandom.current().nextInt(z.size()))), Util.percentRoll(20)));
                }
            } else if (i / 9 == 1) {
                if (Util.percentRoll(20)) {
                    PEnchant en = PEnchant.getRandomEnchant(new PEnchantTier[]{PEnchantTier.UNCOMMON});
                    iv.setItem(i, SetBuyPrice(en.getBook(ThreadLocalRandom.current().nextInt(1, en.getMaxLevel()*5 +1)), Util.percentRoll(40)));
                } else {
                    iv.setItem(i, SetBuyPrice(RandomItem.randomItem(PEnchantTier.UNCOMMON, z.get(ThreadLocalRandom.current().nextInt(z.size()))), Util.percentRoll(40)));
                }
            } else if (i / 9 == 2) {
                if (Util.percentRoll(20)) {
                    PEnchant en = PEnchant.getRandomEnchant(new PEnchantTier[]{PEnchantTier.ELITE});
                    iv.setItem(i, SetBuyPrice(en.getBook(ThreadLocalRandom.current().nextInt(1, en.getMaxLevel()*5 +1)), Util.percentRoll(80)));
                } else {
                    iv.setItem(i, SetBuyPrice(RandomItem.randomItem(PEnchantTier.ELITE, z.get(ThreadLocalRandom.current().nextInt(z.size()))), Util.percentRoll(80)));
                }
            } else if (i / 9 == 3) {
                if (Util.percentRoll(20)) {
                    PEnchant en = PEnchant.getRandomEnchant(new PEnchantTier[]{PEnchantTier.ULTIMATE});
                    iv.setItem(i, SetBuyPrice(en.getBook(ThreadLocalRandom.current().nextInt(1, en.getMaxLevel()*5 +1)), true));
                } else {
                    iv.setItem(i, SetBuyPrice(RandomItem.randomItem(PEnchantTier.ULTIMATE, z.get(ThreadLocalRandom.current().nextInt(z.size()))), true));
                }
            } else if (i / 9 == 4) {
                if (Util.percentRoll(20)) {
                    PEnchant en = PEnchant.getRandomEnchant(new PEnchantTier[]{PEnchantTier.LEGENDARY});
                    iv.setItem(i, SetBuyPrice(en.getBook(ThreadLocalRandom.current().nextInt(1, en.getMaxLevel()*5 +1)),true));
                } else {
                    iv.setItem(i, SetBuyPrice(RandomItem.randomItem(PEnchantTier.LEGENDARY, z.get(ThreadLocalRandom.current().nextInt(z.size()))), true));
                }

            }
        }
        inv = iv;
    }

    private static ItemStack SetBuyPrice(ItemStack is, boolean point) {
        if (!is.hasItemMeta()) return null;
        if (!is.getItemMeta().hasLore()) return null;
        ItemMeta im = is.getItemMeta();
        ArrayList<String> lore = (ArrayList<String>) im.getLore();
        if (point) {
            lore.add(ChatColor.YELLOW + ChatColor.BOLD.toString() + "GIÁ: " + Util.format( RandomItem.getItemLevel(is) ) + " Point");
        } else {
            lore.add(ChatColor.YELLOW + ChatColor.BOLD.toString() + "GIÁ: " +Util.format( RandomItem.getItemLevel(is) * 10000000 ) + " $");
        }
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    private static boolean PointItem(ItemStack is) {
        if (!is.hasItemMeta()) return false;
        if (!is.getItemMeta().hasLore()) return false;
        ItemMeta im = is.getItemMeta();
        if (im.getLore().get(im.getLore().size() - 1).contains("$")) {
            return false;
        } else {
            return true;
        }


    }


    public static ItemStack removeBuyPrice(ItemStack is) {
        if (!is.hasItemMeta()) return null;
        if (!is.getItemMeta().hasLore()) return null;
        ItemMeta im = is.getItemMeta();
        ArrayList<String> lore = (ArrayList<String>) im.getLore();
        lore.remove(lore.size() - 1);
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    @EventHandler
    public void clik(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;

        if(e.getInventory().getHolder() instanceof ZHolder){
            if(e.getClick() == ClickType.SHIFT_LEFT ||e.getClick() == ClickType.SHIFT_RIGHT ){
                e.setCancelled(true);
            }
        }
        if (e.getClickedInventory().getHolder() instanceof ZHolder) {
            if (e.getCurrentItem() == null) {
                e.setCancelled(true);
                return;
            }
            if (e.getCurrentItem().getType() == Material.AIR) {
                e.setCancelled(true);
                return;
            }
            if (PointItem(e.getCurrentItem())) {
                if (RandomItem.getItemLevel(e.getCurrentItem()) <= Main.getPlayerPoints().getAPI().look(e.getWhoClicked().getUniqueId())) {

                    ItemStack is = e.getCurrentItem();
                    Player player = (Player) e.getWhoClicked();
                    if (player.getInventory().firstEmpty() != -1) {
                        player.getInventory().addItem(removeBuyPrice(is));
                        Main.getPlayerPoints().getAPI().take(player.getUniqueId(),(int) RandomItem.getItemLevel(e.getCurrentItem()));
                        e.getClickedInventory().setItem(e.getSlot(), new ItemStack(Material.AIR));
                        e.setCancelled(true);
                    } else {
                        e.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "Kho đồ đầy rồi !!");
                    }

                } else {
                    e.setCancelled(true);
                }
            } else {
                if (RandomItem.getItemLevel(e.getCurrentItem()) * 10000000 <= Util.getBalance((Player) e.getWhoClicked())) {

                    ItemStack is = e.getCurrentItem();
                    Player player = (Player) e.getWhoClicked();
                    if (player.getInventory().firstEmpty() != -1) {
                        player.getInventory().addItem(removeBuyPrice(is));
                        Util.takeMoney(player, RandomItem.getItemLevel(e.getCurrentItem()) * 10000000);
                        e.getClickedInventory().setItem(e.getSlot(), new ItemStack(Material.AIR));
                        e.setCancelled(true);
                    } else {
                        e.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "Kho đồ đầy rồi !!");
                    }

                } else {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void event(EntityDamageByBlockEvent e) {
        e.setCancelled(true);
    }


}

class ZHolder implements InventoryHolder {
    @Override
    public org.bukkit.inventory.Inventory getInventory() {
        return null;
    }
}
