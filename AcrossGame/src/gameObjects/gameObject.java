package gameObjects;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public abstract class gameObject {
	protected int x = 0;
	protected int y = 0;
	protected int width;
	protected int height;
	public String objectName;
	private String objectTag;
	protected SpriteSheet[] SpriteSheets;
	protected BufferedImage currentSprite;
	protected Rectangle hitBox;
	protected int SpriteSheetIndex;
	public String collisionType;
	public boolean killable = true;
	public gameObject(String name,int width, int height)
	{
		//Sets fields
		objectName = name;
		this.width = width;
		this.height = height;
		setSprites();
		//Adds to objectHandler
		ObjectManager.objectAddQueue.add(this);
	}
	//For updates
	public abstract void UpdateGameObject();
	//Sprites
	public abstract void setSprites();
	
	public void setAnimation(int index)
	{
		currentSprite = SpriteSheets[index].getSprite(0);
		SpriteSheetIndex = index;
	}
	//On collision, applies effect to gameObject g
	public abstract void onCollisionTo(gameObjectPhysics g);
	public abstract void onCollisionFrom(gameObjectPhysics g);
	//Setgetttesr
	public void setCoords(int x,int y)
	{
		setX(x);
		setY(y);
	}
	public BufferedImage getCurrentSprite()
	{
		return currentSprite;
	}
	public int getCurrentSpriteSheetIndex()
	{
		return SpriteSheetIndex;
	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public void setX(int par)
	{
		x = par;
	}
	public void setY(int par)
	{
		y = par;
	}
	public void Destroy()
	{
		ObjectManager.objectDestroyQueue.add(this);
	}
	public int getWidth()
	{
		return width;
	}
	public int getHeight()
	{
		return height;
	}
	public abstract Rectangle getHitBox();

	
}

