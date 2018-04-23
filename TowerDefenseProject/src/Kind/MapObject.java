package Kind;

import Extender.TowerDefenseObject;

/**
 * 
 * Map Object
 * 
 * @author kyungyoonkim
 *
 */
public class MapObject {
	
	public enum MapObjectType {
		Enemy, Tower, Tile, Projectile;
	}
	
	private MapObjectType 		mapObjectType;
	private TowerDefenseObject 	object;
	
	public MapObject()
	{
		mapObjectType = null;
		object = null;
	}
	
	public MapObject(MapObjectType mapObjectType)
	{
		this.mapObjectType = mapObjectType;
		object = null;
	}
	
	public MapObjectType getMapObjectType() {
		return mapObjectType;
	}

	public MapObject setMapObjectType(MapObjectType mapObjectType) {
		this.mapObjectType = mapObjectType;
		return this;
	}
	
	public MapObject setObject(TowerDefenseObject object)
	{
		this.object = object;
		return this;
	}
	
	public TowerDefenseObject getObject()
	{
		return this.object;
	}
	
}
