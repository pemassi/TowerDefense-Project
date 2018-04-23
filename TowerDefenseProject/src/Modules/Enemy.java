package Modules;

import java.awt.image.BufferedImage;
import java.util.Arrays;

import Extender.MovingTowerDefenseObject;
import Kind.MapObject;
import Kind.MapTile;
import Kind.MapObject.MapObjectType;
import Unit.Direction;
import Unit.Pos;
import Unit.Velocity;


/**
 * 
 * Enemy Class
 * 
 * @author kyungyoonkim
 *
 */
public class Enemy extends MovingTowerDefenseObject {

	private int 		hpMax 		= 0;
	private int 		hpNow 		= 0;
	private int 		money 		= 0;
	private Direction 	direction	= Direction.Null;
	private MapTile[][] mapTile 	= null;
	private Pos			target;
	
	public Enemy(Pos pos, BufferedImage image, Velocity velocity, int hpMax, int volume, int money, MapTile[][] mapTile) {
		super(pos, image, new MapObject(MapObjectType.Enemy), velocity, volume);
		
		this.hpMax = hpMax;
		this.hpNow = hpMax;
		this.direction = Direction.Null;
		this.money = money;
		
		this.mapTile = new MapTile[mapTile.length][];
		for (int i = 0; i < mapTile.length; i++) {
			this.mapTile[i] = Arrays.copyOf(mapTile[i], mapTile[i].length);
		}
	}

	public int getHpNow() {
		return hpNow;
	}
	
	public void reduceHp(int howmany) {
		this.hpNow -= howmany;
	}

	public void setHpNow(int hpNow) {
		this.hpNow = hpNow;
	}

	public int getHpMax() {
		return hpMax;
	}
	
	public boolean isDead() {
		return hpNow <= 0;
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public Direction getDirection() {
		return direction;
	}

	public Pos getTarget() {
		return target;
	}

	public void setTarget(Pos target) {
		this.target = target;
	}

	public MapTile[][] getMapTile() {
		return mapTile;
	}

	public void setMapTile(MapTile[][] mapTile) {
		this.mapTile = mapTile;
	}

	public int getMoney() {
		return money;
	}

	
	
	
	
}
