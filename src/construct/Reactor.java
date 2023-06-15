package construct;

import engine.Globals;

/**
 * Increase speed
 * @author Eddie Ouyang
 *
 */
public class Reactor extends Tower{
	private float force = 100;
	public Reactor() {
		image = "Reactor";
		cost = 300;
		hp = 50;
		weight = 10;
		direction = -1;
	}

	@Override
	public String description() {
		return "Increases base movement \nspeed. Unlocks and \ngenerates nuclear energy\nwhich can boost speed\nand power up Shields";
	}
	
	/**
	 * Generate resources
	 * @post resources is changed
	 */
	public void farm() {
		if(Globals.resources[4] < Globals.fuel) Globals.resources[4]++;
	}

	@Override
	public Tower copySelf() {
		return new Reactor();
	}

	/**
	 * Get force
	 * @return force
	 */
	public float getForce () {
		return force;
	}
}
