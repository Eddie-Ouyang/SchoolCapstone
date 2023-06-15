package screen;
import java.text.DecimalFormat;

import Combat.physics.PhysicsEngine;
import Combat.player.Player;
import Combat.*;
import engine.Game;
import engine.Globals;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Screen for player to fight enemies
 * @author Jiaming Situ
 *
 */
public class Battle extends Screen {
	private Player player;
	private Map map;
	private Hangar hangar;
	private PImage background,skull,handL,handR,screen,meter,arrow,warp,target;
	private int border = 40;
	private float pRad;
	private boolean freeze, aimMode;
	private int[] keys = new int[200];
	private DecimalFormat df;
	private waveFunctionCollapse wfc;
 	
	public Battle(Game g) {
		display = g;
		df = new DecimalFormat("0.0");
		wfc = new waveFunctionCollapse();
	}
	
	@Override
	public void setup() {
		background = display.loadImage("img/bg.png");
		screen = display.loadImage("img/display.png");
		skull = display.loadImage("img/skull.png");
		meter = display.loadImage("img/meter.png");
		handL = display.loadImage("img/handL.png");
		handR = display.loadImage("img/handR.png");
		arrow = display.loadImage("img/arrow.png");
		warp = display.loadImage("img/warp.png");
		target = display.loadImage("img/target.png");
		Globals.SCREEN_H = height;
		Globals.SCREEN_W = width;
		wfc.setup(display);
	}
	
	@Override
	public void draw() {
		move();
		if(quit) {
			super.draw();
			gray = 360;
			outro();
			if(out.size() == 0) display.exit();
			return;
		}
		if(player.getHealth() == 0) {
			super.draw();
			gray = 0;
			outro();
			if(out.size() == 0) display.switchScreen(3, 0);
			return;
		}

		if(help) {
			help();
			return;
		}
		
		if(PhysicsEngine.isIntersecting(hangar, player)) {
			super.draw();
			freeze = true;
			gray = 360;
			outro();
			if(out.size() == 0) {
				display.toBuild(player.getShip());
				Globals.DIFFICULTY++;
			}
			return;
		}
		
		map.draw(display);
		wfc.draw(display,-Player.x,-Player.y);
		map.drawEnemies(display);
		if(aimMode)player.turnTowards(display.mouseX, display.mouseY);
		player.draw(display);
		player.drawModel(display,display.width-screen.width*0.925f-border, display.height/2 - screen.height*0.37f,screen.width*0.85f,screen.width*0.85f);
		map.drawProjectiles(display);
		hangar.draw(display);
		
		map.stepPhysics(player);
		drawHUD();
		Globals.CURRENT_FRAME++;
		Globals.TIME++;

		player.farm();
		intro();
		if(in.size()!=0) {
			display.push();
			display.fill(255);
			display.textSize(height/12);
			display.textAlign(PApplet.CENTER, PApplet.CENTER);
			display.text("Desceding Into " + Globals.layerText() + " Layer", width/2, height/2);
			display.pop();
		}
	}
	
	@Override
	public void mousePressed() {
		player.attack();
	}
	
	@Override
	public void keyPressed() {
		if(freeze)return;
		super.keyPressed();
		switch(display.keyCode) {
		case ' ': player.attack();
			return;
		case PApplet.TAB: aimMode = !aimMode;
			return;
		}
		keys[display.keyCode] = 1;
	}
	
	@Override
	public void keyReleased() {
		if(freeze)return;
		keys[display.keyCode] = 0;
	}
	
	private void drawHUD() {
		display.push();
		float length = display.width*(0.4f*Globals.timer()+0.02f);
		float xLeft = width/2 - length;
		float barLeft = border+0.05f*meter.width;
		float barLength = meter.width*0.91f;
		float barHeight = meter.height*0.07f;
		display.fill(255,0,0);
		display.strokeWeight(2);
		display.stroke(200,40,70);
		display.rect(xLeft, border + skull.height*0.65f, 2*length, skull.height*0.15f);
		display.image(handL, xLeft-handL.width*0.15f, border+skull.height*0.35f);
		display.image(handR, xLeft+2*length-handR.width*0.8f, border+skull.height*0.35f);
		display.image(skull, display.width/2-skull.width/2,border);
		display.noStroke();
		display.fill(0,255,0);
		display.rect(barLeft, display.height/2-meter.height*0.39f, barLength * (Globals.fuel==0?0:1f*Globals.resources[4]/Globals.fuel), barHeight);
		display.fill(255);
		display.rect(barLeft, display.height/2-meter.height*0.16f, barLength * (Globals.resources[0]>Globals.threshold[0]?1:1f*Globals.resources[0]/Globals.threshold[0]), barHeight);
		display.fill(255,190,220);
		display.rect(barLeft, display.height/2+meter.height*0.085f, barLength * (Globals.resources[1]>Globals.threshold[1]?1:1f*Globals.resources[1]/Globals.threshold[1]), barHeight);
		display.fill(190,120,60);
		display.rect(barLeft, display.height/2+meter.height*0.315f, barLength * (Globals.resources[2]>Globals.threshold[2]?1:1f*Globals.resources[2]/Globals.threshold[2]), barHeight);
		display.image(meter, border, display.height/2-meter.height/2);
		display.image(screen, display.width-screen.width-border, display.height/2-screen.height/2);
		display.pop();
		
		display.push();
		display.translate(display.width/2, display.height/2);
		display.rotate((float)Math.PI/4+hangar.getAngle());
		display.image(arrow, pRad, -pRad-arrow.height);
		display.pop();
		if(aimMode) {
			display.cursor();
			display.push();
			display.translate(display.width/2, display.height/2);
			display.rotate((float)Math.PI+Player.angle);
			display.image(target, (float)Math.hypot(display.mouseX-display.width/2, display.mouseY-display.height/2)-target.height/2f, -target.height/2f);
			display.pop();
		}
		display.pushStyle();
		display.textAlign(PApplet.CENTER,PApplet.TOP);
		display.fill(255);
		display.textFont(display.createFont("Impact", border/2));
		display.text(Globals.timer()!=0?df.format(Globals.timer()*Globals.FRAMES_PER_LIFE/60):"JUDGEMENT", display.width/2, border+skull.height);
		display.textFont(display.createFont(Screen.font, border/2));
		display.text(aimMode?"Mouse":"[Q]/[E]", display.width/2, border*1.75f+skull.height);
		display.popStyle();
		
		if(map.joeved())return;
		
		if(Globals.timer() == 0) map.joever(display.height/2);
	}
	
	@Override
	public void reset () {
		super.reset();
		player = new Player(0,0);
		pRad = player.getHitboxRadius();
		hangar = new Hangar(display.displayWidth*2, warp);
		map = new Map(background,hangar);
		freeze = false;
		quit = false;
		keys = new int[200];
		aimMode = true;
		background.resize(display.width, display.height);
		Globals.CURRENT_FRAME = 0;
		Globals.TIME = 0;
		wfc.generate();
	}
	
	private void move() {
		if(freeze)return;
		player.move(keys['D']-keys['A'], keys['S']-keys['W'], keys[PApplet.SHIFT]);
		if(!aimMode)player.turn(keys['E']-keys['Q']);
	}
}
