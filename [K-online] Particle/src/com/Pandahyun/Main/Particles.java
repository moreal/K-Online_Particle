package com.Pandahyun.Main;

import org.bukkit.Bukkit;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Particles extends BukkitRunnable {

	public Player _p;

	public int _x = 0, _y = 0, _z = 0, i = 0;

	Location lc;
	
	public JavaPlugin plugin;

	public Particles(JavaPlugin plugin, Player player) {
		this.plugin = plugin;
		_p = player;
	}

	@Override
	public void run() {
		plugin.getServer().broadcastMessage("hello" + _p.getUniqueId().toString());
		if (this.plugin.getConfig().getBoolean("Players." + _p.getUniqueId().toString() + ".Settings.OnOff")) {
			_p.sendMessage("non");
			if (main.TaskIds.containsKey(_p.getUniqueId().toString()))
				_p.sendMessage("Ok");
			else
				_p.sendMessage("NONO");
			Flame(_p);
			// setPosition();
		} else {
			Bukkit.getServer().getScheduler().cancelTask(main.TaskIds.get(_p.getUniqueId().toString()));
		}

	}

	public void Flame(Player player) {
		ParticleEffect.FLAME.display(0, 0, 0, 0.000001F, 1, lc, 192);
	}

	public void s(Player p) {
		if (sGetU(p))
		Flame(p);
	}
	
	public void  circle(Player p)
	{
		lc = p.getLocation().add(0, 1, 0);
		lc.setX(lc.getX() + Math.cos(i * 0.5));
		lc.setZ(lc.getZ() + Math.sin(i * 0.5));
		i++;
		if (i == 360)
			i = 0;
	}
	
	public void  up_circle(Player p)
	{
		lc = p.getLocation().add(0, 1, 0);
		lc.setX(lc.getX() + Math.cos(i * 0.5));
		lc.setZ(lc.getZ() + Math.sin(i * 0.5));
		lc.setY(lc.getY() + i/360);
		i++;
		if (i == 360)
			i = 0;
	}

	public void setPosition() {

	}
}