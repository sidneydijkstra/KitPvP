
package plugin.kitpvp;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import plugin.database.*;
import plugin.database.entities.*;

/**
 * The KitPvP class handles the Plugin logic
 */
public class KitPvP extends JavaPlugin{
	
	/**
	 * The KitController variable
	 */
	private KitController _kitController;
	
	/**
	 * The KitGUI variable
	 */
	private KitGUI _kitGUI;
	
	/**
	 * The KitScoreboard variable
	 */
	private KitScoreboard _kitScoreboard;
	
	/**
	 * The SignListener variable
	 */
	private SignListener _signListener;
	
	/**
	 * The EventListener variable
	 */
	private EventListener _eventListener;
	
	/**
	 * The ArraList with all the Players that are in the lobby
	 */
	private ArrayList<Player> _playersLobby;
	
	/**
	 * The ArraList with all the Players that are in the game
	 */
	private ArrayList<Player> _playersGame;
	
	/**
	 * <p>This is the onEnable function that initializes the Plugin</p>
	 * @since 1.0
	 */
	@Override
    public void onEnable() {
		this.getLogger().info("[KITPVP] Enabled");
		Exception e = Database.initializeDatabase();
		if(e != null)
			this.getLogger().info(e.toString());

		PluginManager pm = this.getServer().getPluginManager();
		
		_kitController = new KitController(this);
		_kitGUI = new KitGUI(this);
		_kitScoreboard = new KitScoreboard(this);
		_signListener = new SignListener(this);
		_eventListener = new EventListener(this);
		
		_playersLobby = new ArrayList<Player>();
		_playersGame = new ArrayList<Player>();

		pm.registerEvents(_kitGUI, this);
		pm.registerEvents(_signListener, this);
		pm.registerEvents(_eventListener, this);
		
		this.saveDefaultConfig();
    }
	
	/**
	 * <p>Get the KitController of the Plugin</p>	 
	 * @return the KitController
	 * @since 1.0
	 */
	public KitController getKitController() {
		return _kitController;
	}
	
	/**
	 * <p>Get the KitGUI of the Plugin</p>	 
	 * @return the KitGUI
	 * @since 1.0
	 */
	public KitGUI getKitGUI() {
		return _kitGUI;
	}

	/**
	 * <p>Get the KitScoreboard of the Plugin</p>	 
	 * @return the KitScoreboard
	 * @since 1.0
	 */
	public KitScoreboard getKitScoreboard() {
		return _kitScoreboard;
	}
	
	/**
	 * <p>Get the SignListener of the Plugin</p>	 
	 * @return the SignListener
	 * @since 1.0
	 */
	public SignListener getSignListener() {
		return _signListener;
	}

	/**
	 * <p>Get all the players in the lobby</p>	 
	 * @return ArrayList<Player>
	 * @since 1.0
	 */
	public ArrayList<Player> getLobbyPlayers(){
		return this._playersLobby;
	}

	/**
	 * <p>Get all the players in the game</p>	 
	 * @return ArrayList<Player>
	 * @since 1.0
	 */
	public ArrayList<Player> getGamePlayers(){
		return this._playersGame;
	}

	/**
	 * <p>Get a player to join the lobby or if he is in the lobby let him join the game</p>	 
	 * @param the Player
	 * @since 1.0
	 */
	public void join(Player _player) {
		if(_playersLobby.contains(_player) && !_playersGame.contains(_player)) {
			_playersLobby.remove(_player);
			_playersGame.add(_player);
			
			SpawnPosition sp = GameSpawnPositionTable.retrieveRandomSpawnPositions();
			Location loc = sp.getLocation();
			loc.setWorld(_player.getWorld());
			_player.teleport(loc);
			_player.sendMessage("Goodluck my friend!");
		}else if(!_playersLobby.contains(_player)){
			_playersLobby.add(_player);
			SpawnPosition sp = LobbySpawnPositionTable.retrieveRandomSpawnPositions();
			Location loc = sp.getLocation();
			loc.setWorld(_player.getWorld());
			_player.teleport(loc);
			_player.sendMessage("Welkom to KitPvP!");
			
			ScoreStat score = ScoreTable.getScore(_player.getName());
			if(score == null)
				score = ScoreTable.createScore(_player.getName(), 0, 0);
			
			this.updateScoreboards();
		}
	}

	/**
	 * <p>Get a Player to leave the lobby/game and remove its Scoreboard</p>	 
	 * @param the Player
	 * @since 1.0
	 */
	public void leave(Player _player) {
		if(_playersLobby.contains(_player)) {
			_playersLobby.remove(_player);
		}
		
		if(_playersGame.contains(_player)) {
			_playersGame.remove(_player);
		}
		
		_player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		this.updateScoreboards();
	}
	
	/**
	 * <p>Put the Player back in the lobby list</p>	 
	 * @param the Player
	 * @since 1.0
	 */
	public void kill(Player _player) {
		if(_playersGame.contains(_player)) {
			_playersGame.remove(_player);
			_playersLobby.add(_player);
		}
	}
	
	/**
	 * <p>Get all the players in the lobby and game</p>	 
	 * @return ArrayList<Player>
	 * @since 1.0
	 */
	public ArrayList<Player> getAllPlayers(){
		ArrayList<Player> list = new ArrayList<Player>();
		list.addAll(_playersLobby);
		list.addAll(_playersGame);
		return list;
	}

	/**
	 * <p>Update the Scoreboard of all players</p>	
	 * @since 1.0
	 */
	public void updateScoreboards() {
		ArrayList<Player> players = this.getAllPlayers();
		for(Player p : players) {
			_kitScoreboard.updateScoreboard(p);
		}
	}

	/**
	 * <p>The onDisable function that stops the Plugin</p>	
	 * @since 1.0
	 */
    @Override
    public void onDisable() {
		this.getLogger().info("[KITPVP] Disabled");
    }

	/**
	 * <p>Get a command send by a Player</p>	
	 * @param the sender of the command
	 * @param the command
	 * @param the command label
	 * @param the arguments
	 * @return boolean
	 * @since 1.0
	 */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	
    	if (cmd.getName().equalsIgnoreCase("createkit") && args.length > 1) {
    		if (!(sender instanceof Player)) {
    			sender.sendMessage("This command can only be run by a player.");
    		} else {
    			Player player = (Player)sender;
    			try {
    				_kitController.createKit(player, args[0], args[1], "", Integer.parseInt(args[2]));
    			}catch(Exception e) {}
        		return true;
    		}
    	}
    	else if (cmd.getName().equalsIgnoreCase("getkit") && args.length > 0) {
    		if (!(sender instanceof Player)) {
    			sender.sendMessage("This command can only be run by a player.");
    		} else {
    			Player player = (Player)sender;
    			_kitController.getKit(player, args[0]);
        		return true;
    		}
    	}
    	
    	else if (cmd.getName().equalsIgnoreCase("setlobbyspawn")) {
    		if (!(sender instanceof Player)) {
    			sender.sendMessage("This command can only be run by a player.");
    		} else {
    			Player player = (Player)sender;
    			LobbySpawnPositionTable.createSpawnPosition(player.getLocation());
        		return true;
    		}
    	}else if (cmd.getName().equalsIgnoreCase("setgamespawn")) {
    		if (!(sender instanceof Player)) {
    			sender.sendMessage("This command can only be run by a player.");
    		} else {
    			Player player = (Player)sender;
    			GameSpawnPositionTable.createSpawnPosition(player.getLocation());
        		return true;
    		}
    	}
    	
    	else if (cmd.getName().equalsIgnoreCase("removelobbyspawn") && args.length > 0) {
    		try{
    			int id = Integer.parseInt(args[0]);
        		LobbySpawnPositionTable.removeSpawnPosition(id);
        		sender.sendMessage(String.format("Successfully removed id %s from lobby spawn list", id));
        		return true;
    		}catch(Exception e){
    			
    		}
    	}
    	else if (cmd.getName().equalsIgnoreCase("removegamespawn") && args.length > 0) {
    		try{
    			int id = Integer.parseInt(args[0]);
        		GameSpawnPositionTable.removeSpawnPosition(id);
        		sender.sendMessage(String.format("Successfully removed id %s from game spawn list", id));
        		return true;
    		}catch(Exception e){
    			
    		}
    	}
    	
    	
    	else if (cmd.getName().equalsIgnoreCase("listlobbyspawn")) {
    		ArrayList<SpawnPosition> list = LobbySpawnPositionTable.retrieveSpawnPositions();
    		String message = "Lobby Spawn List:\n";
    		for (SpawnPosition pos : list) {
    			message += String.format("[%s] x: %s y: %s z: %s\n", pos.getId(), pos.getLocation().getX(), pos.getLocation().getY(), pos.getLocation().getZ());
    		}
    		sender.sendMessage(message);
    	}else if (cmd.getName().equalsIgnoreCase("listgamespawn")) {
    		ArrayList<SpawnPosition> list = GameSpawnPositionTable.retrieveSpawnPositions();
    		String message = "Game Spawn List:\n";
    		for (SpawnPosition pos : list) {
    			message += String.format("[%s] x: %s y: %s z: %s\n", pos.getId(), pos.getLocation().getX(), pos.getLocation().getY(), pos.getLocation().getZ());
    		}
    		sender.sendMessage(message);
    	}
    	
    	return false;
    }
}
