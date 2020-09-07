package plugin.database.entities;

import org.bukkit.Location;

/**
 * The SpawnPosition class for storing a spawn location
 */
public class SpawnPosition {

	/**
	 * The id variable
	 */
	private int _id;

	/**
	 * The location variable
	 */
	private Location _location;

	/**
	 * <p>The SpawnPosition constructor</p>	 
	 * @param the id
	 * @param the location
	 * @return instance of class
	 * @since 1.0
	 */
	public SpawnPosition(int _id, Location _location) {
		this._id = _id;
		this._location = _location;
	}

	/**
	 * <p>Get the id</p>
	 * @return the id
	 * @since 1.0
	 */
	public int getId() {
		return _id;
	}

	/**
	 * <p>Get the location</p>
	 * @return the location
	 * @since 1.0
	 */
	public Location getLocation() {
		return _location;
	}
}
