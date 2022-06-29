package CustomEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;



public class ExpEvent extends Event {
	private Player player;
	private double amount;
	private static final HandlerList handlers = new HandlerList();
	public ExpEvent(Player player ,double amount) {

		this.amount = amount;
		this.player = player;
	}
	public Player getPlayer(){
		return this.player;
	}

	public double getAmount(){
		return amount;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
