package gameObjects.gameObjectSub;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.IOException;

import GameManager.levelManager;
import controller.keyBinder;
import gameObjects.ObjectManager;
import gameObjects.ScreenToGameEnvironment;
import gameObjects.SpriteSheet;
import gameObjects.gameObject;
import gameObjects.gameObjectPhysics;

public class Player extends gameObjectPhysics{

	public Player(String name, int width, int height){
		super(name, width, height,0);
		setCoords(500,100);
		setAnimation(0);
	}

	@Override
	public void UpdateGameObject() {
		airborn = true;
		//Slowdown
		int slowdown = (int) (friction * Math.signum(xVel));
		if(slowdown*slowdown > xVel*xVel)
			xVel = 0;
		else
			xVel = (int)(xVel - slowdown);
		//Gravitys
		yVel = (int)(yVel - gAccel);	
		//Collision checker
		boolean xHit = false;
		boolean yHit = false;
		gameObjectPhysics xHitObject = null;
		gameObjectPhysics yHitObject = null;
		//Checks for collisions on x
		Rectangle checkHitBox = new Rectangle(hitBox);
		checkHitBox.height -= 8;
		checkHitBox.x = (int) (x+xVel);
		//layer mask
		int[] mask = {2,4,5,6,8};
		//Raycast x collisions
		int xincr = (int) Math.signum(xVel);
		Rectangle xColl = new Rectangle();
		for(int c = 0;c <= xVel*xincr;c++)
		{
			checkHitBox.x = (int)(x+c*xincr);
			xHitObject = ObjectManager.checkForCollisions(checkHitBox,this,mask);			
			if(xHitObject != null && xHitObject.getHitBox()!= null)
			{
				xColl = xHitObject.getHitBox();
				xHit = true;
				break;
			}
		}
		//Raycast y collisions
		int yincr = (int) Math.signum(yVel);
		if(yincr == -1)
			checkHitBox.y = y + height;
		else if(yincr == 1)
			checkHitBox.y = y;
		Rectangle yColl = new Rectangle();
		for(int c = 0;c <= yVel*yincr;c++)
		{
			//solids
			checkHitBox.x = x;
			checkHitBox.width = width;
			checkHitBox.y = (int)(y - yincr * c);
			checkHitBox.height = height;
			yHitObject = ObjectManager.checkForCollisions(checkHitBox,this,mask);			
			if(yHitObject != null && yHitObject.getHitBox()!= null)
			{
				yColl = yHitObject.getHitBox();
				yHit = true;
				break;
			}
			else if(yVel <= 0 && !isDroppingThroughPlatforms)
			{
				checkHitBox.y += height-3;
				checkHitBox.height = 3;
				checkHitBox.x = x + width/4 + xincr * width/4;
				checkHitBox.width = width/2;
				//platforms
				yHitObject = ObjectManager.checkForCollisions(checkHitBox,this,3);			
				if(yHitObject != null && yHitObject.getHitBox()!= null)
				{
					yColl = yHitObject.getHitBox();
					yHit = true;
					break;
				}
				checkHitBox.y -= height-2;
			}
		}
		if(xHit)
		{
			if(xHitObject.objectName.equals("terrainnormal"))
			{
				if(x+width/2 > xColl.x+xColl.width/2)//right
				{
					x = xColl.x + xColl.width;
				}
				else//left
				{
					x = xColl.x - width;
				}
				xVel = 0;
			}
		}
		if(yHit)
		{
			//Solid terrain
			if(yHitObject.objectName.equals("terrainnormal"))
			{
				if(y+height/2>= yColl.y+yColl.height/2)//below
				{
					y = yColl.y + yColl.height+1;
				}
				else//above
				{
					airborn = false;
					keyBinder.jumpCounter = 0;
					ScreenToGameEnvironment.shouldUpdateTerrain = true;
					y = yColl.y - height;
				}
				yVel = 0;
			}			
			if(yHitObject.objectName.equals("terrainplatform"))
			{
				if(y + height <= yColl.y + yColl.height)//above
				{
					airborn = false;
					Rectangle dCheck = new Rectangle(hitBox);
					dCheck.y = yColl.y - height;
					gameObjectPhysics checkForDisplaceCollision = ObjectManager.checkForCollisions(dCheck,this,mask);		
					if(checkForDisplaceCollision == null)
					{
						keyBinder.jumpCounter = 0;
						ScreenToGameEnvironment.shouldUpdateTerrain = true;
						y = yColl.y - height;
						yVel = 0;
					}
				}
			}
		}
		//Appliesvelocity
		int nx,ny;
		nx = (int) (x + xVel);
		ny = (int)(y - yVel);
		//Checks bounds
		if(nx < leftBound)
		{
			nx = leftBound;
			xVel = 0;
		}
		else if(nx > rightBound - width)
		{
			nx = rightBound - width;
			xVel = 0;
		}
		if(ny < upperBound)
		{
			ny = upperBound;
			yVel = 0;
		}
		else if(ny > lowerBound - height)
		{
			airborn = false;
			ny = lowerBound - height;
			yVel = 0;
			keyBinder.jumpCounter = 0;
			ScreenToGameEnvironment.shouldUpdateTerrain = true;
		}
		x = nx;
		y = ny;
		//Updates hitbox
		hitBox.x = x;
		hitBox.y = y;
	}

	@Override
	public void setSprites() {
		SpriteSheets = new SpriteSheet[2];
		SpriteSheets[0] = new SpriteSheet("IdleRight");
		SpriteSheets[0].addSprite("src/Assets/PlayerRight.png");
		SpriteSheets[1] = new SpriteSheet("IdleLeft");
		SpriteSheets[1].addSprite("src/Assets/PlayerLeft.png");
		

	}

	@Override
	public void Destroy()
	{
		levelManager.loadLevel(levelManager.level,new Color(50,0,0));
	}

	@Override
	public void onCollisionTo(gameObjectPhysics g) {
		if(g.objectName == "basicfloatingenemy")
		{
			Destroy();
		}		
	}

	@Override
	public void onCollisionFrom(gameObjectPhysics g) {
		// TODO Auto-generated method stub
		
	}
}
