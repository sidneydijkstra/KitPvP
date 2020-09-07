package plugin.database;

import java.sql.*;


/**
 * The Main Database class for creating the database connection and the tables
 */
public class Database {
	
	/**
	 * The ConnectionInfo variable that stores the database connection information
	 */
	private static ConnectionInfo connectionInfo;
	
	/**
	 * <p>Get the ConnectionInfo, this contains the information to connect with the database</p>
	 * @return the ConnectionInfo
	 * @since 1.0
	 */
	public static ConnectionInfo getConnectionInfo() {
		if(connectionInfo == null)
			connectionInfo = new ConnectionInfo("jdbc:mysql://localhost:3306/kitpvp_database", "root", "root");
		return connectionInfo;
	}
	
	/**
	 * <p>Initialize the database and create all the tables if the did not exists before</p>
	 * @return a Exception
	 * @since 1.0
	 */
	public static Exception initializeDatabase() {
		ConnectionInfo info = Database.getConnectionInfo();
		try (Connection connection = DriverManager.getConnection(info.getConnectionString(), info.getUsername(), info.getPassword())){
			
			PreparedStatement lobbySpawnPositionTable = connection.prepareStatement(
			"CREATE TABLE IF NOT EXISTS LobbySpawnPosition" +
            "(id BIGINT NOT NULL AUTO_INCREMENT," +
            " x DOUBLE NOT NULL," +
            " y DOUBLE NOT NULL," +
            " z DOUBLE NOT NULL," +
            " pitch FLOAT NOT NULL," +
            " yaw FLOAT NOT NULL," +
            " PRIMARY KEY (id))");
			
			PreparedStatement gameSpawnPositionTable = connection.prepareStatement(
			"CREATE TABLE IF NOT EXISTS GameSpawnPosition" +
            "(id BIGINT NOT NULL AUTO_INCREMENT," +
            " x DOUBLE NOT NULL," +
            " y DOUBLE NOT NULL," +
            " z DOUBLE NOT NULL," +
            " pitch FLOAT NOT NULL," +
            " yaw FLOAT NOT NULL," +
            " PRIMARY KEY (id))");
			
			PreparedStatement scoreTable = connection.prepareStatement(
			"CREATE TABLE IF NOT EXISTS Score" +
            "(id BIGINT NOT NULL AUTO_INCREMENT," +
            " name CHAR(255) NOT NULL," +
            " kills INT NOT NULL," +
            " deaths INT NOT NULL," +
            " PRIMARY KEY (id))");
			
			lobbySpawnPositionTable.executeUpdate();
			gameSpawnPositionTable.executeUpdate();
			scoreTable.executeUpdate();
			
		}catch(Exception e) {
			return e;
		}
		return null;
	}
}