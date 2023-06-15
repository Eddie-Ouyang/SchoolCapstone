package Combat.projectiles;

import engine.Globals;
import processing.core.PApplet;
import processing.core.PImage;
import screen.GameImage;


public class EggProjectile extends Projectile {
	/**
	 * Creates an EggProjectile 
	 * @param x x coordinate of projectile
	 * @param y y coordinate of projectile
	 * @param angle angle the projectile is facing
	 */
	public EggProjectile(float x, float y, float angle) {
		super(x, y, 90, 5, 12, angle, 1, true);
	}
	
	@Override
	public void draw (PApplet surface) {
		if (life != 0) {
			super.draw(surface);
			surface.push();

			if(life<15)surface.tint(255,255*(life)/15f);
			surface.translate(x, y);
			surface.rotate(angle + (float)Math.PI/2);
			PImage sprite = GameImage.get("EggShell");
			surface.image(sprite, -sprite.width/2, -sprite.height/2);
			if (Globals.DEBUG_MODE) Globals.drawDebugInfo(surface, this, x, y);
			
			surface.translate(-x, -y);
			
			surface.pop();
		}
	}
}
