package Combat.enemies;

import Combat.projectiles.DelayedProjectile;
import Combat.projectiles.DemonProjectile;
import engine.Globals;
import processing.core.PApplet;
import processing.core.PImage;
import screen.GameImage;

/**
 * Basic enemy with shotgun attack
 * @author Jiaming Situ
 *
 */
public class DemonEnemy extends Enemy {
	private final int cooldown = 120;
	private int currentCooldown = (1 + (1 / (Globals.DIFFICULTY + 1))) * cooldown;
	private int shots, nextShot;
	
	/**
	 * Creates a new DemonEnemy
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public DemonEnemy (float x, float y){
		super(x, y, 100); 
		
		this.controller = new DemonAI(this);
	}
	
	
	@Override
	public void draw (PApplet surface) {
		super.draw(surface);
		
		surface.push();
		surface.fill(128, 0, 0);
		PImage sprite = GameImage.get("Hand");
		sprite.resize(this.size, this.size);
		surface.image(sprite, this.X, this.Y);
		if (Globals.DEBUG_MODE) Globals.drawDebugInfo(surface, this, this.X, this.Y);
		surface.pop();
	}
	
	@Override
	public void shoot () {
		if(shots > 0 && Globals.CURRENT_FRAME == nextShot) {
			DemonProjectile proj = new DemonProjectile(X + size/2, Y + size/2, controller.getPredictedAngle(new DemonProjectile(0,0,0)));
			Globals.PROJECTILE_QUEUE.add(new DelayedProjectile(Globals.CURRENT_FRAME + 1, proj));
			nextShot += 20;
			shots--;
		}
		
		if (currentCooldown > 0) {
			currentCooldown--;
			return;
		}
		currentCooldown = cooldown + (int)(60 * Math.random() - 30);
		
		if(Math.random() > 0.5) {
			shots = 4;
			nextShot = Globals.CURRENT_FRAME+1;
			currentCooldown += shots*20;
		} else {
			boolean mode = Math.random()>0.5;
			int burstSize = 4;
			
			for (int i = 0; i < burstSize; i++) {
				float deltaX = (float)Math.random() * 20 - 10;
				float deltaY = (float)Math.random() * 20 - 10;
				DemonProjectile proj = null;
				if(mode) proj = new DemonProjectile(X + size/2 + deltaX, Y + size/2 + deltaY, controller.getAngleToPlayer() + (float)(0.4 * Math.random() - 0.2));
				else proj = new DemonProjectile(X + size/2 + deltaX, Y + size/2 + deltaY, controller.getAngleToPlayer() + (float)Math.PI*(i%2==0?0.1f:-0.1f) + (float)(0.2 * Math.random() - 0.1));;
				Globals.PROJECTILE_QUEUE.add(new DelayedProjectile(Globals.CURRENT_FRAME + 1, proj));
			}
		}
	}
}