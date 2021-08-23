package fun.peaksol.blctimermanager;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Utils {
	public static ItemStack parseItemStack(String name) {
		if(name.contains(":")) {
			String[] split = name.split(":");
			return new ItemStack(Material.matchMaterial(split[0]), 1, Short.parseShort(split[1]));
		}
		else {
			return new ItemStack(Material.matchMaterial(name));
		}
	}
	public static Collection<String> collectionPlayerToString(Collection<Player> players) {
		Collection<String> result = new ArrayList<>();
		for (Player player : players) {
			result.add(player.getName());
		}
		return result;
	}
}
