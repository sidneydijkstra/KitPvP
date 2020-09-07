package plugin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;

import plugin.database.entities.SpawnPosition;

/**
 * The LobbySpawnPositionTable class for storing all the lobby SpawnPosition
 */
public class LobbySpawnPositionTable {
	
	/**
	 * <p>Create a new SpawnPosition in the table</p>
	 * @param the location
	 * @since 1.0
	 */
	public static void createSpawnPosition(Location _location) {
		LobbySpawnPositionTable.createSpawnPosition(_location.getX(), _location.getY(), _location.getZ(), _location.getPitch(), _location.getYaw());
	}

	/**
	 * <p>Create a new SpawnPosition in the table</p>
	 * @param the x position
	 * @param the y position
	 * @param the z position
	 * @param the pitch
	 * @param the yaw
	 * @since 1.0
	 */
	public static void createSpawnPosition(double _x, double _y, double _z, float _pitch, float _yaw) {
		ConnectionInfo info = Database.getConnectionInfo();
		try (Connection connection = DriverManager.getConnection(info.getConnectionString(), info.getUsername(), info.getPassword())){
			
			PreparedStatement command = connection.prepareStatement("INSERT INTO LobbySpawnPosition (x, y, z, pitch, yaw) VALUES (?, ?, ?, ?, ?)");
			
			command.setDouble(1, _x);
			command.setDouble(2, _y);
			command.setDouble(3, _z);
			command.setFloat(4, _pitch);
			command.setFloat(5, _yaw);
			
			command.executeUpdate();
			
		}catch(Exception e) {
			
		}
	}
	
	/**
	 * <p>Remove a SpawnPosition from the table</p>
	 * @param the table id
	 * @since 1.0
	 */
	public static void removeSpawnPosition(int _id) {
		ConnectionInfo info = Database.getConnectionInfo();
		try (Connection connection = DriverManager.getConnection(info.getConnectionString(), info.getUsername(), info.getPassword())){
			
			PreparedStatement command = connection.prepareStatement("DELETE FROM LobbySpawnPosition WHERE id=?");
			
			command.setInt(1, _id);
			
			command.executeUpdate();
			
		}catch(Exception e) {
			
		}
	}

	/**
	 * <p>Get all the SpawnPosition</p>
	 * @return a ArrayList with SpawnPosition
	 * @since 1.0
	 */
	public static ArrayList<SpawnPosition> retrieveSpawnPositions() {
		ConnectionInfo info = Database.getConnectionInfo();
		try (Connection connection = DriverManager.getConnection(info.getConnectionString(), info.getUsername(), info.getPassword())){
			
			Statement command = connection.createStatement();
			ResultSet result = command.executeQuery("select * from LobbySpawnPosition");
			
			ArrayList<SpawnPosition> spawnPositions = new ArrayList<SpawnPosition>();
			while (result.next()) {
				Location loc = new Location(null, result.getDouble("x"),result.getDouble("y"),result.getDouble("z"));
				loc.setPitch(result.getFloat("pitch"));
				loc.setYaw(result.getFloat("yaw"));
				SpawnPosition sp = new SpawnPosition(result.getInt("id"), loc);
				spawnPositions.add(sp);
			}
			
			return spawnPositions;
		}catch(Exception e) {
			
		}
		return new ArrayList<SpawnPosition>();
	}

	/**
	 * <p>Get a random SpawnPosition</p>
	 * @return the SpawnPosition
	 * @since 1.0
	 */
	public static SpawnPosition retrieveRandomSpawnPositions() {
		ConnectionInfo info = Database.getConnectionInfo();
		try (Connection connection = DriverManager.getConnection(info.getConnectionString(), info.getUsername(), info.getPassword())){
			
			Statement command = connection.createStatement();
			ResultSet result = command.executeQuery("select * from LobbySpawnPosition");
			
			ArrayList<SpawnPosition> spawnPositions = new ArrayList<SpawnPosition>();
			while (result.next()) {
				Location loc = new Location(null, result.getDouble("x"),result.getDouble("y"),result.getDouble("z"));
				loc.setPitch(result.getFloat("pitch"));
				loc.setYaw(result.getFloat("yaw"));
				SpawnPosition sp = new SpawnPosition(result.getInt("id"), loc);
				spawnPositions.add(sp);
			}
			
			return spawnPositions.get(new Random().nextInt(spawnPositions.size()));
		}catch(Exception e) {
			
		}
		return null;
	}
	
}
