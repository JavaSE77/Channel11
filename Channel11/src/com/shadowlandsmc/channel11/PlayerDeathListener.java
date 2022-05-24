package com.shadowlandsmc.channel11;

import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
  

  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerDeathEvent(PlayerDeathEvent event){
	  
	  plugin.getLogger().severe("Player " + event.getEntity().getDisplayName() + " died at: " + event.getEntity().getLocation().toString());      
	  plugin.getLogger().severe(event.getDeathMessage());
	  
	  //If the message has already been set by another plugin, ignore
	  if(event.getDeathMessage() == null || event.getDeathMessage().isEmpty()) {
		  return;
	  }

	  
	  //remove the message so other plugins don't use it
	  event.setDeathMessage(null);

    if(event.getEntity().getKiller() != null) {
    	
        //killed by custom item
        if(event.getEntity().getKiller().getInventory().getItemInMainHand() != null 
        		&& event.getEntity().getKiller().getInventory().getItemInMainHand().hasItemMeta()
        		&& event.getEntity().getKiller().getInventory().getItemInMainHand().getItemMeta().getDisplayName() != "") {

        	killedByCustom(event.getEntity().getDisplayName(), event.getEntity().getKiller().getName(), event.getEntity()
                    .getKiller().getInventory().getItemInMainHand().getItemMeta().getDisplayName());
          return;    
          //killed by entity, no custom
        } else {
          if(event.getEntity().getKiller().getName() != null) {

        	  killedByEntityNoCustom(event.getEntity().getDisplayName(), event.getEntity().getKiller().getName());
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
    	    	     
    	    	  mobWithCustomName(event.getEntity().getDisplayName(), entityDamageCause.getDamager().getCustomName());
        	      return;
    	      } else {
    	     
    	    	  //an arrow is an instance of a mob without name
    	    	  
    	    	  if(entityDamageCause.getDamager() instanceof Projectile) {
    	    		  
    	    		  Projectile projectile = (Projectile) entityDamageCause.getDamager();
    	    		  
    	    		  if(projectile.getShooter() != null) {
    	    			  
    	    			  if(projectile.getShooter() instanceof Player) {
    	    				  
    	    				  Player killer = (Player) projectile.getShooter();
    	    				  
    	    			        //killed by custom item. Check if it is in main hand
    	    			        if(killer.getInventory().getItemInMainHand() != null 
    	    			        		&& (killer.getInventory().getItemInMainHand().getType() == Material.BOW || killer.getInventory().getItemInMainHand().getType() == Material.CROSSBOW)
    	    			        		&& killer.getInventory().getItemInMainHand().hasItemMeta()
    	    			        		&& killer.getInventory().getItemInMainHand().getItemMeta().getDisplayName() != "") {
    	    			          killedByCustom(event.getEntity().getDisplayName(), killer.getDisplayName(), killer.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
    	    			          return;
    	    			          
    	    			          //killed by entity, no custom
    	    			        } else  if(killer.getInventory().getItemInOffHand() != null 
    	    			        		&& (killer.getInventory().getItemInOffHand().getType() == Material.BOW || killer.getInventory().getItemInOffHand().getType() == Material.CROSSBOW)
    	    			        		&& killer.getInventory().getItemInOffHand().hasItemMeta()
    	    			        		&& killer.getInventory().getItemInOffHand().getItemMeta().getDisplayName() != "") {
      	    			          killedByCustom(event.getEntity().getDisplayName(), killer.getDisplayName(), killer.getInventory().getItemInOffHand().getItemMeta().getDisplayName());
      	    			          return;	
    	    			        	
    	    			        }
    	    			        	else {
    	    			        
    	    			          if(event.getEntity().getKiller().getDisplayName() != null) {
    	    			        	  killedByEntityNoCustom(event.getEntity().getDisplayName(), killer.getDisplayName());
    	    			            return;
    	    			            
    	    			          }
    	    			        }
    	    				  
    	    			  } else {
    	    				  
    	    				  if(projectile.getShooter() instanceof Entity) {
    	    					  Entity killer = (Entity) projectile.getShooter();
    	    					  
    	    					  mobWithoutName(event.getEntity().getDisplayName(), killer.getType().toString().toLowerCase().replaceAll("_", " "));
    	    					  return;
    	    					  
    	    				  }
    	    				  
    	    			  }
    	    			  
    	    			  
    	    			  
    	    		  } else {
    	    			  
    	    			  //if we don't know who shot the arrow
    	    			  mobWithoutName(event.getEntity().getDisplayName(), "unknown projectile");
    	    	    	      return;  
    	    			  
    	    		  }    		  
    	    	  } else {  	    	
    	    		  mobWithoutName(event.getEntity().getDisplayName(), entityDamageCause.getDamager().getType().toString().toLowerCase().replaceAll("_", " "));
    	      return;
    	    	  }
    	      }
    	    }
    	  }   
    	  
    	  //not killed by entity
    	  noKiller(event.getEntity().getDisplayName());
    	    
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
  
  public void noKiller(String player) {
	  
	  //not killed by entity
	        String msg = plugin.getConfig().getString("msgNoKiller");
	        //The way we are replacing color codes is bad practice, and may not work in future updates
	        plugin.getServer().broadcastMessage(msg.replaceAll("&", "§").replaceAll("%announcer%", getAnnoucer()).
	            replaceAll("%player%", player));
  }
  
  public void mobWithoutName(String player, String killer) {
      String msg = plugin.getConfig().getString("msgMobWithoutName");
      //The way we are replacing color codes is bad practice, and may not work in future updates
      plugin.getServer().broadcastMessage(msg.replaceAll("&", "§").replaceAll("%announcer%", getAnnoucer()).
          replaceAll("%player%", player).replaceAll("%killer%", killer));
  }
  
  public void mobWithCustomName(String player, String killer) {
      String msg = plugin.getConfig().getString("msgMobWithName");
      //The way we are replacing color codes is bad practice, and may not work in future updates
      plugin.getServer().broadcastMessage(msg.replaceAll("&", "§").replaceAll("%announcer%", getAnnoucer()).
          replaceAll("%player%", player).replaceAll("%killer%", killer));
  }
  
  public void killedByCustom(String player, String killer, String item) {
      String msg = plugin.getConfig().getString("msgKilledByCustom");
      //The way we are replacing color codes is bad practice, and may not work in future updates
      plugin.getServer().broadcastMessage(msg.replaceAll("&", "§").replaceAll("%announcer%", getAnnoucer()).
          replaceAll("%player%", player).replaceAll("%item%", item).replaceAll("%killer%", 
                  killer));
  }
  
  public void killedByEntityNoCustom(String player, String killer) {
      String msg = plugin.getConfig().getString("msg");
      //The way we are replacing color codes is bad practice, and may not work in future updates
      plugin.getServer().broadcastMessage(msg.replaceAll("&", "§").replaceAll("%announcer%", getAnnoucer()).
          replaceAll("%player%", player).replaceAll("%killer%", killer));
  }
  
}
