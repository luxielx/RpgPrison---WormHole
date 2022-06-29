package Commands;

import CustomEvents.ExpEvent;
import Enchantment.*;
import Listeners.*;
import Menus.*;
import NPC.CreateNPC;
import Solar.prison.Main;
import Solar.prison.Prisoner;
import Utils.LevelTop;
import Utils.Util;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.Random;

public class MainCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
//        try {
        if (cmd.getName().equalsIgnoreCase("prison") && sender.hasPermission("prison.admin")) {
            if (args.length <= 0) {
                sender.sendMessage(ChatColor.GREEN + "/prison setstat <Player> exp/level <amount> // Set Player's level/exp");
                sender.sendMessage(ChatColor.GREEN + "/prison reload//Reload config file");
                sender.sendMessage(ChatColor.GREEN + "/prison enchant list // List of Enchantments");
                sender.sendMessage(ChatColor.GREEN + "/prison enchant getbook <EnumName> <Level>// Get Enchant Book");
                sender.sendMessage(ChatColor.GREEN + "/prison enchant add <EnumName> <Level>//  Add Enchantment to tool on your hand");
                sender.sendMessage(ChatColor.GREEN + "/prison enchant remove <EnumName>//  Remove an enchantment");
                sender.sendMessage(ChatColor.GREEN + "/prison enchant prevent //  anti qlpp");
                sender.sendMessage(ChatColor.GREEN + "/prison energy upgrade // Upgrade Item without Drop it to wormhole");
                sender.sendMessage(ChatColor.GREEN + "/prison energy prevent // Dont mind this ");
                sender.sendMessage(ChatColor.GREEN + "/prison energy add // Dont mind this ");
                sender.sendMessage(ChatColor.GREEN + "/prison energy delete // Dont mind this");
                sender.sendMessage(ChatColor.GREEN + "/prison energy giveraw <Amount> //  Give player <Amount> of raw energy");
                sender.sendMessage(ChatColor.GREEN + "/prison dust give <Player> <Tier> // Give player Enchant dust");
                sender.sendMessage(ChatColor.GREEN + "/prison npc spawn extractor // &c Spawn extractor NPC");
                sender.sendMessage(ChatColor.GREEN + "/prison shard give <Player> <Tier>// &cGive player Shard");
                sender.sendMessage(ChatColor.GREEN + "/prison orb give <Player> // give player protection orb ");
                sender.sendMessage(ChatColor.GREEN + "/prison boost <Player> global/self // active boost ");
                sender.sendMessage(ChatColor.GREEN + "/prison upgrade givescroll <Player> // give player angel scroll");
                sender.sendMessage(ChatColor.GREEN + "/prison upgrade givedust <Player> // give player lucky dust");
                sender.sendMessage(ChatColor.GREEN + "/prison hammer give <Player> // give player fixing hammer");
                sender.sendMessage(ChatColor.GREEN + "/prison warp <Player> <warp> // teleport player to warp");
                sender.sendMessage(ChatColor.GREEN + "/prison dropparty // drop party");
                sender.sendMessage(ChatColor.GREEN + "/prison clearshard // clear shard");
                sender.sendMessage(ChatColor.GREEN + "/prison clearholo // clear holo");
                sender.sendMessage(ChatColor.GREEN + "/prison resetmarket // resetmarket");
                sender.sendMessage(ChatColor.GREEN + "/prison setmaxmob 70 // setmaxmob in dungeon");
//				sender.sendMessage(ChatColor.GREEN + "/prison meteor coal_ore false");
//				sender.sendMessage(ChatColor.GREEN + "/prison meteor remove");
                return false;
            }
            if (args[0].equalsIgnoreCase("setmaxmob")) {
                Dungeon.setMaxMob(Integer.valueOf(args[1]));
                sender.sendMessage("Max mob set to " + args[1]);
            }
            if(args[0].equalsIgnoreCase("test")){
                Location loc = null;
                int tried = 0;
                while(!Util.isSafeLocation(loc)) {
                    if(tried >= 50) break;
                    loc = Util.randomLocation(new Location(Bukkit.getWorld("Dungeon"), -41, 31, 469), new Location(Bukkit.getWorld("Dungeon"), -154, 70, 395));

                    tried++;
                }
                ((Player)sender).teleport(loc);
            }
            if (args[0].equalsIgnoreCase("clearholo") && args.length > 0) {
                int i = Integer.valueOf(args[1]);
                for (Entity s : ((Player) sender).getNearbyEntities(i, i, i)) {
                    if (s.getType() == EntityType.ARMOR_STAND) s.remove();

                }
            }
            if (args[0].equalsIgnoreCase("clearshard")) {
                for (World w : Bukkit.getServer().getWorlds()) {
                    for (Entity s : w.getEntities()) {
                        if (s.getType() == EntityType.ARMOR_STAND) {
                            if (!s.getMetadata("solar.upgrade.armorstand").isEmpty()) {
                                s.remove();
                            }
                        }
                    }
                }
            }
            if (args[0].equalsIgnoreCase("warp") && args.length > 2) {
                Player player = Bukkit.getPlayer(args[1]);
                Teleportation.teleportToWarp(player, args[2]);
            }
            if (args[0].equalsIgnoreCase("resetmarket")) {
                Player player = (Player) sender;
                RandomItemGui.createInv();
            }
            if (args[0].equalsIgnoreCase("dropparty")) {
                ArrayList<ItemStack> a = Main.DropItems();
                ArrayList<String> b = Main.DropCommand();
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
            if (args[0].equalsIgnoreCase("upgrade") && args.length > 1) {
                Player player = Bukkit.getPlayer(args[2]);
                if (args[1].equalsIgnoreCase("givedust")) {
                    player.getInventory().addItem(EnchantCombine.Dust());
                }
                if (args[1].equalsIgnoreCase("givescroll")) {
                    player.getInventory().addItem(EnchantCombine.protectionScroll());
                }
            }
            if (args[0].equalsIgnoreCase("boost") && args.length > 1) {

                if (args[2].equalsIgnoreCase("global")) {
                    ExpBooster.addBooster(Bukkit.getPlayer(args[1]).getUniqueId());
                } else if (args[2].equalsIgnoreCase("self")) {
                    Bukkit.getPlayer(args[1]).getInventory().addItem(ExpBooster.getExpBottle());
                }

            }
            if (args[0].equalsIgnoreCase("orb") && args.length > 1) {
                ((Player) sender).getInventory().addItem(PEnchant.ProtectionORB());
            }
            // /prison setstat SolarFavor exp 10

            if (args[0].equalsIgnoreCase("reload")) {
                Main.getMain().reloadConfigFile();
                sender.sendMessage(ChatColor.RED + "Đã reload config thành công");
            }
            if (args[0].equalsIgnoreCase("shard") && args.length > 3) {
                if (args[1].equalsIgnoreCase("give")) {
                    Player player = Bukkit.getPlayer(args[2]);
                    PEnchantTier tier = PEnchantTier.valueOf(args[3].toUpperCase());
                    player.getInventory().addItem(Shard.getShard(tier));

                }

            }
            if (args[0].equalsIgnoreCase("hammer") && args.length > 2) {
                if (args[1].equalsIgnoreCase("give")) {
                    Player player = Bukkit.getPlayer(args[2]);
                    player.getInventory().addItem(RepairItem.FixingHammer());

                }

            }
            if (args[0].equalsIgnoreCase("npc")) {

                if (args[1].equalsIgnoreCase("spawn")) {
                    Player player = (Player) sender;
                    if (args[2].equalsIgnoreCase("extractor")) {
                        CreateNPC.CreateRawExtractor(player.getLocation());
                    }
                    if (args[2].equalsIgnoreCase("guard")) {
                        CreateNPC.createGuard(player.getLocation());
                    }
                }
                if (args[1].equalsIgnoreCase("resetskin")) {
                    NPCRegistry registry = CitizensAPI.getNPCRegistry();
                    for (org.bukkit.World s : Bukkit.getServer().getWorlds()) {
                        for (LivingEntity z : s.getLivingEntities()) {
                            if (!z.getMetadata("NPC").isEmpty()) {
                                NPC zxc = registry.getNPC(z);

                                if (zxc.getName().contains(ChatColor.RED + "Phân Giải")) {
                                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                                            "npc sel " + zxc.getId());
                                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "npc skin 1918_m8 ");
                                }

                            }
                        }
                    }

                }
                if (args[1].equalsIgnoreCase("reset")) {
                    NPCRegistry registry = CitizensAPI.getNPCRegistry();
                    for (org.bukkit.World s : Bukkit.getServer().getWorlds()) {
                        for (LivingEntity z : s.getLivingEntities()) {
                            if (!z.getMetadata("NPC").isEmpty()) {
                                NPC zxc = registry.getNPC(z);

                                if (zxc.getName().contains(ChatColor.RED + "Vệ Binh")) {

                                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                                            "npc sel " + zxc.getId());
                                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "npc remove");

                                }

                            }
                        }
                    }

                }
            }
            if (args[0].equalsIgnoreCase("dust") && args.length > 3) {
                if (args[1].equalsIgnoreCase("give")) {
                    Player player = Bukkit.getPlayer(args[2]);
                    PEnchantTier tier = PEnchantTier.valueOf(args[3].toUpperCase());
                    player.getInventory().addItem(Dust.getDust(tier));

                }
            }

            if (args[0].equalsIgnoreCase("addblock")) {
                if (args.length > 2) {
                    Main.getMain().getConfig().set("block." + args[1], Integer.parseInt(args[2]));
                    Main.getMain().saveConfig();
                    Main.getMain().reloadConfigFile();

                    sender.sendMessage(ChatColor.GREEN + "block " + args[1] + " đã đi thêm vào config thành công");

                } else {
                    sender.sendMessage(ChatColor.GREEN + "/prison addblock STONE 10");
                }
            }

            if (args[0].equalsIgnoreCase("enchant") && sender instanceof Player) {
                Player player = (Player) sender;
                if (args[1].equalsIgnoreCase("getlevel")) {
                    player.sendMessage(ChatColor.RED + "" + RandomItem.getItemLevel(player.getItemInHand()) + "");
                }
                if (args[1].equalsIgnoreCase("list")) {
                    StringBuilder sb = new StringBuilder();
                    for (PEnchant pen : PEnchant.values()) {
                        sb.append(pen.getTier().getNameColor() + pen.toString() + " ");
                    }
                    sender.sendMessage(sb.toString());

                    EnchantslistGUI.openEnchant(player);

                }
                if (args[1].equalsIgnoreCase("prevent")) {
                    if (player.getItemInHand() == null) return false;
                    if (player.getItemInHand().getType() == Material.AIR) return false;
                    PEnchant.preventBookWithdraw(player.getItemInHand());


                }
                if (args[1].equalsIgnoreCase("getbook") && args.length > 3) {
                    String enchantname = args[2].toUpperCase();
                    int level = Integer.parseInt(args[3]);
                    player.getInventory().addItem(PEnchant.valueOf(enchantname).getBook(level));
                }
                if (args[1].equalsIgnoreCase("add") && args.length > 3) {
                    String enchantname = args[2].toUpperCase();
                    int level = Integer.parseInt(args[3]);
                    PEnchant.valueOf(enchantname).apply(player.getItemInHand(), level);

                }
                if (args[1].equalsIgnoreCase("remove") && args.length > 2) {
                    String enchantname = args[2].toUpperCase();
                    PEnchant.valueOf(enchantname).remove(player.getItemInHand());

                }
                return true;

            }
            if (args[0].equalsIgnoreCase("energy") && sender instanceof Player) {
                Player player = (Player) sender;
                ItemStack is = player.getItemInHand();
                if (args[1].equalsIgnoreCase("upgrade")) {
                    Energy.levelUpEnergy(is);
                }
                if (args[1].equalsIgnoreCase("add")) {
                    Energy.addEnergy(is, 0);
                }
                if (args[1].equalsIgnoreCase("delete")) {
                    Energy.deleteEnergy(is);
                }
                if (args[1].equalsIgnoreCase("prevent")) {
                    PEnchant.preventEnergyLevelup(player.getItemInHand());
                }
                if (args[1].equalsIgnoreCase("give") && args.length > 1) {
                    player.getInventory().addItem(Energy.getRawEnergy(Integer.valueOf(args[2])));

                }

            }
            if (args[0].equalsIgnoreCase("setstat")) {
                if (args.length > 3) {
                    Player specify = Bukkit.getPlayer(args[1]);
                    Prisoner specified = Main.getMain().getPrisoners().get(specify.getUniqueId());
                    if (args[2].equalsIgnoreCase("Level")) {
                        double number = Double.parseDouble(args[3]);
                        specified.setLevel(number);
                        sender.sendMessage(ChatColor.RED + "Change " + specify.getName() + " Level to " + args[3]);
                    }
                    if (args[2].equalsIgnoreCase("EXP")) {
                        double number = Double.parseDouble(args[3]);
                        specified.setEXP(number);
                        sender.sendMessage(ChatColor.RED + "Change " + specify.getName() + " EXP to " + args[3]);
                        Bukkit.getServer().getPluginManager().callEvent(new ExpEvent(specify, number));

                    }
                } else {
                    sender.sendMessage("/prison setstat SolarFavor exp 10");
                }
            } else if (args[0].equalsIgnoreCase("addstat")) {
                if (args.length > 3) {
                    Player specify = Bukkit.getPlayer(args[1]);

                    Prisoner specified = Main.getMain().getPrisoners().get(specify.getUniqueId());
                    if (args[2].equalsIgnoreCase("Level")) {
                        double number = Double.parseDouble(args[3]);

                        specified.setEXP(Main.getCf().getInt("Level." + args[3]) + specified.getEXP());
                        sender.sendMessage(ChatColor.RED + "Gave " + specify.getName() + args[3] + " Level");
                        Bukkit.getServer().getPluginManager().callEvent(
                                new ExpEvent(specify, Main.getCf().getInt("Level." + number) + specified.getEXP()));
                    }
                    if (args[2].equalsIgnoreCase("EXP")) {
                        double number = Double.parseDouble(args[3]);
                        specified.setEXP(number + specified.getEXP());
                        sender.sendMessage(ChatColor.RED + "Gave " + specify.getName() + args[3] + " EXP");
                        Bukkit.getServer().getPluginManager()
                                .callEvent(new ExpEvent(specify, number + specified.getEXP()));
                    }
                } else {
                    sender.sendMessage("/prison addstat SolarFavor exp 10");
                }

            }
            return true;

        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("en")) {
                EnchantslistGUI.openEnchant(player);
            }
            if (cmd.getName().equalsIgnoreCase("spawn")) {
                Teleportation.teleportToSpawn(player);
            }
            if (cmd.getName().equalsIgnoreCase("qlpp")) {

                QLPP.openEnchant(player);

            }
            if (cmd.getName().equalsIgnoreCase("doiso") && args.length > 0) {
                String s = args[0];
                try {
                    player.sendMessage(ChatColor.YELLOW + s + ChatColor.BLUE + " -> " + ChatColor.YELLOW + Util.convertToRoman(Integer.parseInt(args[0])));
                } catch (Exception e) {
                    if (Util.romanConvert(args[0]) != 0) {
                        player.sendMessage(ChatColor.YELLOW + s + ChatColor.BLUE + " -> " + ChatColor.YELLOW + Util.romanConvert(args[0]));
                    } else {
                        player.sendMessage(ChatColor.RED + "Số nhập vào phải là số la mã hoặc số thực");
                    }
                }
            }
            if (cmd.getName().equalsIgnoreCase("me")) {

                InfoMenu.openInfo(player, player);

            }
            if (cmd.getName().equalsIgnoreCase("sx")) {
                if (player.getItemInHand() != null)
                    player.setItemInHand(PEnchant.resolveItemStack(player.getItemInHand()));

            }
            if (cmd.getName().equalsIgnoreCase("nangcap")) {
                EnchantCombine.openEnchant((Player) sender);
            }
            if (cmd.getName().equalsIgnoreCase("lvtop")) {
                if (args.length > 0) {
                    player.sendMessage(ChatColor.GREEN + "Đang tải dữ liệu , vui lòng chờ chút");
                    Bukkit.getScheduler().runTaskAsynchronously(Main.getMain(), new Runnable() {

                        @Override
                        public void run() {

                            int page = Integer.valueOf(args[0]);
                            player.sendMessage(Util.getRandomChatColor() + "----------------------------");
                            player.sendMessage(ChatColor.RED + "Danh Sách Cấp Độ Cao Nhất , Trang " + page);
                            player.sendMessage(Util.getRandomChatColor() + "----------------------------");
                            int index = (page - 1) * 10;
                            for (Prisoner p : LevelTop.getTop(index, index + 11)) {
                                if (p == null)
                                    continue;
                                player.sendMessage(ChatColor.RED + "" + index + " " + ChatColor.BLUE
                                        + p.getPlayer().getName() + " " + ChatColor.RED + "Có cấp độ "
                                        + ChatColor.YELLOW + "" + p.getLevel());
                                index++;
                            }
                            player.sendMessage("");
                            player.sendMessage(ChatColor.YELLOW + "Vị trí của bạn " + ChatColor.AQUA
                                    + LevelTop.getPlace(player.getUniqueId()));

                        }
                    });

                } else {
                    player.sendMessage(ChatColor.GREEN + "Đang tải dữ liệu , vui lòng chờ chút");
                    Bukkit.getScheduler().runTaskAsynchronously(Main.getMain(), new Runnable() {

                        @Override
                        public void run() {

                            player.sendMessage(Util.getRandomChatColor() + "----------------------------");
                            player.sendMessage(ChatColor.RED + "Danh Sách Cấp Độ Cao Nhất , Trang 1");
                            player.sendMessage(Util.getRandomChatColor() + "----------------------------");
                            int index = 0;
                            for (Prisoner p : LevelTop.getTop(0, 10)) {
                                if (p == null)
                                    continue;
                                player.sendMessage(ChatColor.RED + "" + index + " " + ChatColor.BLUE
                                        + p.getPlayer().getName() + " " + ChatColor.RED + "Có cấp độ "
                                        + ChatColor.YELLOW + "" + p.getLevel());
                                index++;
                            }
                            player.sendMessage("");
                            player.sendMessage(ChatColor.YELLOW + "Vị trí của bạn " + ChatColor.AQUA
                                    + LevelTop.getPlace(player.getUniqueId()));
                        }
                    });
                }

            }
            if (cmd.getName().equalsIgnoreCase("xem") && args.length > 0) {
                @SuppressWarnings("deprecation")
                OfflinePlayer specify = Bukkit.getOfflinePlayer(args[0]);
                if (specify == null) {
                    player.sendMessage(ChatColor.RED + args[0] + " Không tồn tại");
                    return false;
                }
                InfoMenu.openInfo(player, specify);
            }
            if (cmd.getName().equalsIgnoreCase("mine")) {
                WarpGUI.openWarpGui(player);
            }
            if (cmd.getName().equalsIgnoreCase("prestige")) {
                Expmanager.LevelUpPrestige(player);
            }
            if (cmd.getName().equalsIgnoreCase("choden")) {
                RandomItemGui.openGUI(player);
            }
            if (cmd.getName().equalsIgnoreCase("help")) {
                player.sendMessage(ChatColor.GREEN + "/me " + ChatColor.YELLOW + ": " + ChatColor.BLUE + "Xem thông tin cá nhân");
                player.sendMessage(ChatColor.GREEN + "/choden " + ChatColor.YELLOW + ": " + ChatColor.BLUE + "Mở giao diện chợ đen");
                player.sendMessage(ChatColor.GREEN + "/hu " + ChatColor.YELLOW + ": " + ChatColor.BLUE + "Mở giao diện cánh");
                player.sendMessage(ChatColor.GREEN + "/spawn " + ChatColor.YELLOW + ": " + ChatColor.BLUE + "Trở về spawn");
                player.sendMessage(ChatColor.GREEN + "/xem <tên người chơi> " + ChatColor.YELLOW + ": " + ChatColor.BLUE + "Xem thông tin người chơi khác");
                player.sendMessage(ChatColor.GREEN + "/menu " + ChatColor.YELLOW + ": " + ChatColor.BLUE + "Mở menu chính");
                player.sendMessage(ChatColor.GREEN + "/warp " + ChatColor.YELLOW + ": " + ChatColor.BLUE + "Mở danh sách dịch chuyển");
                player.sendMessage(ChatColor.GREEN + "/mine " + ChatColor.YELLOW + ": " + ChatColor.BLUE + "Mở danh sách mỏ");
                player.sendMessage(ChatColor.GREEN + "/qlpp " + ChatColor.YELLOW + ": " + ChatColor.BLUE + "Mở giao diện quản lý phù phép");
                player.sendMessage(ChatColor.GREEN + "/nc " + ChatColor.YELLOW + ": " + ChatColor.BLUE + "Mở giao diện nâng cấp");
                player.sendMessage(ChatColor.GREEN + "/plot " + ChatColor.YELLOW + ": " + ChatColor.BLUE + "Các lệnh plot");
                player.sendMessage(ChatColor.GREEN + "/sa " + ChatColor.YELLOW + ": " + ChatColor.BLUE + "Bán hết khối [ cần có quyền ]");
                player.sendMessage(ChatColor.GREEN + "/en " + ChatColor.YELLOW + ": " + ChatColor.BLUE + "Xem danh sách phù phép");
                player.sendMessage(ChatColor.GREEN + "/lvtop " + ChatColor.YELLOW + ": " + ChatColor.BLUE + "Xem danh sách top level");
                player.sendMessage(ChatColor.GREEN + "/doiso <Số la mã / Số thường>] " + ChatColor.YELLOW + ": " + ChatColor.BLUE + "Đổi từ số la mã sang số thường hoặc ngược lại");
                player.sendMessage("Truy Cập: http://mine108.com/prison2/ để xem các hướng dẫn ");
            }
            if (cmd.getName().equalsIgnoreCase("sellall") && player.hasPermission("prison.sellall")) {
                SellEvent.SellAllItem(player);
            }
            return true;

        }
//        } catch (Exception buoi) {
//        }
        return false;
    }


}
