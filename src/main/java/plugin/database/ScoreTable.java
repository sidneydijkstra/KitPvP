package plugin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import plugin.database.entities.ScoreStat;


/**
 * The ScoreTable class for storing all the scores of the Player
 */
public class ScoreTable {
	
	/**
	 * <p>Create a new score in the table</p>
	 * @param the player name
	 * @param the player kill amount
	 * @param the player death amount
	 * @return the ScoreStat
	 * @since 1.0
	 */
	public static ScoreStat createScore(String _name, int _kills, int _deaths) {
		ConnectionInfo info = Database.getConnectionInfo();
		try (Connection connection = DriverManager.getConnection(info.getConnectionString(), info.getUsername(), info.getPassword())){
			
			PreparedStatement command = connection.prepareStatement("INSERT INTO Score (name, kills, deaths) VALUES (?, ?, ?)");
			
			command.setString(1, _name);
			command.setInt(2, _kills);
			command.setInt(3, _deaths);
			
			command.executeUpdate();
			
			return new ScoreStat(_name, _kills, _deaths);
		}catch(Exception e) {
			
		}
		return null;
	}

	/**
	 * <p>Get a score from the table</p>
	 * @param the player name
	 * @return the ScoreStat
	 * @since 1.0
	 */
	public static ScoreStat getScore(String _name) {
		ConnectionInfo info = Database.getConnectionInfo();
		try (Connection connection = DriverManager.getConnection(info.getConnectionString(), info.getUsername(), info.getPassword())){
			
			PreparedStatement command = connection.prepareStatement("SELECT * FROM Score WHERE name LIKE ?");
			
			command.setString(1, _name);
			
			ResultSet result = command.executeQuery();
			
			ScoreStat score = null;
			while(result.next()) {
				score = new ScoreStat(result.getString("name"), result.getInt("kills"), result.getInt("deaths"));
			}
			
			return score;
		}catch(Exception e) {
			
		}
		return null;
	}
	
	/**
	 * <p>Get the top score's from the table</p>
	 * @param the amount
	 * @return a ArrayList with ScoreStat's
	 * @since 1.0
	 */
	public static ArrayList<ScoreStat> getTopScore(int _amount) {
		ConnectionInfo info = Database.getConnectionInfo();
		try (Connection connection = DriverManager.getConnection(info.getConnectionString(), info.getUsername(), info.getPassword())){
			
			PreparedStatement command = connection.prepareStatement("SELECT * FROM Score ORDER BY kills DESC LIMIT ?");
			command.setInt(1, _amount);
			ResultSet result = command.executeQuery();
			
			ArrayList<ScoreStat> scores = new ArrayList<ScoreStat>();
			while(result.next()) {
				ScoreStat score = new ScoreStat(result.getString("name"), result.getInt("kills"), result.getInt("deaths"));
				scores.add(score);
			}
			
			return scores;
		}catch(Exception e) {
			
		}
		return new ArrayList<ScoreStat>();
	}
	

	/**
	 * <p>Add a kill to the score table</p>
	 * @param the Player name
	 * @since 1.0
	 */
	public static void addKill(String _name) {
		ConnectionInfo info = Database.getConnectionInfo();
		try (Connection connection = DriverManager.getConnection(info.getConnectionString(), info.getUsername(), info.getPassword())){
			
			PreparedStatement command = connection.prepareStatement("UPDATE Score SET kills = kills + 1 WHERE name = ?");
			
			command.setString(1, _name);
			
			command.executeUpdate();
			
		}catch(Exception e) {
			
		}
	}
	

	/**
	 * <p>Add a death to the score table</p>
	 * @param the Player name
	 * @since 1.0
	 */
	public static void addDeath(String _name) {
		ConnectionInfo info = Database.getConnectionInfo();
		try (Connection connection = DriverManager.getConnection(info.getConnectionString(), info.getUsername(), info.getPassword())){
			
			PreparedStatement command = connection.prepareStatement("UPDATE Score SET deaths = deaths + 1 WHERE name = ?");
			
			command.setString(1, _name);
			
			command.executeUpdate();
			
		}catch(Exception e) {
			
		}
	}
	
}
