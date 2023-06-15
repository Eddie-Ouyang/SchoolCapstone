package Combat.projectiles;

import engine.Globals;
import processing.core.PApplet;
import processing.core.PImage;
import screen.GameImage;

public class BaconProjectile extends Projectile {
	/**
	 * Creates a BaconProjectile 
	 * @param x x coordinate of projectile
	 * @param y y coordinate of projectile
	 * @param angle angle the projectile is facing
	 */
	public BaconProjectile(float x, float y, float angle) {
		super(x, y, 40, 4, 10, angle, 2, true);
	}
	
	@Override
	public void draw (PApplet surface) {
		super.draw(surface);
		
		if (life != 0) {
			super.draw(surface);
			surface.push();

			if(life<5)surface.tint(255,255*(life)/5f);
			surface.translate(x, y);
			surface.rotate(angle);
			
			PImage sprite = GameImage.get("BaconPiece");
			surface.image(sprite, -sprite.width/2, -sprite.height/2);
			if (Globals.DEBUG_MODE) Globals.drawDebugInfo(surface, this, x, y); 
			
			surface.translate(-x, -y);
			
			surface.pop();
		}
	}
}
