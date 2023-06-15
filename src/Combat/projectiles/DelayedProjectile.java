package Combat.projectiles;

import Combat.player.Player;
import construct.Tower;

public class DelayedProjectile implements Comparable<DelayedProjectile> {
	public int frame; // what frame should this projectile be drawn at
	
	private Projectile proj;
	private float originalX, originalY, originalAngle;
	private boolean compensateMovement = true;

	/**
	 * Creates a DelayedProjectile that will only show at a later time
	 * @param f frame at which the projectile will show up
	 * @param p projectile which will be displayed
	 */
	public DelayedProjectile (int f, Projectile p) {
		frame = f;
		proj = p;
		
		// used to compensate for player movement after firing
		originalX = Player.x;
		originalY = Player.y;
		originalAngle = Player.angle;
	}

	/**
	 * @return the original projectile wrapped within  
	 */
	public Projectile getProjectile () {
		if (compensateMovement) {
			float compX = Player.x - originalX;
			float compY = Player.y - originalY;
			float compAngle = Player.angle - originalAngle;
			
			proj.x += compX;
			proj.y += compY;
			proj.angle += compAngle;
		}
		
		return proj;
	}
	
	/**
	 * disable movement compensation with respect to the player
	 */
	public void noCompensate() {
		this.compensateMovement = false;
	}
	
	@Override
	public int compareTo(DelayedProjectile other) {
		return this.frame - other.frame;
	}
}
