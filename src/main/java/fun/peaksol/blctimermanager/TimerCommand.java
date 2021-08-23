package fun.peaksol.blctimermanager;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import net.badlion.timers.api.Timer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TimerCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission("blctimermanager.command")) {
			sender.sendMessage("§cYou don't have permission to use this command.");
			return true;
		}
		switch(args.length > 0 ? args[0] : "help") {
			case "help":
				sender.sendMessage(new String[] {
					"§b§l----- BLCTimerManager by Peaksol -----",
					"§3/" + label + " help §f- §6Show all commands",
					"§3/" + label + " create §f- §6Creates a timer",
					"§3/" + label + " remove §f- §6Removes a timer",
					"§3/" + label + " list §f- §6Lists all timers created",
					"§3/" + label + " info §f- §6Shows properties of a timer",
					"§3/" + label + " edit §f- §6Edits properties of a timer",
					"§3/" + label + " receiver §f- §6Manages receivers of a timer"
				});
				break;
			case "create":
				if(args.length < 5) {
					sender.sendMessage(new String[] {
						"§cCorrect usage: /" + label + " create <name> <item> <time> <title>",
						"§3- <name>: §6Identifier of the timer",
						"§3- <item>: §6Item to show in the client (Use \":\" to split item name and meta value)",
						"§3- <time>: §6Countdown time in seconds (Use suffix \"r\" for repeating, e.g. 10r)",
						"§3- <title>: §6Name to show in the client"
					});
					return true;
				}

				if(BLCTimerManager.timers.containsKey(args[1])) {
					sender.sendMessage("§cA timer named \"" + args[1] + "\" already exists.");
					return true;
				}

				ItemStack createItemStack;
				try {
					createItemStack = Utils.parseItemStack(args[2]);
				}
				catch(Exception e) {
					sender.sendMessage("§cCouldn't find item \"" + args[2] + "\".");
					return true;
				}

				long createTime;
				boolean createRepeating;
				try {
					if(args[3].endsWith("r")) {
						createTime = Long.parseLong(args[3].substring(0, args[3].length() - 1));
						createRepeating = true;
					}
					else {
						createTime = Long.parseLong(args[3]);
						createRepeating = false;
					}
				}
				catch(Exception e) {
					sender.sendMessage("§cTime \"" + args[3] + "\" is invalid.");
					return true;
				}

				String createTitle = String.join(" ", Arrays.copyOfRange(args, 4, args.length));

				Timer creatTimer = BLCTimerManager.timerApi.createTimeTimer(createTitle, createItemStack, createRepeating, createTime, TimeUnit.SECONDS);
				BLCTimerManager.timers.put(args[1], creatTimer);
				sender.sendMessage(new String[] {
					"§aSuccessfully created timer \"" + args[1] + "\":",
					"§3- title: §6" + createTitle,
					"§3- item: §6" + createItemStack.getType() + ":" + createItemStack.getDurability(),
					"§3- time: §6" + createTime + " sec",
					"§3- repeating: §6" + (createRepeating ? "yes" : "no")
				});
				break;
			case "remove":
				if(args.length < 2) {
					sender.sendMessage(new String[] {
						"§cCorrect usage: /" + label + " remove <name>",
						"§3- <name>: §6Identifier of the timer"
					});
					return true;
				}

				if(!BLCTimerManager.timers.containsKey(args[1])) {
					sender.sendMessage("§cCouldn't find a timer named \"" + args[1] + "\".");
					return true;
				}

				BLCTimerManager.timers.get(args[1]).clearReceivers();
				BLCTimerManager.timers.remove(args[1]);
				sender.sendMessage("§aSuccessfully removed timer \"" + args[1] + "\":");
				break;
			case "list":
				sender.sendMessage("§3Timers (" + BLCTimerManager.timers.size() + "): §6" + String.join(", ", BLCTimerManager.timers.keySet()));
				break;
			case "info":
				if(args.length < 2) {
					sender.sendMessage(new String[] {
						"§cCorrect usage: /" + label + " info <name>",
						"§3- <name>: §6Identifier of the timer"
					});
					return true;
				}

				if(!BLCTimerManager.timers.containsKey(args[1])) {
					sender.sendMessage("§cCouldn't find a timer named \"" + args[1] + "\".");
					return true;
				}

				Timer infoTimer = BLCTimerManager.timers.get(args[1]);
				sender.sendMessage(new String[] {
					"§aProperties of timer \"" + args[1] + "\":",
					"§3- title: §6" + infoTimer.getName(),
					"§3- item: §6" + infoTimer.getItem().getType() + ":" + infoTimer.getItem().getDurability(),
					"§3- time: §6" + infoTimer.getMillis() / 1000 + " sec",
					"§3- repeating: §6" + (infoTimer.isRepeating() ? "yes" : "no")
				});
				break;
			case "edit":
				if(args.length < 4) {
					sender.sendMessage(new String[] {
						"§cCorrect usage: /" + label + " edit <name> <property> <value>",
						"§3- <name>: §6Identifier of the timer",
						"§3- <property>: §6Property to edit (item/time/title)",
						"§3- <value>: §6Value of the property"
					});
					return true;
				}

				if(!BLCTimerManager.timers.containsKey(args[1])) {
					sender.sendMessage("§cCouldn't find a timer named \"" + args[1] + "\".");
					return true;
				}

				Timer editTimer = BLCTimerManager.timers.get(args[1]);
				switch(args[2]) {
					case "item":
						try {
							ItemStack item = Utils.parseItemStack(args[3]);
							editTimer.setItem(item);
							sender.sendMessage("§aSuccessfully set timer \"" + args[1] + "\"'s item to \"" + item.getType() + ":" + item.getDurability() + "\".");
						}
						catch(Exception e) {
							sender.sendMessage("§cCouldn't find item \"" + args[2] + "\".");
						}
						break;
					case "time":
						try {
							long editTime;
							boolean editRepeating;
							if(args[3].endsWith("r")) {
								editTime = Long.parseLong(args[3].substring(0, args[3].length() - 1));
								editRepeating = true;
							}
							else {
								editTime = Long.parseLong(args[3]);
								editRepeating = false;
							}
							editTimer.setTime(editTime, TimeUnit.SECONDS);
							editTimer.setRepeating(editRepeating);
							sender.sendMessage("§aSuccessfully set timer \"" + args[1] + "\"'s time to " + editTime + " sec (" + (editRepeating ? "" : "no ") + "repeating).");
						}
						catch(Exception e) {
							sender.sendMessage("§cTime \"" + args[3] + "\" is invalid.");
						}
						break;
					case "title":
						String editTitle = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
						editTimer.setName(editTitle);
						sender.sendMessage("§aSuccessfully set timer \"" + args[1] + "\"'s title to \"" + editTitle + "\".");
						break;
					default:
						sender.sendMessage("§cProperty \"" + args[2] + "\" is unknown.");
				}
				break;
			case "receiver":
				if(args.length < 3) {
					sender.sendMessage(new String[] {
						"§cCorrect usage: /" + label + " receiver <name> <operation> [player]",
						"§3- <name>: §6Identifier of the timer",
						"§3- <operation>: §6Operation to perform (add/remove/clear/list)",
						"§3- [player]: §6The target player (not required when <operation> is \"clear\" or \"list\")"
					});
					return true;
				}

				if(!BLCTimerManager.timers.containsKey(args[1])) {
					sender.sendMessage("§cCouldn't find a timer named \"" + args[1] + "\".");
					return true;
				}

				Timer receiverTimer = BLCTimerManager.timers.get(args[1]);
				switch(args[2]) {
					case "add":
						if(args.length < 4) {
							sender.sendMessage("§cTarget player is not specified.");
							return true;
						}

						Player addPlayer = Bukkit.getPlayer(args[3]);

						if(addPlayer == null || !addPlayer.isOnline()) {
							sender.sendMessage("§cCouldn't find the player named \"" + args[3] + "\".");
							return true;
						}

						if(receiverTimer.getReceivers().contains(addPlayer)) {
							sender.sendMessage("§aPlayer \"" + addPlayer.getName() + "\" is already a receiver of timer \"" + args[1] + "\".");
							return true;
						}

						receiverTimer.addReceiver(addPlayer);
						sender.sendMessage("§aSuccessfully added player \"" + addPlayer.getName() + "\" as a receiver of timer \"" + args[1] + "\".");
						break;
					case "remove":
						if(args.length < 4) {
							sender.sendMessage("§cTarget player is not specified.");
							return true;
						}

						Player removePlayer = Bukkit.getPlayer(args[3]);

						if(removePlayer == null || !removePlayer.isOnline()) {
							sender.sendMessage("§cCouldn't find the player named \"" + args[3] + "\".");
							return true;
						}

						if(!receiverTimer.getReceivers().contains(removePlayer)) {
							sender.sendMessage("§aPlayer \"" + removePlayer.getName() + "\" is not a receiver of timer \"" + args[1] + "\".");
							return true;
						}

						receiverTimer.removeReceiver(removePlayer);
						sender.sendMessage("§aSuccessfully removed player \"" + removePlayer.getName() + "\" from receivers of timer \"" + args[1] + "\".");
						break;
					case "clear":
						receiverTimer.clearReceivers();
						sender.sendMessage("§aSuccessfully removed all receivers of timer \"" + args[1] + "\".");
						break;
					case "list":
						Collection<String> receiverList = Utils.collectionPlayerToString(receiverTimer.getReceivers());
						sender.sendMessage("§3Timer \"" + args[1] + "\"'s receivers (" + receiverList.size() + "): §6" + String.join(", ", receiverList));
						break;
					default:
						sender.sendMessage("§cOperation \"" + args[2] + "\" is unknown.");
				}
				break;
			default:
				sender.sendMessage("§cIncorrect usage. Type \"/" + label + " help\" for help.");
		}
		return true;
	}
}
