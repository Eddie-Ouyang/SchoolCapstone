package construct;
/**
 * Increases rotation speed
 * @author Eddie Ouyang
 *
 */
public class Thruster extends Tower{
	private float force = 0.1f;
	public Thruster(int i) {
		direction = i;
		cost = 75;
		hp = 50;
		weight = 3;
		image = "Thruster" + directionToChar();
	}
	
	@Override
	public String description() {
		return "Provides torque to assist\nin rotating"; 
	}

	/**
	 * Get thruster force
	 * @return force
	 */
	public float getForce () {
		return force;
	}

	@Override
	public Tower copySelf() {
		return new Thruster(direction);
	}
}
