package Kind;

public enum MapTile {
	User("res/tile/tile_grass_1.png"),
	Enemy("res/tile/tile_stone_1.png");
	
	private String 	imgURI 		= null;
	
	private MapTile(String imgURI) {
		this.imgURI = imgURI;
	}
	
	public String getImgURI() {
		return imgURI;
	}

	
}
