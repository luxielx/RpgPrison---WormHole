package Enchantment;

import Utils.Glow;
import Utils.ToolList;
import Utils.Util;
import io.netty.util.internal.ThreadLocalRandom;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public enum PEnchant {
    // simple
    GIATOC("Gia Tốc", "Một loại phù phép làm cho bạn đào nhanh hơn bình thường.", PEnchantTier.SIMPLE, ToolList.PICKAXE, 20),
    RANNUT("Rạn Nứt", "Tạo một vụ địa chấn nhỏ làm nứt quặng xung quanh bản thân.", PEnchantTier.SIMPLE, ToolList.PICKAXE, 10),
    THANTAI("Thần Tài", "Có cơ hội nhận thêm quặng khi đào", PEnchantTier.SIMPLE, ToolList.PICKAXE, 20),
    KHAOCO("Khảo Cổ", "Tăng khả năng nhặt được các mảnh vỡ hư không.", PEnchantTier.SIMPLE, ToolList.PICKAXE, 20),


    TUYETTHUC("Tuyệt Thực", "Khi tấn công có khả năng hút thức ăn của mục tiêu", PEnchantTier.SIMPLE, ToolList.SWORD, 6),
    DOC("Độc", "Khi tấn công có khả năng làm mục tiêu dính độc", PEnchantTier.SIMPLE, ToolList.SWORD, 6),

    HOIPHUC("Hồi Phục", "Hồi phục nhiều máu hơn", PEnchantTier.SIMPLE, ToolList.ARMOR, 8),
    NETRANH("Né Tránh", "Có cơ hội né đòn của đối phương", PEnchantTier.SIMPLE, ToolList.ARMOR, 5),

    SATLUC("Sát Lực", "Tăng sát thương của mũi tên", PEnchantTier.SIMPLE, ToolList.BOW, 10),
    TENDOC("Tên Độc", "Mũi tên tẩm độc làm mục tiêu dính độc . Không tác dụng với Hỏa Cầu , Băng Cầu và Thần Y", PEnchantTier.SIMPLE, ToolList.BOW, 1),

    // uncommon
    DIENTICHCAU("Điện Tích Cầu", "Có khả năng nhận được thêm năng lượng khi đào", PEnchantTier.UNCOMMON, ToolList.PICKAXE, 20),
    NHAINUOT("Nhai Nuốt", "Có khả năng hồi đầy thức ăn khi đào", PEnchantTier.UNCOMMON, ToolList.PICKAXE, 5),

    CHOANGVANG("Choáng Váng", "Có khả năng làm kẻ địch hoa mắt trong thời gian ngắn", PEnchantTier.UNCOMMON, ToolList.SWORD, 5),
    SACBEN("Sắc bén", "Tăng sát thương mỗi đòn đánh", PEnchantTier.UNCOMMON, ToolList.SWORD, 10),

    THETHU("Thế Thủ", "Khi cúi người (giữ nút shift) bạn sẽ nhận ít sát thương hơn", PEnchantTier.UNCOMMON, ToolList.ARMOR, 5),
    TOCDO("Tốc Độ", "Giúp bạn chạy nhanh", PEnchantTier.UNCOMMON, ToolList.BOOTS, 1),
    CHANTHO("Chân Thỏ", "Nhảy cao hơn", PEnchantTier.UNCOMMON, ToolList.BOOTS, 1),
    GIAPCUNG("Giáp Cứng", "Giảm sát thương nhận vào", PEnchantTier.UNCOMMON, ToolList.ARMOR, 10),

    TENNO("Tên Nổ", "Có khả năng nổ tung khi dính mục tiêu . Không tác dụng với Hỏa Cầu , Băng Cầu và Thần Y", PEnchantTier.UNCOMMON, ToolList.BOW, 4),
    HOACAU("Hỏa Cầu", "Khi mở chế độ Hỏa Cầu sẽ bắn cầu lửa vào mục tiêu", PEnchantTier.UNCOMMON, ToolList.BOW, 1),
    DAYTHEP("Dây Thép", "Dây càng mạnh bắn lực bắn càng cao !!", PEnchantTier.UNCOMMON, ToolList.BOW, 1),
    BANGCAU("Băng Cầu", "Khi mở chế độ Băng Cầu sẽ bắn bóng tuyết vào mục tiêu và làm chậm mục tiêu", PEnchantTier.UNCOMMON, ToolList.BOW, 1),
    // elite
    CAULUA("Cầu Lửa", "Tích tụ năng lượng của bạn thân lại và ném ra 1 quả cầu lửa tàn phá các khối mà nó gặp phải!", PEnchantTier.ELITE, ToolList.PICKAXE, 2),
    TANPHA("Tàn Phá", "Tạo ra 1 vụ nổ rất to làm nổ tất cả các khối trong khu vực", PEnchantTier.ELITE, ToolList.PICKAXE, 5),
    SAMSET("Sấm sét", "Tạo ra 1 cơn bão sấm đánh vào các khối bên cạnh biến nó thành khối cao cấp", PEnchantTier.ELITE, ToolList.PICKAXE, 5),
    DIENCUONG("Điên Cuồng", "Khi đang đào có khả năng kích hoạt chế độ điên cuồng giúp bạn đập khối cực nhanh", PEnchantTier.ELITE, ToolList.PICKAXE, 4),
    HANBANG("Hàn Băng", "Khi tấn công sẽ làm chậm đối phương", PEnchantTier.ELITE, ToolList.SWORD, 5),
    HOADIEM("Hỏa Diệm", "Khi tấn công sẽ đốt cháy đối phương", PEnchantTier.ELITE, ToolList.SWORD, 5),


    GIAPLUA("Giáp Lửa", "Khi bị tấn công sẽ có cơ hội đốt cháy kẻ địch , Đồng thời giúp người sử dụng kháng lửa", PEnchantTier.ELITE, ToolList.ARMOR, 4),
    NGUYENRUA("Nguyền Rủa", "Khi bị tấn công sẽ có cơ hội làm kẻ địch chóng mặt", PEnchantTier.ELITE, ToolList.ARMOR, 4),
    THEPHOA("Thép Hóa", "Khi bị tấn công có cơ hội đẩy kẻ địch ra xa", PEnchantTier.ELITE, ToolList.ARMOR, 2),
    TUHUY("Tự Hủy", "Khi chết bạn sẽ có cơ hội nổ tung gây sát thương lên kẻ địch xung quanh", PEnchantTier.ELITE, ToolList.ARMOR, 5),

    NGUOICA("Người Cá", "Không thể nghẹt thở dưới nước", PEnchantTier.ELITE, ToolList.HELMET, 1),

    LOI("Lôi", "Có tỉ lệ đánh sét vào nơi mũi tên rơi", PEnchantTier.ELITE, ToolList.BOW, 4),
    HOA("Hỏa", "Có tỉ lệ làm mục tiêu dính lửa", PEnchantTier.ELITE, ToolList.BOW, 4),
    HAN("Hàn", "Có tỉ lệ làm mục tiêu bị đóng băng", PEnchantTier.ELITE, ToolList.BOW, 4),
    PHONG("Phong", "Có tỉ lệ làm mục tiêu bị đẩy lùi", PEnchantTier.ELITE, ToolList.BOW, 4),
    THUY("Thủy", "Có tỉ lệ làm mục tiêu bị lơ lửng trên không", PEnchantTier.ELITE, ToolList.BOW, 4),
    BONGTOI("Bóng Tối", "Có tỉ lệ làm mục tiêu bị Mù", PEnchantTier.ELITE, ToolList.BOW, 4),
//	ANHSANG("Ánh Sáng", "Có tỉ lệ làm mục tiêu phát sáng" , PEnchantTier.ELITE , ToolList.BOW, 3),

    // ultimate

    LOITHAN("Lôi Thần", "Khi tấn công có cơ hội gọi sét đánh xuống đối phương", PEnchantTier.ULTIMATE, ToolList.SWORD, 5),
    ANTU("Án Tử", "Khi tấn công sẽ có cơ hội kéo đối phương lại gần", PEnchantTier.ULTIMATE, ToolList.SWORD, 5),
    PHONGTHAN("Phong Thần ", "Khi tấn công có cơ hội hất tung kẻ địch lên không trung", PEnchantTier.ULTIMATE, ToolList.SWORD, 5),


    MATTHAN("Mắt Thần", "Cho phép nhìn trong bóng tối", PEnchantTier.ULTIMATE, ToolList.HELMET, 1),
    HAPTHU("Hấp Thụ", "Khi bị tấn công có cơ hội nhận được hiệu ứng hồi máu", PEnchantTier.ULTIMATE, ToolList.ARMOR, 4),
    GIAPGAI("Giáp Gai", "Khi bị tấn công có cơ hội phản lại 1/2 sát thương", PEnchantTier.ULTIMATE, ToolList.ARMOR, 4),


    LIENCUNG("Liên Cung", "Tăng tốc độ bắn", PEnchantTier.ULTIMATE, ToolList.BOW, 2),
    // legendary

    HUTMAU("Hút Máu", "Khi tấn công có cơ hội hút máu của đối phương", PEnchantTier.LEGENDARY, ToolList.SWORD, 5),
    HOITHOCUARONG("Hơi thở của rồng", "Khi tấn công có cơ hội đốt chảy tất cả kẻ địch trước mặt", PEnchantTier.LEGENDARY, ToolList.SWORD, 5),


    BANPHUOC("Ban Phước", "Khi bị đánh chết sẽ được hồi lại 5 máu (5 phút hồi chiêu)", PEnchantTier.LEGENDARY, ToolList.CHESTPLATE, 2),
    BAOVE("Bảo Vệ", "Bảo vệ vật phẩm của bạn sẽ không rớt ra khi chết . Phù phép này sẽ mất khi bạn chết", PEnchantTier.LEGENDARY, ToolList.ALL, 1),

    THANY("Thần Y", "Khi mục tiêu trúng tên ở chế độ thần Y sẽ được hồi máu", PEnchantTier.LEGENDARY, ToolList.BOW, 1),
    NOTHAN("Nỏ Thần", "Bắn nhiều mũi tên 1 lần", PEnchantTier.LEGENDARY, ToolList.BOW, 2),;
    String name;
    String des;
    PEnchantTier tier;
    ToolList tl;
    Material[] item;
    int maxlevel;

    private PEnchant(String name, String des, PEnchantTier tier, ToolList tl, int maxlevel) {
        this.name = name;
        this.des = des;
        this.tier = tier;
        this.tl = tl;
        this.maxlevel = maxlevel;
        this.item = tl.getItemList();
    }

    public int getMaxLevel() {
        return maxlevel;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return des;
    }

    public PEnchantTier getTier() {
        return tier;
    }

    public Material[] getListItem() {
        return item;
    }

    public ToolList getToolList() {
        return tl;
    }

    public void apply(ItemStack items, int level) {
        if (level <= 0) return;
        if (Arrays.asList(item).contains(items.getType())) {
            long ce = 0;
            long me = 0;
            boolean tachsach = false;
            if (!bookWithdraw(items)) {
                tachsach = true;
            }
            boolean upnl = false;
            if (!energyLevelUpAble(items)) {
                upnl = true;
            }

            if (Energy.hasEnergy(items)) {
                ce = Energy.getCurrentEnergy(items);
                me = Energy.getMaxEnergy(items);
                Energy.deleteEnergy(items);
            }
            if (!hasEnchant(items)) {

                addLore(items, ChatColor.BOLD + "" + tier.getNameColor() + name + "  " + tier.getLevelColor()
                        + Util.convertToRoman(level));
                Energy.setEnergy(items, ce, me);

            } else {

                remove(items);

                addLore(items, ChatColor.BOLD + "" + tier.getNameColor() + name + "  " + tier.getLevelColor()
                        + Util.convertToRoman(level));
                Energy.setEnergy(items, ce, me);

            }
            if (name.equals(PEnchant.GIATOC.getName())) {
                items.addUnsafeEnchantment(Enchantment.DIG_SPEED, level);
                ItemMeta im = items.getItemMeta();
                if (!im.hasItemFlag(ItemFlag.HIDE_ENCHANTS))
                    im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                items.setItemMeta(im);
            }
            if (tachsach)
                preventBookWithdraw(items);
            if (upnl) preventEnergyLevelup(items);


        }
    }

    public void remove(ItemStack items) {
        if (Arrays.asList(item).contains(items.getType())) {
            if (hasEnchant(items)) {
                removeLore(items, name);
            }
            if (name.equals(PEnchant.GIATOC.getName())) {
                items.removeEnchantment(Enchantment.DIG_SPEED);

            }
        }
    }

    public void removeLore(ItemStack is, String string) {
        ItemMeta im = is.getItemMeta();
        if (im.hasLore()) {
            List<String> lores = im.getLore();
            int index = 0;
            for (String s : lores) {
                if (s.contains(string)) {
                    break;
                }
                index++;
            }
            lores.remove(index);
            im.setLore(lores);
        }
        is.setItemMeta(im);

    }

    public boolean hasEnchant(ItemStack is) {
        if (is == null)
            return false;
        if (is.getType() == Material.AIR) {
            return false;
        }
        if (!is.hasItemMeta())
            return false;
        if (!is.getItemMeta().hasLore())
            return false;
        for (String i : is.getItemMeta().getLore()) {
            if (i.contains(name)) {
                return true;

            }
        }
        return false;
    }

    public void addLore(ItemStack is, String lore) {
        ItemMeta im = is.getItemMeta();
        if (im.hasLore()) {
            List<String> lores = im.getLore();
            lores.add(lore);
            im.setLore(lores);
        } else {
            im.setLore(Arrays.asList(new String[]{lore}));
        }
        is.setItemMeta(im);
    }

    public int getLevel(ItemStack is) {
        if (!is.hasItemMeta())
            return 0;
        if (!is.getItemMeta().hasLore())
            return 0;
        ItemMeta im = is.getItemMeta();
        List<String> lore = im.getLore();

        for (String z : lore) {
            if (z.contains(name)) {
                String[] splitz = z.split(" ");
                String roman = ChatColor.stripColor(splitz[splitz.length - 1]);
                return Util.romanConvert(roman);
            }
        }
        return 0;

    }

    public static ItemStack ProtectionORB() {
        ItemStack is = new ItemStack(Material.INK_SACK, 1, (short) 12);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.AQUA + "Ngọc bảo vệ");
        im.setLore(Arrays.asList(
                new String[]{Util.hideS("protection"), ChatColor.BLUE + "Kéo thả vào vật phẩm để phù phép bảo vệ"}));

        is.setItemMeta(im);
        return is;
    }

    public static boolean IsprotectionOrb(ItemStack is) {
        if (is == null)
            return false;
        if (is.getType() == Material.AIR)
            return false;
        if (!is.hasItemMeta())
            return false;
        if (!is.getItemMeta().hasLore())
            return false;
        for (String s : is.getItemMeta().getLore()) {
            if (s.contains(Util.hideS("protection"))) {
                return true;
            }
        }
        return false;

    }

    public ItemStack getBook(int level) {
        ItemStack is = new ItemStack(Material.BOOK);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.BOLD + "" + tier.getNameColor() + name + "  " + tier.getLevelColor()
                + Util.convertToRoman(level));
        im.setLore(Arrays.asList(new String[]{Util.hideS("enchantbook"),
                ChatColor.GREEN + "Kéo thả vào " + tl.getName() + " để phù phép",
                Util.hideS(UUID.randomUUID().toString())}));
        Glow g = new Glow(200);
        im.addEnchant(g, 1, true);
        is.setItemMeta(im);
        return is;
    }

    public static boolean IsBook(ItemStack is) {
        if (is == null)
            return false;
        if (is.getType() == Material.AIR)
            return false;
        if (!is.hasItemMeta())
            return false;
        if (!is.getItemMeta().hasLore())
            return false;
        for (String s : is.getItemMeta().getLore()) {
            if (s.contains(Util.hideS("enchantbook"))) {
                return true;
            }
        }
        return false;
    }

    public static PEnchant getEnchantFromBook(ItemStack is) {
        if (!IsBook(is))
            return null;
        PEnchant en = null;
        for (PEnchant z : PEnchant.values()) {
            if (is.getItemMeta().getDisplayName().contains(z.getName())) {
                en = z;
                break;
            }
        }
        return en;
    }

    public static int getEnchantLevelFromBook(ItemStack is) {
        if (!IsBook(is))
            return 0;
        String name = is.getItemMeta().getDisplayName();
        String[] splitz = name.split(" ");
        String roman = ChatColor.stripColor(splitz[splitz.length - 1]);
        int level = Util.romanConvert(roman);
        return level;
    }

    public static ItemStack combineBooks(ItemStack book1, ItemStack book2) {
        if (!IsBook(book1))
            return null;
        if (!IsBook(book2))
            return null;
        if (getEnchantFromBook(book1) != getEnchantFromBook(book2))
            return null;
        if (getEnchantLevelFromBook(book1) != getEnchantLevelFromBook(book2))
            return null;
        PEnchant z = getEnchantFromBook(book1);
        int level = getEnchantLevelFromBook(book1);
        if (z.getMaxLevel() * 5 <= level)
            return null;
        return z.getBook(level + 1);

    }

    public static PEnchant getEnchantFromName(String name) {
        String s = ChatColor.stripColor(name);
        for (PEnchant z : PEnchant.values()) {
            if (z.getName().contains(s)) {
                return z;
            }
        }
        return null;

    }

    public static boolean canCombine(ItemStack book1, ItemStack book2) {
        if (!IsBook(book1))
            return false;
        if (!IsBook(book2))
            return false;
        if (getEnchantFromBook(book1) != getEnchantFromBook(book2))
            return false;
        if (getEnchantLevelFromBook(book1) != getEnchantLevelFromBook(book2))
            return false;
        PEnchant z = getEnchantFromBook(book1);
        int level = getEnchantLevelFromBook(book1);
        if (z.getMaxLevel() * 5 <= level)
            return false;
        return true;
    }

    public static void resolveItem(ItemStack is) {
        ItemStack l = is.clone();
        boolean he = false;
        for (PEnchant z : PEnchant.values()) {
            if (z.hasEnchant(is)) {
                he = true;
                break;
            }
        }
        if (he) {
            ArrayList<PEnchant> a = new ArrayList<>();
            for (PEnchant z : PEnchant.values()) {
                if (z.hasEnchant(is)) {
                    he = true;
                    a.add(z);
                    z.remove(l);
                }
            }
            for (int m = 5; m >= 1; m--) {
                PEnchantTier t = PEnchantTier.getByID(m);
                for (PEnchant z : a) {
                    if (z.getTier() == t) {
                        z.apply(l, z.getLevel(is));
                    }
                }
                for (PEnchant z : PEnchant.values()) {
                    if (z.getTier() == t) {
                        if (a.contains(z)) {
                            a.remove(z);
                        }
                    }
                }
            }
        }
        is.setItemMeta(l.getItemMeta());
    }

    public static ItemStack resolveItemStack(ItemStack is) {
        ItemStack l = is.clone();
        boolean he = false;
        for (PEnchant z : PEnchant.values()) {
            if (z.hasEnchant(is)) {
                he = true;
                break;
            }
        }
        if (he) {
            ArrayList<PEnchant> a = new ArrayList<>();
            for (PEnchant z : PEnchant.values()) {
                if (z.hasEnchant(is)) {
                    he = true;
                    a.add(z);
                    z.remove(l);
                }
            }
            for (int m = 5; m >= 1; m--) {
                PEnchantTier t = PEnchantTier.getByID(m);
                for (PEnchant z : a) {
                    if (z.getTier() == t) {
                        z.apply(l, z.getLevel(is));
                    }
                }
                for (PEnchant z : PEnchant.values()) {
                    if (z.getTier() == t) {
                        if (a.contains(z)) {
                            a.remove(z);
                        }
                    }
                }
            }
        }
        is.setItemMeta(l.getItemMeta());
        return is;
    }

    public static List<PEnchant> Elements() {
        return Arrays.asList(new PEnchant[]{PEnchant.LOI, /*PEnchant.ANHSANG ,*/ PEnchant.BONGTOI, PEnchant.HOA, PEnchant.THUY, PEnchant.PHONG, PEnchant.HAN});
    }

    public static PEnchant randomElement() {
        return Elements().get(ThreadLocalRandom.current().nextInt(Elements().size()));
    }

    public static enum fireModes {
        HOACAU(ChatColor.RED + "Hỏa Cầu"), BANGCAU(ChatColor.AQUA + "Băng Cầu"), THANY(ChatColor.LIGHT_PURPLE + "Thần Y"), NORMAL(ChatColor.RESET + "Thường");
        private String name;

        fireModes(String s) {
            this.name = s;
        }

        public String getName() {
            return this.name;
        }
    }

    public static boolean bookWithdraw(ItemStack is) {
        if (is == null) return true;
        if (is.getType() == Material.AIR) return true;
        if (!is.hasItemMeta()) return true;
        if (!is.getItemMeta().hasLore()) return true;
        for (String s : is.getItemMeta().getLore()) {
            if (ChatColor.stripColor(s).contains("[Không Thể Tách Sách]")) {
                return false;
            }
        }
        return true;

    }

    public static void preventBookWithdraw(ItemStack is) {
        if (is == null) return;
        if (is.getType() == Material.AIR) return;
        if (!bookWithdraw(is)) return;
        ItemMeta im = is.getItemMeta();
        if (im.hasLore()) {
            List<String> lore = im.getLore();
            lore.addAll(Arrays.asList(new String[]{ChatColor.RED + ChatColor.BOLD.toString() + "[Không Thể Tách Sách]"}));
            im.setLore(lore);
        } else {
            im.setLore(Arrays.asList(new String[]{ChatColor.RED + ChatColor.BOLD.toString() + "[Không Thể Tách Sách]"}));
        }
        is.setItemMeta(im);

    }

    public static void preventEnergyLevelup(ItemStack is) {
        if (is == null) return;
        if (is.getType() == Material.AIR) return;
        if (!energyLevelUpAble(is)) return;
        ItemMeta im = is.getItemMeta();
        if (im.hasLore()) {
            List<String> lore = im.getLore();
            lore.addAll(Arrays.asList(new String[]{ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "[Không Thể Đầy Năng Lượng]"}));
            im.setLore(lore);
        } else {
            im.setLore(Arrays.asList(new String[]{ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "[Không Thể Đầy Năng Lượng]"}));
        }
        is.setItemMeta(im);

    }

    public static boolean energyLevelUpAble(ItemStack is) {
        if (is == null) return true;
        if (is.getType() == Material.AIR) return true;
        if (!is.hasItemMeta()) return true;
        if (!is.getItemMeta().hasLore()) return true;
        for (String s : is.getItemMeta().getLore()) {
            if (ChatColor.stripColor(s).contains("[Không Thể Đầy Năng Lượng]")) {
                return false;
            }
        }
        return true;

    }

    public static PEnchant getRandomEnchant(PEnchantTier[] tier) {
        PEnchant[] en = PEnchant.values();

        while (true) {
            PEnchant enc = Arrays.asList(en).get(ThreadLocalRandom.current().nextInt(Arrays.asList(en).size()));
            if (Arrays.asList(tier).contains(enc.getTier())) {
                return enc;
            }
        }
    }

}
