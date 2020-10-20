package gameObjects.TerrainTypes;

import gameObjects.gameObject;
import gameObjects.gameObjectPhysics;
import gameObjects.gameObjectSub.Terrain;

public class TerrainBooster extends Terrain{
	public TerrainBooster(String name, int width, int height) {
		super(name, width, height, 6);
		isSolid = false;
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCollisionTo(gameObjectPhysics g) {
		// TODO Auto-generated method stub
		if(!g.boostCooldown)
		{
			g.boostCooldown(250);
			int xBoost = (int) (10 * Math.signum(g.getXVel()));
			int yBoost = (int) (6 * Math.signum(g.getYVel()));
			if(Math.abs(g.getXVel()) > Math.abs(g.getXVel()))
				g.setXVel(g.getXVel() * 2.5 + xBoost);
			else
				g.setYVel(g.getYVel() *  1.4 + yBoost);
		}
	}
	@Override
	public void onCollisionFrom(gameObjectPhysics g) {
		// TODO Auto-generated method stub
		
	}
}
 