package gameObjects.TerrainTypes;

import gameObjects.gameObject;
import gameObjects.gameObjectPhysics;
import gameObjects.gameObjectSub.Terrain;

public class TerrainDeadly extends Terrain{

	public TerrainDeadly(String name, int width, int height) {
		super(name, width, height,6);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCollisionTo(gameObjectPhysics g)
	{
		if(g.killable == true)
			g.Destroy();
	}
	@Override
	public void onCollisionFrom(gameObjectPhysics g) {
		// TODO Auto-generated method stub
		
	}
}
