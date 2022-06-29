package Solar.prison;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Prisoner {

    private OfflinePlayer player;

    private int EXP;
    private int level;
    private int diemdanh;
    private int prestige;

    public Prisoner(OfflinePlayer Player) {
        this.player = Player;
        this.EXP = 0;
        this.diemdanh = 0;
        this.prestige = 1;
    }

    public int getPrestige() {
        return this.prestige;
    }

    public void setPrestige(Number level) {
        this.prestige = level.intValue();
    }

    public int getDiemDanh() {
        return this.diemdanh;
    }

    public void setDiemdanh(Number level) {
        this.diemdanh = level.intValue();
    }

    public int getEXP() {
        return this.EXP;
    }

    public void setEXP(Number EXP) {
        this.EXP = EXP.intValue();
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(Number level) {
        this.level = level.intValue();
    }

    public OfflinePlayer getPlayer() {
        return this.player;
    }

    public void setPlayer(OfflinePlayer player) {
        this.player = player;
    }

    public long getBalance() {
        Economy econ = Main.getEconomy();
        return Math.round(econ.getBalance(this.player));
    }

    public void setBalance(Number newBalance) {
        Economy econ = Main.getEconomy();
        double currentBalance = econ.getBalance(this.player);
        econ.withdrawPlayer(player, currentBalance);
        econ.depositPlayer(player, newBalance.longValue());
    }

    public void giveMoney(Number amount) {
        Economy econ = Main.getEconomy();
        econ.depositPlayer(this.player, amount.longValue());
    }

    public void takeMoney(Number amount) {
        Economy econ = Main.getEconomy();
        econ.withdrawPlayer(this.player, amount.longValue());
    }

    public File getDataFile() {
        return new File(Main.getMain().getDataFolder() + "/PlayerData/" + this.player.getUniqueId().toString() + ".yml");

    }

    public boolean isExist() {
        return this.getDataFile().exists();
    }

    public void saveData() {

        FileConfiguration dataFile = YamlConfiguration.loadConfiguration(this.getDataFile());
        String playerName = player.getName();
        dataFile.set("Player.Name", playerName);

        dataFile.set("Player.EXP", EXP);
        dataFile.set("Player.level", level);
        dataFile.set("Player.diemdanh", diemdanh);
        dataFile.set("Player.prestige", prestige);
        try {
            dataFile.save(getDataFile());
        } catch (IOException e) {
            Main.getMain().getLogger().severe(e.getMessage());
            Main.getMain().getLogger().severe("Khong the luu du lieu cua nguoi choi " + player.getName()
                    + " co UUID la " + player.getUniqueId().toString());
        }


    }

    public void loadData() {
        FileConfiguration dataFile = YamlConfiguration.loadConfiguration(this.getDataFile());
        this.EXP = dataFile.getInt("Player.EXP");
        this.level = dataFile.getInt("Player.level");
        this.diemdanh = dataFile.getInt("Player.diemdanh");
        this.prestige = dataFile.getInt("Player.prestige");


    }

}
