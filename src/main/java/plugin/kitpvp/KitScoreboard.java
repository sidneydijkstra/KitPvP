package plugin.kitpvp;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import plugin.database.ScoreTable;
import plugin.database.entities.ScoreStat;

/**
 * The KitScoreboard class for Player Scoreboard handling
 */
public class KitScoreboard {
	
	/**
	 * The KitPvP variable
	 */
	private KitPvP _plugin;
	
	/**
	 * The HashMap<String, Scoreboard> variable that stores all the scores
	 */
	private HashMap<String, Scoreboard> _scoreboards;
	
	/**
	 * <p>The KitScoreboard constructor</p>	 
	 * @param the KitPvP class
	 * @return instance of class
	 * @since 1.0
	 */
	public KitScoreboard(KitPvP _plugin){
		this._plugin = _plugin;
		this._scoreboards = new HashMap<String, Scoreboard>();
	}
	
	/**
	 * <p>Update the Scoreboard of a Player</p>	 
	 * @param the Player
	 * @since 1.0
	 */
	public void updateScoreboard(Player _player) {
		ScoreStat stat = ScoreTable.getScore(_player.getName());
		
		if(stat == null || _player == null)
			return;
		
		ArrayList<ScoreStat> _ranking = ScoreTable.getTopScore(5);

		ScoreboardBuilder builder = new ScoreboardBuilder(this._plugin, 32, "b-" + _player.getName(), ChatColor.RED + "KitPvP", DisplaySlot.SIDEBAR);
		
		builder.addLine('-', ChatColor.RED)
		       .addRow(ChatColor.WHITE + "-=Stats=-")
		       .addRow(ChatColor.GREEN + String.format("Kills: %s", stat.getKills()))
			   .addRow(ChatColor.GREEN + String.format("Deaths: %s", stat.getDeaths()))
		   	   .addRow(ChatColor.GREEN + String.format("KD: %s", stat.getKD()))
		   	   .addLine('-', ChatColor.RED)
		       .addRow(ChatColor.WHITE + "-=Ranking=-");
		
		for(int i = 0; i < _ranking.size(); i++) {
			ChatColor color = i == 0 ? ChatColor.GOLD : i == 1 ? ChatColor.GRAY : ChatColor.GREEN;
			ScoreStat rankStat = _ranking.get(i);
			builder.addRow(color + String.format("%s. %s | %s | %s", i + 1, rankStat.getName(), rankStat.getKills(), rankStat.getKD()));
		}
		
		if(_scoreboards.containsKey(_player.getName())) {
			_scoreboards.replace(_player.getName(), builder.build());
		}else {
			_scoreboards.put(_player.getName(), builder.build());
		}
		
		_player.setScoreboard(_scoreboards.get(_player.getName()));
	}
	
}
