package engine;

import processing.core.*;
import screen.Screen;

/**
 * 
 * The Main class initiates the game engine
 * @author prathamhebbar
 *
 */
public class Main {
	public static void main(String args[]) {
		Game game = new Game();
		PApplet.runSketch(new String[]{""}, game);
		Screen.setSize(game.width, game.height);
		game.windowResizable(true);
		game.windowTitle("Reborn as a Farmer");
	}
}
