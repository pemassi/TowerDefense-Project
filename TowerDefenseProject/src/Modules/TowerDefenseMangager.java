package Modules;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import Clac.Detect;
import Kind.EnemyType;
import Kind.MapObject;
import Kind.MapObject.MapObjectType;
import Kind.MapSoruceType;
import Kind.MapTile;
import Kind.ShootType;
import Kind.TowerType;
import Unit.Direction;
import Unit.Pos;
import Unit.Size;
import Unit.Velocity;
import util.ImageUtil;
import util.Log;

/**
 * 
 * Tower Defense Entire Game Manager
 * 
 * @author kyungyoonkim
 *
 */
public class TowerDefenseMangager {

	//For user interface
	private int 		money 	= 300;
	private int 		life 	= 1;
	private String 		status 	= "Game will be start place towers!";
	
	//Tower Defense variables
	private Size 					screenSize 		= null;
	private Size		 			mapSize 		= null;
	private MapTile[][] 			mapTile 		= null;
	private MapObject[][] 			mapTower 		= null;
	private ArrayList<Enemy> 		mapEnemy 		= new ArrayList<Enemy>();
	private ArrayList<Projectile> 	mapProjectile 	= new ArrayList<Projectile>();
	private ScreenUpdate 			screenUpdate 	= null;
	private RoundSystem 			round 			= null;
	private boolean					isStarted		= false;
	
	//Threading
	private UpdateThread 			updateThread 	= null;
	
	//Global Instance
	private static TowerDefenseMangager instance = null; 
	
	//Const
	public final static int TILE_SIZE 		= 50;
	public final static int UPDATE_DEALY 	= 10;
	
	public ClickMode clickmode = ClickMode.Nothing;
	public enum ClickMode {
		Nothing, Tower;
		
		private TowerType towertype;
		public ClickMode setTowerType(TowerType type) {
			this.towertype = type;
			return this;
		}
		
		public TowerType getTowerType() {
			return towertype;
		}
	}
	
	
	public interface ScreenUpdate {
		public void update();
	}

	public TowerDefenseMangager(ScreenUpdate screenUpdate)
	{
		this.screenUpdate = screenUpdate;
		this.instance = this;
		this.round = new RoundSystem(this);
	}
		
	
	public void startDrawingThread()
	{
		//Create Thread (for preventing freezing)
		updateThread = new UpdateThread();
		updateThread.start();
	}
	
	public static TowerDefenseMangager getInstance()
	{
		return instance;
	}
	
	public Size getScreenSize()
	{
		return screenSize;
	}
	
	/**
	 * Draw Tiles
	 * 
	 * @param g
	 */
	private synchronized void drawTile(Graphics g)
	{
		if(mapTile == null) return;
		
		for(int x=0; x<mapSize.getWidth(); x++)
		{
			for(int y=0; y<mapSize.getHeigth(); y++)
			{
				MapTile tile = this.mapTile[x][y];
				
				try 
				{
					BufferedImage tileImage = ImageIO.read(new File(tile.getImgURI()));
					g.drawImage(tileImage, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			
			}
		}
	}
	
	/**
	 * Draw Towers
	 * 
	 * @param g
	 */
	private synchronized void drawTower(Graphics g)
	{
		if(mapTower == null) return;
		
		for(int x=0; x<mapSize.getWidth(); x++)
		{
			for(int y=0; y<mapSize.getHeigth(); y++)
			{
				MapObject object = this.mapTower[x][y];
				MapObjectType type = object.getMapObjectType();
				
				if(type == null)
				{
					
				}
				else if(type != MapObjectType.Tile)
				{					
					g.drawImage(object.getObject().getImg(), x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
				}
			}
		}
	}
	
	/**
	 * Draw and move Enemy
	 * 
	 * @param g
	 */
	public synchronized void drawMoveEnemy(Graphics g)
	{
		if(mapEnemy.size() == 0) return;
		
		Enemy[] enemy = mapEnemy.toArray(new Enemy[0]);
		
		for(Enemy e:enemy)
		{
			Pos p = e.getPos();
			Velocity v = e.getVelocity();
			int t = UPDATE_DEALY;
			
			Pos tilePos = getClickedPos(p);
			
			double moved_x =  v.getX() * t;
			double moved_y =  v.getY() * t;
			
			//Get Direction
			
			Direction d = e.getDirection();
			if(d == Direction.Null)
			{
				Direction newD = Detect.findNearEnemyBlockDirection(e);
				Pos target = Detect.findNearEnemyBlockPos(e);
			
				e.setDirection(newD);
				e.setTarget(target);
				
			}
			else
			{
				Pos targetP = e.getTarget();
				
				
				if((int) p.getX() == (int)(targetP.getX() * TILE_SIZE) && (int) p.getY() == ((int) targetP.getY()* TILE_SIZE))
				{
					Direction newD = Detect.findNearEnemyBlockDirection(e);
					Pos target = Detect.findNearEnemyBlockPos(e);

					e.setDirection(newD);
					e.setTarget(target);		
				}		
			}
			
			d = e.getDirection();
			
			switch(d)
			{
				case Left:
					e.setPos(new Pos(p.getX() - moved_x, p.getY()));			
					
					break;
					
				case Right:
					
					e.setPos(new Pos(p.getX() + moved_x, p.getY()));
					break;
					 
					 
				case Down:
					
					e.setPos(new Pos(p.getX(), p.getY() + moved_y));
					break;
					
				case Up:
					
					e.setPos(new Pos(p.getX(), p.getY() - moved_y));
					break;
			}
			
			if(d == Direction.Null)
			{
				mapEnemy.remove(e);
				Log.i("Life -1");
				life--;
			}
			else
			{
				g.drawImage(e.getImg(), (int) e.getPosX(), (int) e.getPosY(), TILE_SIZE, TILE_SIZE, null);
				
			}

		}
	}
	
	/**
	 * Shoot projectile
	 * 
	 */
	private synchronized void towerShoot()
	{
		if(mapTower == null) return;
				
		for(int y=0; y<mapSize.getHeigth(); y++)
		{
			for(int x=0; x<mapSize.getWidth(); x++)
			{
				MapObject object = this.mapTower[x][y];
				MapObjectType type = object.getMapObjectType();
				
				if(type == MapObjectType.Tower)
				{		
					Tower tower = (Tower) object.getObject();
					
					//check ready to shoot
					if(tower.isShootReady())
					{

						Projectile shootType = tower.shoot(mapEnemy);
						
						if(shootType!=null) 
						{
							mapProjectile.add(shootType);
							Log.i("Tower (" + tower.getPos().toString() + ") SHOOT");
						}
						
					}
					else
					{
						tower.addShootDelayCount(UPDATE_DEALY);
					}
			
				}
			}
		}	
	}
	
	/**
	 * Calc Projectile is crashed
	 * @param g
	 */
	private synchronized void calcProjectile(Graphics g)
	{
		if(mapProjectile.size() == 0) return;
		
		Projectile[] projectile = mapProjectile.toArray(new Projectile[0]);
		
		for(int i=0; i<mapProjectile.size(); i++)
		{
			Projectile e = projectile[i];
			
			e.move2Target();
			
			//Detect is in collision
			if(e.detectCollision())
			{
				Enemy enemy = ((Enemy) e.getTarget());
				
				//if already dead ignore
				if(!enemy.isDead())
				{
					enemy.reduceHp(e.getDamage());
					mapProjectile.remove(e);
					
					Log.i("Enemy (" + e.getTarget().getPos().toString() + ") got " + e.getDamage() + " damage now HP " + 
								enemy.getHpNow() + "/" + enemy.getHpMax());
					
					
					//Check enemy is dead
					if(enemy.isDead())
					{
						Log.i("Enemy (" + e.getTarget().getPos().toString() + ") dead");
						money += enemy.getMoney();
						mapEnemy.remove(enemy);
					}
				}
			}
			else
			{
				g.drawImage(e.getImg(), (int) e.getPosX(), (int) e.getPosY(), TILE_SIZE, TILE_SIZE, null);
			}
		}
	}
	
	/**
	 * Draw Enemy HP
	 * 
	 * @param g
	 */
	private synchronized void drawEnemyHP(Graphics g)
	{
		if(mapEnemy.size() == 0) return;
		
		Enemy[] enemy = mapEnemy.toArray(new Enemy[0]);
		
		for(int i = enemy.length - 1; i >= 0; i--)
		{
			Enemy e = enemy[i];
			int percentage = (int)(	
					((double)e.getHpNow() / (double)e.getHpMax()) * 50
					);
			
			//if HP is not max, draw HP bar
			if(percentage != 50)
			{
				Pos drawPos;
				if(e.getPosY() < TILE_SIZE / 2) drawPos = new Pos(e.getPosX(), e.getPosY() + TILE_SIZE + 5);
				else drawPos = new Pos(e.getPosX(), e.getPosY() - 5);
				
				//Inside
				if(percentage < 20)	g.setColor(Color.RED);
				else g.setColor(Color.GREEN);
				g.fillRect((int)drawPos.getX(), (int)drawPos.getY(), percentage, 3);
				
				//Outline
				g.setColor(Color.BLACK);
				g.drawRect((int)drawPos.getX(), (int)drawPos.getY(), percentage, 3);
			}
		}
	}
	
	/**
	 * Get Clicked Tile Position
	 * 
	 * @param pos
	 * @return
	 */
	private Pos getClickedPos(Pos pos)
	{
		int x = (int) ( (int)pos.getX() / TILE_SIZE);
		int y = (int) ( (int)pos.getY() / TILE_SIZE);
		
		return new Pos(x,y);
	}
		
	/**
	 * Put Tower
	 * 
	 * @param type
	 * @param pos
	 */
	public void putTower(TowerType type, Pos pos)
	{
		Pos clicked = getClickedPos(pos);
		
		int x = (int) clicked.getX();
		int y = (int) clicked.getY();
		
		Pos towerPos = new Pos(x * TILE_SIZE, y * TILE_SIZE);
		
		//If tile is not for user
		if(mapTile[x][y]!=MapTile.User)
		{
			Log.e("You cannot put a tower there (" + clicked.toString() + ")");
			setStatus("You cannot put a tower there, click another place");
			return;
		}
		
		//If there is already tower
		if(mapTower[x][y].getMapObjectType() != MapObjectType.Tile)
		{
			Log.e("There is already tower (" + clicked.toString() + ")");
			setStatus("There is already tower, click another place");
			return;
		}
		try 
		{
			String		imageURI		= type.getImageURI();
			int 		shootRange 		= type.getShootRange();
			double 		shootDelay 		= type.getShootDelay();
			ShootType 	shootType 		= type.getShootType();
			
			
			BufferedImage towerImage = ImageIO.read(new File(imageURI));
			Tower tier1 = new Tower(towerPos, towerImage, shootRange, shootDelay, shootType);
									
			this.mapTower[x][y] = new MapObject(MapObjectType.Tower).setObject(tier1);;
			
			money -= type.getPrice();
			clickmode = ClickMode.Nothing;
			
			setStatus("The tower is placed");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		Log.i("Tower " + type.toString() + "(" + clicked.toString() + ") Added");
		//printMap();
	}
	
	public void clicked(Pos pos)
	{
		int x = (int) pos.getX();
		int y = (int) pos.getY();
		
		//If user clicked inside map
		if( x < mapSize.getWidth() * TILE_SIZE && y < mapSize.getHeigth() * TILE_SIZE)
		{
			if(clickmode != ClickMode.Nothing)
			{
				TowerType type = clickmode.getTowerType();
				
				putTower(type, pos);
			}
		}
		//If user clicked outside map
		else
		{
			int startPointX = 10;
			int startPointY = screenSize.getHeigth() - 110;
			
			if((startPointX < x && x < startPointX + 50) && (startPointY + 10 < y && y < startPointY + 60))
			{
				if(money < TowerType.Tier1.getPrice())
				{
					setStatus("Not enough money to buy that tower");
				}
				else
				{
					setStatus("Click any place you want to place that tower");
					clickmode = ClickMode.Tower.setTowerType(TowerType.Tier1);
				}
			}
			
			startPointX += 60;
			
			if((startPointX < x && x < startPointX + 50) && (startPointY + 10 < y && y < startPointY + 60))
			{
				if(money < TowerType.Tier2.getPrice())
				{
					setStatus("Not enough money to buy that tower");
				}
				else
				{
					setStatus("Click any place you want to place that tower");
					clickmode = ClickMode.Tower.setTowerType(TowerType.Tier2);
				}
			}
			
			startPointX += 60;
			
			if((startPointX < x && x < startPointX + 50) && (startPointY + 10 < y && y < startPointY + 60))
			{
				if(money < TowerType.Tier3.getPrice())
				{
					setStatus("Not enough money to buy that tower");
				}
				else
				{
					setStatus("Click any place you want to place that tower");
					clickmode = ClickMode.Tower.setTowerType(TowerType.Tier3);
				}
			}
			
		}	
	}
	
	
	
	/**
	 * Put Enemy
	 * 
	 * @param type
	 */
	public synchronized void putEnemy(EnemyType type)
	{
		//Find Enemy Start Point
		Pos startPoint = null;
		
		findingloop:
		for(int x=0; x<mapSize.getWidth(); x++)
		{
			for(int y=0; y<mapSize.getHeigth(); y++)
			{
				if(mapTile[x][y] == MapTile.Enemy)
				{
					startPoint = new Pos(x*TILE_SIZE,y*TILE_SIZE);
					break findingloop;
				}
			}
		}
		
		if(startPoint == null)
		{
			Log.e("Cannot find enemy start point");
			return;
		}
		
		try 
		{
			String imgURI = type.getImgURI();
			double velocityX = type.getVelocityX();
			double velocityY = type.getVelocityY();
			int maxHP = type.getMaxHP();
			int volume = type.getVolume();
			int money = type.getMoney();
			
			
			BufferedImage image = ImageIO.read(new File(imgURI));
			Enemy enemy = new Enemy(startPoint, image, new Velocity(velocityX,velocityY), maxHP, volume, money, mapTile);
			
			this.mapEnemy.add(enemy);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
				
		Log.i("Enemy " + type.toString() + "(" + startPoint.toString() + ") Added");
			
	}	
	
	/**
	 * Draw indicator 
	 * @param g
	 */
	private synchronized void drawInfo(Graphics g)
	{
		int startPointX = screenSize.getWidth() - 100;
		int startPointY = 0;
		
		g.setColor(Color.BLACK);
		
		//Level Label
		g.drawString(round.getRound().toString(), startPointX, startPointY + 20);
		
		//Life Label
		g.drawString("Life", startPointX, startPointY + 40);
		g.drawString(Integer.toString(life), startPointX, startPointY + 55);
		
		//Moeny Label
		g.drawString("Money", startPointX, startPointY + 80);
		g.drawString(Integer.toString(money), startPointX, startPointY + 95);
		
	}
	
	/**
	 * Crate button for buying tower
	 * @param g
	 */
	private synchronized void drawTowerButton(Graphics g)
	{
		int startPointX = 10;
		int startPointY = screenSize.getHeigth() - 110;
		
		g.drawString("Buy Tower", startPointX, startPointY);
		
		//Tier1
		
		BufferedImage image;
		try 
		{
			image = ImageIO.read(new File(TowerType.Tier1.getImageURI()));
			g.drawImage(image, startPointX, startPointY + 10, 50, 50, null);
			g.drawString("" + TowerType.Tier1.getPrice(), startPointX, startPointY + 65);
			
			startPointX += 60;
			
			image = ImageIO.read(new File(TowerType.Tier2.getImageURI()));
			g.drawImage(image, startPointX, startPointY + 10, 50, 50, null);
			g.drawString("" + TowerType.Tier2.getPrice(), startPointX, startPointY + 65);
			
			startPointX += 60;
			
			image = ImageIO.read(new File(TowerType.Tier3.getImageURI()));
			g.drawImage(image, startPointX, startPointY + 10, 50, 50, null);
			g.drawString("" + TowerType.Tier3.getPrice(), startPointX, startPointY + 65);
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Drawing the guide line when user put cursor on the map
	 * @param g
	 * @param p
	 */
	private synchronized void drawGuideLine(Graphics g, Point p)
	{
		if(p == null) return;
		
		int x = (int)p.getX();
		int y = (int)p.getY();
				
		if( x <= mapSize.getWidth() * TILE_SIZE && y <= mapSize.getHeigth() * TILE_SIZE)
		{
			Pos tilePos = getClickedPos(new Pos(x, y));
			
			g.setColor(Color.BLACK);
			g.drawRect((int)(tilePos.getX() * TILE_SIZE), (int)(tilePos.getY() * TILE_SIZE), TILE_SIZE, TILE_SIZE);
			
		}
		
		
	}
	
	private synchronized void drawStatus(Graphics g)
	{
		int startPointX = 200;
		int startPointY = screenSize.getHeigth() - 55;
	
		g.drawString(status, startPointX, startPointY);
	}
	
	public synchronized void setStatus(String s) {
		status = s;
	}
	
	/**
	 * Start the game
	 * 
	 */
	public synchronized void start()
	{
		Log.i("Game start...");
		
		isStarted = true;
	}

	/**
	 * Set the map
	 * 
	 * @param mapType
	 * @return
	 */
	public synchronized boolean setMap(MapSoruceType mapType)
	{
		Log.i("Map set : " + mapType.toString() + "(" + mapType.getURL() + ")");
		
		try 
		{
			Scanner file = new Scanner(new File(mapType.getURL()));
			
			//Get Map Size
			int height = file.nextInt();
			int width = file.nextInt();
			
			//Set map, screen size
			this.mapSize = new Size(width, height);
			this.screenSize = new Size(width * TILE_SIZE + 5 + 100, height * TILE_SIZE + 30 + 100);

			//Init Map Arrays
			this.mapTile = new MapTile[width][height];
			this.mapTower = new MapObject[width][height];
			
			Log.i("Map Size : " + mapSize.toString());
			
			file.nextLine();
			
			//W - user, R - enemy
			//Read Map
			for(int y=0; y<height; y++)
			{
				for(int x=0; x<width; x++)
				{
					String temp = file.next();
					System.out.printf("%s ", temp);
					
					if(temp.equals("W")) mapTile[x][y] = MapTile.User;
					else mapTile[x][y] = MapTile.Enemy;
					
					mapTower[x][y] = new MapObject(MapObjectType.Tile);
				}
			
				file.nextLine();
				System.out.println();
			}
			
			return true;

		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Update Screen
	 * 
	 * @param g
	 */
	public synchronized void update(Graphics g, Point mousePoint)
	{
		//Log.d("Start updating screen");
		
		if(isStarted) round.update(UPDATE_DEALY);
		
		drawTile(g);
		
		drawTower(g);
		
		towerShoot();
		
		calcProjectile(g);
		
		drawMoveEnemy(g);
		
		drawEnemyHP(g);
		
		drawInfo(g);
		
		drawTowerButton(g);
		
		drawGuideLine(g, mousePoint);
		
		
		if(life <= 0 && isStarted == true)
		{
			this.stop();
			status = "You're lose";
			JOptionPane.showMessageDialog(null, "You're Lose!!!");
		}
		
		drawStatus(g);
	}
	
	public void stop()
	{
		updateThread.finish();
		isStarted = false;
	}

	/**
	 * Game Process Thread
	 * 
	 */
	private class UpdateThread extends Thread {
		
		private boolean loop = true;
		
		@Override
		public void run() {
			while(loop)
			{
				try
				{					
					Thread.sleep(UPDATE_DEALY);
					screenUpdate.update();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
			}
			
		}
		
		public void finish() {
			this.loop = false;
		}
		
	}
	
	
	private void printMap() {
		
		int height = mapSize.getHeigth();
		int width = mapSize.getWidth();
		
		for(int y=0; y<height; y++)
		{
			for(int x=0; x<width; x++)
			{
				
				if(mapTower[x][y].getMapObjectType() != MapObjectType.Tile) System.out.printf("%5s ", mapTower[x][y].toString());
				else System.out.printf("%5s ", mapTile[x][y].toString());
				
			}
		
			System.out.println();
		}
		
	}

	public Size getMapSize() {
		return mapSize;
	}
	
	
	
	
	
	
	
}
