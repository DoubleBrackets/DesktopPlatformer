package GameManager;

import java.awt.Color;
import java.util.ArrayList;

import EnemyTypes.BasicFloatingEnemy;
import EnemyTypes.TurretEnemy;
import Graphicals.mainWindow;
import Graphicals.mainWindowGameComponent;
import controller.keyBinder;
import gameObjects.ObjectManager;
import gameObjects.gameObjectPhysics;
import gameObjects.gameObjectSub.LevelEnd;

public class levelManager {
	public static int level = 0;
	static LevelEnd levelendportal;
	static ArrayList<gameObjectPhysics> levelassets = new ArrayList<gameObjectPhysics>();
	static boolean loading = false;
	public static void loadLevel(int level,Color transitionColor)
	{
		if(!loading){
			loading = true;
			Runnable levelloader = new Runnable()
			{
				@Override
				public void run() {
					levelManager.level = level;
					for (gameObjectPhysics g : levelassets)
					{
						g.Destroy();
					}
					levelassets.clear();
					mainWindowGameComponent.flash(25, transitionColor);
					if(level == 1)
					{
						levelendportal = new LevelEnd(mainWindow.width-150,mainWindow.height-150);
						ObjectManager.player.setCoords(mainWindow.width/2,mainWindow.height-250);
						levelassets.add(levelendportal);
						keyBinder.inputActive = false;
						mainWindowGameComponent.bannerText = "Reach the triangle rose portal end thing";
						sleep(5000);
						mainWindowGameComponent.bannerText = "Touching red = bad (true for some enemies too)";
						sleep(5000);
						mainWindowGameComponent.bannerText = "green = boost";
						sleep(5000);
						mainWindowGameComponent.bannerText = "wasd spacebar controls, q to left click with character";
						sleep(5000);
						mainWindowGameComponent.bannerText = "gl";
						sleep(3000);
						mainWindowGameComponent.bannerText = "1 - A short distance";
						keyBinder.inputActive = true;
						
					}
					else if(level == 2)
					{
						mainWindowGameComponent.bannerText = "2 - Height?";
						levelendportal = new LevelEnd(150,200);
						ObjectManager.player.setCoords(mainWindow.width-200,mainWindow.height-250);
						levelassets.add(levelendportal);
					}
					else if(level == 3)
					{
						mainWindowGameComponent.bannerText = "3 - Don't touch the purple boi :(";
						levelendportal = new LevelEnd(mainWindow.width-150,mainWindow.height/2);
						levelassets.add(levelendportal);
						BasicFloatingEnemy joe = new BasicFloatingEnemy();
						levelassets.add(joe);
						joe.setCoords(mainWindow.width/2, mainWindow.height/2);
						ObjectManager.player.setCoords(150,200);
					}
					else if(level == 4)
					{
						mainWindowGameComponent.bannerText = "4 - two";
						levelendportal = new LevelEnd(mainWindow.width-150,mainWindow.height/2);
						levelassets.add(levelendportal);
						BasicFloatingEnemy joe = new BasicFloatingEnemy();
						joe.setCoords(mainWindow.width/2, mainWindow.height/2 - 200);
						levelassets.add(joe);
						BasicFloatingEnemy joe2 = new BasicFloatingEnemy();
						joe2.setCoords(mainWindow.width/2, mainWindow.height/2+ 200);
						levelassets.add(joe2);
						ObjectManager.player.setCoords(150,200);
					}
					else if(level == 5)
					{
						mainWindowGameComponent.bannerText = "5 - probably more than two";
						levelendportal = new LevelEnd(mainWindow.width-150,mainWindow.height/2);
						levelassets.add(levelendportal);
						for(int x = 0;x < 20;x++)
						{
							BasicFloatingEnemy joe = new BasicFloatingEnemy();
							joe.setCoords(mainWindow.width/2, 64 + x * 80);
							levelassets.add(joe);
						}
						ObjectManager.player.setCoords(150,200);
					}
					else if(level == 6)
					{
						mainWindowGameComponent.bannerText = "6 - pew(hes immune to red btw)";
						levelendportal = new LevelEnd(mainWindow.width/2,mainWindow.height-100);
						levelassets.add(levelendportal);
						TurretEnemy jim = new TurretEnemy();
						jim.setCoords(mainWindow.width/2  + 250, mainWindow.height/2 + 150);
						levelassets.add(jim);
						ObjectManager.player.setCoords(mainWindow.width-150,mainWindow.height/2);
					}
					else if(level == 7)
					{
						mainWindowGameComponent.bannerText = "7 - firing squad";
						levelendportal = new LevelEnd(mainWindow.width-300,mainWindow.height/2);
						levelassets.add(levelendportal);
						for(int x = 0;x < 15;x++)
						{
							TurretEnemy jim = new TurretEnemy();
							jim.setCoords(mainWindow.width/2 + x * 64, 64 + x * 75);
							levelassets.add(jim);
						}
						ObjectManager.player.setCoords(150,mainWindow.height-150);
					}
					else if(level == 8)
					{
						mainWindowGameComponent.bannerText = "8 - its the last level so its just a sh_t ton of enemies";
						levelendportal = new LevelEnd(mainWindow.width-150,150); 
						levelassets.add(levelendportal);
						for(int x = 0;x < 20;x++)
						{
							BasicFloatingEnemy joe = new BasicFloatingEnemy();
							joe.setCoords(mainWindow.width/2, 64 + x * 80);
							levelassets.add(joe);
						}
						for(int x = 0;x < 20;x++)
						{
							TurretEnemy jim = new TurretEnemy();
							jim.setCoords(mainWindow.width - 250 + x * 64, 64 + x * 75);
							levelassets.add(jim);
						}
						ObjectManager.player.setCoords(150,mainWindow.height-150);
					}
					loading = false;
				}			
			};	
			Thread t = new Thread(levelloader);
			t.start();
		}
	}
	static void sleep(int mili)
	{
		try {
			Thread.sleep(mili);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
