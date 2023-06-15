package Combat.physics;

// only static methods here that will be used by Map.java
public class PhysicsEngine {
	/**
	 * @param A 		
	 * @param B
	 * @return true if the hit boxes intersect, false otherwise
	 */
	public static boolean isIntersecting (Hitbox A, Hitbox B) {
		return intersectAmount(A, B) > 0;
	}
	
	/**
	 * @param A
	 * @param B
	 * @return how much the two hit boxes intersect; 0 if they don't intersect
	 */
	public static float intersectAmount (Hitbox A, Hitbox B) {
		double hypot = Math.hypot(A.getHitboxCenterX() - B.getHitboxCenterX(), A.getHitboxCenterY() - B.getHitboxCenterY());
		double radSum = A.getHitboxRadius() + B.getHitboxRadius();
		if (hypot > radSum) return 0;
		return (float)(radSum - hypot);
	}
	
	/**
	 * @param target the base hit box 
	 * @param other the other one
	 * @return angle from target to other from 0 to 2 PI
	 */
	public static float angleBetween (Hitbox target, Hitbox other) {
		return (float)(Math.atan2(other.getHitboxCenterY() - target.getHitboxCenterY(), other.getHitboxCenterX() - target.getHitboxCenterX()) + Math.PI);
	}
}
