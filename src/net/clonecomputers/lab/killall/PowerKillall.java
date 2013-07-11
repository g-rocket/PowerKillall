package net.clonecomputers.lab.killall;

import org.bukkit.plugin.java.JavaPlugin;

/* Example Template
 * By Adamki11s
 * HUGE Plugin Tutorial
 */


public class PowerKillall extends JavaPlugin {

    public void onDisable() {
    	getCommand("powerkillall").setExecutor(null);
		getLogger().info("PowerKillall 1.0 is disabled!");
	}

	public void onEnable() {
		getLogger().info("PowerKillall 1.0 is enabled!");
        getCommand("powerkillall").setExecutor(new KillallCommand(this));
	}
	
}