package Combat;

import Combat.physics.Hitbox;
import Combat.player.Player;
import processing.core.PApplet;
import processing.core.PImage;

public class Hangar implements Hitbox{
	private float x,y;
	private PImage img;
	
	/**
	 * Creates a hangar
	 * @param f distance away from player where the hangar should be generated
	 * @param p Hangar's sprite
	 */
	public Hangar(float f, PImage p) {
		double d = Math.random()*Math.PI*2;
		x = f*(float)Math.cos(d);
		y = f*(float)Math.sin(d);
		img = p;
	}
	
	/** 
	 * @return angle from hangar to the player
	 */
	public float getAngle() {
		return (float)Math.atan2(y-Player.y, x-Player.x);
	}
	
	/**
	 * Draws the hangar on screen
	 * @param p PApplet drawing surface
	 */
	public void draw(PApplet p) {
		p.push();
		p.translate(-Player.x+p.width/2, -Player.y+p.height/2);
		p.image(img, x-img.width/2, y-img.height/2);
		p.pop();
	}
	
	@Override
	public float getHitboxRadius() {
		return img.width/10;
	}

	@Override
	public float getHitboxCenterX() {
		return x;
	}

	@Override
	public float getHitboxCenterY() {
		return y;
	}

}
