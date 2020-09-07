package plugin.kitpvp;

import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import plugin.database.LobbySpawnPositionTable;
import plugin.database.ScoreTable;
import plugin.database.entities.SpawnPosition;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;


/**
 * The EventListener class for listening to basic Plugin logic events
 */
public class EventListener implements Listener {


	/**
	 * The KitPvP variable
	 */
	private KitPvP _plugin;
	

	/**
	 * <p>The EventListener constructor</p>	 
	 * @param the KitPvP class
	 * @return instance of class
	 * @since 1.0
	 */
	public EventListener(KitPvP _plugin) {
		this._plugin = _plugin;
	}
	

	/**
	 * <p>The onEntityDamagedByEntity event for negating damage in the lobby and checking player kill in game</p>	 
	 * @param the onEntityDamagedByEntity
	 * @since 1.0
	 */
	@EventHandler
    public void onEntityDamagedByEntity(EntityDamageByEntityEvent  e) {
		if(e.getDamager() instanceof Player && e.getEntityType() == EntityType.PLAYER) {
			if(_plugin.getGamePlayers().contains((Player)e.getDamager()) && e.getEntity().isDead()) {
				ScoreTable.addKill(e.getDamager().getName());
				_plugin.updateScoreboards();
			}else if(_plugin.getLobbyPlayers().contains((Player)e.getDamager())) {
				e.setCancelled(true);
			}
		}
    }

	/**
	 * <p>The onDeath event for checking player kill in game</p>	 
	 * @param the PlayerDeathEvent
	 * @since 1.0
	 */
	@EventHandler
    public void onDeath(PlayerDeathEvent e) {
		if(_plugin.getGamePlayers().contains(e.getEntity())){
			if(e.getEntity().getKiller() instanceof Player) {
				ScoreTable.addKill(e.getEntity().getKiller().getName());
				_plugin.updateScoreboards();
			}
		}
    }

	/**
	 * <p>The onPlayerDeath event for removing drops and adding death score</p>	 
	 * @param the PlayerDeathEvent
	 * @since 1.0
	 */
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player player = e.getEntity();
		if(_plugin.getGamePlayers().contains(player)) {
			ScoreTable.addDeath(player.getName());
			_plugin.updateScoreboards();
			e.getDrops().clear();
			player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(Material.ARROW, new Random().nextInt(8) + 1));
			player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(Material.BAKED_POTATO, new Random().nextInt(2) + 1));
		}
	}

	/**
	 * <p>The PlayerRespawnEvent event for teleportation on player respawn</p>	 
	 * @param the PlayerRespawnEvent
	 * @since 1.0
	 */
	@EventHandler
	public void PlayerRespawnEvent(PlayerRespawnEvent e) {
		Player player = e.getPlayer();
		if(_plugin.getGamePlayers().contains(player)) {
			SpawnPosition sp = LobbySpawnPositionTable.retrieveRandomSpawnPositions();
			Location loc = sp.getLocation();
			loc.setWorld(player.getWorld());
			e.setRespawnLocation(loc);
			_plugin.kill(player);
		}
	}
}
