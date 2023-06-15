package Combat.enemies;

import engine.Globals;

public class Routine {
	
	public int duration, startFrame, endFrame;
	public float speed, acceleration; // speed and acceleration (both scalars) of the entity while moving towards target 
	public float targetX, targetY; // x and y that entity should be at after finishing routine
	public float completionRadius; // how close to target x and y the entity can get before stopping
	public boolean halt;
	
	/**
	 * Creates a new Routine 
	 * @param E Enemy that runs this routine
	 * @param duration Total duration of this routine
	 */
	public Routine (Enemy E, int duration) {
		this.duration = duration;
		
		startFrame = Globals.CURRENT_FRAME;
		endFrame = startFrame + duration;
		
		// defaults
		speed = 5;
		acceleration = 0;
		targetX = E.X;
		targetY = E.Y;
		completionRadius = 10;
	}
	
	/**
	 * @param s new speed 
	 * @return updated routine
	 */
	public Routine setSpeed (float s) {
		this.speed = s;
		return this;
	}
	
	/**
	 * @param a new acceleration 
	 * @return updated routine
	 */
	public Routine setAcceleration (float a) {
		this.acceleration = a;
		return this;
	}
	
	/**
	 * @param x desired x coordinate 
	 * @return updated routine
	 */
	public Routine setTargetX (float x) {
		this.targetX = x;
		return this;
	}
	
	/**
	 * @param y desired y coordinate 
	 * @return updated routine
	 */
	public Routine setTargetY (float y) {
		this.targetY = y;
		return this;
	}
	
	/**
	 * halts all movement
	 * @return updated routine
	 */
	public Routine stopMovement () {
		this.halt = true;
		return this;
	}
	
	/**
	 * checks whether or not this routine is complete
	 * @param E enemy that's currently running the routine
	 * @return true if the enemy has finished this routine, false otherwise
	 */
	public boolean routineCompleted (Enemy E) {
		if (!halt && Math.hypot(E.X - this.targetX, E.Y - this.targetY) < completionRadius) return true;
		if (Globals.CURRENT_FRAME >= endFrame) return true;
		
		return false;
	}
}
