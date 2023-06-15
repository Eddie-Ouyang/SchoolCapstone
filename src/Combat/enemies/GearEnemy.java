package Combat.enemies;

import Combat.projectiles.DelayedProjectile;
import Combat.projectiles.DemonProjectile;
import Combat.projectiles.GearProjectile;
import engine.Globals;
import processing.core.PApplet;
import processing.core.PImage;
import screen.GameImage;

public class GearEnemy extends Enemy {
	private final int cooldown = 360;
	private int currentCooldown = (1 + (1 / (Globals.DIFFICULTY + 1))) * cooldown;
	private float angle;
	
	/**
	 * Creates a new GearEnemy
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public GearEnemy (float x, float y){
		super(x, y, 200); 
		
		this.controller = new GearAI(this);
	}
	
	
	@Override
	public void draw (PApplet surface) {
		super.draw(surface);
		
		surface.push();
		surface.fill(128, 0, 0);
		PImage sprite = GameImage.get("Gear");
		sprite.resize(this.size, this.size);
		surface.translate(X+size/2, Y+size/2);
		surface.rotate(angle);
		angle += 0.05f;
		surface.image(sprite, -size/2, -size/2);
		if (Globals.DEBUG_MODE) Globals.drawDebugInfo(surface, this, this.X, this.Y);
		surface.pop();
	}
	
	@Override
	public void shoot () {
		if (currentCooldown > 0) {
			currentCooldown--;
			return;
		}
		currentCooldown = cooldown + (int)(60 * Math.random() - 30);
		
		int attackCount = 16;
		for (int i = 0; i < attackCount; i++) {
			float deltaX = (float)Math.random() * 20 - 10;
			float deltaY = (float)Math.random() * 20 - 10;
			GearProjectile proj = new GearProjectile(X + size/2 + deltaX, Y + size/2 + deltaY, controller.getAngleToPlayer() + (float)Math.PI * 2 / attackCount * i);
			Globals.PROJECTILE_QUEUE.add(new DelayedProjectile(Globals.CURRENT_FRAME + 1, proj));
		}
	}
}
