package Listeners;

import Enchantment.PEnchant;
import Solar.prison.Main;
import Solar.prison.Prisoner;
import Utils.Util;
import com.earth2me.essentials.commands.WarpNotFoundException;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.ess3.api.InvalidWorldException;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.particle.ParticleEffect;

import java.util.ArrayList;

public class Teleportation implements Listener {
    @EventHandler
    public void ded(PlayerDeathEvent e) {
        Player player = e.getEntity();
        Prisoner p = Main.getMain().getPrisoners().get(player.getUniqueId());
        ArrayList<ItemStack> a = new ArrayList<>();

        for (ItemStack is : e.getDrops()) {
            if (PEnchant.BAOVE.hasEnchant(is)) {
                a.add(is);
                continue;
            }
        }
        for (ItemStack z : a) {
            e.getDrops().remove(z);
        }
        try {
            player.teleport(Main.getEss().getWarps().getWarp("spawn"), TeleportCause.PLUGIN);
        } catch (WarpNotFoundException ez) {
            // TODO Auto-generated catch block

        } catch (InvalidWorldException ez) {
            // TODO Auto-generated catch block

        }
        Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {

            @Override
            public void run() {

                for (ItemStack z : a) {
                    PEnchant.BAOVE.remove(z);
                    player.getInventory().addItem(z);
                }
            }
        }, 1);

    }
    @EventHandler
    public void teleevent(PlayerTeleportEvent e){
        if(e.getPlayer().isFlying()){
            e.getPlayer().setFlying(false);
        }
        if(e.getPlayer().getAllowFlight()){
            e.getPlayer().setAllowFlight(false);
        }
    }


    @EventHandler
    public void portal(PlayerPortalEvent e) throws WarpNotFoundException, InvalidWorldException {

        if (e.getCause() == TeleportCause.END_PORTAL) {

            Player player = e.getPlayer();
            ApplicableRegionSet a = Main.getWG().getRegionManager(player.getWorld())
                    .getApplicableRegions(player.getLocation());

            boolean spawnp = false;
            boolean cteleport = false;
            for (ProtectedRegion b : a.getRegions()) {

                if (b.getId().equalsIgnoreCase("spawnp")) {
                    spawnp = true;
                    break;
                }

                if (b.getId().contains("cellteleport")) {
                    cteleport = true;
                    break;
                }
            }

            if (spawnp) {
                e.setCancelled(true);
                player.teleport(Main.getEss().getWarps().getWarp("spawn"), TeleportCause.PLUGIN);
            }

            if (cteleport) {

                e.setCancelled(true);
                player.teleport(Main.getEss().getWarps().getWarp("cell"), TeleportCause.PLUGIN);

            }
        }
    }

    public static void createHeliX(Location loc) {
        double radius = 0.8;
        new BukkitRunnable() {
            double y = 0;

            @Override
            public void run() {
                if (y > 3)
                    this.cancel();

                double x = radius * Math.cos(y);
                double z = radius * Math.sin(y);
                ParticleEffect.FLAME.send(Util.getNearbyPlayerAsync(loc, 30d), loc.clone().add(x, y, z), 0, 0, 0, 0, 1);
                ParticleEffect.FLAME.send(Util.getNearbyPlayerAsync(loc, 30d), loc.clone().add(z, y, x), 0, 0, 0, 0, 1);
                y += 0.1;

            }
        }.runTaskTimerAsynchronously(Main.getMain(), 0, 1);
    }

    public static void createHeliY(Location loc) {
        double radius = -0.8;
        new BukkitRunnable() {
            double y = 0;

            @Override
            public void run() {
                if (y > 3)
                    this.cancel();
                double x = radius * Math.cos(y);
                double z = radius * Math.sin(y);
                ParticleEffect.FLAME.send(Util.getNearbyPlayerAsync(loc, 30d), loc.clone().add(x, y, z), 0, 0, 0, 0, 1);
                ParticleEffect.FLAME.send(Util.getNearbyPlayerAsync(loc, 30d), loc.clone().add(z, y, x), 0, 0, 0, 0, 1);
                y += 0.1;

            }
        }.runTaskTimerAsynchronously(Main.getMain(), 0, 1);
    }
    public static void teleportToSpawn(Player player){
        if (player.getWorld().getName().equalsIgnoreCase("spawn")) {
            try {
                player.teleport(Main.getEss().getWarps().getWarp("spawn"), TeleportCause.PLUGIN);
            } catch (WarpNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvalidWorldException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 400, 2));

            new BukkitRunnable() {
                int tptime = 10;
                int counter = 0;
                Location prev = player.getLocation();

                @Override
                public void run() {

                    if (player.hasPermission("prison.vip1")) {
                        tptime = 7;
                    }
                    if (player.hasPermission("prison.vip2")) {
                        tptime = 5;
                    }
                    if (player.hasPermission("prison.vip3")) {
                        tptime = 3;
                    }
                    if (prev.getWorld() == player.getWorld()) {
                        if (prev.distance(player.getLocation()) >= 0.1) {
                            if (player.hasPotionEffect(PotionEffectType.CONFUSION)) {
                                player.removePotionEffect(PotionEffectType.CONFUSION);
                            }
                            player.sendMessage(ChatColor.RED + "Đã hủy dịch chuyển");
                            this.cancel();
                        }
                    } else {
                        this.cancel();
                    }

                    if (counter % 2 == 0) {
                        createHeliX(player.getLocation());
                        createHeliY(player.getLocation());
                        Util.sendTitleBar(player ,ChatColor.RED + "Đang dịch chuyển",ChatColor.GREEN + "Còn " + (tptime * 2 - counter) / 2 + " giây",20,5  );
                        Util.sendActionBar(player,ChatColor.RED + "Đang dịch chuyển , Đừng di chuyển "
                                + ChatColor.GREEN + "Còn " + (tptime * 2 - counter) / 2 + " giây");
                    }
                    if (counter >= tptime * 2) {
                        try {
                            player.teleport(Main.getEss().getWarps().getWarp("spawn"), TeleportCause.PLUGIN);
                        } catch (WarpNotFoundException e) {

                            e.printStackTrace();
                        } catch (InvalidWorldException e) {
                            e.printStackTrace();
                        }
                        if (player.hasPotionEffect(PotionEffectType.CONFUSION)) {
                            player.removePotionEffect(PotionEffectType.CONFUSION);
                        }
                        this.cancel();
                    }
                    prev = player.getLocation();
                    counter++;
                }
            }.runTaskTimer(Main.getMain(), 0, 10);
        }
    }
    public static void teleportToWarp(Player player,String warp){

            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 400, 2));

            new BukkitRunnable() {
                int tptime = 10;
                int counter = 0;
                Location prev = player.getLocation();

                @Override
                public void run() {

                    if (player.hasPermission("prison.vip1")) {
                        tptime = 7;
                    }
                    if (player.hasPermission("prison.vip2")) {
                        tptime = 5;
                    }
                    if (player.hasPermission("prison.vip3")) {
                        tptime = 3;
                    }
                    if (prev.getWorld() == player.getWorld()) {
                        if (prev.distance(player.getLocation()) >= 0.1) {
                            if (player.hasPotionEffect(PotionEffectType.CONFUSION)) {
                                player.removePotionEffect(PotionEffectType.CONFUSION);
                            }
                            player.sendMessage(ChatColor.RED + "Đã hủy dịch chuyển");
                            this.cancel();
                        }
                    } else {
                        this.cancel();
                    }

                    if (counter % 2 == 0) {
                        createHeliX(player.getLocation());
                        createHeliY(player.getLocation());
                        Util.sendTitleBar(player ,ChatColor.RED + "Đang dịch chuyển",ChatColor.GREEN + "Còn " + (tptime * 2 - counter) / 2 + " giây",20,5  );
                        Util.sendActionBar(player,ChatColor.RED + "Đang dịch chuyển , Đừng di chuyển "
                                + ChatColor.GREEN + "Còn " + (tptime * 2 - counter) / 2 + " giây");
                    }
                    if (counter >= tptime * 2) {
                        try {
                            player.teleport(Main.getEss().getWarps().getWarp(warp), TeleportCause.PLUGIN);
                        } catch (WarpNotFoundException e) {

                            e.printStackTrace();
                        } catch (InvalidWorldException e) {
                            e.printStackTrace();
                        }
                        if (player.hasPotionEffect(PotionEffectType.CONFUSION)) {
                            player.removePotionEffect(PotionEffectType.CONFUSION);
                        }
                        this.cancel();
                    }
                    prev = player.getLocation();
                    counter++;
                }
            }.runTaskTimer(Main.getMain(), 0, 10);

    }


}
