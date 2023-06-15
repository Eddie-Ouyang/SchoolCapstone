package Combat.projectiles;

import engine.Globals;
import processing.core.PApplet;
import processing.core.PImage;
import screen.GameImage;

public class DemonProjectile extends Projectile {
	/**
	 * Creates a DemonProjectile 
	 * @param x x coordinate of projectile
	 * @param y y coordinate of projectile
	 * @param angle angle the projectile is facing
	 */
	public DemonProjectile(float x, float y, float angle) {
		super(x, y, 150, 10 + Globals.DIFFICULTY, 5, angle, 1, false);
	}
	
	@Override
	public void draw (PApplet surface) {
		super.draw(surface);
		
		if (life != 0) {
			super.draw(surface);
			surface.pushStyle();
			surface.fill(255, 0, 0);
			PImage sprite = GameImage.get("Bullet0");
			surface.image(sprite, this.x, this.y);
			if (Globals.DEBUG_MODE) Globals.drawDebugInfo(surface, this, x, y);
			surface.popStyle();
		}
	}
}
