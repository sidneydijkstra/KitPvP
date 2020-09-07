package plugin.kitpvp;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


/**
 * The KitGUI class for handling the kit select GUI
 */
public class KitGUI implements Listener {

	/**
	 * The KitPvP variable
	 */
	private KitPvP _plugin; 
	
	/**
	 * The Inventory variable
	 */
	private Inventory _inventory;
	
	/**
	 * The FileConfiguration variable
	 */
	private FileConfiguration _config;
	

	/**
	 * <p>The EventListener constructor</p>	 
	 * @param the KitPvP class
	 * @return instance of class
	 * @since 1.0
	 */
	public KitGUI(KitPvP _plugin) {
		this._plugin = _plugin;
		_inventory = Bukkit.createInventory(null, 9, "Select Kit");
		this._config = _plugin.getConfig();
		
		initializeInventory();
    }


	/**
	 * <p>Initialize kit GUI Inventory</p>	 
	 * @since 1.0
	 */
	public void initializeInventory() {
		_inventory.clear();
        ConfigurationSection section = _config.getConfigurationSection("kits");
        
        if(section == null)
        	return;
        
        for (String str : section.getKeys(false)) {
            String icon = _config.getString("kits." + str + ".icon");
            String desc = _config.getString("kits." + str + ".description");
            int slot = _config.getInt("kits." + str + ".slot");
            Material mat = Material.matchMaterial(icon.toUpperCase());
        	_inventory.setItem(slot, createGuiItem(mat, str, desc));
        }
        
	}
	

	/**
	 * <p>Create a new GUI item</p>	 
	 * @param the Material of the item
	 * @param the Name of the item
	 * @param the array of item lore
	 * @return the ItemStack
	 * @since 1.0
	 */
	private ItemStack createGuiItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);

        return item;
    }
	
	/**
	 * <p>Open the Inventory on a Player</p>	 
	 * @param the Material of the item
	 * @since 1.0
	 */
	public void openInventory(Player e) {
        e.openInventory(_inventory);
    }
	

	/**
	 * <p>The onInventoryClick event for negating item moving and checking item clicked</p>	 
	 * @param the InventoryClickEvent
	 * @since 1.0
	 */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!e.getInventory().equals(_inventory)) 
        	return;

        e.setCancelled(true);

        ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) 
        	return;

        Player p = (Player)e.getWhoClicked();
        
        _plugin.getKitController().getKit(p, clickedItem.getItemMeta().getDisplayName());
        p.closeInventory();
    }


	/**
	 * <p>The onInventoryClick event for negating item dragging</p>	 
	 * @param the InventoryDragEvent
	 * @since 1.0
	 */
    @EventHandler
    public void onInventoryClick(InventoryDragEvent e) {
        if (!e.getInventory().equals(_inventory))
          e.setCancelled(true);
    }
}
