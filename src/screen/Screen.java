package screen;

import java.util.ArrayList;

import engine.Game;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Super class for different screens
 * @author Eddie Ouyang
 *
 */
public class Screen {
	public static String font = "UD Digi Kyokasho N-R";
	protected Game display;
	protected int gray;
	protected int countdown;
	protected ArrayList<rain> out,in;
	protected boolean quit,help;
	protected ArrayList<Particle> items;
	
	protected static float width,height;
	
	/**
	 * Can't get width and height of PApplet on laucnh
	 * @param x Width
	 * @param y Height
	 */
	public static void setSize(int x, int y) {
		width = x;
		height = y;
	}
	
	/**
	 * Called on setup, mostly to load PImages
	 */
	public void setup() {}
	
	/**
	 * Draws stuff on display
	 */
	public void draw() {
		display.colorMode(PApplet.HSB,360,1,1,1);
	}
	
	/**
	 * When the mouse is pressed
	 */
	public void mousePressed() {}
	
	/**
	 * When key is pressed
	 */
	public void keyPressed() {
		if(display.key == PApplet.ESC) {
			display.key = 0;
			quit = true;
		}
		if(display.keyCode == 'H') help = !help;
	}
	
	/**
	 * When key is released
	 */
	public void keyReleased() {}
	
	/**
	 * Set color of transition
	 * @param i Gray
	 */
	public void transition(int i) {gray = i;}
	
	protected void intro() {
		if(in.size() == 0) return;
		display.pushStyle();
		display.colorMode(PApplet.HSB,360,1,1,1);
		display.fill(gray);
		display.noStroke();
		for(rain r:in) r.draw(display, 25);
		display.popStyle();
		in.removeIf(n -> (n.height > 0));
	}
	
	protected void outro() {
		if(out.size() == 0) return;
		display.pushStyle();
		display.colorMode(PApplet.HSB,360,1,1,1);
		display.fill(gray);
		display.noStroke();
		for(rain r:out) r.draw(display, 25);
		out.removeIf(n -> (n.height > height));
		display.popStyle();
	}
	
	/**
	 * Reset fields when switching screen
	 */
	public void reset() {
		items = new ArrayList<Particle>();
		out = new ArrayList<rain>();
		in = new ArrayList<rain>();
		for(float x = 0, count = 7, dx = width/(2f*count-1), d2 = dx*1.01f; x < count; x++) {
			out.add(new rain(width/2 + x*dx, 0, d2, -dx*x));
			out.add(new rain(width/2 - x*dx, 0, d2, -dx*x));
			in.add(new rain(width/2 + x*dx, height, d2, -height-dx*x));
			in.add(new rain(width/2 - x*dx, height, d2, -height-dx*x));
		}
		out.remove(0);
		in.remove(0);
	}
	
	protected void particles() {
		for(Particle p:items)p.draw(display);
		items.removeIf(n -> (n.dead()));
	}
	
	protected void Gradient(float x, float y, float width, float length, boolean axis, int steps, float[] a, float[] b ) {
		float[] color = new float[a.length];
		float[] delta = new float[a.length];
		for(int i = 0; i < a.length; i++) {
			color[i] = a[i];
			delta[i] = (b[i] - a[i])/steps;
		}
		
		display.noStroke();
		for(float s = axis? x:y, d = (axis?width:length)/steps, end = axis?x+width:y+length, direc = Math.signum(axis?width:length);
			direc > 0?s<end:s>end; s += d) {
			switch(a.length) {
			case 1: display.fill(color[0]); break;
			case 3: display.fill(color[0],color[1],color[2]); break;
			case 4: display.fill(color[0],color[1],color[2],color[3]); 
			}
			display.rect(axis?s:x, axis?y:s, axis?d:width, axis?length:d);
			for(int i = 0; i < color.length; i++) color[i] += delta[i];
		}
	}
	
	protected void help() {
		display.push();
		display.fill(25);
		display.rect(display.width*0.2f, display.height*0.3f, display.width*0.6f, display.height*0.62f);
		display.fill(255);
		display.textAlign(PApplet.CENTER,PApplet.TOP);
		display.textSize(height/8f);
		display.text("INSTRUCTIONS", display.width/2, display.height*0.3f);
		display.textSize(height/36f);
		display.textAlign(PApplet.LEFT, PApplet.TOP);
		display.text("[H] - Access Help Screen\n"
				   + "[W] - Forward\n"
				   + "[A] - Left\n"
				   + "[S] - Backward\n"
				   + "[D] - Right\n"
				   + "[TAB] - Toggle Mouse/Key aiming\n"
				   + "[M1]/[SPACE] - Fire\n"
				   + "[Q] - Turn Right\n"
				   + "[E] - Turn Left\n"
				   + "[MOUSE] - Aim", display.width*0.25f, display.height/2);
		display.text("[1-8] - Hotkey Tower\n"
				   + "[M1] - Select Tower\n"
				   + "[MOVE] - Swap tower location\n"
				   + "[SELL] - Sell tower or tile\n"
				   + "[R]/[ROTATE] - Change tower version\n"
				   + "[D] - Deselect tower\n"
				   + "[BACK] - Quit Game\n"
				   + "BATTLE:\n"
				   + "L: Boost/Egg/Bacon/Cow Meters\n"
				   + "R: Individual health bars", display.width/2, display.height/2);
		display.pop();
	}
}

class rain {
	float x,y,width,height;
	
	public rain(float x, float y, float width, float height) {
		this.x = x-width/2;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void draw(PApplet p, int delta) {
		height += delta;
		p.rect(x, y, width, height);
	}
}

class Button {
	private String msg;
	private float x,y,w,h;
	
	public Button(float x, float y, float width, float height, String text) {
		this.x = x;
		this.y = y;
		w = width;
		h = height;
		msg = text;
	}
	
	public void move(float dx, float dy) {
		x+=dx;
		y+=dy;
	}
	
	public boolean contains(float mx, float my) {
		return (mx > x && mx < x+w) && (my > y && my < y+h);
	}
	
	public void translate(float dx, float dy) {
		x += dx;
		y += dy;
	}
	
	public void draw(PApplet p) {
//		p.Gradient(x-5, y-5, w+10, h+10, false, 10, new float[] {0,0,1,0.15f}, new float[] {0,0,0.5f,0.15f});
//		p.Gradient(x, y, w, h, false, 10, new float[] {0,0,1,0.3f}, new float[] {0,0,0.5f,0.3f});
		p.push();
		p.colorMode(PApplet.HSB,360,1,1,1);
		p.noStroke();
		p.fill(1,0.3f);
		p.rect(x-5, y-5, w+10, h+10);
		p.fill(1,0.2f);
		p.rect(x, y, w, h);
		p.fill(360);
		p.textAlign(PApplet.CENTER);
		p.textFont(p.createFont(Screen.font, Math.min(h*0.8f,w*0.8f)));
		p.text(msg, x+w/2, y+h*0.8f);
		p.pop();
	}
}

class Particle{
	private float x,y,vx,vy,angle,wheta,ax,ay;
	private int life,max;
	private PImage image;
	
	public Particle(float x0, float y0, float vx0, float vy0, float ax0, float ay0, float a, float w, int l, PImage p) {
		x = x0;
		y = y0;
		vx = vx0;
		vy = vy0;
		angle = a;
		wheta = w;
		image = p;
		ax = ax0;
		ay = ay0;
		life = l;
		max = life;
	}
	
	public void draw(PApplet p) {
		move();
		p.push();
		p.translate(x, y);
		p.tint(255,1f*life/max);
		p.rotate(angle);	
		p.image(image, -image.width/2, -image.height/2);
		p.pop();
	}
	
	public void drawEnd(PApplet p) {
		move();
		p.push();
		p.translate(x, y);
		p.tint(0,(y/p.height),1,1-(y/p.height));
		p.rotate(angle);
		p.image(image, -image.width/2, -image.height/2);
		p.pop();
	}
	
	public boolean dead() {
		return life == 0;
	}
	
	public float Y() {
		return y;
	}
	
	private void move() {
		life = --life < 0?0:life;
		angle += wheta;
		x += vx;
		y += vy;
		vy+= ax;
		vy+= ay;
	}
}

