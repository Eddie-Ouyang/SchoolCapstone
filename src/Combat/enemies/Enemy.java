package Combat.enemies;

import Combat.physics.Hitbox;
import Combat.player.*;
import Combat.projectiles.*;
import engine.*;
import processing.core.*;

/**
 * 
 * Represents a boilerplate enemy
 * @author prathamhebbar
 *
 */
public abstract class Enemy implements Hitbox {
	public float X, Y;
	public float angle;
	public int health, maxHealth;
	
	public boolean dead;
	
	protected EnemyAI controller;
	protected final int size = 64; // size of sprite
	
	/**
	 * Creates a new Enemy
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param health maximum health of the enemy
	 */
	public Enemy(float x, float y, int health) {	
		this.X = x;
		this.Y = y;
		this.health = health;
		maxHealth = health;
		
		this.dead = false;
	}
	
	/**
	 * Moves this enemy by a certain amount 
	 * @param xVel x velocity (pixels/frame)
	 * @param yVel y velocity (pixels/frame)
	 */
	public void move(int xVel, int yVel) {
		X -= xVel;
		Y -= yVel;
	}
	
	/**
	 * Rotates this enemy by a certain amount
	 * @param ROTATE_BY rotation amount (radians)
	 */
//	public void rotate(double ROTATE_BY) {
//		
//		double fullRevolutions = ROTATE_BY / 180;
//		double partialRevolution = ROTATE_BY % 180;
//		
//		double fullRevolution = Math.PI * 2;
//		
//		double total;
//		
//		total = (fullRevolution * fullRevolutions);
//		total += (partialRevolution / fullRevolution);
//		
//		angle += total;
//	}
	
	/**
	 * @return health
	 */
	public double getHealth() {
		return health;
	}
	
	/**
	 * @param health new health
	 */
	public void setHealth(int health) {
		this.health = health;
	}
	
	/**
	 * Damages this enemy and kills it if health goes below 0
	 * @param proj projectile that hits the enemy
	 */
	public void takeDamage (Projectile proj) {
		this.health -= proj.getDamage();
		if (health <= 0) this.kill();
	}
	
	/**
	 * Kills this enemy
	 */
	public void kill () {
		if (!dead) {
			this.dead = true;
			Globals.TIME -= 60;
			Globals.resources[3] += maxHealth/10;
			Globals.KILLS++;
		}
	}
	
	/**
	 * Use this enemy's AI controller to perform its next move 
	 */
	public void move () {
		if(Globals.CURRENT_FRAME%300 == 0 && Math.hypot(controller.getDeltaXPlayer(), controller.getDeltaYPlayer()) > Globals.MAX_RANGE) {
			double angle = controller.getAngleToPlayer() + (Math.random()/4 +1)*Math.PI;
			double dist = (Math.random()/4+1)*Globals.WARP_RANGE;
			X = Player.x-(float)(dist*Math.cos(angle));
			Y = Player.y-(float)(dist*Math.sin(angle));
			controller.currentRoutine = controller.nextRoutine();
		}
		this.controller.move();
	}
	
	/**
	 * Rotates toward the player by 0.05 radians / frame
	 */
//	public void turn () {
//		float rotationAmount = 0.05f;
//		
//		float desiredAngle = this.controller.getAngleToPlayer();
//		float curAngle = this.angle;
//		float deltaAngle = (float)Math.atan2(Math.sin(desiredAngle-curAngle), Math.cos(desiredAngle-curAngle));
//		
//		if (Math.abs(deltaAngle) < rotationAmount * 2) {
//			this.angle = desiredAngle;
//		}
//		else if (deltaAngle > rotationAmount * 2) {
//			this.angle += rotationAmount;
//		}
//		else if (deltaAngle < -rotationAmount * 2){
//			this.angle -= rotationAmount;
//		}
//	}
	
	/**
	 * Draws this enemy 
	 * @param surface PApplet drawing surface
	 */
	public void draw(PApplet surface) {
		if (!dead) {
			move();
			healthBar(surface);
			this.shoot();
		}
	}
	
	private void healthBar(PApplet surface) {
		float percentage = 1f*health/maxHealth;
		
		surface.pushStyle();
		surface.colorMode(PApplet.HSB,360,1,1,1);
		surface.fill(50);
		surface.rect(X,Y+size, size, size/10);
		surface.fill(120*percentage,1,1);
		surface.rect(X,Y+size,size*percentage,size/10);
		surface.popStyle();
	}
	
	/**
	 * @return this enemy's AI controller
	 */
	public EnemyAI getController () {
		return this.controller;
	}
	
	/**
	 * Makes this enemy fire a projectile at the player
	 */
	public abstract void shoot();
	
	
	@Override
	public float getHitboxRadius () { return size/2; } 
	@Override
	public float getHitboxCenterX () { return this.X + size/2; }
	@Override
	public float getHitboxCenterY () { return this.Y + size/2; }
}
