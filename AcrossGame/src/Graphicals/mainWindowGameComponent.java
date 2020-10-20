package Graphicals;



import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import controller.keyBinder;
import gameObjects.ObjectManager;
import gameObjects.gameObject;

public class mainWindowGameComponent extends JPanel {
	//Window and Frame
	static int width, height;
	public boolean shouldRenderGame = true;
	public static String bannerText = "";
	static Color flashColor = Color.white;
	static int flashCounter;
	static int flashCounterMax;//Debugging
	public static int x1 = 0,x2 = 0,y1 = 0,y2 = 0;
	public mainWindowGameComponent(int fwidth, int fheight)
	{
		super();
		setSize(width,height);
		width = fwidth;
		height = fheight;
		//Sets background to clear
		setBackground(new Color(0,0,0,0));
		setVisible(true);
	}
	public void paint(Graphics g)
	{
		if(shouldRenderGame)
		{
			drawVisuals(g);
			super.paint(g);
		}
	}
	static void drawVisuals(Graphics g)
	{
		Graphics2D pGraphics = (Graphics2D)g;
		//Background
		pGraphics.setBackground(new Color(0,0,0,0));
		//Game border
		if(mainWindow.hasFocus)
			pGraphics.setColor(Color.CYAN);
		else
			pGraphics.setColor(Color.GRAY);
		pGraphics.setStroke(new BasicStroke(6));
		pGraphics.drawRect(0, 0, width, height);
		//Debugging line
		pGraphics.drawLine(x1, y1, x2, y2);
		//Banner text
		pGraphics.setColor(Color.CYAN);
		Font bannerFont = new Font("Serif", Font.BOLD, 48);
		pGraphics.setFont(bannerFont);
		 //GlyphVector gv = bannerFont.createGlyphVector(((Graphics2D) g).getFontRenderContext(), bannerText);
		pGraphics.drawString(bannerText, width/2-150- 7 * bannerText.length(), 200);
		pGraphics.setColor(Color.LIGHT_GRAY);
		pGraphics.drawString(bannerText, width/2-152- 7 * bannerText.length(), 200);
		//Renders objects
		ArrayList<gameObject> toRender = ObjectManager.getObjects();
		for(gameObject obj : toRender)
		{
			if(obj != null)
			{
				BufferedImage sprite = obj.getCurrentSprite();
				if(sprite != null)
				{
					int x = obj.getX();
					int y = obj.getY();
					pGraphics.drawImage(sprite, (int)x,(int)y,null);
				}
				if(keyBinder.showHitboxes)
				{
					pGraphics.setColor(Color.GRAY);
					pGraphics.setStroke(new BasicStroke(1));
					pGraphics.drawRect(obj.getX(), obj.getY(), obj.getWidth(),obj.getHeight());
				}				
			}			
		}
		//Flash effect!
		if(flashCounter > 0)
		{
			flashCounter--;
			flashColor = new Color(flashColor.getRed(),flashColor.getGreen(),flashColor.getBlue(),(int) (255* (double)flashCounter / flashCounterMax));
			pGraphics.setColor(flashColor);
			pGraphics.fillRect(0, 0, width, height);
		}
	}
	public static void flash(int time, Color c)
	{
		flashColor = c;
		flashCounterMax = time;
		flashCounter = time;
	}

}
