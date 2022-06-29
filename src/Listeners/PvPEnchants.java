package Listeners;

import CustomEvents.ArmorEquipEvent;
import Enchantment.Energy;
import Enchantment.PEnchant;
import Enchantment.PEnchant.fireModes;
import Solar.prison.Main;
import Solar.prison.Prisoner;
import Utils.ToolList;
import Utils.Util;
import de.tr7zw.itemnbtapi.NBTItem;
import io.netty.util.internal.ThreadLocalRandom;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.inventivetalent.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;


public class PvPEnchants implements Listener {
    HashMap<UUID, LivingEntity> damaged = new HashMap<>();
    HashMap<UUID, Long> bless = new HashMap<>();
    HashMap<UUID, Long> bowcd = new HashMap<>();

    @EventHandler
    public void otherdamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (e.getCause() == DamageCause.DROWNING) {
                if (player.getInventory().getHelmet() != null
                        && player.getInventory().getHelmet().getType() != Material.AIR) {
                    if (PEnchant.NGUOICA.hasEnchant(player.getInventory().getHelmet())) {
                        player.setRemainingAir(player.getMaximumAir());
                        e.setCancelled(true);
                    }

                }
            }
            if (e.getCause() == DamageCause.FIRE_TICK || e.getCause() == DamageCause.FIRE
                    || e.getCause() == DamageCause.LAVA) {
                for (ItemStack armor : player.getInventory().getArmorContents()) {

                    if (armor == null)
                        continue;
                    if (armor.getType() == Material.AIR)
                        continue;
                    if (PEnchant.GIAPLUA.hasEnchant(armor)) {
                        e.setCancelled(true);
                        break;
                    }
                }

            }
        }
    }

    @EventHandler
    public void armor(ArmorEquipEvent e) {

        Player player = e.getPlayer();
        ItemStack NewItem = e.getNewArmorPiece();
        ItemStack OldItem = e.getOldArmorPiece();
        Prisoner p = Main.getMain().getPrisoners().get(player.getUniqueId());
        boolean c = false;
        for (Material s : ToolList.ARMOR.getItemList()) {
            if (NewItem == null)
                continue;
            if (NewItem.getType() == s) {
                c = true;
                break;
            }
        }
        if (!c)
            return;
        if (!Energy.hasEnergy(NewItem)) {
            Energy.addEnergy(NewItem, 0);
        }
        if (p.getLevel() < Energy.getRequiredLevel(NewItem)) {
            player.sendMessage(ChatColor.RED + "Bạn chưa đủ cấp độ để trang bị giáp này");
            e.setCancelled(true);
            return;
        }
        boolean haveenchantold = false;
        boolean haveenchantnew = false;
        for (PEnchant z : PEnchant.values()) {
            if (!haveenchantold && !haveenchantnew) {
                if (z.hasEnchant(OldItem))
                    haveenchantold = true;
                if (z.hasEnchant(NewItem))
                    haveenchantnew = true;
            } else {
                break;
            }

        }
        if (haveenchantold) {

            removeEffect(player, OldItem);

        }
        if (haveenchantnew) {

            addEffect(player, NewItem);
        }

    }

    public static void removeEffect(Player player, ItemStack is) {
        if (PEnchant.TOCDO.hasEnchant(is)) {
            if (player.hasPotionEffect(PotionEffectType.SPEED))
                player.removePotionEffect(PotionEffectType.SPEED);
        }
        if (PEnchant.CHANTHO.hasEnchant(is)) {
            if (player.hasPotionEffect(PotionEffectType.JUMP))
                player.removePotionEffect(PotionEffectType.JUMP);
        }
        if (PEnchant.MATTHAN.hasEnchant(is)) {
            if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION))
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }

    }

    public static void addEffect(Player player, ItemStack is) {
        if (PEnchant.TOCDO.hasEnchant(is))
            player.addPotionEffect(
                    new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, PEnchant.TOCDO.getLevel(is)));
        if (PEnchant.CHANTHO.hasEnchant(is))
            player.addPotionEffect(
                    new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, PEnchant.CHANTHO.getLevel(is)));
        if (PEnchant.MATTHAN.hasEnchant(is))
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
    }

    @EventHandler
    public void damage(EntityDamageByEntityEvent e) {

        if (!(e.getDamager() instanceof Player) && !(e.getDamager() instanceof Projectile))
            return;

        if (!(e.getEntity() instanceof LivingEntity) || e.getEntity() instanceof ArmorStand)
            return;

        if (e.isCancelled())
            return;
        if (e.getEntity() instanceof Player && e.getDamager() instanceof SmallFireball) {
            if (!e.getDamager().getMetadata("solar.firebal").isEmpty()) {
                e.setCancelled(true);
                return;
            }
        }

        if (e.getDamager().hasMetadata("NPC") || e.getEntity().hasMetadata("NPC"))
            return;

        LivingEntity victim = (LivingEntity) e.getEntity();
        double damage = e.getDamage();

        Player attacker;
        boolean sworda = false;
        boolean bowa = false;

        if (e.getDamager() instanceof Player) {
            attacker = (Player) e.getDamager();
            sworda = true;
        } else {
            if (((Projectile) e.getDamager()).getShooter() instanceof Player) {
                attacker = (Player) ((Projectile) e.getDamager()).getShooter();
                bowa = true;
            } else {
                return;
            }
        }
        ItemStack is = attacker.getItemInHand();
        if (!Arrays.asList(ToolList.SWORD.getItemList()).contains(is.getType()) && sworda) {
            e.setDamage(0);
            e.setCancelled(true);
            attacker.sendMessage(ChatColor.RED + "Bạn cần có kiếm để chiến đấu !!");
            return;
        }

        Prisoner prisoner = Main.getMain().getPrisoners().get(attacker.getUniqueId());
        if (!Energy.hasEnergy(is)) {
            Energy.addEnergy(is, 0);
        }
        if (prisoner.getLevel() < Energy.getRequiredLevel(is)) {
            e.setDamage(0);
            e.setCancelled(true);
            attacker.sendMessage(ChatColor.RED + "Bạn chưa đủ cấp độ dùng vũ khí này");

            return;
        }
        int armorr = 0;
        int sword = 0;
        if (is != null && is.getType().toString().contains("SWORD")) {
            if (is.getType() == Material.WOOD_SWORD) {
                sword += 2;
            } else if (is.getType() == Material.STONE_SWORD) {
                sword += 5;
            } else if (is.getType() == Material.IRON_SWORD) {
                sword += 10;
            } else if (is.getType() == Material.GOLD_SWORD) {
                sword += 15;
            } else if (is.getType() == Material.DIAMOND_SWORD) {
                sword += 20;
            }
        }
        boolean thany = false;
        int blessheal = 0;
        int thethupercent = 1;
        int tuhuychance = 0;
        int giamdamage = 0;
        // Giáp
        if (victim instanceof Player) {
            Prisoner vpr = Main.getMain().getPrisoners().get(((Player) victim).getUniqueId());
//			if (!GangsPlugin.getInstance().fightManager.areFighting(GangsPlusAPI.getPlayersGang((Player) victim),
//					GangsPlusAPI.getPlayersGang(attacker))) {
//				if (prisoner.getLevel() > vpr.getLevel() && prisoner.getLevel() - vpr.getLevel() > 20) {
//					e.setDamage(0);
//					e.setCancelled(true);
//					attacker.sendMessage(ChatColor.RED + "Chênh lệch cấp độ quá lớn");
//					return;
//				} else if (vpr.getLevel() > prisoner.getLevel() && vpr.getLevel() - prisoner.getLevel() > 20) {
//					e.setDamage(0);
//					e.setCancelled(true);
//					attacker.sendMessage(ChatColor.RED + "Chênh lệch cấp độ quá lớn");
//					return;
//				}
//			}
            int chance = 0;
            int burnchance = 0;
            int cursechance = 0;
            int pushchance = 0;
            int enlightchance = 0;
            int giapgai = 0;
            for (ItemStack armor : ((HumanEntity) victim).getInventory().getArmorContents()) {

                if (armor == null)
                    continue;
                if (armor.getType() == Material.AIR)
                    continue;
                if (armor.getType().toString().contains("LEATHER")) {
                    armorr += 1;
                } else if (armor.getType().toString().contains("CHAINMAIL")) {
                    armorr += 2;
                } else if (armor.getType().toString().contains("IRON")) {
                    armorr += 3;
                } else if (armor.getType().toString().contains("GOLD")) {
                    armorr += 4;
                } else if (armor.getType().toString().contains("DIAMOND")) {
                    armorr += 5;
                }

                if (PEnchant.NETRANH.hasEnchant(armor)) {

                    chance += PEnchant.NETRANH.getLevel(armor);

                }
                if (PEnchant.THETHU.hasEnchant(armor) && ((Player) victim).isSneaking()) {
                    thethupercent += PEnchant.THETHU.getLevel(armor);
                }
                if (PEnchant.GIAPCUNG.hasEnchant(armor)) {
                    giamdamage += PEnchant.GIAPCUNG.getLevel(armor);
                }
                if (PEnchant.GIAPLUA.hasEnchant(armor)) {
                    burnchance += PEnchant.GIAPLUA.getLevel(armor);
                }
                if (PEnchant.NGUYENRUA.hasEnchant(armor)) {
                    cursechance += PEnchant.NGUYENRUA.getLevel(armor);
                }
                if (PEnchant.THEPHOA.hasEnchant(armor)) {
                    pushchance += PEnchant.THEPHOA.getLevel(armor);
                }
                if (PEnchant.HAPTHU.hasEnchant(armor)) {
                    enlightchance += PEnchant.HAPTHU.getLevel(armor);
                }
                if (PEnchant.BANPHUOC.hasEnchant(armor)) {
                    blessheal += 5;
                }
                if (PEnchant.TUHUY.hasEnchant(armor)) {
                    tuhuychance += PEnchant.TUHUY.getLevel(armor);

                }
                if (PEnchant.GIAPGAI.hasEnchant(armor)) {
                    giapgai += PEnchant.GIAPGAI.getLevel(armor);

                }

            }

            if (Util.percentRoll(chance)) {
                e.setCancelled(true);
                ((Player) victim).playSound(victim.getLocation(), Sound.ENDERDRAGON_WINGS, 1, 1);
                return;
            }
            if (Util.percentRoll(giapgai)) {
                attacker.damage(damage / 2, victim);
            }
            if (Util.percentRoll(burnchance)) {
                attacker.setFireTicks(100);
            }
            if (Util.percentRoll(cursechance)) {
                attacker.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 5));
            }
            if (Util.percentRoll(pushchance)) {
                ((Player) victim).playSound(victim.getLocation(), Sound.ANVIL_LAND, 1, 1);
                Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getMain(), new Runnable() {
                    @Override
                    public void run() {
                        victim.setVelocity(new Vector(0, 0, 0));
                        attacker.setVelocity(attacker.getLocation().toVector().subtract(victim.getLocation().toVector())
                                .normalize().multiply(1).add(new Vector(0, 0.5, 0)));
                    }
                }, 1);
            }
            if (Util.percentRoll(enlightchance)) {
                victim.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 5));
            }

        }

        damage += sword - armorr;
        // kiếm
        if (sworda) {
            if (PEnchant.SACBEN.hasEnchant(is)) {
                damage += PEnchant.SACBEN.getLevel(is);
            }
            if (PEnchant.TUYETTHUC.hasEnchant(is) && victim instanceof Player) {
                int level = PEnchant.TUYETTHUC.getLevel(is);
                if (Util.percentRoll(level)) {
                    int value;
                    if (level <= 5) {
                        value = ThreadLocalRandom.current().nextInt(level, 6);
                    } else {
                        value = 10;
                    }

                    Util.drawLineParticle(victim.getEyeLocation(),
                            attacker.getEyeLocation().clone().subtract(0, 1, 0), ParticleEffect.VILLAGER_HAPPY, 10,
                            false);
                    if (((Player) victim).getFoodLevel() - value >= 0) {
                        ((Player) victim).setFoodLevel(((Player) victim).getFoodLevel() - value);
                    } else {
                        ((Player) victim).setFoodLevel(0);
                    }

                    if (attacker.getFoodLevel() + value <= 20) {
                        attacker.setFoodLevel(attacker.getFoodLevel() + value);
                    } else {
                        attacker.setFoodLevel(20);
                    }

                }
            }
            if (PEnchant.HANBANG.hasEnchant(is)) {
                int level = PEnchant.HANBANG.getLevel(is);
                if (Util.percentRoll(level * 2)) {
                    victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, level * 10, 3));
                    ParticleEffect.BLOCK_CRACK.sendData(Util.getNearbyPlayer(victim.getLocation(), 10),
                            victim.getEyeLocation().getX(), victim.getEyeLocation().getY(),
                            victim.getEyeLocation().getZ(), 0, 0, 0, 0, 10, new ItemStack(Material.ICE));

                }
            }
            if (PEnchant.HOADIEM.hasEnchant(is)) {
                int level = PEnchant.HOADIEM.getLevel(is);
                if (Util.percentRoll(level * 2)) {
                    if (victim.getFireTicks() < level * 20) {
                        victim.setFireTicks(level * 20);
                    }
                    ParticleEffect.FLAME.send(Util.getNearbyPlayer(victim.getLocation(), 30),
                            victim.getEyeLocation(), 0, 0, 0, 1, 20);

                }
            }
            if (PEnchant.CHOANGVANG.hasEnchant(is) && victim instanceof Player) {
                int level = PEnchant.CHOANGVANG.getLevel(is);
                if (Util.percentRoll(level)) {
                    stun((Player) victim);

                    ParticleEffect.EXPLOSION_LARGE.send(Util.getNearbyPlayer(victim.getLocation(), 30),
                            victim.getEyeLocation(), 0, 0, 0, 0, 1);
                }

            }
            if (PEnchant.DOC.hasEnchant(is)) {
                int level = PEnchant.DOC.getLevel(is);
                if (Util.percentRoll(level)) {
                    victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * level, level / 2));
                    ParticleEffect.BLOCK_CRACK.sendData(Util.getNearbyPlayer(victim.getLocation(), 10),
                            victim.getEyeLocation().getX(), victim.getEyeLocation().getY(),
                            victim.getEyeLocation().getZ(), 0, 0, 0, 0, 10,
                            new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14));

                }

            }
            if (PEnchant.LOITHAN.hasEnchant(is)) {
                int level = PEnchant.LOITHAN.getLevel(is);
                if (Util.percentRoll(level)) {
                    damage += level;
                    victim.getWorld().strikeLightningEffect(victim.getLocation());
                    victim.getWorld().playSound(victim.getLocation(), Sound.AMBIENCE_THUNDER, 1, 1);
                }
            }
            if (PEnchant.HUTMAU.hasEnchant(is)) {
                int level = PEnchant.HUTMAU.getLevel(is);
                if (Util.percentRoll(level * 2)) {
                    Util.drawLineParticle(victim.getEyeLocation(),
                            attacker.getEyeLocation().clone().subtract(0, 1, 0), ParticleEffect.REDSTONE, 10,
                            false);
                    if (attacker.getHealth() + damage < attacker.getMaxHealth()) {
                        attacker.setHealth(attacker.getHealth() + damage);
                    } else {
                        attacker.setHealth(attacker.getMaxHealth());
                    }

                }
            }
            if (PEnchant.ANTU.hasEnchant(is)) {
                int level = PEnchant.ANTU.getLevel(is);
                if (Util.percentRoll(level * 2)) {
                    Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getMain(), new Runnable() {

                        @Override
                        public void run() {
                            victim.setVelocity(attacker.getLocation().toVector()
                                    .subtract(victim.getLocation().toVector()).normalize());
                        }
                    }, 1);

                    Util.drawLineParticle(victim.getEyeLocation(),
                            attacker.getEyeLocation().clone().subtract(0, 1, 0), ParticleEffect.END_ROD, 30, false);
                }
            }
            if (PEnchant.PHONGTHAN.hasEnchant(is)) {
                int level = PEnchant.PHONGTHAN.getLevel(is);
                attacker.playSound(victim.getLocation(), Sound.ENDERDRAGON_WINGS, 1, 1);
                if (Util.percentRoll(level)) {
                    Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getMain(), new Runnable() {

                        @Override
                        public void run() {
                            victim.setVelocity(new Vector(0, 1, 0));

                        }
                    }, 1);

                    ParticleEffect.EXPLOSION_LARGE.send(Util.getNearbyPlayer(victim.getLocation(), 30),
                            victim.getLocation(), 0, 0, 0, 0, 1);
                }
            }

            if (PEnchant.HOITHOCUARONG.hasEnchant(is)) {

                int level = PEnchant.HOITHOCUARONG.getLevel(is);

                if (Util.percentRoll(level)) {
                    Location location = attacker.getEyeLocation();
                    attacker.playSound(location, Sound.BLAZE_BREATH, 1, 1);
                    new BukkitRunnable() {
                        int co = 0;
                        int particlesHelix = 3;
                        double radials = Math.PI / 30;
                        float radius = 1.5f;
                        float length = 7;
                        float grow = 0.2f;
                        int step = 0;

                        @Override
                        public void run() {
                            co++;
                            if (co == 25)
                                this.cancel();
                            for (int j = 0; j < particlesHelix; j++) {
                                if (step * grow > length) {
                                    step = 0;
                                }
                                for (int i = 0; i < 2; i++) {
                                    double angle = step * radials + Math.PI * i;
                                    Vector v = new Vector(Math.cos(angle) * radius, step * grow,
                                            Math.sin(angle) * radius);

                                    drawParticle(location, v, attacker, level);

                                }

                                step++;
                            }

                        }
                    }.runTaskTimer(Main.getMain(), 0, 1);
                    damaged.remove(attacker.getUniqueId());
                }
            }
        } else if (bowa) {
            Projectile arrow = (Projectile) e.getDamager();
            boolean satluc = !arrow.getMetadata("bow.satluc").isEmpty();
            boolean tendoc = !arrow.getMetadata("bow.tendoc").isEmpty();
            boolean tenno = !arrow.getMetadata("bow.tenno").isEmpty();
            boolean hoa = !arrow.getMetadata("bow.HOA").isEmpty();
            boolean thuy = !arrow.getMetadata("bow.THUY").isEmpty();
            boolean han = !arrow.getMetadata("bow.HAN").isEmpty();
            boolean bongtoi = !arrow.getMetadata("bow.BONGTOI").isEmpty();
            boolean anhsang = !arrow.getMetadata("bow.ANHSANG").isEmpty();
            boolean loi = !arrow.getMetadata("bow.LOI").isEmpty();
            boolean phong = !arrow.getMetadata("bow.PHONG").isEmpty();
            thany = !arrow.getMetadata("bow.thany").isEmpty();
            // TODO
            if (satluc) {
                damage += 2 + arrow.getMetadata("bow.satluc").get(0).asInt();
            }
            if (arrow.getType() == EntityType.ARROW && !thany) {
                if (tendoc) {
                    victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100,
                            arrow.getMetadata("bow.tendoc").get(0).asInt()));
                }
                if (tenno) {
                    ParticleEffect.EXPLOSION_LARGE.send(Util.getNearbyPlayer(arrow.getLocation(), 30d), arrow.getLocation(), 0, 0, 0, 0, 1);
                    for (Entity ent : arrow.getWorld().getNearbyEntities(arrow.getLocation(), 3, 3, 3)) {
                        if (ent instanceof LivingEntity) {
                            if (ent instanceof Player) {
                                if (ent.getUniqueId() == attacker.getUniqueId()) continue;
                            }
                            ((LivingEntity) ent).damage(arrow.getMetadata("bow.tenno").get(0).asInt());
                        }

                    }
                }
                if (hoa) {
                    victim.setFireTicks(arrow.getMetadata("bow.HOA").get(0).asInt() * 20);
                }
                if (thuy) {
                    victim.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,
                            arrow.getMetadata("bow.THUY").get(0).asInt() * 30, 1));
                }
                if (han) {
                    victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,
                            arrow.getMetadata("bow.HAN").get(0).asInt() * 20, 5));
                }
                if (bongtoi) {
                    victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
                            arrow.getMetadata("bow.BONGTOI").get(0).asInt() * 20, 5));
                }
                if (anhsang) {
                    victim.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,
                            arrow.getMetadata("bow.ANHSANG").get(0).asInt() * 40, 1));
                }
                if (loi) {
                    victim.getWorld().strikeLightning(victim.getLocation());
                    damage += arrow.getMetadata("bow.LOI").get(0).asInt();
                }
                if (phong) {
                    victim.setVelocity(arrow.getLocation().getDirection().normalize()
                            .multiply(-arrow.getMetadata("bow.PHONG").get(0).asInt())
                            .multiply(new Vector(0, 1, 0)));
                }
            } else if (arrow.getType() == EntityType.SMALL_FIREBALL) {
                victim.setFireTicks(60);
            } else if (arrow.getType() == EntityType.SNOWBALL) {
                victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 1));
            }
            victim.setNoDamageTicks(0);
        }
        // 300 000

        damage -= (giamdamage + thethupercent);

        if (victim instanceof Player && damage >= victim.getHealth()) {
            if (PEnchant.BANPHUOC.hasEnchant(((Player) victim).getInventory().getChestplate())) {
                if (bless.containsKey(victim.getUniqueId())) {
                    if (System.currentTimeMillis() - bless.get(victim.getUniqueId()) >= 300000 - (PEnchant.BANPHUOC.getLevel(((Player) victim).getInventory().getChestplate())) * 10000) {
                        ((Player) victim).playSound(victim.getLocation(), Sound.LEVEL_UP, 1, 1);
                        victim.setHealth(blessheal);
                        e.setDamage(0);

                        return;
                    }
                } else {
                    victim.setHealth(blessheal);
                    e.setDamage(0);

                    return;
                }
            }
            if (Util.percentRoll(tuhuychance)) {
                TNTPrimed tnt = (TNTPrimed) victim.getWorld().spawnEntity(victim.getLocation(),
                        EntityType.PRIMED_TNT);
                tnt.setYield(3);
                tnt.setFuseTicks(0);
                tnt.setMetadata("tuhuyenchant", new FixedMetadataValue(Main.getMain(), true));
            }

        }
        if (thany) {
            e.setDamage(0);
            if (victim.getHealth() + damage <= victim.getMaxHealth()) {
                victim.setHealth(victim.getHealth() + damage);
            } else {
                victim.setHealth(victim.getMaxHealth());
            }

        } else {
            e.setDamage(damage);
        }

    }


    @EventHandler
    public void explode(EntityExplodeEvent e) {
        if (e.getEntity().getType() == EntityType.PRIMED_TNT) {
            if (e.getEntity().hasMetadata("tuhuyenchant")) {
                e.blockList().clear();
            }
        }
    }

    @EventHandler
    public void regen(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            double amount = e.getAmount();
            ItemStack helmet, chestplace, leggings, boots;

            if (player.getInventory().getHelmet() != null) {
                helmet = player.getInventory().getHelmet();
                if (PEnchant.HOIPHUC.hasEnchant(helmet)) {
                    if (Util.percentRoll(PEnchant.HOIPHUC.getLevel(helmet) * 2))
                        amount += 0.5;
                }
            }
            if (player.getInventory().getChestplate() != null) {
                chestplace = player.getInventory().getChestplate();
                if (PEnchant.HOIPHUC.hasEnchant(chestplace)) {
                    if (Util.percentRoll(PEnchant.HOIPHUC.getLevel(chestplace) * 2))
                        amount += 0.5;
                }
            }
            if (player.getInventory().getLeggings() != null) {
                leggings = player.getInventory().getLeggings();
                if (PEnchant.HOIPHUC.hasEnchant(leggings)) {
                    if (Util.percentRoll(PEnchant.HOIPHUC.getLevel(leggings) * 2))
                        amount += 0.5;
                }
            }
            if (player.getInventory().getBoots() != null) {
                boots = player.getInventory().getBoots();
                if (PEnchant.HOIPHUC.hasEnchant(boots)) {
                    if (Util.percentRoll(PEnchant.HOIPHUC.getLevel(boots) * 2))
                        amount += 0.5;
                }
            }

            e.setAmount(amount);

        }
    }

    public void stun(Player p) {
        Location l = p.getLocation();
        Location location = p.getLocation();
        location.setYaw(l.getYaw() + ThreadLocalRandom.current().nextInt(-50, 50));
        location.setPitch(l.getPitch() + ThreadLocalRandom.current().nextInt(-50, 50));
        p.teleport(location);

    }

    protected void drawParticle(Location location, Vector v, Player player, int enchantLevel) {
        Util.rotateAroundAxisX(v, (location.getPitch() + 90) * Math.PI / 180);
        Util.rotateAroundAxisY(v, -location.getYaw() * Math.PI / 180);
        location.add(v);
        if (!(location.getBlock().getType() == Material.STATIONARY_WATER)) {
            ParticleEffect.FLAME.send(Util.getNearbyPlayer(location, 30d), location, 0, 0, 0, 0, 1);
            ParticleEffect.LAVA.send(Util.getNearbyPlayer(location, 30d), location, 0, 0, 0, 0, 1);

            for (Entity e : location.getWorld().getNearbyEntities(location, 0.7, 0.7, 0.7)) {
                if (e instanceof LivingEntity) {
                    if (e == player)
                        continue;
                    if (!damaged.containsValue(e)) {
                        ((LivingEntity) e).damage(enchantLevel);
                        e.setFireTicks(enchantLevel * 40);
                    }
                    damaged.put(player.getUniqueId(), (LivingEntity) e);
                }
            }
        }
        location.subtract(v);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void invClick(InventoryClickEvent e) {

        if (e.getClickedInventory() == null)
            return;

        if (e.getCurrentItem() == null)
            return;

        if (e.getClick() == ClickType.LEFT || e.getClick() == ClickType.RIGHT) {

            ItemStack current = e.getCurrentItem();
            ItemStack cursor = e.getCursor();
            Player player = (Player) e.getWhoClicked();
            if (current.getType() == Material.AIR)
                return;
            ;
            if (cursor.getType() == Material.AIR)
                return;

            if (!PEnchant.IsprotectionOrb(cursor))
                return;
            boolean asd = false;
            for (Material s : Arrays.asList(PEnchant.BAOVE.getToolList().getItemList())) {
                if (s == current.getType()) {
                    asd = true;
                    break;
                }
            }
            if (!asd)
                return;
            if (PEnchant.BAOVE.hasEnchant(current))
                return;
            e.setCancelled(true);
            e.setCursor(new ItemStack(Material.AIR));
            PEnchant.BAOVE.apply(current, 1);
            player.updateInventory();
        }

    }

    @EventHandler
    public void addenchant(InventoryClickEvent e) {
        if (e.getClickedInventory() == null)
            return;
        if (e.getCursor() == null && e.getCurrentItem() == null)
            return;
        ItemStack c = e.getCursor();
        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();
        if (!c.hasItemMeta())
            return;
        if (!c.getItemMeta().hasDisplayName())
            return;
        if (c.getType() != Material.BOOK)
            return;
        if (!PEnchant.IsBook(c))
            return;
        PEnchant en = null;
        for (PEnchant z : PEnchant.values()) {
            if (c.getItemMeta().getDisplayName().contains(z.getName())) {
                en = z;
                break;
            }
        }
        if (en == null)
            return;
        boolean match = false;
        for (Material m : en.getToolList().getItemList()) {
            if (item.getType() == m) {
                match = true;
                break;
            }
        }
        if (!match)
            return;
        e.setCancelled(true);
        String name = c.getItemMeta().getDisplayName();
        String[] splitz = name.split(" ");
        String roman = ChatColor.stripColor(splitz[splitz.length - 1]);
        int level = Util.romanConvert(roman);
        int multiply = 1;
        if (item.getType().toString().contains("WOOD")) {
            multiply = 1;
        } else if (item.getType().toString().contains("STONE")) {
            multiply = 2;

        } else if (item.getType().toString().contains("IRON")) {
            multiply = 3;
        } else if (item.getType().toString().contains("GOLD")) {
            multiply = 4;
        } else if (item.getType().toString().contains("DIAMOND")) {
            multiply = 5;
        } else if (item.getType().toString().contains("BOW")) {
            multiply = 5;
        }
        if (level > en.getMaxLevel() * multiply) {
            player.sendMessage(ChatColor.RED + "Cấp độ của sách quá cao");
            return;
        }
        en.apply(item, level);
        player.setItemOnCursor(new ItemStack(Material.AIR));

    }


    @EventHandler
    public void BowAttack(PlayerInteractEvent e) {
        if (e.getPlayer().getItemInHand().getType().equals(Material.BOW)) {
            if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
                Player player = e.getPlayer();
                boolean allow = false;

                if (bowcd.containsKey(e.getPlayer().getUniqueId())) {
                    if (bowcd.get(player.getUniqueId()) < System.currentTimeMillis()) {
                        allow = true;
                    }
                } else {
                    allow = true;

                }
                if (allow) {
                    ArrayList<ParticleEffect> parlist = new ArrayList<>();
                    player.getItemInHand().setDurability((short) (player.getItemInHand().getDurability() + 1));
                    if (player.getItemInHand().getDurability() >= player.getItemInHand().getType().getMaxDurability()) {
                        Util.sendTitleBar(player, ChatColor.RED + "Công cụ sắp hỏng!!", ChatColor.GREEN + "Hãy sửa công cụ để tiếp tục sử dụng", 20, 20);
                        player.updateInventory();
                        e.setCancelled(true);
                        return;
                    }
                    ItemStack is = player.getItemInHand();
                    if (!Energy.hasEnergy(is)) {
                        Energy.addEnergy(is, 0);
                    }
                    fireModes firemode;
                    NBTItem nbti = new NBTItem(is);
                    if (nbti.getString("firemode").equals("")) {
                        nbti.setString("firemode", "NORMAL");
                        firemode = fireModes.NORMAL;
                        player.setItemInHand(nbti.getItem());
                    } else {
                        firemode = fireModes.valueOf(nbti.getString("firemode"));
                    }
                    boolean dacbiet = false;

                    int power = 2;
                    int attackspeed = 0;
                    Class<? extends Projectile> proj = null;
                    if (firemode == fireModes.HOACAU) {
                        dacbiet = true;
                        proj = SmallFireball.class;

                    } else if (firemode == fireModes.BANGCAU) {
                        dacbiet = true;
                        parlist.add(ParticleEffect.SNOW_SHOVEL);
                        parlist.add(ParticleEffect.SNOWBALL);
                        proj = Snowball.class;
                    } else if (firemode == fireModes.THANY) {
                        dacbiet = true;
                        proj = Arrow.class;
                    } else if (firemode == fireModes.NORMAL) {
                        proj = Arrow.class;

                    }
                    if (PEnchant.DAYTHEP.hasEnchant(is)) {
                        power += PEnchant.DAYTHEP.getLevel(is);
                    }
                    if (PEnchant.LIENCUNG.hasEnchant(is)) {
                        attackspeed += PEnchant.LIENCUNG.getLevel(is);
                    }
                    ArrayList<Projectile> projectilelist = new ArrayList<>();
                    for (int i = 0; i <= PEnchant.NOTHAN.getLevel(is); i++) {
                        int z = 1;
                        if (i % 2 == 0) {
                            z = 1;
                        } else {
                            z = -1;
                        }
                        Location l = player.getEyeLocation().clone();
                        l.setYaw(l.getYaw() + 5 * i * z);
                        Projectile prj = player.launchProjectile(proj, l.getDirection().normalize().multiply(power * 0.5));
                        if (prj instanceof SmallFireball) {
                            ((SmallFireball) prj).setIsIncendiary(false);
                        }
                        projectilelist.add(prj);
                    }

                    // TODO
                    for (Projectile arrow : projectilelist) {
                        if (PEnchant.SATLUC.hasEnchant(is)) {
                            arrow.setMetadata("bow.satluc",
                                    new FixedMetadataValue(Main.getMain(), PEnchant.SATLUC.getLevel(is)));
                            parlist.add(ParticleEffect.CRIT);

                        }
                        if (PEnchant.TENDOC.hasEnchant(is) && !dacbiet) {
                            arrow.setMetadata("bow.tendoc",
                                    new FixedMetadataValue(Main.getMain(), PEnchant.TENDOC.getLevel(is)));
                                    parlist.add(ParticleEffect.VILLAGER_HAPPY);
                        }
                        if (PEnchant.TENNO.hasEnchant(is) && !dacbiet) {
                            arrow.setMetadata("bow.tenno",
                                    new FixedMetadataValue(Main.getMain(), PEnchant.TENNO.getLevel(is)));
                        }

                        PEnchant element = PEnchant.randomElement();
                        if (element.hasEnchant(is) && !dacbiet) {
                            if (Util.percentRoll(element.getLevel(is) * 5))
                                arrow.setMetadata("bow." + element.toString(),
                                        new FixedMetadataValue(Main.getMain(), element.getLevel(is)));
                            if (element == PEnchant.BONGTOI) {
                                parlist.add(ParticleEffect.PORTAL);
                            }
                            if (element == PEnchant.LOI) {
                                parlist.add(ParticleEffect.REDSTONE);
                            }
                            if (element == PEnchant.HOA) {
                                parlist.add(ParticleEffect.FLAME);
                            }
                            if (element == PEnchant.THUY) {
                                parlist.add(ParticleEffect.WATER_BUBBLE);
                            }
                            if (element == PEnchant.PHONG) {
                                parlist.add(ParticleEffect.CLOUD);

                            }

                        }
                        if (firemode == fireModes.THANY && PEnchant.THANY.hasEnchant(is)) {
                            arrow.setMetadata("bow.thany", new FixedMetadataValue(Main.getMain(), 1));
                            parlist.add(ParticleEffect.HEART);
                        }

                        Main.getParticleList().put(arrow , parlist);
                        arrow.setShooter(player);
                        e.setCancelled(true);
                        player.updateInventory();
                    }
                    bowcd.put(player.getUniqueId(), System.currentTimeMillis() + 1000 - (95 * attackspeed));
//					EntityPlayer nmsplayer = ((CraftPlayer) e.getPlayer ()). getHandle ();
//					nmsplayer.playerConnection.sendPacket ( new PacketPlayOutSetCooldown (nmsplayer.inventory.getItemInHand (). getItem (),(1000 - (95 * attackspeed))/50 ));


                }

            }
        }
    }

    @EventHandler
    public void removeArrow(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow) {
            e.getEntity().remove();
        }
        if (e.getEntity() instanceof SmallFireball) {
            ParticleEffect.EXPLOSION_LARGE.send(Util.getNearbyPlayer(e.getEntity().getLocation(), 30d), e.getEntity().getLocation(), 0, 0, 0, 0, 1);
            for (Entity ent : e.getEntity().getWorld().getNearbyEntities(e.getEntity().getLocation(), 1, 1, 1)) {
                if (ent instanceof LivingEntity) {
                    if (!ent.hasMetadata("NPC"))
                        Bukkit.getScheduler().runTaskAsynchronously(Main.getMain(), new Runnable() {
                            @Override
                            public void run() {
                                ent.setVelocity(e.getEntity().getLocation().getDirection().normalize().multiply(-1));

                            }
                        });
                }
            }
        }
        if (e.getEntity() instanceof Snowball) {
            for (Entity ent : e.getEntity().getWorld().getNearbyEntities(e.getEntity().getLocation(), 1, 1, 1)) {
                if (ent instanceof LivingEntity) {
                    if (!ent.hasMetadata("NPC"))
                        Bukkit.getScheduler().runTaskAsynchronously(Main.getMain(), new Runnable() {
                            @Override
                            public void run() {
                                ent.setVelocity(new Vector());

                            }
                        });
                }
            }

        }

    }

    @EventHandler
    public void switchbowMode(PlayerInteractEvent e) {
        if (e.getPlayer().getItemInHand() == null)
            return;
        if (e.getPlayer().getItemInHand().getType() != Material.BOW)
            return;
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            ItemStack is = e.getPlayer().getItemInHand();
            NBTItem nbti = new NBTItem(is);
            fireModes firemode;
            if (nbti.getString("firemode").equals("")) {
                nbti.setString("firemode", "NORMAL");
                firemode = fireModes.NORMAL;
            } else {
                firemode = fireModes.valueOf(nbti.getString("firemode"));
            }
            ArrayList<fireModes> availablefiremode = new ArrayList<>();
            availablefiremode.add(fireModes.NORMAL);
            if (PEnchant.THANY.hasEnchant(is)) {
                availablefiremode.add(fireModes.THANY);
            }
            if (PEnchant.HOACAU.hasEnchant(is)) {
                availablefiremode.add(fireModes.HOACAU);
            }
            if (PEnchant.BANGCAU.hasEnchant(is)) {
                availablefiremode.add(fireModes.BANGCAU);
            }
            int index = 0;
            for (fireModes v : availablefiremode) {
                if (v == firemode) {
                    break;
                }
                index++;
            }

            if (availablefiremode.size() <= 1) {
                return;
            }
            if (index < availablefiremode.size() - 1) {
                fireModes switched = availablefiremode.get(index + 1);
                nbti.setString("firemode", switched.toString());
                Util.sendActionBar(e.getPlayer(), ChatColor.BLUE + "Đã chuyển qua chế độ bắn: " + switched.getName());
            } else {
                fireModes switched = availablefiremode.get(0);
                nbti.setString("firemode", switched.toString());
                Util.sendActionBar(e.getPlayer(), ChatColor.BLUE + "Đã chuyển qua chế độ bắn: " + switched.getName());
            }
            e.getPlayer().setItemInHand(nbti.getItem());

        }
    }

}
