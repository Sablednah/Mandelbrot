package me.sablednah.Mandelbrot;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class MandelbrotCommandExecutor implements CommandExecutor {
	public Mandelbrot plugin;

	public MandelbrotCommandExecutor(Mandelbrot instance) {
		this.plugin=instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("mandelbrot")){

			int plotWidth, plotHeight;

			PluginDescriptionFile pdfFile = plugin.getDescription();
			String myName=pdfFile.getName();

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "["+myName+"] Not available from console!");
				return true;
			}
			
			//if (args.length > 0 && args[0].toLowerCase().equals("toggle")) {			

			if (args.length < 2) {
				return false;	
			}

			try { 
				plotWidth = Integer.parseInt (args[0]);
				plotHeight= Integer.parseInt (args[1]);
			} catch (NumberFormatException e) { 
				sender.sendMessage(ChatColor.RED + "["+myName+"] Numbers please.");
				return true;
			}

			
			Player playa=(Player) sender;
			
			//now to make sure they are looking at a block
			Block blockStart = playa.getTargetBlock(null, 200);
			
			if (blockStart.equals(Material.AIR)) {
				sender.sendMessage(ChatColor.RED + "["+myName+"] Look at bottom left first...");
				return true;				
			}
			
			int startX, startY, startZ;
			int changeX = 0, changeY = 0, changeZ = 0;
			
			String playerOrdinal=ordinal(playa);
			
			Location locStart = blockStart.getLocation();
			World w = locStart.getWorld();
			
			startX=locStart.getBlockX();
			startY=locStart.getBlockY();
			startZ=locStart.getBlockZ();
			
			//ok time to plot the brot!
			double x0, y0, x, y, xScale, yScale, xtemp;
			int iteration, max_iteration, colour;

			xScale=plotWidth/3.5;
			yScale=plotHeight/2;

			for(int mX=0; mX<plotWidth; mX++) {
				for(int mY=0; mY<plotHeight; mY++) {
					x0=(mX/xScale)-2.5;
					y0=(mY/yScale)-1;

					x = 0;
					y = 0;

					iteration = 0;
					max_iteration = 150;

					while ( x*x + y*y < 4  &&  iteration < max_iteration )  {
						xtemp = x*x - y*y + x0;
						y = 2*x*y + y0;

						x = xtemp;

						++iteration;
					}					

					

					//plot(mX,My,color);
					//now the hard bit - lol - time to change the blocks!  remember y=altitude!!
					
					if (playerOrdinal=="East") { 
						changeX=startX+mX;
						changeZ=startZ;						
					} else if (playerOrdinal=="West") { 
						changeX=startX-mX;
						changeZ=startZ;						
					} else if (playerOrdinal=="South") { 
						changeX=startX;
						changeZ=startZ+mX;
					} else if (playerOrdinal=="North") { 
						changeX=startX;
						changeZ=startZ-mX;
					}
					changeY=startY+mY;
					
					if(iteration<max_iteration) {
						colour = (iteration % 15);
					} else {
						colour = 15;
					}
					
					Block blockToChange = w.getBlockAt(changeX,changeY,changeZ); // get the block with the current coordinates
					blockToChange.setType(Material.WOOL);
					blockToChange.setData((byte) colour);
				}

			}


			return true;
		}
		return false; 
	}
	
	public String ordinal(Player player) {
	    double rot = (player.getLocation().getYaw() - 90) % 360;
	        if (rot < 0) {
	                rot += 360.0;
	        }
	        return getDirection(rot);
	}

	private static String getDirection(double rot) {
	        if (0 <= rot && rot < 45) {
	                return "North";
	        } else if (45 <= rot && rot < 135) {
	                return "East";
	        } else if (135 <= rot && rot < 225) {
	                return "South";
	        } else if (225 <= rot && rot < 315) {
	                return "West";
	        } else if (315 <= rot && rot < 360) {
	                return "North";
	        } else {
	                return null;
	        }
	}
	
}
