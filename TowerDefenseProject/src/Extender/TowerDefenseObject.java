package Extender;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Kind.MapObject;
import Unit.Pos;
import util.Log;

import static Modules.TowerDefenseMangager.TILE_SIZE;

/**
 * 
 * Tower Object
 * 
 * @author kyungyoonkim
 *
 */
public class TowerDefenseObject {

	private Pos 			pos 		= null;
	private BufferedImage 	image 		= null;
	private MapObject 		type 		= null;
	
	public TowerDefenseObject(Pos pos, BufferedImage image, MapObject type)
	{
		this.pos = pos;
		this.image = image;
		this.type = type;
	}
	
	public Pos getPos()
	{
		return pos;
	}
	
	public void setPos(Pos pos)
	{
		this.pos = pos;
	}
	
	public Pos getCenterPos()
	{
		int pad = TILE_SIZE / 2;
		return new Pos(this.getPosX() + pad, this.getPosY() + pad);
	}
	
	public double getPosX()
	{
		return pos.getX();
	}
	
	public void setPosX(int x)
	{
		this.pos.setX(x);
	}
	
	public double getPosY()
	{
		return pos.getY();
	}
	
	public void setPosY(int y)
	{
		this.pos.setY(y);
	}
	
	public void setImg(BufferedImage image)
	{
		this.image = image;
	}
	
	public int getImgWidth()
	{
		return image.getWidth();
	}
	
	public int getImgHeight()
	{
		return image.getHeight();
	}
	
	public BufferedImage getImg()
	{
		if(image==null) Log.e("ERROR NULL IMAGE");
		return image;
	}
	
	public void setType(MapObject type)
	{
		this.type = type;
	}
	
	public MapObject getType()
	{
		return type;
	}
	
	public void draw(Graphics g)
	{
		g.drawImage(image, (int) pos.getX(), (int) pos.getY(), null);
	
	}
	
	
}
