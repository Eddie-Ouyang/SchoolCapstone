package Combat.projectiles;

import engine.Globals;
import processing.core.PApplet;
import processing.core.PImage;
import screen.GameImage;

public class MilkProjectile extends Projectile {
	/**
	 * Creates a MilkProjectile 
	 * @param x x coordinate of projectile
	 * @param y y coordinate of projectile
	 * @param angle angle the projectile is facing
	 */
	public MilkProjectile(float x, float y, float angle) {
		super(x, y, 90, 35, 7, angle, 1, true);
	}
	
	@Override
	public void draw (PApplet surface) {
		super.draw(surface);
		
		if (life != 0) {
			super.draw(surface);
			surface.push();
			
			surface.translate(x, y);
			surface.rotate(angle + Globals.CURRENT_FRAME % 30 / 30.0f * (float)Math.PI * 2);
			
			PImage sprite = GameImage.get("Calf");
			surface.image(sprite, -sprite.width/2, -sprite.height/2);
			if (Globals.DEBUG_MODE) Globals.drawDebugInfo(surface, this, x, y);
			
			surface.translate(-x, -y);
			
			surface.pop();
		}
	}
	
	@Override
	public void kill () {
		super.kill();
		
		int frags = 13;
		for (int i = 1; i <= frags; i++) {
//			BoneProjectile proj = new BoneProjectile(this.x, this.y, this.angle - (float)Math.PI/8 + (float)Math.PI/4 * ((float)i/13));

			BoneProjectile proj = new BoneProjectile(this.x, this.y, this.angle + (float)Math.PI*2*i/13);
			DelayedProjectile p = new DelayedProjectile(Globals.CURRENT_FRAME + 1, proj);
			p.noCompensate();
			Globals.PROJECTILE_QUEUE.add(p);
		}
	}
}
