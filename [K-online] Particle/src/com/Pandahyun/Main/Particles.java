package com.Pandahyun.Main;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;

//import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.Pandahyun.Main.*;

public class Particles extends BukkitRunnable{
	
	public main m;
	
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
	}
	
	@Override
	public void run()
	{
		plugin.getServer().broadcastMessage("hello");
		for(Player p: Bukkit.getOnlinePlayers())
		{
			if(plugin.getConfig().getBoolean(arg0))
		}
	}
}
