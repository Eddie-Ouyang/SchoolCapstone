package construct;

import Combat.projectiles.MilkProjectile;
import Combat.Map;
import engine.Globals;

/**
 * Shoots a cow then explodes
 * @author Eddie Ouyang, Jiaming Situ
 *
 */
public class Milk extends Tower{
	private int coolDown = 60;
	private int nextFire;
	
	public Milk(int i) {
		cost = 300;
		hp = 100;
		weight = 3;
		direction = i;
		image = "Milk" + directionToChar();
	}

	@Override
	public String description() {
		return "Consumes [MILK] to fire \na cow which explodes into\nbone fragments";
	}

	@Override
	public void resetTime() {nextFire = 0;}
	
	@Override
	public void attack (float x, float y, float angle) {
		if(Globals.resources[2]==0 || Globals.CURRENT_FRAME < nextFire)return;
		Globals.resources[2]--;
		nextFire = Globals.CURRENT_FRAME + coolDown;
		MilkProjectile shell = new MilkProjectile(x, y, angle + (float)Math.PI/2 * direction);
		Map.activeProjectiles.add(shell);
	}

	@Override
	public Tower copySelf() {
		return new Milk(direction);
	}
}
