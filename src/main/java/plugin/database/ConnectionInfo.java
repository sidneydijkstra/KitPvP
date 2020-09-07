package plugin.database;

/**
 * The ConnectionInfo class that contains all the data for connection to the database
 */
public class ConnectionInfo {
	
	/**
	 * The connection string
	 */
	private String _connectionString;

	/**
	 * The database user name
	 */
	private String _username;

	/**
	 * The database password
	 */
	private String _password;
	
	/**
	 * <p>The ConnectionInfo constructor</p>	 
	 * @param the connection string
	 * @param the user name
	 * @param the password
	 * @return instance of class
	 * @since 1.0
	 */
	public ConnectionInfo(String _connectionString, String _username, String _password) {
		this._connectionString = _connectionString;
		this._username = _username;
		this._password = _password;
	}
	
	/**
	 * <p>Get the connection string</p>
	 * @return the connection string
	 * @since 1.0
	 */
	public String getConnectionString() {
		return this._connectionString;
	}

	/**
	 * <p>Get the user name</p>
	 * @return the user name
	 * @since 1.0
	 */
	public String getUsername() {
		return this._username;
	}

	/**
	 * <p>Get the password</p>
	 * @return the password
	 * @since 1.0
	 */
	public String getPassword() {
		return this._password;
	}
	
}
