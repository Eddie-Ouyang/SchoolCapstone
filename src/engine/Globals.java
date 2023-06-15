package engine;

import java.util.*;

import Combat.enemies.*;
import Combat.player.*;
import Combat.projectiles.*;
import processing.core.*;

public class Globals {
	
	public static final boolean DEBUG_MODE = false;
	public static int CURRENT_FRAME;
	public static int TIME;
	public static int fuel;
	public static float SCREEN_W;
	public static float SCREEN_H;
	public static int DIFFICULTY;
	public static int KILLS;
	public static final int PLAYER_SIZE = 420;
	public static final int PLAYER_MODEL_COLS = 21;
	public static final int PLAYER_MODEL_ROWS = 21;
	public static final int POSITION_SIZE = PLAYER_SIZE / PLAYER_MODEL_COLS;
	public static final int FRAMES_PER_WAVE = 1500 - 30 * DIFFICULTY; // 25s - (0.5s * difficulty)
	public static final int FRAMES_PER_LIFE = 3600;
	public static final int MAX_RANGE = 2000;
	public static final int WARP_RANGE = 1200;
	public static final int[] threshold = new int[] {100,60,40};
	public static int[] resources = new int[4];
	
	public static PriorityQueue<DelayedProjectile> PROJECTILE_QUEUE = new PriorityQueue<DelayedProjectile>(); // for semi-auto turrets
	
	/** 
	 * initializes resources at the start of a new game
	 */
	public static void newGame() {
		DIFFICULTY = 0;
		KILLS = 0;
		resources = new int[] {0,0,0,6666,0};
	}
	
	/**
	 * @return remaining time until redemption
	 */
	public static float timer() {
		return TIME<FRAMES_PER_LIFE?1-(1f*TIME/FRAMES_PER_LIFE):0;
	}
	
	public static String layerText() {
		switch(DIFFICULTY) {
		case 0: return "1st";
		case 1: return "2nd";
		case 2: return "3rd";
		default: return (DIFFICULTY+1)+"th";
		}
	}
	
	/**
	 * Draws debug info if debug mode is enabled
	 * @param surface PApplet drawing surface
	 * @param O object to debug
	 * @param xDisp x coordinate to display debug info
	 * @param yDisp y coordinate to display debug info
	 */
	public static void drawDebugInfo (PApplet surface, Object O, float xDisp, float yDisp) {
		String debugInfo = "";
		
		if (O instanceof Player) {
			Player P = (Player) O;
			debugInfo += "x: " + Player.x + '\n';
			debugInfo += "y: " + Player.y + '\n';
			debugInfo += "angle: " + Player.angle  + '\n';
			debugInfo += "cw torque: " + P.getClockwiseTorque(P.getAllThrusterPositions()) + '\n';
			debugInfo += "ccw torque: " + P.getCounterclockwiseTorque(P.getAllThrusterPositions()) + '\n';
		}
		if (O instanceof Projectile) {
			Projectile P = (Projectile) O;

			debugInfo += "x: " + P.getX() + '\n';
			debugInfo += "y: " + P.getY() + '\n';
			debugInfo += "angle: " + P.getAngle();
		}
		if (O instanceof Enemy) {
			Enemy P = (Enemy) O;
			
			debugInfo += "x: " + P.X + '\n';
			debugInfo += "y: " + P.Y + '\n';
			debugInfo += "angle: " + P.angle;
			debugInfo += "target: " + P.getController();
		}
		
		surface.pushStyle();
		surface.fill(255);
		surface.strokeWeight(2);
		surface.text(debugInfo, xDisp, yDisp);
		surface.popStyle();
	}
}
