package gameObjects.gameObjectSub;

import java.awt.image.BufferedImage;

import gameObjects.SpriteSheet;
import gameObjects.gameObject;
import gameObjects.gameObjectPhysics;

public abstract class Terrain extends gameObjectPhysics{
	public Terrain(String name, int width, int height,int layer){
		super(name, width, height, layer);
		gAccel = 0;
		physicsApplied = false;
	}

	@Override
	public void UpdateGameObject() {
		hitBox.x = x;
		hitBox.y = y;	
	}

	@Override
	public void setSprites() {
		//empty
	}
	@Override
	public BufferedImage getCurrentSprite()
	{
		return null;
	}

}
