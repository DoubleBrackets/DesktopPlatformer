package gameObjects;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Graphicals.mainWindow;
import gameObjects.gameObjectSub.Terrain;

public class ScreenToGameEnvironment {
//Generates environment based on bufferedImage of screen
	public static ArrayList<TerrainPoint> terrainParts = new ArrayList<TerrainPoint>();
	public static ArrayList<TerrainPoint> Buffer = new ArrayList<TerrainPoint>();
	static int freq = 3;
	public static Color[][] colorArray = new Color[mainWindow.height/freq][mainWindow.width/freq];
	public static boolean shouldUpdateTerrain = true;
	public static boolean inUpdate = false;
	public static void UpdateEnvironment(boolean ignorePlayer)
	{
		if(shouldUpdateTerrain && !inUpdate)
		{
			inUpdate = true;
			//Clears pervious terrain
			Buffer.clear();
			int width = mainWindow.width;
			int height = mainWindow.height;
			Rectangle pHB = new Rectangle(ObjectManager.player.getHitBox());
			pHB.y -= 1;
			pHB.height -= 5;
			BufferedImage screenShot  = mainWindow.GetTerrainScreenshot();
			//For ignoring pixels around player
			//For merging
			boolean prevSimilarBlack = false;
			int missCounterBlack = 0;
			int bindex = 0;
			boolean prevSimilarGray = false;
			int missCounterGray = 0;
			int gindex = 0;
			boolean prevSimilarRed = false;
			int missCounterRed = 0;
			int rindex = 0;
			boolean prevSimilarGreen = false;
			int missCounterGreen = 0;
			int grindex = 0;
			for(int y = 1;y < height-1;y+=freq)
			{
				prevSimilarBlack = false;
				prevSimilarRed = false;
				prevSimilarGreen = false;
				prevSimilarGray = false;
				for(int x = 1;x < width-1;x+=freq)
				{
					//Gets average of pixel color in a 3x3
					Color pixelColor;
					int r = 0,g = 0,b = 0;
					int count = freq*freq;
					//Ignores pixels around player hitbox
					boolean toIgnore = false;
					Rectangle checkforobjectshb = new Rectangle(x-2,y-2,freq+4,freq+4);
					//Checks for pixel colors, averages into sections
					if((checkforobjectshb.intersects(pHB) || 
							checkForTerrainIntersect(ObjectManager.terrainIgnorePositions, checkforobjectshb)) && 
							ignorePlayer == true)
						toIgnore = true;
					for(int c = -1;c <= 1;c++)
					{
						for(int z = -1;z <= 1;z++)
						{
							pixelColor = new Color(screenShot.getRGB(x+c, y+z));
							r += pixelColor.getRed();
							g += pixelColor.getGreen();
							b += pixelColor.getBlue();
						}
					}
					pixelColor = new Color(r/count,g/count,b/count);
					if(!toIgnore)
						colorArray[y/freq][x/freq] = pixelColor;
					//Converts sections into terrain based on color
					if(colorArray[y/freq][x/freq] != null)
					{
						missCounterBlack++;
						missCounterGray++;
						missCounterRed++;
						missCounterGreen++;
						//Checks for deadly terrain, ignores object mask
						if(isColorSimilar(pixelColor, Color.RED,55))
						{
							missCounterRed--;	
							if(prevSimilarRed && rindex >= 1 && Buffer.get(rindex)!=null )
							{
								Buffer.get(rindex).width  = x - Buffer.get(rindex).x;
								missCounterRed = 0;
							}
							//New hitbox
							else
							{
								TerrainPoint tP = new TerrainPoint(x,y,3,3,"deadly");
								Buffer.add(tP);	
								prevSimilarRed = true;
								rindex = Buffer.size()-1;
							}
						}
						//Boosting terrain
						else if(isColorSimilar(colorArray[y/freq][x/freq], new Color(34,177,76),40))
						{
							missCounterGreen--;
							if(prevSimilarGreen && grindex >= 1 && Buffer.get(grindex)!=null )
							{
								Buffer.get(grindex).width  = x - Buffer.get(grindex).x;
								missCounterGreen = 0;
							}
							//New hitbox
							else
							{
								TerrainPoint tP = new TerrainPoint(x,y,6,10,"booster");
								Buffer.add(tP);	
								prevSimilarGreen = true;
								grindex = Buffer.size()-1;
							}
						}
						//Normal terrain
						else if(isColorSimilar(colorArray[y/freq][x/freq],Color.BLACK,33))
						{			
							missCounterBlack--;
							//Merges with previous detected terrainpoint if exists
							if(prevSimilarBlack && bindex >= 1 && Buffer.get(bindex)!=null )
							{
								Buffer.get(bindex).width  = x - Buffer.get(bindex).x;
								missCounterBlack = 0;
							}
							//New hitbox
							else
							{
								TerrainPoint tP = new TerrainPoint(x,y-4,6,10,"normal");
								Buffer.add(tP);	
								prevSimilarBlack = true;
								bindex = Buffer.size()-1;
							}
						}
						//Creates platforms at points where there is vertical border(difference between color above)
						else if(y >= 3 && colorArray[y/freq-1][x/freq] != null && !isColorSimilar(colorArray[y/freq][x/freq],colorArray[y/freq-1][x/freq],5))
						{		
							missCounterGray--;
							//Merges with previous detected terrainpoint if exists
							if(prevSimilarGray && gindex >= 1 && Buffer.get(gindex)!=null )
							{
								Buffer.get(gindex).width = x - Buffer.get(gindex).x;
								missCounterGray = 0;
							}
							//New hitbox
							else
							{
								TerrainPoint tP = new TerrainPoint(x,y,3,3,"platform");
								Buffer.add(tP);	
								prevSimilarGray = true;
								gindex = Buffer.size()-1;
							}
						}
						//Counts number of spaces of previous terrainpoint
						if(missCounterBlack >= 10)
						{
							missCounterBlack = 0;
							prevSimilarBlack = false;
						}
						if(missCounterGray >= 10)
						{
							missCounterGray = 0;
							prevSimilarGray = false;
						}	
						if(missCounterGreen >= 10)
						{
							missCounterGreen = 0;
							prevSimilarGreen = false;
						}
						if(missCounterRed >= 10)
						{
							missCounterRed = 0;
							prevSimilarRed = false;
						}
					}								
				}				
				missCounterBlack = 0;
				missCounterGray = 0;
				missCounterRed = 0;
				missCounterGreen = 0;
			}
			terrainParts = new ArrayList<TerrainPoint>(Buffer);
			inUpdate = false;
		}
	}
	public static boolean isColorSimilar(Color c1, Color c2, double maxError)
	{
		if(Math.abs(c1.getBlue() - c2.getBlue()) > maxError)
		{
			return false;
		}
		if(Math.abs(c1.getRed() - c2.getRed()) > maxError)
		{
			return false;
		}
		if(Math.abs(c1.getGreen() - c2.getGreen()) > maxError)
		{
			return false;
		}
		return true;
	}
	static boolean checkForTerrainIntersect(ArrayList<Rectangle> arr, Rectangle refHitbox)
	{
		int l = arr.size();
		for(int x = 0;x < l;x++)
		{
			if(refHitbox.intersects(arr.get(x)))
			{
				return true;
			}
		}
		return false;
	}
}

