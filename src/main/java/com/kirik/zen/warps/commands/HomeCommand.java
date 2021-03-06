package com.kirik.zen.warps.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.config.PlayerConfiguration;
import com.kirik.zen.main.ZenCommandException;

@Names("home")
@Help("Teleports player to their home")
@Usage("/home")
@Permission("zen.home")
public class HomeCommand extends ICommand {
	
	@Override
	public void run(final CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		final Player player = (Player)commandSender;
		PlayerConfiguration playerConfig = new PlayerConfiguration(player.getUniqueId());
		Location prevLoc = player.getLocation();
		prevLoc.setYaw(player.getLocation().getYaw());
		prevLoc.setPitch(player.getLocation().getPitch());
		if(playerHelper.getHome(player) == null){
			
			if(player.hasPermission("zen.home.override")){
				playerConfig.getPlayerConfig().set("previousLocation", prevLoc);
				playerConfig.savePlayerConfig();
				player.teleport(plugin.getServer().getWorld("world").getSpawnLocation());
				playerHelper.sendDirectedMessage(commandSender, "Teleported home.");
			}else{
				playerHelper.sendDirectedMessage(player, "Please wait 5 seconds for teleportation.");
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run(){
						playerConfig.getPlayerConfig().set("previousLocation", prevLoc);
						playerConfig.savePlayerConfig();
						player.teleport(plugin.getServer().getWorld("world").getSpawnLocation());
						playerHelper.sendDirectedMessage(commandSender, "Teleported home.");
					}
				}, 100L);
			}
		}else{
			if(player.hasPermission("zen.home.override")){
				playerConfig.getPlayerConfig().set("previousLocation", prevLoc);
				playerConfig.savePlayerConfig();
				player.teleport(playerHelper.getHome(player));
				playerHelper.sendDirectedMessage(commandSender, "Teleported home.");
			}else{
				playerHelper.sendDirectedMessage(player, "Please wait 5 seconds for teleportation.");
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run(){
						playerConfig.getPlayerConfig().set("previousLocation", prevLoc);
						playerConfig.savePlayerConfig();
						player.teleport(playerHelper.getHome(player));
						playerHelper.sendDirectedMessage(commandSender, "Teleported home.");
					}
				}, 100L);
			}
			//player.teleport(playerHelper.getHome(player));
		}
	}
	
}