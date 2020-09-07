package plugin.kitpvp;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * The SignListener class for handling sing event logic
 */
public class SignListener implements Listener {
	
	/**
	 * The KitController variable
	 */
	private KitPvP _plugin;
	
	/**
	 * The KitGUI variable
	 */
	private KitGUI _kitGUI;
	
	/**
	 * <p>The SignListener constructor</p>	 
	 * @param the KitPvP class
	 * @return instance of class
	 * @since 1.0
	 */
	public SignListener(KitPvP _plugin) {
		this._plugin = _plugin;
		this._kitGUI = _plugin.getKitGUI();
	}
	
	/**
	 * <p>The onSignChange event for handling sing placement/editing</p>	 
	 * @param the SignChangeEvent
	 * @since 1.0
	 */
	@EventHandler
	public void onSignChange(SignChangeEvent e) {
	    //if (e.getPlayer().hasPermission("kitpvp.createsign")) {
			e.getPlayer().sendMessage(e.getLine(0));
	    	if(e.getLine(0).equals("[KITPVP]")) {
		        for (int i = 0; i < 4; i++) {
		            String line = e.getLine(i);
		            if (line != null && !line.equals("")) {
		                e.setLine(i, ChatColor.RED + line);
		            }
		        }
	    	}
	    //}
	}
	
	/**
	 * <p>The onSignClick event for handling sing clicking</p>	 
	 * @param the PlayerInteractEvent
	 * @since 1.0
	 */
	@EventHandler
	public void onSignClick(PlayerInteractEvent e) {
		
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
	        return;
	    }
		
		Player player = e.getPlayer();
		Block block = e.getClickedBlock();
        if (block.getType() == Material.SIGN || block.getType() == Material.WALL_SIGN) {
        	
        	Sign sign = (Sign) block.getState();
            if(ChatColor.stripColor(sign.getLine(0)).equals("[KITPVP]") && ChatColor.stripColor(sign.getLine(1)).equalsIgnoreCase("join")) {
            	_plugin.join(player);
            }else if(ChatColor.stripColor(sign.getLine(0)).equals("[KITPVP]") && ChatColor.stripColor(sign.getLine(1)).equalsIgnoreCase("kit")) {
            	_kitGUI.openInventory(player);
            }else if(ChatColor.stripColor(sign.getLine(0)).equals("[KITPVP]") && ChatColor.stripColor(sign.getLine(1)).equalsIgnoreCase("leave")) {
            	_plugin.leave(player);
            }
         
        }
		
	}
	
}	
