package Combat.player;

import java.util.ArrayList;

import construct.Reactor;
import construct.Shield;
import engine.Globals;
import processing.core.PApplet;
import terra.Tile;

/**
 * A model of the player's Towers while in Battle screen
 * @author Jiaming Situ
 *
 */
public class PlayerModel {
	public Position positions[][];
	
	/**
	 * Creates a new player model of the Player's towers in Battle screen
	 * @param tiles all the tiles of the Player from the Build screen
	 */
	public PlayerModel(Tile tiles[][]) {
		positions = new Position[Globals.PLAYER_MODEL_ROWS][Globals.PLAYER_MODEL_COLS];
		
		
		for (int i = 0; i < Globals.PLAYER_MODEL_ROWS; i++) {
			for (int j = 0; j < Globals.PLAYER_MODEL_COLS; j++) {
				positions[i][j] = new Position(i, j, tiles[i][j].getTower());
				positions[i][j].active = tiles[i][j].active();
				if(tiles[i][j].getTower() != null &&  tiles[i][j].getTower() instanceof Reactor) Globals.fuel+=120;
				if(positions[i][j].active) {
					if(i<Player.r0) Player.r0 = i;
					if(i>Player.rM) Player.rM = i;
					if(j<Player.c0) Player.c0 = j;
					if(j>Player.cM) Player.cM = j;
				}
			}
		}
	}
	
	/**
	 * @return health of the core
	 */
	public float getHealth() {
		return positions[Globals.PLAYER_MODEL_COLS/2][Globals.PLAYER_MODEL_ROWS/2].tower==null?0:positions[Globals.PLAYER_MODEL_COLS/2][Globals.PLAYER_MODEL_ROWS/2].tower.getHealth();
	}
	
	/**
	 * Draws the player's tiles and towers on screen
	 * @param surface PApplet drawing surface
	 */
	public void draw (PApplet surface) {
		surface.pushMatrix();
		ArrayList<float[]> Shields = new ArrayList<float[]>();
		for (int i = Player.r0; i <= Player.rM; i++) {
			for (int j = Player.c0; j <= Player.cM; j++) {
				Position p = positions[i][j];
				p.draw(surface);
				if(p.tower instanceof Shield) {
					Shields.add(new float[] {p.getRelativeX()-Globals.POSITION_SIZE,p.getRelativeY()-Globals.POSITION_SIZE,p.tower.getHealth()*100+25});
				}
			}
		}
		surface.pushStyle();
		surface.noStroke();
		for(float[] f:Shields) {
			surface.fill(190,50,255,f[2]);
			surface.rect(f[0], f[1], Globals.POSITION_SIZE*2.9f, Globals.POSITION_SIZE*2.9f, 20);
			
		}
		surface.popStyle();
		
		surface.popMatrix();
	}
}
