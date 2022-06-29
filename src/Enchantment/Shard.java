package Enchantment;

import Solar.prison.Main;
import Utils.Glow;
import Utils.Util;
import io.netty.util.internal.ThreadLocalRandom;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;


public class Shard {
    public static HashMap<PEnchantTier, ArrayList<ItemStack>> itemmap = new HashMap<>();

    public static PEnchantTier getShardTier(ItemStack shard) {
        PEnchantTier tier = null;
        if (!isShard(shard))
            return null;
        tier = PEnchantTier
                .valueOf(Util.unhideS(shard.getItemMeta().getLore().get(shard.getItemMeta().getLore().size() - 1)));

        return tier;
    }

    public static ItemStack getShard(PEnchantTier tier) {
        ItemStack is = new ItemStack(Material.PRISMARINE_SHARD);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(tier.getLevelColor() + "Mảnh" + tier.getNameColor() + " " + tier.getName());
        im.setLore(Arrays.asList(new String[]{Util.hideS("shard"),
                ChatColor.AQUA + "Sử dụng bằng cách ấn chuột phải xuống đất", Util.hideS(tier.toString())}));
        im.addEnchant(new Glow(200), 1, true);
        is.setItemMeta(im);
        return is;
    }

    public static boolean isShard(ItemStack is) {
        if (!is.hasItemMeta())
            return false;
        if (!is.getItemMeta().hasLore())
            return false;
        for (String s : is.getItemMeta().getLore()) {
            if (s.contains(Util.hideS("shard"))) {
                return true;
            }
        }
        return false;
    }

    public static void openShard(PEnchantTier tier, Player player, Block block) {
        ArrayList<ItemStack> content = getRewards(tier);
        Location lz = block.getLocation();
        lz.add(0.5, 0, 0.5);

        ArmorStand am = (ArmorStand) player.getWorld().spawnEntity(lz.clone().add(0, -0.1, 0), EntityType.ARMOR_STAND);
        am.setVisible(false);
        am.setCustomNameVisible(true);
        am.setGravity(false);
        am.setMetadata("solar.upgrade.armorstand", new FixedMetadataValue(Main.getMain(), true));

        if (block.getType() == Material.CHEST || block.getType() == Material.ENDER_CHEST) {
            Util.changeChestState(block.getLocation(), true);

        }

        new BukkitRunnable() {
            int counter = ThreadLocalRandom.current().nextInt(10, 45);

            @Override
            public void run() {
                ItemStack is = Util.getRandomItemStackFromArray(content);
                if (counter > 70) {
                    am.remove();
                    block.setType(Material.AIR);
                    this.cancel();
                }
                if (counter == 50) {
                    am.setItemInHand(is);
                    if (is.getType() != Material.AIR) {
                        if (is.hasItemMeta()) {
                            if (is.getItemMeta().hasDisplayName()) {
                                am.setCustomName(
                                        is.getItemMeta().getDisplayName() + " " + ChatColor.RED + "x" + is.getAmount());
                            }
                        }
                    }
                    player.getInventory().addItem(is);
                    block.setMetadata("destroy.me" , new FixedMetadataValue(Main.getMain(), true));
                    Bukkit.getScheduler().runTask(Main.getMain(), new Runnable() {

                        @Override
                        public void run() {
                            Firework fw = (Firework) lz.getWorld().spawnEntity(lz.clone(), EntityType.FIREWORK);
                            FireworkMeta fwm = fw.getFireworkMeta();
                            Random r = new Random();

                            int rt = r.nextInt(4) + 1;
                            Type type = Type.BALL;
                            if (rt == 1)
                                type = Type.BALL;
                            if (rt == 2)
                                type = Type.BALL_LARGE;
                            if (rt == 3)
                                type = Type.BURST;
                            if (rt == 4)
                                type = Type.STAR;

                            int r1i = r.nextInt(17) + 1;
                            int r2i = r.nextInt(17) + 1;
                            Color c1 = Util.getColor(r1i);
                            Color c2 = Util.getColor(r2i);

                            FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1)
                                    .withFade(c2).with(type).trail(r.nextBoolean()).build();

                            fwm.addEffect(effect);

                            fwm.setPower(0);

                            fw.setFireworkMeta(fwm);

                        }
                    });

                }
                if (counter <= 50) {
                    if (am.getRightArmPose() != new EulerAngle(Math.toRadians(-100), Math.toRadians(-40), 0))
                        am.setRightArmPose(new EulerAngle(Math.toRadians(-100), Math.toRadians(-40), 0));
                    am.setItemInHand(is);
                    if (is.getType() != Material.AIR) {
                        if (is.hasItemMeta()) {
                            if (is.getItemMeta().hasDisplayName()) {
                                am.setCustomName(
                                        is.getItemMeta().getDisplayName() + " " + ChatColor.RED + "x" + is.getAmount());
                            }
                        }
                    }

                    Location l = am.getLocation();
                    l.setDirection(player.getLocation().getDirection().subtract(l.getDirection()).normalize().multiply(-1));
                    am.teleport(l);

                }
                counter++;
            }
        }.runTaskTimer(Main.getMain(), 0, ThreadLocalRandom.current().nextInt(1, 5));

    }

    private static ArrayList<ItemStack> getRewards(PEnchantTier tier) {
        ArrayList<ItemStack> content = new ArrayList<>();
        if (!itemmap.containsKey(tier)) {
            if (tier == PEnchantTier.SIMPLE) {
                for (int i = 0; i <= 10; i++) {
                    ItemStack is = new ItemStack(Material.WOOD_PICKAXE);
                    Energy.addEnergy(is, 0);
                    content.add(is);
                }
                for (int i = 0; i <= 5; i++) {
                    ItemStack is = new ItemStack(Material.WOOD_SWORD);
                    Energy.addEnergy(is, 0);
                    content.add(is);
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Energy.getRawEnergy(100));
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Energy.getRawEnergy(200));
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Shard.getShard(PEnchantTier.UNCOMMON));
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Dust.getDust(PEnchantTier.SIMPLE));
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Dust.getDust(PEnchantTier.UNCOMMON));
                }

            }
            if (tier == PEnchantTier.UNCOMMON) {

                for (int i = 0; i <= 10; i++) {
                    ItemStack is = new ItemStack(Material.STONE_PICKAXE);
                    Energy.addEnergy(is, 0);
                    content.add(is);
                }
                for (int i = 0; i <= 5; i++) {
                    ItemStack is = new ItemStack(Material.STONE_SWORD);
                    Energy.addEnergy(is, 0);
                    content.add(is);
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Energy.getRawEnergy(500));
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Energy.getRawEnergy(1000));
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Shard.getShard(PEnchantTier.ELITE));
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Dust.getDust(PEnchantTier.SIMPLE));
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Dust.getDust(PEnchantTier.UNCOMMON));
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Dust.getDust(PEnchantTier.ELITE));
                }
            }
            if (tier == PEnchantTier.ELITE) {

                for (int i = 0; i <= 10; i++) {
                    ItemStack is = new ItemStack(Material.IRON_PICKAXE);
                    Energy.addEnergy(is, 0);
                    content.add(is);
                }

                for (int i = 0; i <= 5; i++) {
                    ItemStack is = new ItemStack(Material.IRON_SWORD);
                    Energy.addEnergy(is, 0);
                    content.add(is);
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Energy.getRawEnergy(1000));
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Energy.getRawEnergy(2000));
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Shard.getShard(PEnchantTier.ULTIMATE));
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Dust.getDust(PEnchantTier.SIMPLE));
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Dust.getDust(PEnchantTier.UNCOMMON));
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Dust.getDust(PEnchantTier.ELITE));
                }
            }
            if (tier == PEnchantTier.ULTIMATE) {

                for (int i = 0; i <= 10; i++) {
                    ItemStack is = new ItemStack(Material.GOLD_PICKAXE);
                    Energy.addEnergy(is, 0);
                    content.add(is);
                }
                for (int i = 0; i <= 5; i++) {
                    ItemStack is = new ItemStack(Material.GOLD_SWORD);
                    Energy.addEnergy(is, 0);
                    content.add(is);
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Energy.getRawEnergy(2000));
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Energy.getRawEnergy(4000));
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Shard.getShard(PEnchantTier.LEGENDARY));
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Dust.getDust(PEnchantTier.UNCOMMON));
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Dust.getDust(PEnchantTier.ELITE));
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Dust.getDust(PEnchantTier.ULTIMATE));
                }
            }
            if (tier == PEnchantTier.LEGENDARY) {
                for (int i = 0; i <= 10; i++) {
                    ItemStack is = new ItemStack(Material.DIAMOND_PICKAXE);
                    Energy.addEnergy(is, 0);
                    content.add(is);
                }
                for (int i = 0; i <= 5; i++) {
                    ItemStack is = new ItemStack(Material.DIAMOND_SWORD);
                    Energy.addEnergy(is, 0);
                    content.add(is);
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Energy.getRawEnergy(5000));
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Energy.getRawEnergy(10000));
                }
                for (int i = 0; i <= 10; i++) {
                    content.add(Dust.getDust(PEnchantTier.ELITE));
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Dust.getDust(PEnchantTier.LEGENDARY));
                }
                for (int i = 0; i <= 5; i++) {
                    content.add(Dust.getDust(PEnchantTier.ULTIMATE));
                }
            }
            itemmap.put(tier, content);
        }

        return itemmap.get(tier);
    }

}
