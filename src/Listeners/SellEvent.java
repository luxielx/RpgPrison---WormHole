package Listeners;

import Solar.prison.Main;
import Solar.prison.Prisoner;
import Utils.CenteredMessage;
import Utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Created by ADMIN on 1/9/2018.
 */
public class SellEvent implements Listener {
    public boolean isSellSign(Block b) {
        if (!b.getType().toString().contains("SIGN")) return false;
        BlockState state = b.getState();
        if (state instanceof Sign) {
            Sign s = (Sign) state;
            if (s.getLine(0).contains(ChatColor.DARK_RED + "["+ChatColor.RED+"Bán Khối"+ChatColor.DARK_RED+"]")) {
                return true;
            }
        }
        return false;

    }

    public static void createSellSign(Block b) {

        Sign s = (Sign) b.getState();
        s.setLine(0, ChatColor.DARK_RED + "["+ChatColor.RED+"Bán Khối"+ChatColor.DARK_RED+"]");
        s.setLine(1, ChatColor.DARK_RED + "Chuột Trái để xem giá");
        s.setLine(2, ChatColor.DARK_RED + "Chuột Phải để bán hết");
        s.update();
    }

    public static void SellAllItem(Player p) {
        Prisoner pr = Main.getMain().getPrisoners().get(p.getUniqueId());
        Inventory inv = p.getInventory();
        double money = 0;
        int block = 0;
        double multi = 1;
        for (ItemStack i : inv.getContents()) {

            if (i == null) continue;
            if (i.getType() == Material.AIR) continue;
            if (Main.getBlocksFromCf().containsKey(i.getType()) && !i.hasItemMeta()) {
                block += i.getAmount();
                money += Main.getBlocksFromCf().get(i.getType()) * 10 * i.getAmount();
                inv.removeItem(i);
            }


        }
        if (block == 0) return;
        if (p.hasPermission("prison.vip1")) {
            multi = 1.25;
        }
        if (p.hasPermission("prison.vip2")) {
            multi = 1.5;
        }
        if (p.hasPermission("prison.vip3")) {
            multi = 2;
        }
        if(pr.getPrestige() == 0) pr.setPrestige(1);
        money *= multi;
        money *= 1+pr.getLevel()/10;
        money *= pr.getPrestige();
        CenteredMessage.sendCenteredMessage(p, ChatColor.AQUA + "====================================");
        CenteredMessage.sendCenteredMessage(p, ChatColor.RED + "Đã bán thành công " + ChatColor.YELLOW + "" + block + ChatColor.RED + " Khối");
        CenteredMessage.sendCenteredMessage(p, ChatColor.RED + "Nhận Được " + ChatColor.GREEN + "+" + money + " $");
        CenteredMessage.sendCenteredMessage(p, ChatColor.AQUA + "====================================");
        Util.giveMoney(p, money);

    }


    @EventHandler
    public void clickEvent(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType().toString().contains("SIGN")) {
                if (e.getPlayer().hasPermission("magicsign")) {
                    if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
                        if (e.getPlayer().getItemInHand().getType() == Material.STICK) {


                            createSellSign(e.getClickedBlock());


                        }
                    }
                }
                if (isSellSign(e.getClickedBlock())) {
                    SellAllItem(e.getPlayer());
                }
            }
        } else if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType().toString().contains("SIGN")&& isSellSign(e.getClickedBlock())) {
                Inventory inv = Bukkit.createInventory(new CCHolder(), 54, ChatColor.GREEN + "Giá Bán Khối");
                int index = 0;
                for (Material m : Main.getBlocksFromCf().keySet()) {
                    if(m == Material.GLOWING_REDSTONE_ORE){
                        continue;
                    }
                    ItemStack is = new ItemStack(m, 1);
                    ItemMeta im = is.getItemMeta();
                    im.setLore(Arrays.asList(new String[]{ChatColor.AQUA + "Giá : " + ChatColor.GREEN + Main.getBlocksFromCf().get(m) * 10 + "$/1 khối"}));
                    is.setItemMeta(im);
                    inv.setItem(index, is);
                    index++;
                }
                e.getPlayer().openInventory(inv);
            }
        }

    }

    @EventHandler
    public void invClick(InventoryClickEvent e) {
        if(e.getClickedInventory() == null) return;
        if (e.getClickedInventory().getHolder() instanceof CCHolder) {
            e.setCancelled(true);
        }


    }
}

class CCHolder implements InventoryHolder {
    @Override
    public Inventory getInventory() {
        return null;
    }

}
