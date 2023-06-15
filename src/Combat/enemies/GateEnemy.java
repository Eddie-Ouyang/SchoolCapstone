package Combat.enemies;

import Combat.projectiles.DelayedProjectile;
import Combat.projectiles.GateProjectile;
import engine.Globals;
import processing.core.PApplet;
import processing.core.PImage;
import screen.GameImage;

public class GateEnemy extends Enemy {
	private final int cooldown = 330;
	private int currentCooldown = (1 + (1 / (Globals.DIFFICULTY + 1))) * cooldown;
	private float[] end;
	
	/**
	 * Creates a new GateEnemy
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public GateEnemy (float x, float y){
		super(x, y, 100); 
		
		this.controller = new GateAI(this);
	}
	
	
	@Override
	public void draw (PApplet surface) {
		super.draw(surface);
		
		surface.push();
		PImage sprite = GameImage.get("Gate");
		
		if(currentCooldown < 40) {
			surface.stroke(255);
			surface.strokeWeight(16*(currentCooldown/40f));
			surface.line(X+size/2, Y+size/2, end[0], end[1]);
			surface.stroke(0);
			surface.strokeWeight(14*(currentCooldown/40f));
			surface.line(X+size/2, Y+size/2, end[0], end[1]);
		}
		sprite.resize(this.size, this.size);
		surface.image(sprite, this.X, this.Y);	
		if (Globals.DEBUG_MODE) Globals.drawDebugInfo(surface, this, this.X, this.Y);
		surface.pop();
	}
	
	@Override
	public void shoot () {
		if(currentCooldown == 40) {
			end = new float[] {controller.getDeltaXPlayer()*15+X,controller.getDeltaYPlayer()*15+Y};
			this.controller.currentRoutine = new Routine(this, 32).stopMovement();
		}
		if (currentCooldown > 0) {
			currentCooldown--;
			return;
		}
		currentCooldown = cooldown + (int)(60 * Math.random() - 30);
		
		int laserLength = 100;
		int laserTime = 10;
		float angle = (float)Math.atan2(end[1]-Y-size/2, end[0]-X-size/2);
		for (int i = 1; i <= laserTime; i++) {
			for (int j = 0; j < laserLength; j++) {
				float deltaX = (float)Math.cos(angle) * 16 * j;
				float deltaY = (float)Math.sin(angle) * 16 * j;
				GateProjectile proj = new GateProjectile(X + size/2 + deltaX, Y + size/2 + deltaY, angle);
				DelayedProjectile p = new DelayedProjectile(Globals.CURRENT_FRAME + i , proj);
				p.noCompensate();
				Globals.PROJECTILE_QUEUE.add(p);
			}
		}
	}
}
