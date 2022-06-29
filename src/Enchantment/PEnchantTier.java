package Enchantment;

import net.md_5.bungee.api.ChatColor;

public enum PEnchantTier {
	SIMPLE(1, "Rởm", ChatColor.GRAY, ChatColor.WHITE , 80 , 95),
	UNCOMMON(2, "Tầm Trung", ChatColor.GREEN,ChatColor.DARK_GREEN , 50 , 70), 		
	ELITE(3, "Cao Cấp", ChatColor.BLUE,	ChatColor.AQUA ,  30 , 40 ),				
	ULTIMATE(4, "Tối Thượng", ChatColor.RED, ChatColor.DARK_RED , 1 , 20),
	LEGENDARY(5, "Huyền Thoại" , ChatColor.GOLD , ChatColor.YELLOW , 1 , 5);
	
	int id;
	String name;
	ChatColor namecolor;
	ChatColor levelcolor;
	int minspercent;
	int maxspercent;
	private PEnchantTier(int id, String name, ChatColor color, ChatColor color2 , int minspercent , int maxspercent ) {
		this.id = id;
		this.name = name;
		this.namecolor = color;
		this.levelcolor = color2;

		this.minspercent = minspercent;
		this.maxspercent = maxspercent;

	}

	
	public int getMinSuccessPercent(){
		return minspercent;
	}
	
	public int getMaxSuccessPercent(){
		return maxspercent;
	}
	public int getID() {
		return id;
	}

	public String getName() {
		return name;

	}

	public ChatColor getNameColor() {
		return namecolor;
	}

	public ChatColor getLevelColor() {
		return levelcolor;
	}
	public static PEnchantTier getByID(int id){
		for(PEnchantTier p : PEnchantTier.values()){
			if(p.getID() == id){
				return p;
			}
		}
		return null;
	}
}
