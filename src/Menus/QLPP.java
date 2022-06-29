package Menus;

import Enchantment.PEnchant;
import Enchantment.PEnchantTier;
import Utils.Glow;
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

public class QLPP implements Listener {
    public static void openEnchant(Player player) {
        Inventory inv = Bukkit.createInventory(new cholder(), 54, ChatColor.AQUA + "Quản lý phù phép");
        inv.setItem(0, glass(14));
        inv.setItem(1, glass(14));
        inv.setItem(2, glass(5));
        inv.setItem(3, glass(3));
        inv.setItem(4, glass(3));
        inv.setItem(5, glass(3));
        inv.setItem(6, glass(5));
        inv.setItem(7, glass(14));
        inv.setItem(8, glass(14));
        inv.setItem(9, glass(14));
        inv.setItem(10, glass(3));
        inv.setItem(12, glass(2));
        inv.setItem(14, glass(2));
        inv.setItem(16, glass(3));
        inv.setItem(17, glass(14));
        for (int i = 18; i < 27; i++) {
            inv.setItem(i, glass(14));
        }
        player.openInventory(inv);
    }

    private static ItemStack glass(int i) {
        ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) i);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.AQUA + "Hãy bỏ vật phẩm có phù phép vào");
        is.setItemMeta(im);
        return is;
    }

    @EventHandler
    public void click(InventoryClickEvent e) {
        if (e.getClickedInventory() == null)
            return;
        if (e.getCurrentItem().getType() == Material.AIR)
            return;
        if (e.getInventory().getHolder() instanceof cholder) {
            Player player = (Player) e.getWhoClicked();
            ItemStack is = e.getCurrentItem();
            Inventory iv = e.getInventory();
            e.setCancelled(true);
            if (e.getSlot() == 13 && e.getClickedInventory().getHolder() instanceof cholder) {
                e.getWhoClicked().getInventory().addItem(e.getClickedInventory().getItem(13));
                e.getClickedInventory().setItem(13, new ItemStack(Material.AIR));
                for (int i = 27; i < 54; i++) {
                    e.getInventory().setItem(i, new ItemStack(Material.AIR));
                }
            }
            if (e.getCurrentItem().getType() == Material.PAPER && !(e.getClickedInventory().getHolder() instanceof cholder)) {

                boolean isscroll = false;
                for (String s : e.getCurrentItem().getItemMeta().getLore()) {
                    if (s.contains(Util.hideS("upgrade.scroll"))) {
                        isscroll = true;
                        break;
                    }
                }
                if (isscroll) {

                    if (iv.getItem(15) == null) {
                        ItemStack b = is.clone();
                        b.setAmount(1);
                        player.getInventory().removeItem(b);
                        iv.setItem(15, b);
                    } else {
                        return;
                    }
                    update(player);
                }
                return;

            }
            if (e.getSlot() == 15 && e.getClickedInventory().getHolder() instanceof cholder) {
                player.getInventory().addItem(e.getInventory().getItem(15));
                e.getInventory().setItem(15, new ItemStack(Material.AIR));


            }
            if (e.getSlot() == 11 && e.getClickedInventory().getHolder() instanceof cholder) {
                player.getInventory().addItem(e.getInventory().getItem(11));
                e.getInventory().setItem(11, new ItemStack(Material.AIR));
                update(player);

            }

            if (e.getCurrentItem().getType() == Material.GLOWSTONE_DUST
                    && !(e.getClickedInventory().getHolder() instanceof cholder)) {

                boolean isdust = false;
                for (String s : e.getCurrentItem().getItemMeta().getLore()) {
                    if (s.contains(Util.hideS("upgrade.dust"))) {
                        isdust = true;
                        break;
                    }
                }
                if (isdust) {
                    ItemStack b = is.clone();
                    b.setAmount(1);
                    player.getInventory().removeItem(b);
                    if (iv.getItem(11) == null) {
                        iv.setItem(11, b);
                    } else {
                        if (iv.getItem(11).getAmount() < 65)
                            iv.getItem(11).setAmount(iv.getItem(11).getAmount() + 1);
                    }
                    update(player);
                }
                return;

            }

            boolean en = false;

            for (PEnchant enchant : PEnchant.values()) {
                if (enchant.hasEnchant(e.getCurrentItem())) {
                    en = true;
                    break;
                }
            }
            if (en && e.getInventory().getItem(13) == null) {
                if (!PEnchant.bookWithdraw(is)) {
                    e.setCancelled(true);
                    return;
                }
                e.getInventory().setItem(13, is);
                e.getWhoClicked().getInventory().removeItem(is);
                int index = 27;
                for (PEnchant enchant : PEnchant.values()) {
                    if (enchant.hasEnchant(is)) {
                        e.getInventory().setItem(index, book(enchant, enchant.getLevel(is)));
                        index++;
                    } else {
                        continue;
                    }
                }

            }
            if (is.getType() == Material.ENCHANTED_BOOK) {
                int percent = getPercent(is);
                if (Util.percentRoll(percent)) {
                    player.getInventory().addItem(getPE(is).getBook(getLevel(is)));
                    getPE(is).remove(iv.getItem(13));
                    iv.setItem(11, new ItemStack(Material.AIR));
                    iv.setItem(15, new ItemStack(Material.AIR));
                    player.closeInventory();
                    player.sendMessage(ChatColor.GREEN + "Thành Công!");
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1, 1);
                    if (iv.getItem(15) == null) {
                        iv.setItem(13, new ItemStack(Material.AIR));
                        iv.setItem(11, new ItemStack(Material.AIR));
                        player.sendMessage(ChatColor.GREEN + "Thất Bại! Vật phẩm bị phá hủy vì không có bùa bảo vệ");
                        player.closeInventory();
                    } else {
                        iv.setItem(11, new ItemStack(Material.AIR));
                        getPE(is).remove(iv.getItem(13));
                        iv.setItem(15, new ItemStack(Material.AIR));
                        player.sendMessage(ChatColor.GREEN + "Thất Bại! Nhưng vì có bùa bảo vệ nên Công Cụ của bạn không bị phá hủy");
                        player.closeInventory();
                    }


                }
            }

        }

    }

    private void update(Player p) {
        if (p.getOpenInventory().getTopInventory() == null)
            return;
        Inventory iv = p.getOpenInventory().getTopInventory();
        if (!(iv.getHolder() instanceof cholder))
            return;
        for (int i = 27; i < 54; i++) {
            if (iv.getItem(i) == null)
                continue;
            if (iv.getItem(i).getType() == Material.AIR)
                continue;
            if (iv.getItem(i).getType() == Material.ENCHANTED_BOOK) {
                ItemStack is = iv.getItem(i);
                String name = is.getItemMeta().getDisplayName();

                String[] sp = name.split(" ");
                String n = name.replaceAll(" " + sp[sp.length - 1], "").replaceAll(" " + sp[sp.length - 2], "");
                PEnchant pe = PEnchant.getEnchantFromName(n);
                ItemMeta im = is.getItemMeta();
                int amount = 0;
                if (iv.getItem(11) != null && iv.getItem(11).getType() == Material.GLOWSTONE_DUST) {
                    amount = iv.getItem(11).getAmount();
                }
                int percent = getPercentbyTier(pe.getTier());

                im.setDisplayName(pe.getTier().getNameColor() + pe.getName() + " " + sp[sp.length - 2] + " "
                        + ChatColor.RED + (percent + (5 * amount)) + "%");
                is.setItemMeta(im);
            }
        }
    }

    public int getPercentbyTier(PEnchantTier tier) {
        int percent = 0;
        if (tier == PEnchantTier.SIMPLE) {
            percent = 70;
        }
        if (tier == PEnchantTier.UNCOMMON) {
            percent = 60;
        }
        if (tier == PEnchantTier.ELITE) {
            percent = 50;
        }
        if (tier == PEnchantTier.ULTIMATE) {
            percent = 40;
        }
        if (tier == PEnchantTier.LEGENDARY) {
            percent = 30;
        }
        return percent;
    }

    public static PEnchant getPE(ItemStack is) {
        if (is == null)
            return null;
        if (is.getType() == Material.AIR)
            return null;
        String name = is.getItemMeta().getDisplayName();
        String[] sp = name.split(" ");
        String n = name.replaceAll(" " + sp[sp.length - 1], "").replaceAll(" " + sp[sp.length - 2], "");
        PEnchant pe = PEnchant.getEnchantFromName(n);
        return pe;
    }

    @EventHandler
    public void close(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof cholder) {
            if (e.getInventory().getItem(13) != null) {
                e.getPlayer().getInventory().addItem(e.getInventory().getItem(13));
            }
            if (e.getInventory().getItem(11) != null) {
                e.getPlayer().getInventory().addItem(e.getInventory().getItem(11));
            }
            if (e.getInventory().getItem(15) != null) {
                e.getPlayer().getInventory().addItem(e.getInventory().getItem(15));
            }

        }
    }

    public int getPercent(ItemStack is) {
        if (!is.hasItemMeta())
            return 0;
        if (!is.getItemMeta().hasDisplayName())
            return 0;
        String name = is.getItemMeta().getDisplayName();
        String[] sp = name.split(" ");
        String z = ChatColor.stripColor(sp[sp.length - 1].replaceAll("%", "").replaceAll(" ", ""));
        return Integer.valueOf(z);

    }

    public int getLevel(ItemStack is) {
        if (!is.hasItemMeta())
            return 0;
        if (!is.getItemMeta().hasDisplayName())
            return 0;
        String name = is.getItemMeta().getDisplayName();
        String[] sp = name.split(" ");
        String z = ChatColor.stripColor(sp[sp.length - 2].replaceAll(" ", ""));
        return Integer.valueOf(z);

    }

    private ItemStack book(PEnchant pe, int level) {
        ItemStack is = new ItemStack(Material.ENCHANTED_BOOK);
        int percent = getPercentbyTier(pe.getTier());
        ItemMeta im = is.getItemMeta();
        String des = pe.getDescription();
        String[] spl = des.split(" ");
        String zz = spl[spl.length / 2];
        String[] spl2 = des.split(zz);
        String s1a = spl2[0];
        String s1b = spl2[1];
        im.setDisplayName(
                pe.getTier().getNameColor() + pe.getName() + " " + level + " " + ChatColor.RED + percent + "%");
        im.setLore(Arrays.asList(
                new String[]{" " + pe.getTier().getLevelColor() + s1a + zz, pe.getTier().getLevelColor() + s1b,
                        ChatColor.BLUE + "" + ChatColor.BOLD + "Bấm vào để tách sách ra khỏi vật phẩm", ChatColor.YELLOW + "" + ChatColor.BOLD + "Đừng quên bỏ bùa bảo vệ nếu không Công Cụ sẽ bị phá hủy"}));
        im.addEnchant(new Glow(200), 1, true);
        is.setItemMeta(im);
        return is;
    }
}

class cholder implements InventoryHolder {

    @Override
    public Inventory getInventory() {
        // TODO Auto-generated method stub
        return null;
    }

}
