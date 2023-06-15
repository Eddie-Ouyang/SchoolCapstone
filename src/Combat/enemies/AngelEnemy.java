package Combat.enemies;

import Combat.player.Player;
import Combat.projectiles.AngelProjectile;
import Combat.projectiles.DelayedProjectile;
import engine.Globals;
import processing.core.PApplet;
import processing.core.PImage;
import screen.GameImage;

public class AngelEnemy extends Enemy {
	private final int cooldown = 300;
	private int currentCooldown = 60,fade = 30;
	
	/**
	 * Creates a new AngelEnemy
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public AngelEnemy(float x, float y) {
		super(x, y, 9999);
		this.controller = new AngelAI(this);
	}

	@Override
	public void draw (PApplet surface) {
		move();
		this.shoot();
		
		surface.push();
		PImage sprite = GameImage.get("Angel");
		if(currentCooldown < 40) {
			surface.stroke(255);
			surface.strokeWeight(15*(currentCooldown/40f));
			surface.line(X, Y, Player.x, Player.y);
			surface.stroke(0);
			surface.strokeWeight(12*(currentCooldown/40f));
			surface.line(X, Y, Player.x, Player.y);
		}
		if(fade > 0) surface.tint(255,255*(1-(fade/30f)));
		fade--;
		surface.image(sprite, this.X-sprite.width/2, this.Y-sprite.width/2);	
		if (Globals.DEBUG_MODE) Globals.drawDebugInfo(surface, this, this.X, this.Y);
		surface.pop();
	}
	
	@Override
	public void shoot() {
		if (currentCooldown > 0) {
			currentCooldown--;
			return;
		}
		currentCooldown = cooldown;
		
		int laserLength = (int)(controller.getDistToPlayer()/16 + 0.5);
		int laserTime = 5;
		float angle = controller.getAngleToPlayer();
		for (int i = 1; i <= laserTime; i++) {
			for (int j = 0; j < laserLength; j++) {
				float deltaX = (float)Math.cos(angle) * 16 * j;
				float deltaY = (float)Math.sin(angle) * 16 * j;
				AngelProjectile proj = new AngelProjectile(X + deltaX, Y + deltaY, angle);
				DelayedProjectile p = new DelayedProjectile(Globals.CURRENT_FRAME + i, proj);
				p.noCompensate();
				Globals.PROJECTILE_QUEUE.add(p);
			}
		}
	}
}
