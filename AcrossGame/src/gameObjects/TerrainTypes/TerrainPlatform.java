package gameObjects.TerrainTypes;

import gameObjects.gameObject;
import gameObjects.gameObjectPhysics;
import gameObjects.gameObjectSub.Terrain;

public class TerrainPlatform extends Terrain{

	public TerrainPlatform(String name, int width, int height) {
		super(name, width, height, 3);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCollisionTo(gameObjectPhysics g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCollisionFrom(gameObjectPhysics g) {
		// TODO Auto-generated method stub
		
	}
	
}
