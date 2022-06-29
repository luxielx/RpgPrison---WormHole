package Utils;

import Solar.prison.Main;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import io.netty.util.internal.ThreadLocalRandom;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.inventivetalent.particle.ParticleEffect;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Util {
    public static ChatColor getRandomChatColor() {
        ArrayList<ChatColor> list = new ArrayList<>();
        for (ChatColor c : ChatColor.values()) {
            if (c != ChatColor.MAGIC && c != ChatColor.STRIKETHROUGH && c != ChatColor.BOLD && c != ChatColor.ITALIC && c != ChatColor.UNDERLINE) {
                list.add(c);
            }
        }
        return list.get(ThreadLocalRandom.current().nextInt(0, list.size()));
    }


    public static Color getColor(int i) {
        Color c = null;
        if (i == 1) {
            c = Color.AQUA;
        }
        if (i == 2) {
            c = Color.BLACK;
        }
        if (i == 3) {
            c = Color.BLUE;
        }
        if (i == 4) {
            c = Color.FUCHSIA;
        }
        if (i == 5) {
            c = Color.GRAY;
        }
        if (i == 6) {
            c = Color.GREEN;
        }
        if (i == 7) {
            c = Color.LIME;
        }
        if (i == 8) {
            c = Color.MAROON;
        }
        if (i == 9) {
            c = Color.NAVY;
        }
        if (i == 10) {
            c = Color.OLIVE;
        }
        if (i == 11) {
            c = Color.ORANGE;
        }
        if (i == 12) {
            c = Color.PURPLE;
        }
        if (i == 13) {
            c = Color.RED;
        }
        if (i == 14) {
            c = Color.SILVER;
        }
        if (i == 15) {
            c = Color.TEAL;
        }
        if (i == 16) {
            c = Color.WHITE;
        }
        if (i == 17) {
            c = Color.YELLOW;
        }

        return c;
    }

    public static ItemStack getRandomItemStackFromArray(ArrayList<ItemStack> array) {
        try {
            return array.get(ThreadLocalRandom.current().nextInt(array.size()));
        } catch (Throwable e) {
            return null;
        }
    }

    public static ArrayList<Player> getNearbyPlayer(Location player, double i) {
        ArrayList<Player> list = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
        for (Entity online : player.getWorld().getNearbyEntities(player, i, i, i)) {
            if (online instanceof Player) {
                list.add((Player) online);
            }
        }
        return list;
    }

    public static ArrayList<Player> getNearbyPlayerAsync(Location player, double radius) {
        World world = player.getWorld();
        ArrayList<Player> list = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            if (online.getWorld().getName().equalsIgnoreCase(world.getName())) {
                if (online.getLocation().distance(player) > radius) {
                    list.remove(online);
                }
            }
        }
        return list;
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

    public static String hideS(String s) {
        String hidden = "";
        for (char c : s.toCharArray())
            hidden += ChatColor.COLOR_CHAR + "" + c;
        return hidden;
    }

    public static String unhideS(String s) {
        String r = s.replaceAll("§", "");
        return r;
    }

    public static String colorCode(String s) {

        return s.replaceAll("&", "§");

    }

    public static long getBalance(Player player) {
        Economy econ = Main.getEconomy();
        return Math.round(econ.getBalance(player));
    }

    public static void setBalance(Player player, Number newBalance) {
        Economy econ = Main.getEconomy();
        double currentBalance = econ.getBalance(player);
        econ.withdrawPlayer(player, currentBalance);
        econ.depositPlayer(player, newBalance.longValue());
    }

    public static void giveMoney(Player player, Number amount) {
        Economy econ = Main.getEconomy();
        econ.depositPlayer(player, amount.longValue());
    }

    public static void takeMoney(Player player, Number amount) {
        Economy econ = Main.getEconomy();
        econ.withdrawPlayer(player, amount.longValue());
    }

    @SuppressWarnings("deprecation")
    public static void changeChestState(Location loc, boolean open) {
        if (open) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.playNote(loc, (byte) 1, (byte) 1);
            }
        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.playNote(loc, (byte) 1, (byte) 0);
            }
        }
    }

    public static void sendTitleBar(Player p, String title, String subtitle, int fadein, int fadeout) {
        CraftPlayer cp = (CraftPlayer) p;
        PacketPlayOutTitle packettitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE,
                IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + title + "\"}"), 0, fadein, fadeout);
        cp.getHandle().playerConnection.sendPacket(packettitle);

        PacketPlayOutTitle packetsubtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE,
                IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + subtitle + "\"}"), 0, fadein, fadeout);
        cp.getHandle().playerConnection.sendPacket(packetsubtitle);
    }

    public static void sendActionBar(Player p, String message) {
        CraftPlayer cp = (CraftPlayer) p;
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        cp.getHandle().playerConnection.sendPacket(ppoc);
    }

    public static String convertToRoman(int mInt) {
        if (mInt >= 1000) return "I";
        String[] rnChars = {"M", "CM", "D", "C", "XC", "L", "X", "IX", "V", "I"};
        int[] rnVals = {1000, 900, 500, 100, 90, 50, 10, 9, 5, 1};
        String retVal = "";

        for (int i = 0; i < rnVals.length; i++) {
            int numberInPlace = mInt / rnVals[i];
            if (numberInPlace == 0)
                continue;
            retVal += numberInPlace == 4 && i > 0 ? rnChars[i] + rnChars[i - 1]
                    : new String(new char[numberInPlace]).replace("\0", rnChars[i]);
            mInt = mInt % rnVals[i];
        }
        return retVal;
    }

    public static int romanConvert(String roman) {
        String romanNumeral = roman.toUpperCase();

        boolean valid = romanNumeral.matches("^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$");
        if (!valid) return 0;
        int decimal = 0;
        int lastNumber = 0;
        for (int x = romanNumeral.length() - 1; x >= 0; x--) {
            char convertToDecimal = romanNumeral.charAt(x);

            switch (convertToDecimal) {
                case 'M':
                    decimal = processDecimal(1000, lastNumber, decimal);
                    lastNumber = 1000;
                    break;

                case 'D':
                    decimal = processDecimal(500, lastNumber, decimal);
                    lastNumber = 500;
                    break;

                case 'C':
                    decimal = processDecimal(100, lastNumber, decimal);
                    lastNumber = 100;
                    break;

                case 'L':
                    decimal = processDecimal(50, lastNumber, decimal);
                    lastNumber = 50;
                    break;

                case 'X':
                    decimal = processDecimal(10, lastNumber, decimal);
                    lastNumber = 10;
                    break;

                case 'V':
                    decimal = processDecimal(5, lastNumber, decimal);
                    lastNumber = 5;
                    break;

                case 'I':
                    decimal = processDecimal(1, lastNumber, decimal);
                    lastNumber = 1;
                    break;
            }
        }
        return decimal;
    }

    public static int processDecimal(int decimal, int lastNumber, int lastDecimal) {
        if (lastNumber > decimal) {
            return lastDecimal - decimal;
        } else {
            return lastDecimal + decimal;
        }
    }

    public static String percentMeter(double i, double j, String chars, int lenght) {
        StringBuilder str = new StringBuilder();
        int percent = (int) (Math.round((i / j) * 100));
        boolean addColor = true;
        for (int counter = 0; counter < lenght; counter++) {
            if (counter >= (percent / (100 / lenght)) && addColor) {
                str.append(ChatColor.GRAY);
                addColor = false;
            }
            str.append(chars);

        }
        String pers = ChatColor.GREEN + str.toString() + ChatColor.RED + " " + (percent) + ChatColor.YELLOW + " %";
        return pers;
    }

    public static boolean percentRoll(int percent) {
        return (ThreadLocalRandom.current().nextInt(0, 100) < percent);
    }

    public static boolean percentRoll(int percent, int maxpercent) {

        return (ThreadLocalRandom.current().nextDouble(0, maxpercent) < percent);
    }

    public static void drawLineParticle(Location location, Location target, ParticleEffect particle, int particles, boolean isZigzag) {

        Vector zigZagOffset = new Vector(0, 0.1, 0);
        boolean isZigZag = false;
        int zigZags = 10;
        int step = 0;
        boolean zag = false;
        double amount = particles / zigZags;
        Vector link = target.toVector().subtract(location.toVector());
        float length = (float) link.length();
        float ratio = length / particles;
        link.normalize();
        Vector v = link.multiply(ratio);
        Location loc = location.clone().subtract(v);
        for (int i = 0; i < particles; i++) {
            if (isZigZag) {
                if (zag) {
                    loc.add(zigZagOffset);
                } else {
                    loc.subtract(zigZagOffset);
                }
            }
            if (step >= amount) {
                if (zag) {
                    zag = false;
                } else {
                    zag = true;
                }
                step = 0;
            }
            step++;
            loc.add(v);
            particle.send(Util.getNearbyPlayer(target, 30d), loc, 0, 0, 0, 0, 1);
        }
    }

    public static final Vector rotateAroundAxisX(Vector v, double angle) {
        double y, z, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        y = v.getY() * cos - v.getZ() * sin;
        z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    public static final Vector rotateAroundAxisY(Vector v, double angle) {
        double x, z, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = v.getX() * cos + v.getZ() * sin;
        z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    public static String convertMiliToMinute(long milisecond) {
        if (milisecond <= 0) return "Hết hạn";
        return String.format("%02d Phút, %02d Giây",
                TimeUnit.MILLISECONDS.toMinutes(milisecond),
                TimeUnit.MILLISECONDS.toSeconds(milisecond) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milisecond))
        );
    }

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, " Ngàn");
        suffixes.put(1_000_000L, " Triệu");
        suffixes.put(1_000_000_000L, " Tỉ");
        suffixes.put(1_000_000_000_000L, " Ngàn Tỉ");
        suffixes.put(1_000_000_000_000_000L, " Triệu Tỉ");
        suffixes.put(1_000_000_000_000_000_000L, " Tỉ Tỉ");
    }

    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static Location randomLocation(Location min, Location max) {
        Location range = new Location(min.getWorld(), Math.abs(max.getX() - min.getX()), min.getY(), Math.abs(max.getZ() - min.getZ()));
        return new Location(min.getWorld(), (Math.random() * range.getX()) + (min.getX() <= max.getX() ? min.getX() : max.getX()), range.getY(), (Math.random() * range.getZ()) + (min.getZ() <= max.getZ() ? min.getZ() : max.getZ()));
    }

    public static Location randomLocationInRegion(ProtectedRegion reg) {
        return randomLocation(new Location(Bukkit.getWorld("Dungeon"), reg.getMinimumPoint().getX(), reg.getMinimumPoint().getY(), reg.getMinimumPoint().getZ()), new Location(Bukkit.getWorld("Dungeon"), reg.getMaximumPoint().getX(), reg.getMaximumPoint().getY(), reg.getMaximumPoint().getZ()));
    }
    public static boolean isSafeLocation(Location location) {
        if (location == null) return false;
        Block feet = location.getBlock();
        if (!feet.getType().isTransparent() && !feet.getLocation().add(0, 1, 0).getBlock().getType().isTransparent()) {
            return false; // not transparent (will suffocate)
        }
        Block head = feet.getRelative(BlockFace.UP);
        if (!head.getType().isTransparent()) {
            return false; // not transparent (will suffocate)
        }
        Block ground = feet.getRelative(BlockFace.DOWN);
        if (!ground.getType().isSolid()) {
            return false; // not solid
        }
        return true;
    }

}
