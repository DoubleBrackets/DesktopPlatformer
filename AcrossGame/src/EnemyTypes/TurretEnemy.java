package EnemyTypes;

import gameObjects.ObjectManager;
import gameObjects.SpriteSheet;
import gameObjects.gameObjectEnemy;

public class TurretEnemy extends gameObjectEnemy{
	int attackcooldown = 150;
	public TurretEnemy() {
		super("turretenemy", 64, 80);
		setAnimation(0);
		isDroppingThroughPlatforms = false;
		killable = false;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void EnemyUpdate() {
		if(attackcooldown == 0)
		{
			attackcooldown = 100;
			int targetx = ObjectManager.player.getX();
			int targety = ObjectManager.player.getY();
			int xdiff =  targetx - x;
			int ydiff = y - targety;
			double angle = Math.atan2(ydiff, xdiff);
			int xLaunchVel = (int) (Math.cos(angle)*25);
			int yLaunchVel = (int) (Math.sin(angle)*25);
			DeadlyProjectile pew = new DeadlyProjectile(xLaunchVel,yLaunchVel);
			pew.setCoords(x + width/2,y + height/2);
		}
		else
			attackcooldown--;
	}

	@Override
	public void setSprites() {
		SpriteSheets = new SpriteSheet[1];
		SpriteSheets[0] = new SpriteSheet("Idle");
		SpriteSheets[0].addSprite("src/Assets/Enemy2.png");
		
	}

}
