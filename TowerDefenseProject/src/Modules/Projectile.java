package Modules;

import java.awt.image.BufferedImage;

import Clac.Detect;
import Extender.MovingTowerDefenseObject;
import Kind.MapObject;
import Kind.MapObject.MapObjectType;
import Kind.ShootType;
import Unit.Pos;
import Unit.Velocity;

/**
 * 
 * ProjectTile Class
 * 
 * @author kyungyoonkim
 *
 */
public class Projectile extends MovingTowerDefenseObject {
	
	private int 						damage 	= 0;
	private MovingTowerDefenseObject 	target;

	public Projectile(Pos pos, BufferedImage image, Velocity velocity, MovingTowerDefenseObject target, int damage, int volume) {
		super(pos, image, new MapObject(MapObjectType.Projectile), velocity, volume);
	
		this.damage = damage;
		this.target = target;
	}

	public int getDamage() {
		return damage;
	}


	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public MovingTowerDefenseObject getTarget() {
		return this.target;
	}
	
	public void move2Target() {
		
		Pos newPos = Detect.move2Target(this, this.getTarget());
		this.setPos(newPos);
	}
	
	public boolean detectCollision() {
		
		return Detect.isCollision(this, this.getTarget());
		
	}

}
