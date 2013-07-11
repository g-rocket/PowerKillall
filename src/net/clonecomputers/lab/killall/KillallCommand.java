package net.clonecomputers.lab.killall;

import java.util.*;
import org.bukkit.command.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.*;

public class KillallCommand implements CommandExecutor{
	PowerKillall plugin;

	public KillallCommand(PowerKillall plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		int radius = Integer.parseInt(args[0]);
		Location center = null;
		String entityName = args[args.length-1];

		if(args.length != 6 && !(sender instanceof Player)){
			sender.sendMessage("only player can use powerkillall with that few arguments");
			return false;
		}
		if(args.length > 5){
			World w = args.length == 6?
				plugin.getServer().getWorld(args[4]): ((Player)sender).getWorld();
			center = new Location(w,Double.parseDouble(args[1]),
					Double.parseDouble(args[2]),Double.parseDouble(args[3]));
		}else{
			center = ((Player)sender).getLocation();
			if(args.length == 3){
				center.setWorld(plugin.getServer().getWorld(args[1]));
			}
		}
		
		Class<? extends Entity> entityType = null;
		try {
			entityType = (Class<? extends Entity>)Class.forName(entityName);
		} catch (ClassNotFoundException e) {
			try{
				entityType = (Class<? extends Entity>)Class.forName("org.bukkit.entity." + entityName);
				//c2 = Class.forName("org.bukkit.craftbukkit.v1_5_R3.entity.Craft" + s); // hack
			}catch(ClassNotFoundException notFoundEx){
				sender.sendMessage(entityName + " is not a known type");
				return true;
			}catch (ClassCastException cantCastEx) {
				sender.sendMessage(entityName + " is not an entity");
				return true;
			}
		}

		int r2 = radius * radius;
		int killCount = 0;
		for(Chunk chunk: center.getWorld().getLoadedChunks()){
			for(Entity ent: chunk.getEntities()){
				if(!entityType.isInstance(ent)) continue;
				if(ent.getLocation().distanceSquared(center) > r2) continue; // too far away
				if(ent instanceof Player) continue; // we won't remove players
				// now we know that ent is something we want to kill
				kill(ent);
				killCount++;
			}
		}
		if(sender instanceof Player){
			plugin.getServer().broadcast(sender.getName() + " PowerKilled " + killCount+
					" " + entityName, "powerkillall.command");
		}
		System.out.println(sender.getName() + " PowerKilled " + killCount + " " + entityName);
		return true;
	}

	private void kill(Entity ent){
		//System.out.println("killing "+ent.getClass().getSimpleName());
		if(ent instanceof LivingEntity){
			EntityDeathEvent event = new EntityDeathEvent((LivingEntity)ent, new ArrayList<ItemStack>(0));
			plugin.getServer().getPluginManager().callEvent(event);
		}else{
			// no other special actions yet
		}
		ent.eject(); // just in case
		ent.remove();
	}

}
