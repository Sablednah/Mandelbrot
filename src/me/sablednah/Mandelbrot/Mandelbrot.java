/**
 * @author	sable <darren.douglas@gmail.com>
 * @version	0.1
 * 
 */
package me.sablednah.Mandelbrot;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Mandelbrot extends JavaPlugin {

	public static Mandelbrot plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	private MandelbrotCommandExecutor cmdMandelbrot;
	
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " : --- END OF LINE ---");
		
	}
	@Override
	public void onEnable() {
//		PluginManager pm = getServer().getPluginManager();

		cmdMandelbrot = new MandelbrotCommandExecutor(this);
		getCommand("Mandelbrot").setExecutor(cmdMandelbrot);
		
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " version " + pdfFile.getVersion()+" online.");
	}
	
}
