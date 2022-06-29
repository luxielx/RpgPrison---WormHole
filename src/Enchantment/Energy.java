package Enchantment;

import Utils.Util;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Energy {

    public static boolean hasEnergy(ItemStack is) {
        if (!is.hasItemMeta())
            return false;
        if (!is.getItemMeta().hasLore())
            return false;
        List<String> lore = is.getItemMeta().getLore();
        for (String s : lore) {

            if (s.equalsIgnoreCase(Util.hideS("energy"))) {

                return true;
            }
        }
        return false;

    }

    public static long getTotalEnergy(ItemStack is) {
        if (!hasEnergy(is)) {
            return 0;

        }
        long energy = getMaxEnergy(is);
        long tt = 0;
        while (energy >= 100) {
            tt += energy;
            energy = energy / 2;
        }
        return tt - getMaxEnergy(is) + getCurrentEnergy(is) - (tt - getMaxEnergy(is) + getCurrentEnergy(is)) / 2;


    }

    public static void addEnergy(ItemStack is, long eamount) {
        long currentenergy = getCurrentEnergy(is) + eamount;
        boolean tachsach = false;
        if (!PEnchant.bookWithdraw(is)) {
            tachsach = true;
        }
        if (!PEnchant.energyLevelUpAble(is)) {
            return;
        }
        long maxenergy = getMaxEnergy(is);
        if (maxenergy == 0) {
            maxenergy = 100;
        }
        if (currentenergy > maxenergy) {
            currentenergy = maxenergy;
        }
        if (hasEnergy(is)) {
            deleteEnergy(is);
        }

        ItemMeta im = is.getItemMeta();
        ArrayList<String> energy = new ArrayList<>();

        energy.add(Util.hideS("energy"));
        energy.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Năng Lượng:");
        energy.add(Util.percentMeter(currentenergy, maxenergy, ChatColor.BOLD + "│", 25));
        energy.add(ChatColor.RED + "" + Math.round(currentenergy) + ChatColor.YELLOW + " / " + ChatColor.RED
                + Math.round(maxenergy));
        energy.add("");
        energy.add(ChatColor.RED + "Cấp độ cần thiết: " + ChatColor.YELLOW + getRequiredLevel(is));
        if (tachsach) {
            energy.add(ChatColor.RED + ChatColor.BOLD.toString() + "[Không Thể Tách Sách]");
        }
        if (im.hasLore()) {
            List<String> lore = im.getLore();
            lore.addAll(energy);
            im.setLore(lore);
        } else {
            im.setLore(energy);
        }

        is.setItemMeta(im);

    }


    public static int getRequiredLevel(ItemStack is) {
        if (is.getType().toString().contains("WOOD") || is.getType().toString().contains("LEATHER")) {
            return 0;
        }
        if (is.getType().toString().contains("STONE") || is.getType().toString().contains("CHAIN")) {
            return 10;
        }
        if (is.getType().toString().contains("IRON")) {
            return 30;
        }
        if (is.getType().toString().contains("GOLD")) {
            return 50;
        }
        if (is.getType().toString().contains("DIAMOND")) {
            return 70;
        }
        return 0;

    }

    public static void setEnergy(ItemStack is, long currentenergy, long maxenergy) {
        if (is == null) return;
        if (is.getType() == Material.AIR) return;
        boolean tachsach = false;
        if (!PEnchant.bookWithdraw(is)) {
            tachsach = true;
        }
        if (!PEnchant.energyLevelUpAble(is)) {
            currentenergy = 0;
        }
        if (maxenergy == 0) {
            maxenergy = 100;
        }
        if (currentenergy > maxenergy) {
            currentenergy = maxenergy;
        }
        if (hasEnergy(is)) {
            deleteEnergy(is);
        }

        ItemMeta im = is.getItemMeta();
        ArrayList<String> energy = new ArrayList<>();

        energy.add(Util.hideS("energy"));
        energy.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Năng Lượng:");
        energy.add(Util.percentMeter(currentenergy, maxenergy, ChatColor.BOLD + "│", 25));
        energy.add(ChatColor.RED + "" + Math.round(currentenergy) + ChatColor.YELLOW + " / " + ChatColor.RED
                + Math.round(maxenergy));
        energy.add("");
        energy.add(ChatColor.RED + "Cấp độ cần thiết: " + ChatColor.YELLOW + getRequiredLevel(is));
        if (tachsach) {
            energy.add(ChatColor.RED + ChatColor.BOLD.toString() + "[Không Thể Tách Sách]");
        }
        if (im.hasLore()) {
            List<String> lore = im.getLore();
            lore.addAll(energy);
            im.setLore(lore);
        } else {
            im.setLore(energy);
        }

        is.setItemMeta(im);
    }

    public static void levelUpEnergy(ItemStack is) {
        long currentenergy = 0;
        double maxenergy = getMaxEnergy(is) * 2;
        if (!isMaxEnergy(is))
            return;
        boolean tachsach = false;
        if (!PEnchant.bookWithdraw(is)) {
            tachsach = true;
        }
        if (hasEnergy(is)) {
            deleteEnergy(is);
            ItemMeta im = is.getItemMeta();
            ArrayList<String> energy = new ArrayList<>();

            energy.add(Util.hideS("energy"));
            energy.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Năng Lượng:");
            // │ ║
            energy.add(Util.percentMeter(currentenergy, maxenergy, ChatColor.BOLD + "│", 25));
            energy.add(ChatColor.RED + "" + Math.round(currentenergy) + ChatColor.YELLOW + " / " + ChatColor.RED
                    + Math.round(maxenergy));
            energy.add("");
            energy.add(ChatColor.RED + "Cấp độ cần thiết: " + ChatColor.YELLOW + getRequiredLevel(is));
            if (tachsach) {
                energy.add(ChatColor.RED + ChatColor.BOLD.toString() + "[Không Thể Tách Sách]");
            }
            if (im.hasLore()) {
                List<String> lore = im.getLore();
                lore.addAll(energy);
                im.setLore(lore);
            } else {
                im.setLore(energy);
            }

            is.setItemMeta(im);
        }
    }

    public static boolean isMaxEnergy(ItemStack is) {
        if (getMaxEnergy(is) == 0)
            return false;
        if (getCurrentEnergy(is) >= getMaxEnergy(is)) {
            return true;
        }
        return false;
    }

    public static long getMaxEnergy(ItemStack is) {
        if (!hasEnergy(is))
            return 0;
        ItemMeta im = is.getItemMeta();

        int index = 0;
        List<String> lore = im.getLore();
        for (String s : lore) {
            if (s.equalsIgnoreCase(Util.hideS("energy"))) {
                break;
            }
            index++;
        }
        index += 3;
        String max = lore.get(index);
        ChatColor.stripColor(max);
        String[] split = ChatColor.stripColor(max).replace(" ", "").split("/");
        String value = split[1];

        return Long.valueOf(value);

    }

    public static long getCurrentEnergy(ItemStack is) {
        if (!hasEnergy(is))
            return 0;
        ItemMeta im = is.getItemMeta();

        int index = 0;
        List<String> lore = im.getLore();
        for (String s : lore) {
            if (s.equalsIgnoreCase(Util.hideS("energy"))) {
                break;
            }
            index++;
        }
        index += 3;
        String max = lore.get(index);
        ChatColor.stripColor(max);
        String[] split = ChatColor.stripColor(max).replace(" ", "").split("/");
        String value = split[0];
        return Long.valueOf(value);
    }

    public static void deleteEnergy(ItemStack is) {
        if (!hasEnergy(is))
            return;
        List<String> lore = is.getItemMeta().getLore();
        int index = 0;
        for (String s : lore) {
            if (s.equalsIgnoreCase(Util.hideS("energy"))) {
                break;
            }
            index++;
        }

        while (index < lore.size()) {

            lore.remove(index);

        }
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);

    }

    public static boolean isRawEnergy(ItemStack raw) {
        if (!raw.hasItemMeta()) return false;
        if (!raw.getItemMeta().hasLore()) return false;
        for (String s : raw.getItemMeta().getLore()) {
            if (s.equalsIgnoreCase(Util.hideS("rawenergy"))) {
                return true;
            }
        }
        return false;
    }

    public static long getEnergyAmount(ItemStack raw) {
        if (!isRawEnergy(raw)) return 0;
        return Long.valueOf(Util.unhideS(raw.getItemMeta().getLore().get(raw.getItemMeta().getLore().size() - 1)));

    }

    public static ItemStack getRawEnergy(long amount) {
        if(amount < 0){
            amount = 50000000000l;
        }
        if(amount > Long.MAX_VALUE){
            amount = 50000000000l;
        }
        ItemStack raw = new ItemStack(Material.INK_SACK, 1, (short) 12);
        ItemMeta meta = raw.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "" + amount + ChatColor.AQUA + "" + ChatColor.BOLD + " Năng Lượng");
        meta.setLore(Arrays.asList(new String[]{ChatColor.RED + "Dùng để tăng năng lượng cho 1 vật phẩm", Util.hideS("rawenergy"), ChatColor.RED + "Có thể gộp các khối năng lượng lại với nhau", Util.hideS(amount + "")}));
        raw.setItemMeta(meta);
        return raw;

    }

    public static ItemStack combineEnergy(ItemStack a, ItemStack b) {
        if (hasEnergy(a)) {
            if (isRawEnergy(b)) {
                addEnergy(a, getEnergyAmount(b));
                return a;
            }
        }
        if (hasEnergy(b)) {
            if (isRawEnergy(a)) {
                addEnergy(b, getEnergyAmount(a));
                return b;
            }
        }
        if (isRawEnergy(a)) {
            if (isRawEnergy(b)) {
                return getRawEnergy(getEnergyAmount(a) * a.getAmount() + getEnergyAmount(b) * b.getAmount());

            }
        }
        return null;

    }
}
