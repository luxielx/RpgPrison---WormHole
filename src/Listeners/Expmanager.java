package Listeners;

import CustomEvents.ExpEvent;
import Enchantment.Energy;
import Enchantment.PEnchantTier;
import Enchantment.Shard;
import Solar.prison.Main;
import Solar.prison.Prisoner;
import Utils.CenteredMessage;
import Utils.Util;
import com.earth2me.essentials.commands.WarpNotFoundException;
import io.netty.util.internal.ThreadLocalRandom;
import net.ess3.api.InvalidWorldException;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Arrays;
import java.util.Random;

public class Expmanager implements Listener {
    @EventHandler
    public void ExpChangeEvent(ExpEvent e) {

        Player player = e.getPlayer();
        Prisoner pn = Main.getMain().getPrisoners().get(player.getUniqueId());

        int level = pn.getLevel();
        int exp = pn.getEXP();
        if (pn.getLevel() < 100 && exp >= Main.getCf().getInt("Level." + (level + 1))) {
            pn.setLevel(pn.getLevel() + 1);
            pn.setEXP(0);
            Util.sendTitleBar(player, ChatColor.GREEN + "Chúc Mừng! ", ChatColor.AQUA + "Bạn đã đạt cấp độ " + ChatColor.LIGHT_PURPLE + pn.getLevel(), 10, 0);
            CenteredMessage.sendCenteredMessage(player, ChatColor.BLUE + "Hãy " + ChatColor.RED + "/me " + ChatColor.BLUE + "để xem chỉ số của chính mình");
            Bukkit.getPluginManager().callEvent(new ExpEvent(player, level));
            if (pn.getLevel() % 5 == 0) {
                PEnchantTier t = PEnchantTier.getByID(ThreadLocalRandom.current().nextInt(1, 5));
                player.getInventory().addItem(Shard.getShard(t));
                CenteredMessage.sendCenteredMessage(player, ChatColor.GREEN + "Bạn đã nhận được Mảnh " + t.getNameColor() + t.getName());

            }
            if (pn.getLevel() == 100) {
                CenteredMessage.sendCenteredMessage(player, ChatColor.GREEN + "Bạn đã lên cấp độ 100 , Chúc mừng . Hãy /prestige để chuyển sinh");
                CenteredMessage.sendCenteredMessage(player, ChatColor.GREEN + "Để chuyển sinh bạn cần 1 tỉ $ ");
                CenteredMessage.sendCenteredMessage(player, ChatColor.GREEN + "Sau khi chuyển sinh bạn sẽ về lại khu đào 1");
                CenteredMessage.sendCenteredMessage(player, ChatColor.GREEN + "Tất cả các vật phẩm cao cấp sẽ không dùng được cho đến khi bạn đạt lại level cần thiết");
                CenteredMessage.sendCenteredMessage(player, ChatColor.GREEN + "Sau khi chuyển sinh , số tiền bạn bán khối sẽ được nhân lên");
                CenteredMessage.sendCenteredMessage(player, ChatColor.GREEN + "Tuy nhiên lượng kinh nghiệm nhận được sẽ giảm đi");

            }


            if ((int) (pn.getLevel() / 2.5) > (int) ((pn.getLevel() - 1) / 2.5)) {
                CenteredMessage.sendCenteredMessage(player, ChatColor.AQUA + "Mở khóa khu mỏ " + ChatColor.YELLOW + "" + Util.convertToRoman((int) (pn.getLevel() / 2.5)));
            }
            Bukkit.broadcastMessage(ChatColor.RED + "Chúc mừng " + ChatColor.BLUE + player.getName() + ChatColor.RED + " Đã đạt cấp độ " + ChatColor.GREEN + pn.getLevel());

        }
        if (pn.getEXP() > Main.getCf().getInt("Level.100")) {
            pn.setEXP(Main.getCf().getInt("Level.100"));
            return;
        }

    }

    public static void LevelUpPrestige(Player player) {
        Prisoner pn = Main.getMain().getPrisoners().get(player.getUniqueId());
        if (pn.getLevel() < 100) {
            player.sendMessage(ChatColor.RED + "Bạn phải đủ level 100 mới có thê chuyển sinh");
            return;
        }

        for (ItemStack a : Arrays.asList(player.getInventory().getArmorContents())) {

            if(a.getType() != Material.AIR){
                player.sendMessage(ChatColor.RED + "Bạn phải cởi hết giáp ra mới có thể chuyển sinh");
                return;
            }
        }



        SellEvent.SellAllItem(player);
        pn.setLevel(1);
        pn.setPrestige(pn.getPrestige() + 1);
        player.getInventory().addItem(Shard.getShard(PEnchantTier.LEGENDARY));
        player.getInventory().addItem(Shard.getShard(PEnchantTier.LEGENDARY));
        player.getInventory().addItem(Shard.getShard(PEnchantTier.LEGENDARY));
        player.getInventory().addItem(Shard.getShard(PEnchantTier.LEGENDARY));
        player.getInventory().addItem(Shard.getShard(PEnchantTier.LEGENDARY));
        if(pn.getPrestige() * 1000000000 >= Long.MAX_VALUE){
            player.getInventory().addItem(Energy.getRawEnergy(Long.MAX_VALUE));
        }else{
            player.getInventory().addItem(Energy.getRawEnergy(pn.getPrestige() * 100000000));

        }
        CenteredMessage.sendCenteredMessage(player, ChatColor.GREEN + "Bạn đã chuyển sinh thành công");
        CenteredMessage.sendCenteredMessage(player, ChatColor.GREEN + "Bạn sẽ bắt đầu lại từ cấp độ 1");
        CenteredMessage.sendCenteredMessage(player, ChatColor.GREEN + "Trang bị của bạn sẽ có thể dùng lại khi đủ cấp độ");
        Bukkit.broadcastMessage(ChatColor.GREEN + "Chúc mừng " + ChatColor.BLUE + player.getName() + ChatColor.GREEN + " Đã chuyển sinh thành công lần thứ " + ChatColor.YELLOW + Util.convertToRoman(pn.getPrestige()));

        try {
            player.teleport(Main.getEss().getWarps().getWarp("spawn"));
        } catch (WarpNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidWorldException e) {
            e.printStackTrace();
        }
        for (int i = 0; i <= 5; i++) {
            Firework fw = (Firework) player.getWorld().spawnEntity(player.getLocation().clone(), EntityType.FIREWORK);
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

}
