package Modules;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Clac.Detect;
import Extender.TowerDefenseObject;
import Kind.MapObject;
import Kind.MapObject.MapObjectType;
import Kind.ShootType;
import Unit.Pos;
import Unit.Velocity;
import util.ImageUtil;

public class Tower extends TowerDefenseObject {
	
	
	//Shooting
	private int 		shootRange 		= 0;
	private double 		shootDelay 		= 0;
	private double 		shootDelayCount = 0;
	private boolean 	shootReady 		= true;
	private ShootType 	shootType 		= ShootType.Null;
	
	public Tower(Pos pos, BufferedImage image, int shootRange, double shootDelay, ShootType shootType) {
		super(pos, image, new MapObject(MapObjectType.Tower));
		
		this.shootRange = shootRange;
		this.shootDelay = shootDelay;
		this.shootDelayCount = 0;
		this.shootReady = true;
		this.shootType = shootType;

	}
	
	public int getShootRange() {
		return shootRange;
	}
	
	public void setShootRange(int shootRange) {
		this.shootRange = shootRange;
	}
	
	public double getShootDelay() {
		return shootDelay;
	}
	
	public void setShootDelay(double shootDelay) {
		this.shootDelay = shootDelay;
	}
	
	public double getShootDelayCount() {
		return shootDelayCount;
	}
	
	public void setShootDelayCount(double shootDelayCount) {
		this.shootDelayCount = shootDelayCount;
	}
	
	public void addShootDelayCount(double shootDelayCount) {
		this.shootDelayCount += shootDelayCount;
		
		if(this.shootDelayCount >= this.shootDelay)
		{
			this.setShootReady(true);
		}
	}
	
	public boolean isShootReady() {
		return shootReady;
	}
	
	public void setShootReady(boolean shootReady) {
		this.shootReady = shootReady;
	}
	
	public ShootType getShootType() {
		return shootType;
	}
	
	public void setShootType(ShootType shootType) {
		this.shootType = shootType;
	}
	
	/**
	 * Shoot
	 * 
	 * @param mapEnemy
	 * @return
	 */
	public Projectile shoot(ArrayList<Enemy> mapEnemy) {
		
		Enemy enemyTarget = Detect.findNearEnemy(this, mapEnemy);
		
		if(enemyTarget==null) return null;
			
		this.setShootDelayCount(0.0);
		this.setShootReady(false);
		
		ShootType shootType = this.getShootType();
		
		double velocityX = shootType.getVelocityX();
		double velocityY = shootType.getVelocityY();
		
		int damage = shootType.getDamage();
		int voulme = shootType.getVolume();
	
		BufferedImage towerImage = ImageUtil.getImage(shootType.getImgURI());
		
		return new Projectile(this.getPos(), towerImage , new Velocity(velocityX, velocityY), enemyTarget, damage, voulme);			
	}
	
	
	
	
	
}
