package Graphicals;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.text.Position;

import controller.keyBinder;
import gameObjects.ObjectManager;
import gameObjects.ScreenToGameEnvironment;
import gameObjects.gameObjectPhysics;


public class mainWindow extends JFrame
{
		//Window and Frame
		static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		public static int width = (int) screenSize.getWidth(), height = (int)screenSize.getHeight();
		public static boolean hasFocus = true;
		public static mainWindow Window;
		public static mainWindowGameComponent WindowComp;
		public static boolean autoFocus = true;
		//For Panel
		public static mainWindowGameComponent gamePanel;
		//keybindings
		public static keyBinder keyBind;
		//snapshot
		static Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		public static Robot robot;
		public mainWindow()
		{
			super("UwU");
			//Panel Creation
			gamePanel = new mainWindowGameComponent(width,height);
			gamePanel.setPreferredSize(new Dimension(width,height));
			Window = this;
			WindowComp = gamePanel;
			//Frame
			setSize(width,height);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setAlwaysOnTop(true);
			setFocusableWindowState(true);
			//KeyBindings
			keyBind = new keyBinder();
			gamePanel.addKeyListener(keyBind);
			FocusDetector fd = new FocusDetector();
			//Screen capturing
			screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			try {
				robot = new Robot();
			} catch (AWTException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			gameObjectPhysics.setBounds(0, width, height, 0);
			//Deco
			setUndecorated(true);
			setBackground(new Color(0,0,0,0));
			setOpacity(1);
			//Finish
			add(gamePanel);
			pack();
			setVisible(true);
		}
		public void paint(Graphics g)
		{
			//Point pos = MouseInfo.getPointerInfo().getLocation();
			//Color clr = robot.getPixelColor(pos.x,pos.y);
			//System.out.println(clr);
			super.paint(g);
			super.paintComponents(g);
		}
		static public BufferedImage GetTerrainScreenshot()
		{
			BufferedImage capture;
			//gamePanel.shouldRenderGame = false;
			//mainWindow.Window.repaint();
			capture = robot.createScreenCapture(screenRect);
			ObjectManager.savePositions();
			//gamePanel.shouldRenderGame = true;
			return capture;
		}
		static public Color GetPixelColorAt(int x, int y)
		{
			return robot.getPixelColor(x, y);
		}
		
}
//Detects if in focus or not 
class FocusDetector implements FocusListener
{
	FocusDetector()
	{
		mainWindow.gamePanel.addFocusListener(this);
	}

	@Override
	public void focusGained(FocusEvent e) {
		mainWindow.hasFocus = true;
		mainWindow.gamePanel.requestFocusInWindow();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		mainWindow.autoFocus = true;
	}

	@Override
	public void focusLost(FocusEvent e) {
		mainWindow.hasFocus = false;
		mainWindow.autoFocus = false;
		if(keyBinder.focusTiming)
			keyBinder.notifyFocusTimer();
	}
}
