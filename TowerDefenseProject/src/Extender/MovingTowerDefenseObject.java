package Extender;

import java.awt.image.BufferedImage;

import Kind.MapObject;
import Unit.Pos;
import Unit.Velocity;

public class MovingTowerDefenseObject extends TowerDefenseObject {

	private Velocity velocity = null;
	private int volume = 0;
	
	public MovingTowerDefenseObject(Pos pos, BufferedImage image, MapObject type, Velocity velocity, int voulme) {
		super(pos, image, type);
		this.velocity = velocity;
		this.volume = voulme;
	}
	
	public Velocity getVelocity()
	{
		return this.velocity;
	}
	
	public int getVolume() {
		return this.volume;
	}
		
}
