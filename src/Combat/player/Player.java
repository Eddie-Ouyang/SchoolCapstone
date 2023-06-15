package Combat.player;

import java.util.ArrayList;

import Combat.physics.Hitbox;
import Combat.projectiles.Projectile;
import construct.*;
import engine.*;
import processing.core.PApplet;
import screen.*;
import terra.Base;

/**
 * 
 * Represents a player
 * @author prathamhebbar
 *
 */
public class Player implements Hitbox {
	public static float x, y;
	public static float vx, vy;
	public static float angle;
	public static int r0,rM,c0,cM;

	private int screenX, screenY;
	private PlayerModel playerModel;
	private DamageModel damageModel;
	
	public Player(float x, float y) {
		Globals.fuel = 0;
		Player.x = x;
		Player.y = y;
		
		angle = 0;
		
		r0 = 99;
		c0 = 99;
		rM = -99;
		cM = -99;
		
		playerModel = new PlayerModel(Base.tiles);
		damageModel = new DamageModel(playerModel.positions);
	}
	
	/**
	 * @return position array of the ship
	 */
	public Position[][] getShip(){
		return playerModel.positions;
	}
	
	/**
	 * Draws the player on screen
	 * @param surface PApplet drawing surface
	 */
	public void draw (PApplet surface) {
		drawSelf(surface);
	}
	
	/**
	 * Draws the player's damage model on screen
	 * @param display PApplet drawing surface
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param w width 
	 * @param h height
	 */
	public void drawModel(Game display, float x, float y, float w, float h) {
		damageModel.draw(display, x,y,w,h);
	}
	
	private void drawSelf (PApplet surface) {
		screenX = surface.width / 2;
		screenY = surface.height / 2;
		
		surface.push();
		surface.translate(screenX, screenY);
		surface.rotate(Player.angle);
		
		// 0, 0 becomes "top left" of player, angle can be ignored 
		surface.pushMatrix();
		surface.translate(-Globals.PLAYER_SIZE / 2f, -Globals.PLAYER_SIZE / 2f);
		playerModel.draw(surface); // draws the mini towers 
		surface.popMatrix();
		

		if (Globals.DEBUG_MODE) this.drawDebug(surface);
		surface.pop();
	}
	
	/**
	 * Turns towards a direction
	 * @param direc desired direction
	 */
	public void turn(int direc) {
		if(direc == 0)return;
		float momentOfInertia = this.getMomentOfInertia();
		ArrayList<Position> allThrusterPositions = this.getAllThrusterPositions();
		float cwRads = this.getClockwiseTorque(allThrusterPositions) / momentOfInertia;
		float ccwRads = this.getCounterclockwiseTorque(allThrusterPositions) / momentOfInertia;
		
		float defaultRotationAmount = 0.005f;
		
		cwRads += defaultRotationAmount;
		ccwRads += defaultRotationAmount;
		
		if(direc == 1) Player.angle += cwRads;
		else Player.angle -= ccwRads;
		
		Player.angle %= 2 * Math.PI;
		while (Player.angle < 0) Player.angle += 2 * Math.PI;
	}
	
	/**
	 * Turns towards the desired direction, takes moment of inertia and net torques into account
	 * @param direc desired direction
	 */
	public void turnTowards (float mouseX, float mouseY) {
		float momentOfInertia = this.getMomentOfInertia();
		ArrayList<Position> allThrusterPositions = this.getAllThrusterPositions();
		float cwRads = this.getClockwiseTorque(allThrusterPositions) / momentOfInertia;
		float ccwRads = this.getCounterclockwiseTorque(allThrusterPositions) / momentOfInertia;
		
		float defaultRotationAmount = 0.005f;
		
		cwRads += defaultRotationAmount;
		ccwRads += defaultRotationAmount;
		
		float targetAngle = (float)(Math.atan2(mouseY - screenY, mouseX - screenX) + Math.PI);		
		float deltaAngle = (float)Math.atan2(Math.sin(targetAngle-Player.angle), Math.cos(targetAngle-Player.angle));
		

		if(Math.abs(deltaAngle) < Math.min(cwRads, ccwRads)) {
			Player.angle = targetAngle;
		} else {
			if (deltaAngle > 0) {
				if (deltaAngle > cwRads) Player.angle += cwRads;
				else Player.angle = targetAngle;
			}
			if (deltaAngle < 0){
				if (deltaAngle < -ccwRads) Player.angle -= ccwRads;
				else Player.angle = targetAngle;
			}
		}
		Player.angle %= 2 * Math.PI;
		while (Player.angle < 0) Player.angle += 2 * Math.PI;
	}
	
	/**
	 * Moves the player by a certain amount (1 frame only)
	 * @param dx x movement
	 * @param dy y movement
	 * @param w 
	 */
	public void move (int dx, int dy, int boosting) {
		float defaultForce = 50;
		if(boosting == 1) {
			if(Globals.resources[4] >= Globals.fuel/40) {
				defaultForce += getTotalReactorForce();
				Globals.resources[4] -= Globals.fuel/40;
			} else {
				defaultForce += Globals.resources[4]/Globals.fuel/120*getTotalReactorForce();
				Globals.resources[4] = 0;
			}
		}
		int movementAmount = (int)((getTotalReactorForce() + defaultForce) / getTotalMass()); 
		
		Player.vy = dy * movementAmount;
		Player.vx = dx * movementAmount;
		Player.y += Player.vy;
		Player.x += Player.vx;
	}
	
	/**
	 * Makes all the player's towers attack
	 */
	public void attack () {
		for (int i = r0; i <= rM; i++) {
			for (int j = c0; j <= cM; j++) {
				Position pos = playerModel.positions[i][j];
				if (pos.tower == null || pos.tower.dead()) continue;
				pos.tower.attack(pos.getMapX(), pos.getMapY(), Player.angle - (float)Math.PI); // since player default angle is to the left
			}
		}
	}
	
	/**
	 * Inflicts damage on the player
	 * @param angle angle at which the projectile hits
	 * @param proj projectile that hits the player
	 */
	public void takeDamage (float angle, Projectile proj) {
		damageModel.takeDamage(proj);
	}
	
	/**
	 * @return player's health
	 */
	public float getHealth() {
		return playerModel.getHealth();
	}
	
	/**
	 * Simulates the resource production of production towers
	 */
	public void farm() {
		for (int i = r0; i <= rM; i++) {
			for (int j = c0; j <= cM; j++) {
				Tower t = playerModel.positions[i][j].tower;
				if(t == null)continue;
				if(t instanceof Farm) ((Farm)t).farm();
				else if (t instanceof Reactor) ((Reactor)t).farm();
			}
		}
	}
	
	private float getMomentOfInertia () {
		float total = 0.01f;
		for (int i = r0; i <= rM; i++) {
			for (int j = c0; j <= cM; j++) {
				Position pos = playerModel.positions[i][j];
				if (pos.tower == null || pos.tower.dead() || !pos.active) continue;
				float r = (float)Math.hypot(pos.r - Globals.PLAYER_MODEL_ROWS/2f, pos.c - Globals.PLAYER_MODEL_COLS/2f);
				total += r * r * pos.tower.getWeight();
			}
		}
		return total;
	}
	
	/** 
	 * @return ArrayList of all the Positions that contain thrusters
	 */
	public ArrayList<Position> getAllThrusterPositions () {
		ArrayList<Position> output = new ArrayList<Position>();
		for (int i = r0; i <= rM; i++) {
			for (int j = c0; j <= cM; j++) {
				Position pos = playerModel.positions[i][j];
				if (pos.tower == null || pos.tower.dead() || !pos.active) continue;
				if (pos.tower instanceof Thruster) output.add(pos);
			}
		}
		return output;
	}
	
	/**
	 * @param thrusterPositions list of all positions which contain thrusters
	 * @return total clockwise torque that can be provided with the given thrusters
	 */
	public float getClockwiseTorque (ArrayList<Position> thrusterPositions) {
		float netTorque = 0;
		for (Position pos : thrusterPositions) {
			float angleTo = (float)(Math.atan2(pos.r - Globals.PLAYER_MODEL_ROWS/2, pos.c - Globals.PLAYER_MODEL_COLS/2) + Math.PI);
			float curAngle = pos.tower.getDirection() * (float)Math.PI/2;
			float distTo = (float)Math.hypot(pos.r - Globals.PLAYER_MODEL_ROWS/2, pos.c - Globals.PLAYER_MODEL_COLS/2) * Globals.POSITION_SIZE;
			
			float deltaAngle = (float)Math.atan2(Math.sin(curAngle-angleTo), Math.cos(curAngle-angleTo));
			float crossProduct = ((Thruster)pos.tower).getForce() * distTo * (float)Math.sin(deltaAngle);
			
			
			if (crossProduct > 0) netTorque += crossProduct;
		}
		return netTorque;
	}
	
	/**
	 * @param thrusterPositions list of all positions which contain thrusters
	 * @return total counterclockwise torque that can be provided with the given thrusters
	 */
	public float getCounterclockwiseTorque (ArrayList<Position> thrusterPositions) {
		float netTorque = 0;
		for (Position pos : thrusterPositions) {
			float angleTo = (float)(Math.atan2(pos.r - Globals.PLAYER_MODEL_ROWS/2, pos.c - Globals.PLAYER_MODEL_COLS/2) + Math.PI);
			float curAngle = pos.tower.getDirection() * (float)Math.PI/2;
			float distTo = (float)Math.hypot(pos.r - Globals.PLAYER_MODEL_ROWS/2, pos.c - Globals.PLAYER_MODEL_COLS/2) * Globals.POSITION_SIZE;
			
			
			float deltaAngle = (float)Math.atan2(Math.sin(curAngle-angleTo), Math.cos(curAngle-angleTo));
			float crossProduct = ((Thruster)pos.tower).getForce() * distTo * (float)Math.sin(deltaAngle);
			
			
			if (crossProduct < 0) netTorque += Math.abs(crossProduct);
		}
		return netTorque;
	}
	
	/**
	 * @return total force that the ship's reactors can provide
	 */
	public float getTotalReactorForce () {
		float totalForce = 0;
		Position allPos[][] = playerModel.positions;
		for (int i = r0; i <= rM; i++) {
			for (int j = c0; j <= cM; j++) {
				Position pos = allPos[i][j];
				if (!pos.active || pos.tower == null || pos.tower.dead()) continue;
				if (pos.tower instanceof Reactor) totalForce += ((Reactor)pos.tower).getForce();
			}
		}
		return totalForce;
	}
	
	/**
	 * @return total mass of the entire ship
	 */
	public float getTotalMass () {
		float totalMass = 0;
		Position allPos[][] = playerModel.positions;
		for (int i = r0; i <= rM; i++) {
			for (int j = c0; j <= cM; j++) {
				Position pos = allPos[i][j];
				if (!pos.active || pos.tower == null || pos.tower.dead()) continue;
				totalMass += pos.tower.getWeight();
			}
		}
		return totalMass;
	}
	
	
	@Override
	public float getHitboxRadius() {
		float maxDist = Globals.POSITION_SIZE/2f; // if only core
		
		for (int i = r0; i <= rM; i++) {
			for (int j = c0; j <= cM; j++) {
				Position P = playerModel.positions[i][j];
				if (!P.active) continue;
				maxDist = Math.max(maxDist, (float)Math.hypot(P.getRelativeX() - Globals.PLAYER_SIZE/2f, P.getRelativeY() - Globals.PLAYER_SIZE/2f));
			}
		}
		
		return maxDist;
	}
	@Override
	public float getHitboxCenterX() { return Player.x; }
	@Override
	public float getHitboxCenterY() { return Player.y; }
	
	
	private void drawDebug (PApplet surface) {
		surface.pushStyle();
		surface.stroke(255, 0, 0);
		surface.strokeWeight(5);
		surface.line(0, 0, -100, 0); 
		Globals.drawDebugInfo(surface, this, 0, 0);
		surface.popStyle();
	}
}

