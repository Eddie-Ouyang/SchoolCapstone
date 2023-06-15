package Combat.enemies;

import Combat.player.Player;
import engine.Globals;

class DemonAI extends EnemyAI {
	private int strafeDirection = 1;
	/**
	 * Creates a controller for a DemonEnemy 
	 * @param E DemonEnemy that the controller is bound to
	 */
	public DemonAI (DemonEnemy E) {
		super.entity = E;
		
		if (Math.random() > 0.5) strafeDirection *= -1;
	}
	
	@Override
	public Routine nextRoutine() {
		// moves towards player when offscreen
		if (Math.abs(getDeltaYPlayer()) > Globals.SCREEN_H / 2 || Math.abs(getDeltaXPlayer()) > Globals.SCREEN_W / 2) 
			return new Routine(super.entity, 20)
				.setTargetX(Player.x)
				.setTargetY(Player.y)
				.setSpeed(3 + (float)Math.sqrt(Globals.DIFFICULTY))
				.setAcceleration(0.02f);
		
		// strafes around player when dist <= 600
		if (getDistToPlayer() <= 600) 
			return new Routine(super.entity, 120)
				.setTargetX(entity.X + strafeX(200, strafeDirection))
				.setTargetY(entity.Y + strafeY(200, strafeDirection))
				.setSpeed(2 + (float)Math.sqrt(Globals.DIFFICULTY));
		
		// stands still when 600 < dist <= 800
		if (getDistToPlayer() <= 800) 
			return new Routine(super.entity, 60)
				.stopMovement();
		
		
		
		// default behavior: stand still
		return new Routine(super.entity, 150)
				.stopMovement();
	}
	
	private float strafeX (float amount, int dir) { 
		return (float)Math.cos(getAngleToPlayer() + dir * Math.PI / 2) * amount;
	}
	
	private float strafeY (float amount, int dir) {
		return (float)Math.sin(getAngleToPlayer() + dir * Math.PI / 2) * amount;
	}
	
//	private float randomBetween (float min, float max) {
//		return (float)(Math.random() * (max - min) + min);
//	}
}
