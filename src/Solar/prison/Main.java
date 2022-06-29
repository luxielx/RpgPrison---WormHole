package Solar.prison;

import Commands.MainCommands;
import CustomEvents.ArmorListener;
import Enchantment.Energy;
import Enchantment.ExpBooster;
import Enchantment.PEnchantTier;
import Enchantment.Shard;
import Listeners.*;
import Menus.*;
import Utils.LevelTop;
import Utils.Util;
import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.earth2me.essentials.Essentials;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.slikey.effectlib.EffectManager;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import net.lightshard.prisonmines.MineAPI;
import net.lightshard.prisonmines.PrisonMines;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.inventivetalent.bossbar.BossBar;
import org.inventivetalent.bossbar.BossBarAPI;
import org.inventivetalent.particle.ParticleEffect;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class Main extends JavaPlugin implements Listener {
    public static EffectManager em;
    private static Economy economy = null;
    private static PlayerPoints playerPoints;
    private BukkitTask saveTask;
    private BukkitTask boosbartask;
    public ConcurrentHashMap<UUID, Prisoner> Prisoners = new ConcurrentHashMap<>();
    private boolean disable = false;
    public static Main plugin;
    private BukkitTask updateGBtask;
    public static FileConfiguration config;
    private final Logger logger = getLogger();
    private final PluginDescriptionFile pdfile = getDescription();
    public static Essentials ess;
    public static LuckPermsApi lpapi;
    public ConsoleCommandSender console;
    private ProtocolManager protocolManager;
    public static MineAPI prisonmineapi;
    public static HashMap<Entity, ArrayList<ParticleEffect>> ParticleList = new HashMap<>();

    // private ItemStats item = new ItemStats(this);
    private void registerallEvents() {
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new Expmanager(), this);
        getServer().getPluginManager().registerEvents(new InfoMenu(), this);
        getServer().getPluginManager().registerEvents(new JoinQuitEvent(), this);
        getServer().getPluginManager().registerEvents(new BreakEvent(), this);
        getServer().getPluginManager().registerEvents(new PowerBallListener(), this);
        getServer().getPluginManager().registerEvents(new EnergyLevelup(), this);
        getServer().getPluginManager().registerEvents(new CombineEnergy(), this);
        getServer().getPluginManager().registerEvents(new NPCTouch(), this);
        getServer().getPluginManager().registerEvents(new OpenShardEvent(), this);
        getServer().getPluginManager().registerEvents(new EnchantslistGUI(), this);
        getServer().getPluginManager().registerEvents(new PvPEnchants(), this);
        getServer().getPluginManager().registerEvents(new QLPP(), this);
        getServer().getPluginManager().registerEvents(new ArmorListener(), this);
        getServer().getPluginManager().registerEvents(new Teleportation(), this);
        getServer().getPluginManager().registerEvents(new PlayerChatEvent(), this);
        getServer().getPluginManager().registerEvents(new ExpBooster(), this);
        getServer().getPluginManager().registerEvents(new EnchantCombine(), this);
        getServer().getPluginManager().registerEvents(new MiscLis(), this);
        getServer().getPluginManager().registerEvents(new RepairItem(), this);
        getServer().getPluginManager().registerEvents(new SellEvent(), this);
        getServer().getPluginManager().registerEvents(new WarpGUI(), this);
        getServer().getPluginManager().registerEvents(new DailyReward(), this);
        getServer().getPluginManager().registerEvents(new RandomItemGui(), this);

    }

    private void registercommands() {
        getCommand("en").setExecutor(new MainCommands());
        getCommand("me").setExecutor(new MainCommands());
        getCommand("xem").setExecutor(new MainCommands());
        getCommand("prison").setExecutor(new MainCommands());
        getCommand("qlpp").setExecutor(new MainCommands());
        getCommand("spawn").setExecutor(new MainCommands());
        getCommand("nangcap").setExecutor(new MainCommands());
        getCommand("mine").setExecutor(new MainCommands());
        getCommand("doiso").setExecutor(new MainCommands());
        getCommand("lvtop").setExecutor(new MainCommands());
        getCommand("sellall").setExecutor(new MainCommands());
        getCommand("help").setExecutor(new MainCommands());
        getCommand("choden").setExecutor(new MainCommands());
        getCommand("prestige").setExecutor(new MainCommands());
    }

    public ProtocolManager getProtocolLib() {
        return protocolManager;
    }

//	private void runMeteorTask() {
//		int time =  Main.config.getInt("main.auto-save.interval") * 20 * 60;
//		meteorSpawn = Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
//			boolean brk = false;
//			@Override
//			public void run() {
//				ArrayList<Player> plist = new ArrayList<>();
//				plist.addAll(  Bukkit.getWorld("world").getPlayers());
//				for (Location l : Meteor.meteorTask.keySet()) {
//					Meteor.removeMeteor(l.getWorld().getBlockAt(l));
//				}
//				for(int c = 0; c <= Bukkit.getWorld("world").getPlayers().size() ; c++){
//					if(Bukkit.getOnlinePlayers().size() < 10) break;
//					Player player = Bukkit.getWorld("world").getPlayers().get(ThreadLocalRandom.current().nextInt(0, plist.size()-1));
//					plist.remove(player);
//
//					Location l = player.getLocation();
//					ApplicableRegionSet a = Main.getWG().getRegionManager(player.getWorld())
//							.getApplicableRegions(player.getLocation());
//
//					for (ProtectedRegion b : a.getRegions()) {
//						if (b.getId().contains("mine")) {
//							String id = b.getId();
////							String name = null;
////							switch (id) {
////							case ("mine1"):
////								name = ChatColor.RESET + "Hang Động";
////								break;
////							case ("mine2"):
////								name = ChatColor.YELLOW + "Hoang Mạc";
////								break;
////							case ("mine3"):
////								name = ChatColor.RED + "Đảo Núi Lửa";
////								break;
////							case ("mine4"):
////								name = ChatColor.AQUA + "Khu Mỏ Bỏ Hoang";
////								break;
////							}
//							int totallevel = 0;
//							int number = 0;
//							Material m = Material.COAL_ORE;
//							for(Player z :Bukkit.getOnlinePlayers()){
//								Prisoner zz = Main.getMain().getPrisoners().get(z.getUniqueId());
//								number++;
//								totallevel += zz.getLevel();
//							}
//							int lv =(int) totallevel/number;
//							if(lv > 20 && lv <= 30){
//								m = Material.IRON_ORE;
//							}
//							if(lv > 30 && lv <= 40){
//								m = Material.GOLD_ORE;
//							}
//							if(lv > 40 && lv <= 60){
//								m = Material.LAPIS_ORE;
//							}
//							if(lv > 60 && lv <= 70){
//								m = Material.REDSTONE_ORE;
//							}
//							if(lv > 70){
//								m = Material.DIAMOND_ORE;
//							}
//
//							Meteor.spawnMeteor(l, m, false);
//							brk = true;
//							break;
//
//						} else {
//							continue;
//						}
//					}
//					if(brk){
//						break;
//					}
//				}
//			}
//		}, time, time);
//     }


    @SuppressWarnings("deprecation")
    @Override
    public void onEnable() {

        Server server = Bukkit.getServer();
        prisonmineapi = PrisonMines.getAPI();
        protocolManager = ProtocolLibrary.getProtocolManager();
        console = server.getConsoleSender();
        em = new EffectManager(this);
        lpapi = LuckPerms.getApi();
        ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
        registerallEvents();
        registercommands();
        loadConfigFile();
        config.options().copyDefaults(true);

        if (getServer().getPluginManager().isPluginEnabled(this)) {
            this.logger.info("Loaded config.yml successfully.");

            loadTasks();
            this.logger.info(this.pdfile.getName() + " v" + this.pdfile.getVersion() + " enabled.");
        }
        setupEconomy();
        if (config.getConfigurationSection("enc") != null) {
            for (String s : config.getConfigurationSection("enc").getKeys(true)) {
                EnergyLevelup.getEnchantingPlayers().put(UUID.fromString(s), config.getItemStack("enc." + s));
                config.set("enc." + s, null);

            }
        }
        if (config.getConfigurationSection("booster") != null) {
            for (String z : config.getConfigurationSection("booster").getKeys(true)) {
                ExpBooster.Multiplying.put(UUID.fromString(z),
                        System.currentTimeMillis() + config.getLong("booster." + z));
                config.set("booster." + z, null);
            }
        }
        saveConfig();
        plugin = this;

        if (getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
            if (PlaceholderReplacer.class != null) {


                PlaceholderAPI.registerPlaceholder(this, "prison.getlevel", new PlaceholderReplacer() {

                    @Override
                    public String onPlaceholderReplace(PlaceholderReplaceEvent arg0) {
                        Player player = arg0.getPlayer();
                        if (player.isOnline()) {
                            Prisoner p = plugin.getPrisoners().get(player.getUniqueId());
                            return String.valueOf(p.getLevel());
                        }
                        return "";
                    }
                });
                PlaceholderAPI.registerPlaceholder(this, "prison.getexp", new PlaceholderReplacer() {

                    @Override
                    public String onPlaceholderReplace(PlaceholderReplaceEvent arg0) {
                        Player player = arg0.getPlayer();
                        if (player.isOnline()) {
                            Prisoner p = plugin.getPrisoners().get(player.getUniqueId());
                            return String.valueOf(p.getEXP());
                        }
                        return "";
                    }
                });
                PlaceholderAPI.registerPlaceholder(this, "prison.getmaxexp", new PlaceholderReplacer() {

                    @Override
                    public String onPlaceholderReplace(PlaceholderReplaceEvent arg0) {
                        Player player = arg0.getPlayer();
                        if (player.isOnline()) {
                            Prisoner p = plugin.getPrisoners().get(player.getUniqueId());
                            if (Main.getCf().getString("Level." + (p.getLevel() + 1)) != null) {
                                return Main.getCf().getString("Level." + (p.getLevel() + 1));
                            } else {
                                return "";
                            }
                        }
                        return "";
                    }
                });
                PlaceholderAPI.registerPlaceholder(this, "prison.booster", new PlaceholderReplacer() {

                    @Override
                    public String onPlaceholderReplace(PlaceholderReplaceEvent arg0) {
                        Player player = arg0.getPlayer();
                        return ExpBooster.timeleft(player);

                    }
                });

            }
        }
        updateGBtask = Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {

            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                ExpBooster.gbUpdate();

            }
        }, 0, 2);
        boosbartask = new BukkitRunnable() {
            @Override
            public void run() {
                if (ExpBooster.IsGlobalBoosting()) {
                    for (Player p : getServer().getOnlinePlayers()) {

                        BossBar bb;
                        if (!BossBarAPI.hasBar(p)) {
                            bb = BossBarAPI.addBar(p, new TextComponent(ChatColor.RED + Bukkit.getOfflinePlayer(ExpBooster.gbBooster()).getName()
                                    + ChatColor.GREEN + " Đang Nhân Đôi Kinh Nghiệm Toàn Server, Thời Gian Còn Lại "
                                    + ChatColor.BLUE + ExpBooster.Globaltimeleft()), BossBarAPI.Color.RED, BossBarAPI.Style.NOTCHED_20, 1f);
                        } else {
                            bb = BossBarAPI.getBossBar(p);
                        }
                        if (!bb.isVisible()) bb.setVisible(true);
                        for (Long z : ExpBooster.gb.values()) {
                            bb.setProgress((float) (z - System.currentTimeMillis()) / 3600000);
                        }
                        bb.setMessage(ChatColor.RED + Bukkit.getOfflinePlayer(ExpBooster.gbBooster()).getName()
                                + ChatColor.GREEN + " Đang Nhân Đôi Kinh Nghiệm Toàn Server, Thời Gian Còn Lại "
                                + ChatColor.BLUE + ExpBooster.Globaltimeleft());


                    }
                } else {
                    for (Player p : getServer().getOnlinePlayers()) {
                        if (BossBarAPI.hasBar(p)) {
                            BossBarAPI.removeAllBars(p);

                        }
                    }

                }
            }
        }.runTaskTimer(this, 0, 20);
        Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {

            @Override
            public void run() {
                LevelTop.updateArrayList();

            }
        });
        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().size() >= 50) {
                    Bukkit.broadcastMessage(ChatColor.RED + "Drop Party sẽ bắt đầu trong vòng 1 phút , đừng đi đâu cả");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ArrayList<ItemStack> a = DropItems();
                            ArrayList<String> b = DropCommand();
                            for (ItemStack is : a) {

                                Player player = (Player) Bukkit.getOnlinePlayers().toArray()[new Random().nextInt(Bukkit.getOnlinePlayers().toArray().length)];
                                player.getInventory().addItem(is);
                                Firework fw = (Firework) player.getWorld().spawnEntity(player.getEyeLocation().clone(), EntityType.FIREWORK);
                                FireworkMeta fwm = fw.getFireworkMeta();
                                Random r = new Random();

                                int rt = r.nextInt(4) + 1;
                                FireworkEffect.Type type = FireworkEffect.Type.BALL;
                                if (rt == 1)
                                    type = FireworkEffect.Type.BALL;
                                if (rt == 2)
                                    type = FireworkEffect.Type.BALL_LARGE;
                                if (rt == 3)
                                    type = FireworkEffect.Type.BURST;
                                if (rt == 4)
                                    type = FireworkEffect.Type.STAR;

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
                            for (String s : b) {
                                Player player = (Player) Bukkit.getOnlinePlayers().toArray()[new Random().nextInt(Bukkit.getOnlinePlayers().toArray().length)];
                                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), s.replace("%player%", player.getName()));
                                Firework fw = (Firework) player.getWorld().spawnEntity(player.getEyeLocation().clone(), EntityType.FIREWORK);
                                FireworkMeta fwm = fw.getFireworkMeta();
                                Random r = new Random();

                                int rt = r.nextInt(4) + 1;
                                FireworkEffect.Type type = FireworkEffect.Type.BALL;
                                if (rt == 1)
                                    type = FireworkEffect.Type.BALL;
                                if (rt == 2)
                                    type = FireworkEffect.Type.BALL_LARGE;
                                if (rt == 3)
                                    type = FireworkEffect.Type.BURST;
                                if (rt == 4)
                                    type = FireworkEffect.Type.STAR;

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
                        }
                    }.runTaskLater(Main.getMain(), 20 * 60);
                }
            }
        }, 20 * 30 * 60, 20 * 30 * 60);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                ArrayList<Entity> removethis = new ArrayList<Entity>();
                for (Entity e : ParticleList.keySet()) {
                    if (ParticleList.containsKey(e)) {
                        if (e.isValid()) {
                            for (ParticleEffect ef : ParticleList.get(e))
                                ef.send(Util.getNearbyPlayerAsync(e.getLocation(), 100d), e.getLocation(), 0, 0, 0, 0, 1);
                        } else {
                            removethis.add(e);
                        }
                    }
                }
                for (Entity z : removethis) {
                    ParticleList.remove(z);
                }
            }
        }, 0, 1);
        hookPlayerPoints();
    }

    public static HashMap<Entity, ArrayList<ParticleEffect>> getParticleList() {
        return ParticleList;
    }

    public static PlayerPoints getPlayerPoints() {
        return playerPoints;
    }

    private boolean hookPlayerPoints() {
        final Plugin plugin = this.getServer().getPluginManager().getPlugin("PlayerPoints");
        playerPoints = PlayerPoints.class.cast(plugin);
        return playerPoints != null;
    }

    public static ArrayList<ItemStack> DropItems() {
        ArrayList<ItemStack> a = new ArrayList<>();
        for (int i = 0; i <= 50; i++) {

            a.add(new ItemStack(Material.DIAMOND_ORE, 64));
            a.add(new ItemStack(Material.REDSTONE_ORE, 64));
            a.add(new ItemStack(Material.EMERALD_ORE, 64));
            a.add(new ItemStack(Material.LAPIS_ORE, 64));
            a.add(new ItemStack(Material.DIAMOND_BLOCK, 64));
            a.add(new ItemStack(Material.REDSTONE_BLOCK, 64));
            a.add(new ItemStack(Material.LAPIS_BLOCK, 64));
            a.add(new ItemStack(Material.EMERALD_BLOCK, 64));
            a.add(new ItemStack(Material.GOLD_BLOCK, 64));
            a.add(new ItemStack(Material.OBSIDIAN, 64));
        }
        for (int i = 0; i <= 50; i++) {

            a.add(Shard.getShard(PEnchantTier.SIMPLE));
            a.add(Shard.getShard(PEnchantTier.UNCOMMON));
            a.add(Shard.getShard(PEnchantTier.ELITE));
            a.add(Shard.getShard(PEnchantTier.ULTIMATE));
            a.add(Shard.getShard(PEnchantTier.LEGENDARY));
        }
        for (int i = 0; i <= 50; i++) {
            a.add(Energy.getRawEnergy(ThreadLocalRandom.current().nextInt(500, 10000)));
        }


        return a;
    }

    public static ArrayList<String> DropCommand() {
        ArrayList<String> a = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            a.add("p give %player% 10");
        }
        for (int i = 0; i <= 10; i++) {
            a.add("eco give %player% 100000");
        }
        return a;
    }

    @SuppressWarnings("unused")
    private void setupLevelConfig() {
        for (int i = 1; i < 101; i++) {
            if (i >= 21) {
                config.set("Level." + i, Math.pow(10 * i * 2, 2));
            } else {
                config.set("Level." + i, Math.pow(10 * i, 2));
            }

        }
    }

    @Override
    public void onDisable() {
        em = new EffectManager(this);
        em.dispose();
        if (!this.disable) {

            this.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Saving Player Data...");
            for (Player player : getServer().getOnlinePlayers()) {
                this.Prisoners.get(player.getUniqueId()).saveData();
                player.kickPlayer(ChatColor.RED + "Server Closed");

            }
            for (UUID u : EnergyLevelup.getEnchantingPlayers().keySet()) {
                getConfig().set("enc." + u, EnergyLevelup.getEnchantingPlayers().get(u));
            }
            for (UUID z : ExpBooster.Multiplying.keySet()) {
                getConfig().set("booster." + z, ExpBooster.Multiplying.get(z) - System.currentTimeMillis());
            }

            this.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Player Data saved");
            if (this.saveTask != null) {
                this.saveTask.cancel();
            }
        }
        Bukkit.getScheduler().cancelTask(boosbartask.getTaskId());
        Bukkit.getScheduler().cancelTask(updateGBtask.getTaskId());
        saveConfig();
        plugin = null;

    }

    // Register Money'sss

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
                .getRegistration(Economy.class);
        if (economyProvider != null) {
            Main.economy = ((Economy) economyProvider.getProvider());
        }
    }

    // get plugin nek
    public static Main getMain() {
        return plugin;
    }

    public static Economy getEconomy() {
        return economy;
    }

    private void loadTasks() {
        if (Main.config.getBoolean("main.auto-save.enable")) {
            int savePeriod = Main.config.getInt("main.auto-save.interval");
            this.saveTask = new BukkitRunnable() {
                public void run() {

                    LevelTop.updateArrayList();
                    Main plugin = (Main) Bukkit.getPluginManager().getPlugin("SPrison");
                    plugin.getLogger().info("Saving Player Data.");
                    for (Map.Entry<UUID, Prisoner> entry : plugin.getPrisoners().entrySet()) {
                        ((Prisoner) entry.getValue()).saveData();
                    }
                    plugin.getLogger().info("Saved Player Data");

                }
            }.runTaskTimerAsynchronously(this, savePeriod * 60 * 20, savePeriod * 60 * 20);
        }
    }

    public ConcurrentHashMap<UUID, Prisoner> getPrisoners() {
        return this.Prisoners;
    }

    public void loadConfigFile() {
        saveDefaultConfig();
        Main.config = getConfig();
    }

    public void reloadConfigFile() {
        reloadConfig();
        loadConfigFile();
        this.saveTask.cancel();
        loadTasks();
    }

    public static FileConfiguration getCf() {
        return config;
    }

    public static String fixFontSize(String s, int size) {

        String ret = s.toUpperCase();

        if (s != null) {

            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == 'I' || s.charAt(i) == ' ') {
                    ret += " ";
                }
            }

            int faltaEspacos = size - s.length();
            faltaEspacos = (faltaEspacos * 2);

            for (int i = 0; i < faltaEspacos; i++) {
                ret += " ";
            }
        }

        return (ret);
    }

    public static WorldGuardPlugin getWG() {
        Plugin plugin = Main.getMain().getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null; // Maybe you want throw an exception instead
        }

        return (WorldGuardPlugin) plugin;
    }

    public static HashMap<Material, Integer> getBlocksFromCf() {
        HashMap<Material, Integer> a = new HashMap<>();
        for (String b : Main.getCf().getConfigurationSection("block").getKeys(true)) {
            a.put(Material.matchMaterial(b), Main.getCf().getInt("block." + b));

        }
        return a;

    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static Essentials getEss() {
        return ess;
    }

    public static LuckPermsApi getLPAPI() {
        return lpapi;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent e) {
        ItemStack i = e.getItemDrop().getItemStack();
        Player p = e.getPlayer();
        String name;
        Location loc = p.getLocation();
        String vitri = loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
        if (i.hasItemMeta() && i.getItemMeta().hasDisplayName()) {
            name = i.getItemMeta().getDisplayName();
        } else {
            name = i.getType().toString();
        }
        getLogger().info(p.getName() + " da drop " + name + " tai " + vitri);
    }

    public static MineAPI getPM() {
        return prisonmineapi;
    }

    ;

}
