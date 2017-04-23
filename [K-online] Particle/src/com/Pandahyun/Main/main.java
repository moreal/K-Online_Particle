package com.Pandahyun.Main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.Pandahyun.Main.Particles;



public class main extends JavaPlugin implements Listener
{

	public HashMap<String, Boolean> Setting = new HashMap<String, Boolean>();
	public HashMap<String, List<Boolean>> Originparticles = new HashMap<String, List<Boolean>>();
	public HashMap<String, Integer> TaskIds = new HashMap<String, Integer>();
	
	private FileConfiguration CustomConfig = null;
	private File CustomFile = null;
	private File ConfigFile = new File(getDataFolder() + "config.yml");
	
	int i=0,j,nMax;
	
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[K-online]" + ChatColor.WHITE + " 파티클 후원 시스템 기동!");
		if(!ConfigFile.exists())
		{
			getConfig().set("Settings.Particles.Max", 32);
			saveConfig();
		}
		nMax = getConfig().getInt("Settings.Particles.Max");
	}
	
	public void onDisable()
	{
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[K-online]" + ChatColor.WHITE + " 파티클 후원 시스템 정상종료!");
	}
	
	public boolean onCommand(CommandSender sender, Command label, String command, String[] args)
	{
		Player player = (Player) sender;
		Particles.s(player);
		if(command.equalsIgnoreCase("kparticle")||command.equalsIgnoreCase("kpt"))
		{
			if(args.length > 0)
			{
				if(args[0].equalsIgnoreCase("help"))
				{
					sender.sendMessage("\n" + ChatColor.AQUA + "K-Particle 케이온라인 후원서비스 플러그인 By Pandahyun\n" + ChatColor.RESET
							+ "명령어 보기 - /kpt help"
							+ "\n자신의 후원GUI 열기 - /kpt view"
							+ "\n다른이의 후원GUI 보기[OP] - /kpt view <플레이어명>"
							+ "\n후원 효과 얻기[OP] - /kpt get <플레이어명> <얻는 효과>"
							+ "\n후원 효과 뺐기[OP] - /kpt steal <플레이어명> <뺏는 효과>"
							+ "\n후원 효과 만들기[OP] - /kpt new <효과 이름> <명령어>"
							+ "\n후원 효과 지우기[OP] - /kpt remove <효과 이름>"
							+ "\n후원 파티클 실행하기[OP] - /kpt run <효과 이름>");
					getConfig().set("hello.hel.hel1o.helo", "hello");
					saveConfig();
				}
				
				else if(args[0].equalsIgnoreCase("view"))
				{
					openGUI(ChatColor.BLUE + "" + ChatColor.BOLD + "K-Particle 케이온라인 후원 서비스", 45, player);
				}
				
				else if(args[0].equalsIgnoreCase("masteriskong"))
				{
					getConfig().set("Players."+player.getUniqueId().toString()+".Settings.OnOff", true);
				}
			}
			else
			{
				sender.sendMessage("\n" + ChatColor.AQUA + "K-Particle 케이온라인 후원서비스 플러그인 By Pandahyun\n" + ChatColor.RESET
						+ "명령어 보기 - /kpt help"
						+ "\n자신의 후원GUI 열기 - /kpt view"
						+ "\n다른이의 후원GUI 보기[OP] - /kpt view <플레이어명>"
						+ "\n후원 효과 얻기[OP] - /kpt get <플레이어명> <얻는 효과>"
						+ "\n후원 효과 뺐기[OP] - /kpt steal <플레이어명> <뺏는 효과>"
						+ "\n후원 효과 만들기[OP] - /kpt new <효과 이름> <명령어>"
						+ "\n후원 효과 지우기[OP] - /kpt remove <효과 이름>"
						+ "\n후원 파티클 실행하기[OP] - /kpt run <효과 이름>");
			}
		}
		return false;
	}
	
	@EventHandler
	public void onJoinEvent(PlayerJoinEvent e)
	{
		if(!getConfig().contains(e.getPlayer().getUniqueId().toString()))
		{
			List<Boolean> pList = new ArrayList<Boolean>();
			for(int i = 0; i<nMax;++i)
				pList.set(i, false);
			getConfig().set("Players."+e.getPlayer().getUniqueId().toString()+".HavingOriginParticles",pList);
			getConfig().set("Players."+e.getPlayer().getUniqueId().toString()+".Settings.Selected", 0);
			saveConfig();
		}
		//showparticle(e.getPlayer());
		//BukkitTask task = new Particles(this,e.getPlayer()).runTaskTimer(this, 0, 20);
		TaskIds.put(e.getPlayer().getUniqueId().toString(), getServer().getScheduler().scheduleSyncRepeatingTask(this, new Particles(this,e.getPlayer()), 0, 20));
	}
	
	public void openGUI(String name, int size, Player player)
	{
		Inventory inv = Bukkit.createInventory(player, size, name);
		
		setItem("케이온라인 흥해라", 10, 0, 20, Arrays.asList(ChatColor.WHITE + "하지만 그 실체는" + ChatColor.RED +" 용암"), 2, inv);
		setItem("케이온라인 망해라", 1, 0, 10, Arrays.asList("안녕 프렌즈들",ChatColor.WHITE + "농담인거 알지?"),3,inv);
		
		player.openInventory(inv);
	}
	
	private void setItem(String Display, int ID, int DATA, int STACK, List<String> lore, int loc, Inventory inv)
	{
		ItemStack item = new MaterialData(ID,(byte)DATA).toItemStack(STACK);
		ItemMeta itemMeta = item.getItemMeta();
		
		itemMeta.setDisplayName(Display);
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		inv.setItem(loc, item);
	}
	
	public void showparticle(Player player)
	{
		
	}
	
	
	
	
	
	private void reloadCustomConfig()
	{
		if(CustomFile == null) CustomFile = new File(getDataFolder(), "customconfig.yml");
		CustomConfig = YamlConfiguration.loadConfiguration(CustomFile);
		
		InputStream defConfigStream = this.getResource("config.yml");
		if(defConfigStream != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			CustomConfig.setDefaults(defConfig);
		}
	}
	
	public void saveCustomConfig()
	{
		if(CustomFile == null || CustomConfig == null) return;
		try {
			getCustomConfig().save(CustomFile);
		} catch (IOException e) {
			Bukkit.getConsoleSender().sendMessage("Can't save config");
		}
	}
	
	public void saveDefaultConfig()
	{
		if(CustomFile == null) CustomFile = new File(getDataFolder(),"customconfig.yml");
		if(!CustomFile.exists()) this.saveResource("config.yml", false);
	}
	
	public FileConfiguration getCustomConfig()
	{
		if(CustomConfig == null) reloadCustomConfig();
		return CustomConfig;
	}
	
}