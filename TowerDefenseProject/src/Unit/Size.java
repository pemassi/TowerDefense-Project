package Unit;

/**
 * Position
 * 
 * 
 * @author kyungyoonkim
 */
public class Size {

	private int width = 0;
	private int height = 0;
	
	public Size()
	{
		width = 0;
		height = 0;
	}
	
	public Size(int width, int height)
	{
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeigth() {
		return height;
	}

	public void setHeigth(int Heigth) {
		this.height = Heigth;
	}
	
	public String toString()
	{
		return "Width:" + this.width + "/Heigth:" + this.height;
	}	
}
