package construct;

import engine.Globals;
import processing.core.PApplet;
import processing.core.PImage;
import screen.GameImage;
/**
 * Your heart
 * @author Eddie Ouyang
 *
 */
public class Core extends Tower{
	public Core() {
		image = "Core";
		hp = 500;
		cost = 999999;
		weight = 40;
		direction = -1;
	}
	
	@Override
	public String description() {
		return "The heart of your ship\nDefend it at all costs";
	}

	@Override
	public boolean sell() {
		return false;
	}

	@Override
	public int sellPrice() {
		return -1;
	}

	@Override
	public Tower copySelf() {
		return new Core();
	}
}
