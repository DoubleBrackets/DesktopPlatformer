package gameObjects;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class SpriteSheet
{
	String sheetName;
	ArrayList<BufferedImage> Sprites; 
	int size= 0;
	public SpriteSheet(String sheetName)
	{
		this.sheetName = sheetName;
		Sprites = new ArrayList<BufferedImage>();
	}
	public BufferedImage getSprite(int index)
	{
		return Sprites.get(index);
	}
	public String getSheetName()
	{
		return sheetName;
	}
	public void addSprite(String filePath)
	{
		File s = new File(filePath);
		BufferedImage sprite;
		try {
			sprite = ImageIO.read(s);
			Sprites.add(sprite);
			size++;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
