package botChess;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class SpriteLoader {
	
	private static HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();

	public static BufferedImage getImage(String name) {
		if(!images.containsKey(name)) {
			loadImage(name);
		}
		return images.get(name);
	}
	
	private static void loadImage(String name) {
		try {
		    images.put(name, ImageIO.read(new File(name)));
		} catch (IOException e) {
		}
	}
}
