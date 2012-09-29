package ru.ufatos.chat;

import java.util.LinkedList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.configuration.file.FileConfiguration;
import ru.tehkode.permissions.bukkit.PermissionsEx;



@SuppressWarnings("deprecation")
public class Chat implements Listener {
	
	private double RangeMain;
	private double RangeWhispering;
	private double RangeShout;
	private double RangeAction;
	private boolean DeathMessage;
	private double ConfigChance;
	public int ololo;
	public String ololo1;
	
	public Chat(FileConfiguration config) {
		this.RangeMain = config.getDouble("Range.main", this.RangeMain);
		this.RangeWhispering = config.getDouble("Range.whispering", this.RangeWhispering);
		this.RangeShout = config.getDouble("Range.shout", this.RangeShout);
		this.RangeAction = config.getDouble("Range.action", this.RangeAction);
		this.DeathMessage = config.getBoolean("disableDeathMessage", this.DeathMessage);
		this.ConfigChance = config.getDouble("ConfigChance", this.ConfigChance);

	}
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
	  	Player player = event.getPlayer();
		String message = "%1$s: %2$s";		
		String chatMessage = event.getMessage();	
		boolean ranged = true; // я не знаю как это назвать, поэтому ranged (органичен ли чат)
		
		if (chatMessage.startsWith("^g")) {			
			if (PermissionsEx.getUser(player).has("rpchat.global")){
				ranged = false;
				message = "%1$s [GlobalChat]: %2$s";
				chatMessage = ChatColor.GOLD+chatMessage.substring(2);
			}
			else {
				player.sendMessage("У вас нет прав писать в глобальный чат");
				event.setCancelled(true);
			}			
		}
			
		double range = RangeMain; 
		
			if (chatMessage.startsWith("!")) {
			range = RangeShout;
			message = "%1$s кричит: %2$s";
			//chatMessage = ChatColor.BOLD+chatMessage.substring(1); жирный смотрится плохо
			chatMessage = ChatColor.RED+chatMessage.substring(1);
			}
			
			if (chatMessage.startsWith("@")) {
			range = RangeWhispering;
			message = "%1$s шепчет: %2$s";
			chatMessage = ChatColor.ITALIC+chatMessage.substring(1);
			chatMessage = ChatColor.GRAY+chatMessage;			
			}
			
			if (chatMessage.startsWith("***")) {
			range = RangeAction;
			chatMessage = ChatColor.LIGHT_PURPLE+chatMessage.substring(3);
			double chance = Math.random()*100;
			String luck=ChatColor.RED+"(неудачно)"+ChatColor.LIGHT_PURPLE;
			if (chance<ConfigChance){
			luck = ChatColor.GREEN+"(удачно)"+ChatColor.LIGHT_PURPLE;
			}
			message = ChatColor.LIGHT_PURPLE+"**%1$s %2$s "+luck+" **";
			}
			
			if (chatMessage.startsWith("**")) {
			range = RangeAction;
			message = ChatColor.LIGHT_PURPLE+"**%1$s %2$s**";
			chatMessage = chatMessage.substring(2);	
			}
			
		if (ranged)
		{	
			event.getRecipients().clear();
			event.getRecipients().addAll(this.getLocalRecipients(player, message, range));
		}
		event.setFormat(message);
		event.setMessage(chatMessage);	
	    }
	/*убираем сообщение о смерти*/
	 @EventHandler(priority = EventPriority.NORMAL)
	 public void onEntityDeath(EntityDeathEvent event) {
		 if (DeathMessage){
			 if (event instanceof PlayerDeathEvent) {
				 PlayerDeathEvent deathEvent = (PlayerDeathEvent) event;
				 deathEvent.setDeathMessage(null);
         	}
		 }
	 }
		
	 //способ органичения слышимости с одного распростаненого плага
	protected List<Player> getLocalRecipients(Player sender, String message, double range) {
		Location playerLocation = sender.getLocation();
		List<Player> recipients = new LinkedList<Player>();
		double squaredDistance = Math.pow(range, 2);
		for (Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			if (!recipient.getWorld().equals(sender.getWorld())) {
				continue;
			}
			if (playerLocation.distanceSquared(recipient.getLocation()) > squaredDistance) {
				continue;
			}
			recipients.add(recipient);
		}
		return recipients;		
	}
}
