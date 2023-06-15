package screen;

import engine.Game;
import engine.Globals;
import processing.core.*;
/**
 * End screen
 * @author Eddie Ouyang, Pratham Hebbar
 *
 */
public class End extends Screen {
	private Button[] buttons;
	private PImage joever;
	private boolean menu;
	
	public End(Game g) {
		display = g;
	}
	
	@Override
	public void setup() {
		joever = display.loadImage("img/joever.png");
	}
	
	@Override
	public void reset() {
		super.reset();
		buttons = new Button[] {new Button(display.width*0.4f, display.height/2, display.width*0.2f,display.height/15, "MENU"),
								new Button(display.width*0.4f, display.height*0.6f, display.width*0.2f,display.height/15, "QUIT")};
		joever.resize(display.width, display.width*joever.height/joever.width);
		menu = false;
		quit = false;
	}
	
	@Override
	public void draw() {
		super.draw();
		
		if(quit || menu) {
			gray = 0;
			outro();
			if(out.size() == 0) {
				if(quit)display.exit();
				else display.switchScreen(0, 0);
			}
			return;
		}

		display.background(0);
		display.noStroke();
		Gradient(0, display.height/4, display.width, display.height*0.75f, false, 100, new float[] {360,1,0,1}, new float[] {360,1,0.5f,1});

		for(Particle s:items) s.drawEnd(display);
		items.removeIf(n -> (n.Y() > display.height));
		spawn();
		for(Button b:buttons) b.draw(display);
		display.image(joever, 0, height/10);
		display.fill(0,1,1);
		display.textAlign(PApplet.CENTER);
		display.text("Level: "+Globals.DIFFICULTY +"    Kills: " + Globals.KILLS, width/2, height/10+joever.height);
		intro();
	}
	
	@Override
	public void mousePressed() {
		for(int i = 0; i < buttons.length; i++) {
			if(buttons[i].contains(display.mouseX, display.mouseY)) {
				switch(i) {
				case 0: menu = true;
					break;
				case 1: quit = true;
					break;
				}
			}
		}
	}
	
	private void spawn() {
		if(items.size() > 200) return;
		for(int i = 0, max = (int)(Math.random()*5+3); i < max; i++) {
			boolean side = Math.random()<0.5;
			items.add(new Particle(((float)Math.random()*0.6f+0.2f)*display.width,
									(float)Math.random()*height/4,
									(float)Math.random()*10 *(side?1:-1),
									(float)Math.random()*-10,
									0,
									0.5f,
									(float)(Math.random()*Math.PI*2), 
									(float)(Math.random()*Math.PI/6),
									1,
									GameImage.get("Bullet0")));
		}
	}
}

