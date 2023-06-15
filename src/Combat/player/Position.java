package Combat.player;

import construct.Tower;
import engine.Globals;
import processing.core.PApplet;
import processing.core.PImage;
import screen.GameImage;

public class Position {
	public int r, c,health;
	public Tower tower;
	public boolean active;
	
	private PImage sprite;
	
	/**
	 * Creates a new position that can hold a tower
	 * @param r row relative to top left
	 * @param c col relative to top left
	 * @param t tower which is held by the position
	 */
	public Position(int r, int c, Tower t) {
		this.r = r;
		this.c = c;
		this.health = 100;
		this.tower = t;
		this.active = false;
		
		this.sprite = GameImage.getSmol("Grass");
		this.sprite.resize(Globals.POSITION_SIZE, Globals.POSITION_SIZE);
	}
	
	/**
	 * Damages this current Position. The Position is destroyed if the health goes below 0. 
	 * @param dmg Amount of damage to inflict to the Position
	 */
	public void takeDmg(int dmg) {
		if(tower != null) {
			tower.takeDmg(dmg);
			if(tower.dead())tower = null;
		}
		else health -= dmg;
		if (this.health <= 0) {
			this.health = 0;
			this.active = false;
		}
	}
	
	/**
	 * Draws the current tile on screen
	 * @param surface PApplet drawing surface
	 */
	public void draw (PApplet surface) {
		if(!active) return;
		surface.pushMatrix();
		drawSelf(surface);
		drawTower(surface);
		surface.popMatrix();
	}
	
	private void drawSelf (PApplet surface) {
		if (!this.active) return;
		
		surface.pushStyle();		
		surface.image(sprite, getRelativeX(), getRelativeY());
		surface.popStyle();
	}
	
	private void drawTower (PApplet surface) {
		if (tower == null) return;
		tower.drawOnPlayer(surface, getRelativeX(), getRelativeY());
	}
	
	/**
	 * @return x coordinate of this position relative to the map 
	 */
	public float getMapX () {
		float xPos = (float)((getRelativeX() - Globals.PLAYER_SIZE/2f + Globals.POSITION_SIZE/2f) * Math.cos(Player.angle) - (getRelativeY() - Globals.PLAYER_SIZE/2f + Globals.POSITION_SIZE/2f) * Math.sin(Player.angle));
		xPos += Player.x;
		return xPos;
	}
	
	/**
	 * @return y coordinate of this position relative to the map 
	 */
	public float getMapY () {
		float yPos = (float)((getRelativeY() - Globals.PLAYER_SIZE/2f + Globals.POSITION_SIZE/2f) * Math.cos(Player.angle) + (getRelativeX() - Globals.PLAYER_SIZE/2f + Globals.POSITION_SIZE/2f) * Math.sin(Player.angle));
		yPos += Player.y;
		return yPos;
	}
	
	/**
	 * @return x coordinate of this position relative to the top left of the player 
	 */
	public float getRelativeX () {
		return c * Globals.POSITION_SIZE;
	}
	
	/**
	 * @return y coordinate of this position relative to the top left of the player 
	 */
	public float getRelativeY () {
		return r * Globals.POSITION_SIZE;
	}
}
