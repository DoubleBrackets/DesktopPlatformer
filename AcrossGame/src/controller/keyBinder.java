package controller;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.KeyStroke;

import Graphicals.mainWindow;
import gameObjects.ObjectManager;
import gameObjects.ScreenToGameEnvironment;
public class keyBinder implements KeyListener{
	public static boolean a_active = false,d_active = false, space_active,s_active = false;
	public static int jumpCounter = 0;
	static Robot bot;
	public static Runnable Ste;
	static Thread SteThread;
	public static boolean focusTiming = false;
	static int playerFacing = 0;
	private static long s_timer = 1;
	private static long s_interval = 200;
	public static boolean inputActive = true;
	public static boolean showHitboxes = false;
	public keyBinder()
	{
		try {
			bot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void keyBindUpdate()
	{
		if(inputActive)
		{
			if(a_active)
			{
				playerFacing = -1;
				if(ObjectManager.player.getXVel() >= -6 && ObjectManager.player.getXVel() <= 0 )
					ObjectManager.player.setXVel(-6);
				else if (ObjectManager.player.getXVel() > 0)
					ObjectManager.player.setXVel(ObjectManager.player.getXVel() - 6);
				ObjectManager.player.setAnimation(1);
			}
			if(d_active)
			{
				playerFacing = 1;
				if(ObjectManager.player.getXVel() >= 0 && ObjectManager.player.getXVel() <= 6 )
					ObjectManager.player.setXVel(6);
				else if (ObjectManager.player.getXVel() < 0)
					ObjectManager.player.setXVel(ObjectManager.player.getXVel() + 6);
				ObjectManager.player.setAnimation(0);
			}
			if(space_active && jumpCounter < 1)
			{
				ScreenToGameEnvironment.shouldUpdateTerrain = false;
				space_active = false;
				jumpCounter++;
				ObjectManager.player.setYVel(20);
			}
			//Dropping through platforms garuntees dropping for at least a certain period of time
			s_timer -= 30 ;
			if(s_timer <= 0)
			{
				s_timer = 0;
				if(s_active && ObjectManager.player.isDroppingThroughPlatforms == false)
				{
					s_timer = s_interval;
					ObjectManager.player.isDroppingThroughPlatforms = true;
				}
				else if(ObjectManager.player.isDroppingThroughPlatforms == true && s_active == false)
				{
					s_timer = s_interval;
					ObjectManager.player.isDroppingThroughPlatforms = false;
				}
			}
		}		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_SPACE && !space_active)
		{
			space_active = true;
		}
		if(key == KeyEvent.VK_A && !a_active)
		{
			a_active = true;
		}
		if(key == KeyEvent.VK_S)
		{
			s_active = true;
		}
		if(key == KeyEvent.VK_D && !d_active)
		{
			d_active = true;
		}
		if(key == KeyEvent.VK_W)
		{
			//showHitboxes = true;
		}
		if(key == KeyEvent.VK_Q)
		{
			focusTimer();
			moveMouseToPlayer();			
		    bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		    bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		}	
		if(key == KeyEvent.VK_E)
		{
			focusTimer();
			moveMouseToPlayer();
		    bot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
		    bot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
		}	
	}
	private void moveMouseToPlayer()
	{
		for(int x = 0;x < 3;x++)
			bot.mouseMove(ObjectManager.player.getX() + ObjectManager.player.getWidth()/2+playerFacing * 30, ObjectManager.player.getY() + ObjectManager.player.getHeight()/2+15);  
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == InputEvent.BUTTON1_DOWN_MASK)
		{
			mainWindow.autoFocus = false;
		}
		if(key == KeyEvent.VK_SPACE)
		{
			space_active = false;
		}
		if(key == KeyEvent.VK_A && a_active)
		{
			a_active = false;
		}
		if(key == KeyEvent.VK_S)
		{
			s_active = false;
		}
		if(key == KeyEvent.VK_D && d_active)
		{
			d_active = false;
		}
		if(key == KeyEvent.VK_W)
		{
			//showHitboxes = false;
		}
		
	}
	public static void focusTimer()
	{
		if(!focusTiming)
		{
			focusTiming = true;
			Ste = new Runnable()
			{
				public void run()
				{
					while(focusTiming)
					{
						//Delay
						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}				
					}
					mainWindow.autoFocus = true;
					mainWindow.gamePanel.grabFocus();
					focusTiming = false;
				}							
			};
			SteThread = new Thread(Ste);
			SteThread.start();
		}		
	}
	public static void notifyFocusTimer()
	{
		focusTiming = false;
	}
}
