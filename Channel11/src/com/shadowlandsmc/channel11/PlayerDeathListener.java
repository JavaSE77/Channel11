package com.shadowlandsmc.channel11;

import java.util.List;
import java.util.Random;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

public class PlayerDeathListener implements Listener {

  private static Plugin plugin = Main.plugin;
  //[Channel 11 news] %announcer% presenting for channel 11 news. %player% was found dead. Police have ruled it a homicide 
  @SuppressWarnings("unchecked")
  public List<String> announcer = (List<String>) plugin.getConfig().getList("announcers");
  
  
  @EventHandler
  public void onPlayerDeathEvent(PlayerDeathEvent event){
    
	  
	  //If the message has already been set by another plugin, ignore
	  if(event.getDeathMessage() == null) {
		  return;
	  }
    //remove default death message
    event.setDeathMessage(null);
    
    if(event.getEntity().getKiller() != null) {
    	
        //killed by custom item
        if(event.getEntity().getKiller().getInventory().getItemInMainHand() != null 
        		&& event.getEntity().getKiller().getInventory().getItemInMainHand().getItemMeta().getDisplayName() != "") {
          String msg = plugin.getConfig().getString("msgKilledByCustom");
          //The way we are replacing color codes is bad practice, and may not work in future updates
          plugin.getServer().broadcastMessage(msg.replaceAll("&", "§").replaceAll("%announcer%", getAnnoucer()).
              replaceAll("%player%", event.getEntity().getDisplayName()).replaceAll("%item%", event.getEntity()
                  .getKiller().getInventory().getItemInMainHand().getItemMeta().getDisplayName()).replaceAll("%killer%", 
                      event.getEntity().getKiller().getName()));
          return;
          
          //killed by entity, no custom
        } else {
          if(event.getEntity().getKiller().getName() != null) {
            String msg = plugin.getConfig().getString("msg");
            //The way we are replacing color codes is bad practice, and may not work in future updates
            plugin.getServer().broadcastMessage(msg.replaceAll("&", "§").replaceAll("%announcer%", getAnnoucer()).
                replaceAll("%player%", event.getEntity().getDisplayName()).replaceAll("%killer%", event.getEntity().getKiller().getName()));
            return;
            
          }
        }
    } else {
    	//Code from https://www.spigotmc.org/threads/getkiller-returning-null.395106/
    	//Fixed returning null error in 1.17
    	  EntityDamageEvent damageCause = event.getEntity().getLastDamageCause();
    	  if (damageCause != null) {
    	    if (damageCause instanceof EntityDamageByEntityEvent) {
    	      EntityDamageByEntityEvent entityDamageCause = (EntityDamageByEntityEvent) damageCause;
    	      
    	      //check if the mob has a custom name
    	      if(entityDamageCause.getDamager().getCustomName() != null) {
    	    	     
                  String msg = plugin.getConfig().getString("msgMobWithName");
                  //The way we are replacing color codes is bad practice, and may not work in future updates
                  plugin.getServer().broadcastMessage(msg.replaceAll("&", "§").replaceAll("%announcer%", getAnnoucer()).
                      replaceAll("%player%", event.getEntity().getDisplayName()).replaceAll("%killer%", entityDamageCause.getDamager().getCustomName()));
        	      return;
    	      } else {
    	     
              String msg = plugin.getConfig().getString("msgMobWithoutName");
              //The way we are replacing color codes is bad practice, and may not work in future updates
              plugin.getServer().broadcastMessage(msg.replaceAll("&", "§").replaceAll("%announcer%", getAnnoucer()).
                  replaceAll("%player%", event.getEntity().getDisplayName()).replaceAll("%killer%", entityDamageCause.getDamager().getType().toString().toLowerCase()));
    	      return;
    	      }
    	    }
    	  }   
    	  
    	  
    	  //not killed by entity
    	        String msg = plugin.getConfig().getString("msgNoKiller");
    	        //The way we are replacing color codes is bad practice, and may not work in future updates
    	        plugin.getServer().broadcastMessage(msg.replaceAll("&", "§").replaceAll("%announcer%", getAnnoucer()).
    	            replaceAll("%player%", event.getEntity().getDisplayName()));
    	    
    }

    
  }
  

  /**
   * Get the announcer for the news cast
   * @return String
   * */
  public String getAnnoucer() {

    if(announcer.isEmpty() != true) {
    
    Random random = new Random();
    int random_int = random.nextInt(announcer.size());
    
    return announcer.get(random_int);
    }
    
    return "generic announcer";
  }
  
}
