package screen;

import engine.Game;
import engine.Globals;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Title screen
 * @author Eddie Ouyang
 *
 */
public class Title extends Screen{
	private boolean idle;
	private Button[] buttons;
	private int shift;
	private PImage text;
	private waveFunctionCollapse wfc;
	private int dy;
	
	public Title(Game g) {
		display = g;
		wfc = new waveFunctionCollapse();
		buttons = new Button[3];
	}
	
	@Override
	public void setup() {
		text = display.loadImage("img/title.png");
		wfc.setup(display);
	}

	@Override
	public void draw() {
		super.draw();
		if(quit) {
			gray = 0;
			outro();
			if(out.size() == 0) display.exit();
			return;
		}
		
		if(help) {
			help();
			return;
		}
		
		if(shift<355 && !quit) {
//			display.fill(359,1,0.1f);
//			display.rect(0, 0, width, height/2);
//			Gradient(0,height/2,width,height,false,25,new float[] {359,1,0.1f}, new float[] {200,.5f,.5f});
			wfc.draw(display, 0, dy);
			dy+=5;
		}
		
		float dy = idle?(float)(15 *Math.sin(0.05*display.frameCount)):-shift;
		if(!idle) shift+= shift<360?15:shift/12; 
		if(shift<360) {
			display.image(text, display.width/2-text.width/2, display.height/10+dy);
		} 
		if(shift > 150) {
			display.fill(0,0,1,0.1f);
			display.noStroke();
			display.circle(width/2, height/2, 2*(shift-150));
		}
		if(shift < 300)for(Button b:buttons) {
			b.draw(display);
			if(!idle) b.translate(0, (int)(height/48f));
		}
		if(shift > 3000) display.switchScreen(1, 360);
		if(idle)particles();
		intro();
	}
	
	@Override
	public void mousePressed() {
		if(help) help = false;
		for(int i = 0; i < buttons.length; i++) {
			if(buttons[i].contains(display.mouseX, display.mouseY)) {
				switch(i) {
				case 0: idle = false;
					shift = -(int)(15 *Math.sin(0.05*display.frameCount) + 0.5);
					Globals.newGame();
					break;
				case 1: help = true;
					break;
				case 2: quit = true;
				}
			}
		}
	}

	@Override
	public void reset() {
		super.reset();
		dy = 0;
		wfc.generate();
		if(text!=null)text.resize((int)(display.width*0.8f), (int)(display.width*0.8f*text.height/text.width));
		idle = true;
		shift = 0;
		buttons[0] = new Button(width*5f/12, height*0.55f,width/6,height/20,"START");
		buttons[1] = new Button(width*5f/12, height*0.65f,width/6,height/20,"HELP");
		buttons[2] = new Button(width*5f/12, height*0.75f,width/6,height/20,"QUIT");
	}
}




