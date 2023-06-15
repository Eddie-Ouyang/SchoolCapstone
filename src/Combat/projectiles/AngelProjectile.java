package Combat.projectiles;

import engine.Globals;
import processing.core.PApplet;
import processing.core.PImage;
import screen.GameImage;

public class AngelProjectile extends Projectile {
	/**
	 * Creates an AngelProjectile 
	 * @param x x coordinate of projectile
	 * @param y y coordinate of projectile
	 * @param angle angle the projectile is facing
	 */
	public AngelProjectile(float x, float y, float angle) {
		super(x, y, 2, 99, 0, angle, 10, false);
	}
	
	@Override
	public void draw (PApplet surface) {
		if (life != 0) {
			super.draw(surface);
			surface.push();
			surface.fill(255, 0, 0);
			surface.translate(x, y);
			surface.rotate(angle);
			PImage sprite = GameImage.get("Bullet3");
			surface.image(sprite, -sprite.width/2, -sprite.height/2);
			if (Globals.DEBUG_MODE) Globals.drawDebugInfo(surface, this, x, y);
			surface.translate(-x, -y);
			surface.pop();
		}
	}
}
