package Listeners;

import Enchantment.Energy;
import Solar.prison.Main;
import Solar.prison.Prisoner;
import Utils.CenteredMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by ADMIN on 2/10/2018.
 */
public class DailyReward implements Listener {
    @EventHandler
    public void join(PlayerJoinEvent e) {

        Player player = e.getPlayer();
        Prisoner pn = Main.getMain().getPrisoners().get(player.getUniqueId());
        if (System.currentTimeMillis() - player.getLastPlayed() >= 86400000 * 2) {
            pn.setDiemdanh(1);
        } else if (System.currentTimeMillis() - player.getLastPlayed() >= 86400000) {

            pn.setDiemdanh(pn.getDiemDanh() + 1);
            if (pn.getDiemDanh() >= 10) {
                player.getInventory().addItem(Energy.getRawEnergy(500 * 10));

            }else{
                player.getInventory().addItem(Energy.getRawEnergy(500 * pn.getDiemDanh()));

            }


        } else {
            pn.setDiemdanh(pn.getDiemDanh() + 1);
        }
        if (pn.getDiemDanh() >= 10) {
            CenteredMessage.sendCenteredMessage(player, ChatColor.RED + "Điểm danh ngày thứ " + ChatColor.YELLOW + "" + pn.getDiemDanh() + ChatColor.RED + " nhận được " + ChatColor.BLUE + " " + 500 * 10 + " Năng lượng");
            CenteredMessage.sendCenteredMessage(player, ChatColor.GREEN + "Hãy quay lại vào ngày mai để tiếp tục nhận thưởng");
        } else {
            CenteredMessage.sendCenteredMessage(player, ChatColor.RED + "Điểm danh ngày thứ " + ChatColor.YELLOW + "" + pn.getDiemDanh() + ChatColor.RED + " nhận được " + ChatColor.BLUE + " " + 500 * pn.getDiemDanh() + " Năng lượng");
            CenteredMessage.sendCenteredMessage(player, ChatColor.GREEN + "Hãy quay lại vào ngày mai để tiếp tục nhận thưởng");
        }
    }


}
