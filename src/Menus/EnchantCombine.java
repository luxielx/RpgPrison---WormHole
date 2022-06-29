package Menus;

import Enchantment.PEnchant;
import Enchantment.PEnchantTier;
import Utils.Util;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class EnchantCombine implements Listener {
    public static void openEnchant(Player player) {
        Inventory inv = Bukkit.createInventory(new EHolder(), 54, ChatColor.AQUA + "Nâng cấp Sách");
        inv.setItem(38, glass(0));
        inv.setItem(39, glass(0));
        inv.setItem(41, glass(0));
        inv.setItem(42, glass(0));
        inv.setItem(10, glass(2));
        inv.setItem(16, glass(2));
        inv.setItem(30, glass(2));
        inv.setItem(32, glass(2));
        inv.setItem(22, redstone(0, false));
        for (int i = 11; i < 16; i++) {
            inv.setItem(i, glass(14));
        }
        for (int i = 19; i < 26; i++) {
            if (i == 20 || i == 22 || i == 24)
                continue;

            inv.setItem(i, glass(14));
        }
        for (int i = 28; i < 35; i++) {
            if (i == 30 || i == 32)
                continue;
            inv.setItem(i, glass(14));
        }
        for (int i = 0; i < 54; i++) {
            if (i == 20 || i == 22 || i == 24 || i == 37 || i == 40 || i == 43)
                continue;
            if (inv.getItem(i) != null)
                continue;

            inv.setItem(i, glass(3));
        }

        player.openInventory(inv);
    }

    private static ItemStack redstone(int i, boolean z) {
        ItemStack is = new ItemStack(Material.REDSTONE);
        ItemMeta im = is.getItemMeta();
        if (z)
            im.setDisplayName(ChatColor.GREEN + "Tỉ lệ thành công " + ChatColor.RED + i + ChatColor.GREEN + "%");
        im.setLore(Arrays.asList(new String[]{ChatColor.RED + "Bấm để tiến hành", Util.hideS("upgrade.cango"),
                Util.hideS(String.valueOf(i))}));
        if (!z) {
            im.setDisplayName(ChatColor.GREEN + "Hãy bỏ 2 cuốn sách vào để tiến hành");
            im.setLore(Arrays.asList(new String[]{ChatColor.RED + "Hãy nhớ bỏ bột vào để tăng tỉ lệ",
                    ChatColor.RED + "Đừng quên bùa để sách không bị phá hủy"}));
        }
        is.setItemMeta(im);
        return is;
    }

    private static ItemStack glass(int i) {
        ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) i);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("");
        is.setItemMeta(im);
        return is;
    }

    @EventHandler
    public void click(InventoryClickEvent e) {
        if (e.getClickedInventory() == null)
            return;
        if (e.getCurrentItem() == null)
            return;
        if (e.getInventory().getHolder() instanceof EHolder) {
            e.setCancelled(true);

            ItemStack is = e.getCurrentItem();
            Inventory iv = e.getInventory();
            Player player = (Player) e.getWhoClicked();
            if (PEnchant.IsBook(is) && !(e.getClickedInventory().getHolder() instanceof EHolder)) {

                if (iv.getItem(20) == null) {
                    if (PEnchant.getEnchantFromBook(is).getMaxLevel() * 5 <= PEnchant.getEnchantLevelFromBook(is))
                        return;

                    ItemStack zxczxc = is.clone();
                    zxczxc.setAmount(1);
                    player.getInventory().removeItem(zxczxc);
                    iv.setItem(20, zxczxc);
                } else if (iv.getItem(24) == null) {

                    if (PEnchant.getEnchantFromBook(is).getMaxLevel() * 5 <= PEnchant.getEnchantLevelFromBook(is))
                        return;

                    if (PEnchant.getEnchantLevelFromBook(is) != PEnchant.getEnchantLevelFromBook(iv.getItem(20)))
                        return;

                    ItemStack zxczxc = is.clone();
                    zxczxc.setAmount(1);
                    player.getInventory().removeItem(zxczxc);
                    iv.setItem(24, zxczxc);
                    if (PEnchant.canCombine(iv.getItem(20), iv.getItem(24))) {

                        update(player);
                    }
                }
            }
            if (is.getType() == Material.REDSTONE && e.getClickedInventory().getHolder() instanceof EHolder) {
                if (!is.hasItemMeta())
                    return;
                if (!is.getItemMeta().hasLore())
                    return;
                boolean go = false;
                for (String s : is.getItemMeta().getLore()) {
                    if (s.contains(Util.hideS("upgrade.cango"))) {
                        go = true;
                        break;
                    }
                }
                if (go) {
                    int percent = Integer.valueOf(
                            Util.unhideS(is.getItemMeta().getLore().get(is.getItemMeta().getLore().size() - 1)));
                    if (Util.percentRoll(percent)) {
                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                        if (iv.getItem(40) != null) {
                            player.getInventory().addItem(iv.getItem(40));
                            iv.setItem(40, new ItemStack(Material.AIR));
                        }
                        iv.setItem(40, PEnchant.combineBooks(iv.getItem(20), iv.getItem(24)));
                        iv.setItem(20, new ItemStack(Material.AIR));
                        iv.setItem(24, new ItemStack(Material.AIR));
                        update(player);
                        iv.setItem(37, new ItemStack(Material.AIR));
                        iv.setItem(43, new ItemStack(Material.AIR));
                    } else {
                        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1, 1);
                        if (iv.getItem(37) != null) {
                            if (iv.getItem(37).getType() == Material.PAPER) {
                                iv.setItem(37, new ItemStack(Material.AIR));
                                iv.setItem(43, new ItemStack(Material.AIR));
                                update(player);
                            }
                        } else {
                            iv.setItem(20, new ItemStack(Material.AIR));
                            iv.setItem(24, new ItemStack(Material.AIR));
                            update(player);
                            iv.setItem(37, new ItemStack(Material.AIR));
                            iv.setItem(43, new ItemStack(Material.AIR));
                        }
                    }
                }
            }
            if (is.getType() == Material.PAPER && !(e.getClickedInventory().getHolder() instanceof EHolder)) {
                if (iv.getItem(37) != null)
                    return;
                if (!is.hasItemMeta())
                    return;
                if (!is.getItemMeta().hasLore())
                    return;
                ItemStack b = is.clone();
                b.setAmount(1);
                boolean isScroll = false;
                for (String s : is.getItemMeta().getLore()) {
                    if (s.contains(Util.hideS("upgrade.scroll"))) {
                        isScroll = true;
                        break;
                    }
                }
                if (isScroll) {
                    player.getInventory().removeItem(b);
                    iv.setItem(37, b);
                }
            }
            if (is.getType() == Material.GLOWSTONE_DUST && !(e.getClickedInventory().getHolder() instanceof EHolder)) {
                if (iv.getItem(20) == null)
                    return;
                if (iv.getItem(24) == null)
                    return;
                if (!is.hasItemMeta())
                    return;
                if (!is.getItemMeta().hasLore())
                    return;
                ItemStack b = is.clone();
                b.setAmount(1);
                boolean isdust = false;
                for (String s : is.getItemMeta().getLore()) {
                    if (s.contains(Util.hideS("upgrade.dust"))) {
                        isdust = true;
                        break;
                    }
                }
                if (isdust) {
                    player.getInventory().removeItem(b);

                    if (iv.getItem(43) == null) {
                        iv.setItem(43, b);
                    } else {
                        if (iv.getItem(43).getAmount() < 64)
                            iv.getItem(43).setAmount(iv.getItem(43).getAmount() + 1);
                    }
                    update(player);
                }
            }
            if (e.getClickedInventory().getHolder() instanceof EHolder) {

                if (is.getType() != Material.STAINED_GLASS_PANE && is.getType() != Material.REDSTONE) {


                    e.getInventory().setItem(e.getSlot(), new ItemStack(Material.AIR));
                    player.getInventory().addItem(is);


                }
                update(player);
            }

        }
    }

    private void update(Player p) {
        if (p.getOpenInventory().getTopInventory() == null)
            return;
        Inventory iv = p.getOpenInventory().getTopInventory();
        if (!(iv.getHolder() instanceof EHolder))
            return;

        ItemStack is = iv.getItem(22);
        ItemMeta im = is.getItemMeta();
        if (iv.getItem(20) != null && iv.getItem(24) != null) {
            int i = getSuccessChance(PEnchant.getEnchantFromBook(iv.getItem(20)).getTier()) - (PEnchant.getEnchantLevelFromBook(iv.getItem(20)) * 5);
            int amount = 0;
            if (iv.getItem(43) != null && iv.getItem(43).getType() == Material.GLOWSTONE_DUST) {
                amount = iv.getItem(43).getAmount();
            }
            im.setDisplayName(
                    ChatColor.GREEN + "Tỉ lệ thành công " + ChatColor.RED + (i + (5 * amount)) + ChatColor.GREEN + "%");
            im.setLore(Arrays.asList(new String[]{ChatColor.RED + "Bấm để tiến hành", Util.hideS("upgrade.cango"),
                    Util.hideS(String.valueOf((i + (5 * amount))))}));
        } else {

            im.setDisplayName(ChatColor.GREEN + "Hãy bỏ 2 cuốn sách vào để tiến hành");
            im.setLore(Arrays.asList(new String[]{ChatColor.RED + "Hãy nhớ bỏ bột vào để tăng tỉ lệ",
                    ChatColor.RED + "Đừng quên bùa để sách không bị phá hủy"}));
        }
        is.setItemMeta(im);

    }

    @EventHandler
    public void invClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof EHolder) {
            Inventory iv = e.getInventory();
            Inventory inv = e.getPlayer().getInventory();
            if (iv.getItem(40) != null) {
                inv.addItem(iv.getItem(40));
            }
            if (iv.getItem(20) != null) {
                inv.addItem(iv.getItem(20));
            }
            if (iv.getItem(24) != null) {
                inv.addItem(iv.getItem(24));
            }
            if (iv.getItem(37) != null) {
                inv.addItem(iv.getItem(37));
            }
            if (iv.getItem(43) != null) {
                inv.addItem(iv.getItem(43));
            }
        }
    }

    public static ItemStack protectionScroll() {
        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.BLUE + "Bùa bảo vệ");
        im.setLore(Arrays.asList(new String[]{ChatColor.RED + "Dùng khi nâng cấp sách",
                ChatColor.RED + "Khi có bùa này sách sẽ không bị phá hủy", Util.hideS("upgrade.scroll")}));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack Dust() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.BLUE + "Bột thiên thần");
        im.setLore(Arrays.asList(new String[]{ChatColor.RED + "Dùng khi nâng cấp sách",
                ChatColor.RED + "Tăng tỉ lệ thành công lên 5%", Util.hideS("upgrade.dust")}));
        is.setItemMeta(im);
        return is;
    }

    public static int getSuccessChance(PEnchantTier tier) {
        if (tier == PEnchantTier.SIMPLE)
            return 80;
        if (tier == PEnchantTier.UNCOMMON)
            return 75;
        if (tier == PEnchantTier.ELITE)
            return 70;
        if (tier == PEnchantTier.ULTIMATE)
            return 65;
        if (tier == PEnchantTier.LEGENDARY)
            return 60;
        return 0;
    }
}

class EHolder implements InventoryHolder {

    @Override
    public Inventory getInventory() {
        // TODO Auto-generated method stub
        return null;
    }

}
