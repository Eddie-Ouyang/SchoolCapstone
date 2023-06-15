package Combat.player;


import Combat.projectiles.Projectile;
import construct.Shield;
import engine.Globals;
import processing.core.*;

/**
 * 
 * Represents the damage that has been done to the player
 * @author prathamhebbar
 *
 */
public class DamageModel {
	
	private Position positions[][];
	
	/**
	 * Creates a new DamageModel for the Player
	 * @param P 2d array of positions to model
	 */
	public DamageModel (Position P[][]) {
		positions = P;
	}
	
	/**
	 * simulates the player taking damage from a projectile 
	 * @param proj projectile which hits the player
	 */
	public void takeDamage(Projectile proj) {
		Position closest = null;
		
		double minDist = Double.MAX_VALUE;
		for (int i = Player.r0; i <= Player.rM; i++) {
			for (int j = Player.c0; j <= Player.cM; j++) {
				Position position = positions[i][j];
				if (!position.active) continue;
				
				float x = position.getMapX();
				float y = position.getMapY();
				
				float projX = proj.getX();
				float projY = proj.getY();
				
				double horizontalDistance = Math.pow(Math.abs(x - projX), 2);
				double verticalDistance = Math.pow(Math.abs(y - projY), 2);
				
				double hypotenuse = Math.sqrt(horizontalDistance + verticalDistance);
				
				if (hypotenuse < minDist) {
					minDist = hypotenuse;
					closest = position;
				}
			}
		}
		
		if (closest != null && minDist <= Globals.POSITION_SIZE * 4) {
			int r = closest.r;
			int c = closest.c;
			
			for(int i = r==Player.r0?r:r-1; i <= (r==Player.rM?r:r+1); i++) {
				for(int j = c==Player.c0?c:c-1; j <= (c==Player.cM?c:c+1); j++) {
					if((r!=i || j!=c) && positions[i][j] != null && positions[i][j].tower instanceof Shield) {
						int dmgCost = proj.getDamage()*2;
						if(Globals.resources[4] >= dmgCost) {
							positions[i][j].takeDmg(dmgCost/4);
							Globals.resources[4] -= dmgCost; 
						} else {
							positions[i][j].takeDmg(proj.getDamage());
						}
						return;
					}
				}
			}
			closest.takeDmg(proj.getDamage());
		}
	}
	
	/**
	 * Draws a visual representation of the player's damage model on screen
	 * @param surface PApplet drawing surface
	 * @param x0 x coordinate
	 * @param y0 y coordinate
	 * @param w width of model
	 * @param h height of model
	 */
	public void draw (PApplet surface, float x0, float y0, float w, float h) {
		surface.pushStyle();
		surface.colorMode(PApplet.HSB,360,1,1,1);
		surface.stroke(360);
		float size = Math.min(w/(Player.cM-Player.c0+1), h/(Player.rM-Player.r0+1));
		drawPositions(surface, x0+w/2-size*(Player.cM-Player.c0+1)/2f, y0+h/2-size*(Player.rM-Player.r0+1)/2f, size);
		surface.popStyle();
	}
	
	private void drawPositions (PApplet surface, float x0, float y0, float size) {
		for (int i = Player.r0; i <= Player.rM; i++) {
			for (int j = Player.c0; j <= Player.cM; j++) {
				
				Position pos = positions[i][j];
				if (!pos.active) continue;
				
				float curX = (j-Player.c0)*size + x0;
				float curY = (i-Player.r0)*size + y0;
				
				// TODO: do your weird color gradient thing
				surface.strokeWeight(2);
				surface.fill(196, 0.57f, pos.health/100f*0.63f,0.5f);
				surface.square(curX, curY, size);
				surface.strokeWeight(0);
				
				if (pos.tower == null || pos.tower.dead()) continue;
				float hp = pos.tower.getHealth();
				surface.fill(0,0.5f);
				surface.square(curX+size/10, curY+size/10, size*0.8f);
				if(i == 5 && j == 5) {
					surface.fill(0,1,hp,0.5f);
				} else if (pos.tower instanceof Shield){
					surface.fill(275,1,0.5f);
				} else {
					surface.fill(120*hp,.73f,.93f*hp,0.5f);
				}
				surface.rect(curX+size/10, curY+size/10+(size*0.8f*(1-hp)), size*0.8f,size*0.8f*hp);
			}
		}
	}
}
