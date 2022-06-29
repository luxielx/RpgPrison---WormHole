package Utils;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public enum ToolList {
    PICKAXE(new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE,
            Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE,
            Material.WOOD_AXE, Material.STONE_AXE, Material.IRON_AXE,
            Material.GOLD_AXE, Material.DIAMOND_AXE,
            Material.WOOD_SPADE, Material.STONE_SPADE, Material.IRON_SPADE,
            Material.GOLD_SPADE, Material.DIAMOND_SPADE
    }
            , "Công Cụ", Material.DIAMOND_PICKAXE),

    SWORD(new Material[]{Material.WOOD_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLD_SWORD,
            Material.DIAMOND_SWORD}, "Kiếm", Material.DIAMOND_SWORD),

    ARMOR(new Material[]{Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.GOLD_BOOTS,
            Material.DIAMOND_BOOTS, Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS,
            Material.GOLD_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.LEATHER_CHESTPLATE,
            Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLD_CHESTPLATE,
            Material.DIAMOND_CHESTPLATE, Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET, Material.IRON_HELMET,
            Material.GOLD_HELMET, Material.DIAMOND_HELMET,}, "Giáp",
            Material.GOLD_CHESTPLATE), CHESTPLATE(
            new Material[]{Material.LEATHER_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE,
                    Material.IRON_CHESTPLATE, Material.IRON_CHESTPLATE, Material.DIAMOND_CHESTPLATE},
            "Giáp", Material.DIAMOND_CHESTPLATE), LEGGINGS(
            new Material[]{Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS,
                    Material.IRON_LEGGINGS, Material.IRON_LEGGINGS, Material.DIAMOND_LEGGINGS},
            "Quần", Material.DIAMOND_LEGGINGS), HELMET(
            new Material[]{Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET,
                    Material.IRON_HELMET, Material.IRON_HELMET, Material.DIAMOND_HELMET},
            "Mũ", Material.DIAMOND_HELMET), BOOTS(
            new Material[]{Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS,
                    Material.IRON_BOOTS, Material.IRON_BOOTS, Material.DIAMOND_BOOTS},
            "Giày",
            Material.DIAMOND_BOOTS), ALL(new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE,
            Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE,
            Material.WOOD_AXE, Material.STONE_AXE, Material.IRON_AXE,
            Material.GOLD_AXE, Material.DIAMOND_AXE,
            Material.WOOD_SPADE, Material.STONE_SPADE, Material.IRON_SPADE,
            Material.GOLD_SPADE, Material.DIAMOND_SPADE,
            Material.WOOD_SWORD, Material.STONE_SWORD, Material.IRON_SWORD,
            Material.GOLD_SWORD, Material.DIAMOND_SWORD, Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.GOLD_BOOTS,
            Material.DIAMOND_BOOTS, Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS,
            Material.GOLD_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.LEATHER_CHESTPLATE,
            Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLD_CHESTPLATE,
            Material.DIAMOND_CHESTPLATE, Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET, Material.IRON_HELMET,
            Material.GOLD_HELMET, Material.DIAMOND_HELMET , Material.BOW}, "Tất cả", Material.DIAMOND), BOW(new Material[]{Material.BOW}, "Cung", Material.BOW);

    Material[] itemlist;
    String name;
    Material represent;

    ToolList(Material[] itemlist, String name, Material represent) {
        this.itemlist = itemlist;
        this.name = name;
        this.represent = represent;
    }

    public Material[] getItemList() {
        return this.itemlist;
    }

    public String getName() {
        return this.name;
    }

    public Material getRepresent() {
        return this.represent;
    }

    public Material getRandomTool(){
        return Arrays.asList(this.itemlist).get(ThreadLocalRandom.current().nextInt(this.itemlist.length));
    }

}
