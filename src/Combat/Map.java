package Combat;

import java.util.*;

import Combat.enemies.AngelEnemy;
import Combat.enemies.Enemy;
import Combat.physics.Hitbox;
import Combat.physics.PhysicsEngine;
import Combat.player.Player;
import Combat.projectiles.Projectile;
import engine.Globals;
import processing.core.PApplet;
import processing.core.PImage;


/**
 * 
 * @author prathamhebbar
 *
 */
public class Map {

	private PImage bg;
	private boolean joeved;
	
	public static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	public static ArrayList<Projectile> activeProjectiles = new ArrayList<Projectile>();
	private Hangar hangar;
	
	/**
	 * Creates a new Map instance
	 * @param p background image
	 * @param h target hangar which appears on the Map
	 */
	public Map (PImage p, Hangar h) {
		bg = p;
		enemies.clear();
		activeProjectiles.clear();
		joeved = false;
		hangar = h;
	}
	
	/**
	 * @return angle to the hangar
	 */
	public float interceptAngle() {
		return hangar.getAngle();
	}
	
	/** 
	 * kills player after they run out of time
	 * @param r radius of enemies spawned
	 */
	public void joever(float r) {
		joeved = true;
		enemies.clear();
		activeProjectiles.clear();
		double da = Math.PI*2/7;
		for(int i = 0; i < 7; i++) {
			enemies.add(new AngelEnemy(Player.x + r*(float)Math.cos(da*i+Math.PI/2), Player.y + r*(float)Math.sin(da*i+Math.PI/2)));
		}
	}
	
	/**
	 * @return whether or not the player is in the judgement screen
	 */
	public boolean joeved() {
		return joeved;
	}
	
	
	// ----- background stuff -----
	/**
	 * Draws the background
	 * @param surface PApplet drawing surface
	 */
	public void draw(PApplet surface) {
		if ((Globals.CURRENT_FRAME - 60) % Globals.FRAMES_PER_WAVE == 0) Waves.spawnRandomWave(interceptAngle());
		
//		surface.push();
//		surface.translate(-Player.x+surface.width/2, -Player.y+surface.height/2); // translate background to simulate movement of player
//		drawBackground(surface);
//		surface.pop();
	}
	private void drawBackground (PApplet surface) {
		int pX = (int)(Player.x/bg.width + 0.5*Math.signum(Player.x));
		int pY = (int)(Player.y/bg.height+ 0.5*Math.signum(Player.y));

		surface.image(bg,bg.width*(-1+pX), bg.height*(-1+pY));
		surface.image(bg,bg.width*(-1+pX), bg.height*(pY));
		surface.image(bg,bg.width*(pX), bg.height*(-1+pY));
		surface.image(bg,bg.width*(pX), bg.height*(pY));
	}
	
	// ----------------------------
	
	
	// ----- projectile stuff -----
	/**
	 * Draws all projectiles
	 * @param surface PApplet drawing surface
	 */
	public void drawProjectiles (PApplet surface) {
		surface.push();
		surface.translate(-Player.x+surface.width/2, -Player.y+surface.height/2);
		updateProjectiles();
		for (Projectile P : activeProjectiles) P.draw(surface);
		surface.pop();
	}
	private void updateProjectiles() {
		activeProjectiles.removeIf((n) -> (n.getLife() <= 0 || n.isDead()));
		
		while (Globals.PROJECTILE_QUEUE.size() > 0 && Globals.PROJECTILE_QUEUE.peek().frame <= Globals.CURRENT_FRAME) 
			activeProjectiles.add(Globals.PROJECTILE_QUEUE.poll().getProjectile());
	}
	// ----------------------------
	
	
	// ----- enemies -----
	/**
	 * Draws all enemies
	 * @param surface PApplet drawing surface
	 */
	public void drawEnemies (PApplet surface) {
		surface.push();
		surface.translate(-Player.x+surface.width/2, -Player.y+surface.height/2);
		enemies.removeIf((E) -> E.dead);
		for (Enemy E : enemies) E.draw(surface);
		surface.pop();
	}
	
	// -------------------
	
	
	// ----- physics -----
	/**
	 * Simulates one tick in the game
	 * @param player the player
	 * @pre should be called only from Battle.java
	 */
	public void stepPhysics (Player player) {
		for (Projectile proj : activeProjectiles) { 
			if (PhysicsEngine.isIntersecting(player, proj)) handlePlayerPhysics(player, proj);
		}
		for (Enemy entity : enemies) {
//			if (PhysicsEngine.isIntersecting(player, entity)) handlePlayerPhysics(player, entity); // this is really laggy for some reason

			for (Projectile proj : activeProjectiles) {
				if (PhysicsEngine.isIntersecting(entity, proj)) handleEnemyPhysics(entity, proj);
			}
			for (Enemy other : enemies) {
				if (other == entity) continue;
				if (PhysicsEngine.isIntersecting(entity, other)) handleEnemyPhysics(entity, other);
			}
		}
		
		
	}
	private void handlePlayerPhysics (Player P, Hitbox other) {
		if (other instanceof Projectile) {
			Projectile proj = (Projectile) other;
			if (proj.isFriendly()) return; 
			
			float angle = PhysicsEngine.angleBetween(P, other);
			
			P.takeDamage(angle, proj);
			proj.kill();
		}
		else if (other instanceof Enemy) {
			Enemy entity = (Enemy) other;
			
			float overlap = PhysicsEngine.intersectAmount(P, entity);
			
			float angle1 = PhysicsEngine.angleBetween(P, entity);
			Player.y += Math.cos(angle1) * overlap/2;
			Player.x += Math.sin(angle1) * overlap/2;
			
			float angle2 = PhysicsEngine.angleBetween(entity, P);
			entity.X += Math.cos(angle2) * overlap/2;
			entity.Y += Math.sin(angle2) * overlap/2;
		}
	}
	
	private void handleEnemyPhysics (Enemy E, Hitbox other) {
		if (other instanceof Projectile) {
			Projectile proj = (Projectile) other;
			if (!proj.isFriendly()) return;
			
			if (proj.handleCollision(E)) E.takeDamage(proj); 
		}
		else if (other instanceof Enemy) {
			Enemy entity = (Enemy) other;

			float overlap = PhysicsEngine.intersectAmount(E, entity);
			
			float angle1 = PhysicsEngine.angleBetween(E, entity);
			E.X += Math.cos(angle1) * overlap/2;
			E.Y += Math.sin(angle1) * overlap/2;
			
			float angle2 = PhysicsEngine.angleBetween(entity, E);
			entity.X += Math.cos(angle2) * overlap/2;
			entity.Y += Math.sin(angle2) * overlap/2;
		}
	}
	// -------------------
}
