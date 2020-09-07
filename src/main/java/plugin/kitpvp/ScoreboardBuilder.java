package plugin.kitpvp;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;


/**
 * The ScoreboardBuilder class for building Player Scoreboard's
 */
public class ScoreboardBuilder {

	/**
	 * The KitPvP variable
	 */
	private KitPvP _plugin;

	/**
	 * The Scoreboard variable
	 */
	private Scoreboard _scoreboard;
	
	/**
	 * The Scoreboard Objective variable
	 */
	private Objective _objective;
	
	/**
	 * The row size
	 */
	private int _rowSize;
	
	/**
	 * The Scoreboard name
	 */
	private String _name;

	/**
	 * The list of all score's
	 */
	private ArrayList<Score> _scores;
	
	/**
	 * <p>The KitScoreboard constructor</p>	 
	 * @param the KitPvP class
	 * @return instance of class
	 * @since 1.0
	 */
	public ScoreboardBuilder(KitPvP _plugin, int _rowSize, String _name, String _title, DisplaySlot _displaySlot) {
		this._plugin = _plugin;
		this._rowSize = _rowSize;
		this._name = _name;
		
		this._scores = new ArrayList<Score>();
		
		_scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		_objective = _scoreboard.registerNewObjective(this._name, "criteria", _title);
		
		_objective.setDisplaySlot(_displaySlot);
	}
	
	/**
	 * <p>Add a row to the Scoreboard</p>	 
	 * @param the message
	 * @return the ScoreboardBuilder
	 * @since 1.0
	 */
	public ScoreboardBuilder addRow(String _message) {
		
		if(_message.length() > _rowSize) {
			_message = "[X]";
		}else {
			_message = this.fillString(_message);
		}
		
		Score score =  _objective.getScore(_message);
		this._scores.add(score);
		return this;
	}

	/**
	 * <p>Add a line to the Scoreboard</p>	 
	 * @param the line char
	 * @param the line color
	 * @return the ScoreboardBuilder
	 * @since 1.0
	 */
	public ScoreboardBuilder addLine(char _line, ChatColor _color) {
		String _message = "";
		for(int i = 0; i < _rowSize; i++) {
			_message += _line;
		}
		
		Score score =  _objective.getScore(_color + _message);
		this._scores.add(score);
		return this;
	}

	/**
	 * <p>Build the Scoreboard</p>	 
	 * @return the Scoreboard
	 * @since 1.0
	 */
	public Scoreboard build() {
		this.updateScoresOrder();
		return _scoreboard;
	}

	/**
	 * <p>Update the order of the Scoreboard</p>	 
	 * @return the Scoreboard
	 * @since 1.0
	 */
	private void updateScoresOrder() {
		for(int i = 1; i <= _scores.size(); i++) {
			_scores.get(i-1).setScore(_scores.size() - i);
		}
	}
	
	/**
	 * <p>Fill a string to the max row size</p>	 
	 * @param the message
	 * @return the filed message
	 * @since 1.0
	 */
	private String fillString(String _message) {
		int count = _rowSize - _message.length();
		for(int i = 0; i < count; i++)
			_message += " ";
		return _message;
	}
	
}
