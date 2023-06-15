package Combat.enemies;

public class AngelAI extends EnemyAI{
	/**
	 * Creates a controller for an AngelEnemy 
	 * @param E AngleEnemy that the controller is bound to
	 */
	public AngelAI(AngelEnemy E) {
		super.entity = E;
	}
	
	@Override
	protected Routine nextRoutine() {
		return new Routine(super.entity, 9999).stopMovement();
	}
}
