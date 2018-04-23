package Unit;

/**
 * Position
 * 
 * 
 * @author kyungyoonkim
 */
public class Pos {

	private double x = 0;
	private double y = 0;
	
	public Pos()
	{
		x = 0;
		y = 0;
	}
	
	public Pos(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Pos(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public String toString()
	{
		return "x:" + this.x + "/y:" + this.y;
	}	
}
