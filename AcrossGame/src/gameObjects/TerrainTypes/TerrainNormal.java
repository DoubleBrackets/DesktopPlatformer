package gameObjects.TerrainTypes;

import gameObjects.gameObject;
import gameObjects.gameObjectPhysics;
import gameObjects.gameObjectSub.Terrain;

public class TerrainNormal extends Terrain{

	public TerrainNormal(String name, int width, int height) {
		super(name, width, height,2);
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
