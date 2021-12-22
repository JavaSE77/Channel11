package com.shadowlandsmc.channel11;


import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

 public static Main plugin;
  


  // Called when the plugin is enabled. It is used to set up variables and to register things such as commands.
  @Override
  public void onEnable() {
    plugin = getPlugin(Main.class);
    PluginManager pluginManager = getServer().getPluginManager();
    
      getConfig().options().copyDefaults(true); 
      saveDefaultConfig();
      

    
    /*
     * Register a command to the list of usable commands. If you don't register the
     * command, it won't work! Also if you change the command name, make sure to
     * also change in the plugin.yml file.
     */

    //pluginManager.registerEvents(new commandProcessor(), this);
    /*
     * Make sure you register your listeners if you have any! If you have a class
     * that implements Listener, you need to make sure to register it. Otherwise it
     * will DO NOTHING!
     */
    pluginManager.registerEvents(new PlayerDeathListener(), this);

    /*
     * This line lets you send out information to the console. In this case it would
     * say: Yay, Template-Plugin is now enabled!
     */
    this.getLogger().info("Channel 11 news, created by JavaSE");
  }

}
