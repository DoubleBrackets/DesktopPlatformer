package gameObjects.gameObjectSub;

import java.awt.Color;

import GameManager.levelManager;
import gameObjects.SpriteSheet;
import gameObjects.gameObject;
import gameObjects.gameObjectPhysics;

public class LevelEnd extends gameObjectPhysics{

	public LevelEnd(int x,int y) {
		super("levelEnd", 128, 128, 8);
		killable = false;
		setCoords(x,y);
		setAnimation(0);
		physicsApplied = false;
		isSolid = false;
	}

	@Override
	public void setSprites() {
		SpriteSheets = new SpriteSheet[1];
		SpriteSheets[0] = new SpriteSheet("LevelEnd");
		SpriteSheets[0].addSprite("src/Assets/levelEnd.png");
		
	}

	@Override
	public void onCollisionTo(gameObjectPhysics g) {
		if(g.objectName == "Player");
			levelManager.loadLevel(levelManager.level+1,new Color(0,100,120));		
	}

	@Override
	public void onCollisionFrom(gameObjectPhysics g) {
		// TODO Auto-generated method stub
		
	}

}
