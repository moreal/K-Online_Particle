package com.Pandahyun.Main;

import org.bukkit.Bukkit;

//import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Particles extends BukkitRunnable{
	
	public main m;
	public Player _player;
	
	public static void Flame(Player player)
	{
		int i = 0, j;
		Location lc = player.getLocation().add(0, 1, 0);
		for (j = 0; j < 2; j++) {
			lc = player.getLocation().add(0, 1, 0);
			lc.setX(lc.getX() + Math.sin(i));
			lc.setZ(lc.getZ() + Math.cos(i));
			ParticleEffect.FLAME.display(0, 0, 0, 0.000001F, 1, lc, 192);
			i++;
		}
		if (i == 360)
			i = 0;
	}

	public static void s(Player p) {
		//Bukkit.getScheduler().
		Flame(p);
	}
	
	private JavaPlugin plugin;
	
	public Particles(JavaPlugin plugin, Player player)
	{
		this.plugin = plugin;
		_player = player;
	}
	
	@Override
	public void run()
	{
		plugin.getServer().broadcastMessage("hello");
		for(Player p: Bukkit.getOnlinePlayers())
		{
			if(plugin.getConfig().getBoolean("Players."+p.getUniqueId().toString()+".Settings.OnOff"))
			{
				
			}
			else cancel();
		}
	}
}