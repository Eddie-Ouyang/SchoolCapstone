package construct;

import Combat.projectiles.BaconProjectile;
import Combat.projectiles.DelayedProjectile;
import engine.Globals;

/**
 * Fires bacon buckshot
 * @author Eddie Ouyang, Jiaming Situ
 *
 */
public class Bacon extends Tower{
	private int coolDown = 40;
	private int nextFire;
	
	public Bacon(int d) {
		direction = d;
		cost = 150;
		hp = 100;
		weight = 5;
		image = "Bacon" + directionToChar();
	}

	@Override
	public String description() {
		return "Consume [BACON] to fire\n2 waves of pellets";
	}
	
	@Override
	public void resetTime() {nextFire = 0;}

	@Override
	public void attack (float x, float y, float angle) { 
		if(Globals.resources[1] == 0 || Globals.CURRENT_FRAME < nextFire) return;
		Globals.resources[1]--;
		nextFire = Globals.CURRENT_FRAME + coolDown;
		int burstCount = 3;
		int burstSize = 4; 
		int delayBetweenShots = 5;
		
		for (int i = 1; i <= burstCount; i++) {
			for (int j = 1; j <= burstSize; j++) {
				float deviation = 0.4f;
				float randomSpread = (float)(deviation * Math.random() - deviation/2);
				BaconProjectile shell = new BaconProjectile(x, y, angle + (float)Math.PI/2 * direction + randomSpread);
				Globals.PROJECTILE_QUEUE.add(new DelayedProjectile(Globals.CURRENT_FRAME + i * delayBetweenShots, shell));
			}
		}
	}

	@Override
	public Tower copySelf() {
		return new Bacon(direction);
	}
}
