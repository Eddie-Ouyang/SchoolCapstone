package Combat.projectiles;
import java.util.HashSet;

import Combat.physics.Hitbox;
import engine.Globals;
import processing.core.*;

/**
 * 
 * @author prathamhebbar
 *
 */
public abstract class Projectile implements Hitbox {
	
	protected float x, y;
	protected int life;
	protected int damage;
	protected float speed;
	protected float angle;
	protected int pierce;
	protected boolean friendly;
	protected boolean dead;
	
	private HashSet<Hitbox> collidedHitboxes;
	
	/**
	 * Creates a Projectile
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param life total lifetime of the projectile (frames)
	 * @param damage damage of the projectile
	 * @param speed speed of the projectile (pixels/frame)
	 * @param angle angle of the projectile
	 * @param pierce how many hitboxes this projectile can impact before disappearing
	 * @param friendly whether or not this projectile came from the player or the enemy
	 */
	public Projectile(float x, float y, int life, int damage, float speed, float angle, int pierce, boolean friendly) {
		this.x = x;
		this.y = y;
		this.life = life;
		this.damage = damage;
		this.speed = speed; 
		this.angle = angle;
		this.pierce = pierce; 
		this.friendly = friendly;
		this.dead = false;
		
		this.collidedHitboxes = new HashSet<Hitbox>();
	}
	
	/**
	 * @return remaining life of this projectile
	 */
	public int getLife() {
		return life;
	}
	
	/**
	 * @return Speed of this projectile
	 */
	public float getSpeed() {
		return speed;
	}
	
	/**
	 * @return damage of this projectile
	 */
	public int getDamage() {
		return damage;
	}
	
	/**
	 * @param velocity new speed of the projectile
	 */
	public void setVelocity(int velocity) {
		this.speed = velocity;
	}
	
	/**
	 * @return current speed of the projectile
	 */
	public double getVelocity() {
		return speed;
	}
	
	/**
	 * next tick of the projectile's movement
	 * @param destinationX x coordinate which the projectile is shot at
	 * @param destinationY y coordinate which the projectile is shot at
	 */
	public void move(int destinationX, int destinationY) {
		
		while (x != destinationX && y != destinationY) {
			shoot(destinationX, destinationY);
		}
	}
	
	/**
	 * simulate collision with another hitbox 
	 * @param H hitbox which is collided with
	 * @return true if this projectile has successfully hit the hitbox, false if not
	 */
	public boolean handleCollision (Hitbox H) {
		if (collidedHitboxes.contains(H)) return false;
		
		collidedHitboxes.add(H);
		this.pierce--;
		if (this.pierce == 0) this.kill();
		return true;
	}
	
	/**
	 * kills this projectile
	 */
	public void kill () {
		this.dead = true;
	}
	
	/**
	 * Shoots a projectile at a certain target coordinate
	 * @param destinationX target x coordinate
	 * @param destinationY target y coordinate
	 */
	public void shoot(int destinationX, int destinationY) {
		
		if (x != destinationX && y != destinationY) {
			int A = (int) Math.pow(Math.abs(destinationX - x), 2);
			int B = (int) Math.pow(Math.abs(destinationY - y), 2);
			int C = (int) Math.sqrt(A + B);
			
			double angle = Math.atan(B/A);
			
			x += Math.cos(angle) * speed;
			y += Math.sin(angle) * speed;
		}
	}
	
	/**
	 * Draws this projectile on the surface
	 * @param surface PApplet drawing surface
	 */
	public void draw(PApplet surface) {
		this.x += this.speed * Math.cos(this.angle);
		this.y += this.speed * Math.sin(this.angle);
		life--;
		if (life <= 0) this.kill();
	}
	
	/**
	 * @return x coordinate
	 */
	public float getX () {
		return this.x;
	}
	/**
	 * @return y coordinate
	 */
	public float getY () {
		return this.y;
	}
	/**
	 * @return current angle
	 */
	public float getAngle () {
		return this.angle;
	}
	/**
	 * @return true if the projectile is shot from the player, false otherwise
	 */
	public boolean isFriendly () {
		return this.friendly;
	}
	/**
	 * @return true if the projectile is still alive, false otherwise
	 */
	public boolean isDead () {
		return this.dead;
	}
	

	@Override
	public float getHitboxRadius () { return 20; } // TODO: maybe change
	@Override
	public float getHitboxCenterX () { return this.x; }
	@Override
	public float getHitboxCenterY () { return this.y; }
}
