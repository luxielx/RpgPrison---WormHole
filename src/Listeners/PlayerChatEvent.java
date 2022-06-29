package Listeners;

import Solar.prison.Main;
import Solar.prison.Prisoner;
import Utils.Util;
import com.earth2me.essentials.Essentials;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class PlayerChatEvent implements Listener {
    HashMap<UUID, Long> timers = new HashMap<>();
    public static int chattime = 3;

    public static long cooldown(int level) {

        return (long) (1000 * (chattime));

    }

//	public static boolean isMentionable(Player player) {
//		if (disableMention.contains(player.getUniqueId())) {
//			return false;
//		} else {
//			return true;
//		}
//	}
//
//	public static void setMentionable(Player player, boolean bl) {
//		if (bl) {
//
//			if (disableMention.contains(player.getUniqueId())) {
//				disableMention.remove(player.getUniqueId());
//			}
//		} else {
//
//			if (!disableMention.contains(player.getUniqueId())) {
//				disableMention.add(player.getUniqueId());
//			}
//		}
//	}

    public boolean cooldown(int level, Player player) {
        if (!timers.containsKey(player.getUniqueId())) {
            timers.put(player.getUniqueId(), 0l);
        }

        if (System.currentTimeMillis() - timers.get(player.getUniqueId()) < cooldown(level)) {

            player.sendMessage(ChatColor.GREEN + "Bạn cần đợi " + ChatColor.RED
                    + ((cooldown(level) - System.currentTimeMillis() + timers.get(player.getUniqueId())) / 1000 + 1)
                    + ChatColor.GREEN + " giây Để chat tiếp");
            return true;
        }

        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void chatformat(AsyncPlayerChatEvent e) {
        if (e.isCancelled())
            return;
        if (cooldown(1, e.getPlayer())) {
            e.setCancelled(true);
            return;
        }

        if (!e.getPlayer().hasPermission("prison.bypasschat")) {
            timers.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
        }
        e.setCancelled(true);
        Player player = e.getPlayer();
        Prisoner prisoner = Main.getMain().getPrisoners().get(player.getUniqueId());
        Essentials ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
        // PermissionsEx.getUser(player).getPrefix().replaceAll("&", "§");
        User user = Main.getLPAPI().getUser(player.getUniqueId());
        Contexts contexts = Main.getLPAPI().getContextForUser(user).orElse(null);

        MetaData metadata = user.getCachedData().getMetaData(contexts);
        String playerprefix = metadata.getPrefix().replaceAll("&", "§");
        String suffix = metadata.getSuffix().replaceAll("&", "§");
        com.earth2me.essentials.User sender = ess.getUser(e.getPlayer());

        Bukkit.getScheduler().runTaskAsynchronously(Main.getMain(), new Runnable() {

            @Override
            public void run() {
                int plevel = prisoner.getLevel();
                String gang = "";
                TextComponent level = new TextComponent(gang);
                int prestige = prisoner.getPrestige();

                TextComponent plevelc = new TextComponent(ChatColor.RED + "" + ChatColor.MAGIC + "|" + ChatColor.GOLD + ChatColor.BOLD + Util.convertToRoman(prestige) + ChatColor.RED + "" + ChatColor.MAGIC + "|"+ChatColor.RESET + " " + ChatColor.AQUA + "" + ChatColor.MAGIC + "|" + ChatColor.DARK_RED + ChatColor.BOLD + plevel + ChatColor.AQUA + "" + ChatColor.MAGIC + "|" + ChatColor.RESET + " ");

                String message = e.getMessage();

                if (sender.isMuted()) {
                    Main.getMain().getServer().getConsoleSender().sendMessage(
                            ChatColor.RED + "[ChatLogger] [Muted] " + e.getPlayer().getName() + ": " + e.getMessage());
                    return;
                }

                long money = sender.getMoney().longValue();


                TextComponent prefix = new TextComponent(playerprefix + " ");

                HoverEvent playerstats = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(
                                ChatColor.YELLOW + "Số tiền: " + ChatColor.GREEN + "$" + ChatColor.RED + money + "\n"
                                        + ChatColor.LIGHT_PURPLE + "Tên thật: " + ChatColor.RED + player.getName()
                                        + "\n" + ChatColor.YELLOW + "Level: " + ChatColor.RED + prisoner.getLevel())
                                .create());
                plevelc.setHoverEvent(playerstats);
                TextComponent name = new TextComponent(e.getPlayer().getName());
                name.setColor(ChatColor.RESET);
                name.setBold(false);
                if (sender.getNickname() != null) {
                    name = new TextComponent(sender.getNickname());
                }
                name.setHoverEvent(playerstats);

                TextComponent end = new TextComponent(" >> ");
                end.setColor(ChatColor.GOLD);
                String[] split = message.split(" ");


                HoverEvent msgh = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(ChatColor.RED + "Bấm vào để xem thông tin").create());
                plevelc.addExtra(level);
                plevelc.addExtra(prefix);

                plevelc.addExtra(name);
                plevelc.addExtra(end);

                plevelc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/xem" + " " + e.getPlayer().getName()));

                for (String z : split) {
                    TextComponent cm = null;
                    if (z.equalsIgnoreCase("[item]") && player.hasPermission("prison.showitem")
                            && player.getItemInHand() != null) {
                        ItemStack is = player.getItemInHand();
                        String itemJson = convertItemStackToJsonRegular(is);
                        BaseComponent[] hoverEventComponents = new BaseComponent[]{new TextComponent(itemJson)};
                        if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
                            cm = new TextComponent(" " + is.getItemMeta().getDisplayName() + " ");
                        } else {
                            cm = new TextComponent(" " + is.getType().toString() + " ");
                        }
                        HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_ITEM, hoverEventComponents);
                        cm.setHoverEvent(event);
                    } else if (z.equalsIgnoreCase("[money]") && player.hasPermission("prison.showitem")
                            && player.getItemInHand() != null) {
                        cm = new TextComponent(" " + ChatColor.GREEN + "" + ChatColor.BOLD + "$" + prisoner.getBalance() + " ");
                        cm.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new ComponentBuilder(ChatColor.BLUE + "Số Tiền Của " + ChatColor.RED + player.getName()).create()));
                    } else if (z.equalsIgnoreCase("[level]") && player.hasPermission("prison.showitem")
                            && player.getItemInHand() != null) {
                        cm = new TextComponent(" " + ChatColor.BLUE + "" + ChatColor.BOLD + "" + prisoner.getLevel() + " ");
                        cm.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new ComponentBuilder(ChatColor.GREEN + "Cấp độ Của " + ChatColor.RED + player.getName()).create()));
                    } else {
                        cm = new TextComponent(suffix + z + " ");
                        cm.setHoverEvent(msgh);
                    }
                    plevelc.addExtra(cm);

                }


                TextComponent staff = new TextComponent(ChatColor.RED + " ❖");
                if (e.getPlayer().hasPermission("prison.staff")) {
                    staff.setBold(true);
                    staff.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder(ChatColor.AQUA + "Đây là Staff").create()));

                }

                for (Player pl : Main.getMain().getServer().getOnlinePlayers()) {
                    com.earth2me.essentials.User receiver = ess.getUser(pl);
                    if (receiver.isIgnoredPlayer(sender) && !receiver.isIgnoreExempt())
                        continue;
                    if (message.contains(pl.getName())) {
                        plevelc.setColor(ChatColor.RED);
                        plevelc.setBold(true);
                    }
                    if (e.getPlayer().hasPermission("prison.staff")) {

                        pl.spigot().sendMessage(plevelc, staff);

                    } else if (!e.getPlayer().hasPermission("prison.staff") || e.getPlayer().isOp() == false) {

                        pl.spigot().sendMessage(plevelc);

                    }

                }

                Main.getMain().getServer().getConsoleSender().sendMessage(
                        ChatColor.GREEN + "[ChatLogger] " + e.getPlayer().getName() + ": " + e.getMessage());

            }
        });

    }

    public String convertItemStackToJsonRegular(ItemStack is) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(is);
        NBTTagCompound compound = new NBTTagCompound();
        compound = nmsItemStack.save(compound);

        return compound.toString();
    }

}
