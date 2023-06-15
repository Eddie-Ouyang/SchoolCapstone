package engine;

import java.util.ArrayList;

import Combat.player.Position;
import processing.core.PApplet;
import screen.*;

/**
 * The game
 * @author Eddie Ouyang
 *
 */
public class Game extends PApplet {
	
	private ArrayList<Screen> screens;
	private Screen current;
	
	public Game() {
		screens = new ArrayList<Screen>();
		screens.add(new Title(this));
		screens.add(new Build(this));
		screens.add(new Battle(this));
		screens.add(new End(this));
		
		current = screens.get(0);
		current.reset();
	}
	
	@Override
	public void setup() {
		for (Screen s: screens) s.setup();
		GameImage.loadImage(this);
	}

	@Override
	public void settings() {
		fullScreen();
	}

	@Override
	public void draw() {
		if (frameCount < 2) current.reset();
		
		push();
		current.draw();
		pop();
	}

	@Override
	public void mousePressed()  { current.mousePressed(); }

	@Override
	public void keyPressed()    { current.keyPressed(); }

	@Override
	public void keyReleased()   { current.keyReleased(); }
	
	/**
	 * Switches the active screen
	 * @param i Next screen
	 * @param gray Color of transition
	 */
	public void switchScreen(int i, int gray) {
		current = screens.get(i);
		current.reset();
		current.transition(gray);
	}
	
	/**
	 * Special case for switching to [Build] from [Battle]
	 * @param a
	 */
	public void toBuild(Position[][] a) {
		current = screens.get(1);
		current.reset();
		((Build)current).setMap(a);
		current.transition(360);
	}
}
