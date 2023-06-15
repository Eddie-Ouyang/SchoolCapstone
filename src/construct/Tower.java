package construct;

import java.util.HashMap;

import engine.Globals;
import processing.core.PApplet;
import processing.core.PImage;
import screen.GameImage;

/**
 * Super class for buildable towers
 * @author Eddie Ouyang
 *
 */
public abstract class Tower {
	
	protected String image;
	protected int hp, dmg, weight,direction,cost;
	private static HashMap<String,Tower> towers;
	
	/**
	 * Fills static HashMap
	 */
	public static void setup() {
		towers = new HashMap<String,Tower>(); 
		towers.put("Core", new Core());
		towers.put("Farm", new Farm());
		towers.put("Farm0", new Farm(0));
		towers.put("Farm1", new Farm(1));
		towers.put("Farm2", new Farm(2));
		towers.put("ThrusterN", new Thruster(1));
		towers.put("ThrusterE", new Thruster(2));
		towers.put("ThrusterS", new Thruster(3));
		towers.put("ThrusterW", new Thruster(0));
		towers.put("EggN", new Egg(1));
		towers.put("EggE", new Egg(2));
		towers.put("EggS", new Egg(3));
		towers.put("EggW", new Egg(0));
		towers.put("BaconN", new Bacon(1));
		towers.put("BaconE", new Bacon(2));
		towers.put("BaconS", new Bacon(3));
		towers.put("BaconW", new Bacon(0));
		towers.put("MilkN", new Milk(1));
		towers.put("MilkE", new Milk(2));
		towers.put("MilkS", new Milk(3));
		towers.put("MilkW", new Milk(0));
		towers.put("Reactor", new Reactor());
		towers.put("Shield", new Shield());
	}
	
	/**
	 * Copy a tower
	 * @param s Name of tower to copy
	 * @return Copied Tower
	 */
	public static Tower copyTower(String s) {
		return towers.get(s).copySelf();
	}
	
	/**
	 * Get tower's price
	 * @param s Name of tower
	 * @return Price
	 */
	public static int price(String s) {
		return towers.get(s).cost;
	}
	
	/**
	 * Get name of Image
	 * @return Image name
	 */
	public String getImg() {
		return image;
	}
	
	/**
	 * Take damage
	 * @param dmg Amount of damage
	 */
	public void takeDmg(int dmg) {
		hp -= dmg;
		if(hp<0)hp=0;
	}
	
	/**
	 * Return if this tower has no health
	 * @return True if health is 0, false otherwise
	 */
	public boolean dead() {
		return hp == 0;
	}
	
	/**
	 * Copy this tower
	 * @return Copy of this tower
	 */
	public Tower copySelf() {return null;}
	
	/**
	 * Rotate the state of this tower
	 */
	public void rotate() {
		if(direction == -1)return;
		direction = (direction+1)%4;
		image = image.substring(0,image.length()-1) + directionToChar();
	}
	
	/**
	 * Draw this tower
	 * @param x Top left x
	 * @param y Top left y
	 * @param size Size to scale to
	 * @param p PApplet to draw to
	 * @post Contents of p are changed
	 */
	public void draw(float x, float y, float size, PApplet p) {
		p.image(GameImage.get(image), x, y, size, size);
	}

	/**
	 * Draw this tower
	 * @param x Top left x
	 * @param y Top left y
	 * @param p PApplet to draw to
	 * @post Contents of p are changed
	 */
	public void draw(float x, float y, PApplet p) {
		p.image(GameImage.get(image), x, y);
	}
	
	/**
	 * Buy this tower
	 * @return True if affordable, false otherwise
	 */
	public boolean buy() {
		if(Globals.resources[3] >= cost) {
			Globals.resources[3] -= cost;
			return true;
		}
		return false;
	}
	
	/**
	 * Sells this tower and adds the funds to resource pool
	 * @return True if this tower can be sold, false otherwise
	 */
	public boolean sell() {
		Globals.resources[3] += sellPrice();
		return true;
	}
	
	/**
	 * Draws this tower as an icon in [Battle]
	 * @param surface PApplet to draw to
	 * @param x Top left x
	 * @param y Top left y
	 * @post Contents of surface are changed
	 */
	public void drawOnPlayer(PApplet surface, float x, float y) {
		surface.pushStyle();
		surface.image(GameImage.getSmol(image), x, y);
		surface.popStyle();
	}
	
	/**
	 * Draws the stats of this tower
	 * @param p PApplet to draw to
	 * @param x Top left x
	 * @param y Top left y
	 * @param w Width of graphic
	 * @param h Height of graphic
	 * @post Contents of p are changed
	 */
	public void drawStats(PApplet p, float x, float y, float w, float h) {
		p.push();
		p.textSize(w/5);
		p.fill(360);
		p.textAlign(PApplet.LEFT,PApplet.TOP);
		p.text(" " + name(), x, y);
		p.textSize(w/12);
		p.text(description(), x+5, y+w*0.4f);
		p.fill(100);
		p.rect(x+w/10, y+w/4, w*0.8f, w/7);
		p.fill(360);
		p.rect(x+w/10, y+w/4, w*0.8f*getHealth(), w/7);
		p.pop();
	}
	
	/**
	 * Heal this tower to max health
	 */
	public void heal() {
		hp = towers.get(image).hp;
	}
	
	/**
	 * For attacking towers
	 */
	public void resetTime() {}
	
	/**
	 * Get the cost required to repair this tower
	 * @return Repair cost
	 */
	public int repairCost() {
		int repair = (int)(cost*(1-(1f*hp/towers.get(image).hp)));
		if(repair == 0)return 0;
		return repair < cost/5?cost/5:repair;
	}

	/**
	 * Return on selling this tower
	 * @return Sell price
	 */
	public int sellPrice() {
		return hp == towers.get(image).hp?cost:(int)(0.8*cost);
	}
	
	protected String description() {
		return "";
	}
	
	protected char directionToChar() {
		return (direction>1?(direction==2?'E':'S'):(direction==0?'W':'N'));
	}
	
	/**
	 * Get percentage of current health over max health
	 * @return Percent max health
	 */
	public float getHealth() {
		return 1f*hp/towers.get(image).hp;
	}
	
	/**
	 * Get weight
	 * @return weight
	 */
	public int getWeight () {
		return weight;
	}
	
	/**
	 * Get rotation of this tower
	 * @return
	 */
	public int getDirection () {
		return direction;
	}

	/**
	 * Get simple name of this tower
	 * @return
	 */
	public String name() {
		return direction==-1?image:image.substring(0,image.length()-1);
	}
	
	/**
	 * For towers than can attack
	 * @param x X of attack origin
	 * @param y Y of attack origin
	 * @param angle Angle of attack
	 * @post Contents of POJECTILE_QUEUE are changed
	 */
	public void attack (float x, float y, float angle) {}
}
