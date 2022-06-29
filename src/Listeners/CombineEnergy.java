package Listeners;

import Enchantment.Energy;
import Enchantment.PEnchant;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CombineEnergy implements Listener {
    @SuppressWarnings("deprecation")
    @EventHandler
    public void clickEvent(InventoryClickEvent e) {
        if (e.getClickedInventory() == null)
            return;
        if (Energy.isRawEnergy(e.getCurrentItem())) {
            if (Energy.getEnergyAmount(e.getCurrentItem()) >= 50000000000l) {
                ItemStack is =  e.getCurrentItem();
                is.setType(Material.AIR);
                e.getClickedInventory().setItem(e.getSlot(),new ItemStack(Material.AIR));
                return;
            }
        }
        if (Energy.isRawEnergy(e.getCursor())) {
            if (Energy.getEnergyAmount(e.getCursor()) >= 50000000000l) {
                e.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                return;
            }
        }
        if (e.getClick() == ClickType.LEFT || e.getClick() == ClickType.RIGHT) {
            ItemStack current = e.getCurrentItem();
            ItemStack cursor = e.getCursor();
            Player player = (Player) e.getWhoClicked();
            if (current.getType() == Material.AIR)
                return;

            if (cursor.getType() == Material.AIR)
                return;

            if (!Energy.isRawEnergy(cursor))
                return;
            if ((!Energy.isRawEnergy(current)) && (!Energy.hasEnergy(current)))
                return;
            if (Energy.getEnergyAmount(cursor) >= 50000000000l) {
                cursor.setType(Material.AIR);
                return;
            }

            e.setCancelled(true);
            if (Energy.hasEnergy(current)) {
                if (!PEnchant.energyLevelUpAble(current)) {
                    return;
                }
                if (Energy.getEnergyAmount(cursor) <= (Energy.getMaxEnergy(current) - Energy.getCurrentEnergy(current))) {
                    e.setCursor(new ItemStack(Material.AIR));
                } else {
                    e.setCursor(Energy.getRawEnergy(Energy.getEnergyAmount(cursor) - (Energy.getMaxEnergy(current) - Energy.getCurrentEnergy(current))));
                }
            } else {
                e.setCursor(new ItemStack(Material.AIR));
            }

            e.getClickedInventory().setItem(e.getSlot(), Energy.combineEnergy(current, cursor));
            player.updateInventory();
        } else if (e.getClick() == ClickType.MIDDLE) {


            if (e.getCursor().getType() != Material.AIR) return;

            if (Energy.isRawEnergy(e.getCurrentItem())) {

                long number = Energy.getEnergyAmount(e.getCurrentItem());
                if (number <= 1) return;
                long number2 = Energy.getEnergyAmount(e.getCurrentItem());
                number = number / 2;
                e.setCursor(Energy.getRawEnergy(number));
                e.setCurrentItem(Energy.getRawEnergy(number2 - number));

            }
        }
    }

}
