package Clac;

import java.util.ArrayList;

import Extender.MovingTowerDefenseObject;
import Kind.MapTile;
import Modules.Enemy;
import Modules.Tower;
import Modules.TowerDefenseMangager;
import Unit.Direction;
import Unit.Pos;
import Unit.Velocity;

public class Detect {
	
	/**
	 * Calculate collision
	 * 
	 * @param target1   Stand Object
	 * @param target2	Coming Object
	 * @return
	 */
	public static boolean isCollision(MovingTowerDefenseObject target1, MovingTowerDefenseObject target2)
	{
		Pos pos1 = target1.getCenterPos();
		Pos pos2 = target2.getCenterPos();
		
		int vol1 = target1.getVolume();
		int vol2 = target2.getVolume();
		
		Pos startPos1 = new Pos(pos1.getX()-vol1, pos1.getY()-vol1);
		Pos endPos1 = new Pos(pos1.getX()+vol1, pos1.getY()+vol1);
		
		Pos startPos2 = new Pos(pos2.getX()-vol2, pos2.getY()-vol2);
		Pos endPos2 = new Pos(pos2.getX()+vol2, pos2.getY()+vol2);
		
		
		boolean isInsideX = (startPos1.getX() <= pos2.getX() && pos2.getX() <= endPos1.getX());
		boolean isInsideY = (startPos1.getY() <= pos2.getY() && pos2.getY() <= endPos1.getY());
		
		if(isInsideX && isInsideY)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	/**
	 * Calculate movement
	 * 
	 * @param move
	 * @param target
	 * @return
	 */
	public static Pos move2Target(MovingTowerDefenseObject move, MovingTowerDefenseObject target)
	{		
		Velocity v = move.getVelocity();

		Pos distance = new Pos(target.getPosX() - move.getPosX(), target.getPosY() - move.getPosY());
		
		//Gaps btw target
		double gapX = distance.getX();
		double gapY = distance.getY();
		
		double gapDistance = Math.sqrt(Math.pow(target.getPosX() - move.getPosX(), 2) + Math.pow(target.getPosY() - move.getPosY(), 2));
		double moveDistance = Math.sqrt(Math.pow(v.getX(), 2) + Math.pow(v.getY(), 2));
		double rateGapNMove = moveDistance / gapDistance;
		
		double moveX = gapX * rateGapNMove;
		double moveY = gapY * rateGapNMove;
		
		return new Pos(move.getPosX() + moveX, move.getPosY() + moveY);
	}
	
	/**
	 * Find near enemy from tower
	 * 
	 * @param tower
	 * @param mapEnemy
	 * @return
	 */
	public static Enemy findNearEnemy(Tower tower, ArrayList<Enemy> mapEnemy)
	{
		if(mapEnemy.size() == 0) return null;
		
		Enemy[] enemy = mapEnemy.toArray(new Enemy[0]);
		
		Pos towerPos = tower.getCenterPos();
		Enemy target = null;
		
		for(Enemy e:enemy)
		{
			if(target==null) target = e;
			else
			{
				Pos pos1 = e.getCenterPos();
				Pos pos2 = target.getCenterPos();
				
				double distance1 = Math.sqrt(Math.pow(pos1.getX() - towerPos.getX(), 2) + Math.pow(pos1.getY() - towerPos.getY(), 2));
				double distance2 = Math.sqrt(Math.pow(pos2.getX() - towerPos.getX(), 2) + Math.pow(pos2.getY() - towerPos.getY(), 2));
				
				if(distance1<distance2) target = e;
			}
		}
		
		return target;
	}

	public static Direction findNearEnemyBlockDirection(Enemy enemy)
	{
		Direction ret = Direction.Null;
		MapTile[][] mapTile = enemy.getMapTile();
		Pos nowPos = getMapPos(enemy.getPos());
		
		int x = (int) nowPos.getX();
		int y = (int) nowPos.getY();
		
		int maxX = TowerDefenseMangager.getInstance().getMapSize().getWidth();
		int maxY = TowerDefenseMangager.getInstance().getMapSize().getHeigth();
		
		if(!(x<1)) if(mapTile[x-1][y] == MapTile.Enemy) ret = Direction.Left; 
		if(!(x>maxX-2)) if(mapTile[x+1][y] == MapTile.Enemy) ret = Direction.Right; 
		if(!(y<1)) if(mapTile[x][y-1] == MapTile.Enemy) ret = Direction.Up; 
		if(!(y>maxY-2)) if(mapTile[x][y+1] == MapTile.Enemy) ret = Direction.Down; 
	
		if(ret != Direction.Null)
		{
			mapTile[x][y] = MapTile.User;
			return ret;
		}
		
		return Direction.Null;
	}
	
	
	public static Pos findNearEnemyBlockPos(Enemy enemy)
	{
		
		Direction direction = enemy.getDirection();
		MapTile[][] mapTile = enemy.getMapTile();
		Pos nowPos = getMapPos(enemy.getPos());
		
		
		int x = (int) nowPos.getX();
		int y = (int) nowPos.getY();
		
		int maxX = TowerDefenseMangager.getInstance().getMapSize().getWidth();
		int maxY = TowerDefenseMangager.getInstance().getMapSize().getHeigth();
		
		if(direction == Direction.Null)
		{
			if(!(x<1)) if(mapTile[x-1][y] == MapTile.Enemy) return new Pos(x-1, y);
			if(!(x>maxX-2)) if(mapTile[x+1][y] == MapTile.Enemy) return new Pos(x+1, y);
			if(!(y<1)) if(mapTile[x][y-1] == MapTile.Enemy) return new Pos(x, y-1);
			if(!(y>maxY-2)) if(mapTile[x][y+1] == MapTile.Enemy)  return new Pos(x, y+1);
		}
		else
		{
			if(direction != Direction.Right) if(!(x<1)) if(mapTile[x-1][y] == MapTile.Enemy) return new Pos(x-1, y);
			if(direction != Direction.Left) if(!(x>maxX-2)) if(mapTile[x+1][y] == MapTile.Enemy) return new Pos(x+1, y);
			if(direction != Direction.Down) if(!(y<1)) if(mapTile[x][y-1] == MapTile.Enemy) return new Pos(x, y-1);
			if(direction != Direction.Up) if(!(y>maxY-2)) if(mapTile[x][y+1] == MapTile.Enemy)  return new Pos(x, y+1);
		}
		
		return new Pos(-1, -1);
	}
	
	
	
	private static Pos getMapPos(Pos pos)
	{
		int x = (int) (pos.getX() / TowerDefenseMangager.TILE_SIZE);
		int y = (int) (pos.getY() / TowerDefenseMangager.TILE_SIZE);
		
		return new Pos(x,y);
	}
		
	
	
	
	
	
	
	
	
	
}

