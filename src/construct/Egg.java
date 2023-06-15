package construct;

import Combat.projectiles.DelayedProjectile;
import Combat.projectiles.EggProjectile;
import engine.Globals;
/**
 * Shoots a burst of 6 eggs
 * @author Eddie Ouyang, Jiaming Situ
 *
 */
public class Egg extends Tower {
	private int coolDown = 30;
	private int nextFire;
	
	public Egg(int d) {
		direction = d; 
		hp = 100;
		cost = 100;
		weight = 3;
		image = "Egg" + directionToChar();
	}
	
	@Override
	public String description() {
		return "Consume [EGG] to fire a\n6 shot burst";
	}

	@Override
	public void resetTime() {nextFire = 0;}
	
	@Override
	public void attack (float x, float y, float angle) {
		if(Globals.resources[0] < 2 || Globals.CURRENT_FRAME < nextFire) return;
		Globals.resources[0]-=3;
		nextFire = Globals.CURRENT_FRAME+coolDown;
		int burstSize = 6;
		int delayBetweenShots = 3;
		
		for (int i = 1; i <= burstSize; i++) {
			float randomSpread = (float)(0.1 * Math.random());
			EggProjectile shell = new EggProjectile(x, y, angle + (float)Math.PI/2 * direction + randomSpread);
			Globals.PROJECTILE_QUEUE.add(new DelayedProjectile(Globals.CURRENT_FRAME + i * delayBetweenShots, shell));
		}
	}

	@Override
	public Tower copySelf() {
		return new Egg(direction);
	}
}
