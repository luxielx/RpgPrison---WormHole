package Listeners;

import Enchantment.PEnchant;
import Enchantment.PEnchantTier;
import Utils.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ADMIN on 2/15/2018.
 */
public class RandomItem {
    public static ItemStack randomItem(PEnchantTier tier, Material tool) {
        ItemStack is = new ItemStack(tool);
        int level = 0;
        int multiply = 1;
        int avglevel = 0;
        if (is.getType().toString().contains("WOOD")) {
            multiply = 1;
        } else if (is.getType().toString().contains("STONE")) {
            multiply = 2;
        } else if (is.getType().toString().contains("IRON")) {
            multiply = 3;
        } else if (is.getType().toString().contains("GOLD")) {
            multiply = 4;
        } else if (is.getType().toString().contains("DIAMOND")) {
            multiply = 5;
        } else if (is.getType().toString().contains("BOW")) {
            multiply = 5;
        }
        if (tier == PEnchantTier.SIMPLE) {
            avglevel = 20;
        } else if (tier == PEnchantTier.UNCOMMON) {
            avglevel = 50;
        } else if (tier == PEnchantTier.ELITE) {
            avglevel = 100;
        } else if (tier == PEnchantTier.ULTIMATE) {
            avglevel = 200;
        } else if (tier == PEnchantTier.LEGENDARY) {
            avglevel = 500;
        }
        boolean done = true;
        while (done) {
            for (PEnchant en : PEnchant.values()) {
                if (Arrays.asList(en.getToolList().getItemList()).contains(tool)) {
                    if (Util.percentRoll(30)) {
                        int maxlevel = en.getMaxLevel() * multiply;
                        int lv = ThreadLocalRandom.current().nextInt(0, maxlevel);
                        if (lv == 0) continue;
                        int tierlevel = 0;
                        en.apply(is, lv);
                        if (en.getTier() == PEnchantTier.SIMPLE) {
                            tierlevel = 1;
                        } else if (en.getTier() == PEnchantTier.UNCOMMON) {
                            tierlevel = 2;
                        } else if (en.getTier() == PEnchantTier.ELITE) {
                            tierlevel = 3;
                        } else if (en.getTier() == PEnchantTier.ULTIMATE) {
                            tierlevel = 4;
                        } else if (en.getTier() == PEnchantTier.LEGENDARY) {
                            tierlevel = 5;
                        }
                        level += (tierlevel * lv);
                        if (level > avglevel - avglevel / 5) {
                            done = false;
                            break;
                        }
                    }
                } else {
                    continue;
                }

            }
        }
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(tier.getNameColor() + getRandomName(tier, is.getType()) + tier.getLevelColor() + " " + getItemLevel(is));
        is.setItemMeta(im);
        PEnchant.preventBookWithdraw(is);
        PEnchant.preventEnergyLevelup(is);
        return PEnchant.resolveItemStack(is);
    }

    private static String getRandomName(PEnchantTier tier, Material material) {
        return getFirstName(material) + " " + getLastName(tier);
    }

    private static String getFirstName(Material material) {
        ArrayList<String> name = new ArrayList<>();
        if (material.toString().contains("PICKAXE") || material.toString().contains("AXE") || material.toString().contains("SPADE")) {
            name.add("Máy Khoan");
            name.add("Máy Đào");
            name.add("Mũi Khoan");
            name.add("Công Cụ");

        } else if (material.toString().contains("SWORD")) {
            name.add("Kiếm");
            name.add("Đao");
            name.add("Dao găm");
            name.add("Thần Kiếm");
            name.add("Thần Khí");
            name.add("Ma Kiếm");
        } else if (material.toString().contains("BOOTS")) {
            name.add("Ủng");
            name.add("Giày");
            name.add("Dép");
            name.add("Hài");
        } else if (material.toString().contains("LEGGINGS")) {
            name.add("Quần");
            name.add("Xịp");
        } else if (material.toString().contains("CHESTPLATE")) {
            name.add("Giáp");
            name.add("Áo");
            name.add("Áo Ngực");
        } else if (material.toString().contains("HELMET")) {
            name.add("Mũ");
            name.add("Mũ Bảo Hiểm");
            name.add("Nón");
        } else if (material.toString().contains("BOW")) {
            name.add("Cơ");
            name.add("Cung");
            name.add("Nỏ");
            name.add("Thần Cơ");
            name.add("Ma Khí");
            name.add("Cung Thần");
        }
        return name.get(ThreadLocalRandom.current().nextInt(name.size()));
    }

    private static String getLastName(PEnchantTier tier) {
        ArrayList<String> name = new ArrayList<>();
        if (tier == PEnchantTier.SIMPLE) {
            name.add("Ngu Ngốc");
            name.add("Của Gà");
            name.add("Cùi Bắp");
            name.add("Gãy");
            name.add("Rởm");
            name.add("Vứt Đi");
        } else if (tier == PEnchantTier.UNCOMMON) {
            name.add("Tạm Được");
            name.add("Tầm Trung");
            name.add("Ngon");
            name.add("Tinh Anh");
        } else if (tier == PEnchantTier.ELITE) {
            name.add("Hiếm");
            name.add("Xịn");
            name.add("Yasou");
            name.add("Hiếm");
            name.add("Alessa");
            name.add("Leos");
            name.add("Katsuki");
            name.add("Zinka");
        } else if (tier == PEnchantTier.ULTIMATE) {
            name.add("Tối Thượng");
            name.add("Vô Cực");
            name.add("Anh Hùng");
            name.add("Vô Hạn");
            name.add("Rất Hiếm");
            name.add("Thần Thánh");
            name.add("Gintama");
            name.add("Rem");
            name.add("Ram");
            name.add("Kurumi");
            name.add("Tohka");
            name.add("Shinichi");
            name.add("Tohka");
            name.add("Bá Vương");
            name.add("Emilia");

        } else if (tier == PEnchantTier.LEGENDARY) {
            name.add("Huyền Thoại");
            name.add("Thần Thánh");
            name.add("Naruto");
            name.add("Kaneki");
            name.add("Kirito");
            name.add("Asuna");
            name.add("SolarFavor");
            name.add("Mine108");
            name.add("Loki");
            name.add("Thần Ra");
            name.add("Helios");
            name.add("Long Thần");
            name.add("Phantom");
        }
        return name.get(ThreadLocalRandom.current().nextInt(name.size()));
    }

    public static long getItemLevel(ItemStack is) {
        if (!is.hasItemMeta()) return 0;
        if (!is.getItemMeta().hasLore()) return 0;
        int level = 0;
        if (PEnchant.IsBook(is)) {
            PEnchant en = PEnchant.getEnchantFromBook(is);
            int tierlevel = 1;
            if (en.getTier() == PEnchantTier.SIMPLE) {
                tierlevel = 1;
            } else if (en.getTier() == PEnchantTier.UNCOMMON) {
                tierlevel = 2;
            } else if (en.getTier() == PEnchantTier.ELITE) {
                tierlevel = 3;
            } else if (en.getTier() == PEnchantTier.ULTIMATE) {
                tierlevel = 4;
            } else if (en.getTier() == PEnchantTier.LEGENDARY) {
                tierlevel = 5;
            }
            return tierlevel * PEnchant.getEnchantLevelFromBook(is) * 3;
        }else{
            for (PEnchant en : PEnchant.values()) {
                if (en.hasEnchant(is)) {
                    int tierlevel = 1;
                    if (en.getTier() == PEnchantTier.SIMPLE) {
                        tierlevel = 1;
                    } else if (en.getTier() == PEnchantTier.UNCOMMON) {
                        tierlevel = 2;
                    } else if (en.getTier() == PEnchantTier.ELITE) {
                        tierlevel = 3;
                    } else if (en.getTier() == PEnchantTier.ULTIMATE) {
                        tierlevel = 4;
                    } else if (en.getTier() == PEnchantTier.LEGENDARY) {
                        tierlevel = 5;
                    }
                    level += tierlevel * en.getLevel(is);

                }
            }
        }
        return level;
    }
}
