package util;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageUtil {

	public static BufferedImage getImage(String uri)
	{
		try
		{
			return ImageIO.read(new File(uri));
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
}

