package Listeners;

import Enchantment.PEnchant;
import Solar.prison.Main;
import Utils.Util;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.VectorUtils;
import net.lightshard.prisonmines.mine.Mine;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.inventivetalent.particle.ParticleEffect;

import java.util.HashMap;
import java.util.UUID;

public class PowerBallListener implements Listener {
    HashMap<UUID, Long> timers = new HashMap<>();

    public static long cooldown(int level) {

        return (long) (1000 * (150 - 5 * (level - 1)));

    }

    public boolean cooldown(int level, Player player) {
        if (!timers.containsKey(player.getUniqueId())) {
            timers.put(player.getUniqueId(), 0l);
        }

        if (System.currentTimeMillis() - timers.get(player.getUniqueId()) < cooldown(level)) {

            Util.sendActionBar(player, ChatColor.RED + "Kĩ năng " + ChatColor.AQUA + "Cầu Lửa " + ChatColor.RED + "còn "
                    + ChatColor.YELLOW
                    + ((cooldown(level) - System.currentTimeMillis() + timers.get(player.getUniqueId())) / 1000 + 1)
                    + ChatColor.RED + " giây để có thể tái sử dụng");
            return true;
        }

        return false;
    }

    @EventHandler
    public void rightclick(PlayerInteractEvent event) {

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            Player player = event.getPlayer();
            ItemStack item = player.getItemInHand();
            if (PEnchant.CAULUA.hasEnchant(item)) {

                int enchantLevel = PEnchant.CAULUA.getLevel(item);
                if (cooldown(enchantLevel, player))
                    return;

                timers.put(player.getUniqueId(), System.currentTimeMillis());

                new BukkitRunnable() {
                    float radius = 2;

                    float grow = .1f;

                    double radials = Math.PI / 16;
                    int circles = 3;

                    int helixes = 10;
                    double step = 0.1;
                    int counter = 0;
                    Location location = player.getEyeLocation()
                            .add(player.getLocation().getDirection().normalize().multiply(2));
                    Vector dir = player.getLocation().getDirection().normalize();

                    @Override
                    public void run() {

                        if (counter > 50) {

                            SmallFireball fireball = player.getWorld().spawn(location,
                                    SmallFireball.class);

                            fireball.setVelocity(dir.normalize().multiply(0.1));
                            fireball.setShooter(player);

                            fireball.setMetadata("solar.fireball", new FixedMetadataValue(Main.getMain(), 1));
                            fireball.setMetadata("solar.fireball1", new FixedMetadataValue(Main.getMain(), enchantLevel));
                            new BukkitRunnable() {
                                int i = 0;

                                @Override
                                public void run() {
                                    if (i >= 10 * 60) this.cancel();
                                    if (!fireball.isValid()) this.cancel();
                                    for (Block bl : Util.getNearbyBlocks(fireball.getLocation(), 1)) {
                                        Mine mine = Main.getPM().getByLocation(bl.getLocation());
                                        if (mine != null) {
                                            if (Main.getBlocksFromCf().containsKey(bl.getType())) {
                                                ParticleEffect.EXPLOSION_LARGE.send(Util.getNearbyPlayer(bl.getLocation(), 20d), bl.getLocation(), 0, 0, 0, 0, 1);
                                                new BukkitRunnable() {
                                                    @SuppressWarnings("deprecation")
                                                    @Override
                                                    public void run() {
                                                        BreakEvent.addItem(player, bl.getType(), null, bl.getData());

                                                        bl.setType(Material.AIR);
                                                    }
                                                }.runTask(Main.getMain());

                                            }
                                        }
                                    }
                                    i++;
                                }
                            }.runTaskTimerAsynchronously(Main.getMain(), 0, 1);
                            this.cancel();
                        } else {
                            counter++;
                            for (int x = 0; x < circles; x++) {
                                for (int i = 0; i < helixes; i++) {
                                    double angle = step * radials + (2 * Math.PI * i / helixes);
                                    Vector v = new Vector(Math.cos(angle) * radius, step * grow,
                                            Math.sin(angle) * radius);
                                    VectorUtils.rotateAroundAxisX(v,
                                            (location.getPitch() + 90) * MathUtils.degreesToRadians);
                                    VectorUtils.rotateAroundAxisY(v, -location.getYaw() * MathUtils.degreesToRadians);

                                    location.add(v);
                                    ParticleEffect.FLAME.send(Bukkit.getOnlinePlayers(), location, 0, 0, 0, 0, 1);
                                    location.subtract(v);

                                }
                                radius -= 0.01;
                                step += 0.1;

                            }

                        }
                    }
                }.runTaskTimer(Main.getMain(), 0, 1);

            }
        }
    }

}
