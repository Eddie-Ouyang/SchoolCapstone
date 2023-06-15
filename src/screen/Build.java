package screen;

import java.awt.Rectangle;
import java.text.DecimalFormat;

import Combat.player.Position;
import construct.Tower;
import engine.Game;
import engine.Globals;
import processing.core.PApplet;
import terra.Base;

/**
 * Screen for the player to build their farm
 * @author Eddie Ouyang
 *
 */
public class Build extends Screen{
	private Base map;
	private float x0, y0, size;
	private boolean crops;
	private Tower selectedTower;
	private TowerButton[] towers,cropMarket;
	private Button toBattle,sellCrops;
	private float[] rates;
	
	public Build(Game g) {
		map = new Base();
		display = g;
	}
	
	@Override
	public void reset() {
		super.reset();
		x0 = width/5f;
		y0 = height/18;
		size = width/2;
		map.reset((int)width,(int)height);
		float c1 = width * 0.75f;
		float c2 = width * 0.88f;
		float size = width/14;
		sellCrops = new Button(c1, y0+size*6.1f, width/5,size*0.33f, "CROPS");
		toBattle = new Button(c1, y0+size*6.6f, width/5, size*0.33f, "LAUNCH");
		towers = new TowerButton[] {new TowerButton(c1, y0, size, size, "Core"),
							   		new TowerButton(c2, y0, size, size, "Farm"),
							   		new TowerButton(c1, y0+size*1.5f, size, size, "ThrusterW", "Thruster"),
							   		new TowerButton(c2, y0+size*1.5f, size, size, "EggW", "S-eggstuple Bofors"),
							   		new TowerButton(c1, y0+size*3, size, size, "BaconW", "Baconator 375F"),
									new TowerButton(c2, y0+size*3, size, size, "MilkW", "Un-abductifier"),
							   		new TowerButton(c1, y0+size*4.5f, size, size, "Shield", "Vorpal Shield"),
									new TowerButton(c2, y0+size*4.5f, size, size, "Reactor", "Nuclear Reactor")};
		size = height/4;
		float y1 = height*3/8;
		float gap = height/10;
		cropMarket = new TowerButton[] {new TowerButton(width/2-gap-1.5f*size,y1,size,size,"Farm0"),
										new TowerButton(width/2-0.5f*size,y1,size,size,"Farm1"),
										new TowerButton(width/2+gap+0.5f*size,y1,size,size,"Farm2")};
		rates = new float[] {(float)Math.random() + 4,
							 (float)Math.random()*8 + 16,
							 (float)Math.random()*12 + 32};
		selectedTower = null;
	}
	
	/**
	 * Sets the map when switching from [Battle] to [Build]
	 * @param p
	 */
	public void setMap(Position[][] p) {
		map.setMap(p);
	}
	
	@Override
	public void draw() {
		super.draw();
		if(quit) {
			gray = 360;
			outro();
			if(out.size() == 0) display.exit();
			return;
		}
		
		if(help) {
			help();
			return;
		}
		
		map.draw(display, x0, y0, size);
		if((selectedTower != null || map.isMoving()) && display.mouseX > x0 && display.mouseX < x0+size && display.mouseY > y0 && display.mouseY <y0+size) {
			int xcoord = (int)((display.mouseX-x0)/map.size);
			int ycoord = (int)((display.mouseY-y0)/map.size);
			map.highlight(display,xcoord, ycoord, x0, y0, size);
		}
		for(TowerButton b:towers)b.draw(display);
		if(selectedTower != null) selectedTower.draw(display.mouseX, display.mouseY, size/10, display);
		
		toBattle.draw(display);
		sellCrops.draw(display);
		if(crops) {
			display.push();
			display.background(0);
			for(TowerButton b:cropMarket)b.drawCrop(display, rates);
			display.pop();
		}
		drawResources(display.width/20,y0,display.height/20);
		particles();
		intro();
	}
	
	
	private void drawResources(float x, float y, float size) {
		display.push();
		display.textAlign(PApplet.LEFT);
		display.textSize(size);
		display.fill(130,1,1);
		display.text(Globals.resources[3], x, y);
		display.fill(0,0,1);
		display.text(Globals.resources[0], x, y+=size);
		display.fill(300,0.5f,1);
		display.text(Globals.resources[1], x, y+=size);
		display.fill(30,1,0.5f);
		display.text(Globals.resources[2], x, y+=size);
		display.pop();

	}
	
	@Override
	public void mousePressed() {
		if(crops) {
			for(int i = 0; i < 3; i++) {
				if(cropMarket[i].contains(display.mouseX, display.mouseY)) {
					Globals.resources[3] += Globals.resources[i] * rates[i];
					Globals.resources[i] = 0;
					return;
				}
			}
			crops = false;
			return;
		}
		if(display.mouseX > x0 && display.mouseX < x0+size && display.mouseY > y0 && display.mouseY <y0+size) {
			int xcoord = (int)((display.mouseX-x0)/map.size);
			int ycoord = (int)((display.mouseY-y0)/map.size);
			if(display.mouseButton == PApplet.RIGHT) {
				selectedTower = map.getTower(xcoord, ycoord);
				return;
			}
			if(selectedTower == null) 
				map.click(xcoord, ycoord);
			else {
				boolean success = map.buy(xcoord, ycoord, selectedTower);
				if(success) {
					map.setCurrent(xcoord, ycoord);
					selectedTower = selectedTower.copySelf();
				} else {
					selectedTower = null;
				}
			}
		} else {	
			for(TowerButton b:towers) {
				if(b.contains(display.mouseX, display.mouseY)) {
					selectedTower = b.getTower();
					break;
				}
			}
		}
		if(toBattle.contains(display.mouseX, display.mouseY)) display.switchScreen(2, 0);
		if(sellCrops.contains(display.mouseX, display.mouseY)) crops = true;
		map.tryButton(display.mouseX, display.mouseY);
	}
	
	@Override
	public void keyPressed() {
		super.keyPressed();	
		switch(display.keyCode) {
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
			selectedTower = towers[display.keyCode-'1'].getTower();
			break;
		case 'R': if(selectedTower!=null)selectedTower.rotate();
			else map.tryRotate();
			break;
		case 'S': map.trySell();
			break;
		case 'D': map.clearCurrent();
			selectedTower = null;
			break;
		}
	}
}



class TowerButton extends Rectangle {
	private String tower,label;
	private DecimalFormat df = new DecimalFormat("$#.##");
	
	public TowerButton(float x, float y, float width, float height, String tower) {
		super((int)x,(int)y,(int)width,(int)height);
		this.tower = tower;
		this.label = tower;
	}
	
	public TowerButton(float x, float y, float width, float height, String tower, String label) {
		super((int)x,(int)y,(int)width,(int)height);
		this.tower = tower;
		this.label = label;
	}
	
	public void draw(PApplet p) {
		p.push();
		p.fill(188);
		p.rect(x, y, width, height);
		p.image(GameImage.get(tower), x+width/10, y+height/10, width*0.8f, height*0.8f);
		p.textFont(p.createFont(Screen.font, width/5f));
		p.fill(360);
		p.textAlign(PApplet.CENTER, PApplet.TOP);
		p.text(label, x+width/2, y+height);
		p.text(Tower.price(tower), x+width/2, y);
		p.pop();
	}
	
	public void drawCrop(PApplet p, float[] r) {
		int i = tower.charAt(tower.length()-1)-'0';
		p.push();
		p.fill(188);
		p.rect(x, y, width, height);
		p.image(GameImage.get(tower), x+width/10, y+height/10, width*0.8f, height*0.8f);
		p.textFont(p.createFont(Screen.font, width/10f));
		p.fill(360);
		p.textAlign(PApplet.CENTER, PApplet.TOP);
		p.text("Unit Price: " +   df.format(r[i]), x+width/2, y);
		p.text("Total: " + (int)(r[i]*Globals.resources[i]), x+width/2, y+height);
		p.pop();
	}
	
	public Tower getTower() {
		return Tower.copyTower(tower);
	}
}
