package Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

import org.bukkit.Bukkit;

import Solar.prison.Main;
import Solar.prison.Prisoner;

public class LevelTop {
	public static ArrayList<Prisoner> list = new ArrayList<>();
	public static void updateArrayList(){
		list.clear();
		File f =new File( Main.getMain().getDataFolder() + "/PlayerData/");
		for(File z : f.listFiles()){
			Prisoner p = new Prisoner(Bukkit.getOfflinePlayer(UUID.fromString( z.getName().replaceAll(".yml", ""))));	
			p.loadData();
			list.add(p);
		}
		sortTop();
		return;
	}
	public static void sortTop(){
		list.sort(new Comparator<Prisoner>() {
			@Override
			public int compare(Prisoner o1, Prisoner o2) {
				return Integer.valueOf(o2.getLevel()).compareTo(o1.getLevel());
			}
		});
	}
	public static ArrayList<Prisoner> getTopList(){
		return list;
	}
	public static int getPlace(UUID p){
		Prisoner pz = new Prisoner(Bukkit.getOfflinePlayer(p));
		for(Prisoner pzz : list){
			if(pzz.getPlayer().getUniqueId().toString().equalsIgnoreCase(pz.getPlayer().getUniqueId().toString())){
				return list.indexOf(pzz);
						
			}
		}
		return 0;
	}
	public static ArrayList<Prisoner> getTop(int i , int z){
		ArrayList<Prisoner> l = new ArrayList<>();
		while(i+1<=z){
			if(list.size() <= i) break;
			l.add(list.get(i));
			i++;
			
		}
		
		return l;
	}
}
