package Combat.enemies;

import Combat.player.Player;
import Combat.projectiles.Projectile;
import engine.Globals;

public abstract class EnemyAI {
	protected Enemy entity;
	protected Routine currentRoutine;
	
	protected abstract Routine nextRoutine();
	
	/**
	 * Moves the entity which this AI controller is bound to
	 */
	public void move() {
		if (currentRoutine == null || currentRoutine.routineCompleted(entity)) 
			currentRoutine = nextRoutine();

		// ------ move towards target ------
		if (!currentRoutine.halt) {
			float angleToDest = (float)Math.atan2(currentRoutine.targetY - entity.Y, currentRoutine.targetX - entity.X);
			entity.X += currentRoutine.speed * Math.cos(angleToDest);
			entity.Y += currentRoutine.speed * Math.sin(angleToDest);
			currentRoutine.speed += currentRoutine.acceleration;
			
			if (Math.hypot(entity.X - currentRoutine.targetX, entity.Y - currentRoutine.targetY) < currentRoutine.completionRadius) {
				currentRoutine.halt = true;
			}
		}
		// ---------------------------------
	}
	
	
	/**
	 * @return x displacement to the player
	 */
	public float getDeltaXPlayer () { return Player.x - entity.X; }
	
	/**
	 * @return y displacement to the player
	 */
	public float getDeltaYPlayer () { return Player.y - entity.Y; }
	
	/**
	 * @return angular displacement to the player
	 */
	public float getAngleToPlayer () { return (float)Math.atan2(getDeltaYPlayer(), getDeltaXPlayer()); }
	
	public float getPredictedAngle(Projectile p) {
		float time = getDistToPlayer()/p.getSpeed()/2;
		float nx = Player.x + time*Player.vx;
		float ny = Player.y + time*Player.vy;
		return (float)Math.atan2(ny - entity.Y, nx - entity.X);
	}
	
	/**
	 * @return pythagorean distance to the player
	 */
	public float getDistToPlayer () { return (float)Math.hypot(getDeltaXPlayer(), getDeltaYPlayer()); }
	
	
	@Override
	public String toString () {
		return "\nTargetX: " + currentRoutine.targetX 
				+ "\nTargetY: " + currentRoutine.targetY 
				+ "\nTime Remaining: " + (currentRoutine.endFrame - Globals.CURRENT_FRAME)
				+ "\nDist to Player: " + getDistToPlayer()
				+ "\nHalted: " + currentRoutine.halt
				+ "\nRoutine Status: " + currentRoutine.routineCompleted(entity);
	}
}
