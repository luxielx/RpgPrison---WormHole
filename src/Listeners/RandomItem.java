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
            name.add("M??y Khoan");
            name.add("M??y ????o");
            name.add("M??i Khoan");
            name.add("C??ng C???");

        } else if (material.toString().contains("SWORD")) {
            name.add("Ki???m");
            name.add("??ao");
            name.add("Dao g??m");
            name.add("Th???n Ki???m");
            name.add("Th???n Kh??");
            name.add("Ma Ki???m");
        } else if (material.toString().contains("BOOTS")) {
            name.add("???ng");
            name.add("Gi??y");
            name.add("D??p");
            name.add("H??i");
        } else if (material.toString().contains("LEGGINGS")) {
            name.add("Qu???n");
            name.add("X???p");
        } else if (material.toString().contains("CHESTPLATE")) {
            name.add("Gi??p");
            name.add("??o");
            name.add("??o Ng???c");
        } else if (material.toString().contains("HELMET")) {
            name.add("M??");
            name.add("M?? B???o Hi???m");
            name.add("N??n");
        } else if (material.toString().contains("BOW")) {
            name.add("C??");
            name.add("Cung");
            name.add("N???");
            name.add("Th???n C??");
            name.add("Ma Kh??");
            name.add("Cung Th???n");
        }
        return name.get(ThreadLocalRandom.current().nextInt(name.size()));
    }

    private static String getLastName(PEnchantTier tier) {
        ArrayList<String> name = new ArrayList<>();
        if (tier == PEnchantTier.SIMPLE) {
            name.add("Ngu Ng???c");
            name.add("C???a G??");
            name.add("C??i B???p");
            name.add("G??y");
            name.add("R???m");
            name.add("V???t ??i");
        } else if (tier == PEnchantTier.UNCOMMON) {
            name.add("T???m ???????c");
            name.add("T???m Trung");
            name.add("Ngon");
            name.add("Tinh Anh");
        } else if (tier == PEnchantTier.ELITE) {
            name.add("Hi???m");
            name.add("X???n");
            name.add("Yasou");
            name.add("Hi???m");
            name.add("Alessa");
            name.add("Leos");
            name.add("Katsuki");
            name.add("Zinka");
        } else if (tier == PEnchantTier.ULTIMATE) {
            name.add("T???i Th?????ng");
            name.add("V?? C???c");
            name.add("Anh H??ng");
            name.add("V?? H???n");
            name.add("R???t Hi???m");
            name.add("Th???n Th??nh");
            name.add("Gintama");
            name.add("Rem");
            name.add("Ram");
            name.add("Kurumi");
            name.add("Tohka");
            name.add("Shinichi");
            name.add("Tohka");
            name.add("B?? V????ng");
            name.add("Emilia");

        } else if (tier == PEnchantTier.LEGENDARY) {
            name.add("Huy???n Tho???i");
            name.add("Th???n Th??nh");
            name.add("Naruto");
            name.add("Kaneki");
            name.add("Kirito");
            name.add("Asuna");
            name.add("SolarFavor");
            name.add("Mine108");
            name.add("Loki");
            name.add("Th???n Ra");
            name.add("Helios");
            name.add("Long Th???n");
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
