package com.Pandahyun.Main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.Pandahyun.Main.Particles;

public class main extends JavaPlugin implements Listener
{

	public HashMap<String, Boolean> Setting = new HashMap<String, Boolean>();
	public static HashMap<String, Integer> TaskIds = new HashMap<String, Integer>();
	public List<String> Particle = new ArrayList<String>();
	public List<String> Shape = new ArrayList<String>();
	
	public final int BOOK = 403;
	/*private FileConfiguration CustomConfig = null;
	private File CustomFile = null;*/
	private File ConfigFile = new File(getDataFolder() + "config.yml");
	
	int i=0,j,nMax;
	
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[K-online]" + ChatColor.WHITE + " 파티클 후원 시스템 기동!");
		if(!ConfigFile.exists())
		{
			Shape.add("Circle");
			Shape.add("Up_Circle");
			Shape.add("On_Head");
			Shape.add("AngelWing");
			
			Particle.add("Flame");
			Particle.add("Heart");
			Particle.add("Smoke");
			Particle.add("Fireworks_spark");
			Particle.add("Spell");
			
			getConfig().set("Settings.Shapes", Shape);
			getConfig().set("Settings.Particles", Particle);
			
			saveConfig();
			return;
		}
		Particle = getConfig().getStringList("Settings.Particles");
		Shape = getConfig().getStringList("Settings.Shapes");
	}
	
	public void onDisable()
	{
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[K-online]" + ChatColor.WHITE + " 파티클 후원 시스템 정상종료!");
	}
	
	public boolean onCommand(CommandSender sender, Command label, String command, String[] args)
	{
		Player player = (Player) sender;
		if(command.equalsIgnoreCase("kparticle")||command.equalsIgnoreCase("kpt"))
		{
			if(args.length > 0)
			{
				if(args[0].equalsIgnoreCase("help"))
					CommandHelp(player);
				
				else if(args[0].equalsIgnoreCase("view"))
				{
					if(args.length > 0)
					{
						for(Player p : Bukkit.getOnlinePlayers())
							if (player.getName().equalsIgnoreCase(p.getName())) ParticleGUI(ChatColor.BLUE + "" + ChatColor.BOLD + "K-Particle 케이온라인 후원 서비스", 45, player, p);
							else player.sendMessage("[ERROR] 없는 플레이어 입니다");
					}
					else ParticleGUI(ChatColor.BLUE + "" + ChatColor.BOLD + "K-Particle 케이온라인 후원 서비스", 45, player, player);
				}
				
				else if(args[0].equalsIgnoreCase("stop"))
				{
					if(args.length>1)
						for(Player p : Bukkit.getOnlinePlayers())
							if(p.getName().equalsIgnoreCase(args[1]))
								stopParticle(p);
					else Bukkit.getScheduler().cancelTask(TaskIds.get(sGetU(player)));
				}
				
				else if(args[0].equalsIgnoreCase("give"))
				{
					if(args.length>3)
					{
						if(args[2].equalsIgnoreCase("Particle"))
							giveItem("[K - Particle] Particle " + args[3], BOOK, 0, 1,
									Arrays.asList(ChatColor.AQUA + "케이온라인 파티클 시스템",
												  Particle.contains(args[3]) ? args[3] : ChatColor.RED + "없는 파티클입니다."), player);
						
						else if(args[2].equalsIgnoreCase("Shape"))
							giveItem("[K - Particle] Shape " + args[3], BOOK, 0, 1,
									Arrays.asList(ChatColor.AQUA + "케이온라인 파티클 시스템",
												  Shape.contains(args[3]) ? args[3] : ChatColor.RED + "없는 모양입니다."), sGetP(args[1]));
						
						else player.sendMessage("[Error] /kpt give <Player> particle (or shape) <String>");
					}
					else player.sendMessage("[Error] /kpt give <Player> particle (or shape) <String>");
					saveConfig();
				}
			}
			else
				CommandHelp(player);
		}
		return false;
	}
	

	
	@EventHandler
	public void onJoinEvent(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		
		if(!(getConfig().contains("Players." + e.getPlayer().getUniqueId().toString())))
		{
			List<String> pList = new ArrayList<String>();
			getConfig().set("Players."+e.getPlayer().getUniqueId().toString()+".Having.Particles",pList);
			getConfig().set("Players."+e.getPlayer().getUniqueId().toString()+".Having.Shape",pList);
			getConfig().set("Players."+e.getPlayer().getUniqueId().toString()+".Settings.Selected.Shape", "None");
			getConfig().set("Players."+e.getPlayer().getUniqueId().toString()+".Settings.Selected.Particle", "None");
			getConfig().set("Players."+sGetU(p)+".Settings.OnOff", false);
			saveConfig();
		}
		
		TaskIds.put(sGetU(p), getServer().getScheduler().scheduleSyncRepeatingTask(this, new Particles(this,p), 0, 2));
		e.getPlayer().sendMessage(TaskIds.get(sGetU(p)).toString() + "로 생성됨");
		
		giveItem("[K - Particle] Flame", BOOK, 0, 1, Arrays.asList(ChatColor.AQUA + "케이온라인 파티클 시스템", "Flame"), p);
		
		p.sendMessage(Particle.toString());
	}
	
	@EventHandler
	public void onQuitEvent(PlayerQuitEvent e)
	{
		if(TaskIds.containsKey(sGetU(e.getPlayer())))
		{
			getServer().getScheduler().cancelTask(TaskIds.get(sGetU(e.getPlayer())));
			TaskIds.remove(sGetU(e.getPlayer()));
		}
	}
	
	//
	
	public void ParticleGUI(String name, int size, Player showP, Player tarP)
	{
		Inventory inv = Bukkit.createInventory(showP, size, name);
		List<String> Having = getConfig().getStringList("Players."+tarP.getUniqueId().toString()+".Having.Particles");
		if(!(Having.isEmpty()))
			for(int i=0; i < Having.size(); ++i)
				setItem(Having.get(i), BOOK, 0, 1, 
						Arrays.asList(Having.get(i).equalsIgnoreCase(getConfig().getString("Players." + tarP.getUniqueId().toString() + ".Settings.Selected.Particle")) ? "사용중" : "클릭시 사용"),i,inv);
		else
		{
			showP.sendMessage("[KPT] " + tarP.getName() + " 플러이어는 가지고 있는 파티클이 없습니다.");
			return;
		}
		showP.openInventory(inv);
	}
	
	@EventHandler
	public void getParticle(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		Action a = e.getAction();
		String DisplayName = p.getItemInHand().getItemMeta().getDisplayName();
		List lore = p.getItemInHand().getItemMeta().getLore();
		List<String> Having = getConfig().getStringList("Players." + p.getUniqueId().toString() + ".Having.Particles");
		
		if(a==Action.RIGHT_CLICK_AIR || a==Action.RIGHT_CLICK_BLOCK)
			
			if(p.getItemInHand().getType().getId() == BOOK &&
			p.getItemInHand().getItemMeta().getLore().size() > 1 &&
			Particle.contains(lore.get(1)))
			{
				if(Having.contains(lore.get(1)))
					p.sendMessage("이미 가지고 계십니다");
				
				else 
				{
					Having.add("[KPT]" + lore.get(1));
					getConfig().set("Players." + p.getUniqueId().toString() + ".Having.Particles", Having);
					saveConfig();
					p.getInventory().remove(p.getItemInHand());
					p.sendMessage("[KPT] " + DisplayName + " 파티클을 얻으셨습니다\n"
							+ "확인 명령어 /kpt view 로 확인 하실수 있습니다.");
				}
			}
	}
	
	//private ItemStack makeItem(String Display, int ID, int DATA, int STACK, List<String> lore)
	private void giveItem(String Display, int ID, int DATA, int STACK, List<String> lore, Player p)
	{
		ItemStack item = new MaterialData(ID,(byte)DATA).toItemStack(STACK);
		ItemMeta itemMeta = item.getItemMeta();
		
		itemMeta.setDisplayName(Display);
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		
		p.getInventory().addItem(item);
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
	
	public void setParticle(Player player, String Particle)
	{
		getConfig().set("Players."+player.getUniqueId().toString()+".Settings.Selected.Particle", Particle);
		saveConfig();
	}
	
	public void setShape(Player player, String Shape)
	{
		getConfig().set("Players."+player.getUniqueId().toString()+".Settings.Selected.Shape", Shape);
		saveConfig();
	}
	
	public void stopParticle(Player p)
	{
		getServer().getScheduler().cancelTask(TaskIds.get(sGetU(p)));
		p.sendMessage(p.getName() + "의 태스크를 삭제했습니다." + TaskIds.get(sGetU(p)));
		TaskIds.remove(sGetU(p));
	}
	
	
	
	/*private void reloadCustomConfig()
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
	}*/
	
	public String sGetU(Player p) // 유니크 아이디 받아오기
	{
		return p.getUniqueId().toString();
	}
	
	public void CommandHelp(Player sender)
	{
		sender.sendMessage("\n" + ChatColor.AQUA + "K-Particle 케이온라인 후원서비스 플러그인 By Pandahyun\n" + ChatColor.RESET
				+ "명령어 보기 - /kpt help"
				+ "\n자신의 후원GUI 열기 - /kpt view"
				+ "\n후원 파티클 멈추기 - /kpt stop"
				+ "\n다른이의 후원GUI 보기[OP] - /kpt view <플레이어명>"
				+ "\n후원 효과 주기[OP] - /kpt give <플레이어명> <Particle or Shape> <얻는 효과>"
				+ "\n후원 효과 뺐기[OP] - /kpt steal <플레이어명> <Particle or Shape> <뺏는 효과>"
				+ "\n후원 책 만들기[OP] - /kpt getBook <효과 이름> <파티클>"
				);
	}
	
	private Player sGetP(String s) // 플레이어 받아오기
	{
		/*for (Player p : getServer().getOnlinePlayers())
			{
				if(p.getDisplayName().equals(s)) return p;
			}*/
		return getServer().getPlayer(s);
	}
}