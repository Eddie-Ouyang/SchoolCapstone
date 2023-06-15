package Combat.projectiles;

import Combat.player.Player;
import engine.Globals;
import processing.core.PApplet;
import processing.core.PImage;
import screen.GameImage;

public class GearProjectile extends Projectile {
	/**
	 * Creates a GearProjectile 
	 * @param x x coordinate of projectile
	 * @param y y coordinate of projectile
	 * @param angle angle the projectile is facing
	 */
	public GearProjectile(float x, float y, float angle) {
		super(x, y, 150, 20 + Globals.DIFFICULTY, 5, angle, 1, false);
	}
	
	@Override
	public void draw (PApplet surface) {
		super.draw(surface);
		
		if (life != 0) {
			super.draw(surface);
			surface.pushStyle();
			surface.fill(255, 0, 0);
			PImage sprite = GameImage.get("Bullet1");
			surface.image(sprite, x, y);
			if (Globals.DEBUG_MODE) Globals.drawDebugInfo(surface, this, x, y);
			surface.popStyle();
			turnTowardsPlayer();
		}
	}
	
	private void turnTowardsPlayer () {
		if(Math.hypot(Player.y-y, Player.x-x) > 200) return;
		float rotationAmount = 0.1f;
		float EPSILON = rotationAmount * 5;
		
		float targetAngle = (float)(Math.atan2(Player.y - this.y, Player.x-this.x));
		float deltaAngle = (float)Math.atan2(Math.sin(targetAngle-this.angle), Math.cos(targetAngle-this.angle));
		
		if (Math.abs(deltaAngle) <  EPSILON) {
			this.angle = targetAngle;
		}
		else if (deltaAngle > EPSILON) {
			this.angle += rotationAmount;
		}
		else if (deltaAngle < -EPSILON){
			this.angle -= rotationAmount;
		}
	}
}
