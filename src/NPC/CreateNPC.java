package NPC;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.md_5.bungee.api.ChatColor;

public class CreateNPC {
	public static void CreateRawExtractor(Location location) {
		NPCRegistry registry = CitizensAPI.getNPCRegistry();
		NPC npc = registry.createNPC(EntityType.PLAYER, ChatColor.RED + "Phân Giải");
		npc.setProtected(true);
		npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA, "SolarFavor");
		npc.data().set(NPC.RESPAWN_DELAY_METADATA, 1);		
		npc.spawn(location);
	}
	
	public static void createGuard(Location location) {
		NPCRegistry registry = CitizensAPI.getNPCRegistry();
		NPC npc = registry.createNPC(EntityType.PLAYER, ChatColor.RED + "Vệ Binh");	
		npc.setProtected(false);
		npc.data().set(NPC.RESPAWN_DELAY_METADATA, 1200);
		npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA, "_StyleSerene");
		
		npc.spawn(location);
		(  (LivingEntity)	npc.getEntity()).getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
		 (  (LivingEntity)	npc.getEntity()).getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
		 (  (LivingEntity)	npc.getEntity()).getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
		 (  (LivingEntity)	npc.getEntity()).getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
		 (  (LivingEntity)	npc.getEntity()).getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "npc sel " + npc.getId());
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "npc lookclose");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "trait sentinel");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "sentinel addtarget event:pvp");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "sentinel addignore NPCS");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "sentinel range 5");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "sentinel damage 10");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "sentinel spawnpoint");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "sentinel attackrate 10");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "sentinel healrate 1");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "sentinel health 100");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "sentinel chaserange 30");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "sentinel chaseranged");
	}
	
	
	
	
	

}
