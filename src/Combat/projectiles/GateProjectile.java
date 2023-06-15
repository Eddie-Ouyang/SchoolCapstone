package Combat.projectiles;

import engine.Globals;
import processing.core.PApplet;
import processing.core.PImage;
import screen.GameImage;

public class GateProjectile extends Projectile {
	/**
	 * Creates a GateProjectile 
	 * @param x x coordinate of projectile
	 * @param y y coordinate of projectile
	 * @param angle angle the projectile is facing
	 */
	public GateProjectile(float x, float y, float angle) {
		super(x, y, 2, 2 + (int)Math.sqrt(Globals.DIFFICULTY / 2), 16, angle, 10, false);
	}
	
	@Override
	public void draw (PApplet surface) {
		if (life != 0) {
			super.draw(surface);
			surface.push();
			surface.fill(255, 0, 0);
			surface.translate(x, y);
			surface.rotate(angle);
			PImage sprite = GameImage.get("Bullet2");
			surface.image(sprite, -sprite.width/2, -sprite.height/2);
			if (Globals.DEBUG_MODE) Globals.drawDebugInfo(surface, this, x, y);
			surface.translate(-x, -y);
			surface.pop();
		}
	}
}
