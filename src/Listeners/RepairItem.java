package Listeners;

import Solar.prison.Main;
import Utils.Util;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.inventivetalent.particle.ParticleEffect;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class RepairItem implements Listener {
    HashMap<UUID, Long> cd = new HashMap<>();
  public static  HashMap<UUID, ItemStack> fixing = new HashMap<>();

//    @EventHandler disabled
    public void repair(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType() == Material.ANVIL) {
                e.setCancelled(true);
                Block b = e.getClickedBlock();
                Player player = e.getPlayer();
                if (fixing.keySet().contains(player.getUniqueId())) return;
                if (!cd.containsKey(player.getUniqueId()))
                    cd.put(player.getUniqueId(), System.currentTimeMillis() - 400);
                if (cd.get(player.getUniqueId()) > System.currentTimeMillis()) return;
                cd.put(player.getUniqueId(), System.currentTimeMillis() + 200);
                boolean entity = false;
                ArmorStand am = null;
                for (Entity en : b.getLocation().getWorld().getNearbyEntities(b.getLocation(), 2, 2, 2)) {
                    if (en.getType() == EntityType.ARMOR_STAND) {
                        if (!en.getMetadata("anvil.user").isEmpty()) {
                            entity = true;
                            am = (ArmorStand) en;
                        }

                    }
                }
                if (entity) {
                    if (!am.getMetadata("anvil.user").isEmpty()) {
                        String s = am.getMetadata("anvil.user").get(0).asString();
                        UUID u = UUID.fromString(s);
                        if (player.getUniqueId().equals(u)) {
                            if (isFixingHammer(player.getItemInHand())) {
                                //FIX ITEM

                                ItemStack item = am.getItemInHand().clone();
                                fixing.put(player.getUniqueId(), item);
                                item.setDurability((short) 0);
                                short dura = 1;
                                if (item.getType().toString().contains("WOOD")) {
                                    dura = 2;
                                }
                                if (item.getType().toString().contains("STONE")) {
                                    dura = 4;
                                }
                                if (item.getType().toString().contains("IRON")) {
                                    dura = 5;
                                }
                                if (item.getType().toString().contains("GOLD")) {
                                    dura = 10;
                                }
                                if (item.getType().toString().contains("DIAMOND")) {
                                    dura = 15;
                                }
                                if (item.getType() == Material.BOW) {
                                    dura = 5;
                                }
                                player.getItemInHand().setDurability((short) (player.getItemInHand().getDurability() + dura));
                                if (player.getItemInHand().getDurability() >= player.getItemInHand().getType().getMaxDurability()) {
                                    player.setItemInHand(new ItemStack(Material.AIR));
                                }
                                new BukkitRunnable() {
                                    int c = 0;

                                    @Override
                                    public void run() {
                                        if(!player.isOnline()){
                                            for (Entity s : e.getClickedBlock().getWorld().getNearbyEntities(e.getClickedBlock().getLocation(),2,2, 2)) {
                                                if (s.getType() == EntityType.ARMOR_STAND) s.remove();
                                            }
                                            this.cancel();
                                        }
                                        if (c == 60) {
                                            if(player.getInventory().firstEmpty() == -1){
                                                player.getEnderChest().addItem(item);
                                                player.sendMessage(ChatColor.RED + "Vật phẩm của bạn đã được sửa xong , Vì túi đồ đầy nên đã chuyển vào enderchest!!");
                                            }else{
                                                player.getInventory().addItem(item);
                                            }

                                            for (Entity en : b.getLocation().getWorld().getNearbyEntities(b.getLocation(), 2, 2, 2)) {
                                                if (en.getType() == EntityType.ARMOR_STAND) {
                                                    if (!en.getMetadata("anvil.user").isEmpty()) {
                                                        if (player.getUniqueId().toString().equals(en.getMetadata("anvil.user").get(0).asString()))
                                                            en.remove();
                                                    }

                                                }
                                            }
                                            if (fixing.keySet().contains(player.getUniqueId()))
                                                fixing.remove(player.getUniqueId());

                                            this.cancel();
                                        }
                                        if (c % 10 == 0) {
                                            player.getWorld().strikeLightningEffect(b.getLocation().add(0.5, 1, 0.5));
                                            player.playSound(b.getLocation(), Sound.ANVIL_USE, 1, 0);
                                        }
                                        c++;
                                    }
                                }.runTaskTimer(Main.getMain(), 0, 2);

                            } else {
                                player.getInventory().addItem(am.getItemInHand());
                                am.remove();
                            }

                        }
                    }
                } else {
                    if (player.getItemInHand().getType().isBlock() || player.getItemInHand().getType().getMaxDurability() < 1 || player.getItemInHand().getType() == Material.FLINT_AND_STEEL) {
                        player.sendMessage(ChatColor.RED + "Vật phẩm này không thể sửa chữa");
                        return;
                    }
                    if (player.getItemInHand().getDurability() == 0) {
                        player.sendMessage(ChatColor.RED + "Vật phẩm này không cần sửa chữa");
                        return;
                    }
                    ArmorStand as;
                    if (player.getItemInHand().getType() == Material.BOW) {
                        as = (ArmorStand) player.getWorld().spawnEntity(b.getLocation().clone().add(0.33, -0.3, 0.5), EntityType.ARMOR_STAND);

                    } else {
                        as = (ArmorStand) player.getWorld().spawnEntity(b.getLocation().clone().add(0.33, -0.43, 1), EntityType.ARMOR_STAND);
                    }
                    as.setMetadata("anvil.user", new FixedMetadataValue(Main.getMain(), player.getUniqueId().toString()));
                    as.setArms(true);
                    as.setBasePlate(false);
                    as.setGravity(false);
                    as.setVisible(false);
                    as.setItemInHand(player.getItemInHand());
                    as.setMarker(true);
                    as.setRightArmPose(new EulerAngle(Math.toRadians(350), Math.toRadians(180), Math.toDegrees(23)));
                    as.setFireTicks(Integer.MAX_VALUE);
                    if (player.getItemInHand().hasItemMeta()) {
                        if (player.getItemInHand().getItemMeta().hasDisplayName()) {
                            as.setCustomName(player.getItemInHand().getItemMeta().getDisplayName());
                        } else {
                            as.setCustomName(player.getDisplayName());
                        }
                    } else {
                        as.setCustomName(player.getDisplayName());

                    }
                    player.setItemInHand(new ItemStack(Material.AIR));

                    new BukkitRunnable() {
                        int c = 0;

                        @Override
                        public void run() {
                            c++;
                            if (!as.isValid()) this.cancel();
                            if (c == 60) {
                                player.getInventory().addItem(as.getItemInHand());
                                as.remove();
                                this.cancel();
                            }
                            if (fixing.keySet().contains(player.getUniqueId())) {
                                ParticleEffect.CRIT_MAGIC.send(Util.getNearbyPlayer(b.getLocation(), 20d), b.getLocation().clone().add(0.5, 1, 0.5), 0, 0, 0, 1, 30);
                                ParticleEffect.LAVA.send(Util.getNearbyPlayer(b.getLocation(), 20d), b.getLocation().clone().add(0.5, 1, 0.5), 0, 0, 0, 1, 30);
                            } else {
                                ParticleEffect.CRIT.send(Util.getNearbyPlayer(b.getLocation(), 20d), b.getLocation().clone().add(0.5, 1, 0.5), 0, 0, 0, 1, 10);

                            }


                        }
                    }.runTaskTimer(Main.getMain(), 20, 20);


                }
            }
        }
    }

    public static ItemStack FixingHammer() {
        ItemStack is = new ItemStack(Material.FLINT_AND_STEEL);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.YELLOW + "Công cụ Sửa Chữa");
        im.setLore(Arrays.asList(new String[]{ChatColor.BLUE + "Chuột phải lên cái de [Anvil] để sửa vật phẩm", Util.hideS("fixhammer")}));
        is.setItemMeta(im);
        return is;
    }

    public static boolean isFixingHammer(ItemStack is) {
        if (!is.hasItemMeta()) return false;
        if (!is.getItemMeta().hasLore()) return false;
        for (String s : is.getItemMeta().getLore()) {
            if (s.contains(Util.hideS("fixhammer"))) return true;
        }
        return false;
    }


}
