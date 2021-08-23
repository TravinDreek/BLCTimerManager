package fun.peaksol.blctimermanager;

import org.bukkit.plugin.java.JavaPlugin;

import net.badlion.timers.api.Timer;
import net.badlion.timers.api.TimerApi;

import java.util.HashMap;

public class BLCTimerManager extends JavaPlugin {
	public static TimerApi timerApi;
	public static HashMap<String, Timer> timers = new HashMap<String, Timer>();

	@Override
	public void onEnable() {
		timerApi = TimerApi.getInstance();
		getServer().getPluginManager().registerEvents(new EventListener(), this);
		getCommand("blctimermanager").setExecutor(new TimerCommand());
		getLogger().info("Enabled " + getDescription().getFullName());
	}

	@Override
	public void onDisable() {
		getLogger().info("Disabled " + getDescription().getVersion());
	}
}
