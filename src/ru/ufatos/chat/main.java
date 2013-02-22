package ru.ufatos.chat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
 
public class main extends JavaPlugin {
	
	public static final Logger _log  = Logger.getLogger("Minecraft");
 
	public void onEnable(){
		//спасибо DmitriyMX за распаковку конфига. Туторы на его сайте DmitriyMX.ru 
		File fileConf = new File(getDataFolder(), "config.yml");
		if(!fileConf.exists()){
		    InputStream resourceAsStream = main.class.getResourceAsStream("/ru/ufatos/chat/config.yml");
		    getDataFolder().mkdirs();
		    try {
		        FileOutputStream fos = new FileOutputStream(fileConf);
		        byte[] buff = new byte[65536];
		        int n;
		        while((n = resourceAsStream.read(buff)) > 0){
		            fos.write(buff, 0, n);
		            fos.flush();
		        }
		        fos.close();
		        buff = null;
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    getLogger().info("Сonfig loaded");
		}
		FileConfiguration config = this.getConfig();
		getServer().getPluginManager().registerEvents(new Chat(config), this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("updnames") & sender.isOp()){
			Chat.updateDisplayNames();
			//sender.sendMessage(sender.getName());
			return true;
		}else if(cmd.getName().equalsIgnoreCase("rpchat")){
			sender.sendMessage(ChatColor.GOLD+"------RPchat-by-ufatos---------------------");
			sender.sendMessage("!      крик");
			sender.sendMessage("@     шепот");
			sender.sendMessage("**    действие");
			sender.sendMessage("***   действие с шансом");
			sender.sendMessage("#     глобальный чат");
			sender.sendMessage(ChatColor.GOLD+"-------------------------------------------");
			return true;
		}
		return false; 
	}
}