package fun.peaksol.blctimermanager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.badlion.timers.api.Timer;

public class EventListener implements Listener {
	@EventHandler
	public void onLogout(PlayerQuitEvent e){
		Player p = e.getPlayer();
		for(Timer timer : BLCTimerManager.timers.values()) {
			if(timer.getReceivers().contains(p)) {
				timer.removeReceiver(p);
			}
		}
	}
}
