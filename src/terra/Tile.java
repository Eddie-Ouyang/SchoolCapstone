package terra;

import java.awt.Rectangle;

import construct.Tower;
import engine.Globals;
import processing.core.PApplet;
import screen.GameImage;
import screen.Screen;

/**
 * Represents a plot of land 
 * @author Eddie Ouyang
 */
public class Tile {
	protected final int x, y;
	private int cost;
	protected boolean active,core;
	private Tower structure;
	
	/**
	 * Constructor
	 * @param price Price to active tile
	 */
	public Tile(int price, int x, int y) {
		cost = price;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * 
	 * @return active
	 */
	public boolean active() {
		return active;
	}
	
	/**
	 * Makes the middle tower the core
	 */
	public void setCore() {
		active = true;
		structure = Tower.copyTower("Core");
		core = true;
	}
	
	/**
	 * Tries to build a new tower 
	 * @param t Tower to build
	 * @return True if [t] was purchased, false if the space is occupied or a tower couldn't be afforded
	 */
	public boolean build(Tower t) {
		if(structure != null) return false;
		if(t.buy()) {
			structure = t;
			return true;
		}
		return false;
	}
	
	/**
	 * Set the structure to [t]
	 * @param t Tower to set
	 */
	public void setTower(Tower t) {
		structure = t;
	}
	
	/**
	 * Swaps towers with another tile
	 * @param t Tile to swap with
	 */
	public void swap(Tile t) {
		Tower temp = structure;
		structure = t.structure;
		t.structure = temp;
	}
	
	/**
	 * Buys or sells this tile based on the circumstance
	 * @return True if this tile is active, false otherwise
	 */
	public boolean buy() {
		if(!active) {
			if(Globals.resources[3] >= cost) {
				Globals.resources[3] -= cost;
				active = true;
			}
		} else {
			if(cost == 0)return true;
			Globals.resources[3] += cost;
			active = false;
			sell();
		}
		return active;
	}
	
	/**
	 * Rotates the tower if possible
	 */
	public void rotate() {
		if(structure!=null)structure.rotate();
	}
	
	/**
	 * Sells the tower if possible
	 * @return True if sold
	 */
	public boolean sell() {
		if(structure!=null && structure.sell()) {
			structure = null;
			return true;
		}
		return false;
	}
	
	/**
	 * Repairs the tower if possible
	 */
	public void repair() {
		if(structure==null)return;
		int cost = structure.repairCost();
		if(Globals.resources[3] >= cost) {
			Globals.resources[3]-=cost;
			structure.heal();
		}
	}
	
	/**
	 * Draws this tile
	 * @param x Top left X
	 * @param y Top left y
	 * @param f Size of tile
	 * @param p PApplet to draw to
	 * @post Contents of p are changed
	 */
	public void draw(float x, float y, float f, PApplet p) {
		if(!active) {
			p.push();
			p.noFill();
			p.stroke(360);
			p.strokeWeight(2);
			p.square(x, y, f);
			p.pop();
		}
		if(active) p.image(GameImage.get("Grass"), x, y, f, f);
		if(structure != null) structure.draw(x, y, f, p);
	}
	
	/**
	 * Draws the menu of options for the current tile
	 * @param x Top left x
	 * @param y Top left y
	 * @param width Width of menu
	 * @param height Height of menu
	 * @param button Array of buttons to draw
	 * @param p PApplet to draw to
	 * @post Contents of p are changed
	 */
	public void drawInfo(float x, float y, float width, float height, Rectangle[] button,PApplet p) {
		y+=height/5;
		p.fill(active?22:127);
		p.stroke(360);
		p.rect(button[0].x,button[0].y,button[0].width,button[0].height);
		if(structure != null) {
			
			float y0 = y + height/7, h0 = height*0.5f;
			p.rect(x, y0, width, h0);
			structure.drawStats(p, x, y0, width, h0);
			p.push();
			p.textAlign(PApplet.CENTER, PApplet.TOP);
			String[] text = new String[] {"Repair:"+structure.repairCost(),
										  "Rotate", "Move",
										  "Sell:"+structure.sellPrice()};
			for(int i = 1; i < button.length; i++) {
				Rectangle b = button[i];
				p.fill(0);
				p.rect(b.x,b.y,b.width,b.height);
				p.fill(360);
				p.textSize(b.height*0.8f);
				p.text(text[i-1], b.x+b.width/2, b.y);
			}
			p.pop();
		}
		p.fill(360);
		x += width/2;
		p.textFont(p.createFont(Screen.font, width/5f));
		p.textAlign(PApplet.CENTER, PApplet.TOP);
		p.fill(360);
		p.text((active?"Sell: ":"Cost: ")+cost, x, y+height/40);
		
	}
	
	/**
	 * Get this tile's tower
	 * @return Structure
	 */
	public Tower getTower () {
		if(structure != null)structure.resetTime();
		return structure;
	}
}
