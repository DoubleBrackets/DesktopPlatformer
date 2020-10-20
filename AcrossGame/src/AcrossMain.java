import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.awt.GraphicsDevice;
import Graphicals.mainWindow;
import controller.keyBinder;
import gameObjects.ObjectManager;
import gameObjects.ScreenToGameEnvironment;

public class AcrossMain extends Thread{
	static mainWindow gameWindow;
	public static ObjectManager objManager;
	public static int gameTickRate = 15;//miliseconds per tick
	public static int renderTickRate = 16;
	public static int environmentUpdateInterval = 100;//Time between each environment update
	public static void main(String[] args) throws IOException
	{
		init();
	}
	private static void init() throws IOException
	{
		//Checks for requireements for transparent window
		//Creates main game window, window also starts keybindings
		checkTransparencyCompat();
		gameWindow = new mainWindow();
		//Starts object manager
		objManager = new ObjectManager();
		//Starts game loop
		AcrossMain gameMain = new AcrossMain();
		gameMain.start();
		ScreenToEnvironmentLoop();
		//screen rendering loop
		RenderScreenLoop();
	}
	//Main game loop
	public void run()
	{
		ScreenToGameEnvironment.UpdateEnvironment(true);
		while(true)
		{
			keyBinder.keyBindUpdate();
			ObjectManager.updateObjects();
			try {
				Thread.sleep(gameTickRate);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	//Rendering screen loop
	public static void RenderScreenLoop()
	{
		Runnable screenR = new Runnable()
		{
			public void run(){
				mainWindow.WindowComp.grabFocus();
				while(true)
				{
					try {
						Thread.sleep(1);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(!mainWindow.hasFocus && mainWindow.autoFocus)
					{
						mainWindow.WindowComp.grabFocus();
					}
					//Repaints the screen
					mainWindow.Window.repaint();
					//Delay
					try {
						Thread.sleep(gameTickRate);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		};
		Thread renderThread = new Thread(screenR);
		renderThread.start();
	}
	//Updating the environment loop
	public static void ScreenToEnvironmentLoop()
	{
		Runnable ste = new Runnable()
		{
			public void run(){
				while(true)
				{
					ScreenToGameEnvironment.UpdateEnvironment(true);
					//Delay
					try {
						Thread.sleep(environmentUpdateInterval);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		Thread renderThread = new Thread(ste);
		renderThread.start();
	}
	//Checks if os can run transparent windows
	private static void checkTransparencyCompat()
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		boolean isUniformTranslucencySupported =
		gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT);
		boolean isPerPixelTranslucencySupported =
		gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT);
		boolean isShapedWindowSupported =
		gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSPARENT);
		//System.out.println("" + (isUniformTranslucencySupported && isPerPixelTranslucencySupported && isShapedWindowSupported));
		if(!(isUniformTranslucencySupported && isPerPixelTranslucencySupported && isShapedWindowSupported))
		{
			System.out.println("Graphic translucency not supporteed !");
			System.exit(0);
		}
	}
}
