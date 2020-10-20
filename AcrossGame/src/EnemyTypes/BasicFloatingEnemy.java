package EnemyTypes;

import gameObjects.ObjectManager;
import gameObjects.SpriteSheet;
import gameObjects.gameObjectEnemy;

public class BasicFloatingEnemy extends gameObjectEnemy {

	public BasicFloatingEnemy() {
		super("basicfloatingenemy", 70, 70);
		setAnimation(0);
		gAccel = 0;
		friction = 1;
		isDroppingThroughPlatforms = true;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void EnemyUpdate() {
		// TODO Auto-generated method stub
		int targetx = ObjectManager.player.getX();
		int targety = ObjectManager.player.getY();
		int xdiff =  targetx - x;
		int ydiff = y - targety;
		if(Math.sqrt(xdiff*xdiff + ydiff * ydiff) <= 64)
			ObjectManager.player.Destroy();
		double angle = Math.atan2(ydiff, xdiff);
		xVel = Math.cos(angle)*4.5;
		yVel = Math.sin(angle)*4.5;
		int yRound = (int) Math.signum(yVel);
		yVel += yRound;
	}

	@Override
	public void setSprites() {
		SpriteSheets = new SpriteSheet[1];
		SpriteSheets[0] = new SpriteSheet("Idle");
		SpriteSheets[0].addSprite("src/Assets/Enemy1.png");
	
	}
	
}
