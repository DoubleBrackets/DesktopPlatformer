package EnemyTypes;

import gameObjects.SpriteSheet;
import gameObjects.gameObjectPhysics;

public class DeadlyProjectile extends gameObjectPhysics{

	public DeadlyProjectile(int xVel, int yVel) {
		super("deadlyprojectile", 32, 32, 2);
		setAnimation(0);
		gAccel = 0;
		friction = 0;
		isDroppingThroughPlatforms = true;
		isSolid = false;
		this.xVel = xVel;
		this.yVel = yVel;
	}

	@Override
	public void setSprites() {
		SpriteSheets = new SpriteSheet[1];
		SpriteSheets[0] = new SpriteSheet("Idle");
		SpriteSheets[0].addSprite("src/Assets/DeadlyProjectile.png");
		
	}
	@Override
	public void onCollisionFrom(gameObjectPhysics g) {
		if(g.objectName == "terrainnormal")
			Destroy();				
	}

	@Override
	public void onCollisionTo(gameObjectPhysics g) {
		if(g.killable == true && g.objectName == "Player")
			g.Destroy();
	}

}
