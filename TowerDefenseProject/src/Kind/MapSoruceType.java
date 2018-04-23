package Kind;

/**
 * 
 * Map Source Types
 * 
 * @author kyungyoonkim
 *
 */
public enum MapSoruceType {
	
	MAP_1("res/Map1.txt"), 
	MAP_2("res/Map2.txt");
	
	private String url;
	private MapSoruceType(String url)
	{
		this.url = url;
	}
	
	public String getURL()
	{
		return this.url;
	}
}