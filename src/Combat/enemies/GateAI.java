package Combat.enemies;

import Combat.player.Player;
import engine.Globals;

public class GateAI extends EnemyAI {
	/**
	 * Creates a controller for a GateEnemy 
	 * @param E GateEnemy that the controller is bound to
	 */
	public GateAI (GateEnemy E) {
		super.entity = E;
	}
	
	@Override
	public Routine nextRoutine() {
		// moves towards player when offscreen
		if (Math.abs(getDeltaYPlayer()) > Globals.SCREEN_H / 2 || Math.abs(getDeltaXPlayer()) > Globals.SCREEN_W / 2) 
			return new Routine(super.entity, 20)
				.setTargetX(Player.x)
				.setTargetY(Player.y)
				.setSpeed(2 + (float)Math.sqrt(Globals.DIFFICULTY))
				.setAcceleration(0.02f);
		
		if (getDistToPlayer() <= 500)
			return new Routine(super.entity, 30)
				.setTargetX(moveAwayX(getAngleToPlayer(), 300))
				.setTargetY(moveAwayY(getAngleToPlayer(), 300))
				.setSpeed(5 + (float)Math.sqrt(Globals.DIFFICULTY));
		
		if (getDistToPlayer() > 800) 
			return new Routine(super.entity, 60)
				.setTargetX(Player.x)
				.setTargetY(Player.y)
				.setSpeed(3 + (float)Math.sqrt(Globals.DIFFICULTY));
		
		
		// default behavior: stand still
		return new Routine(super.entity, 150)
				.stopMovement();
	}
	
	private float moveAwayX (float angle, float distance) {
		return (float)Math.cos(angle + Math.PI) * distance;
	}
	
	private float moveAwayY (float angle, float distance) {
		return (float)Math.sin(angle + Math.PI) * distance;
	}
}
