package gameObjects;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;

import GameManager.levelManager;
import gameObjects.TerrainTypes.TerrainBooster;
import gameObjects.TerrainTypes.TerrainDeadly;
import gameObjects.TerrainTypes.TerrainNormal;
import gameObjects.TerrainTypes.TerrainPlatform;
import gameObjects.gameObjectSub.Player;
import gameObjects.gameObjectSub.Terrain;

public class ObjectManager {
	//object arraylsits
	static ArrayList<gameObject> objectList = new ArrayList<gameObject>();
	static ArrayList<objectPhysicsLayer> physicsObjectsLayers = new ArrayList<objectPhysicsLayer>();
	static ArrayList<Terrain> terrainParts = new ArrayList<Terrain>();
	static ArrayList<TerrainPoint> tChangeCheck = new ArrayList<TerrainPoint>();;
	//Destroy queue
	static ArrayList<gameObject> objectDestroyQueue = new ArrayList<gameObject>();
	static ArrayList<gameObjectPhysics> objectPhysicsDestroyQueue = new ArrayList<gameObjectPhysics>();
	//Add queue
	static ArrayList<gameObject> objectAddQueue = new ArrayList<gameObject>();
	static ArrayList<gameObjectPhysics> objectPhysicsAddQueue = new ArrayList<gameObjectPhysics>();
	//Saves positions for screen to terrain
	static ArrayList<Rectangle> terrainIgnorePositions = new ArrayList<Rectangle>();
	static int objectCount = 0;
	public static Player player;
	//Layers; 0 is player, solid terrain is 2, platforms are 3, level points are 4
	public ObjectManager() throws IOException
	{
		//Adds base layers
		 for(int x = 0;x < 20;x++)
			 physicsObjectsLayers.add(new objectPhysicsLayer(x));	 
		 player = new Player("Player", 50,110);
		 //Starts game
		 levelManager.loadLevel(1,new Color(0,100,120));
	}
	public static void updateObjects()
	{
		if(ScreenToGameEnvironment.shouldUpdateTerrain && !tChangeCheck.equals(ScreenToGameEnvironment.terrainParts))
		{
			//Gets terrain from screentogameenvironment
			for(Terrain t : terrainParts)
				t.Destroy();
			terrainParts.clear();
			ArrayList<TerrainPoint> tPoints = new ArrayList<TerrainPoint>(ScreenToGameEnvironment.terrainParts);
			for(TerrainPoint p: tPoints)
			{
				if(p!= null)
				{
					if(p.tag.equals("normal"))
					{
						TerrainNormal t = new TerrainNormal("terrainnormal",p.width,p.height);
						t.setCoords(p.x-2, p.y-2);
						t.UpdateGameObject();
						terrainParts.add(t);
					}
					else if(p.tag.equals("deadly"))
					{
						TerrainDeadly t = new TerrainDeadly("terraindeadly",p.width,p.height);
						t.setCoords(p.x-2, p.y-2);
						t.UpdateGameObject();
						terrainParts.add(t);
					}
					else if(p.tag.equals("platform"))
					{
						TerrainPlatform t = new TerrainPlatform("terrainplatform",p.width,p.height);
						t.setCoords(p.x-2, p.y-2);
						t.UpdateGameObject();
						terrainParts.add(t);
					}
					else if(p.tag.equals("booster"))
					{					
						TerrainBooster t = new TerrainBooster("terrainbooster",p.width,p.height);
						t.setCoords(p.x-2, p.y-2);
						t.UpdateGameObject();
						terrainParts.add(t);
					}
				}
			}
			tChangeCheck = new ArrayList<TerrainPoint>(ScreenToGameEnvironment.terrainParts);
		}		
		//Adds new objects
		ArrayList<gameObject> ta = new ArrayList<gameObject>(objectAddQueue);
		ArrayList<gameObjectPhysics> pa = new ArrayList<gameObjectPhysics>(objectPhysicsAddQueue);
		for(gameObject object: ta)
		{
			addgameObject(object);
		}
		for(gameObjectPhysics object: pa)
		{
			addgameObjectPhysics(object);
		}
		objectAddQueue.clear();
		objectPhysicsAddQueue.clear();
		//Destroys all objects queued 
		ArrayList<gameObject> td = new ArrayList<gameObject>(objectDestroyQueue);
		ArrayList<gameObjectPhysics> pd = new ArrayList<gameObjectPhysics>(objectPhysicsDestroyQueue);
		for(gameObject object: td)
		{
			removegameObject(object);
		}
		for(gameObjectPhysics object: pd)
		{
			removegameObject(object);
			removegameObjectPhysics(object);
		}
		objectDestroyQueue.clear();
		objectPhysicsDestroyQueue.clear();
		//Updates object states
		for(gameObject object: objectList)
		{
			object.UpdateGameObject();
		}
		
	}
	public static void removegameObjectPhysics(gameObjectPhysics g)
	{
		ObjectManager.physicsObjectsLayers.get(g.objectPhysicsLayer).objectPhysicsList.remove(g);
	}
	public static void removegameObject(gameObject g)
	{
		objectList.remove(g);
	}
	public static void addgameObjectPhysics(gameObjectPhysics g)
	{
		ObjectManager.physicsObjectsLayers.get(g.objectPhysicsLayer).objectPhysicsList.add(g);
	}
	public static void addgameObject(gameObject g)
	{
		objectList.add(g);
	}
	public static ArrayList<gameObject> getObjects()
	{
		return new ArrayList<gameObject>(objectList);
	}
	public static ArrayList<objectPhysicsLayer> getPhysicsObjects()
	{
		return new ArrayList<objectPhysicsLayer>(physicsObjectsLayers);
	}
	//Layer mask for collisions
	public static gameObjectPhysics checkForCollisions(Rectangle hitBox, gameObjectPhysics subject, int layer)
	{
		for(gameObjectPhysics toCheck: physicsObjectsLayers.get(layer).objectPhysicsList)
		{
			if(!toCheck.equals(subject))
			{
				Rectangle toCheckHb = toCheck.getHitBox();
				if(hitBox.intersects(toCheckHb))
				{
					if(subject != null)
					{
						toCheck.onCollisionTo(subject);
						subject.onCollisionFrom(toCheck);
					}
					if(toCheck.getIsSolid())
						return toCheck;
				}
			}			
		}
		return null;
	}
	//Multiple layers
	public static gameObjectPhysics checkForCollisions(Rectangle hitBox, gameObjectPhysics subject, int[] layer)
	{
		int l = layer.length;
		for(int x = 0;x <l;x++)
		{
			for(gameObjectPhysics toCheck: physicsObjectsLayers.get(layer[x]).objectPhysicsList)
			{
				if(!toCheck.equals(subject))
				{
					Rectangle toCheckHb = toCheck.getHitBox();
					if(hitBox.intersects(toCheckHb))
					{
						if(subject != null)
						{
							toCheck.onCollisionTo(subject);
							subject.onCollisionFrom(toCheck);
						}
						if(toCheck.getIsSolid())
							return toCheck;
					}
				}			
			}
		}
		return null;
	}
	//For terraing generation; gets hitboxes to skip over when scanning
	public static void savePositions()
	{
		terrainIgnorePositions.clear();
		int l = physicsObjectsLayers.size();
		for(gameObjectPhysics toCheck: physicsObjectsLayers.get(5).objectPhysicsList)
		{
			Rectangle toCheckHb = toCheck.getHitBox();
			if(toCheck.objectName == "turretenemy")
				toCheckHb.height -= 3d ;
			if(toCheck.objectName == "basicfloatingenemy")
			{
				toCheckHb.width -= 2;
				toCheckHb.x -= 2;
				toCheckHb.height -= 2;
				toCheckHb.y -= 2;
			}
			terrainIgnorePositions.add(toCheckHb);
		}
		for(gameObjectPhysics toCheck: physicsObjectsLayers.get(8).objectPhysicsList)
		{
			Rectangle toCheckHb = toCheck.getHitBox();
			terrainIgnorePositions.add(toCheckHb);
		}
	}
}
//Layer class
class objectPhysicsLayer
{
	public ArrayList<gameObjectPhysics> objectPhysicsList;
	int layer;
	public objectPhysicsLayer(int layer)
	{
		this.layer = layer;
		objectPhysicsList = new ArrayList<gameObjectPhysics>();
	}
}
