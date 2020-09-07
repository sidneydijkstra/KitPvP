package plugin.kitpvp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;


/**
 * The KitController class for handling kit creation and loading
 */
public class KitController {
	
	/**
	 * The KitPvP variable
	 */
	private KitPvP _plugin;

	/**
	 * The FileConfiguration variable
	 */
	private FileConfiguration _config;
	
	/**
	 * <p>The KitController constructor</p>	 
	 * @param the KitPvP class
	 * @return instance of class
	 * @since 1.0
	 */
	public KitController(KitPvP _plugin) {
		this._plugin = _plugin;
		this._config = _plugin.getConfig();
	}
	

	/**
	 * <p>Create a kit and add it to the config file</p>	 
	 * @param the Player
	 * @param the kit name
	 * @param the kit icon name
	 * @param the kit description
	 * @param the kit display slot
	 * @since 1.0
	 */
	public void createKit(Player _player, String _kitName, String _iconItemName, String _description, int _slot) {
		
        if (_config.getConfigurationSection("kits." + _kitName) != null) {
            _player.sendMessage(_kitName + " already exists!");
            return;
        }
        
        String path = "kits." + _kitName + ".";
        _config.createSection("kits." + _kitName);

        _config.set(path + ".icon", _iconItemName);
        _config.set(path + ".description", _description);
        _config.set(path + ".slot", _slot);
        
        PlayerInventory inventory = _player.getInventory();
		
        for (int i = 0; i < 36; i++) {
            ItemStack item = inventory.getItem(i);
 
            if (item == null || item.getType() == Material.AIR)
                continue;

            String slot = path + "items." + i;
            
            if(item.getType() == Material.POTION || item.getType() == Material.SPLASH_POTION || item.getType() == Material.LINGERING_POTION) {
            	this.createPotion(slot, item);
            	continue;
            }
 
            _config.set(slot + ".type", item.getType().toString().toLowerCase());
            _config.set(slot + ".amount", item.getAmount());
 
            if (!item.hasItemMeta())
                continue;
 
            if (item.getItemMeta().hasDisplayName())
                _config.set(slot + ".name", item.getItemMeta().getDisplayName());
 
            if (item.getItemMeta().hasLore())
                _config.set(slot + ".lore", item.getItemMeta().getLore());
            
            if (item.getItemMeta().hasEnchants()) {
                Map<Enchantment, Integer> enchants = item.getEnchantments();
                ArrayList<String> enchantList = new ArrayList<String>();
                for (Enchantment e : item.getEnchantments().keySet()) {
                    int level = enchants.get(e);
                    enchantList.add(e.getKey().toString() + ":" + level);
                }
                _config.set(slot + ".enchants", enchantList);
            }
        }
        
       this.createArmor(path, ".helmet", inventory.getHelmet());
       this.createArmor(path, ".chestplate", inventory.getChestplate());
       this.createArmor(path, ".leggings", inventory.getLeggings());
       this.createArmor(path, ".boots", inventory.getBoots());
       
        _plugin.saveConfig();
        _plugin.getKitGUI().initializeInventory();
	}
	

	/**
	 * <p>Get a kit and add give it to a Player</p>	 
	 * @param the Player
	 * @param the kit name
	 * @since 1.0
	 */
	public void getKit(Player _player, String _kitName) {
        
        if (_config.getConfigurationSection("kits." + _kitName) == null) {
        	_player.sendMessage(_kitName + " does not exist!");
            return;
        }
 
        _player.getInventory().clear();
        
        String path = "kits." + _kitName + ".";
        
        ConfigurationSection section = _config.getConfigurationSection(path + "items");
        
        for (String str : section.getKeys(false)) {
            int slot = Integer.parseInt(str);
            if (0 > slot && slot > 36)
                return;
 
            String string = path + "items." + slot + ".";
            String type = _config.getString(string + "type");
            
            if(type.equals(Material.POTION.toString().toLowerCase()) || type.equals(Material.SPLASH_POTION.toString().toLowerCase()) || type.equals(Material.LINGERING_POTION.toString().toLowerCase())) {
            	ItemStack potion = this.getPotion(string);
                _player.getInventory().setItem(slot, potion);
                _player.sendMessage(potion.toString());
            	continue;
        	}
            
            String name = _config.getString(string + "name");
            List<String> lore = _config.getStringList(string + "lore");
            List<String> enchants = _config.getStringList(string + "enchants");
            int amount = _config.getInt(string + "amount");
 
            ItemStack item = new ItemStack(Material.matchMaterial(type.toUpperCase()), amount);
            ItemMeta meta = item.getItemMeta();
 
            if (meta == null)
                continue;
 
            if (name != null)
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
 
            if (lore != null)
                meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lore.toString())));
 
            if (enchants != null) {
                for (String s1 : enchants) {
                    String[] splitEnchants = s1.split(":");
                    meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft(splitEnchants[1])), Integer.parseInt(splitEnchants[2]), true);
                }
            }
 
            item.setItemMeta(meta);
            _player.getInventory().setItem(slot, item);
        }
 
        _player.getInventory().setHelmet(this.getArmor(path, ".helmet"));
        _player.getInventory().setChestplate(this.getArmor(path, ".chestplate"));
        _player.getInventory().setLeggings(this.getArmor(path, ".leggings"));
        _player.getInventory().setBoots(this.getArmor(path, ".boots"));
 
        _player.updateInventory();
	}
	
	/**
	 * <p>Add a potion to the config</p>	 
	 * @param the config path
	 * @param the item
	 * @since 1.0
	 */
	public void createPotion(String _path, ItemStack _item) {
		
		if(_item.getType() != Material.POTION && _item.getType() != Material.SPLASH_POTION && _item.getType() != Material.LINGERING_POTION) 
        	return;

        _config.set(_path + ".type", _item.getType().toString().toLowerCase());
        
		PotionMeta meta = (PotionMeta)_item.getItemMeta();
		PotionData data = meta.getBasePotionData();
        _config.set(_path + ".potion_type", data.getType().toString().toLowerCase());
	}

	/**
	 * <p>Add armor to the config</p>	 
	 * @param the config path
	 * @param the armor type
	 * @param the item
	 * @since 1.0
	 */
	public void createArmor(String _path, String _type, ItemStack _item) {

        String slot = _path + ".armor" + _type;
		if (_item == null || _item.getType() == Material.AIR)
            return;

        _config.set(slot + ".type", _item.getType().toString().toLowerCase());
        _config.set(slot + ".amount", _item.getAmount());

        if (!_item.hasItemMeta())
            return;

        if (_item.getItemMeta().hasDisplayName())
            _config.set(slot + ".name", _item.getItemMeta().getDisplayName());

        if (_item.getItemMeta().hasLore())
            _config.set(slot + ".lore", _item.getItemMeta().getLore());

        if (_item.getItemMeta().hasEnchants()) {
            Map<Enchantment, Integer> enchants = _item.getEnchantments();
            ArrayList<String> enchantList = new ArrayList<String>();
            for (Enchantment e : _item.getEnchantments().keySet()) {
                int level = enchants.get(e);
                enchantList.add(e.getKey().toString() + ":" + level);
            }
            _config.set(slot + ".enchants", enchantList);
        }
	}

	/**
	 * <p>Get a potion from the config</p>	 
	 * @param the config path
	 * @return the potion
	 * @since 1.0
	 */
	public ItemStack getPotion(String _path) {
        String type = _config.getString(_path + ".type");
        String potionType = _config.getString(_path + ".potion_type");
        
		ItemStack potion = new ItemStack(Material.matchMaterial(type.toUpperCase()), 1);
		PotionMeta meta = (PotionMeta)potion.getItemMeta();
		PotionData data = new PotionData(PotionType.valueOf(potionType.toUpperCase()));
        meta.setBasePotionData(data);
        potion.setItemMeta(meta);
		
		return potion;
	}
	

	/**
	 * <p>Get armor from the config</p>	 
	 * @param the config path
	 * @param the armor type
	 * @return the armor
	 * @since 1.0
	 */
	public ItemStack getArmor(String _path, String _type) {
		
		String string = _path + ".armor" + _type;
		if(!_config.isSet(string))
			return new ItemStack(Material.AIR);
		
        String type = _config.getString(string + ".type");
        String name = _config.getString(string + ".name");
        List<String> lore = _config.getStringList(string + ".lore");
        List<String> enchants = _config.getStringList(string + ".enchants");
		
		ItemStack item = new ItemStack(Material.matchMaterial(type.toUpperCase()), 1);
        ItemMeta meta = item.getItemMeta();

        if (meta == null)
            return item;

        if (name != null)
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        if (lore != null)
            meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lore.toString())));

        if (enchants != null) {
            for (String s1 : enchants) {
                String[] splitEnchants = s1.split(":");
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft(splitEnchants[1])), Integer.parseInt(splitEnchants[2]), true);
            }
        }

        item.setItemMeta(meta);
		return item;
	}
}
