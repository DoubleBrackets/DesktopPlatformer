package gameObjects;

import java.awt.Rectangle;
import java.io.IOException;

import Graphicals.mainWindow;
import controller.keyBinder;

public abstract class gameObjectPhysics extends gameObject{
	//Gameobject, but applies hitboxes and physics
	//Physics
	protected double xVel = 0;
	protected double yVel = 0;
	protected double friction = 1;
	protected double gAccel = 1;
	public boolean isDroppingThroughPlatforms = false;
	protected boolean isSolid = true;
	protected boolean physicsApplied = true;
	public boolean boostCooldown = false;
	public boolean airborn = false;
	//Screen Bounds
	protected static int leftBound = 0;
	protected static int rightBound;
	protected static int upperBound = 0;
	protected static int lowerBound;
	//Hitboxes
	protected Rectangle hitBox;
	int objectPhysicsLayer = 0; //0 for solid objects, 1 for platforms
	public gameObjectPhysics(String name, int width, int height, int layer){
		super(name, width, height);
		objectPhysicsLayer = layer;
		//Creating hitbox
		hitBox = new Rectangle(width,height);
		ObjectManager.objectPhysicsAddQueue.add(this);
	}
	@Override
	public void UpdateGameObject()
	{
		if(physicsApplied)
			updatePhysics();
	}
	void updatePhysics()
	{
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
		if(isSolid)
		{
			boolean xHit = false;
			boolean yHit = false;
			gameObjectPhysics xHitObject = null;
			gameObjectPhysics yHitObject = null;
			//Checks for collisions on x
			Rectangle checkHitBox = new Rectangle(hitBox);
			checkHitBox.height = height;
			//layer mask
			int[] mask = {2,4,5,6};
			//Raycast x collisions
			int xincr = (int) Math.signum(xVel);
			Rectangle xColl = new Rectangle();
			checkHitBox.width = width/2;
			for(int c = 0;c <= xVel*xincr;c++)
			{
				checkHitBox.x = (int)(x+c*xincr) + (width/4*(xincr + 1));
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
			checkHitBox.width = width;
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
					checkHitBox.y += height-2;
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
			if(yHit)
			{
				if(y+height/2> yColl.y+yColl.height/2)//below
				{
					y = yColl.y + yColl.height;
				}
				else//above
				{
					airborn = false;
					y = yColl.y - height;
				}
				yVel = 0;			
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
							y = yColl.y - height;
							yVel = 0;
						}
					}
				}
			}
		}
		//If not solid, just applies triggers
		else
		{
			int[] mask = {2,4,5,6};
			ObjectManager.checkForCollisions(hitBox,this,mask);		
		}
		//Applies velocity
		int nx,ny;
		nx = (int) (x + xVel);
		ny = (int)(y - yVel);
		//Checks bounds
		if(isSolid)
		{
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
			else if(ny > lowerBound- height)
			{
				airborn = false;
				ny = lowerBound - height;
				yVel = 0;
			}	
		}
			
		x = nx;
		y = ny;
		//Updates hitbox
		hitBox.x = x;
		hitBox.y = y;
	}
	
	//Get setters
	public void setXVel(double par)
	{
		xVel = par;
	}
	public void setYVel(double par)
	{
		yVel = par;
	}
	public double getXVel()
	{
		return xVel;
	}
	public double getYVel()
	{
		return yVel;
	}
	@Override
	public void setX(int par)
	{
		x = par;
		hitBox.x = par;
	}
	@Override
	public void setY(int par)
	{
		y = par;
		hitBox.y = par;
	}
	public Rectangle getHitBox()
	{
		return new Rectangle(hitBox);
	}
	public boolean getIsSolid()
	{
		return isSolid;
	}
	public void setIsSolid(boolean s)
	{
		isSolid = s;
	}
	@Override
	public void Destroy()
	{
		super.Destroy();
		ObjectManager.objectPhysicsDestroyQueue.add(this);
	}
	public static void setBounds(int left, int right, int lower, int upper)
	{
		leftBound = left;
		rightBound = right;
		lowerBound = lower;
		upperBound = upper;
	}
	public void boostCooldown(int mili)
	{
		boostCooldown = true;
		Runnable cd = new Runnable()
				{

					@Override
					public void run() {
						try {
							Thread.sleep(mili);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						boostCooldown = false;
					}			
				};
		Thread t = new Thread(cd);
		t.start();
	}
	public abstract void onCollisionTo(gameObjectPhysics g);

}
