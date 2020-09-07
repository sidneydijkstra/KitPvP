package plugin.database.entities;

/**
 * The ScoreStat class for storing a Player's score
 */
public class ScoreStat {

	/**
	 * The Player name
	 */
	private String _name;

	/**
	 * The amount of kills
	 */
	private int _kills;

	/**
	 * The amount of deaths
	 */
	private int _deaths;
	
	/**
	 * <p>The ScoreStat constructor</p>	 
	 * @param the Player name
	 * @param the kill amount
	 * @param the death amount
	 * @return instance of class
	 * @since 1.0
	 */
	public ScoreStat(String _name, int _kills, int _deaths) {
		this._name = _name;
		this._kills = _kills;
		this._deaths = _deaths;
	}
	
	/**
	 * <p>Get the Player name</p>
	 * @return the Player name
	 * @since 1.0
	 */
	public String getName() {
		return this._name;
	}

	/**
	 * <p>Get the kill amount</p>
	 * @return the kill amount
	 * @since 1.0
	 */
	public int getKills() {
		return this._kills;
	}
	
	/**
	 * <p>Get the death amount</p>
	 * @return the death amount
	 * @since 1.0
	 */
	public int getDeaths() {
		return this._deaths;
	}
	
	/**
	 * <p>Get the KD</p>
	 * @return the KD
	 * @since 1.0
	 */
	public float getKD() {
		if(this._deaths == 0) {
			return this._kills;
		}
		
		if(this._kills == 0) {
			return -this._deaths;
		}
		
		return this._kills / this._deaths;
	}
}
