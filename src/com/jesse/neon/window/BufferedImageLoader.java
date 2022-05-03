package com.jesse.neon.window;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BufferedImageLoader {

	private BufferedImage image;
	
	public BufferedImage loadImage(String path) {
		
		System.out.println(getClass().getResource("./res/level.png"));
		
		try {
			image = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return image;	
	}
	
	
}
