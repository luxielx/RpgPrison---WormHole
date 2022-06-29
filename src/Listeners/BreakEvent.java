package Listeners;

import CustomEvents.ExpEvent;
import Enchantment.*;
import Solar.prison.Main;
import Solar.prison.Prisoner;
import Utils.Util;
import net.lightshard.prisonmines.mine.Mine;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.inventivetalent.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.HashSet;


public class BreakEvent implements Listener {

    @SuppressWarnings("deprecation")
    public ArrayList<Material> spademinethis() {
        ArrayList<Material> a = new ArrayList<>();
        a.add(Material.DIRT);
        a.add(Material.SAND);
        a.add(Material.GRAVEL);
        a.add(Material.SNOW);
        a.add(Material.SNOW_BLOCK);
        a.add(Material.SOUL_SAND);
        a.add(Material.GRASS);
        return a;
    }

    public ArrayList<Material> axeminethis() {
        ArrayList<Material> a = new ArrayList<>();
        a.add(Material.WOOD);
        a.add(Material.LOG);
        a.add(Material.LOG_2);
        a.add(Material.LEAVES);
        a.add(Material.LEAVES_2);
        a.add(Material.PUMPKIN);
        return a;
    }

    public ArrayList<Material> pickaxeminethis() {
        ArrayList<Material> a = new ArrayList<>();
        a.add(Material.STONE);
        a.add(Material.COBBLESTONE);
        a.add(Material.COAL_ORE);
        a.add(Material.GOLD_ORE);
        a.add(Material.IRON_ORE);
        a.add(Material.GLASS);
        a.add(Material.STAINED_GLASS);
        a.add(Material.SANDSTONE);
        a.add(Material.SMOOTH_BRICK);
        a.add(Material.MOSSY_COBBLESTONE);
        a.add(Material.QUARTZ_BLOCK);
        a.add(Material.BRICK);
        a.add(Material.HARD_CLAY);
        a.add(Material.SMOOTH_BRICK);
        a.add(Material.LAPIS_BLOCK);
        a.add(Material.LAPIS_ORE);
        a.add(Material.DISPENSER);
        a.add(Material.SANDSTONE);
        a.add(Material.GOLD_BLOCK);
        a.add(Material.IRON_BLOCK);
        a.add(Material.OBSIDIAN);
        a.add(Material.DIAMOND_ORE);
        a.add(Material.DIAMOND_BLOCK);
        a.add(Material.REDSTONE_ORE);
        a.add(Material.REDSTONE_BLOCK);
        a.add(Material.GLOWING_REDSTONE_ORE);
        a.add(Material.ICE);
        a.add(Material.EMERALD_ORE);
        a.add(Material.EMERALD_BLOCK);
        a.add(Material.BEACON);
        return a;


    }

    @EventHandler
    public void interract(PlayerInteractEvent e) {
        HashSet<Material> set = new HashSet<>();
        set.add(Material.AIR);
        set.add(Material.WATER);
        set.add(Material.LONG_GRASS);
        set.add(Material.LAVA);
        Block bl = e.getPlayer().getTargetBlock(set, 6);
        if (e.getPlayer().getItemInHand() == null) return;
        if (e.getPlayer().getItemInHand().getType() == Material.AIR) return;
        ItemStack is = e.getPlayer().getItemInHand();
        boolean ispick = false;
        if (is.getType().toString().contains("PICKAXE") || is.getType().toString().contains("AXE") || is.getType().toString().contains("SPADE")) {
            ispick = true;
        }
        if (!ispick) return;
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
            if (spademinethis().contains(bl.getType()) && !is.getType().toString().contains("_SPADE")) {
                is.setType(Material.getMaterial(is.getType().toString().replace("_PICKAXE", "_SPADE").replace("_AXE", "_SPADE")));
            } else if (axeminethis().contains(bl.getType()) && !is.getType().toString().contains("_AXE")) {
                is.setType(Material.getMaterial(is.getType().toString().replace("_SPADE", "_AXE").replace("_PICKAXE", "_AXE")));

            } else if (pickaxeminethis().contains(bl.getType()) && !is.getType().toString().contains("_PICKAXE")) {
                is.setType(Material.getMaterial(is.getType().toString().replace("_AXE", "_PICKAXE").replace("_SPADE", "_PICKAXE")));

            }
        }

    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void breakEvt(BlockBreakEvent e) {

        if (e.isCancelled()) return;

        Player player = e.getPlayer();
        Prisoner pn = Main.getMain().getPrisoners().get(player.getUniqueId());
        ItemStack is = player.getItemInHand();
        boolean ispick = false;

        if (is.getType().toString().contains("PICKAXE") || is.getType().toString().contains("AXE") || is.getType().toString().contains("SPADE")) {

            ispick = true;
        }
        if(player.hasPermission("prison.unbreak")){
            is.setDurability((short)0);
            player.updateInventory();
        }
        if (is.getDurability() >= is.getType().getMaxDurability() - 1 && ispick) {
            e.setCancelled(true);
            Util.sendTitleBar(player, ChatColor.RED + "Công cụ sắp hỏng!!", ChatColor.GREEN + "Hãy sửa công cụ để tiếp tục sử dụng", 20, 20);
            player.updateInventory();
            return;
        }

        Mine minee = Main.getPM().getByLocation(e.getBlock().getLocation());
        if(minee == null){
            if((player.getWorld().getName().equalsIgnoreCase("mines") || player.getWorld().getName().equalsIgnoreCase("maps")) && !player.hasPermission("prison.grief")){
                e.setCancelled(true);
            }
            return;
        }




        if(player.getWorld().getName().contains("plots")) return;
        if (Energy.isMaxEnergy(is) && ispick) {

            Util.sendActionBar(player, ChatColor.RED + "Công cụ của bạn đã đầy năng lượng , Hãy tăng cấp nó trước khi tiếp tục sử dụng");

            return;
        }

        if (pn.getLevel() < Energy.getRequiredLevel(is) && ispick) {
            player.sendMessage(ChatColor.RED + "Bạn chưa đủ cấp độ sử dụng công cụ này");
            return;
        }
        Block bl = e.getBlock();

        Material mat = bl.getType();
        // ENCHANTMENTS
        if (PEnchant.RANNUT.hasEnchant(is) || PEnchant.TANPHA.hasEnchant(is)) {
            int level = 0;
            int radius = 1;
            if (PEnchant.RANNUT.hasEnchant(is)) {
                level = PEnchant.RANNUT.getLevel(is);
            }
            if (PEnchant.TANPHA.hasEnchant(is)) {
                level = PEnchant.TANPHA.getLevel(is);
                radius = 3;
            }
            final int zxc = radius;
            if (Util.percentRoll(level)) {
                new BukkitRunnable() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void run() {
                        for (Block b : Util.getNearbyBlocks(bl.getLocation(), zxc)) {

                            if (Main.getBlocksFromCf().get(b.getType()) == null) continue;

                            if (b == bl)
                                continue;
                            ParticleEffect.EXPLOSION_LARGE.send(Util.getNearbyPlayer(bl.getLocation(), 20d), b.getLocation(), 0, 0, 0, 0, 1);
                            Mine mine = Main.getPM().getByLocation(b.getLocation());

                            if (mine != null) {
                                Bukkit.getScheduler().runTask(Main.getMain(), () -> {


                                    addItem(player, b.getType(), null, bl.getData());
                                    b.setType(Material.AIR);
                                });


                            }

                        }
                    }
                }.runTaskAsynchronously(Main.getMain());


            }

        }
        if (PEnchant.NHAINUOT.hasEnchant(is)) {
            if (Util.percentRoll(PEnchant.NHAINUOT.getLevel(is))) {
                player.setFoodLevel(20);
            }
        }
        if (PEnchant.SAMSET.hasEnchant(is)) {
            if (Util.percentRoll(PEnchant.SAMSET.getLevel(is))) {
                Location loc = bl.getLocation().clone();

                bl.getWorld().strikeLightningEffect(bl.getLocation());
                loc.setYaw(0);
                Vector v = loc.getDirection().normalize();
                loc.setYaw(45);
                Vector v1 = loc.getDirection().normalize();
                loc.setYaw(90);
                Vector v2 = loc.getDirection().normalize();
                loc.setYaw(135);
                Vector v3 = loc.getDirection().normalize();
                loc.setYaw(180);
                Vector v4 = loc.getDirection().normalize();
                loc.setYaw(225);
                Vector v5 = loc.getDirection().normalize();
                loc.setYaw(270);
                Vector v6 = loc.getDirection().normalize();
                loc.setYaw(315);
                Vector v7 = loc.getDirection().normalize();
                loc.setYaw(0);
                Location l = loc.clone();
                Location l1 = loc.clone();
                Location l2 = loc.clone();
                Location l3 = loc.clone();
                Location l4 = loc.clone();
                Location l5 = loc.clone();
                Location l6 = loc.clone();
                Location l7 = loc.clone();
                new BukkitRunnable() {
                    int i = 0;

                    @SuppressWarnings("deprecation")
                    @Override
                    public void run() {
                        if (i > 10) this.cancel();
                        l.add(v);
                        l1.add(v1);
                        l2.add(v2);
                        l3.add(v3);
                        l4.add(v4);
                        l5.add(v5);
                        l6.add(v6);
                        l7.add(v7);
                        ParticleEffect.EXPLOSION_LARGE.send(Util.getNearbyPlayer(bl.getLocation(), 20d), l, 0, 0, 0, 0, 1);
                        ParticleEffect.EXPLOSION_LARGE.send(Util.getNearbyPlayer(bl.getLocation(), 20d), l1, 0, 0, 0, 0, 1);
                        ParticleEffect.EXPLOSION_LARGE.send(Util.getNearbyPlayer(bl.getLocation(), 20d), l2, 0, 0, 0, 0, 1);
                        ParticleEffect.EXPLOSION_LARGE.send(Util.getNearbyPlayer(bl.getLocation(), 20d), l3, 0, 0, 0, 0, 1);
                        ParticleEffect.EXPLOSION_LARGE.send(Util.getNearbyPlayer(bl.getLocation(), 20d), l4, 0, 0, 0, 0, 1);
                        ParticleEffect.EXPLOSION_LARGE.send(Util.getNearbyPlayer(bl.getLocation(), 20d), l5, 0, 0, 0, 0, 1);
                        ParticleEffect.EXPLOSION_LARGE.send(Util.getNearbyPlayer(bl.getLocation(), 20d), l6, 0, 0, 0, 0, 1);
                        ParticleEffect.EXPLOSION_LARGE.send(Util.getNearbyPlayer(bl.getLocation(), 20d), l7, 0, 0, 0, 0, 1);
                        Mine mine = Main.getPM().getByLocation(l);
                        if (mine != null && Main.getBlocksFromCf().get(l.getBlock().getType()) != null) {

                            addItem(player, l.getBlock().getType(), null, l.getBlock().getData());
                            l.getBlock().setType(Material.AIR);
                        }
                        mine = Main.getPM().getByLocation(l1);
                        if (mine != null && Main.getBlocksFromCf().get(l1.getBlock().getType()) != null) {

                            addItem(player, l1.getBlock().getType(), null, l1.getBlock().getData());
                            l1.getBlock().setType(Material.AIR);
                        }
                        mine = Main.getPM().getByLocation(l2);
                        if (mine != null && Main.getBlocksFromCf().get(l2.getBlock().getType()) != null) {

                            addItem(player, l2.getBlock().getType(), null, l2.getBlock().getData());
                            l2.getBlock().setType(Material.AIR);
                        }
                        mine = Main.getPM().getByLocation(l3);
                        if (mine != null && Main.getBlocksFromCf().get(l3.getBlock().getType()) != null) {

                            addItem(player, l3.getBlock().getType(), null, l3.getBlock().getData());
                            l3.getBlock().setType(Material.AIR);
                        }
                        mine = Main.getPM().getByLocation(l4);
                        if (mine != null && Main.getBlocksFromCf().get(l4.getBlock().getType()) != null) {

                            addItem(player, l4.getBlock().getType(), null, l4.getBlock().getData());
                            l4.getBlock().setType(Material.AIR);
                        }
                        mine = Main.getPM().getByLocation(l5);
                        if (mine != null && Main.getBlocksFromCf().get(l5.getBlock().getType()) != null) {

                            addItem(player, l5.getBlock().getType(), null, l5.getBlock().getData());
                            l5.getBlock().setType(Material.AIR);
                        }
                        mine = Main.getPM().getByLocation(l6);
                        if (mine != null && Main.getBlocksFromCf().get(l6.getBlock().getType()) != null) {

                            addItem(player, l6.getBlock().getType(), null, l6.getBlock().getData());
                            l6.getBlock().setType(Material.AIR);
                        }
                        mine = Main.getPM().getByLocation(l7);
                        if (mine != null && Main.getBlocksFromCf().get(l7.getBlock().getType()) != null) {

                            addItem(player, l7.getBlock().getType(), null, l7.getBlock().getData());
                            l7.getBlock().setType(Material.AIR);
                        }
                        i++;

                    }

                }.runTaskTimer(Main.getMain(), 0, 0);


            }
        }
        if (PEnchant.DIENCUONG.hasEnchant(is)) {
            if (Util.percentRoll(PEnchant.DIENCUONG.getLevel(is)))
                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, PEnchant.DIENCUONG.getLevel(is) * 100, PEnchant.DIENCUONG.getLevel(is)));
        }
        if (PEnchant.KHAOCO.hasEnchant(is)) {
            int lz = PEnchant.KHAOCO.getLevel(is);
            if (Util.percentRoll(lz, 1000)) {
                player.sendMessage(PEnchantTier.SIMPLE.getNameColor() + "Bạn đã khai thác được mảnh "
                        + PEnchantTier.SIMPLE.getName());
                player.getInventory().addItem(Shard.getShard(PEnchantTier.SIMPLE));
            } else if (Util.percentRoll(lz, 2000)) {
                player.sendMessage(PEnchantTier.UNCOMMON.getNameColor() + "Bạn đã khai thác được mảnh "
                        + PEnchantTier.UNCOMMON.getName());
                player.getInventory().addItem(Shard.getShard(PEnchantTier.UNCOMMON));
            } else if (Util.percentRoll(lz, 5000)) {
                player.sendMessage(PEnchantTier.ELITE.getNameColor() + "Bạn đã khai thác được mảnh "
                        + PEnchantTier.ELITE.getName());
                player.getInventory().addItem(Shard.getShard(PEnchantTier.ELITE));
            } else if (Util.percentRoll(lz, 100000)) {
                player.sendMessage(PEnchantTier.ULTIMATE.getNameColor() + "Bạn đã khai thác được mảnh "
                        + PEnchantTier.ULTIMATE.getName());
                player.getInventory().addItem(Shard.getShard(PEnchantTier.ULTIMATE));
                Bukkit.broadcastMessage(ChatColor.AQUA + "Chúc Mừng " + PEnchantTier.ULTIMATE.getNameColor()
                        + player.getName() + ChatColor.AQUA + " đã nhận được mảnh "
                        + PEnchantTier.ULTIMATE.getNameColor() + PEnchantTier.ULTIMATE.getName());
            } else if (Util.percentRoll(lz, 200000)) {
                player.sendMessage(PEnchantTier.LEGENDARY.getNameColor() + "Bạn đã khai thác được mảnh "
                        + PEnchantTier.LEGENDARY.getName());
                player.getInventory().addItem(Shard.getShard(PEnchantTier.LEGENDARY));
                Bukkit.broadcastMessage(ChatColor.AQUA + "Chúc Mừng " + PEnchantTier.LEGENDARY.getNameColor()
                        + player.getName() + ChatColor.AQUA + " đã nhận được mảnh "
                        + PEnchantTier.LEGENDARY.getNameColor() + PEnchantTier.LEGENDARY.getName());
            }
        }
        // FULL INVENTORY
        if (player.getInventory().firstEmpty() == -1) {
            Util.sendTitleBar(player, ChatColor.RED + "Này này", ChatColor.GREEN + "Kho đồ đầy rồi !!", 10, 10);
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1, 1);
            if(player.hasPermission("prison.sellall")){
                SellEvent.SellAllItem(player);
            }
        }
        // BOOST
        double multiplier = 1;
        double multi = 1;
        if (ExpBooster.multiornah(player)) {
            multiplier = 2;
        }
        if (player.hasPermission("prison.vip1")) {
            multi = 1.25;
        }
        if (player.hasPermission("prison.vip2")) {
            multi = 1.5;
        }
        if (player.hasPermission("prison.vip3")) {
            multi = 2;
        }
        multiplier *= multi;
        if (ExpBooster.IsGlobalBoosting()) {
            multiplier *= 2;
        }
        // energy
        if (Main.getBlocksFromCf().get(mat) == null) return;
        double eamount = Main.getBlocksFromCf().get(mat) * multiplier;
        if (is != null && ispick) {
            double zxczxc = eamount;

            if (Energy.hasEnergy(is)) {

                if (PEnchant.DIENTICHCAU.hasEnchant(is)) {
                    zxczxc += PEnchant.DIENTICHCAU.getLevel(is);
                }
                Energy.addEnergy(is, (int) zxczxc);
            } else {
                Energy.setEnergy(is, 0, 0);
            }

        }
//        if (pn.getLevel() < 10) {
//            eamount *= 4;
//        }
        if(pn.getPrestige() == 0) pn.setPrestige(1);
        pn.setEXP(pn.getEXP() + (eamount*1/pn.getPrestige()));
        Util.sendActionBar(player, ChatColor.AQUA + " + " + ChatColor.LIGHT_PURPLE + (eamount*1/pn.getPrestige()) + ChatColor.AQUA + " Exp");
        addItem(player, bl.getType(), is, bl.getData());
        Bukkit.getServer().getPluginManager().callEvent(new ExpEvent(player, (eamount*1/pn.getPrestige())));


    }

    public static void addItem(Player player, Material material, ItemStack is, byte data) {
        int amount = 1;
        if (is != null && PEnchant.THANTAI.hasEnchant(is)) {
            if (Util.percentRoll(10)) {
                amount += PEnchant.THANTAI.getLevel(is);
            }
        }
        if(material == Material.GLOWING_REDSTONE_ORE){
            material = Material.REDSTONE_ORE;
        }
     player.getInventory().addItem(new ItemStack(material, amount, data));
    }
    @EventHandler
    public void explode(EntityExplodeEvent e){
        e.blockList().clear();
    }
}

